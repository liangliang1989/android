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

public class TwoOldChildVisit extends VisitBaseActivity {

	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_archivel_twoold_visit);
		setChoiceEditText();
		initView();
	}

	protected void initView() {
		setChoiceEditText();
		setText(R.id.twoold_name, BaseActivity.getUser().getName());
		editHelpBtn = (Button) findViewById(R.id.edit_help_button);
		saveBtn = (Button) findViewById(R.id.twoold_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.table_body_sv);
		setState(!NEW.equals(sysId));// 新建时，进来不锁定，查看时进来默认锁定
		setText(R.id.twoold_serial_id, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	protected void initFromDb() {
		Cursor cursor = dbService.query(TwoOldChildTable.table,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = TwoOldChildTable.cloumIdmap;
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
		for (Map.Entry<String, Integer> entry : TwoOldChildTable.cloumIdmap
				.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// 新建
			dbService.insert(TwoOldChildTable.table, content);
		else {// 修改
			dbService.update(TwoOldChildTable.table, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();
	}

	protected void setState(boolean lock) {
		L.i("setState", this.lock + "-->" + lock);
		this.lock = lock;
		if (lock) {
			saveBtn.setText("修改");
			editHelpBtn.setVisibility(View.VISIBLE);
			bodySv.setBackgroundColor(getResources().getColor(
					R.color.shallow_blue));

		} else {
			saveBtn.setText("保存");
			editHelpBtn.setVisibility(View.GONE);
			bodySv.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		}
	}

	/***
	 * 设置选择项的内容
	 */
	private void setChoiceEditText() {
		// 月龄age
		setChoiceEditText(R.id.twoold_age, new String[] { "12月龄", "18月龄",
				"24月龄", "30月龄" }, null);
		// 面色face
		setChoiceEditText(R.id.twoold_face, new String[] { "1红润 " }, "2其他");
		// 皮肤skin
		setChoiceEditText(R.id.twoold_skin, new String[] { "1未见异常 " }, "2异常");
		// 前囟状况
		setChoiceEditText(R.id.twoold_bregma_state,
				new String[] { "1闭合", "2未闭" }, null);
		// 眼外观
		setChoiceEditText(R.id.twoold_eye, new String[] { "1未见异常 " }, "2异常");
		// 耳外观
		setChoiceEditText(R.id.twoold_ear, new String[] { "1未见异常 " }, "2异常");
		// 听力
		setChoiceEditText(R.id.twoold_hear, new String[] { "1通过", "2未通过" },
				null);
		// 心肺
		setChoiceEditText(R.id.twoold_heart_hear, new String[] { "1未见异常" },
				"2异常");
		// 腹部
		setChoiceEditText(R.id.twoold_abdomen_touch, new String[] { "1未见异常" },
				"2异常");
		// 脐部
		setChoiceEditText(R.id.twoold_funicle, new String[] { "1未见异常" }, "2异常");
		// 四肢
		setChoiceEditText(R.id.twoold_limbs, new String[] { "1未见异常" }, "2异常");

		// 可疑佝偻病体征
		setChoiceEditText(R.id.twoold_rickets_feature, new String[] { "1“O”型腿",
				"2“X”型腿" }, null);

		// 发育评估growth_ assess
		setChoiceEditText(R.id.twoold_growth_assess,
				new String[] { "通过", "未通过" }, null);
		// 两次随访间患病情况
		setChoiceEditText(R.id.twoold_seak_state, new String[] { "1未患病" },
				"2患病");
		// 转诊建议
		setChoiceEditText(R.id.twoold_transfer_advise, new String[] { "1无",
				"2有" }, null);
		// 指导
		setChoiceEditText(R.id.twoold_guide, new String[] { "1科学喂养", "2生长发育",
				"3疾病预防", "4预防意外伤害", "5口腔保健" }, null);
		// 中医健康管理
		setChoiceEditText(R.id.twoold_TCM, new String[] { "1饮食调养指导", "2起居调摄指导",
				"3摩腹、捏脊", "4健康教育处方" }, null);
	}

}
