package com.KRL.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothConnectThread extends Thread {
	private static final String TAG = "BluetoothConnectThread";
	private static final int mBufferMax = 540;
	private BluetoothSocket mSocket = null;
	private final BluetoothDevice mmDevice;
	private InputStream mInputStream = null;
	private OutputStream mOutputStream = null;
	private byte[] mReadBuffer = new byte[mBufferMax];
	private Handler mHandler = null;
	private BluetoothConnectionState mState = BluetoothConnectionState.DISCONNECTED;
	private int mDataType = 0;
	private BluetoothDataParser mParser = null;
	private BluetoothAdapter mAdapter = null;
	private String mAddress = "";
	private boolean canceled = false;
	private Object releaseLock = new Object();

	public enum BluetoothConnectionState {
		DISCONNECTED, CONNECTED, CONNECTING, IOEXCEPTION, INTERRUPTEDEXCEPTION;
	}

	//
	public BluetoothConnectThread(BluetoothDevice device, Handler handler,
			int dataType, BluetoothDataParser parser, BluetoothAdapter adapter) {
		mmDevice = device;
		mAddress = device.getAddress();
		mHandler = handler;
		mDataType = dataType;
		mParser = parser;
		mAdapter = adapter;
	}

	public boolean isConnected() {
		return mState == BluetoothConnectionState.CONNECTED ? true : false;
	}

	public int getConnectionState() {
		return mState.ordinal();
	}

	public boolean connect() {
		try {
			onConnectonStateChanged(BluetoothConnectionState.CONNECTING);
			// if (null != mAdapter)
			// mAdapter.cancelDiscovery();
			if (null == mSocket)
				mSocket = mmDevice
						.createRfcommSocketToServiceRecord(Bluetooth.MY_UUID);
			if (!mSocket.isConnected())
				mSocket.connect();
			mInputStream = mSocket.getInputStream();
			mOutputStream = mSocket.getOutputStream();
			onConnectonStateChanged(BluetoothConnectionState.CONNECTED);
			// if (mDataType == 0) {
			// this.SendECGCommand();
			// } else if (mDataType == 1) {
			// this.SendVCGCommand();
			// }

			return SendECGCommand();
		} catch (IOException e) {
			e.printStackTrace();
			onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
			release();
			return false;
		}
	}

	@Override
	public void run() {
		Log.e(TAG, "start");
		if (!connect())
			return;
		Log.e(TAG, "connected ");
		int bytes = 0, offset = 0;
		while (!canceled && isConnected()) {
			try {
				bytes = mInputStream.read(mReadBuffer, offset, mBufferMax
						- offset);
				if (bytes <= 0) {
					Thread.sleep(5);
				} else {
					offset = mParser.push(mReadBuffer, offset, bytes,
							mBufferMax);
					onRecivedData();
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
				release();
				break;
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				onConnectonStateChanged(BluetoothConnectionState.INTERRUPTEDEXCEPTION);
				release();
				break;
			}
		}
		release();
	}

	public void cancel() {
		Log.e(TAG, "canceled");
		canceled = true;
		mState = BluetoothConnectionState.DISCONNECTED;
		onConnectonStateChanged(BluetoothConnectionState.DISCONNECTED);
		release();
	}

	public void SendBatteryCommand() {
		try {
			if (mOutputStream != null)
				mOutputStream.write(Bluetooth.BATTERY, 0,
						Bluetooth.BATTERY.length);
		} catch (IOException e) {
			e.printStackTrace();
			onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
			release();
		}
	}

	public boolean SendECGCommand() {
		boolean result = false;
		try {
			if (mOutputStream != null)
				mOutputStream.write(Bluetooth.ECG, 0, Bluetooth.ECG.length);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
			release();
		}
		return result;
	}

	public void SendVCGCommand() {
		try {
			if (mOutputStream != null)
				mOutputStream.write(Bluetooth.VCG, 0, Bluetooth.VCG.length);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
			release();
		}
	}

	public boolean SendStop() {
		boolean result = false;
		try {
			if (mOutputStream != null)
				mOutputStream.write(Bluetooth.STOP, 0, Bluetooth.STOP.length);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
			release();
		}
		return result;
	}

	public void write(byte[] command) {
		try {
			if (mOutputStream != null)
				mOutputStream.write(command, 0, command.length);
		} catch (IOException e) {
			e.printStackTrace();
			onConnectonStateChanged(BluetoothConnectionState.IOEXCEPTION);
			release();
		}
	}

	private void release() {
		synchronized (releaseLock) {
			Log.e(TAG, "release");
			// this.write(KRLECG.STOP);
			if (null != mInputStream) {
				try {
					mInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != mOutputStream) {
				try {
					mOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != mSocket) {
				try {
					mSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			mInputStream = null;
			mOutputStream = null;
			mSocket = null;
		}
	}

	private void onConnectonStateChanged(BluetoothConnectionState state) {
		mState = state;
		if (null != mHandler) {
			Message msg = mHandler.obtainMessage(Bluetooth.MESSAGE_CONNECTED);
			Bundle data = new Bundle();
			data.putString(Bluetooth.MESSAGE_KEY_BT_ADDRESS, mAddress);
			data.putInt(Bluetooth.MESSAGE_KEY_BT_STATE, state.ordinal());
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	}

	private void onRecivedData() {
		if (null != mHandler) {
			Message msg = mHandler.obtainMessage(Bluetooth.MESSAGE_DATA);
			mHandler.sendMessage(msg);
		}
	}

	// /**
	// *
	// * ���������������������������
	// *
	// */
	// public static class KrlEcgPersistWriter extends Thread {
	// private BluetoothConnectThread bluetoothService;
	// private static final String TAG = "PC300.PersistWriter";
	// private byte[] command;// ���ݵ�����
	// private long sleepTime;// ���ʱ��
	//
	// public KrlEcgPersistWriter(BluetoothConnectThread bluetoothService,
	// byte[] command, long sleepTime) {
	// this.bluetoothService = bluetoothService;
	// this.command = command;
	// this.sleepTime = sleepTime;
	// }
	//
	// @Override
	// public void run() {
	// while (true) {
	// if (bluetoothService.isConnected()) {
	// Log.i(TAG, "send persist command");
	// bluetoothService.write(command);// ��������
	// }
	// try {
	// TimeUnit.MILLISECONDS.sleep(sleepTime);// ÿ��һ��ʱ�䷢��һ�ε�����ѯ����
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
}
