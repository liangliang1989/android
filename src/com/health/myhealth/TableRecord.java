package com.health.myhealth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cn.younext.R;

import com.KRL.Staticecg.StaticApp;
import com.KRL.service.BackService;
import com.KRL.service.IBackService;
import com.health.BaseActivity;
import com.health.database.Tables;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.util.ThreeCloumAdpter;
import com.health.util.TimeHelper;
import com.health.web.WebService;

/***
 * �����ʾ���м�¼
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-12-19 ����10:00:56
 */
public class TableRecord extends BaseActivity {
	final String TAG = "TableRecord";
	public static final String SELECTED_TOKEN = "selectedToken";
	public static final String SELECTED_PATH = "selectedPath";
	private static final Integer PAGE_COUNT = 10;
	private static final int OK = 0;
	private static final int NET_ERROR = 1;
	private static final int NO_DATA = 2;

	private Context context;
	private Button bpButton;// Ѫѹ
	private Button pulseButton;// ����
	private Button boButton;// Ѫ��
	private Button tempButton;// ����
	private Button gluButton;// Ѫ��
	private Button uaButton;// ����
	private Button cholButton;// �ܵ��̴�
	private Button wbcButton;// ��ϸ��
	private Button urineButton;// �򳣹�
	private Button ecgButton;
	private Button tableShowButton;// ���
	private Button curveShowButton;// ����
	private Button homeButton;// ��������
	private Button backButton;// ����
	private Button lastButton = null;// �ϴε���㰴ť
	private ListView listview;// ��ʾ����listview
	private ThreeCloumAdpter mAdpter;
	private List<String[]> datas = new ArrayList<String[]>();// ��ʾ����
	private String[] titles = { "����ʱ��", "����ֵ", "�������" };
	private int pageIndex = 0;// Ĭ��Ϊ��һҳ
	private boolean hasMore = false;// ���и���������ʾ
	private String cPath = WebService.PATH_QUERY_ECG;// ��ǰ��Ŀ�ĺ�׺
	private String cToken = Tables.ECG_KRL;// ��ǰ��Ŀ
//	private String cPath = WebService.PATH_QUERY_BP;// ��ǰ��Ŀ�ĺ�׺
//	private String cToken = Tables.SBP;// ��ǰ��Ŀ
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	private MyProgressDialog mDiaog;
	private IBackService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_record);
		regist();
		context = this;
		parseBundle();
		initView();
		setButtonListener();
		tableShowButton.setSelected(true);
		curveShowButton.setSelected(false);
		setSelected(cToken);
		Intent intent = getIntent();
		String item = intent.getStringExtra("item");
		if (item != null)
			setSelected(item);
		clearAndaddTitles();
		mAdpter = new ThreeCloumAdpter(context, datas);
		listview.setAdapter(mAdpter);
		setListViewListener();
		mDiaog = new MyProgressDialog(this);
//		mDiaog.show();
		exec.execute(new DataDenerator(mHanlder, cPath, cToken, pageIndex));
	}

	private void regist() {
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

	/***
	 * ���ó�ʼʱ��ѡ��button
	 * 
	 * @param token
	 */
	private void setSelected(String token) {
		Button cButton;
		if (Tables.SBP.equals(token))
			cButton = bpButton;
		else if (Tables.PULSE.equals(token))
			cButton = pulseButton;
		else if (Tables.SBP.equals(token))
			cButton = bpButton;
		else if (Tables.BO.equals(token))
			cButton = boButton;
		else if (Tables.TEMP.equals(token))
			cButton = tempButton;
		else if (Tables.GLU.equals(token))
			cButton = gluButton;
		else if (Tables.UA.equals(token))
			cButton = uaButton;
		else if (Tables.CHOL.equals(token))
			cButton = cholButton;
		else if (Tables.WBC.equals(token))
			cButton = wbcButton;
		else if(Tables.ECG_KRL.equals(token)){
			cButton = ecgButton;
		}
		else
			cButton = urineButton;
		setSelected(cButton);
	}

	/***
	 * ����bundle������
	 * 
	 * @param savedInstanceState
	 */
	private void parseBundle() {
		Bundle state = getIntent().getExtras();
		if (state != null) {
			String path = state.getString(TableRecord.SELECTED_PATH);
			if (null != path)
				cPath = path;
			String token = state.getString(TableRecord.SELECTED_TOKEN);
			if (null != token)
				cToken = token;
		}
	}

	private void setListViewListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == datas.size() - 1 && hasMore) {
					datas.remove(position);
					mDiaog.show();
					pageIndex++;
					exec.execute(new DataDenerator(mHanlder, cPath, cToken,
							pageIndex));
				} else {
					if (cToken == Tables.ECG_KRL) {
						String[] ecgItem = datas.get(position);
						String pdfUrl = ecgItem[2];
						Log.e(TAG, "pdfUrl:" + pdfUrl);
						File localFile = getLocalFile(pdfUrl);
						if (null != localFile) {
							openfile(localFile);
						} else {
							downloadPdfAlert(pdfUrl);
						}
					}
				}
			}
		});
	}

	// ����pdf�����Ƿ����
	private File getLocalFile(String fileUrl) {
		String filePath[] = fileUrl.split("/");
		String localFilePath = StaticApp.getinstance().mDataPath
				+ filePath[filePath.length - 1];
		File file = new File(localFilePath);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	private void openfile(File file) {
		Uri path = Uri.fromFile(file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(path, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			T.show(this, "�밲װPDF�Ķ���", Toast.LENGTH_SHORT);
		}
	}

	private void downloadPdfAlert(final String fileUrl) {
		new AlertDialog.Builder(TableRecord.this).setTitle("ȷ��")
				.setMessage("��Ҫ���ر��棬ȷ�����أ�")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							if (null != service)
								service.downloadEcgFile(fileUrl);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * ����ѡ�а�ť,ȡ���ϴΰ�ť��ѡ��
	 * 
	 * @param button
	 */
	private void setSelected(Button button) {
		if (lastButton != null)
			lastButton.setSelected(false);
		lastButton = button;
		lastButton.setSelected(true);
	}

	/***
	 * ���ü�����
	 */
	private void setButtonListener() {
		OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == curveShowButton) {
					jump2curve();
				} else if (v == tableShowButton) {

				} else if (v == homeButton) {
					TableRecord.this.setResult(RESULT_OK);
					TableRecord.this.finish();
				} else if (v == backButton) {
					TableRecord.this.finish();
				} else {
					String path = WebService.PATH_QUERY_BP;
					String token = Tables.SBP;
					if (v == bpButton) {
						path = WebService.PATH_QUERY_BP;
						token = Tables.SBP;// ��������ѹ������ѹ
					} else if (v == pulseButton) {
						path = WebService.PATH_QUERY_PULSE;
						token = Tables.PULSE;
					} else if (v == boButton) {
						path = WebService.PATH_QUERY_BO;
						token = Tables.BO;
					} else if (v == tempButton) {
						path = WebService.PATH_QUERY_TEMP;
						token = Tables.TEMP;
					} else if (v == gluButton) {
						path = WebService.PATH_QUERY_GLU;
						token = Tables.GLU;
					} else if (v == uaButton) {
						path = WebService.PATH_QUERY_UA;
						token = Tables.UA;
					} else if (v == cholButton) {
						path = WebService.PATH_QUERY_CHOL;
						token = Tables.CHOL;
					} else if (v == wbcButton) {
						path = WebService.PATH_QUERY_WBC;
						token = Tables.WBC;
					} else if (v == urineButton) {
						path = WebService.PATH_QUERY_URINE;
						token = Tables.LEU;// �����򳣹�11��
					} else if (v == ecgButton) {
						path = WebService.PATH_QUERY_ECG;
						token = Tables.ECG_KRL;
					}
					// �����µİ��o
					if (!cToken.equals(token)) {
						cToken = token;
						cPath = path;
						// ҳ����0
						pageIndex = 0;
						setSelected((Button) v);
						clearAndaddTitles();
						mAdpter.notifyDataSetChanged();
						mDiaog.show();
						exec.execute(new DataDenerator(mHanlder, path, token,
								pageIndex));

					}

				}
			}

			private void jump2curve() {
				Intent intent;
				if (cToken.equals(Tables.LEU)) {
					intent = new Intent(TableRecord.this, UrineChart.class);
				} else {
					intent = new Intent(TableRecord.this, CurveRecord.class);
					Bundle bundle = new Bundle();
					bundle.putString(TableRecord.SELECTED_TOKEN, cToken);
					bundle.putString(TableRecord.SELECTED_PATH, cPath);
					intent.putExtras(bundle);
				}
				startActivity(intent);
				TableRecord.this.finish();
			}
		};
		bpButton.setOnClickListener(listener);
		pulseButton.setOnClickListener(listener);
		boButton.setOnClickListener(listener);
		tempButton.setOnClickListener(listener);
		gluButton.setOnClickListener(listener);
		uaButton.setOnClickListener(listener);
		cholButton.setOnClickListener(listener);
		wbcButton.setOnClickListener(listener);
		urineButton.setOnClickListener(listener);
		tableShowButton.setOnClickListener(listener);
		curveShowButton.setOnClickListener(listener);
		homeButton.setOnClickListener(listener);
		backButton.setOnClickListener(listener);
		ecgButton.setOnClickListener(listener);
	}

	Handler mHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mDiaog.cancel();
			switch (msg.what) {
			case OK:
				List<String[]> recoreds = (List<String[]>) msg.obj;
				datas.addAll(recoreds);
				if (recoreds.size() == PAGE_COUNT) {
					hasMore = true;// ���и���
					datas.add(new String[] { "", "���ظ���", "" });
				}
				mAdpter.notifyDataSetChanged();
				break;
			case NET_ERROR:
				if (pageIndex == 0) {// �����쳣,����0ҳʱ,��Ҫ����ʾ������������ݸ�ɾ��
					clearAndaddTitles();
					mAdpter.notifyDataSetChanged();
				}
				Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
				break;
			case NO_DATA:
				if (pageIndex == 0) {// ��0ҳû������ʱ,��Ҫ����ʾ������������ݸ�ɾ��
					clearAndaddTitles();
					mAdpter.notifyDataSetChanged();
				}
				Toast.makeText(context, "�����ݣ�", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private class DataDenerator implements Runnable {
		public Handler handler;
		private String path;
		private String token;
		private int pageIndex;

		public DataDenerator(Handler handler, String path, String token,
				int pageIndex) {
			this.handler = handler;
			this.path = path;
			this.token = token;
			this.pageIndex = pageIndex;
		}

		@Override
		public void run() {
			List<String[]> recoreds;
			if (token.equals(Tables.WBC)) {
				recoreds = getWbcData(pageIndex);
			} else
				recoreds = getQueueDatas(path, pageIndex, token);

			hasMore = false;// �ȼ����Ѿ�û�и���
			if (recoreds != null) {
				if (recoreds.size() == 0) {
					handler.obtainMessage(NO_DATA).sendToTarget();
				} else {
					handler.obtainMessage(OK, recoreds).sendToTarget();
				}
			} else
				handler.obtainMessage(NET_ERROR).sendToTarget();
		}

	}

	/***
	 * ��ӱ���,��������ʱ���������
	 */
	private void clearAndaddTitles() {
		datas.clear();
		datas.add(titles);
	}

	public List<String[]> getWbcData(int pIndex) {
		try {
			List<String[]> result = new ArrayList<String[]>();
			JSONObject resultJs = WebService.getWbcData(pIndex);
			if (resultJs == null) {
				return null;
			}
			int status = resultJs.getInt(WebService.STATUS_CODE);
			if (status != WebService.OK) {// ȡ����ʧ��
				return null;
			}
			JSONArray array = resultJs.getJSONArray(WebService.DATA);
			for (int i = 0; i < array.length(); i++) {
				JSONObject cJson = array.getJSONObject(i);
				result.add(new String[] {
						cJson.getString(WebService.CHECKTIME),
						cJson.getString(WebService.WBC), "*" });
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * ��ȡ��ѯ���ݵĲ������������еĲ�ѯ����������ͬ�ģ�ֻ�в�ѯ·����ͬ
	 * 
	 * @param pageIndex
	 * @return
	 * @throws JSONException 
	 */
	private JSONObject getQueueParas(Integer pageIndex) throws JSONException {
		JSONObject para = new JSONObject();
		para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
		para.put(WebService.CARDNO,  BaseActivity.getUser().getCardNo());
		JSONObject data = new JSONObject();
		String startTime = TimeHelper.getBeforeTime(365);
		String endTime = TimeHelper.getCurrentTime();
		data.put(WebService.ENDTIME, endTime);
		data.put(WebService.STARTTIME, startTime);	
		data.put(WebService.PAGECOUNT, PAGE_COUNT.toString());
		data.put(WebService.PAGEINDEX, pageIndex.toString());
		para.put(WebService.DATA, data);
		return para;
	}

	/***
	 * ��ȡ��ѯ�õ�������
	 * 
	 * @param pageIndex
	 * @param path
	 * @param token
	 * @return
	 */
	private List<String[]> getQueueDatas(String path, Integer pageIndex,
			String token) {		
		try {
			JSONObject para = getQueueParas(pageIndex);
			List<String[]> recoreds = DataService.getDataForTable(context,
					para, path, token);
			return recoreds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		bpButton = (Button) this.findViewById(R.id.show_bp);
		pulseButton = (Button) this.findViewById(R.id.show_pulse);
		boButton = (Button) this.findViewById(R.id.show_bo);
		tempButton = (Button) this.findViewById(R.id.show_temp);
		gluButton = (Button) this.findViewById(R.id.show_glu);
		uaButton = (Button) this.findViewById(R.id.show_ua);
		cholButton = (Button) this.findViewById(R.id.show_chol);
		wbcButton = (Button) this.findViewById(R.id.show_wbc);
		urineButton = (Button) this.findViewById(R.id.show_urine);
		tableShowButton = (Button) this.findViewById(R.id.table_show);
		curveShowButton = (Button) this.findViewById(R.id.curve_show);
		homeButton = (Button) this.findViewById(R.id.to_home_button);
		backButton = (Button) this.findViewById(R.id.return_button);
		listview = (ListView) this.findViewById(R.id.show_table_listview);
		ecgButton = (Button) this.findViewById(R.id.show_ecg);
	}

	@Override
	protected void onDestroy() {
		unbindService(connection);
		super.onDestroy();
	}

}
