package com.health.measurement;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bean.User;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.database.Cache;
import com.health.database.Tables;
import com.health.device.HealthDevice;
import com.health.device.PC300;
import com.health.util.L;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.DataSaver;
import com.health.web.WebService;

/***
 * ��ϸ������
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-3-20 ����11:01:19
 */
public class MeasureWbc extends BaseActivity {
	private static MyProgressDialog mDialog;
	private static Context context;
	private static EditText wbcEt;
	private static BluetoothService bluetoothService = null;
	private String btName = "";
	private static ImageView imageview;
	private static TextView statusView;
	private static WbcHandler wbcHandler;
	private static final int UPLOAD = 0x100189;
	private static final int NO_DATA = 0x100190;
	private static final String TAG = "MeasureWbc";
	private static String btMac = "";
	private List<Byte> receiveBytes = new ArrayList<Byte>();
	private static final byte[] WBCSS = { 87, 66, 67, 13, 10 };
	private Float value = null;
	private String unit = null;
	private boolean hasGetData = false;// �Ƿ��Ѿ���ȡ������
	private DataSaver dataSaver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_wbc);
		context = this;
		dataSaver = new DataSaver(context);
		initView();
		wbcHandler = new WbcHandler();
		mDialog = new MyProgressDialog(context);
		bluetoothService = BluetoothService.getService(wbcHandler, true);
	}

	private void initView() {
		imageview = (ImageView) findViewById(R.id.hp_image);
		wbcEt = (EditText) findViewById(R.id.wbc_et);
		statusView = (TextView) findViewById(R.id.connect_status);
	}

	public void uploadWbc(View view) {
		final User user = BaseActivity.getUser();
		if (user == null) {
			T.showLong(context, "�û���¼�ѹ���,���˳����µ�¼");
			return;
		}
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				String wbc  = wbcEt.getText().toString();
				if (wbc == null) {
					wbcHandler.obtainMessage(UPLOAD, NO_DATA, 0).sendToTarget();
					return;
				}
			//	String wbc = String.valueOf(value);
				ContentValues cv = getDefaultValues();
				cv.put(Tables.WBC, wbc);
				cv.put(Tables.TABLE_NAME, Tables.WBC);
				try {
					JSONObject para = new JSONObject();
					para.put(WebService.CARDNO, user.getCardNo());
					para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
					JSONObject data = new JSONObject();
					data.put(WebService.WBC, wbc);
					data.put(WebService.CHECKTIME, TimeHelper.getCurrentTime());
					para.put(WebService.DATA, data);

					JSONObject uploadPara = new JSONObject();
					uploadPara.put(DataSaver.PATH, WebService.PATH_UPLOAD_WBC);
					uploadPara.put(DataSaver.PARA, para);

					JSONObject result = dataSaver.uploadAndSave2db(cv,
							uploadPara);
					int status = result.getInt(WebService.STATUS_CODE);
					wbcHandler.obtainMessage(UPLOAD, status, 0).sendToTarget();
				} catch (Exception e) {
					L.e(e.getMessage());
					wbcHandler.obtainMessage(UPLOAD, WebService.ERROE, 0)
							.sendToTarget();
				}
			}
		}).start();
		mDialog.show();
	}

	/**
	 * ��ȡ�����Ĳ���,���ڱ��汾�����ݿ�
	 * 
	 * @return
	 */
	private ContentValues getDefaultValues() {
		ContentValues cv = new ContentValues();
		String time = TimeHelper.getCurrentTime();
		String idCard = BaseActivity.getUser().getCardNo();
		cv.put(Tables.CARDNO, idCard);
		cv.put(Tables.TIME, time);
		cv.put(Tables.DEVICENAME, btName);
		cv.put(Tables.DEVICEMAC, btMac);
		cv.put(Tables.FROM_TYPE, Tables.FROM_DEVICE);
		return cv;
	}

	private class WbcHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					Toast.makeText(context, "�����ӵ�" + btName, Toast.LENGTH_LONG)
							.show();
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					break;
				}
				break;

			case BluetoothService.MESSAGE_WRITE:
				// byte[] writeBuf = (byte[]) msg.obj;
				// new String(writeBuf);
				break;
			case BluetoothService.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				if (readBuf == null)
					return;
				if (hasGetData == false) {// û�л������
					hasGetData = check(readBuf);// ����һ��
					if (hasGetData) {// ��������ݼ���ʾ
						// wbcEt.setText(value + " (" +
						// unit + ")");
						wbcEt.setText(value + "");
					}
				}
				break;
			case BluetoothService.MESSAGE_TOAST:
				Toast.makeText(context,
						msg.getData().getString(BluetoothService.TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			case BluetoothService.MESSAGE_DEVICE:
				btName = msg.getData().getString(BluetoothService.DEVICE_NAME);
				String address = msg.getData().getString(
						BluetoothService.DEVICE_ADDRESS);
				cache.saveDeviceAddress(Cache.GMPUA, address);// �����ַ,�Ա��´��Դ�����
				break;
			case UPLOAD:
				mDialog.cancel();
				setImageView(imageview, msg.arg1);
				switch (msg.arg1) {
				case WebService.OK:
					T.showLong(context, "�ϴ��ɹ�,�յ�����:" + value);
					break;
				case WebService.ERROE:
					T.showLong(context, "�ϴ�ʧ��!");
					break;
				case NO_DATA:
					T.showLong(context, "�޲�����ֵ!");
					break;
				}
				break;
			}
		}
	};

	private static void setImageView(ImageView imageview, int status) {
		if (status == WebService.OK)
			imageview.setImageResource(R.drawable.light_greed);
		else if (status == -1)
			imageview.setImageResource(R.drawable.light_blue);
		else
			imageview.setImageResource(R.drawable.light_red);
	}

	/***
	 * �����յ������Ƿ��в�����ֵ��
	 * 
	 * @param readBuf
	 */
	private boolean check(byte[] readBuf) {
		for (byte b : readBuf) {
			receiveBytes.add(b);
		}
		int index = contains(receiveBytes, WBCSS);
		if (index != -1) {// ���õ�wbcss
			if (receiveBytes.size() >= index + 13) {
				List<Byte> sub = receiveBytes.subList(index, index + 13);
				byte[] subArray = new byte[sub.size()];
				for (int i = 0; i < sub.size(); i++)
					subArray[i] = sub.get(i);
				String result = new String(subArray);
				// System.out.println(result);
				String[] results = result.split("\\*");
				if (results.length < 2)
					return false;
				value = Float.valueOf(results[0].trim());
				unit = results[1].trim();
				return true;
			}
		}
		return false;
	}

	private int contains(List<Byte> bytes, byte[] buf) {
		int i = 0;
		int j = 0;
		while (i < bytes.size() && j < buf.length) {
			if (bytes.get(i).equals(Byte.valueOf(buf[j]))) {
				i++;
				j++;
			} else {
				i++;
				j = 0;// ����
			}
		}
		if (j == buf.length)
			return i;
		else
			return -1;
	}

	/**
	 * �������������豸��activity
	 */
	public void startDeviceListActivity(View view) {
		Intent serverIntent = new Intent(this, BluetoothListActivity.class);
		startActivityForResult(serverIntent,
				BluetoothListActivity.REQUEST_CONNECT_DEVICE);
	}

	/**
	 * �����������û�ָ�������豸�����ؽ�������
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
				connectWbc(address);
			}
			break;
		}
	}

	/**
	 * ������������PC300�豸
	 * 
	 * @param address
	 */
	private void connectWbc(String address) {
		btMac = address;
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, PC300.COMMAND_BETTERY, 3000);
		persistWriter.start();// ��������
	}

}
