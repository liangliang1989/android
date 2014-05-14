package com.health.measurement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
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
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.database.Cache;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.device.BeneCheck;
import com.health.device.HealthDevice;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.DataSaver;
import com.health.web.Uploader;
import com.health.web.WebService;
import com.health.web.BackGroundThread.BackGroundTask;

/**
 * Ѫ�ǲ���
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-10-25 ����9:15:11
 */
public class MeasureGlucose extends BaseActivity {
	private static EditText glueEditText = null;// Ѫ�ǲ������
	private static EditText uaEditText = null;// ����������
	private static EditText cholEditText = null;// �ܵ��̴��������
	private static TextView statusView = null;// ��������״̬
	private static Button getDataButton = null;// ��ȡ���ݰ�ť
	private static String btName = "BeneCheck";// ��������
	private static String btMac = null;// ����mac��ַ
	private static BCHandler handler = null;
	private static Context context;
	private static int noDataCount = 0;// û�����ݵ���ĸ�����noDataCountΪ3�Ǳ�ʾ���û��
	private static final String TAG = "MeasureGlucose";
	private static BluetoothService bluetoothService = null;
	private static Button findButton = null;// �����豸��ť
	private static Button homeButton;// ���������水ť
	private static Button returnButton;// ������һ����ť
	private static Button uploadButton;// �ϴ�����button
	private static ImageView gluImageView;
	private static ImageView uaImageView;
	private static ImageView cholImageView;

	private static String gluDate;
	private static String uaDate;
	private static String cholDate;
	private EditText glu_data;
	private EditText ua_data;
	private EditText chol_data;

	private DataSaver dataSaver;
	public static final int UPLOAD_RESULT = 0x10040;
	OnClickListener clickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_glucose);
		context = this;
		cache = new Cache(context);
		dataSaver = new DataSaver(context);
		findViewId();// ��ʼ���ؼ�
		setOnClickListener();// ���ü�����
		if (handler == null)// ֻ����һ��handler
			handler = new BCHandler();
		bluetoothService = BluetoothService.getService(handler, false);// ͬ����ʽ
		connectBeneCheck();// ���Ӱٽ�
		setConnectState();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setOnClickListener() {
		clickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (view == homeButton) {// ����������
					MeasureGlucose.this.setResult(RESULT_OK);
					MeasureGlucose.this.finish();
				} else if (view == returnButton) {// ������һ������
					MeasureGlucose.this.finish();
				} else if (view == findButton) {
					startDeviceListActivity();// ������������activity
				} else if (view == getDataButton) {// ����Ѫ��
					sendCommd(BeneCheck.QUERY_GLU_NUM);// ��ѯ��¼�������ٲ�ѯ���µļ�¼
					sendCommd(BeneCheck.QUERY_UA_NUM);
					sendCommd(BeneCheck.QUERY_CHOL_NUM);
					uploadButton.setEnabled(true);// �����ϴ���ť���Ե��
				} else if (view == uploadButton) {
					uploadButton.setEnabled(false);// �ϴ���button���ɵ��
					try {
						upload();
					} catch (JSONException e) {
						// TODO Auto-generated catch
						// block
						e.printStackTrace();
					}
					Toast.makeText(context, "��̨��ʼ�ϴ�", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
		homeButton.setOnClickListener(clickListener);
		returnButton.setOnClickListener(clickListener);
		uploadButton.setOnClickListener(clickListener);
		getDataButton.setOnClickListener(clickListener);
		findButton.setOnClickListener(clickListener);
	}

	protected void upload() throws JSONException {
		String glu = glueEditText.getText().toString();
		if (glu.length() > 0) {// �����������ݣ������ϴ�
			ContentValues cv = getDefaultValues();
			cv.put(Tables.GLU, glu);
			cv.put(Tables.TIME, gluDate);
			cv.put(Tables.TABLE_NAME, Tables.TABLE_GLU);			
			JSONObject data = getDefaltAttrs();
			data.put(Tables.GLU, glu);
			data.put(Tables.TIME, gluDate);
			uploadInBack(data, WebService.PATH_GLU,cv);
		}
		String ua = uaEditText.getText().toString();// ���������ϴ�
		if (ua.length() > 0) {
			ContentValues cv = getDefaultValues();
			cv.put(Tables.UA, ua);
			cv.put(Tables.TIME, uaDate);
			cv.put(Tables.TABLE_NAME, Tables.TABLE_UA);			
			JSONObject data = getDefaltAttrs();
			data.put(Tables.UA, ua);
			data.put(Tables.TIME, uaDate);
			uploadInBack(data, WebService.PATH_UA,cv);
		}
		String chol = cholEditText.getText().toString();
		if (chol.length() > 0) {// �ܵ��̴������ݣ������ϴ�
			ContentValues cv = getDefaultValues();
			cv.put(Tables.CHOL, chol);
			cv.put(Tables.TIME, cholDate);
			cv.put(Tables.TABLE_NAME, Tables.TABLE_CHOL);			
			JSONObject data = getDefaltAttrs();
			data.put(Tables.CHOL, chol);
			data.put(Tables.TIME, cholDate);
			uploadInBack(data, WebService.PATH_CHOL,cv);
		}
	}

	/***
	 * ��ȡ����������Ŀ���еļ�������
	 * 
	 * @return
	 */
	/*
	 * private Map<String, String> getDefaltAttrs() {
	 * String idCard =
	 * BaseActivity.getUser().getCardNo(); Map<String,
	 * String> dataMap = new HashMap<String, String>();
	 * dataMap.put(Tables.DEVICEMAC, btMac);
	 * dataMap.put(Tables.DEVICENAME, btName);
	 * dataMap.put(Tables.CARDNO, idCard);
	 * dataMap.put(WebService.STATUS,
	 * WebService.UNUPLOAD);// ״̬Ϊδ�ϴ�
	 * dataMap.put(WebService.PLAT_ID_KEY,
	 * WebService.PLAT_ID_VALUE);// return dataMap; }
	 */

	/**
	 * ��ʼ���ؼ�
	 */
	private void findViewId() {
		glueEditText = (EditText) this.findViewById(R.id.gluse_value_et);
		uaEditText = (EditText) this.findViewById(R.id.ua_value_et);
		cholEditText = (EditText) this.findViewById(R.id.chol_value_et);
		statusView = (TextView) this.findViewById(R.id.connect_status);
		findButton = (Button) this.findViewById(R.id.find_device);
		homeButton = (Button) findViewById(R.id.to_home_button);
		returnButton = (Button) findViewById(R.id.return_button);
		getDataButton = (Button) findViewById(R.id.measure_getdata);
		homeButton = (Button) findViewById(R.id.to_home_button);
		returnButton = (Button) findViewById(R.id.return_button);
		uploadButton = (Button) findViewById(R.id.upload_button);
		gluImageView = (ImageView) findViewById(R.id.glu_image);
		uaImageView = (ImageView) findViewById(R.id.ua_image);
		cholImageView = (ImageView) findViewById(R.id.chol_image);
		glu_data = (EditText) findViewById(R.id.xt_data);
		ua_data = (EditText) findViewById(R.id.ua_data);
		chol_data = (EditText) findViewById(R.id.dgc_data);
	}

	/**
	 * ���ü���button����ʾ������
	 * 
	 * @param status
	 */
	private static void setVisibility() {
		int status;
		if (bluetoothService != null
				&& bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			status = View.VISIBLE;// ����ʱ���ÿɼ�
		} else {
			status = View.INVISIBLE;// δ����ʱ���ò��ɼ�
		}
		getDataButton.setVisibility(status);
	}

	/**
	 * ��������״̬
	 */
	private void setConnectState() {
		setVisibility();
		if (bluetoothService == null) {
			statusView.setText(R.string.unconnect);
			return;
		}
		switch (bluetoothService.getState()) {
		case BluetoothService.STATE_CONNECTING:
			statusView.setText(R.string.connecting);
			break;
		case BluetoothService.STATE_CONNECTED:
			statusView.setText(btName);
			break;
		case BluetoothService.STATE_NONE:
			statusView.setText(R.string.unconnect);
			break;
		}
	}

	/**
	 * �������������豸��activity
	 */
	private void startDeviceListActivity() {
		Intent serverIntent = new Intent(this, BluetoothListActivity.class);
		startActivityForResult(serverIntent,
				BluetoothListActivity.REQUEST_CONNECT_DEVICE);
	}

	/**
	 * ���Ӱٽ�
	 */
	private void connectBeneCheck() {
		if (bluetoothService.getState() == BluetoothService.STATE_NONE) {// ����״̬�²�����
			String address = cache.getDeviceAddress(Cache.BENECHECK);
			btMac = address;
			BluetoothDevice device = bluetoothService
					.getBondedDeviceByAddress(address);
			if (device != null) {
				bluetoothService.connect(device);// �����豸
				HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
						bluetoothService, BeneCheck.ECHO, 1000);
				if (!persistWriter.isAlive())
					persistWriter.start();// ��������
			} else {
				Toast.makeText(context, "���Ѿ����豸�����Բ����豸...", Toast.LENGTH_LONG)
						.show();// û����Թ����豸����������
			}
		}

	}

	/**
	 * �����������İٽ��豸
	 * 
	 * @param address
	 */
	private void connectBeneCheck(String address) {
		btMac = address;
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, BeneCheck.ECHO, 1000);
		persistWriter.start();// ��������
	}

	/**
	 * �����������û�ָ�������豸�����ؽ�������
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
				connectBeneCheck(address);
			}
			break;
		}
	}

	protected void onResume() {
		super.onResume();
		// ע����Ϣ������

	}

	private class BCHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					statusView.setText(btName);
					Toast.makeText(context, "�����ӵ�" + btName, Toast.LENGTH_LONG)
							.show();
					setVisibility();// ���ò�����ť�ɼ�
					break;
				case BluetoothService.STATE_CONNECTING:
					statusView.setText(R.string.connecting);
					setVisibility();
					break;
				case BluetoothService.STATE_NONE:
					statusView.setText(R.string.unconnect);
					setVisibility();
					break;
				}
				break;

			case BluetoothService.MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				new String(writeBuf);
				// Toast.makeText(context,
				// writeMessage,
				// Toast.LENGTH_LONG).show();
				break;
			case BluetoothService.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				Log.i(TAG, "get:" + Arrays.toString(readBuf));
				SparseArray<List<byte[]>> map = BeneCheck
						.getLegalPatternsFromBuffer(readBuf);
				int dataSize = map.size();
				for (int i = 0; i < dataSize; i++) {
					int token = map.keyAt(i);// ��ȡtoken
					List<byte[]> datas = map.get(token);
					switch (token) {
					case BeneCheck.TOKEN_GLU_NUM:// ��ǰ�ܸо�switch��break�ֵֹ�.break�÷�֧����
					case BeneCheck.TOKEN_CHOL_NUM:// ���ڲ���breakʱ����˳��ִ�У����ڶ��caseֵ��Ҫ
					case BeneCheck.TOKEN_UA_NUM:// ��ͬ�Ĵ���ʱ�����ã�����Ĵ�����ظ�
						int num = BeneCheck.getNum(datas.get(datas.size() - 1));
						if (num >= 1) {// �м�¼ʱ��ȥ����
							byte[] command = BeneCheck.getLatestRecordCommand(
									token, 1);
							sendCommd(command);// �õ��˼�¼��Ŀ��,���Ϸ��Ͳ�ѯ��¼����
						} else {
							noDataCount++;
							if (noDataCount >= 3)
								Toast.makeText(context, "������",
										Toast.LENGTH_LONG).show();
						}
						break;
					case BeneCheck.TOKEN_GLU_RECORD:
						BeneCheck.Record gluRecord = BeneCheck.getRecord(datas
								.get(datas.size() - 1));
						gluDate = gluRecord.date;
						glueEditText.setText("" + gluRecord.value);
						glu_data.setText(gluDate);
						break;
					case BeneCheck.TOKEN_UA_RECORD:
						BeneCheck.Record uaRecord = BeneCheck.getRecord(datas
								.get(datas.size() - 1));
						uaDate = uaRecord.date;
						uaEditText.setText("" + uaRecord.value);
						ua_data.setText(uaDate);
						break;
					case BeneCheck.TOKEN_CHOL_RECORD:
						BeneCheck.Record cholRecord = BeneCheck.getRecord(datas
								.get(datas.size() - 1));
						cholDate = cholRecord.date;
						cholEditText.setText("" + cholRecord.value);
						chol_data.setText(cholDate);
						break;
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
				cache.saveDeviceAddress(Cache.BENECHECK, address);// �����ַ,�Ա��´��Դ�����
				break;
			/*
			 * case Uploader.MESSAGE_UPLOADE_RESULT:
			 * Bundle bundler = msg.getData(); String
			 * item = bundler.getString(Cache.ITEM);
			 * int status =
			 * bundler.getInt(Uploader.STUTAS); if
			 * (Cache.GLU.equals(item)) {
			 * setImageView(gluImageView, status); } if
			 * (Cache.UA.equals(item))
			 * setImageView(uaImageView, status); if
			 * (Cache.CHOL.equals(item))
			 * setImageView(cholImageView, status);
			 * break;
			 */
			case UPLOAD_RESULT:
				int status = msg.arg1;
				String item = (String) msg.obj;
				if (WebService.PATH_GLU.equals(item)) {
					setImageView(gluImageView, status);
				} else if (WebService.PATH_UA.equals(item))
					setImageView(uaImageView, status);
				else if (WebService.PATH_CHOL.equals(item))
					setImageView(cholImageView, status);
				break;
			}

		}

	}

	private static void setImageView(ImageView imageview, int status) {
		if (status == WebService.OK)
			imageview.setImageResource(R.drawable.light_greed);
		else if (status == -1)
			imageview.setImageResource(R.drawable.light_blue);
		else
			imageview.setImageResource(R.drawable.light_red);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			MeasureGlucose.this.setResult(RESULT_OK);
			MeasureGlucose.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��������
	 * 
	 * @param command
	 */
	private static void sendCommd(byte[] command) {
		Log.i(TAG, "send:" + Arrays.toString(command));
		if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED
				&& command != null)
			bluetoothService.write(command);
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

	private JSONObject getDefaltAttrs() throws JSONException {
		JSONObject data = new JSONObject();
		data.put(WebService.DEVICENAME, btName);
		data.put(WebService.DEVICEMAC, btMac);
		return data;
	}

	private void uploadInBack(final JSONObject data, final String path,
			final ContentValues values) {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				upload(data, path, values);
			}
		}).start();
	}

	public void upload(JSONObject data, String path, ContentValues values) {
		try {
			JSONObject para = new JSONObject();
			String idCard = BaseActivity.getUser().getCardNo();
			para.put(WebService.CARDNO, idCard);
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			para.put(WebService.DATA, data);
			para.put(WebService.CRC, "");
			JSONObject uploadPara = new JSONObject();
			uploadPara.put(DataSaver.PATH, path);
			uploadPara.put(DataSaver.PARA, para);
			JSONObject result = dataSaver.uploadAndSave2db(values, uploadPara);

			int status = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(UPLOAD_RESULT, status, 0, path)
					.sendToTarget();
		} catch (Exception e) {
			handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE, 0, path)
					.sendToTarget();
		}
	}
}
