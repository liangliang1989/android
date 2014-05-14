package com.KRL.Blood;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BloodBluetoothThread extends Thread {
	private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
	private String TAG = this.getClass().getName();
	// private String deviceaddr = "";
	private BluetoothDevice mDevice = null;
	private final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private BluetoothSocket mSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	// private boolean thrun = false;
	private OnReadbluetoothData mOnReadbluetoothData;
	private OnBluetoothConnectSucess mOnBluetoothConnectSucess;
	private OnBluetoothConnectFailed mOnBluetoothConnectFailed;
	private boolean connectdevice = false;
	byte[] readhead = new byte[2];
	int readlength = 0;
	byte packlength = 0;
	int packcommand = 0;
	byte[] packdata = new byte[100];
	private boolean deviceconnected = false;

	public void setBluetoothMacAddr(BluetoothDevice device) {
		mDevice = device;
	}

	public BluetoothDevice getDevcie() {
		return mDevice;
	}

	/***
	 * 连接蓝牙设备
	 */
	private boolean connectDevice() {
		if (mDevice == null)
			return false;
		if (mAdapter.isEnabled()) {
			try {
				Log.d(TAG,
						"connect bluetooth blooddevice..."
								+ mDevice.getAddress());
				mAdapter.cancelDiscovery();
				mSocket = null;
				mSocket = mDevice.createRfcommSocketToServiceRecord(UUID
						.fromString(SPP_UUID));
				mSocket = getSocket(mDevice);
				if (connectBluetooth())
					return true;
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			mAdapter.enable();
			return false;
		}
	}

	private BluetoothSocket getSocket(BluetoothDevice device) {
		Method m;
		BluetoothSocket socket = null;
		try {
			m = device.getClass().getMethod("createRfcommSocket",
					new Class[] { int.class });
			try {
				socket = (BluetoothSocket) m.invoke(device, 1);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;
	}

	/** 连接蓝牙 */
	private boolean connectBluetooth() {
		try {
			deviceconnected = false;
			mSocket.connect();
			mInputStream = mSocket.getInputStream();
			mOutputStream = mSocket.getOutputStream();
			deviceconnected = true;
			if (mOnBluetoothConnectSucess != null)
				mOnBluetoothConnectSucess.setOnBluetoothConnectSucess();
			Log.d(TAG, mDevice.getAddress() + " connect sucess...");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (mSocket != null) {
				try {
					mSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return false;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// thrun = true;
		// while (thrun) {
		connectdevice = true;
		while (connectdevice) {
			if (connectDevice()) {
				readBluetoothData();
				stopConnecttion();
			} else {
				if (mDevice != null) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/***
	 * 关闭连接
	 */
	private synchronized void stopConnecttion() {
		try {
			if (mSocket != null) {
				if (mInputStream != null)
					mInputStream.close();
				if (mOutputStream != null)
					mOutputStream.close();
				mSocket.close();
				mSocket = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			readlength = 0;
			if (mOnBluetoothConnectFailed != null)
				mOnBluetoothConnectFailed.setOnBluetoothConnectFailed();
			Log.d(TAG, "蓝牙断开...");
		}
	}

	/***
	 * 读取蓝牙数据
	 */
	private void readBluetoothData() {
		while ((readlength != -1) && connectdevice) {
			if (readHead()) {
				if (readCommand() != -1) {
					if (readLength() != -1) {
						readData();
					}
				}
			}
		}
	}

	private boolean readHead() {
		try {
			readlength = mInputStream.read();
			if (readlength == 0xFA) {
				readlength = mInputStream.read();
				if (readlength == 0xE5) {
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			readlength = -1;
		}
		return false;

	}

	private byte readLength() {
		try {
			packlength = (byte) mInputStream.read();
			readlength = packlength;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			readlength = -1;
		}
		return packlength;
	}

	private int readCommand() {
		try {
			packcommand = mInputStream.read();
			readlength = packcommand;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packcommand;
	}

	private void readData() {
		byte saveindex = 0;
		byte left = packlength;
		try {
			if (left != 0) {
				while (connectdevice
						&& (readlength = mInputStream.read(packdata, saveindex,
								left)) != -1) {
					saveindex += readlength;
					left = (byte) (packlength - saveindex);
					if (left == 0)
						break;
				}
			}
			if (mOnReadbluetoothData != null)
				mOnReadbluetoothData.onReceiveData(packdata, packlength,
						packcommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			readlength = -1;
		}

	}

	// public void startConnectDevice() {
	// connectdevice = true;
	// }

	public void stopConnectDevice() {
		connectdevice = false;
		// stopConnecttion();
	}

	public void stopCurrentConnect() {
		stopConnecttion();
	}

	/***
	 * 停止线程
	 */
	public void stopThread() {
		connectdevice = false;
		// thrun = false;
		stopConnecttion();
	}

	/***
	 * 发送数据
	 * 
	 * @param data
	 *            待发送数据
	 * @return true 发送成功 false 发送失败
	 */
	public boolean writeData(byte[] data) {
		if (mSocket != null)
			if (deviceconnected) {
				try {
					synchronized (mSocket) {
						if (connectdevice)
							mOutputStream.write(data);
					}
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		return false;
	}

	public void setOnReadbluetoothData(OnReadbluetoothData onReadData) {
		mOnReadbluetoothData = onReadData;
	}

	public interface OnReadbluetoothData {
		public void onReceiveData(byte[] buff, int count, int command);
	}

	public void setOnConnectSucess(
			OnBluetoothConnectSucess onBluetoothConnectSucess) {
		mOnBluetoothConnectSucess = onBluetoothConnectSucess;
	}

	public interface OnBluetoothConnectSucess {
		public void setOnBluetoothConnectSucess();
	}

	public void setOnConnectFailed(
			OnBluetoothConnectFailed onBluetoothConnectFailed) {
		mOnBluetoothConnectFailed = onBluetoothConnectFailed;
	}

	public interface OnBluetoothConnectFailed {
		public void setOnBluetoothConnectFailed();
	}
}
