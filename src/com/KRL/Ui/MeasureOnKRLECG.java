package com.KRL.Ui;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.KRL.Bluetooth.Bluetooth;
import com.KRL.Bluetooth.BluetoothConnectThread;
import com.KRL.Bluetooth.BluetoothConnectThread.BluetoothConnectionState;
import com.KRL.Bluetooth.BluetoothDataParser;
import com.KRL.Device.KRLECG;
import com.KRL.Staticecg.ECGSurfaceView;
import com.KRL.Staticecg.StaticApp;
import com.KRL.Tools.Tools;
import com.KRL.service.BackService;
import com.KRL.service.IBackService;
import com.health.BaseActivity;
import com.health.bean.User;
import com.health.bluetooth.BluetoothListActivity;
import com.health.database.Cache;
import com.health.database.DatabaseService;
import com.health.util.T;
import com.health.web.WebService;

/**
 * ����Ѫѹ
 * 
 */
public class MeasureOnKRLECG extends Activity {

	private static EditText HighBpEditText = null;// ��ѹ�ı���
	private static EditText lowBpEditText = null;// ��ѹ�ı���
	private static EditText pulseEditText = null;// ����
	private static EditText boEditText = null;// Ѫ��
	private static EditText tempEditText = null;// ����
	// private static Button measureBpButton = null;// ���Բ���Ѫѹ��ť
	// private static Button stableBoButton = null;// ���Բ���Ѫ����ť
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
	private static final String TAG = "MeasureOnKRLECG";
	// private static BluetoothService bluetoothService = null;
	// private static KrlEcgHandler handler = null;

	private static String btName = "KRL_TMDA";// ��������
	private static String btMac = null;// ��������
	private static Context context;
	private static boolean stop = false;
	private static boolean ecg_stop = false;
	// private static LinearLayout graphLayout;// װѪ��ͼ�Ĳ���
	private static GraphicalView boWaveView;// Ѫ��ͼ
	// private static XYSeries xSeries;
	// private static XYMultipleSeriesRenderer mRenderer;
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

	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothConnectThread mConnectThead = null;
	private BluetoothDataParser mDataParser = null;
	private DisplayMetrics mMetric = null;
	private ECGSurfaceView mEcgView = null;
	private TextView mTimer;
	private boolean mStart = false;
	private IBackService service;
	private Cache cache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regist();
		// ���õ�ǰactivity���� �������setContentView֮ǰ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.measure_on_krlecg);
		findId();
		initECGView();
		context = this;
		dbService = new DatabaseService(context);
		cache = new Cache(context);
		setVisibility();
		// bluetoothService = BluetoothService.getService(handler, true);// �첽��
		// bluetoothService.setSleepTime(20);
		setOnClickListener();
		connectKrlEcg();
		setConnectState();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// xSeries = new XYSeries("");
		// graphLayout.addView(lineView());
	}
	
	private void initECGView() {
		mTimer = (TextView) findViewById(R.id.textView_timer);
		mDataParser = new BluetoothDataParser(1000 * 2 * 12, 27, 12,
				StaticApp.getinstance().mDataMager.mECG);
		mMetric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mMetric);
		mEcgView = (ECGSurfaceView) findViewById(R.id.surfaceView_Sampling);
		mEcgView.mDataSegment = StaticApp.getinstance().mDataMager.mECG;
		mEcgView.init_canvas_param(mMetric, mEcgView.mDataSegment.mDesc,
				mDataParser.GetShowBuffer());
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mDataParser.mDataSegment.mDataSize = 0;
	}

	/**
	 * ���ü�����
	 */
	private void setOnClickListener() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == findButton)
					startDeviceListActivity();// ������������activity
				else if (v == measureEcgButton) {
					takeMeasurEcg();
				} else if (v == uploadButton) {
					// T.showShort(context, "��̨��ʼ�ϴ�");
					// try {
					// // upload();// �ϴ�����
					// } catch (JSONException e) {
					// T.showLong(context, "�������!");
					// e.printStackTrace();
					// }

					try {
						if (null != service)
							service.uploadEcgFileData();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
			}

		};
		// measureBpButton.setOnClickListener(onClickListener);
		// stableBoButton.setOnClickListener(onClickListener);
		measureEcgButton.setOnClickListener(onClickListener);
		uploadButton.setOnClickListener(onClickListener);
		findButton.setOnClickListener(onClickListener);
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
	private void setVisibility() {
		int status;
		if (mConnectThead != null && mConnectThead.isConnected()) {
			status = View.VISIBLE;// ����ʱ���ÿɼ�
		} else {
			status = View.INVISIBLE;// δ����ʱ���ò��ɼ�
		}
		measureEcgButton.setVisibility(status);
	}

	/**
	 * ��ʼ��id
	 */
	private void findId() {
		HighBpEditText = (EditText) findViewById(R.id.hp);
		lowBpEditText = (EditText) findViewById(R.id.lp);
		pulseEditText = (EditText) findViewById(R.id.mb);
		boEditText = (EditText) findViewById(R.id.bo);
		tempEditText = (EditText) findViewById(R.id.temp);
		// measureBpButton = (Button) findViewById(R.id.start_stop_bp_btn);
		// stableBoButton = (Button) findViewById(R.id.bo_stable_button);
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
		// graphLayout = (LinearLayout) this.findViewById(R.id.bo_image_view);
	}

	/**
	 * ��������״̬����ʾ
	 */
	private void setConnectState() {
		if (mConnectThead == null) {
			statusView.setText(R.string.unconnect);
			return;
		}
		int state = mConnectThead.getConnectionState();
		if (BluetoothConnectionState.CONNECTING.ordinal() == state) {
			statusView.setText(R.string.connecting);
		} else if (BluetoothConnectionState.CONNECTED.ordinal() == state) {
			statusView.setText("������");
		} else if (BluetoothConnectionState.IOEXCEPTION.ordinal() == state) {
			statusView.setText("��������쳣");
		} else if (BluetoothConnectionState.DISCONNECTED.ordinal() == state) {
			statusView.setText("�ѶϿ�");
		} else if (BluetoothConnectionState.INTERRUPTEDEXCEPTION.ordinal() == state) {
			statusView.setText("���ж�");
		}
	}

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
	private void takeMeasurEcg() {
		if (mConnectThead.isConnected()) {
			// if (ecg_stop == false) {
			mEcgView.nStartPos = 0;
			mEcgView.nLastPos = 0;
			mStart = true;
			mDataParser.mDataSegment
					.SetSampling(StaticApp.getinstance().mSamplingTime);
//			mTimer.setText("");
//			mConnectThead.SendStop();
			mEcgView.init_canvas_param(mMetric, mDataParser.mDataSegment.mDesc,
					mDataParser.GetShowBuffer());
			mDataParser.mDataSegment.mStartTime = Tools.GetCurrentTime();
			mDataParser.mStartSampling = true;
			mDataParser.clear();
			mConnectThead.write(KRLECG.ECG);
			measureEcgButton.setText("���²���");
			uploadButton.setEnabled(false);
			uploadButton.setClickable(false);
			// } else {
			// mConnectThead.write(KRLECG.STOP);
			// measureEcgButton.setText("��ʼ�ĵ����");
			// }
			// ecg_stop = !ecg_stop;
		}
	}

	/*
	 * ��ʼ��������ʾ����
	 */
	protected void initDataTextEdit() {
		HighBpEditText.setText("");
		lowBpEditText.setText("");
		setPulseText("", PULSE_FROM_UNKOWN);
	}

	/**
	 * ���ӵ�KrlEcg�豸
	 */
	private void connectKrlEcg() {
		String address = cache.getDeviceAddress(Cache.KRLECG);// �����ϴν���������
		if (null != address && !"".equals(address)) {
			connectKrlEcg(address);
		} else {
			Toast.makeText(this, "���Ѿ��󶨵�KRL-ECG�豸�����Բ����豸...",
					Toast.LENGTH_SHORT).show();
		}
		// saveDeviceAddress
	}

	/**
	 * ������������PC300�豸
	 * 
	 * @param address
	 */
	private void connectKrlEcg(String address) {
		// T.show(this, "���������豸�����Ժ󡣡���", Toast.LENGTH_SHORT);
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device != null) {
			if (mConnectThead != null) {
				mConnectThead.cancel();
			}
			mConnectThead = new BluetoothConnectThread(device, mHandler, 0,
					mDataParser, mBluetoothAdapter);
			mConnectThead.start();
			Log.e(TAG, "connectKrlEcg:" + mConnectThead.getId());
			if (null != service) {
				try {
					service.setDeviceInfo(Cache.KRLECG, address);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
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
				cache.saveDeviceAddress(Cache.KRLECG, address);
				connectKrlEcg(address);
			}
			break;
		}
	}

	// private class KrlEcgHandler extends Handler {
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
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
	//
	// case BluetoothService.MESSAGE_WRITE:
	// // byte[] writeBuf = (byte[]) msg.obj;
	//
	// break;
	// case BluetoothService.MESSAGE_READ:
	// byte[] readBuf = (byte[]) msg.obj;
	// Log.i(TAG, Arrays.toString(readBuf));
	// processReadData(readBuf);
	// if (DEBUG) {
	// // Log.d(TAG, readMessage);
	// Log.d(TAG, Arrays.toString(readBuf));
	// }
	// break;
	// case BluetoothService.MESSAGE_TOAST:
	// T.showShort(context,
	// msg.getData().getString(BluetoothService.TOAST));
	// break;
	// case BluetoothService.MESSAGE_DEVICE:
	// btName = msg.getData().getString(BluetoothService.DEVICE_NAME);
	// String address = msg.getData().getString(
	// BluetoothService.DEVICE_ADDRESS);
	// cache.saveDeviceAddress(Cache.KRLECG, address);// �����ַ,�Ա��´��Դ�����
	// break;
	// case UPLOAD_RESULT:
	// int status = msg.arg1;
	// String item = (String) msg.obj;
	// if (WebService.PATH_BP.equals(item)) {
	// setImageView(hBpImageView, status);
	// setImageView(lBpImageView, status);
	// setImageView(pulseImageView, status);
	// } else if (WebService.PATH_BO.equals(item))
	// setImageView(boImageView, status);
	// else if (WebService.PATH_TEMP.equals(item))
	// setImageView(tempImageView, status);
	// else if (WebService.PATH_ECG.equals(item)) {
	// setImageView(ecgImageView, status);
	// if (WebService.OK == status)
	// T.showLong(context, "�ĵ��ϴ��ɹ�");
	// else
	// T.showLong(context, "�ĵ��ϴ�ʧ��");
	// }
	// break;
	// }
	//
	// }
	// };

	// /**
	// * ������������,��������ݿ��ܰ�������Э�����ݣ�����Э��İ�ͷ����������ݷָ
	// * ����ͬһ���͵�����ֻ�������µ���У����ȷ��һ��,���ÿһ���͵�������Ӧ������
	// *
	// * @param buffer
	// *
	// */
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
	// // for (byte[] data : datas) {
	// // Log.i("TOKEN_BO_WAVE", Arrays.toString(data));
	// // int[] value = pc300.getBoWave(data);
	// // if (null != value)
	// // updateWaveImage(value, CurvType.BO);
	// // }
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
	// // for (byte[] each : datas) {
	// // EcgFrame fram = pc300.getEcgFram(each);
	// // if (null != fram) {
	// // pc300.addEcgFrameAndSort(fram);
	// // if (ecgFrameNum == 0) {
	// // updateWaveImage(fram.getEcg(), CurvType.ECG);
	// // } else {
	// // EcgFrame nextFrame = pc300.getNextFrame(0);
	// // if (nextFrame != null) {
	// // ecgFrameNum = nextFrame.getFramNum();
	// // updateWaveImage(nextFrame.getEcg(),
	// // CurvType.ECG);
	// // }
	// // }
	// //
	// // if (fram.isDeviceOff() == true)
	// // T.showLong(context, "��������");
	// // }
	// // }
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
	private static void setPulseText(String value, int source) {
		pulseSource = source;
		pulseEditText.setText(value);

	}

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

	// /**
	// * ����Ѫѹ�����������ʾ�����������쳣�������
	// *
	// * @param bpResult
	// */
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
	// public View lineView() {
	// XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// mRenderer = new XYMultipleSeriesRenderer();
	// XYSeriesRenderer xRenderer = new XYSeriesRenderer();// (������һ���߶���)
	// mDataset.addSeries(xSeries);
	// // ����ͼ���X��ĵ�ǰ����
	// mRenderer
	// .setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
	// // mRenderer.setYTitle("Ѫ��ֵ");// ����y��ı���
	// mRenderer.setAxisTitleTextSize(15);// ����������ı���С
	// // mRenderer.setChartTitle("Ѫ������ͼ");// ����ͼ�����
	// mRenderer.setChartTitleTextSize(15);// ����ͼ��������ֵĴ�С
	// // mRenderer.setLabelsTextSize(18);// ���ñ�ǩ�����ִ�С
	// mRenderer.setXLabels(30);//
	// mRenderer.setYLabels(20);//
	// mRenderer.setLegendTextSize(20);// ����ͼ���ı���С
	// mRenderer.setPointSize(1f);// ���õ�Ĵ�С
	// mRenderer.setXAxisMin(0);// ����y����Сֵ
	// mRenderer.setXAxisMax(300);
	// mRenderer.setYAxisMin(0);// ����y����Сֵ
	// mRenderer.setYAxisMax(110);
	// mRenderer.setShowGrid(true);// ��ʾ����
	// mRenderer.setMargins(new int[] { 1, 15, 1, 1 });// ������ͼλ��
	// xRenderer.setColor(Color.BLUE);// ������ɫ
	// xRenderer.setPointStyle(PointStyle.CIRCLE);// ���õ����ʽ
	// xRenderer.setFillPoints(true);// ���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
	// xRenderer.setLineWidth(2);// �����߿�
	// mRenderer.addSeriesRenderer(xRenderer);
	// mRenderer.setMarginsColor(Color.WHITE);// ��������Ϊ��ɫ
	// mRenderer.setPanEnabled(false, false);// ���ò����ƶ�����
	// mRenderer.setShowLabels(false);
	// boWaveView = ChartFactory.getCubeLineChartView(this, mDataset,
	// mRenderer, 0.0f);
	// boWaveView.setBackgroundColor(Color.WHITE);
	// return boWaveView;
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// bluetoothService.stop();//
			// �˳�activity��ر���������
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// protected void dialog(String msg) {
	// AlertDialog.Builder builder = new Builder(context);
	// builder.setMessage("��������:" + msg + "\n�Ƿ��ϴ���");
	// builder.setTitle("�ĵ�������");
	//
	// builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// try {
	// uploadEcg();
	// } catch (Exception e) {
	// e.printStackTrace();
	// handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE,
	// 0, WebService.PATH_ECG).sendToTarget();
	// }
	//
	// T.showShort(context, "��̨��ʼ�ϴ�");
	//
	// }
	// });
	//
	// builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	// }
	// });
	// builder.show();
	// }

	// /***
	// * �ϴ�Ѫѹ\Ѫ��\����
	// *
	// * @throws JSONException
	// */
	// protected void upload() throws JSONException {
	// String dbp = lowBpEditText.getText().toString();// ����ѹ
	// if (dbp.length() > 0) {// ����ѹ�����ݣ������ϴ�Ѫѹֵ
	// String sbp = HighBpEditText.getText().toString();// ����ѹ
	// String pulse = pulseEditText.getText().toString();// ����
	// JSONObject data = getDefaltAttrs();
	// data.put(Tables.SBP, sbp);
	// data.put(Tables.DBP, dbp);
	// data.put(Tables.PULSE, pulse);
	// uploadInBack(data, WebService.PATH_BP);
	//
	// }
	// String temp = tempEditText.getText().toString();
	// if (temp.length() > 0) {// ����������
	// JSONObject data = getDefaltAttrs();
	// data.put(Tables.TEMP, temp);
	// uploadInBack(data, WebService.PATH_TEMP);
	// }
	// String bo = boEditText.getText().toString();
	// if (bo.length() > 0) {// Ѫ��������
	// JSONObject data = getDefaltAttrs();
	// data.put(Tables.BO, bo);
	// uploadInBack(data, WebService.PATH_BO);
	// }
	// }

	// /***
	// * �ϴ��ĵ�ͼ
	// *
	// * @throws Exception
	// */
	// protected void uploadEcg() throws Exception {
	// JSONObject data = new JSONObject();
	// JSONArray array = new JSONArray();
	// for (EcgFrame each : PC300.ecgFrames) {
	// if (each.getFramNum() != 0)
	// for (int v : each.getEcg())
	// array.put(v);
	// }
	// // originalValue��ԭʼֵ�������0.5
	// // originalUnit��ԭʼ��λ�������scaleUnitһ����uV
	// // scaleUnit����ԭ��һ������uV
	// // scaleValue�������0.1���̶�ֵ�������Ұ�5���Ŵ���ʾ
	// data.put("data", array);
	// data.put("incrementValue", "0.006666667");
	// data.put("incrementUnit", "17");
	// data.put("scaleUnit", "28");
	// data.put("originalValue", "0.5");
	// data.put("originalUnit", "28");
	// data.put("scaleValue", "0.05");
	// data.put("checkTime",
	// TimeHelper.getStringTime(PC300.ecgFrames.get(0).getTime()));
	// data.put(
	// "checkTimeEnd",
	// TimeHelper.getStringTime(PC300.ecgFrames.get(
	// PC300.ecgFrames.size() - 1).getTime()));
	// uploadInBack(data, WebService.PATH_ECG);
	//
	// }

	// /***
	// * ��ȡ����������Ŀ���еļ�������
	// *
	// * @return
	// * @throws JSONException
	// */
	// private JSONObject getDefaltAttrs() throws JSONException {
	// String time = TimeHelper.getCurrentTime();
	// JSONObject data = new JSONObject();
	// data.put(WebService.TIME, time);
	// data.put(WebService.DEVICENAME, btName);
	// data.put(WebService.DEVICEMAC, btMac);
	// return data;
	// }

	// /***
	// * ���ú�̨�߳��ϴ�����
	// *
	// * @param data
	// * @param path
	// */
	// private void uploadInBack(final JSONObject data, final String path) {
	// new BackGroundThread(new BackGroundTask() {
	// @Override
	// public void process() {
	// upload(data, path);
	// }
	// }).start();
	// }

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO
			// �Զ����ɵķ������
			switch (msg.what) {
			case Bluetooth.MESSAGE_DATA:// �ĵ�����
				// Log.e(TAG, "MESSAGE_DATA");
				if (mEcgView != null) {
					mDataParser.mScreenWidth = mEcgView.mScreenWidthInCx;
				}
				// mDataParser.push(msg.getData().getByteArray(Bluetooth.MESSAGE_KEY_DATA),
				// msg.getData().getInt(Bluetooth.MESSAGE_KEY_SIZE));
				mDataParser.Parser();
				if (mEcgView != null) {
					mDataParser.mScreenWidth = mEcgView.mScreenWidthInCx;
					mEcgView.nEndPos = mDataParser.GetCurIndex();
					if (mStart == true) {
						if (mTimer != null) {
							mTimer.setText(String.format("��ʣ:%d��",
									mDataParser.mDataSegment.RemainTime()));
						}
					}
					if (mDataParser.mDataSegment.IsCompleted()
							&& mStart == true) {
						mStart = false;
						mTimer.setText("���ڴ���");
						// measureEcgButton.setText("��ʼ�ĵ����");
						// ecg_stop = !ecg_stop;
						// ֹͣ
						mConnectThead.write(KRLECG.STOP);
						Toast.makeText(MeasureOnKRLECG.this, "���ڱ������ݣ������ĵȴ�...",
								Toast.LENGTH_LONG).show();
						if (null != service) {
							try {
								User patient = BaseActivity.getUser();
								service.setPatientInfo(patient.getName(),
										patient.getCardNo(),
										patient.getBirthday(),
										patient.getSex(),
										patient.getNickName(),
										patient.getCardNo(),
										patient.getUserGuid(),
										patient.getMobile(), patient.getEmail());
								service.saveECGToFile();
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
			case Bluetooth.MESSAGE_CONNECTED: {
				String address = msg.getData().getString(
						Bluetooth.MESSAGE_KEY_BT_ADDRESS);
				int state = msg.getData()
						.getInt(Bluetooth.MESSAGE_KEY_BT_STATE);
				Log.e(TAG, "MESSAGE_CONNECTED:" + state);
				if (BluetoothConnectionState.CONNECTING.ordinal() == state) {
					T.show(MeasureOnKRLECG.this, "���������豸�����Ժ󡣡���",
							Toast.LENGTH_SHORT);
					statusView.setText("�豸������");
					measureEcgButton.setVisibility(View.INVISIBLE);
				} else if (BluetoothConnectionState.CONNECTED.ordinal() == state) {
					onDeviceConnected(address);
				} else if (BluetoothConnectionState.IOEXCEPTION.ordinal() == state) {
					T.show(MeasureOnKRLECG.this, "�Ͽ��豸����", Toast.LENGTH_SHORT);
					statusView.setText("�豸�����ѶϿ�");
					measureEcgButton.setVisibility(View.INVISIBLE);
				} else if (BluetoothConnectionState.DISCONNECTED.ordinal() == state) {
					T.show(MeasureOnKRLECG.this, "�Ͽ��豸����", Toast.LENGTH_SHORT);
					statusView.setText("�豸�����ѶϿ�");
					measureEcgButton.setVisibility(View.INVISIBLE);
				} else if (BluetoothConnectionState.INTERRUPTEDEXCEPTION
						.ordinal() == state) {
					T.show(MeasureOnKRLECG.this, "�Ͽ��豸����", Toast.LENGTH_SHORT);
					statusView.setText("�豸�����ѶϿ�");
					measureEcgButton.setVisibility(View.INVISIBLE);
				}
				break;
			}
			case Bluetooth.MESSAGE_CLOSED: {
				Toast.makeText(MeasureOnKRLECG.this, "�ɼ�ʧ�ܣ������²ɼ�",
						Toast.LENGTH_LONG).show();
				finish();
				break;
			}
			default:
				break;
			}
		}
	};

	private void onDeviceConnected(String address) {
		T.show(this, "�豸���ӳɹ�", Toast.LENGTH_SHORT);
		BluetoothDevice remoteDevice = mBluetoothAdapter
				.getRemoteDevice(address);
		if (null != remoteDevice) {
			statusView.setText("�Ѿ����ӵ�:" + remoteDevice.getName());
		}

		measureEcgButton.setVisibility(View.VISIBLE);
		StaticApp.getinstance().mMAC = address;
		if (null != service) {
			try {
				service.setDeviceInfo(Cache.KRLECG, address);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	// private void onDeviceDisConnected(String address) {
	//
	// }

	// //���ɲ�����Ϣ
	// DataRecord dr = new DataRecord();
	// dr.fromCursor(cursor);
	// NetworkPacketFactory.make_patient_info((byte) 1, (byte) 1, dr);
	//
	//
	// public void AutoUploadData()
	// {
	// int state = Tools.cheackNetConnection();
	// if (state <= 0 || StaticApp.getinstance().mDataMager.mSaving)
	// {
	// //Log.e(TAG, "���粻���û������ڱ�������");
	// return;
	// }
	// if (mConnectThread == null || !mConnectThread.IsConnected())
	// {
	// //Log.e(TAG, "û�����ӷ����������ڳ������ӷ�����...");
	// ConnectNetwork();
	// }
	// if (this.mUploadFiles.size() <= 0)
	// {
	// String sql = "select * from " + mDBAdapter.getTableName() + " where " +
	// PatientTableCol.SAMPLINGSATE + "=1";
	// Cursor cr = mDBAdapter.Query(sql, null);
	// if (cr != null && cr.getCount() > 0)
	// {
	// while (cr.moveToNext())
	// {
	// UploadFileDesc desc = new UploadFileDesc();
	// desc.fromCursor(cr);
	// if (desc.nDataID == 0)
	// {
	// desc = null;
	// continue;
	// }
	// boolean have = false;
	// for (int i = 0; i > this.mUploadFiles.size(); i++)
	// {
	// if (this.mUploadFiles.get(i).nDataID == desc.nDataID)
	// {
	// have = true;
	// break;
	// }
	// }
	// if (!have)
	// {
	// this.mUploadFiles.add(desc);
	// }
	// else
	// {
	// desc = null;
	// }
	// }
	// }
	// }
	// if (mUploadTask == null && mUploadFiles.size() > 0)
	// this.StartUploadFile();
	// }

	@Override
	protected void onDestroy() {
		Log.e(TAG, "onDestroy");
		unbindService(connection);
		unregisterReceiver(uploadResultReceiver);
		// TODO �Զ����ɵķ������

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mConnectThead != null) {
			Log.e(TAG, "onPause:" + mConnectThead.getId());
			mConnectThead.write(KRLECG.STOP);
			mConnectThead.cancel();
		}
	}

//	@Override
//	protected void onResume() {
//		// TODO �Զ����ɵķ������
//		super.onResume();
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
//	}

	private void regist() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BackService.UPLOAD_RESULT_ACTION);
		intentFilter.addAction(BackService.SAMPLING_RESULT_ACTION);
		registerReceiver(uploadResultReceiver, intentFilter);
		bindService(new Intent(this, BackService.class), connection,
				Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			service = IBackService.Stub.asInterface(arg1);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			service = null;
		}
	};

	// �㲥������ - �㲥�Ľ���
	private BroadcastReceiver uploadResultReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BackService.UPLOAD_RESULT_ACTION.equals(action)) {
				Bundle data = intent.getExtras();
				if (null != data) {
					int status = data.getInt("status");
					String item = data.getString("path");
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
				}
			} else if (BackService.SAMPLING_RESULT_ACTION.equals(action)) {
				// Bundle data = intent.getExtras();
				// if(null != data){
				// String occurdtm = data.getString("occurdtm");
				// String fileName = data.getString("fileName");
				// String filedesc = data.getString("filedesc");
				// int fileLength = data.getInt("fileLength");
				// int dataState = data.getInt("dataState");
				// int samplingstate = data.getInt("samplingstate");
				//
				// }
				boolean saveFileSuccess = intent.getBooleanExtra(
						"SaveFileSuccess", false);
				if (saveFileSuccess) {
					uploadButton.setEnabled(true);
					uploadButton.setClickable(true);
					mTimer.setText("����ɹ�");
				}else{
					mTimer.setText("����ʧ��");
				}
			}
		}
	};
}
