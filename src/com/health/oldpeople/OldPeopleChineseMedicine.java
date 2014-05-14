package com.health.oldpeople;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.health.BaseActivity;
import com.health.archive.VisitBaseActivity;
import com.health.archive.baby.BabyHomeVistit;
import com.health.archive.baby.BabyTable;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.T;

import cn.younext.R;
import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class OldPeopleChineseMedicine extends VisitBaseActivity {

	private DatabaseService dbService;
	int flag = -1;
	private Context context;
	private int[] score = new int[100];
	private Collection<int[]> c = new HashSet<int[]>(Arrays.asList(new int[] {
			2, 4, 5, 13 }));
	private int[] physcialscore = new int[10];
	private int i;
	private int id;
	
	private Map<String, int[]> map = OldPeopleChineseMedicineTable.cloumIdmap;
	
	private boolean lock = false;
	private Button editHelpBtn;
	private Button saveBtn;
	private Button submitbtn;
	private View bodySv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 来自于查看还是新建,0表示新建,1表示查看
		sysId = getIntent().getStringExtra(SYS_ID);
		setContentView(R.layout.oldpeopleservice);
		dbService = new DatabaseService(this);
		context = this;
		
		initView();
		

	}

	private void setOnRatingBarChangeListener() {
		for (i = 1; i <= 33; i++) {

			id = map.get("Tittle" + i)[1];

			Log.i("Tittle" + i, String.valueOf(id));
			((RatingBar) findViewById(id))
					.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

						@Override
						public void onRatingChanged(RatingBar ratingBar,
								float rating, boolean fromUser) {
							// TODO Auto-generated method stub
							//System.out.println("rating ---->" + rating);

							//score[i] = (int) rating;
							// Log.i("Tittle" + i, String.valueOf(id));
							//Log.i("score[" + i + "]", String.valueOf(score[i]));
						}

					});
		}
	}

	private void getcount() {

		for (i = 1; i <= 33; i++) {

			id = map.get("Tittle" + i)[1];

			Log.i("Tittle" + i, String.valueOf(id));
			score[i]=(int) ((RatingBar) findViewById(id)).getRating();
			Log.i("score[" + i + "]", String.valueOf(score[i]));
			if(score[i]==0){
				Toast.makeText(context, "每一项均不能为空，请补充完整！", 500);
				
			}		

			// if (i != 0 && c.contains(i))
			// score[i] = 6 - score[i];

		}
		physcialscore[1] = score[2] + score[3] + score[4] + score[14];
		physcialscore[2] = score[11] + score[12] + score[13] + score[29];
		physcialscore[3] = score[10] + score[21] + score[26] + score[31];
		physcialscore[4] = score[9] + score[16] + score[28] + score[32];
		physcialscore[5] = score[23] + score[25] + score[27] + score[30];
		physcialscore[6] = score[19] + score[22] + score[24] + score[33];
		physcialscore[7] = score[5] + score[6] + score[7] + score[8];
		physcialscore[8] = score[15] + score[17] + score[18] + score[20];
		physcialscore[9] = 24 + score[1] - score[2] - score[4] - score[5]
				- score[13];

	}

	protected void initView() {

		setChoiceEditText();
		setText(R.id.patient_name, BaseActivity.getUser().getName());
		setOnRatingBarChangeListener();
		editHelpBtn = (Button) findViewById(R.id.oldpeople_edit_helpbutton);
		saveBtn = (Button) findViewById(R.id.oldpeopleservice_save);
		submitbtn = (Button) findViewById(R.id.oldpeopleservice_submit);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickButton(v);
			}
		});
		submitbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getcount();
				LinearLayout layout = (LinearLayout) findViewById(R.id.oldpeopleservice_showview);
				layout.setVisibility(0);
				layout.setFocusable(true);
				
				
				for (int i = 1; i <= 9; i++) {
					Log.i("physcialclass[" + i + "]", String.valueOf(physcialscore[i]));
					int id = map.get("physcialclass" + i)[1];
					if (i != 9) {

						if (physcialscore[i] < 9) {
							((EditText) findViewById(id)).setText("否");
						} else if (physcialscore[i] < 11) {
							((EditText) findViewById(id)).setText("基本是");
						} else {
							((EditText) findViewById(id)).setText("是");
						}
					} else {
						if (physcialscore[9] >= 17) {// 如果平和质得分大于17，
							for (int j = 1; j < 9; j++) {
								if (physcialscore[j] > 9) {
									flag = 0;// 否状态
									break;
								} else if (physcialscore[j] > 7) {
									flag = 1;// 基本是状态
								}

							}
						}
						else{
							flag=0;
						}
						if (flag == -1) {
							((EditText) findViewById(id)).setText("是");
						}
						if (flag == 0) {
							((EditText) findViewById(id)).setText("否");
						}
						if (flag == 1) {
							((EditText) findViewById(id)).setText("基本是");
						}
					}
				}

			}
		});
		bodySv = findViewById(R.id.oldpeopleservice_body);
		setState(!NEW.equals(sysId));// 新建时，进来不锁定，查看时进来默认锁定
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
	 * 从数据库中初始化数据
	 */
	private void initFromDb() {
		Cursor cursor = dbService.query(
				OldPeopleChineseMedicineTable.OLDPEOPLETABLE,
				DataOpenHelper.SYS_ID, sysId);
		if (cursor.getCount() == 0)
			return;
		cursor.moveToNext();
		Map<String, int[]> map = OldPeopleChineseMedicineTable.cloumIdmap;
		for (Entry<String, int[]> entry : map.entrySet()) {
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
		for (Entry<String, int[]> entry : OldPeopleChineseMedicineTable.cloumIdmap
				.entrySet()) {
			if(entry.getValue()[0]==0){
			content.put(entry.getKey(), getEditTextString(entry.getValue()[1]));
			}
			if(entry.getValue()[0]==1){
				content.put(entry.getKey(), getRatingbarValue(entry.getValue()[1]));
				}
		}

		if (NEW.equals(sysId))// 新建
			dbService.insert(OldPeopleChineseMedicineTable.OLDPEOPLETABLE,
					content);
		else {// 修改
			dbService.update(OldPeopleChineseMedicineTable.OLDPEOPLETABLE,
					DataOpenHelper.SYS_ID, sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	/***
	 * 设置选择项的内容
	 */
	private void setChoiceEditText() {
		// 母亲妊娠期患病情况
		setChoiceEditText(R.id.oldpeopleservice_mce_physcialguide,
				new String[] { "情志调摄", "饮食调养", "起居调摄", "运动保健", "穴位保健" }, "其他");
	}
	
	private int getRatingbarValue(int id){
		
		return (int) ((RatingBar) findViewById(id)).getRating();

	}
}
