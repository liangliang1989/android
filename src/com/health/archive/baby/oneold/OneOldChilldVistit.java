package com.health.archive.baby.oneold;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.archive.VisitBaseActivity;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;

public class OneOldChilldVistit extends VisitBaseActivity {	

	

	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.health_archivel_oneold_visit);		
		initView();
	}

	protected void initView() {
		setChoiceEditText();
		setText(R.id.oneold_name,BaseActivity.getUser().getName());
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.oneold_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// �½�ʱ���������������鿴ʱ����Ĭ������
		setText(R.id.oneold_serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	private void setChoiceEditText() {
		// ����
		setChoiceEditText(R.id.oneold_age, new String[] { "����", "3����", "6����",
				"8����" }, null);
		// ��ɫ���
		setChoiceEditText(R.id.oneold_face, new String[] { "1����", "2��Ⱦ" }, "3����");
		// Ƥ��
		setChoiceEditText(R.id.oneold_skin, new String[] { "1δ���쳣 " }, "2�쳣");
		// ǰض״��
		setChoiceEditText(R.id.oneold_bregma_state,
				new String[] { "1�պ�", "2δ��" }, null);
		// ��������
		setChoiceEditText(R.id.oneold_neck_block, new String[] { "��", "��" },
				null);
		// �����
		setChoiceEditText(R.id.oneold_eye, new String[] { "1δ���쳣 " }, "2�쳣");
		// �����
		setChoiceEditText(R.id.oneold_ear, new String[] { "1δ���쳣 " }, "2�쳣");// ��ɫ���
		// ����
		setChoiceEditText(R.id.oneold_hear, new String[] { "1ͨ��", "2δͨ��" }, null);
		// ��ǻ
		setChoiceEditText(R.id.oneold_mouth, new String[] { "1δ���쳣" }, "2�쳣");
		// �ķ�
		setChoiceEditText(R.id.oneold_heart_hear, new String[] { "1δ���쳣" },
				"2�쳣");
		// ����
		setChoiceEditText(R.id.oneold_abdomen_touch, new String[] { "1δ���쳣" },
				"2�쳣");
		// �겿
		setChoiceEditText(R.id.oneold_funicle, new String[] { "1δ���쳣" }, "2�쳣");
		// ��֫
		setChoiceEditText(R.id.oneold_limbs, new String[] { "1δ���쳣" }, "2�쳣");
		// �������Ͳ�֢״
		setChoiceEditText(R.id.oneold_rickets_sign, new String[] { "1��", "2ҹ��",
				"3�ູ", "4����" }, null);

		// �������Ͳ�����
		setChoiceEditText(R.id.oneold_rickets_feature,
				new String[] { "1��", "2­����", "3��­", "4��ͺ", "5�ߴ���", "6���ⷭ",
						"7����ǹ�", "8����", "9������" }, null);
		// ����/����ֳ��
		setChoiceEditText(R.id.oneold_externalia, new String[] { "1δ���쳣" },
				"2�쳣");
		// ��������growth_ assess
		setChoiceEditText(R.id.oneold_growth_assess,
				new String[] { "ͨ��", "δͨ��" }, null);
		// ������ü仼�����
		setChoiceEditText(R.id.oneold_seak_state, new String[] { "1δ����" }, "2����");
		// ת�ｨ��
		setChoiceEditText(R.id.oneold_transfer_advise,
				new String[] { "1��", "2��" }, null);
		// ָ��
		setChoiceEditText(R.id.oneold_guide, new String[] { "1��ѧι��", "2��������",
				"3����Ԥ��", "4Ԥ�������˺�", "5��ǻ����" }, null);
		// ��ҽ��������
		setChoiceEditText(R.id.oneold_TCM, new String[] { "1��ʳ����ָ��", "2��ӵ���ָ��",
				"3Ħ������", "4������������" }, null);
	}

	protected void onClickButton(View v) {
		if (lock)
			setState(false);
		else
			saveToDb();
	}

	/***
	 * ���浽���ݿ�
	 */
	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : OneOldChildTable.cloumIdmap
				.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// �½�
			dbService.insert(OneOldChildTable.oneold_table, content);
		else {// �޸�
			dbService.update(OneOldChildTable.oneold_table,
					DataOpenHelper.SYS_ID, sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	protected void initFromDb() {
		Cursor cursor = dbService.query(OneOldChildTable.oneold_table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = OneOldChildTable.cloumIdmap;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			setTextFromCursor(cursor, entry.getKey(), entry.getValue());
		}
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
}
