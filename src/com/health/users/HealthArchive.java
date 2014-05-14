package com.health.users;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.bean.Group;
import com.health.bean.User;
import com.health.database.Tables;
import com.health.myhealth.TableRecord;
import com.health.util.L;
import com.health.util.T;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;
import com.health.web.WebService.Archive;

/***
 * ��������,�����ģ��,ҽ���Ѿ���¼
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-3-19 ����2:16:07
 */
public class HealthArchive extends BaseActivity {
	private ScrollView contentSv;
	private Button modifyBtn;
	private boolean modifyTag = false;

	private TextView nameTv;
	private TextView sexTv;
	private TextView birthdayTv;
	private TextView idcardTv;

	private EditText raceEt;
	private EditText addressEt;
	private EditText phoneEt;
	private EditText emailEt;

	private RadioGroup degreeRg;
	private RadioGroup marreyStatusRg;
	private RadioGroup liveStyleRg;
	private RadioGroup houseStyleRg;
	private RadioGroup incomeRg;
	private RadioGroup hospitalCostPayRg;
	private static boolean confirmed = false;
	// private RadioGroup tizhiRg;

	// ����radio button��id
	private final int[] degreeIds = { R.id.degree_rb0, R.id.degree_rb1,
			R.id.degree_rb2, R.id.degree_rb3, R.id.degree_rb4, R.id.degree_rb5,
			R.id.degree_rb6 };
	private final int[] marreyIds = { R.id.marrey_status_rb0,
			R.id.marrey_status_rb1, R.id.marrey_status_rb2,
			R.id.marrey_status_rb3 };
	private final int[] liveStyleIds = { R.id.live_style_rb0,
			R.id.live_style_rb1, R.id.live_style_rb2, R.id.live_style_rb3,
			R.id.live_style_rb4 };

	private final int[] houseStyleIds = { R.id.house_style_rb0,
			R.id.house_style_rb1, R.id.house_style_rb2, R.id.house_style_rb3 };
	private final int[] incomeIds = { R.id.income_rb0, R.id.income_rb1,
			R.id.income_rb2, R.id.income_rb3, R.id.income_rb4, };
	private final int[] hospitalCostIds = { R.id.hospital_cost_pay_rb0,
			R.id.hospital_cost_pay_rb1, R.id.hospital_cost_pay_rb2,
			R.id.hospital_cost_pay_rb3, R.id.hospital_cost_pay_rb4 };
	private final int[] tizhiIds = { R.id.tizhi_rb0, R.id.tizhi_rb1,
			R.id.tizhi_rb2, R.id.tizhi_rb3, R.id.tizhi_rb4, R.id.tizhi_rb5,
			R.id.tizhi_rb6, R.id.tizhi_rb7, R.id.tizhi_rb8, R.id.tizhi_rb9, };

	private static MyHandler mHandler;
	private static final String TAG = "";
	private static final int GET_DATA_STATE = 0x819;
	private static final int TOAST_MSG = 0x820;
	private static final int UPLOAD_DATA_STATE = 0x821;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_archivel_basic_infor);
		initView();
//		initListener();
		mHandler = new MyHandler();
		setEditable(false);// ���ò��ɱ༭
		refreshHealthArchive();

	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_DATA_STATE:
				switch (msg.arg1) {
				case WebService.OK:
					JSONObject json = (JSONObject) msg.obj;
					showArchive(json);
					break;
				default:
					break;
				}
				break;
			case UPLOAD_DATA_STATE:
				switch (msg.arg1) {
				case WebService.OK:
					modifyTag = false;
					setEditable(modifyTag);
					T.showShort(context, "���浵���ɹ�");
					break;
				default:
					T.showShort(context, "����ʧ��");
					break;
				}
				break;
			case TOAST_MSG:
				String s = (String) msg.obj;
				T.showShort(context, s);
			default:
				break;
			}
		}
	}

	/***
	 * ��ȡ��������
	 */
	private void refreshHealthArchive() {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				getHealthArchive();
			}

		}).start();
	}

	/***
	 * ���û�����Ϣ
	 */
	private void setBasicInfo() {
		User user = BaseActivity.getUser().copy();
		user.replace("null", "");
		nameTv.setText(user.getName());
		birthdayTv.setText(user.getBirthday());
		sexTv.setText(user.getSex());
		idcardTv.setText(user.getCardNo());
		phoneEt.setText(user.getMobile());
		emailEt.setText(user.getEmail());
	}

	/***
	 * ���ø��ֵ���
	 * 
	 * @param result
	 */
	public void showArchive(JSONObject result) {
		setBasicInfo();
		try {
			JSONObject data = result.getJSONObject(WebService.DATA);
			data = replaceJsonNull(data, "null", "-1");

			// �Ļ��̶�
			check(data, Archive.EDU, degreeRg, degreeIds);
			int edu = data.getInt(Archive.EDU);
			if (edu >= 0 && edu <= degreeIds.length)
				degreeRg.check(degreeIds[edu]);
			// ����״��
			check(data, Archive.MARITAL_STATUS, marreyStatusRg, marreyIds);
			// ��ס����
			check(data, Archive.LIVING, liveStyleRg, liveStyleIds);
			// ס������
			check(data, Archive.HOUSING, houseStyleRg, houseStyleIds);
			// ������Դ
			check(data, Archive.LIVE_SOURCE, incomeRg, incomeIds);
			// ҽ��֧����ʽ
			check(data, Archive.MEDICAL_PAYMENT, hospitalCostPayRg,
					hospitalCostIds);

		} catch (JSONException e) {
			L.e(TAG, e.getMessage());
		}
	}

	/***
	 * ����groupbutton��ѡ��
	 * 
	 * @param data
	 * @param key
	 * @param group
	 * @param ids
	 * @throws JSONException
	 */
	private void check(JSONObject data, String key, RadioGroup group, int[] ids)
			throws JSONException {
		int index = data.getInt(key);
		if (index >= 0 && index < ids.length)
			group.check(ids[index]);
	}

	/***
	 * ��json��valueΪresource���ַ��滻Ϊdesc
	 * 
	 * @param data
	 * @param resource
	 * @param desc
	 * @return
	 * @throws JSONException
	 */
	private JSONObject replaceJsonNull(JSONObject data, String resource,
			String desc) throws JSONException {
		Iterator<String> iter = data.keys();
		JSONObject newJson = new JSONObject();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = data.getString(key);
			if (value.equals(resource))
				value = desc;
			newJson.put(key, value);
		}
		return newJson;
	}

	private void getHealthArchive() {
		try {
			User user = BaseActivity.getUser();
			Group group = BaseActivity.getGroup();
			if (user == null || group == null) {
				T.showLong(context, "��¼��ʱ,�����µ�¼!");
				mHandler.obtainMessage(WebService.LOGOUT).sendToTarget();
			} else {
				JSONObject para = new JSONObject();
				para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
				para.put(WebService.DOCTOR_NAME, group.getUserName());
				para.put(WebService.PASS_WORD, group.getPassword());
				para.put(WebService.CARDNO, user.getCardNo());
				StringBuilder sb = new StringBuilder(WebService.DOMAIN_V2);
				sb.append(Archive.GET_HEALTH_ARCHIVE);
				sb.append("?");
				// sb.append(para.toString());
				JSONObject result = WebService.httpConenction(para.toString(),
						sb.toString());
				int status = result.getInt(WebService.STATUS_CODE);
				mHandler.obtainMessage(GET_DATA_STATE, status, 0, result)
						.sendToTarget();
			}
		} catch (Exception e) {
			L.e(TAG, e.getMessage());
			mHandler.obtainMessage(GET_DATA_STATE, WebService.ERROE, 0)
					.sendToTarget();
		}
	}

	/***
	 * ����id
	 */
	private void initView() {
		contentSv = (ScrollView) findViewById(R.id.archive_content_sv);
		modifyBtn = (Button) findViewById(R.id.modify_archive);
		nameTv = (TextView) findViewById(R.id.name_tv);
		sexTv = (TextView) findViewById(R.id.sex_tv);
		birthdayTv = (TextView) findViewById(R.id.birthday_tv);
		idcardTv = (TextView) findViewById(R.id.idcard_tv);

		raceEt = (EditText) findViewById(R.id.race_et);
		addressEt = (EditText) findViewById(R.id.address_et);
		phoneEt = (EditText) findViewById(R.id.phone_et);
		emailEt = (EditText) findViewById(R.id.email_et);

		degreeRg = (RadioGroup) findViewById(R.id.degree_rg);
		marreyStatusRg = (RadioGroup) findViewById(R.id.marrey_status_rg);
		liveStyleRg = (RadioGroup) findViewById(R.id.live_style_rg);
		houseStyleRg = (RadioGroup) findViewById(R.id.house_style_rg);
		incomeRg = (RadioGroup) findViewById(R.id.income_rg);
		hospitalCostPayRg = (RadioGroup) findViewById(R.id.hospital_cost_pay_rg);
		// tizhiRg = (RadioGroup)
		// findViewById(R.id.tizhi_rg);

	}

	/***
	 * ����group����button�Ŀ���״̬
	 * 
	 * @param group
	 * @param status
	 */
	private void setEnable(RadioGroup group, boolean status) {
		int num = group.getChildCount();
		for (int i = 0; i < num; i++) {
			group.getChildAt(i).setEnabled(status);
		}
	}

	/***
	 * ������������Ŀɱ༭����״̬
	 * 
	 * @param status
	 */
	private void setEditable(boolean status) {
		raceEt.setEnabled(status);
		addressEt.setEnabled(status);
		phoneEt.setEnabled(status);
		emailEt.setEnabled(status);
		if (status) {
			contentSv.setBackgroundResource(R.drawable.centerbg);
			raceEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			addressEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			phoneEt.setInputType(InputType.TYPE_CLASS_PHONE);
			emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		} else {
			contentSv.setBackgroundColor(getResources().getColor(
					R.color.center_bg_blue));
			raceEt.setInputType(InputType.TYPE_NULL);
			addressEt.setInputType(InputType.TYPE_NULL);
			phoneEt.setInputType(InputType.TYPE_NULL);
			emailEt.setInputType(InputType.TYPE_NULL);
		}

		setEnable(degreeRg, status);
		setEnable(marreyStatusRg, status);
		setEnable(liveStyleRg, status);
		setEnable(degreeRg, status);
		setEnable(houseStyleRg, status);
		setEnable(incomeRg, status);
		setEnable(hospitalCostPayRg, status);
		if (status)
			modifyBtn.setText("����");
		else
			modifyBtn.setText("�޸�");
	}

	/***
	 * ����޸�,��Ӧ����Ŀ��޸�
	 * 
	 * @param view
	 */
	public void modifyArchive(View view) {
		if (modifyTag) {// �ɱ༭״̬
			dialog();
		} else {
			modifyTag = !modifyTag;
			setEditable(modifyTag);

		}
	}

	/***
	 * �����Ի�����ʾ�Ƿ񱣴��޸�
	 */
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("�Ƿ񱣴��޸ģ�");
		builder.setTitle("��ʾ");

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new BackGroundThread(new BackGroundTask() {

					@Override
					public void process() {
						uploadArchive();
					}

				}).start();
				T.showShort(context, "��̨��ʼ�ϴ�");

			}
		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/***
	 * �ϴ��޸ĵĵ���
	 */
	private void uploadArchive() {
		String address = addressEt.getText().toString();
		String race = raceEt.getText().toString();
		String phone = phoneEt.getText().toString();
		String email = emailEt.getText().toString();
		JSONObject data = new JSONObject();
		Group group = BaseActivity.getGroup();
		User user = BaseActivity.getUser();
		if (user == null || group == null) {
			mHandler.obtainMessage(TOAST_MSG, "��¼��ʱ�������µ�¼��").sendToTarget();
			return;
		}
		try {
			if (phone != null)
				data.put(Archive.MOBILE, phone);
			if (email != null && email.length() > 1) {
				if (email.contains("@"))
					data.put(Archive.EMAIL, email);
				else {
					mHandler.obtainMessage(TOAST_MSG, "�ʼ���ַ��ʽ����ȷ")
							.sendToTarget();
					return;
				}
			}
			putCheckStatus(data, degreeRg, degreeIds, Archive.EDU);
			putCheckStatus(data, marreyStatusRg, marreyIds,
					Archive.MARITAL_STATUS);
			putCheckStatus(data, liveStyleRg, liveStyleIds, Archive.LIVING);
			putCheckStatus(data, hospitalCostPayRg, hospitalCostIds,
					Archive.HOUSING);
			putCheckStatus(data, incomeRg, incomeIds, Archive.LIVE_SOURCE);
			putCheckStatus(data, hospitalCostPayRg, hospitalCostIds,
					Archive.MEDICAL_PAYMENT);
			data.put(Archive.MEMBERCODE, user.getCustomerGuid());
			data.put(WebService.NAME, user.getName());
			data.put(WebService.SEX, user.getSex());
			data.put(WebService.BIRTHDAY, user.getBirthday());
			JSONObject para = new JSONObject();
			para.put(WebService.DATA, data);
			para.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			para.put(WebService.CARDNO, user.getCardNo());
			para.put(WebService.DOCTOR_NAME, group.getUserName());
			para.put(WebService.PASS_WORD, group.getPassword());
			L.i("user.getCustomerGuid():", user.getCustomerGuid());
			para.put(WebService.CUSTOMERGUID, user.getCustomerGuid());
			StringBuilder sb = new StringBuilder(WebService.DOMAIN_V2);
			sb.append(Archive.UPLOAD_HEALTH_ARCHIVE);
			sb.append("?");
			// sb.append(para.toString());
			JSONObject result = WebService.httpConenction(para.toString(),
					sb.toString());
			int status = result.getInt(WebService.STATUS_CODE);
			mHandler.obtainMessage(UPLOAD_DATA_STATE, status, 0).sendToTarget();

		} catch (Exception e) {
			e.printStackTrace();
			mHandler.obtainMessage(UPLOAD_DATA_STATE, WebService.ERROE, 0)
					.sendToTarget();
		}
	}

	/***
	 * ���RadioGroup�ĺ����Ƿ��б�ѡ�У�����У�����Ӻ��ӵ��±�
	 * 
	 * @param data
	 * @param group
	 * @param ids
	 * @param key
	 * @throws JSONException
	 */
	private void putCheckStatus(JSONObject data, RadioGroup group, int[] ids,
			String key) throws JSONException {
		int id = group.getCheckedRadioButtonId();
		if (id != -1) {
			int index = getRbIdIndex(ids, id);
			if (index != -1)
				data.put(key, index);
		}
	}

	/***
	 * ��radiogroup�����ҵ�id���±�
	 * 
	 * @param ids
	 * @param id
	 * @return
	 */
	public int getRbIdIndex(int[] ids, int id) {
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == id) {
				return i;
			}
		}
		return -1;
	}

	private TextView xueyaRecTV;
	private TextView mailvRecTV;
	private TextView xueyangRecTV;
	private TextView tiwenRecTV;
	private TextView putaotangRecTV;
	private TextView niaosuanRecTV;
	private TextView zongdanggucunRecTV;
	private TextView baixibaoRecTV;
	private TextView niaochangguiRecTV;

	// ///////////////////////////////////////////////////////
	/*private void initListener() {
		xueyaRecTV = (TextView) findViewById(R.id.tv_rec_xueya);
		mailvRecTV = (TextView) findViewById(R.id.tv_rec_mailv);
		xueyangRecTV = (TextView) findViewById(R.id.tv_rec_xueyang);
		tiwenRecTV = (TextView) findViewById(R.id.tv_rec_tiwen);
		putaotangRecTV = (TextView) findViewById(R.id.tv_rec_putaotang);
		niaosuanRecTV = (TextView) findViewById(R.id.tv_rec_niaosuan);
		zongdanggucunRecTV = (TextView) findViewById(R.id.tv_rec_zongduangucun);
		niaochangguiRecTV = (TextView) findViewById(R.id.tv_rec_niaochanggui);
		baixibaoRecTV = (TextView) findViewById(R.id.tv_rec_baixibao);
		xueyaRecTV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.SBP);
				startActivity(intent);

			}
		});
		mailvRecTV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.PULSE);
				startActivity(intent);
			}
		});
		xueyangRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.BO);
				startActivity(intent);
			}
		});
		tiwenRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.TEMP);
				startActivity(intent);
			}
		});
		putaotangRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.GLU);
				startActivity(intent);
			}
		});
		niaosuanRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.UA);
				startActivity(intent);
			}
		});
		zongdanggucunRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.CHOL);
				startActivity(intent);
			}
		});
		baixibaoRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", Tables.WBC);
				startActivity(intent);
			}
		});
		niaochangguiRecTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthArchive.this,
						TableRecord.class);
				intent.putExtra("item", "Urine");
				startActivity(intent);
			}
		});
	}*/
}
