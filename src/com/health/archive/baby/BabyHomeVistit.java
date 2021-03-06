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
 * 新生儿家庭访视记录表
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-15 上午10:51:11
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
		for (Map.Entry<String, Integer> entry : BabyTable.cloumIdmap.entrySet()) {
			content.put(entry.getKey(), getEditTextString(entry.getValue()));
		}

		if (NEW.equals(sysId))// 新建
			dbService.insert(BabyTable.baby_table, content);
		else {// 修改
			dbService.update(BabyTable.baby_table, DataOpenHelper.SYS_ID,
					sysId, content);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();

	}

	/***
	 * 设置选择项的内容
	 */
	private void setChoiceEditText() {
		// 母亲妊娠期患病情况
		setChoiceEditText(R.id.baby_m_health,
				new String[] { "1糖尿病", "2妊娠期高血压" }, "3异常");
		// 出生情况
		setChoiceEditText(R.id.baby_birth_state, new String[] { "1顺产", "2胎头吸引",
				"3产钳", "4剖宫", "5双多胎", "6臀位" }, "7其他");
		// 新生儿窒息
		setChoiceEditText(R.id.baby_apnea, new String[] { "1无", "2有" }, null);
		// Apgar评分
		setChoiceEditText(R.id.baby_apgar_score, new String[] { "1一分钟",
				" 2五分钟", "3不详" }, null);
		// 是否有畸型
		setChoiceEditText(R.id.baby_malformation, new String[] { "1无" }, "2有");
		// 新生儿听力筛查
		setChoiceEditText(R.id.baby_hearing_check, new String[] { "1通过",
				"2未通过 ", "3未筛查", "4不详" }, null);
		// 新生儿疾病筛查
		setChoiceEditText(R.id.baby_sick_check,
				new String[] { "1甲低", "2 苯丙酮尿症" }, "3其他遗传代谢病");
		// 喂养方式
		setChoiceEditText(R.id.baby_nurse_pattern, new String[] { "1纯母乳",
				"2混合", "3人工" }, null);
		// 呕吐
		setChoiceEditText(R.id.baby_emesis, new String[] { "1 无", "2 有" }, null);
		// 大便
		setChoiceEditText(R.id.baby_excrement, new String[] { "1糊状", " 2 稀" },
				null);
		// 面色
		setChoiceEditText(R.id.baby_complexion, new String[] { "1红润 ", "2黄染" },
				"3其他");
		// 黄疸部位
		setChoiceEditText(R.id.baby_jaundice_part, new String[] { "1面部 ",
				"2躯干", "3四肢", "4 手足" }, null);
		// 前囟状况
		setChoiceEditText(R.id.baby_bregma_state, new String[] { "1正常 ", "2膨隆",
				"3凹陷 " }, "4其他");
		// 眼外观
		setChoiceEditText(R.id.baby_eye, new String[] { "1未见异常" }, "2异常");
		// 四肢活动度
		setChoiceEditText(R.id.baby_limbs, new String[] { "1未见异常" }, "2异常");
		// 耳外观
		setChoiceEditText(R.id.baby_ear, new String[] { "1未见异常" }, "2异常");
		// 颈部包块
		setChoiceEditText(R.id.baby_neck_block, new String[] { "1无" }, "2有");
		// 鼻
		setChoiceEditText(R.id.baby_nose, new String[] { "1未见异常" }, "2异常");
		// 皮肤
		setChoiceEditText(R.id.baby_skin,
				new String[] { "1未见异常", "2湿疹", " 3糜烂" }, " 4其他");
		// 口 腔
		setChoiceEditText(R.id.baby_mouth, new String[] { "1未见异常" }, "2异常");
		// 肛门
		setChoiceEditText(R.id.baby_heart_hear, new String[] { "1未见异常" }, "2异常");
		// 心肺听诊
		setChoiceEditText(R.id.baby_anus, new String[] { "1未见异常" }, "2异常");
		// 外生殖器
		setChoiceEditText(R.id.baby_externalia, new String[] { "1未见异常" }, "2异常");
		// 腹部触诊
		setChoiceEditText(R.id.baby_abdomen_touch, new String[] { "1未见异常" },
				"2异常");
		// 脊柱
		setChoiceEditText(R.id.baby_spine, new String[] { "1未见异常" }, "2异常");
		// 脐带
		setChoiceEditText(R.id.baby_funicle, new String[] { "1未脱", " 2脱落",
				" 3脐部有渗出" }, "4其他");
		// 转诊建议
		setChoiceEditText(R.id.baby_transfer_advise,
				new String[] { "1无", "2有" }, null);
		// 指导
		setChoiceEditText(R.id.baby_guide, new String[] { "1喂养指导", "2发育指导",
				" 3防病指导", "4预防伤害指导 ", "5口腔保健指导" }, null);
	}

}
