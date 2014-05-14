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
import com.health.device.GmpUa;
import com.health.device.GmpUa.UaRecord;
import com.health.device.HealthDevice;
import com.health.util.Constants;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.DataSaver;
import com.health.web.Uploader;
import com.health.web.WebService;
import com.health.web.BackGroundThread.BackGroundTask;

/**
 * ��Һ������,���Բ��� ��ϸ�� �������� ��ԭ ������ pHֵ ǱѪ ���� ͪ�� ������ ������
 * ά����C 12����Ŀ
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-10-30 ����10:39:37
 */
public class MeasureUrine extends BaseActivity {

	private static TextView statusView = null;// ��������״̬
	private static String btName = "EMP-UI";// ��������
	private static String btMac = null;// ����mac
	private static UAHandler handler = null;
	private static Context context;
	private static Button homeButton;// ���������水ť
	private static Button returnButton;// ������һ����ť
	private static Button uploadButton;// �ϴ����ݰ�ť
	private static Button getDataButton;// ��ȡ�������ݰ�ť
	private static Button findButton;// �����豸��ť
	private static EditText leuEditText;
	private static EditText nitEditText;
	private static EditText ubgEditText;
	private static EditText proEditText;
	private static EditText phEditText;
	private static EditText sgEditText;
	private static EditText bldEditText;
	private static EditText ketEditText;
	private static EditText bilEditText;
	private static EditText gluEditText;
	private static EditText vcEditText;
	private static EditText dateEditText;

	private static ImageView imageView1;
	private static ImageView imageView2;
	private static ImageView imageView3;

	private DataSaver dataSaver;
	private static final String TAG = "MeasureUrine";
	private static final int UPLOADE_RESULT = 0x10245;
	private static BluetoothService bluetoothService = null;
	OnClickListener clickListener;
	EditText user;
	String username;
	int userid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_urine);
		context = this;
		dataSaver = new DataSaver(context);
		cache = new Cache(context);
		if (handler == null)
			handler = new UAHandler();
		findViewid();
		setOnClickListener();// ���ü�����
		bluetoothService = BluetoothService.getService(handler, false);
		connectGmpUa();
		setConnectState();
		uploadButton.setEnabled(false);
	}

	/**
	 * ���ü�����
	 */
	private void setOnClickListener() {
		clickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (view == homeButton) {
					MeasureUrine.this.setResult(RESULT_OK);
					MeasureUrine.this.finish();
				} else if (view == returnButton) {
					MeasureUrine.this.finish();
				} else if (view == getDataButton) {
					sendCommd(GmpUa.COMMAND_SINGLE_DATA);
					uploadButton.setEnabled(true);// ��ȡ���ݺ�����ϴ�����
				} else if (view == findButton) {
					startDeviceListActivity();// ������������activity
				} else if (view == uploadButton) {
					uploadButton.setEnabled(false);// ��ť�����ã��������ϴ�
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
		returnButton.setOnClickListener(clickListener);
		getDataButton.setOnClickListener(clickListener);
		findButton.setOnClickListener(clickListener);
	}

	protected void upload() throws JSONException {
		// ExecutorService exec =
		// Executors.newSingleThreadExecutor();// ���̳߳�
		String leu = leuEditText.getText().toString();
		if (leu.length() > 0) {
			String nit = nitEditText.getText().toString();
			String ubg = ubgEditText.getText().toString();
			String pro = proEditText.getText().toString();
			String ph = phEditText.getText().toString();
			String sg = sgEditText.getText().toString();
			String bld = bldEditText.getText().toString();
			String ket = ketEditText.getText().toString();
			String bil = bilEditText.getText().toString();
			String uglu = gluEditText.getText().toString();
			String vc = vcEditText.getText().toString();
			String time = dateEditText.getText().toString();
			// �����ݿ�
			ContentValues cv = getDefaultValues();
			cv.put(Tables.LEU, leu);
			cv.put(Tables.NIT, nit);
			cv.put(Tables.UBG, ubg);
			cv.put(Tables.PRO, pro);
			cv.put(Tables.PH, ph);
			cv.put(Tables.SG, sg);
			cv.put(Tables.BLD, bld);
			cv.put(Tables.KET, ket);
			cv.put(Tables.BLD, bld);
			cv.put(Tables.BIL, bil);
			cv.put(Tables.UGLU, uglu);
			cv.put(Tables.VC, vc);
			cv.put(Tables.TIME, time);
			cv.put(Tables.TABLE_NAME, Tables.TABLE_URINE);		
			// �ϴ���̨
			JSONObject data = getDefaltAttrs();
			data.put(Tables.LEU, leu);
			data.put(Tables.NIT, nit);
			data.put(Tables.UBG, ubg);
			data.put(Tables.PRO, pro);
			data.put(Tables.PH, ph);
			data.put(Tables.SG, sg);
			data.put(Tables.BLD, bld);
			data.put(Tables.KET, ket);
			data.put(Tables.BLD, bld);
			data.put(Tables.BIL, bil);
			data.put(Tables.UGLU, uglu);
			data.put(Tables.VC, vc);
			data.put(Tables.TIME, time);
			uploadInBack(data, WebService.PATH_URINE,cv);

		}
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

	/**
	 * ��ʼ���ؼ�
	 */
	private void findViewid() {
		imageView1 = (ImageView) findViewById(R.id.urine_status_image1);
		imageView2 = (ImageView) findViewById(R.id.urine_status_image2);
		imageView3 = (ImageView) findViewById(R.id.urine_status_image3);
		homeButton = (Button) findViewById(R.id.to_home_button);
		returnButton = (Button) findViewById(R.id.return_button);
		uploadButton = (Button) findViewById(R.id.upload_button);
		getDataButton = (Button) findViewById(R.id.get_data_button);
		leuEditText = (EditText) findViewById(R.id.leu);
		nitEditText = (EditText) findViewById(R.id.nit);
		ubgEditText = (EditText) findViewById(R.id.ubg);
		proEditText = (EditText) findViewById(R.id.pro);
		phEditText = (EditText) findViewById(R.id.pH);
		sgEditText = (EditText) findViewById(R.id.sg);
		bldEditText = (EditText) findViewById(R.id.bld);
		ketEditText = (EditText) findViewById(R.id.ket);
		bilEditText = (EditText) findViewById(R.id.bil);
		gluEditText = (EditText) findViewById(R.id.glu);
		vcEditText = (EditText) findViewById(R.id.vc);
		dateEditText = (EditText) findViewById(R.id.measure_time);
		statusView = (TextView) findViewById(R.id.connect_status);
		findButton = (Button) findViewById(R.id.find_device);
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
	 * ������Һ�������豸
	 */
	private void connectGmpUa() {
		if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
			String address = cache.getDeviceAddress(Cache.GMPUA);
			btMac = address;
			BluetoothDevice device = bluetoothService
					.getBondedDeviceByAddress(address);
			if (device != null) {
				bluetoothService.connect(device);// �����豸
				HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
						bluetoothService, GmpUa.CONFIRM, 10000);
				if (!persistWriter.isAlive())
					persistWriter.start();// ��������
			} else {
				Toast.makeText(context, "���Ѿ����豸�����Բ����豸...", Toast.LENGTH_LONG)
						.show();// û����Թ����豸����������
			}
		}
	}

	/**
	 * ��������������Һ�������豸
	 * 
	 * @param address
	 */
	private void connectGmpUa(String address) {
		btMac = address;
		BluetoothDevice device = bluetoothService
				.getRemoteDeviceByAddress(address);
		bluetoothService.connect(device);
		HealthDevice.PersistWriter persistWriter = new HealthDevice.PersistWriter(
				bluetoothService, GmpUa.CONFIRM, 10000);
		persistWriter.start();// ��������
	}

	private static class UAHandler extends Handler {
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
				SparseArray<List<byte[]>> map = GmpUa
						.getLegalPatternsFromBuffer(readBuf);
				int dataSize = map.size();
				for (int i = 0; i < dataSize; i++) {
					int token = map.keyAt(i);// ��ȡtoken
					List<byte[]> datas = map.get(token);
					switch (token) {
					case GmpUa.TOKEN_SINGLE_DATA:
						GmpUa.UaRecord record = GmpUa.parseRecord(datas
								.get(datas.size() - 1));
						if (null == record) {
							Toast.makeText(context, "�޴洢����", Toast.LENGTH_LONG)
									.show();
						} else
							showRecord(record);// ��ʾ��¼
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
			case UPLOADE_RESULT:

				int status = msg.arg1;
				String result = "�ϴ�ʧ��";
				setImageViews(status);
				if (status == WebService.OK) {
					result = "�ϴ��ɹ�";

				}
				if (status == WebService.NETERROE) {
					result = "�����쳣";
				}
				Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

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
	 * ��ʾ��¼
	 * 
	 * @param record
	 */
	public static void showRecord(UaRecord record) {
		leuEditText.setText(record.leu);
		nitEditText.setText(record.nit);
		ubgEditText.setText(record.ubg);
		proEditText.setText(record.pro);
		phEditText.setText(record.ph);
		sgEditText.setText(record.sg);
		bldEditText.setText(record.bld);
		ketEditText.setText(record.ket);
		bilEditText.setText(record.bil);
		gluEditText.setText(record.glu);
		vcEditText.setText(record.vc);
		dateEditText.setText(record.date);
	}

	private static void setImageViews(int status) {
		setImageView(imageView1, status);
		setImageView(imageView2, status);
		setImageView(imageView3, status);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
				connectGmpUa(address);
			}
			break;
		}
	}

	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			MeasureUrine.this.setResult(RESULT_OK);
			MeasureUrine.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
			// JSONObject result =
			// WebService.postConenction(para, path);
			int status = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(UPLOADE_RESULT, status, 0, path)
					.sendToTarget();
		} catch (Exception e) {
			handler.obtainMessage(UPLOADE_RESULT, WebService.NETERROE, 0, path)
					.sendToTarget();
		}
	}
}
