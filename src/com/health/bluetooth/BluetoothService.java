package com.health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.health.device.PC300;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * �����ṩ�������ӷ���
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-10-16 ����9:07:52
 */
public class BluetoothService {

	private final BluetoothAdapter btAdapter;// ����������
	private final Handler handler;// ������Ϣ�������handler
	private int state;// ������������״̬
	private ConnectThread connectThread = null;// �����߳�
	private ConnectedThread connectedThread = null;// ����ͨ�������߳�

	private static final String UUIDS = "00001101-0000-1000-8000-00805F9B34FB";
	private static final UUID MY_UUID = UUID.fromString(UUIDS);// �����������ӵ�UUID

	// ������Ϣ��״̬
	public static final int STATE_NONE = 0x401180; // ����״̬
	public static final int STATE_CONNECTING = 0x401181; // ��������
	public static final int STATE_CONNECTED = 0x401182; // �Ѿ�����������

	public static final int MESSAGE_DEVICE = 0x401183;// ���������豸���ƺ͵�ַ
	public static final int MESSAGE_STATE_CHANGE = 0x401184;// ������״̬�ı��ź�
	public static final int MESSAGE_READ = 0x401185;// ���������ź�
	public static final int MESSAGE_WRITE = 0x401186;// д�������ź�
	public static final int MESSAGE_TOAST = 0x401187;// ��ʾ���û�����ʾ���ź�

	public static final String TOAST = "toast";// handler���ݹ�ȥ�Ĺؼ���
	public static final String DEVICE_NAME = "device_name";
	public static final String DEVICE_ADDRESS = "device_address";

	private static boolean isAsyn = true;// �첽��ʽ��������
	private static BluetoothService lastInstance = null;
	private  int sleepTime = 100;

	// ������Ϣ
	private static final String TAG = "BluetoothService";
	private static final boolean DEBUG = true;

	private BluetoothService(Handler handler, boolean isAsyn) {
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter.getState() == BluetoothAdapter.STATE_OFF)
			btAdapter.enable();// ������
		this.handler = handler;
		BluetoothService.isAsyn = isAsyn;
		this.state = STATE_NONE;
	}

	public static BluetoothService getService(Handler handler, boolean isAsyn) {
		if (lastInstance != null) {
			if (lastInstance.handler == handler)
				return lastInstance;// ����֮ǰ������ʵ��
			else
				lastInstance.stop();// handler��ͬ��ʾҪ���Ӳ�ͬ���豸����Ҫ���ϴ����Ӷϵ�
			Log.i(TAG, "Stop lastInstance:" + lastInstance.handler.getClass());
		}
		Log.i(TAG, "�½� BluetoothService:" + handler.getClass());
		lastInstance = new BluetoothService(handler, isAsyn);
		return lastInstance;
	}

	/**
	 * �����豸����������
	 * 
	 * @param name
	 * @return
	 */
	public BluetoothDevice getBondedDeviceByName(String name) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		for (BluetoothDevice d : pairedDevices) {
			String n = d.getName();
			if (name.equals(n)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * �����豸����ǰ׺������
	 * 
	 * @param name
	 * @return
	 */
	public BluetoothDevice getBondedDeviceByPrefix(String... names) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		for (BluetoothDevice d : pairedDevices) {
			for (String name : names) {
				String dName = d.getName();
				if (dName.length() >= name.length()) {
					String n = dName.substring(0, name.length());
					if (name.equalsIgnoreCase(n)) {
						return d;
					}
				}
			}
		}
		return null;
	}

	/**
	 * �����豸��ַ���Ѿ��󶨵�����
	 * 
	 * @param name
	 * @return
	 */
	public BluetoothDevice getBondedDeviceByAddress(String address) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		for (BluetoothDevice d : pairedDevices) {
			if (d.getAddress().equals(address)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * ���ݵ�ַ������ȡ�豸
	 * 
	 * @param address
	 * @return
	 */
	public BluetoothDevice getRemoteDeviceByAddress(String address) {
		return btAdapter.getRemoteDevice(address);
	}

	/**
	 * �ر�����
	 */
	public static void close() {
		if (lastInstance != null) {
			lastInstance.stop();
			lastInstance.btAdapter.disable();// �ر�����
		}
	}

	/**
	 * �ر������������Ӻʹ����߳�
	 */
	public synchronized void stop() {
		if (DEBUG)
			Log.d(TAG, "stop");
		if (connectedThread != null) {
			connectedThread.shutdown();
			connectedThread.cancel();
			connectedThread = null;
		}
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * ��ͬ����ʽ��ȡ״̬
	 * 
	 * @return
	 */
	public synchronized int getState() {
		return this.state;
	}

	/**
	 * ���������豸
	 * 
	 * @param device
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (DEBUG)
			Log.i(TAG, "connect to: " + device);
		if (state == STATE_CONNECTING) {// ȡ�����ڽ���������
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}
		if (state == STATE_CONNECTED) {// �Ͽ��Ѿ�����������
			if (connectedThread != null) {
				connectedThread.shutdown();// �ȹر���
				connectedThread.cancel();
				connectedThread = null;
			}
		}
		try {
			connectThread = new ConnectThread(device);
			connectThread.start();// ��ʼ��������
			setState(STATE_CONNECTING);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
	}

	/**
	 * ���������������̣߳���������ͨ���Ķ�д
	 * 
	 * @param socket
	 * @param device
	 */
	private void connected(BluetoothSocket socket, BluetoothDevice device) {
		if (DEBUG)
			Log.i(TAG, "begin connected to" + device);
		if (state == STATE_CONNECTING) {// ȡ�����ڽ���������
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}
		if (state == STATE_CONNECTED) {// �Ͽ��Ѿ�����������
			if (connectedThread != null) {
				connectedThread.shutdown();
				connectedThread.cancel();
				connectedThread = null;
			}
		}
		connectedThread = new ConnectedThread(socket);
		connectedThread.start();// ������������ͨ�����߳�
		Message msg = handler.obtainMessage(MESSAGE_DEVICE);
		Bundle bundle = new Bundle();
		bundle.putString(DEVICE_NAME, device.getName());
		bundle.putString(DEVICE_ADDRESS, device.getAddress());
		msg.setData(bundle);
		handler.sendMessage(msg);
	}

	/**
	 * ����״̬��Ϣ
	 * 
	 * @param state
	 */
	private void setState(int state) {
		if (DEBUG)
			Log.d(TAG, "setState() " + this.state + " -> " + state);
		this.state = state;
		// ��״̬�ı���Ϣ���ݸ�UI����
		handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	/**
	 * �������ӽ���ʧ�ܣ�֪ͨ����
	 */
	private void connectionFailed() {
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "�豸����ʧ��");
		msg.setData(bundle);
		handler.sendMessage(msg);
		setState(STATE_NONE);// ����״̬Ϊ����
	}

	/**
	 * �������Ӷ˿ڣ�֪ͨActivity
	 */
	public void connectionLost() {
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "�豸���ӶϿ�");
		msg.setData(bundle);
		handler.sendMessage(msg);
		if (connectedThread != null) {
			connectedThread.shutdown();
			connectedThread.cancel();
			connectedThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * ������ͨ����д������
	 * 
	 * @param buffer
	 */
	public void write(byte[] out) {
		ConnectedThread ct = null;
		synchronized (this) {// ��ͬ����ʽȡ�����ӹ����̵߳�����
			if (state != STATE_CONNECTED)
				return;
			ct = connectedThread;
		}
		ct.write(out);
	}

	/**
	 * �������ӵ��߳�
	 * 
	 * @author jiqunpeng
	 * 
	 *         ����ʱ�䣺2013-10-16 ����9:31:36
	 */
	private class ConnectThread extends Thread {
		private BluetoothDevice device;
		private BluetoothSocket socket;

		@SuppressLint("NewApi")
		public ConnectThread(BluetoothDevice device) throws SecurityException,
				NoSuchMethodException, IllegalArgumentException,
				IllegalAccessException, InvocationTargetException {
			this.device = device;
			if (btAdapter.getState() == BluetoothAdapter.STATE_OFF)
				btAdapter.enable();// ������
			BluetoothSocket tempSocket = null;
			int sdkVersion = Build.VERSION.SDK_INT;
			Method method = null;
			try {
				if (sdkVersion >= 10) {// 10���ϵ�ʹ�ò���ȫ����
					tempSocket = device
							.createInsecureRfcommSocketToServiceRecord(MY_UUID);
					method = device.getClass().getDeclaredMethod(
							"createInsecureRfcommSocket",
							new Class[] { int.class });
				} else {
					tempSocket = device
							.createRfcommSocketToServiceRecord(MY_UUID);
					if (!tempSocket.isConnected())
						method = device.getClass()
								.getMethod("createRfcommSocket",
										new Class[] { int.class });
				}
				if (!tempSocket.isConnected())
					tempSocket = (BluetoothSocket) method.invoke(device, 1);
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			socket = tempSocket;
		}

		@Override
		public void run() {
			Log.i(TAG, "BEGIN connectThread");
			setName("ConnectThread");
			btAdapter.cancelDiscovery();// ��������ǰȡ������
			try {
				// ��û����Թ�������
				if (device.getBondState() != BluetoothDevice.BOND_BONDED)
					BluetoothDevice.class.getMethod("createBond", new Class[0])
							.invoke(device, new Object[0]);
				try {
					socket.connect();// ��������
				} catch (IOException connectException) {// �����쳣
					Log.e(TAG, "connect failure", connectException);
					connectionFailed();
					try {
						socket.close();
					} catch (IOException closeException) {// �ر��쳣
						Log.e(TAG, "Can't close socket", closeException);
					}
					return;// û�����ӳɹ�������
				}
			} catch (Exception bondException) {// ���쳣
				Log.e(TAG, "createBond error", bondException);
				return;// û�����ӳɹ�������
			}
			// ���ӽ����ɹ��������̲߳�����Ҫ��
			synchronized (BluetoothService.this) {
				connectThread = null;
			}
			connected(socket, device);
		}

		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "close socket error", e);
			}
		}
	}

	/**
	 * �������Ӻõ�ͨ�����̣߳�����i/o����
	 * 
	 * @author jiqunpeng
	 * 
	 *         ����ʱ�䣺2013-10-16 ����3:35:54
	 */
	private class ConnectedThread extends Thread {
		private BluetoothSocket socket;
		private InputStream inStream = null;
		private OutputStream outStream = null;
		private boolean stop = false;

		public ConnectedThread(BluetoothSocket socket) {
			Log.i(TAG, "create ConnectedThread");
			this.socket = socket;
			try {
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "sockets stream not created", e);
			}
		}

		@Override
		public void run() {
			setState(STATE_CONNECTED);// ����״̬
			while (!stop & isAsyn) {// �첽��ʽ�£���ͣ�Ķ�����
				read();
			}
			while (!stop && !isAsyn) {
				synchronized (this) {
					try {
						wait();// ��˯��,��д�����ݽ���������
						read();
					} catch (InterruptedException e) {
						Log.e(TAG, "wait() Interrupted", e);
					}
				}
			}

		}

		/**
		 * ��ȡ����
		 */
		private void read() {
			try {
				int tryTime = 2;
				while (!stop && inStream.available() <= 0 && (tryTime--) >= 0)
					TimeUnit.MILLISECONDS.sleep(sleepTime);
				if (stop || tryTime < 0)// ˯һ������������ͨ�������ˣ�����ɶѽ,��ȥ!
					return;
				byte[] buffer = new byte[256];
				int bytes;
				bytes = inStream.read(buffer);
				if (bytes <= 0)
					return;
				byte[] contend = new byte[bytes];// ֻ������Ч��������
				for (int i = 0; i < bytes; i++) {
					contend[i] = buffer[i];
				}
				handler.obtainMessage(MESSAGE_READ, bytes, -1, contend)
						.sendToTarget();// ���ݸ������������

			} catch (IOException e) {// �������쳣
				Log.e(TAG, "Can't read from socket", e);
				connectionLost();// ���ӶϿ�
			} catch (InterruptedException e) {
				Log.e(TAG, "sleep interrupted", e);
			}
		}

		/**
		 * ������д������
		 * 
		 * @param buffer
		 */
		public synchronized void write(byte[] buffer) {
			try {
				outStream.write(buffer);
				outStream.flush();
				handler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
						.sendToTarget();// ����״̬
				if (!isAsyn) {// ͬ����ʽ�£�д�����ݺ����ϻ��Ѷ�
					notify();
				}
			} catch (IOException e) {
				connectionLost();
				Log.i(TAG, "write error", e);
			}
		}

		/**
		 * �ر��Ѿ���������
		 */
		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "close socket error", e);
			}
		}

		/**
		 * �ر���
		 */
		public void shutdown() {
			stop = true;
			try {
				if (inStream != null)
					inStream.close();
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of stream failed.", e);
			}

		}
	}
	
	public void setSleepTime(int time) {
		sleepTime = time;
	}

}
