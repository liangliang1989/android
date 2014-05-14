package com.health;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.younext.R;

import com.KRL.Data.NetWork.NetType;
import com.KRL.Network.NetworkConnectThread;
import com.KRL.Tools.Tools;
import com.KRL.service.BackService;
import com.KRL.service.IBackService;
import com.health.bean.Group;
import com.health.database.Cache;
import com.health.util.MyMD5;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.util.VersionControler;
import com.health.util.VersionControler.V;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;
import com.health.web.WebService.StatusCode;

public class Login extends BaseActivity {

	protected static final int MESSAGE_TOAST = 100300;
	private static final int ENABLED = 100301;
	protected static final int LOGIN_STATE = 100302;
	private static final String TOAST = "toast";

	private static AutoCompleteTextView countET;
	private static EditText passwordET;
	private static Button loginButton;
	private static Button logupButton;
	private static Context context;
	private static String count = "";
	private static String passWord = "";
	private static MyProgressDialog dialog;
	private CheckBox saveCb;

	private static final String FILE_NAME = "accountsAndPasswords";
	private Map<String, String> accountPasswordMap = new TreeMap<String, String>();
	private List<String> accounts = new ArrayList<String>();

	private NetworkConnectThread connectThread;
	private IBackService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		versionSwitch();

		context = this;
		cache = new Cache(Login.this);
		dialog = new MyProgressDialog(context);
		saveCb = (CheckBox) findViewById(R.id.save_password_cb);
		passwordET = (EditText) this.findViewById(R.id.et_passworld);
		readAccountsAndPasswords();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, accounts);
		countET = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		countET.setAdapter(arrayAdapter);
		countET.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String s = (String) parent.getItemAtPosition(position);

				if (accountPasswordMap.containsKey(s)) {
					passwordET.setText(accountPasswordMap.get(s));
				}
				// �������뷨
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				countET.setCursorVisible(false);
				imm.hideSoftInputFromWindow(countET.getWindowToken(), 0);
			}
		});

		passwordET = (EditText) findViewById(R.id.et_passworld);
		passwordET.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String s = countET.getText().toString();
				if (accountPasswordMap.containsKey(s)) {
					passwordET.setText(accountPasswordMap.get(s));
				}
				return false;
			}
		});

		// countET.setText(COUNT);
		// passwordET.setText(PSWD);
		// countET.clearFocus();
		// passwordET.clearFocus();
		loginButton = (Button) this.findViewById(R.id.loginButton);
		logupButton = (Button) this.findViewById(R.id.btn_read_id);
		OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == loginButton) {
					if (isNetConnected()) {
						dialog.show();
						loginButton.setEnabled(false);// ��¼ʱ�����ٰ���¼
						loginBackGround();
					}
				} else if (v == logupButton) {
					Intent intent = new Intent(Login.this, ReadIDCard.class);
					startActivity(intent);
				}
			}

		};
		/*
		 * OnClickListener listener1 =new mlistener();
		 * class mlistener implements
		 * View.OnClickListener{
		 * 
		 * @Override public void onClick(View v) { //
		 * TODO Auto-generated method stub if (v ==
		 * loginButton) { dialog.show();
		 * loginButton.setEnabled(false);// ��¼ʱ�����ٰ���¼
		 * loginBackGround(); } else if (v ==
		 * logupButton) { Intent intent = new
		 * Intent(Login.this, ReadIDCard.class);
		 * startActivity(intent); } }
		 * 
		 * }
		 */
		loginButton.setOnClickListener(listener);
		logupButton.setOnClickListener(listener);

	}

	private void versionSwitch() {

		startService();
		bindService();

		int imgId = -1;
		switch (VersionControler.version) {
		case KRL:
			setContentView(R.layout.login_krl);
			return;
		case BeiKen:
			imgId = R.drawable.desk_beiken;
			break;
		case PingAnTong:
			imgId = R.drawable.pingantong;
			break;
		case ZTE:
			imgId = R.drawable.desk_zte;
			break;
		}
		setContentView(R.layout.login);
		ImageView backImgView = (ImageView) findViewById(R.id.login_logo);
		if (imgId != -1)
			backImgView.setImageResource(imgId);
	

	}

	private void jump2MainUi() {
		Intent intent = new Intent(Login.this, MainUi.class);
		startActivity(intent);
		finish();
	}

	private void loginBackGround() {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				try {
					login();
					// ��¼krl �в�������������
					if (VersionControler.version == V.KRL)
						loginECG();
				} catch (Exception e) {
					handler.obtainMessage(LOGIN_STATE, WebService.NETERROE, -1)
							.sendToTarget();
					return;
				}
				handler.obtainMessage(ENABLED, 0, -1).sendToTarget();
			}
		}).start();
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_STATE:
				dialog.cancel();
				loginButton.setEnabled(true);
				switch (msg.arg1) {
				case WebService.NETERROE:
					T.showLong(context, "�������!");
					break;
				case WebService.OK:
					T.showLong(context, "��¼�ɹ�!");
					JSONObject json = (JSONObject) msg.obj;
					try {
						JSONObject userInfo = json
								.getJSONObject(WebService.DATA);
						setLoginInfo(userInfo, count, passWord);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					jump2MainUi();
					break;
				case WebService.ERROE:
					T.showLong(context, "�û������������!");
					break;
				case StatusCode.CAN_NOT_LOGIN:
					T.showLong(context, "���û��������¼ϵͳ!");
					break;
				case StatusCode.CAN_NOT_IDENTIFY:
					T.showLong(context, "�޷�ʶ����û�");
					break;
				case StatusCode.NULL_NAME_PASSWORD:
					T.showLong(context, "�û���������Ϊ��");
					break;
				case StatusCode.FAILED_LOGIN:
					T.showLong(context, "�û������������");
					break;
				default:
					break;
				}
			case ENABLED:
				loginButton.setEnabled(true);
				break;

			}
		}
	};

	private void login() throws Exception {
		count = countET.getText().toString();
		passWord = passwordET.getText().toString();
		JSONObject para = new JSONObject();
		para.put("username", count);
		para.put("password", passWord);
		// para.put(WebService.GUID_KEY,
		// WebService.GUID_VALUE);
		String crc = count + passWord + WebService.GUID_VALUE;
		crc = MyMD5.getMD5(crc);
		para.put(WebService.CRC, crc);
		JSONObject result = WebService.postConenction(para,
				WebService.PATH_GROUP_LOGIN);
		if (result == null) {
			handler.obtainMessage(LOGIN_STATE, WebService.NETERROE, 0, null)
					.sendToTarget();
		} else {
			int state = result.getInt(WebService.STATUS_CODE);
			handler.obtainMessage(LOGIN_STATE, state, 0, result).sendToTarget();
		}
	}

	// login ECG server: Johnny Yang
	private void loginECG() {
		// count = countET.getText().toString();
		// passWord = passwordET.getText().toString();
		String account = "demo";
		String pwd = "demo";
		if ("".equals(account) || "".equals(pwd)) {
			T.show(this, "�˺�����Ϊ��", Toast.LENGTH_SHORT);
			return;
		}
		if (null != service) {
			try {
				service.setECGLoginAccount(account, pwd);
				service.loginECGServer();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private void startService() {
		Intent serviceIntent = new Intent(BackService.ACTION);
		startService(serviceIntent);
	}

	private void bindService() {
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

	/**
	 * Johnny Yang
	 * 
	 * @return net connection state
	 */
	private boolean isNetConnected() {
		NetType type = Tools.cheackNetConnection(this);
		switch (type) {
		case NET_3G:
			T.showLong(context, "��ǰ����Ϊ�ƶ����磬���ܻ������������!");
		case NET_WIFI:
			return true;
		case NET_NONE:
		default:
			T.showLong(context, "��ǰ���粻����!");
			return false;
		}
	}

	/**
	 * �����˺�,���õ�¼��Ϣ
	 * 
	 * @throws JSONException
	 */
	private void setLoginInfo(JSONObject userInfo, String count, String passWord)
			throws JSONException {
		writeAccountAndPassword();
		String nickName = userInfo.getString(WebService.NICKNAME);
		if (nickName.contains("ƽ��ͨ"))
			nickName = "��ҽ��";
		BaseActivity.setGroup(new Group(nickName, count, userInfo
				.getString(WebService.TOKEN), passWord));
		// ����guid��������ͬ�İ汾�ò�ͬ��guid
		WebService.setGuid(userInfo.getString(WebService.GUID_KEY));
	}

	/**
	 * Read accounts and passwords from file and store
	 * them in a map
	 */
	private void readAccountsAndPasswords() {
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			byte[] buffer = new byte[1024];
			int count = 0;
			StringBuilder stringBuilder = new StringBuilder("");

			while ((count = fis.read(buffer)) > 0) {
				stringBuilder.append(new String(buffer, 0, count));
			}

			String[] lines = stringBuilder.toString().split("\n");
			for (int i = 0; i < lines.length; i++) {
				String[] accountAndPassword = lines[i].split(",");
				accountPasswordMap.put(accountAndPassword[0],
						accountAndPassword[1]);
			}

			// Get all entries into a set
			Set<Map.Entry<String, String>> entrySet = accountPasswordMap
					.entrySet();

			for (Map.Entry<String, String> entry : entrySet) {
				accounts.add(entry.getKey());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the account and password into the file in
	 * append way
	 */
	private void writeAccountAndPassword() {
		String account = countET.getText().toString();
		String password = passwordET.getText().toString();
		if (!saveCb.isChecked()) {// ����Ҫ��������
			if (accountPasswordMap.containsKey(count))
				accountPasswordMap.remove(count);
		} else {// �����ܴa
			accountPasswordMap.put(count, passWord);
		}
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			PrintStream ps = new PrintStream(fos);
			for (Map.Entry<String, String> entry : accountPasswordMap
					.entrySet())
				ps.println(entry.getKey() + "," + entry.getValue());
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		unbindService(connection);
		super.onDestroy();

	}
}
