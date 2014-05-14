package com.health.archive.baby;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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

/***
 * ��������ͥ���Ӽ�¼��
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-15 ����10:51:11
 */
public class BabyHomeVistit extends VisitBaseActivity {

	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_archivel_baby_visit);

		initView();
	}

	protected void initView() {

		setChoiceEditText();
		setText(R.id.baby_name, BaseActivity.getUser().getName());
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.baby_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// �½�ʱ���������������鿴ʱ����Ĭ������
		setText(R.id.serial_id, Tables.getSerialId());
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
		Cursor cursor = dbService.query(BabyTable.baby_table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = BabyTable.cloumIdmap;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
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
		for (Map.Entry<String, Integer> entry : BabyTable.cloumIdmap.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// �½�
			dbService.insert(BabyTable.baby_table, content);
		else {// �޸�
			dbService.update(BabyTable.baby_table, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	/***
	 * ����ѡ���������
	 */
	private void setChoiceEditText() {
		// ĸ�������ڻ������
		setChoiceEditText(R.id.baby_m_health,
				new String[] { "1����", "2�����ڸ�Ѫѹ" }, "3�쳣");
		// �������
		setChoiceEditText(R.id.baby_birth_state, new String[] { "1˳��", "2̥ͷ����",
				"3��ǯ", "4�ʹ�", "5˫��̥", "6��λ" }, "7����");
		// ��������Ϣ
		setChoiceEditText(R.id.baby_apnea, new String[] { "1��", "2��" }, null);
		// Apgar����
		setChoiceEditText(R.id.baby_apgar_score, new String[] { "1һ����",
				" 2�����", "3����" }, null);
		// �Ƿ��л���
		setChoiceEditText(R.id.baby_malformation, new String[] { "1��" }, "2��");
		// ����������ɸ��
		setChoiceEditText(R.id.baby_hearing_check, new String[] { "1ͨ��",
				"2δͨ�� ", "3δɸ��", "4����" }, null);
		// ����������ɸ��
		setChoiceEditText(R.id.baby_sick_check,
				new String[] { "1�׵�", "2 ����ͪ��֢" }, "3�����Ŵ���л��");
		// ι����ʽ
		setChoiceEditText(R.id.baby_nurse_pattern, new String[] { "1��ĸ��",
				"2���", "3�˹�" }, null);
		// Ż��
		setChoiceEditText(R.id.baby_emesis, new String[] { "1 ��", "2 ��" }, null);
		// ���
		setChoiceEditText(R.id.baby_excrement, new String[] { "1��״", " 2 ϡ" },
				null);
		// ��ɫ
		setChoiceEditText(R.id.baby_complexion, new String[] { "1���� ", "2��Ⱦ" },
				"3����");
		// ���㲿λ
		setChoiceEditText(R.id.baby_jaundice_part, new String[] { "1�沿 ",
				"2����", "3��֫", "4 ����" }, null);
		// ǰض״��
		setChoiceEditText(R.id.baby_bregma_state, new String[] { "1���� ", "2��¡",
				"3���� " }, "4����");
		// �����
		setChoiceEditText(R.id.baby_eye, new String[] { "1δ���쳣" }, "2�쳣");
		// ��֫���
		setChoiceEditText(R.id.baby_limbs, new String[] { "1δ���쳣" }, "2�쳣");
		// �����
		setChoiceEditText(R.id.baby_ear, new String[] { "1δ���쳣" }, "2�쳣");
		// ��������
		setChoiceEditText(R.id.baby_neck_block, new String[] { "1��" }, "2��");
		// ��
		setChoiceEditText(R.id.baby_nose, new String[] { "1δ���쳣" }, "2�쳣");
		// Ƥ��
		setChoiceEditText(R.id.baby_skin,
				new String[] { "1δ���쳣", "2ʪ��", " 3����" }, " 4����");
		// �� ǻ
		setChoiceEditText(R.id.baby_mouth, new String[] { "1δ���쳣" }, "2�쳣");
		// ����
		setChoiceEditText(R.id.baby_heart_hear, new String[] { "1δ���쳣" }, "2�쳣");
		// �ķ�����
		setChoiceEditText(R.id.baby_anus, new String[] { "1δ���쳣" }, "2�쳣");
		// ����ֳ��
		setChoiceEditText(R.id.baby_externalia, new String[] { "1δ���쳣" }, "2�쳣");
		// ��������
		setChoiceEditText(R.id.baby_abdomen_touch, new String[] { "1δ���쳣" },
				"2�쳣");
		// ����
		setChoiceEditText(R.id.baby_spine, new String[] { "1δ���쳣" }, "2�쳣");
		// ���
		setChoiceEditText(R.id.baby_funicle, new String[] { "1δ��", " 2����",
				" 3�겿������" }, "4����");
		// ת�ｨ��
		setChoiceEditText(R.id.baby_transfer_advise,
				new String[] { "1��", "2��" }, null);
		// ָ��
		setChoiceEditText(R.id.baby_guide, new String[] { "1ι��ָ��", "2����ָ��",
				" 3����ָ��", "4Ԥ���˺�ָ�� ", "5��ǻ����ָ��" }, null);
	}

}
