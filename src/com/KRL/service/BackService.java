package com.KRL.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.KRL.Data.DataManager;
import com.KRL.Data.DataRecord;
import com.KRL.Data.DeviceInfo;
import com.KRL.Data.ECGUser;
import com.KRL.Data.PatientInfo;
import com.KRL.Data.ServerInfo;
import com.KRL.LZMA.ICodeProgress;
import com.KRL.Network.MESSAGE;
import com.KRL.Network.NetworkConnectThread;
import com.KRL.Network.NetworkConnectThread.ConnectionState;
import com.KRL.Network.NetworkPacket;
import com.KRL.Network.NetworkPacketFactory;
import com.KRL.Staticecg.StaticApp;
import com.KRL.Staticecg.UploadFileDesc;
import com.KRL.Tools.Tools;
import com.health.BaseActivity;
import com.health.database.Tables;
import com.health.util.T;
import com.health.util.TimeHelper;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;

public class BackService extends Service {
	private final String TAG = "BackService";
	public static final String ACTION = "com.krl.service.BackService";
	public static final String UPLOAD_RESULT_ACTION = "com.krl.upload.RESULT";
	public static final String SAMPLING_RESULT_ACTION = "com.krl.sampling.RESULT";
	private NetworkConnectThread connectThread = null;
	private ServerInfo ecgServerInfo = null;
	private ECGUser ecgUserInfo = null;
	private UploadFileDesc mFileDesc = null;
	private PatientInfo patientInfo = null;
	private DeviceInfo deviceInfo = null;
	private final int UPLOAD_RESULT = 0x10040;
	private String fileName = null;
	private DataRecord curDataRecord;
	private ProgressNotify loginDialog = null;

	// public DataManager mDataMager = new DataManager();

	public IBinder onBind(Intent intent) {
		// String serviceName = intent.getAction();
		// if (serviceName == null
		// || serviceName.equalsIgnoreCase(SipManager.INTENT_SIP_SERVICE)) {
		return binder;
	}

	private final IBackService.Stub binder = new IBackService.Stub() {

		@Override
		public void loginECGServer() throws RemoteException {
			loginEcgServer();
		}

		@Override
		public void setECGLoginAccount(String account, String pwd)
				throws RemoteException {
			ecgUserInfo = new ECGUser(account, pwd);
		}

		@Override
		public void setECGServerInfo(String ip, int port)
				throws RemoteException {
			if (null != ecgServerInfo) {
				String oldIp = ecgServerInfo.getIp();
				int oldPort = ecgServerInfo.getPort();
				if (!oldIp.equals(ip) || oldPort != port) {
					ecgServerInfo.setIp(ip);
					ecgServerInfo.setPort(port);
					saveECGServerInfoToSp(ip, port);
				}
			}
		}

		@Override
		public ServerInfo getECGServerInfo(ServerInfo info)
				throws RemoteException {
			return getECGServerInfoFromSp(info);
		}

		@Override
		public boolean sendMsgToEcgServer(byte[] data) throws RemoteException {
			return writeToEcgServer(data);
		}

		@Override
		public void connectECGDevice(String btAddress) throws RemoteException {

		}

		@Override
		public void saveECGToFile() throws RemoteException {
			saveEcgFile();
		}

		@Override
		public void setPatientInfo(String name, String cardNO, String birthDay,
				String sex, String nickName, String customerGuid,
				String userGuid, String mobile, String email)
				throws RemoteException {
			patientInfo = new PatientInfo(name, cardNO, birthDay, sex,
					nickName, customerGuid, userGuid, mobile, email,
					Tools.GetCurrentTime(), Tools.GetCurrentTime());
		}

		@Override
		public void setDeviceInfo(String deviceName, String deviceMac)
				throws RemoteException {
			deviceInfo = new DeviceInfo(deviceName, deviceMac);
		}

		@Override
		public void downloadEcgFile(String fileUrl) throws RemoteException {
			new DownloadTask().execute(fileUrl);
		}

		@Override
		public void uploadEcgFileData() throws RemoteException {
			// if (null != connectThread && connectThread.hasLogin()) {
			// loginECGServer();
			// }else{
			//
			// }
			commitPatientInfo();
		}

		// @Override
		// public boolean connectECGDevice(String macAddr) throws
		// RemoteException {
		// BluetoothDevice device = BluetoothAdapter.getDefaultAdapter()
		// .getRemoteDevice(macAddr);
		// btConnectThead = new BluetoothConnectThread(device, mHandler, 0,
		// mDataParser);
		// btConnectThead.start();
		//
		// BluetoothConnectThread.KrlEcgPersistWriter persistWriter = new
		// BluetoothConnectThread.KrlEcgPersistWriter(
		// btConnectThead, KRLECG.BATTERY, 3000);
		// persistWriter.start();// ��������
		// return false;
		// }
	};

	private void loginEcgServer() {
		String ip = ecgServerInfo.getIp();
		int port = ecgServerInfo.getPort();

		if ("".equals(ip) || port < 0) {
			T.show(BackService.this, "������IP�Ͷ˿ں�Ϊ��", Toast.LENGTH_SHORT);
			return;
		}

		if (null == ecgUserInfo) {
			T.show(BackService.this, "�û���ϢΪ��", Toast.LENGTH_SHORT);
			return;
		}

		String account = ecgUserInfo.getAccount();
		String pwd = ecgUserInfo.getPassWord();

		if (null != connectThread) {
			connectThread.cancel();
		}
		connectThread = new NetworkConnectThread(mHandler, null);
		connectThread.setServerInfo(ip, port);
		connectThread.setLoginAccount(account, pwd);
		connectThread.start();
		Log.e(TAG, "loginECGServer");
	}

	private boolean writeToEcgServer(byte[] data) {
		boolean result = false;
		if (null != connectThread && connectThread.hasLogin()) {
			result = connectThread.write(data, 0, data.length);
		} else {
			// T.show(BackService.this, "�������ѶϿ����������µ�½", Toast.LENGTH_SHORT);
			loginDialog = new ProgressNotify(BackService.this);
			loginDialog.showWaitingDialog("��¼��", "���ڵ�½����ȴ�������");
			loginEcgServer();
		}
		return result;
	}

	private void saveECGServerInfoToSp(String ip, int port) {
		getSharedPreferences("serverinfo", Context.MODE_PRIVATE).edit()
				.putString("ecg_server_ip", ip).putInt("ecg_server_port", port)
				.commit();
	}

	private ServerInfo getECGServerInfoFromSp(ServerInfo info) {
		SharedPreferences mSp = getSharedPreferences("serverinfo",
				Context.MODE_PRIVATE);
		String ip = mSp.getString("ecg_server_ip", "ecg.koanruler.com");
		// String ip = mSp.getString("ecg_server_ip", "192.168.1.24");
		int port = mSp.getInt("ecg_server_port", 8585);
		if (null == info) {
			info = new ServerInfo(ip, port);
		} else {
			info.setIp(ip);
			info.setPort(port);
		}
		return info;
	}

	public void onCreate() {
		// ��ȡĬ�ϵ�ip�Ͷ˿�
		ecgServerInfo = getECGServerInfoFromSp(ecgServerInfo);
		// ��ȡĬ�ϵ�½���û���Ϣ
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE.MESSAGE_CONNECT:
				int state = msg.getData().getInt(MESSAGE.MESSAGE_KEY_CONNECT);
				Log.e(TAG, "state:" + state);
				if (state == ConnectionState.Connected.ordinal()) {
					T.show(BackService.this, "ECG���������ӳɹ�", Toast.LENGTH_SHORT);
				} else if (state == ConnectionState.SocketTimeout.ordinal()) {
					T.show(BackService.this, "ECG���������ӳ�ʱ", Toast.LENGTH_SHORT);
				} else if (state == ConnectionState.UnknownHost.ordinal()) {
					T.show(BackService.this, "ECG������δ֪�ķ�������ַ",
							Toast.LENGTH_SHORT);
				} else if (state == ConnectionState.IOException.ordinal()) {
					T.show(BackService.this, "ECG�����������ѶϿ�", Toast.LENGTH_SHORT);
				} else if (state == ConnectionState.DisConnected.ordinal()) {
					T.show(BackService.this, "ECG�������˳�����", Toast.LENGTH_SHORT);
				}
				break;
			case MESSAGE.MESSAGE_LOGIN: {
				boolean hasLogin = msg.getData().getBoolean(
						MESSAGE.MESSAGE_KEY_LOGIN);
				if (hasLogin == true) {
					int mCenterID = msg.getData().getInt(
							MESSAGE.MESSAGE_KEY_CENTERID);
					int mCheckstationId = msg.getData().getInt(
							MESSAGE.MESSAGE_KEY_CHECKSTATIONID);
					if (null != ecgUserInfo) {
						ecgUserInfo.setCenterId(mCenterID);
						ecgUserInfo.setCheckStationId(mCheckstationId);
						StaticApp.getinstance().mCenterID = mCenterID;
						StaticApp.getinstance().mCheckstationId = mCheckstationId;
						Log.e(TAG, "mCenterID:" + mCenterID);
						Log.e(TAG, "mCheckstationId:" + mCheckstationId);
						Toast.makeText(BackService.this, "ECG��������¼�ɹ�",
								Toast.LENGTH_SHORT).show();
					}
					if (null != connectThread) {
						connectThread.setLogin();
					}
				} else {
					Toast.makeText(BackService.this, "ECG��������¼ʧ�ܣ��ʺŻ��������",
							Toast.LENGTH_SHORT).show();
				}
				if (null != loginDialog && loginDialog.isShowing()) {
					loginDialog.dismissFileStatusDialog();
				}
				break;
			}
			case MESSAGE.MESSAGE_SYNC_PATIENT_INFO: {
				// String lastSyncTime = msg.getData().getString(
				// MESSAGE.MESSAGE_KEY_SYNC_PATIENT_INFO);
				// Properties btProper = StaticApp.getinstance().loadConfig();
				// btProper.put(
				// Staticecg.CONFIG_LAST_SYNC_TIME
				// + StaticApp.getinstance().mCheckstationId,
				// lastSyncTime);
				// StaticApp.getinstance().saveConfig(btProper);
				// if (connectThread != null)
				// connectThread.cancel();
				// connectThread = null;
				// System.gc();
				// btProper.put(Staticecg.CONFIG_CENTERID,
				// String.valueOf(StaticApp.getinstance().mCenterID));
				// btProper.put(Staticecg.CONFIG_CHECKSTATIONID,
				// String.valueOf(StaticApp.getinstance().mCheckstationId));
				// StaticApp.getinstance().saveConfig(btProper);
				// Toast.makeText(Login_Activity.this, "��¼�ɹ�",
				// Toast.LENGTH_SHORT)
				// .show();
				// if (mMainRun) {
				// setResult(Activity.RESULT_OK);
				// Login_Activity.this.finish();
				// } else {
				// Intent intent = new Intent();
				// intent.setClass(Login_Activity.this, Main_Activity.class);
				// startActivity(intent);
				// Login_Activity.this.finish();
				// }
				break;
			}
			case MESSAGE.MESSAGE_PATIENT_INFO:
				Log.e(TAG, "MESSAGE_PATIENT_INFO");
				byte[] record = msg.getData().getByteArray("DataRecord");
				DataRecord dr = new DataRecord();
				dr.fromBytes(record, 0);
				// ��ʼ��ѯ�ļ���Ϣ
				// nPersonDataID
				// queryEcgFileInfo(dr);
				commitEcgFileInfo(dr);
				break;
			case MESSAGE.MESSAGE_UPLOAD_FILE: {
				int param = msg.getData().getInt(
						MESSAGE.MESSAGE_KEY_UPLOAD_FILE);
				int res = msg.getData().getInt(MESSAGE.MESSAGE_KEY_RESULT);
				Log.e(TAG, "param:" + param);
				Log.e(TAG, "res:" + res);
				switch (param) {
				// ��ѯ�ļ�
				case 1: {
					// Toast.makeText(BackService.this, "�����ϴ��ļ�",
					// Toast.LENGTH_SHORT).show();
					// if (res == 1) {
					// commitEcgFileInfo();
					// } else {
					//
					// }
					break;
				}
				// ��ʼ�ϴ��ļ�
				case 2: {
					uploadEcgFile();
					break;
				}
				case 3: {
					break;
				}
				// �ļ��ϴ�����
				case 4: {
					// 1:chenggong 0:sibai
					Bundle data = msg.getData();
					if (data != null) {
						int result = data.getInt(MESSAGE.MESSAGE_KEY_RESULT);
						if (result == 1) {
							Log.e(TAG, "uploadEcgData to pingantong");
							String pdfUrl = "http://" + ecgServerInfo.getIp()
									+ ":18080/staticeecgreportdata/"
									+ fileName.substring(0, 6) + "/"
									+ fileName.substring(6, 8) + "/" + fileName
									+ ".pdf";
							String srcUrl = "http://" + ecgServerInfo.getIp()
									+ ":18080/staticeecgreportdata/"
									+ fileName.substring(0, 6) + "/"
									+ fileName.substring(6, 8) + "/" + fileName
									+ ".12necg.7z";
							// String pdfUrl =
							// "http://ecg.koanruler.com:18080/staticeecgreportdata/201404/25/20140425083611-0047-000027-0015830CBFEB.pdf";
							// String srcUrl =
							// "http://ecg.koanruler.com:18080/staticeecgreportdata/201404/25/20140425083611-0047-000027-0015830CBFEB.12necg.7z";
							try {
								uploadEcgData(srcUrl, pdfUrl);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {

						}
					}
					break;
				}
				default:
					break;
				}
				break;
			}
			default:
				break;
			}
		}
	};

	private void saveEcgFile() {
		Log.e(TAG, "saveEcgFile");
		SaveTask p = new SaveTask(StaticApp.getinstance().mDataMager);
		p.execute();
	}

	// �½�������Ϣ
	private void commitPatientInfo() {
		T.show(BackService.this, "�����ύ�û���Ϣ", Toast.LENGTH_SHORT);
		writeToEcgServer(NetworkPacketFactory.make_patient_info((byte) 1,
				(byte) 1, curDataRecord));
	}

	// private void queryEcgFileInfo(DataRecord dr) {
	// dr.nPersonDataID;
	// UploadFileDesc mFileDesc = new UploadFileDesc(int nID, int nDataID,
	// String patientname,
	// String occurdtm, String filedesp, String filename, int filelength);
	// // �ϴ��ļ�
	// writeToEcgServer(NetworkPacketFactory.make_upload_file_query(mFileDesc
	// .toBytes()));
	// }

	private void commitEcgFileInfo(DataRecord dr) {
		T.show(BackService.this, "�����ύ�ļ���Ϣ", Toast.LENGTH_SHORT);
		int nDataID = Tools.bytesToInt(dr.nPersonDataID, 0);
		String patientName = dr.getPatientInfo().getName();
		String occurdtm = Tools.GetCurrentTime();
		String filedesp = dr.getFileDesp();
		String fileName = dr.getFileName();
		int fileLength = dr.getFileLength();
		mFileDesc = new UploadFileDesc(-1, nDataID, patientName, occurdtm,
				filedesp, fileName, fileLength);
		// �ύ�ϴ��ļ���Ϣ
		byte[] buffer = NetworkPacketFactory.make_upload_file_start(mFileDesc
				.toBytes());
		writeToEcgServer(buffer);
	}

	private void uploadEcgFile() {
		T.show(BackService.this, "��ʼ�ϴ��ļ�...", Toast.LENGTH_SHORT);
		UploadTask mUploadTask = new UploadTask(
				connectThread.getOutputStream(), mFileDesc);
		mUploadTask.execute();
	}

	public class SaveTask extends AsyncTask<String, Integer, Boolean> implements
			ICodeProgress {
		DataManager mDataMgr = null;
		long mMax = 0;
		ProgressNotify mNotify = new ProgressNotify(BackService.this);
		int mProgress = 0;

		public SaveTask(DataManager dm) {
			mDataMgr = dm;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return mDataMgr.save(this);
		}

		@Override
		protected void onPreExecute() {
			// // Message msg =
			// // mHandler.obtainMessage(MESSAGE.MESSAGE_PATIENT_INFO);
			// String whereClause = PatientTableCol._ID + "="
			// + Tools.bytesToInt(mDataMgr.mDataRecord.nID, 0);
			// ContentValues cv = new ContentValues();
			// cv.put(PatientTableCol.SAMPLINGSATE, 3);
			// // int res = StaticApp.getinstance().getDB()
			// // .update(cv, whereClause, null);
			// // UpdateList();
			mNotify.showProgressDialog("����", "���ڱ������ݡ�����");
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// mNotify.visible(false);
			// if (!result) {
			// String whereClause = PatientTableCol._ID + "="
			// + Tools.bytesToInt(mDataMgr.mDataRecord.nID, 0);
			// ContentValues cv = new ContentValues();
			// cv.put(PatientTableCol.SAMPLINGSATE, 0);
			// // int res = StaticApp.getinstance().getDB()
			// // .update(cv, whereClause, null);
			// }
			// UpdateList();
		}

		@Override
		protected void onCancelled() {
			// TODO �Զ����ɵķ������
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO �Զ����ɵķ������
			if (mProgress != values[0])
				mNotify.changeDialogProgress(values[0]);
			mProgress = values[0];
		}

		@Override
		public void SetProgress(long inSize, long outSize) {
			// TODO �Զ����ɵķ������
			publishProgress((int) ((inSize / (double) mMax) * 100));
		}

		@Override
		public void finished() {
			// TODO �Զ����ɵķ������
		}

		@Override
		public void SetRange(long low, long hight) {
			// TODO �Զ����ɵķ������
			mMax = hight;
		}

		@Override
		public void onSaveSuccess(String occurdtm, String fileName,
				String filedesc, int fileLength, int dataState,
				int samplingstate) {
			mNotify.dismissFileStatusDialog();
			BackService.this.fileName = fileName;
			Log.e(TAG, "commitPatientInfo 2");
			// TODO �Զ����ɵķ������
			String strMAC = "11:22:33:44:55:66";
			BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
			if (ba != null && ba.getAddress() != null) {
				strMAC = ba.getAddress().replaceAll(":", "");
			}
			String curTime = Tools.GetCurrentTime("yyyyMMddHHmmss.SSS");
			// TODO:mCheckstationId��ֵ
			String account = curTime + "."
					+ String.format("%04d", ecgUserInfo.getCheckStationId())
					+ "." + strMAC;
			String time = Tools.GetCurrentTime();
			curDataRecord = new DataRecord(-1, account, "", "",
					ecgUserInfo.getCheckStationId(), fileName, fileLength,
					filedesc, "", occurdtm, 0, dataState, time);
			// TODO:johnny yang
			Log.e(TAG, "patientInfo.getName():" + patientInfo.getName());
			curDataRecord.setPatientInfo(patientInfo);

			// ���ͱ���ɹ��Ĺ㲥
			Intent intent = new Intent();
			intent.setAction(SAMPLING_RESULT_ACTION);
			// intent.putExtra("result_code", value);
			// Bundle bundle = new Bundle();
			// bundle.putString("occurdtm",occurdtm);
			// bundle.putString("fileName",fileName);
			// bundle.putString("filedesc",filedesc);
			// bundle.putInt("fileLength", fileLength);
			// bundle.putInt("dataState", dataState);
			// bundle.putInt("samplingstate", samplingstate);
			intent.putExtra("SaveFileSuccess", true);
			sendBroadcast(intent);
		}
	};

	public class UploadTask extends AsyncTask<String, Integer, Boolean> {
		static final int BUFFER_SIZE = 1024;
		ProgressBar mProgressbar = null;
		OutputStream mOutputStream = null;
		UploadFileDesc mFileDesc = null;
		ProgressNotify mNotify = new ProgressNotify(BackService.this);
		int mProgress = 0;

		public UploadTask(OutputStream outputstream, UploadFileDesc fileDesc) {
			mOutputStream = outputstream;
			mFileDesc = fileDesc;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO �Զ����ɵķ������
			byte[] buffer = new byte[BUFFER_SIZE];
			byte[] data = null;
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(mFileDesc.filePath);
				if (inputStream != null)
					inputStream.skip(mFileDesc.mStartPos);
			} catch (FileNotFoundException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
				Log.e(TAG, e1.toString());
				return false;
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				Log.e(TAG, e.toString());
				return false;
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
				Log.e(TAG, e1.toString());
				return false;
			}
			int filesize = mFileDesc.filelength;
			int count = mFileDesc.mStartPos;
			byte[] sendbuffer = new byte[BUFFER_SIZE
					+ NetworkPacket.HEADER_SIZE];
			int[] length = new int[1];
			int flag = 0;
			while (inputStream != null) {
				try {
					int bytes = inputStream.read(buffer);
					count += bytes;
					if (bytes == -1) {
						publishProgress(100);
						break;
					}
					if (bytes != BUFFER_SIZE) {
						NetworkPacketFactory.make_upload_file_data(buffer,
								bytes, sendbuffer, length);
						mOutputStream.write(sendbuffer, 0, length[0]);
						mOutputStream.flush();
						data = NetworkPacketFactory
								.make_upload_file_end(mFileDesc.toBytes());
						mOutputStream.write(data);
						mOutputStream.flush();
					} else {
						NetworkPacketFactory.make_upload_file_data(buffer,
								bytes, sendbuffer, length);
						mOutputStream.write(sendbuffer, 0, length[0]);
						mOutputStream.flush();
					}
					publishProgress((int) ((count / (float) filesize) * 100));
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, e.toString());
					return false;
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					Log.e(TAG, e.toString());
					return false;
				}
			}
			sendbuffer = null;
			length = null;
			if (mHandler != null) {
				Message msg = mHandler
						.obtainMessage(MESSAGE.MESSAGE_UPLOAD_FILE);
				Bundle extra = new Bundle();
				extra.putInt(MESSAGE.MESSAGE_KEY_DATA, mFileDesc.nDataID);
				msg.setData(extra);
				mHandler.sendMessage(msg);
			}
			return true;
		}

		@Override
		protected void onCancelled() {
			// TODO �Զ����ɵķ������
			super.onCancelled();
			// mUploadTask = null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO �Զ����ɵķ������
			mNotify.dismissFileStatusDialog();
		}

		@Override
		protected void onPreExecute() {
			mNotify.showProgressDialog("�ϴ��ļ�", "�ļ������ϴ��С�����");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO �Զ����ɵķ������
			if (mProgress != values[0])
				mNotify.changeDialogProgress(values[0]);
			// mProgress = values[0];
		}
	};

	public class ProgressNotify {
		private ProgressDialog progressDialog = null;

		public ProgressNotify(Context context) {
			progressDialog = new ProgressDialog(context);
		}

		public void showProgressDialog(String title, String message) {
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.getWindow().setType(
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			progressDialog.show();
		}

		public void showWaitingDialog(String title, String message) {
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.getWindow().setType(
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			progressDialog.show();
		}

		public void changeDialogProgress(int progress) {
			if (null != progressDialog) {
				progressDialog.setProgress(progress);
			}
		}

		public void dismissFileStatusDialog() {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}

		public boolean isShowing() {
			return (null == progressDialog) ? false : (progressDialog
					.isShowing());
		}
	};

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

	private void uploadEcgData(String srcUrl, String pdfUrl)
			throws JSONException {
		JSONObject data = getDefaltAttrs();
		data.put(Tables.ECG_SRC, srcUrl);
		data.put(Tables.ECG_PDF, pdfUrl);
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
		return data;
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

	/***
	 * �ϴ�����,data���ɾ�����Ŀָ��
	 * 
	 * @param data
	 * @param path
	 * @throws JSONException
	 */
	public void upload(JSONObject data, String path) {
		try {
			JSONObject para = new JSONObject();
			String idCard = BaseActivity.getUser().getCardNo();
			if (null != deviceInfo) {
				para.put(WebService.DEVICENAME, deviceInfo.getDeviceName());
			}
			para.put(WebService.DATA, data);
			// para.put(WebService.GUID_KEY, "4086-3783-4074-8809");
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			para.put(WebService.CARDNO, idCard);
			para.put(WebService.CRC, "");
			JSONObject result = WebService.postConenction(para, path);
			int status = result.getInt(WebService.STATUS_CODE);

			Intent intent = new Intent();
			intent.setAction(UPLOAD_RESULT_ACTION);
			Bundle bundle = new Bundle();
			bundle.putInt("status", status);
			bundle.putString("path", path);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		} catch (Exception e) {
			Intent intent = new Intent();
			intent.setAction(UPLOAD_RESULT_ACTION);
			Bundle bundle = new Bundle();
			bundle.putInt("status", WebService.NETERROE);
			bundle.putString("path", path);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}
	}

	public class DownloadTask extends AsyncTask<String, Integer, Boolean> {
		static final int BUFFER_SIZE = 1024;
		String mLoalpath = null;
		URL mUrl = null;
		HttpURLConnection mUrlConn = null;
		ProgressNotify mNotify = new ProgressNotify(BackService.this);
		int mProgress = 0;

		public DownloadTask() {

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO �Զ����ɵķ������
			if (mProgress != values[0])
				mNotify.changeDialogProgress(values[0]);
			mProgress = values[0];
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO �Զ����ɵķ������
			File dir = new File(StaticApp.getinstance().mDataPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String url = params[0];
			String[] filePath = url.split("/");
			String fileName = filePath[filePath.length - 1];
			Log.e(TAG, "fileName:" + fileName);
			mLoalpath = StaticApp.getinstance().mDataPath + fileName;
			try {
				// String url = "http://" + StaticApp.getinstance().mServerIP
				// + ":18080/staticeecgreportdata/"
				// + filename.substring(0, 6) + "/"
				// + filename.substring(6, 8) + "/" + filename + ".pdf";
				mUrl = new URL(url);
			} catch (MalformedURLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				mUrl = null;
			}
			if (mUrl == null) {
				return false;
			}
			final File file = new File(mLoalpath);
			final boolean bexit = false;
			if (file.exists()) {
				return true;
			}
			OutputStream fileoutput = null;
			try {
				mUrlConn = (HttpURLConnection) mUrl.openConnection();
			} catch (IOException e) {
				T.show(BackService.this, "��δ���߱��棬���Ե�", Toast.LENGTH_LONG);
				e.printStackTrace();
				return false;
			}
			InputStream inputstream = null;
			try {
				inputstream = mUrlConn.getInputStream();
			} catch (IOException e) {
				T.show(BackService.this, "��δ���߱��棬���Ե�", Toast.LENGTH_LONG);
				e.printStackTrace();
				return false;
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				return false;
			}
			int filesize = mUrlConn.getContentLength();
			try {
				fileoutput = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				return false;
			}
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			int count = 0;
			int progress = 0, progresslast = 0;
			try {
				while ((len = inputstream.read(buffer, 0, BUFFER_SIZE)) != -1) {
					fileoutput.write(buffer, 0, len);
					count += len;
					publishProgress((int) ((count / (float) filesize) * 100));
				}
				fileoutput.flush();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				return false;
			} finally {
				try {
					fileoutput.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		@Override
		protected void onCancelled() {
			// TODO �Զ����ɵķ������
			mNotify.dismissFileStatusDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO �Զ����ɵķ������
			mNotify.dismissFileStatusDialog();
			if (!result) {
				return;
			}
			File file = new File(mLoalpath);
			if (file.exists()) {
				openfile(file);
			}
		}

		protected void openfile(File file) {
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				T.show(BackService.this, "�밲װPDF�Ķ���", Toast.LENGTH_SHORT);
			}
		}

		@Override
		protected void onPreExecute() {
			mNotify.showProgressDialog("����", "���������ļ�������");
		}
	};

}
