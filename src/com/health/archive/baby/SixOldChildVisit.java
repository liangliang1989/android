package com.health.archive.baby;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.archive.VisitBaseActivity;
import com.health.database.DataOpenHelper;
import com.health.database.Tables;
import com.health.util.L;

public class SixOldChildVisit extends VisitBaseActivity {

	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_archivel_sixold_visit);
		setChoiceEditText();
		initView();
	}

	protected void initView() {
		setChoiceEditText();
		setText(R.id.sixold_name, BaseActivity.getUser().getName());
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.sixold_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// �½�ʱ���������������鿴ʱ����Ĭ������
		setText(R.id.sixold_serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	protected void initFromDb() {
		Cursor cursor = dbService.query(SixOldChildTable.table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = SixOldChildTable.cloumIdmap;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			setTextFromCursor(cursor, entry.getKey(), entry.getValue());
		}

	}

	protected void onClickButton(View v) {
		if (lock)
			setState(false);
		else
			saveToDb();
	}

	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : SixOldChildTable.cloumIdmap
				.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// �½�
			dbService.insert(SixOldChildTable.table, content);
		else {// �޸�
			dbService.update(SixOldChildTable.table, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();
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
	 * ����ѡ���������
	 */
	private void setChoiceEditText() {
		// ����age
		setChoiceEditText(R.id.sixold_age, new String[] { "3��", "4��", "5��",
				"6��" }, null);
		// ����
		setChoiceEditText(R.id.sixold_hear, new String[] { "1ͨ��", "2δͨ��" },
				null);
		// �ķ�
		setChoiceEditText(R.id.sixold_heart_hear, new String[] { "1δ���쳣" },
				"2�쳣");
		// ����
		setChoiceEditText(R.id.sixold_abdomen_touch, new String[] { "1δ���쳣" },
				"2�쳣");
		// ��������growth_ assess
		setChoiceEditText(R.id.sixold_growth_assess,
				new String[] { "ͨ��", "δͨ��" }, null);
		// ������ü仼�����
		setChoiceEditText(R.id.sixold_seak_state, new String[] { "1��", "2����",
				"3��к", "4����" }, "����");
		// ת�ｨ��
		setChoiceEditText(R.id.sixold_transfer_advise, new String[] { "1��",
				"2��" }, null);
		// ָ��
		setChoiceEditText(R.id.sixold_guide, new String[] { "1��ѧι��", "2��������",
				"3����Ԥ��", "4Ԥ�������˺�", "5��ǻ����" }, null);
		// ��ҽ��������
		setChoiceEditText(R.id.sixold_TCM, new String[] { "1��ʳ����ָ��", "2��ӵ���ָ��",
				"3Ħ������", "4������������" }, null);
	}

}
