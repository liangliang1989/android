package com.health.chronicdisease;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import cn.younext.R;

import com.health.archive.VisitBaseActivity;
import com.health.archive.baby.BabyTable;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;

public class HypertensionTure extends VisitBaseActivity {
	

	private DatabaseService dbService;
	private Context context;
	// 更新界面标志
	private static final int FRESH_UI = 0x10;
	// 确认按钮
	private static final int POSITIVE = 0x11;
	// 保存成功
	private static final int SAVE_OK = 0x12;
	// 保存失败
	private static final int SAVE_ERROE = 0x13;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case POSITIVE:
				// saveToDb();// 保存到数据库

				break;
			case SAVE_OK:
				T.showShort(context, "保存成功");
				setState(true);
				finish();
				break;
			case SAVE_ERROE:
				// T.showShort(getActivity(), "保存失败");
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
		setContentView(R.layout.hypertension_report);
		dbService = new DatabaseService(this);
		initView();
	}

	protected void initView() {

		setChoiceEditText();
		editHelpBtn = (Button) findViewById(R.id.hypertension_edit_helpbutton);
		saveBtn = (Button) findViewById(R.id.hypertension_title_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		bodySv = findViewById(R.id.hypertension_body);
		setState(!NEW.equals(sysId));// 新建时，进来不锁定，查看时进来默认锁定
		setText(R.id.hypertension_turenum, Tables.getSerialId());
		if (!NEW.equals(sysId))
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					initFromDb();
				}

			}, 500);

	}

	/***
	 * 从数据库中初始化数据
	 */
	private void initFromDb() {
		Cursor cursor = dbService.query(HypertensionTable.HYPERTENSION_TABLE,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, Integer> map = HypertensionTable.cloumIdmap;
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
	 * 保存到数据库
	 */
	private void saveToDb() {
		ContentValues content = new ContentValues();
		for (Map.Entry<String, Integer> entry : HypertensionTable.cloumIdmap.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// 新建
			dbService.insert(HypertensionTable.HYPERTENSION_TABLE, content);
		else {// 修改
			dbService.update(HypertensionTable.HYPERTENSION_TABLE, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}


	private void setChoiceEditText() {
		setChoiceEditText(R.id.hypertension_sce_tureclass, new String[] {
				"控制满意", "控制不满意", "不良反应", "并发症" }, null);
		setChoiceEditText(R.id.hypertension_sce_mentaladjust, new String[] {
				"良好", "一般", "差", }, null);
		setChoiceEditText(R.id.hypertension_sce_obey, new String[] { "良好",
				"一般", "差", }, null);
		setChoiceEditText(R.id.hypertension_scedt_drugreaction,
				new String[] { "无", }, "有");
		setChoiceEditText(R.id.sce_salt, new String[] { "轻", "中", "重" }, null);
		setChoiceEditText(R.id.hypertension_symps, getResources()
				.getStringArray(R.array.select_hypertensionsymptom), null);
		setChoiceEditText(R.id.hypertension_turemethod, new String[] { "家庭",
				"电话", "门诊" }, null);
		setChoiceEditText(R.id.hypertension_sce_drugobey,new String[] {"规律","间断","不服药"},null);
		}

	
	
	

}
