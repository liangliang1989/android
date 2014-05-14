package com.KRL.Blood;

import java.util.Timer;
import java.util.TimerTask;

import com.KRL.Blood.BloodBluetoothThread.OnBluetoothConnectFailed;
import com.KRL.Blood.BloodBluetoothThread.OnBluetoothConnectSucess;
import com.KRL.Blood.BloodBluetoothThread.OnReadbluetoothData;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

public class BloodBluetoothController implements OnReadbluetoothData,
		OnBluetoothConnectSucess, OnBluetoothConnectFailed {
	private BloodBluetoothThread mBluetoothRunnable = null;
	private boolean devicebusy = false;
	private byte[] writedata = new byte[100];
	private byte batteryvalue = 0;
	private Timer mReadBatteryTimer = new Timer();
	private ReadBatteryValueTask mReadTask;
	private Handler mHandler;
	private static BloodBluetoothController instance;
	public long lastnoticeutc = System.currentTimeMillis();
	public BloodData mBloodData = new BloodData();
	private boolean connected = false;

	public static BloodBluetoothController getInstance() {
		if (instance == null) {
			synchronized (BloodBluetoothController.class) {
				if (instance == null) {
					instance = new BloodBluetoothController();
				}
			}
		}
		return instance;
	}

	public String getDeviceName() {
		String name = mBluetoothRunnable.getDevcie().getName();
		if (name == null)
			name = "no name";
		return name;
	}

	public void setDevice(BluetoothDevice device) {
		mBluetoothRunnable.setBluetoothMacAddr(device);
	}

	public BloodBluetoothController() {
		// mBluetoothRunnable.setOnReadbluetoothData(this);
	}

	/***
	 * 获取电量
	 */
	public short getBatteryValue() {
		return (short) ((10 - batteryvalue) * 10);
	}

	/***
	 * 开始连接线程
	 */
	public void startConnectThread() {
//		if (null != mBluetoothRunnable)
//			mBluetoothRunnable.stopThread();
		mBluetoothRunnable = new BloodBluetoothThread();
		mBluetoothRunnable.setOnConnectSucess(this);
		mBluetoothRunnable.setOnConnectFailed(this);
		mBluetoothRunnable.setOnReadbluetoothData(this);
		mBluetoothRunnable.start();
		
	}

	/***
	 * 开始连接血压终端 //
	 */
	// public synchronized void startConnect() {
	// mBluetoothRunnable.startConnectDevice();
	// }

	/***
	 * 停止连接线程
	 */
	public void stopConnectThread() {
		mBluetoothRunnable.stopThread();
	}

	/***
	 * 停止连接血压终端
	 */
	public void stopConnect() {
		mBluetoothRunnable.stopConnectDevice();
		if (mReadTask != null) {
			mReadTask.cancel();
		}
		mReadBatteryTimer.cancel();
	}

	/***
	 * 控制终端LED 0x00:关 0x01:开
	 */
	public void deviceLedControl(boolean on) {
		byte[] tempdata = { (byte) 0xFF, (byte) 0xFF, 0x12, 0x01, 0x00 };
		if (on) {
			tempdata[4] = 0x01;
		} else {
			tempdata[4] = 0x00;
		}
		mBluetoothRunnable.writeData(tempdata);
	}
	
	public void stopCurrentConnect()
	{
		mBluetoothRunnable.stopCurrentConnect();
	}

	/***
	 * 开始单次测量
	 */
	public void startOnceTest() {
		sendStruct(BloodCommandID.Phone2Device.START_SINGLE_TEST, 0x00, null);
	}

	/***
	 * 发送停止单次测量指令
	 */
	public void stopOnceTest() {

		try {
			sendStruct(BloodCommandID.Phone2Device.STOP_TEST, 0x00, null);
			Thread.sleep(100);
			sendStruct(BloodCommandID.Phone2Device.STOP_TEST, 0x00, null);
			Thread.sleep(100);
			sendStruct(BloodCommandID.Phone2Device.STOP_TEST, 0x00, null);
			Thread.sleep(100);
			sendStruct(BloodCommandID.Phone2Device.STOP_TEST, 0x00, null);
			Thread.sleep(100);
			sendStruct(BloodCommandID.Phone2Device.STOP_TEST, 0x00, null);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// new Thread() {
		// public void run() {
		// int i = 5;
		// while (i-- > 0) {
		// sendStruct(BloodCommandID.Phone2Device.STOP_TEST, 0x00,
		// null);
		// try {
		// Thread.sleep(200);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		// }.start();

	}

	/***
	 * 擦除数据存储区
	 */
	public void cleaHistoryData() {
		sendStruct(BloodCommandID.Phone2Device.CLEAR_TERMDATA, 0x00, null);
	}

	/***
	 * 发送指令
	 * 
	 * @param struct
	 *            命令码
	 * @param length
	 *            长度
	 * @param data
	 *            发送的数据
	 */
	private synchronized void sendStruct(final byte struct, final int length,
			byte[] data) {
		{
			writedata[0] = (byte) 0xFA;
			writedata[1] = (byte) 0xE5;
			writedata[2] = struct;
			writedata[3] = (byte) length;
			if (data != null) {
				System.arraycopy(data, 0, writedata, 4, data.length);
			}
			mBluetoothRunnable.writeData(writedata);
			// new Thread(mWriteRunnable).start();
		}
	}

	/***
	 * 写数据线程
	 * 
	 * @author krl
	 * 
	 */
	// class WriteDataRunnable implements Runnable {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// // mBluetoothRunnable.writeData(writedata);
	// }
	// }

	/***
	 * 获取实时压力数据
	 * 
	 * @param data
	 *            用于计算的数据包数据
	 * @return 实时压力值
	 */
	private void getRealPressureData(byte[] data) {
		int realpressure = AppTools.byte2Int(data[0], data[1]);
		realpressure = realpressure * 300;
		realpressure = (int) (realpressure / 4095.0);
		mBloodData.setBloodPressure(realpressure);
		noticeDataChange(true);

	}

	public void setHandler(Handler handler) {
		synchronized (BloodBluetoothController.class) {
			this.mHandler = handler;
		}

	}

	public void removeHandler() {
		synchronized (BloodBluetoothController.class) {
			mHandler = null;
		}
	}

	private void noticeDataChange(boolean space) {
		if (space) {
			if (System.currentTimeMillis() - lastnoticeutc < 500) {
				return;
			}
		}
		lastnoticeutc = System.currentTimeMillis();
		synchronized (BloodBluetoothController.class) {
			if (mHandler != null)
				mHandler.sendEmptyMessage(BloodMessage.MSG_RECEIVE_BLOODDATA);
		}
	}

	/***
	 * 获取血压数据
	 * 
	 * @param data
	 *            用于计算的数据
	 * @return 收缩压|平均压|舒张压|脉搏
	 */
	private void getBloodPressureData(byte[] data) {
		mBloodData.setBloodData(AppTools.byte2Int(data[0], data[1]),
				AppTools.byte2Int(data[2], data[3]),
				AppTools.byte2Int(data[6], data[7]),
				AppTools.byte2Int(data[4], data[5]));
		noticeDataChange(false);
	}

	@Override
	public void setOnBluetoothConnectSucess() {
		// TODO Auto-generated method stub
		startReadBatteryValueTask();
		connected = true;
		if (mHandler != null)
			mHandler.sendEmptyMessage(BloodMessage.MSG_CONNECT_SUCESS);
	}

	/***
	 * 开始读取电量定时器
	 */
	private void startReadBatteryValueTask() {
		mReadTask = new ReadBatteryValueTask();
		mReadBatteryTimer.schedule(mReadTask, 1000, 30000);
	}

	public BloodData getBloodData() {
		return mBloodData;
	}

	/***
	 * 获取电量
	 */
	public void readBattery() {
		sendStruct(BloodCommandID.Phone2Device.GET_BATTERYVALUE, 0x00, null);
	}

	class ReadBatteryValueTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// LOG.D(TAG, "准备读取电量信息" + btbusy);
			if (!devicebusy) {
				// LOG.D(TAG, "读取电量信息");
				readBattery();
			}
		}

	}

	@Override
	public void onReceiveData(byte[] buff, int count, int command) {
		// TODO Auto-generated method stub
		switch (command) {
		case BloodCommandID.Device2Phone.POSTBACKPRESSURE: {
			// LOG.D(TAG, "获取袖带压力数据");
			devicebusy = true;
			getRealPressureData(buff);
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKBLOODPRESSURERESULT: {/* 血压数据 */
			// LOG.D(TAG, "获取血压数据");
			getBloodPressureData(buff);
			devicebusy = false;
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKTERMNUBER: {/* 返回终端号 */
			// currentternumber = AppTools.byte2String(buff, 0, count);
			// LOG.D(TAG, "获取终端编号" + currentternumber);
			// currentternumber = AppTools.byte2String(buff, 0, count);
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKLEARTERMDATARESULT: {/* 擦除终端历史数据结果 */
			// LOG.D(TAG, "获取擦除终端历史数据状态");
			// getClearStatus(buff, count);
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKREADHISDATA: {/* 读取的历史数据 */
			// LOG.D(TAG, "读取历史数据");
			// getHistoryData(buff, count);
		}
			break;
		case BloodCommandID.Device2Phone.OUTOFMEMORY: {/* 空间不够 */
			// LOG.D(TAG, "终端存储空间已满");
		}
			break;
		case BloodCommandID.Device2Phone.RECEIVESTRUCESUCESS: {/* 收到下发指令 */
			// LOG.D(TAG, "接收到下发指令");
			if (count != 0) {
				if (mHandler != null)
					mHandler.sendEmptyMessage(BloodMessage.MSG_RECEIVE_BATTERY);
				batteryvalue = buff[0];
			}
			// receivepostbackinfo = true;
			// if (count != 0) {
			// LOG.D(TAG, "接收到电量信息");
			// LOG.D(TAG, "电量" + buff[0]);
			// BloodBluetoothInfo.device_battery = buff[0];
			// // BloodBluetoothInfo.device_battery = buff[0];
			// }
		}
			break;
		}
	}

	@Override
	public void setOnBluetoothConnectFailed() {
		// TODO Auto-generated method stub
		connected = false;
		if (mHandler != null)
			mHandler.sendEmptyMessage(BloodMessage.MSG_CONNECT_FAILED);
	}

	public boolean isConnected() {
		return connected;
	}

}
