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
	 * ��ȡ����
	 */
	public short getBatteryValue() {
		return (short) ((10 - batteryvalue) * 10);
	}

	/***
	 * ��ʼ�����߳�
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
	 * ��ʼ����Ѫѹ�ն� //
	 */
	// public synchronized void startConnect() {
	// mBluetoothRunnable.startConnectDevice();
	// }

	/***
	 * ֹͣ�����߳�
	 */
	public void stopConnectThread() {
		mBluetoothRunnable.stopThread();
	}

	/***
	 * ֹͣ����Ѫѹ�ն�
	 */
	public void stopConnect() {
		mBluetoothRunnable.stopConnectDevice();
		if (mReadTask != null) {
			mReadTask.cancel();
		}
		mReadBatteryTimer.cancel();
	}

	/***
	 * �����ն�LED 0x00:�� 0x01:��
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
	 * ��ʼ���β���
	 */
	public void startOnceTest() {
		sendStruct(BloodCommandID.Phone2Device.START_SINGLE_TEST, 0x00, null);
	}

	/***
	 * ����ֹͣ���β���ָ��
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
	 * �������ݴ洢��
	 */
	public void cleaHistoryData() {
		sendStruct(BloodCommandID.Phone2Device.CLEAR_TERMDATA, 0x00, null);
	}

	/***
	 * ����ָ��
	 * 
	 * @param struct
	 *            ������
	 * @param length
	 *            ����
	 * @param data
	 *            ���͵�����
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
	 * д�����߳�
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
	 * ��ȡʵʱѹ������
	 * 
	 * @param data
	 *            ���ڼ�������ݰ�����
	 * @return ʵʱѹ��ֵ
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
	 * ��ȡѪѹ����
	 * 
	 * @param data
	 *            ���ڼ��������
	 * @return ����ѹ|ƽ��ѹ|����ѹ|����
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
	 * ��ʼ��ȡ������ʱ��
	 */
	private void startReadBatteryValueTask() {
		mReadTask = new ReadBatteryValueTask();
		mReadBatteryTimer.schedule(mReadTask, 1000, 30000);
	}

	public BloodData getBloodData() {
		return mBloodData;
	}

	/***
	 * ��ȡ����
	 */
	public void readBattery() {
		sendStruct(BloodCommandID.Phone2Device.GET_BATTERYVALUE, 0x00, null);
	}

	class ReadBatteryValueTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// LOG.D(TAG, "׼����ȡ������Ϣ" + btbusy);
			if (!devicebusy) {
				// LOG.D(TAG, "��ȡ������Ϣ");
				readBattery();
			}
		}

	}

	@Override
	public void onReceiveData(byte[] buff, int count, int command) {
		// TODO Auto-generated method stub
		switch (command) {
		case BloodCommandID.Device2Phone.POSTBACKPRESSURE: {
			// LOG.D(TAG, "��ȡ���ѹ������");
			devicebusy = true;
			getRealPressureData(buff);
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKBLOODPRESSURERESULT: {/* Ѫѹ���� */
			// LOG.D(TAG, "��ȡѪѹ����");
			getBloodPressureData(buff);
			devicebusy = false;
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKTERMNUBER: {/* �����ն˺� */
			// currentternumber = AppTools.byte2String(buff, 0, count);
			// LOG.D(TAG, "��ȡ�ն˱��" + currentternumber);
			// currentternumber = AppTools.byte2String(buff, 0, count);
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKLEARTERMDATARESULT: {/* �����ն���ʷ���ݽ�� */
			// LOG.D(TAG, "��ȡ�����ն���ʷ����״̬");
			// getClearStatus(buff, count);
		}
			break;
		case BloodCommandID.Device2Phone.POSTBACKREADHISDATA: {/* ��ȡ����ʷ���� */
			// LOG.D(TAG, "��ȡ��ʷ����");
			// getHistoryData(buff, count);
		}
			break;
		case BloodCommandID.Device2Phone.OUTOFMEMORY: {/* �ռ䲻�� */
			// LOG.D(TAG, "�ն˴洢�ռ�����");
		}
			break;
		case BloodCommandID.Device2Phone.RECEIVESTRUCESUCESS: {/* �յ��·�ָ�� */
			// LOG.D(TAG, "���յ��·�ָ��");
			if (count != 0) {
				if (mHandler != null)
					mHandler.sendEmptyMessage(BloodMessage.MSG_RECEIVE_BATTERY);
				batteryvalue = buff[0];
			}
			// receivepostbackinfo = true;
			// if (count != 0) {
			// LOG.D(TAG, "���յ�������Ϣ");
			// LOG.D(TAG, "����" + buff[0]);
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
