package com.health;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.bean.Group;
import com.health.bean.User;
import com.health.bluetooth.BluetoothListActivity;
import com.health.bluetooth.BluetoothService;
import com.health.users.HealthArchive;
import com.health.util.IDCard;
import com.health.util.IDCardFormatException;
import com.health.util.L;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;
import com.identity.Shell;
import com.identity.globalEnum;

/**
 * ע��
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-11-29 ����3:33:50
 */
public class Logup extends BaseActivity {
	private static final int MESSAGE_TOAST = 10000;
	private static final int LOGUP_RESULT = 10002;
	private static final int ENABLED = 10001;
	private static final int JUMP_STATUR = 0x10003;
	private static Context context;
	private static EditText nameEditText;
	private static EditText cardNoEditText;
	private static EditText moblieEditText;
	// private static DatePicker datePicker;
	private static EditText passwordEditText;
	private static EditText cPasswordEditText;// ȷ������
	private static EditText emailText;
	private static Button logupButton;

	private static String birthday = null;
	private static String sex = null;

	// ����������
	private static final int DATA_READED = 0x001;
	private static final int PUT_ID_CARD = 0x002;
	private static final int CONNECT_FAILED = 0x003;
	private static final int NOT_FOUND_CARD = 0X004;

	private ImageView iv;
	private Bitmap bm;
	private Button findIdReaderBtn;
	private Button readBtn;
	// private Button goToMeasureBtn;
	private TextView mTVInfo;
	private BluetoothAdapter mAdapter;
	private BluetoothDevice mDevice;
	private static final int REQUEST_ENABLE_BT = 2;
	protected static final int JUMP_SUC = 0x98;
	protected static final int JUMP_FAIL = 0x99;
	protected static final int JUMP_ERROR = 0x100;

	private Shell shell;
	private boolean bStop = false;
	private boolean bConnected = false;
	private BluetoothService bluetoothService;
	private boolean idReaderPaired = false;
	private boolean isConnect = false;
	private String sName = "";// ע��ɹ����û���
	private String sCardNo = "";// ע��ɹ��Ŀ���
	private String sPassword = "";
	Runnable findIdReader = new FindIdReader();
	Thread findIdReaderThread = new Thread(findIdReader);

	private MyProgressDialog mDialog;

	private ExecutorService exec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logup);
		context = this;
		mDialog = new MyProgressDialog(this);
		exec = Executors.newSingleThreadExecutor();
		nameEditText = (EditText) findViewById(R.id.name);
		cardNoEditText = (EditText) findViewById(R.id.cardNo);
		moblieEditText = (EditText) findViewById(R.id.mobile);
		emailText = (EditText) findViewById(R.id.email);
		passwordEditText = (EditText) findViewById(R.id.password);
		cPasswordEditText = (EditText) findViewById(R.id.confirm_password);
		logupButton = (Button) findViewById(R.id.logupButton);
		logupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = nameEditText.getText().toString();
				name = name.trim();
				if (name.length() == 0) {
					Toast.makeText(context, "��������Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				String cardNo = cardNoEditText.getText().toString();
				cardNo = cardNo.trim();
				if (cardNo == null || cardNo.length() == 0) {
					Toast.makeText(context, "���֤���벻��Ϊ��", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (checkIdcard(cardNo) == false) {
					return;
				}
				String moblie = moblieEditText.getText().toString();
				// if (moblie.length() == 0) {
				// Toast.makeText(context, "�绰���벻��Ϊ��",
				// Toast.LENGTH_LONG)
				// .show();
				// return;
				// }
				String email = emailText.getText().toString();
				// if (email.length() == 0) {
				// Toast.makeText(context,
				// "���������һ����룬����Ϊ��", Toast.LENGTH_LONG)
				// .show();
				// return;
				// }
				String password = passwordEditText.getText().toString();
				if (password.length() == 0) {
					Toast.makeText(context, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				String cPassword = cPasswordEditText.getText().toString();
				if (cPassword.length() == 0) {
					Toast.makeText(context, "ȷ�����벻��Ϊ��", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (!cPassword.equals(password)) {
					Toast.makeText(context, "�����������벻��ͬ", Toast.LENGTH_LONG)
							.show();
					return;
				}
				try {
					JSONObject info = toJson(name, cardNo, moblie, birthday,
							email, sex, password);
					logupButton.setEnabled(false);
					sCardNo = cardNo;
					sName = name;
					sPassword = password;
					exec.execute(new Loguper(info));
					mDialog.show();
				} catch (JSONException e) {
					Toast.makeText(context, "�����쳣", Toast.LENGTH_LONG).show();
				}
			}
		});

		OnFocusChangeListener focusListenner = new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					if (nameEditText == view)
						Toast.makeText(context, "����������", Toast.LENGTH_SHORT)
								.show();
					if (cardNoEditText == view) {
						String cardNo = cardNoEditText.getText().toString();
						checkIdcard(cardNo);
					}
				}
			}
		};
		nameEditText.setOnFocusChangeListener(focusListenner);
		cardNoEditText.setOnFocusChangeListener(focusListenner);
		moblieEditText.setOnFocusChangeListener(focusListenner);
		emailText.setOnFocusChangeListener(focusListenner);
		passwordEditText.setOnFocusChangeListener(focusListenner);
		cPasswordEditText.setOnFocusChangeListener(focusListenner);

		// ����������
		iv = (ImageView) findViewById(R.id.ivImageview);
		mTVInfo = (TextView) findViewById(R.id.tv_info);
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		readBtn = (Button) findViewById(R.id.btnInit);
		readBtn.setOnClickListener(new ButtonInitOnClick());
		bluetoothService = BluetoothService.getService(handler, true);
		mTVInfo.setText("��������������...");

		if (!isConnect) {
			findIdReaderThread.start();
		}

		findIdReaderBtn = (Button) findViewById(R.id.btn_find_id_reader);
		findIdReaderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent serverIntent = new Intent(Logup.this,
						BluetoothListActivity.class);
				startActivityForResult(serverIntent,
						BluetoothListActivity.REQUEST_CONNECT_DEVICE);
			}
		});
	}

	private boolean checkIdcard(String cardNo) {
		try {
			IDCard idcard = new IDCard(cardNo);
			birthday = idcard.getFormatBirthDate("yyyy/MM/dd");
			sex = idcard.isFemal() ? "Ů" : "��";
			return true;
		} catch (IDCardFormatException e) {
			e.printStackTrace();
			Toast.makeText(context, "���֤����������������ʵ���֤����!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}

	class FindIdReader implements Runnable {

		private Message msg;

		@Override
		public void run() {
			try {
				if (mAdapter == null) {
					mTVInfo.setText("mAdapter is null!");
				}
				if (!mAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}

				Set<BluetoothDevice> pairedDevices = mAdapter
						.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						String str;
						str = device.getName().substring(0, 3);
						Log.w("pairedDevices",
								"device.getName().substring(0, 3) is:" + str);
						if (str.equalsIgnoreCase("SYN") && !idReaderPaired) {
							mDevice = device;
							idReaderPaired = true;
							Log.i("bluetooth",
									"**************" + mDevice.getName());
							try {
								mAdapter.cancelDiscovery();
								shell = new Shell(
										Logup.this.getApplicationContext(),
										mDevice);
								Log.i("new shell",
										"**************Socket connect ok**********");
								isConnect = true;
								msg = handler.obtainMessage(PUT_ID_CARD);
								handler.sendMessage(msg);
							} catch (IOException e) {
								e.printStackTrace();
								msg = handler.obtainMessage(CONNECT_FAILED);
								handler.sendMessage(msg);
							}
						}
					}
					if (!idReaderPaired) {
						msg = handler.obtainMessage(NOT_FOUND_CARD);
						handler.sendMessage(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
				connectIdReader(address);
			}
			break;
		}
	}

	private void connectIdReader(String address) {
		mDevice = bluetoothService.getRemoteDeviceByAddress(address);
		Message msg;
		try {
			mAdapter.cancelDiscovery();
			shell = new Shell(Logup.this.getApplicationContext(), mDevice);
			Log.i("new shell", "**************Socket connect ok**********");
			isConnect = true;
			msg = handler.obtainMessage(PUT_ID_CARD);
			handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
			msg = handler.obtainMessage(CONNECT_FAILED);
			handler.sendMessage(msg);
		}

		isConnect = true;
		mTVInfo.setText("����������������");

	}

	public Handler handler = new Handler() {
		private byte[] data;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case JUMP_STATUR:
				mDialog.cancel();
				switch (msg.arg1) {
				case JUMP_SUC:
					T.showLong(context, "��ӭ�������������ĵ�����");

					jump2HealthArchive();
					break;
				case JUMP_FAIL:
					T.showLong(context, (String) msg.obj);
					break;
				case JUMP_ERROR:
					T.showLong(context, (String) msg.obj);
					break;
				}
				break;
			case LOGUP_RESULT:
				mDialog.cancel();
				switch (msg.arg1) {
				case WebService.OK:
					T.showShort(context, "ע��ɹ�,������ת!");
					iWantSleep();
					mDialog.show();
					break;
				case WebService.NETERROE:
					T.showShort(context, "�������");
					break;
				case WebService.ERROE:
					String s = (String) msg.obj;
					if (s == null)
						T.showShort(context, "ע��ʧ�ܣ�");
					else
						T.showShort(context, s);
					break;

				default:
					break;
				}
				break;
			case ENABLED:
				logupButton.setEnabled(true);
				break;
			case PUT_ID_CARD:
				mTVInfo.setText("�����Ӷ�����\n��������ȡ�����Ϣ����ť");
				break;
			case CONNECT_FAILED:
				mTVInfo.setText("��������ʧ��");
				break;
			case NOT_FOUND_CARD:
				mTVInfo.setText("δ�ҵ������������ֶ�����");
				break;
			case DATA_READED:
				data = (byte[]) msg.obj;
				if (data == null) {
					mTVInfo.setText("���ݶ�ȡ�� ...");
				} else {
					try {
						// setUser(new
						// User(shell.GetName(data),
						// shell.GetIndentityCard(data)));
						nameEditText.setText(shell.GetName(data));
						cardNoEditText.setText(shell.GetIndentityCard(data));
						mTVInfo.setText("���ã�" + shell.GetName(data));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				break;
			case 100:
				bm = (Bitmap) msg.obj;
				iv.setImageBitmap(bm);
				deleteFile("zp.bmp");
				// goToMeasureBtn.setVisibility(View.VISIBLE);

				break;
			case 101:
				mTVInfo.setText("��Ƭ������Ȩ�ļ�����ȷ");
				break;
			case 102:
				mTVInfo.setText("��Ƭԭʼ���ݲ���ȷ");
				break;
			}
		}
	};

	private class Loguper implements Runnable {
		JSONObject info;

		public Loguper(JSONObject info) {
			this.info = info;

		}

		@Override
		public void run() {
			try {
				logup(info);
				handler.obtainMessage(ENABLED, 0, -1).sendToTarget();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private JSONObject toJson(String name, String cardNo, String moblie,
			String birthday, String email, String sex, String password)
			throws JSONException {
		JSONObject para = new JSONObject();
		para.put("cardNo", cardNo);
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		Group group = getGroup();
		para.put(WebService.NAME, group.getUserName());
		para.put(WebService.PASS_WORD, group.getPassword());
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("cardNo", cardNo);
		if (moblie != null)
			data.put("moblie", moblie);
		data.put("birthday", birthday);
		if (email != null)
			data.put("email", email);
		data.put("sex", sex);
		data.put("password", password);
		para.put(WebService.DATA, data);

		return para;
	}

	private void iWantSleep() {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				try {
					// ��˯���룬������̫����
					TimeUnit.SECONDS.sleep(2);
					// ��һ����������û
					JSONObject result = WebService.getUserList(0, sCardNo);
					int status = result.getInt(WebService.STATUS_CODE);
					if (status == WebService.OK) {
						JSONArray users = (JSONArray) result
								.get(WebService.DATA);
						for (int i = 0; i < users.length(); i++) {
							JSONObject userJson = users.getJSONObject(i);

							String cardNo = userJson
									.getString(WebService.CARDNO);
							String cusId = userJson
									.getString(WebService.CUSTOMERGUID);
							L.i("iWantSleep", cardNo + "-----" + cusId);
							if (sCardNo.equals(cardNo) && cusId != null) {
								User user = new User(
										cardNo,
										userJson.getString(WebService.USERID),
										userJson.getString(WebService.BIRTHDAY),
										userJson.getString(WebService.SEX),
										userJson.getString(WebService.IMAGEURL),
										userJson.getString(WebService.EMAIL),
										userJson.getString(WebService.NICKNAME),
										cusId,
										userJson.getString(WebService.NAME),
										userJson.getString(WebService.USERGUID),
										userJson.getString(WebService.MOBILE));
								BaseActivity.setUser(user);
								handler.obtainMessage(JUMP_STATUR, JUMP_SUC, 0,
										"��¼�ɹ���").sendToTarget();
								return;// ������ת��
							}
						}
						// û���ҵ�
						handler.obtainMessage(JUMP_STATUR, JUMP_FAIL, 0,
								"�˻���������У���5���Ӻ����ԣ�").sendToTarget();
						return;
					}
				} catch (Exception e) {
					handler.obtainMessage(JUMP_STATUR, JUMP_ERROR, 0, "�����쳣��")
							.sendToTarget();
				}

			}
		}).start();

	}

	/***
	 * ��ת����������
	 */
	protected void jump2HealthArchive() {
		Intent intent = new Intent(this, HealthArchive.class);
		startActivity(intent);
		finish();
	}

	private void logup(JSONObject info) throws JSONException {
		JSONObject result = WebService.logup(info);
		if (result == null) {
			handler.obtainMessage(LOGUP_RESULT, WebService.ERROE, 0)
					.sendToTarget();
		} else {
			int status = result.getInt(WebService.STATUS_CODE);
			String msg = result.getString(WebService.STATUS_MSG);
			handler.obtainMessage(LOGUP_RESULT, status, 0, msg).sendToTarget();
		}

	}

	private class ButtonInitOnClick implements OnClickListener {
		public void onClick(View v) {
			Log.i("readBtn", "**************readBtn***************");
			globalEnum ge = globalEnum.NONE;
			try {
				if (shell.Register()) {

					// mInfoView.add("������ע��ɹ���");
					ge = shell.Init();
					if (ge == globalEnum.INITIAL_SUCCESS) {
						readBtn.setEnabled(false);
						mTVInfo.setText("������֤�ŵ���������\n" + "����Ѿ��������ˣ����������ط�һ��");
						// mInfoView.add("�������ӳɹ���");

						bConnected = true;
						new Thread(new GetDataThread()).start();
					} else {
						shell.EndCommunication();// 0316
						mTVInfo.setText("��������ʧ��");
					}
				} else {
					mTVInfo.setText("��������ʧ��");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class GetDataThread implements Runnable {
		private String data = null;
		private byte[] cardInfo = new byte[256];
		private int count = 0;
		private Message msg;
		private boolean bRet = false;
		private String wltPath = "";
		private String termBPath = "";

		public GetDataThread() {
		}

		public void run() {
			Log.i("Activity", "****************GetDataThread --");
			globalEnum ge = globalEnum.GetIndentiyCardData_GetData_Failed;
			try {
				// Thread.sleep(2000);
				globalEnum gFindCard = globalEnum.NONE;
				while (!bStop) {
					count += 1;
					if (count == 10) {
						System.gc();
						System.runFinalization();
						count = 0;
					}
					data = null;
					bRet = shell.SearchCard();
					if (bRet) {
						
						bRet = shell.SelectCard();
						if (bRet) {
							
							ge = shell.ReadCard();
							if (ge == globalEnum.GetDataSuccess) {
								
								cardInfo = shell.GetCardInfoBytes();
								msg = handler.obtainMessage(DATA_READED,
										cardInfo);
								handler.sendMessage(msg);

								wltPath = "/data/data/cn.younext/files/";
								termBPath = "/mnt/sdcard/";
								int nret = shell.GetPic(wltPath, termBPath);
								Log.i("nret", "************" + nret);
								if (nret > 0) {
									Bitmap bm = BitmapFactory
											.decodeFile("/data/data/cn.younext/files/zp.bmp");
									msg = handler.obtainMessage(100, bm);
									handler.sendMessage(msg);

								} else if (nret == -5) {
									msg = handler.obtainMessage(101, data);
									handler.sendMessage(msg);
								} else if (nret == -1) {
									msg = handler.obtainMessage(102, data);
									handler.sendMessage(msg);
								}
							}
						}
					}
					SystemClock.sleep(50);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bStop = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (bConnected) {
				Log.w("Activity", "onDestroy bConnected is true");
				if (shell.EndCommunication()) {
					shell.Destroy();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
