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
		setState(!NEW.equals(sysId));// 新建时，进来不锁定，查看时进来默认锁定
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

		if (NEW.equals(sysId))// 新建
			dbService.insert(SixOldChildTable.table, content);
		else {// 修改
			dbService.update(SixOldChildTable.table, DataOpenHelper.SYS_ID,
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
		setChoiceEditText(R.id.sixold_age, new String[] { "3岁", "4岁", "5岁",
				"6岁" }, null);
		// 听力
		setChoiceEditText(R.id.sixold_hear, new String[] { "1通过", "2未通过" },
				null);
		// 心肺
		setChoiceEditText(R.id.sixold_heart_hear, new String[] { "1未见异常" },
				"2异常");
		// 腹部
		setChoiceEditText(R.id.sixold_abdomen_touch, new String[] { "1未见异常" },
				"2异常");
		// 发育评估growth_ assess
		setChoiceEditText(R.id.sixold_growth_assess,
				new String[] { "通过", "未通过" }, null);
		// 两次随访间患病情况
		setChoiceEditText(R.id.sixold_seak_state, new String[] { "1无", "2肺炎",
				"3腹泻", "4外伤" }, "其他");
		// 转诊建议
		setChoiceEditText(R.id.sixold_transfer_advise, new String[] { "1无",
				"2有" }, null);
		// 指导
		setChoiceEditText(R.id.sixold_guide, new String[] { "1科学喂养", "2生长发育",
				"3疾病预防", "4预防意外伤害", "5口腔保健" }, null);
		// 中医健康管理
		setChoiceEditText(R.id.sixold_TCM, new String[] { "1饮食调养指导", "2起居调摄指导",
				"3摩腹、捏脊", "4健康教育处方" }, null);
	}

}
