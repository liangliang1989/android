package com.health.chronicdisease;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.health.archive.VisitBaseActivity;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;
import com.health.viewUtil.ChoiceEditText;
import com.health.viewUtil.MultiChoiceEditText;
import com.health.viewUtil.SingleChoiceEditText;

import cn.younext.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.opengl.GLSurfaceView.GLWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;



public class GlycuresisTure extends VisitBaseActivity {
	private DatabaseService dbService;
	private Context context;
	// ���½����־
	private static final int FRESH_UI = 0x10;
	// ȷ�ϰ�ť
	private static final int POSITIVE = 0x11;
	// ����ɹ�
	private static final int SAVE_OK = 0x12;
	// ����ʧ��
	private static final int SAVE_ERROE = 0x13;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case POSITIVE:
				// saveToDb();// ���浽���ݿ�

				break;
			case SAVE_OK:
				T.showShort(context, "����ɹ�");
				setState(true);
				finish();
				break;
			case SAVE_ERROE:
				// T.showShort(getActivity(), "����ʧ��");
				break;
			default:
				break;
			}
		}

	};
	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context=this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		sysId = getIntent().getStringExtra(SYS_ID);
		setContentView(R.layout.glycuresis_report);
		dbService = new DatabaseService(this);
		initView();
	}

	protected void initView() {

		setChoiceEditText();
		editHelpBtn = (Button) findViewById(R.id.glycuresis_edit_helpbutton);
		saveBtn = (Button) findViewById(R.id.glycuresis_btn_save);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.glycuresis_body);
		setState(!NEW.equals(sysId));// �½�ʱ���������������鿴ʱ����Ĭ������
		setText(R.id.glycuresis_turenum, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	/***
	 * �����ݿ��г�ʼ������
	 */
	private void initFromDb() {
		Cursor cursor = dbService.query(GlycuresisTable.GLYCURESIS_TABLE,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = GlycuresisTable.cloumIdmap;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			Log.i("entry.getKey()", entry.getKey());
			setTextFromCursor(cursor, entry.getKey(), entry.getValue());
		}
	}

	

	public void onClickButton(View v) {
		if (lock)
			setState(false);
		else
			saveToDb();

	}

	protected void setState(boolean lock) {
		L.i("setState", this.lock + "-->" + lock);
		this.lock = lock;
		if (lock) {
			saveBtn.setText("�޸�");
			editHelpBtn.setVisibility(View.VISIBLE);
			bodySv.setBackgroundColor(getResources().getColor(
					R.color.shallow_blue));

		} else {
			saveBtn.setText("����");
			editHelpBtn.setVisibility(View.GONE);
			bodySv.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		}
	}

	/***
	 * ���浽���ݿ�
	 */
	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : GlycuresisTable.cloumIdmap.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// �½�
			dbService.insert(GlycuresisTable.GLYCURESIS_TABLE, content);
		else {// �޸�
			dbService.update(GlycuresisTable.GLYCURESIS_TABLE, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}


	private void setChoiceEditText() {
		setChoiceEditText(R.id.glycuresis_sce_tureclass, new String[] {
				"��������", "���Ʋ�����", "������Ӧ", "����֢" }, null);
		setChoiceEditText(R.id.glycuresis_sce_mentaladjust, new String[] {
				"����", "һ��", "��", }, null);
		setChoiceEditText(R.id.glycuresis_sce_obey, new String[] { "����",
				"һ��", "��", }, null);
		setChoiceEditText(R.id.glycuresis_scedt_drugreaction,
				new String[] { "��" }, "��");
		setChoiceEditText(R.id.glycuresis_sce_pulse, new String[] { "δ����", "����" }, null);
		setChoiceEditText(R.id.glycuresis_symps, getResources()
				.getStringArray(R.array.select_glycuresissymptom), null);
		setChoiceEditText(R.id.glycuresis_turemethod, new String[] { "��ͥ",
				"�绰", "����" }, null);
		setChoiceEditText(R.id.glycuresis_sce_medicalobey,new String[] {"����","���","����ҩ"},null);
		
		setChoiceEditText(R.id.glycuresis_sce_rhg,new String[] {"��","ż��","Ƶ��"},null);
		}
}
	
	
	


