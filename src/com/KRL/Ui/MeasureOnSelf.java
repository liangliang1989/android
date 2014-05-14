package com.KRL.Ui;

import java.util.Arrays;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.database.Cache;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.device.HealthDevice;
import com.health.device.PC300;
import com.health.device.PC300.EcgFrame;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;

/**
 * ����Ѫѹ
 * 
 */
public class MeasureOnSelf extends BaseActivity {

	private static EditText HighBpEditText = null;// ��ѹ�ı���
	private static EditText lowBpEditText = null;// ��ѹ�ı���
	private static EditText pulseEditText = null;// ����
	private static EditText boEditText = null;// Ѫ��
	private static EditText tempEditText = null;// ����
	private static Button measureBpButton = null;// ���Բ���Ѫѹ��ť
	private static Button stableBoButton = null;// ���Բ���Ѫ����ť
	private static Button measureEcgButton = null;// �����ĵ簴ť
	private static Button uploadButton;// �ϴ����ݰ�ť
	private static boolean boStable = false;// Ѫ���㶨���
	private static boolean tempStable = false;// ���º㶨���
	private static Button findButton = null;// �����豸��ť
	private static TextView statusView = null;// ��������״̬
	private static ImageView hBpImageView = null;// ����ѹǰ��ͼ��
	private static ImageView lBpImageView = null;// ����ѹǰ��ͼ��
	private static ImageView pulseImageView = null;// ����ǰ��ͼ��
	private static ImageView boImageView = null;// Ѫ��ǰ��ͼ��
	private static ImageView tempImageView = null;// ����ǰ��ͼ��
	private static ImageView ecgImageView = null;// �ĵ�ǰ��ͼ��
	private static final boolean DEBUG = true;
	private static final String TAG = "MeasureBp";
	// private static BluetoothService bluetoothService = null;
	private static PC300Handler handler = null;

	private static String btName = "PC_300SNT";// ��������
	private static String btMac = null;// ��������
	private static Context context;
	private static boolean stop = false;
	private static boolean ecg_stop = false;
	private static LinearLayout graphLayout;// װѪ��ͼ�Ĳ���
	private static GraphicalView boWaveView;// Ѫ��ͼ
	private static XYSeries xSeries;
	private static XYMultipleSeriesRenderer mRenderer;
	private static double xAxisMax = 300;
	private static float boWaveIndex = 0;
	private static float ecgFrameNum = 0;
	private static int pulseSource = 4;// ���ʲ�����Դ
	private static final int PULSE_FROM_BP = 1;// ���ʲ�����Դ��Ѫѹ
	private static final int PULSE_FROM_BO = 2;// ���ʲ�����Դ��Ѫ��
	private static final int PULSE_FROM_ECG = 3;// ���ʲ�����Դ���ĵ�
	private static final int PULSE_FROM_UNKOWN = 4;// ���ʲ�����δ֪
	public static final int UPLOAD_RESULT = 0x10040;

	enum CurvType {
		BO, ECG, NULL
	}

	private static CurvType curvType = CurvType.NULL;

	private static DatabaseService dbService;// ���ݿ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���õ�ǰactivity���� �������setContentView֮ǰ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_on_self);
		findId();
		context = this;
		dbService = new DatabaseService(context);
		cache = new Cache(context);
		// setVisibility();
		if (handler == null)
			handler = new PC300Handler();
		// bluetoothService = BluetoothService.getService(handler, true);// �첽��
		// bluetoothService.setSleepTime(20);
		// connectPC300();
		setOnClickListener();
		// setConnectState();

		String measureType = getIntent().getExtras().getString("MeasureType");
		if ("BloodOxygen".equals(measureType)) {
			boEditText.setClickable(true);
			boEditText.setFocusable(true);
			boEditText.setFocusableInTouchMode(true);

		} else if ("Temperature".equals(measureType)) {
			tempEditText.setClickable(true);
			tempEditText.setFocusable(true);
			tempEditText.setFocusableInTouchMode(true);
		}
//		findButton.setVisibility(View.GONE);
		uploadButton.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		xSeries = new XYSeries("");
		graphLayout.addView(lineView());
	}

	/**
	 * ���ü�����
	 */
	private void setOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (v == measureBpButton)
				// takeMeasureBp();// ��ʼ����
				// else if (v == findButton)
				// startDeviceListActivity();// ������������activity
				// else if (v == stableBoButton) {
				// boStable = !boStable;// Ѫ���㶨
				// if (boStable)
				// stableBoButton.setText("ȡ��Ѫ������");
				// else
				// stableBoButton.setText("����Ѫ��");
				// } else if (v == measureEcgButton) {
				// takeMeasurEcg();
				// } else
				if (v == uploadButton) {
					T.showShort(context, "��̨��ʼ�ϴ�");
					try {
						upload();// �ϴ�����
					} catch (JSONException e) {
						T.showLong(context, "�������!");
						e.printStackTrace();
					}
				}
			}

		};
//		measureBpButton.setOnClickListener(onClickListener);
//		stableBoButton.setOnClickListener(onClickListener);
//		measureEcgButton.setOnClickListener(onClickListener);
		uploadButton.setOnClickListener(onClickListener);
//		findButton.setOnClickListener(onClickListener);
		// uploadButton.setClickable(true);//
		// ��ʼ��ʱ�����ϴ�����
	}

	private static void setImageView(ImageView imageview, int status) {
		if (status == WebService.OK)
			imageview.setImageResource(R.drawable.light_greed);
		else if (status == -1)
			imageview.setImageResource(R.drawable.light_blue);
		else
			imageview.setImageResource(R.drawable.light_red);
	}

	/**
	 * ���ü���button����ʾ������
	 * 
	 * @param status
	 */
	// private static void setVisibility() {
	// int status;
	// if (bluetoothService != null
	// && bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
	// status = View.VISIBLE;// ����ʱ���ÿɼ�
	// } else {
	// status = View.INVISIBLE;// δ����ʱ���ò��ɼ�
	// }
	// measureBpButton.setVisibility(status);
	// stableBoButton.setVisibility(status);
	// measureEcgButton.setVisibility(status);
	// }

	/**
	 * ��ʼ��id
	 */
	private void findId() {
		HighBpEditText = (EditText) findViewById(R.id.hp);
		lowBpEditText = (EditText) findViewById(R.id.lp);
		pulseEditText = (EditText) findViewById(R.id.mb);
		boEditText = (EditText) findViewById(R.id.bo);
		tempEditText = (EditText) findViewById(R.id.temp);
		measureBpButton = (Button) findViewById(R.id.start_stop_bp_btn);
		stableBoButton = (Button) findViewById(R.id.bo_stable_button);
		measureEcgButton = (Button) findViewById(R.id.start_stop_ecg_btn);
		findButton = (Button) findViewById(R.id.find_device);
		uploadButton = (Button) findViewById(R.id.upload_button);
		statusView = (TextView) findViewById(R.id.connect_status);
		hBpImageView = (ImageView) findViewById(R.id.hp_image);
		lBpImageView = (ImageView) findViewById(R.id.lp_image);
		pulseImageView = (ImageView) findViewById(R.id.pulse_image);
		boImageView = (ImageView) findViewById(R.id.bo_image);
		tempImageView = (ImageView) findViewById(R.id.temp_image);
		ecgImageView = (ImageView) findViewById(R.id.ecg_image);
		graphLayout = (LinearLayout) this.findViewById(R.id.bo_image_view);
	}

	/**
	 * ��������״̬����ʾ
	 */
	// private void setConnectState() {
	// if (bluetoothService == null) {
	// statusView.setText(R.string.unconnect);
	// return;
	// }
	// switch (bluetoothService.getState()) {
	// case BluetoothService.STATE_CONNECTING:
	// statusView.setText(R.string.connecting);
	// break;
	// case BluetoothService.STATE_CONNECTED:
	// statusView.setText(btName);
	// break;
	// case BluetoothService.STATE_NONE:
	// statusView.setText(R.string.unconnect);
	// break;
	// }
	// }

	// /**
	// * ����Ѫѹ����������ֹͣ����
	// */
	// private void takeMeasureBp() {
	// if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
	// if (stop == false) {
	// bluetoothService.write(PC300.COMMAND_BP_START);
	// initDataTextEdit();
	// measureBpButton.setText("ֹͣѪѹ����");
	// } else {
	// bluetoothService.write(PC300.COMMAND_BP_STOP);
	// measureBpButton.setText("��ʼѪѹ����");
	// }
	// stop = !stop;
	// }
	// }

	/**
	 * �����ĵ����������ֹͣ����
	 */
	// private void takeMeasurEcg() {
	// if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
	// if (ecg_stop == false) {
	// bluetoothService.write(PC300.COMMAND_ECG_START);
	// measureEcgButton.setText("ֹͣ�ĵ����");
	// } else {
	// bluetoothService.write(PC300.COMMAND_ECG_STOP);
	// measureEcgButton.setText("��ʼ�ĵ����");
	// }
	// ecg_stop = !ecg_stop;
	// }
	// }

	// /*
	// * ��ʼ��������ʾ����
	// */
	// protected void initDataTextEdit() {
	// HighBpEditText.setText("");
	// lowBpEditText.setText("");
	// setPulseText("", PULSE_FROM_UNKOWN);
	// }

	/**
	 * ���ӵ�PC300�豸
	 */
	// private void connectPC300() {
	// if (bluetoothService.getState() == BluetoothService.STATE_NONE) {//
	// ����״̬������
	//
	// String address = cache.getDeviceAddress(Cache.PC300);// �����ϴν���������
	// btMac = address;
	// BluetoothDevice device = bluetoothService
	// .getBondedDeviceByAddress(address);
	// if (device != null) {
	// connect(device);
	// } else {
	// device = bluetoothService.getBondedDeviceByPrefix("94:21:97",
	// "00:13:EF");// ����ǰ׺������
	// if (device != null)
	// connect(device);
	// else
	// T.showLong(context, "���Ѿ��󶨵�PC300�豸�����Բ����豸...");// û����Թ����豸����������
	// }
	// }
	// }

	/**
	 * ���������豸
	 */
	// private void connect(BluetoothDevice device) {
	// bluetoothService.connect(device);
	// HealthDevice.PersistWriter persistWriter = new
	// HealthDevice.PersistWriter(
	// bluetoothService, PC300.COMMAND_BETTERY, 3000);
	// persistWriter.start();// ��������
	// }

	/**
	 * ������������PC300�豸
	 * 
	 * @param address
	 */
	// private void connectPC300(String address) {
	// btMac = address;
	// BluetoothDevice device = bluetoothService
	// .getRemoteDeviceByAddress(address);
	// bluetoothService.connect(device);
	// HealthDevice.PersistWriter persistWriter = new
	// HealthDevice.PersistWriter(
	// bluetoothService, PC300.COMMAND_BETTERY, 3000);
	// persistWriter.start();// ��������
	// }

	/**
	 * �������������豸��activity
	 */
	// private void startDeviceListActivity() {
	// Intent serverIntent = new Intent(this, BluetoothListActivity.class);
	// startActivityForResult(serverIntent,
	// BluetoothListActivity.REQUEST_CONNECT_DEVICE);
	// }

	/**
	 * �����������û�ָ�������豸�����ؽ�������
	 */
	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// Log.i(TAG, "onActivityResult " + resultCode);
	// switch (requestCode) {
	// case BluetoothListActivity.REQUEST_CONNECT_DEVICE:
	// if (resultCode == Activity.RESULT_OK) {
	// String address = data.getExtras().getString(
	// BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
	// connectPC300(address);
	// }
	// break;
	// }
	// }

	private class PC300Handler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case BluetoothService.MESSAGE_STATE_CHANGE:
			// switch (msg.arg1) {
			// case BluetoothService.STATE_CONNECTED:
			// statusView.setText(btName);
			// bluetoothService.write(PC300.COMMAND_TEMP_START);
			// uploadButton.setEnabled(true);
			// uploadButton.setClickable(true);// ���ÿ��Ե��
			// T.showLong(context, "�����ӵ�" + btName);
			// setVisibility();// ���ò�����ť�ɼ�
			// break;
			// case BluetoothService.STATE_CONNECTING:
			// statusView.setText(R.string.connecting);
			// break;
			// case BluetoothService.STATE_NONE:
			// statusView.setText(R.string.unconnect);
			// setVisibility();
			// break;
			// }
			// break;

			case BluetoothService.MESSAGE_WRITE:
				// byte[] writeBuf = (byte[]) msg.obj;

				break;
			// case BluetoothService.MESSAGE_READ:

			// byte[] readBuf = (byte[]) msg.obj;
			// Log.i(TAG, Arrays.toString(readBuf));
			// processReadData(readBuf);
			// if (DEBUG) {
			// // Log.d(TAG, readMessage);
			// Log.d(TAG, Arrays.toString(readBuf));
			// }
			// break;
			case BluetoothService.MESSAGE_TOAST:
				T.showShort(context,
						msg.getData().getString(BluetoothService.TOAST));
				break;
			case BluetoothService.MESSAGE_DEVICE:
				btName = msg.getData().getString(BluetoothService.DEVICE_NAME);
				String address = msg.getData().getString(
						BluetoothService.DEVICE_ADDRESS);
				cache.saveDeviceAddress(Cache.PC300, address);// �����ַ,�Ա��´��Դ�����
				break;
			case UPLOAD_RESULT:
				int status = msg.arg1;
				String item = (String) msg.obj;
				if (WebService.PATH_BP.equals(item)) {
					setImageView(hBpImageView, status);
					setImageView(lBpImageView, status);
					setImageView(pulseImageView, status);
				} else if (WebService.PATH_BO.equals(item))
					setImageView(boImageView, status);
				else if (WebService.PATH_TEMP.equals(item))
					setImageView(tempImageView, status);
				else if (WebService.PATH_ECG.equals(item)) {
					setImageView(ecgImageView, status);
					if (WebService.OK == status)
						T.showLong(context, "�ĵ��ϴ��ɹ�");
					else
						T.showLong(context, "�ĵ��ϴ�ʧ��");
				}
				break;
			}

		}
	};

	/**
	 * ������������,��������ݿ��ܰ�������Э�����ݣ�����Э��İ�ͷ����������ݷָ
	 * ����ͬһ���͵�����ֻ�������µ���У����ȷ��һ��,���ÿһ���͵�������Ӧ������
	 * 
	 * @param buffer
	 * 
	 */
	// public void processReadData(byte[] buffer) {
	// PC300 pc300 = new PC300();
	// SparseArray<List<byte[]>> map = pc300
	// .getLegalPatternsFromBuffer(buffer);
	// int dataSize = map.size();
	// for (int i = 0; i < dataSize; i++) {
	// int token = map.keyAt(i);// ��ȡtoken
	// List<byte[]> datas = map.get(token);
	// switch (token) {
	// case PC300.TOKEN_BP_CURRENT:
	// Integer currentBp = pc300
	// .getCurrentBp(datas.get(datas.size() - 1));// ��ȡ��ǰѪѹֵ
	// HighBpEditText.setText(currentBp.toString());
	// break;
	// case PC300.TOKEN_BP_RESULT:
	// int[] bpResult = pc300.getResultBp(datas.get(datas.size() - 1));
	// processBpResult(bpResult);
	// break;
	// case PC300.TOKEN_BO_WAVE:
	// for (byte[] data : datas) {
	// Log.i("TOKEN_BO_WAVE", Arrays.toString(data));
	// int[] value = pc300.getBoWave(data);
	// if (null != value)
	// updateWaveImage(value, CurvType.BO);
	// }
	// break;
	// case PC300.TOKEN_BO_PAKAGE:
	// for (byte[] each : datas) {
	// int[] spO2 = pc300.getSpO2(each);
	// if (spO2[2] == PC300.PROBE_OFF)
	// T.showShort(context, "̽������");
	// else {
	// if (!boStable)
	// boEditText.setText(spO2[0] + "");
	// // setPulseText(String.valueOf(spO2[1]),
	// // PULSE_FROM_BO);
	// }
	//
	// }
	// break;
	// case PC300.TOKEN_ECG_WAVE:// �ĵ�����
	// for (byte[] each : datas) {
	// EcgFrame fram = pc300.getEcgFram(each);
	// if (null != fram) {
	// pc300.addEcgFrameAndSort(fram);
	// if (ecgFrameNum == 0) {
	// updateWaveImage(fram.getEcg(), CurvType.ECG);
	// } else {
	// EcgFrame nextFrame = pc300.getNextFrame(0);
	// if (nextFrame != null) {
	// ecgFrameNum = nextFrame.getFramNum();
	// updateWaveImage(nextFrame.getEcg(),
	// CurvType.ECG);
	// }
	// }
	//
	// if (fram.isDeviceOff() == true)
	// T.showLong(context, "��������");
	// }
	// }
	// break;
	// case PC300.TOKEN_ECG_RESULT:
	// if (datas.size() <= 0)
	// return;
	// int[] ecgResult = pc300.getEcgResult(datas.get(0));
	// if (ecgResult[0] < 18) {
	// // T.showLong(context,
	// // PC300.ECG_RESULT[ecgResult[0]]);
	// dialog(PC300.ECG_RESULT[ecgResult[0]]);
	// }
	// if (ecgResult[1] != 0)
	// ;
	// // setPulseText(String.valueOf(ecgResult[1]),
	// // PULSE_FROM_ECG);
	//
	// break;
	// case PC300.TOKEN_TEMP:
	// Float temp = pc300.getTemp(datas.get(datas.size() - 1));
	// if (!tempStable)
	// tempEditText.setText(temp.toString());
	// break;
	// case PC300.TOKEN_POWER_OFF:
	// bluetoothService.stop();
	// T.showLong(context, "�豸�ѹػ�");
	// break;
	// }
	// }
	// }

	/***
	 * �������ʵ�ֵ,�������Դ
	 * 
	 * @param value
	 *            ����ֵ
	 * @param source
	 *            ������Դ(Ѫ��\Ѫѹ\�ĵ�\����)
	 */
	// private static void setPulseText(String value, int source) {
	// pulseSource = source;
	// pulseEditText.setText(value);
	//
	// }

	/**
	 * ����Ѫ��ͼ
	 * 
	 * @param data
	 */
	// private void updateWaveImage(int[] data, CurvType type) {
	// if (curvType != type) {
	// curvType = type;
	// if (curvType == CurvType.ECG) {
	// mRenderer.setYAxisMin(50);// ����y����Сֵ
	// mRenderer.setYAxisMax(200);
	// xAxisMax = 300;
	// mRenderer.setXAxisMax(xAxisMax);
	// xSeries.setTitle("�ĵ�����ͼ");
	// } else if (curvType == CurvType.BO) {
	// mRenderer.setYAxisMin(0);// ����y����Сֵ��0
	// mRenderer.setYAxisMax(110);
	// xAxisMax = 150;
	// mRenderer.setXAxisMax(xAxisMax);
	// xSeries.setTitle("Ѫ������ͼ");
	// }
	// }
	// int count = 0;
	// int model = 3;
	// if (curvType == CurvType.BO)
	// model = 2;
	// for (int each : data) {
	// count++;
	// if (count % model != 1)
	// continue;
	// boWaveIndex += 1;
	// if (boWaveIndex > xAxisMax) {
	// boWaveIndex -= xAxisMax;
	// }
	// xSeries.add(boWaveIndex, each);
	// }
	// if (boWaveView != null)
	// boWaveView.repaint();
	// }

	/**
	 * ����Ѫѹ�����������ʾ�����������쳣�������
	 * 
	 * @param bpResult
	 */
	// private static void processBpResult(int[] bpResult) {
	// if (bpResult[0] == PC300.ERROR_RESULT) {// �����������
	// Log.i(TAG, "ERROR_RESULT:" + bpResult[1]);
	// String text = new String();
	// switch (bpResult[1]) {
	// case PC300.ILLEGAL_PULSE:
	// text = "����������Ч������";
	// break;
	// case PC300.BAD_BOUND:
	// text = "����û�а��";
	// break;
	// case PC300.ERROR_VALUE:
	// text = "�����ֵ����";
	// break;
	// default:
	// text = "δ֪����";
	// }
	// T.showLong(context, text);
	// } else {
	// String pulseTag = bpResult[0] == 1 ? "���ʲ���" : "��������";
	// T.showLong(context, pulseTag);
	// HighBpEditText.setText(Integer.valueOf(bpResult[1]).toString());
	// lowBpEditText.setText(Integer.valueOf(bpResult[2]).toString());
	// setPulseText(String.valueOf(bpResult[3]), PULSE_FROM_BP);
	// }
	// measureBpButton.setText("���²���");// ������Ϻ��ֿ��Բ���
	// stop = false;
	// }

	/**
	 * ��Ѫ������ͼ
	 * 
	 * @return
	 */
	public View lineView() {
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer xRenderer = new XYSeriesRenderer();// (������һ���߶���)
		mDataset.addSeries(xSeries);
		// ����ͼ���X��ĵ�ǰ����
		mRenderer
				.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		// mRenderer.setYTitle("Ѫ��ֵ");// ����y��ı���
		mRenderer.setAxisTitleTextSize(15);// ����������ı���С
		// mRenderer.setChartTitle("Ѫ������ͼ");// ����ͼ�����
		mRenderer.setChartTitleTextSize(15);// ����ͼ��������ֵĴ�С
		// mRenderer.setLabelsTextSize(18);// ���ñ�ǩ�����ִ�С
		mRenderer.setXLabels(30);//
		mRenderer.setYLabels(20);//
		mRenderer.setLegendTextSize(20);// ����ͼ���ı���С
		mRenderer.setPointSize(1f);// ���õ�Ĵ�С
		mRenderer.setXAxisMin(0);// ����y����Сֵ
		mRenderer.setXAxisMax(300);
		mRenderer.setYAxisMin(0);// ����y����Сֵ
		mRenderer.setYAxisMax(110);
		mRenderer.setShowGrid(true);// ��ʾ����
		mRenderer.setMargins(new int[] { 1, 15, 1, 1 });// ������ͼλ��
		xRenderer.setColor(Color.BLUE);// ������ɫ
		xRenderer.setPointStyle(PointStyle.CIRCLE);// ���õ����ʽ
		xRenderer.setFillPoints(true);// ���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
		xRenderer.setLineWidth(2);// �����߿�
		mRenderer.addSeriesRenderer(xRenderer);
		mRenderer.setMarginsColor(Color.WHITE);// ��������Ϊ��ɫ
		mRenderer.setPanEnabled(false, false);// ���ò����ƶ�����
		mRenderer.setShowLabels(false);
		boWaveView = ChartFactory.getCubeLineChartView(this, mDataset,
				mRenderer, 0.0f);
		boWaveView.setBackgroundColor(Color.WHITE);
		return boWaveView;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// bluetoothService.stop();//
			// �˳�activity��ر���������
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void dialog(String msg) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("��������:" + msg + "\n�Ƿ��ϴ���");
		builder.setTitle("�ĵ�������");

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					uploadEcg();
				} catch (Exception e) {
					e.printStackTrace();
					handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE,
							0, WebService.PATH_ECG).sendToTarget();
				}

				T.showShort(context, "��̨��ʼ�ϴ�");

			}
		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/***
	 * �ϴ�Ѫѹ\Ѫ��\����
	 * 
	 * @throws JSONException
	 */
	protected void upload() throws JSONException {
		String dbp = lowBpEditText.getText().toString();// ����ѹ
		if (dbp.length() > 0) {// ����ѹ�����ݣ������ϴ�Ѫѹֵ
			String sbp = HighBpEditText.getText().toString();// ����ѹ
			String pulse = pulseEditText.getText().toString();// ����
			JSONObject data = getDefaltAttrs();
			data.put(Tables.SBP, sbp);
			data.put(Tables.DBP, dbp);
			data.put(Tables.PULSE, pulse);
			uploadInBack(data, WebService.PATH_BP);
		}
		String temp = tempEditText.getText().toString();
		if (temp.length() > 0) {// ����������
			JSONObject data = getDefaltAttrs();
			data.put(Tables.TEMP, temp);
			uploadInBack(data, WebService.PATH_TEMP);
		}
		String bo = boEditText.getText().toString();
		if (bo.length() > 0) {// Ѫ��������
			JSONObject data = getDefaltAttrs();
			data.put(Tables.BO, bo);
			uploadInBack(data, WebService.PATH_BO);
		}
	}

	/***
	 * �ϴ��ĵ�ͼ
	 * 
	 * @throws Exception
	 */
	protected void uploadEcg() throws Exception {
		JSONObject data = new JSONObject();
		JSONArray array = new JSONArray();
		for (EcgFrame each : PC300.ecgFrames) {
			if (each.getFramNum() != 0)
				for (int v : each.getEcg())
					array.put(v);
		}
		// originalValue��ԭʼֵ�������0.5
		// originalUnit��ԭʼ��λ�������scaleUnitһ����uV
		// scaleUnit����ԭ��һ������uV
		// scaleValue�������0.1���̶�ֵ�������Ұ�5���Ŵ���ʾ
		data.put("data", array);
		data.put("incrementValue", "0.006666667");
		data.put("incrementUnit", "17");
		data.put("scaleUnit", "28");
		data.put("originalValue", "0.5");
		data.put("originalUnit", "28");
		data.put("scaleValue", "0.05");
		data.put("checkTime",
				TimeHelper.getStringTime(PC300.ecgFrames.get(0).getTime()));
		data.put(
				"checkTimeEnd",
				TimeHelper.getStringTime(PC300.ecgFrames.get(
						PC300.ecgFrames.size() - 1).getTime()));
		uploadInBack(data, WebService.PATH_ECG);

	}

	/***
	 * ��ȡ����������Ŀ���еļ�������
	 * 
	 * @return
	 * @throws JSONException
	 */
	private JSONObject getDefaltAttrs() throws JSONException {
		String time = TimeHelper.getCurrentTime();
		JSONObject data = new JSONObject();
		data.put(WebService.TIME, time);
		data.put(WebService.DEVICENAME, btName);
		data.put(WebService.DEVICEMAC, btMac);
		return data;
	}

	/***
	 * �ϴ�����,data���ɾ�����Ŀָ��
	 * 
	 * @param data
	 * @param path
	 * @throws JSONException
	 */
	public static void upload(JSONObject data, String path) {
		try {
			JSONObject para = new JSONObject();
			String idCard = BaseActivity.getUser().getCardNo();
			para.put(WebService.CARDNO, idCard);
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			para.put(WebService.DATA, data);
			para.put(WebService.CRC, "");
			JSONObject result = WebService.postConenction(para, path);
			int status = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(UPLOAD_RESULT, status, 0, path)
					.sendToTarget();
		} catch (Exception e) {
			handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE, 0, path)
					.sendToTarget();
		}
	}

	/***
	 * ���ú�̨�߳��ϴ�����
	 * 
	 * @param data
	 * @param path
	 */
	private void uploadInBack(final JSONObject data, final String path) {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				upload(data, path);
			}
		}).start();
	}

}
