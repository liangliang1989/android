package com.health.myhealth;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.ThreeCloumAdpter;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.DataSaver;
import com.health.web.WebService;

/**
 * 本地测量记录
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-11-27 上午10:59:34
 */
public class LocalRecord extends BaseActivity {
	private Context context;
	private ThreeCloumAdpter mAdapter;
	private ListView listview;
	private List<String[]> datas;

	private static final int[] ids = { R.id.show_ecg, R.id.show_bp,
			R.id.show_bo, R.id.show_temp, R.id.show_glu, R.id.show_ua,
			R.id.show_chol, R.id.show_wbc, R.id.show_urine, };
	private static final int UPLOAD_RESULT = 0;
	private DatabaseService dbService;
	private int lastPressBtnId = ids[1];
	private String[] title = { "测量时间", "测量值", "上传状态" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latest_measure);
		context = this;
		dbService = new DatabaseService(context);
		datas = new ArrayList<String[]>();
		mAdapter = new ThreeCloumAdpter(context, datas);
		listview = (ListView) findViewById(R.id.show_table_listview);
		listview.setAdapter(mAdapter);

		listview.setOnItemLongClickListener(listener);
		setButtonState(lastPressBtnId);
		updataBpDatas();
	}

	private OnItemLongClickListener listener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			if (position > 0) {
				final String[] line = (String[]) parent
						.getItemAtPosition(position);
				if (UPLOAD_NO.equals(line[2])){// 还没有上传的
					new BackGroundThread(new BackGroundTask() {

						@Override
						public void process() {
							reUpload(line[3], line[4]);
						}

					}).start();
					Toast.makeText(context, "开始上传", Toast.LENGTH_SHORT).show();
					return true;//上传数据时震动提示一下
				}
			}
			return false;
		}

	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_RESULT:
				if (msg.arg1 == WebService.OK) {
					Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
					ContentValues cv = new ContentValues(1);
					cv.put(WebService.STATUS_CODE,
							String.valueOf(WebService.OK));
					int rows = dbService
							.update((String) msg.obj, DataOpenHelper.SYS_ID,
									String.valueOf(msg.arg2), cv);
					if (rows > 0)// 更新成功
						switchSelect(lastPressBtnId);
				} else
					Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();
			}
		}
	};

	private void reUpload(String sysId, String table) {
		Cursor cursor = dbService.query(table, DataOpenHelper.SYS_ID, sysId);

		while (cursor.moveToNext()) {
			try {
				JSONObject upPara = new JSONObject(getCursorString(cursor,
						Tables.UPLOAD_PARA));
				String path = upPara.getString(DataSaver.PATH);
				JSONObject para = upPara.getJSONObject(DataSaver.PARA);
				JSONObject result = WebService.postConenction(para, path);
				int status = result.getInt(WebService.STATUS_CODE);
				handler.obtainMessage(UPLOAD_RESULT, status,
						Integer.valueOf(sysId), table).sendToTarget();
			} catch (Exception e) {
				handler.obtainMessage(UPLOAD_RESULT, WebService.NETERROE,
						Integer.valueOf(sysId), table).sendToTarget();
			}
		}

		cursor.close();
	}

	public void clickButton(View view) {
		int id = view.getId();
		if (lastPressBtnId == id)// 当前选中的id
			return;
		setButtonState(id);
		lastPressBtnId = id;
		switchSelect(lastPressBtnId);
	}

	private void switchSelect(int id) {
		switch (id) {
		case R.id.show_bp:
			updataBpDatas();
			break;
		case R.id.show_urine:
			updataUrineDatas();
			break;
		case R.id.show_bo:
			getComonDatas(Tables.TABLE_BO, Tables.BO);
			break;
		case R.id.show_temp:
			getComonDatas(Tables.TABLE_TEMP, Tables.TEMP);
			break;
		case R.id.show_glu:
			getComonDatas(Tables.TABLE_GLU, Tables.GLU);
			break;
		case R.id.show_ua:
			getComonDatas(Tables.TABLE_UA, Tables.UA);
			break;
		case R.id.show_chol:
			getComonDatas(Tables.TABLE_CHOL, Tables.CHOL);
			break;
		case R.id.show_wbc:
			getComonDatas(Tables.TABLE_WBC, Tables.WBC);
			break;
		case R.id.show_ecg:
			getComonDatas(Tables.TABLE_ECG, Tables.ECG);
			break;
		default:
			break;
		}
	}

	private static final String[] URINE_CLOUMS = { Tables.LEU, Tables.BLD,
			Tables.PH, Tables.PRO, Tables.PRO, Tables.UBG, Tables.NIT,
			Tables.SG, Tables.KET, Tables.BIL, Tables.GLU, Tables.VC };

	private void updataUrineDatas() {
		List<String[]> result = new ArrayList<String[]>();
		Cursor cursor = query(Tables.TABLE_URINE);
		while (cursor.moveToNext()) {
			String[] line = new String[5];
			line[0] = getCursorString(cursor, Tables.TIME);
			StringBuilder sb = new StringBuilder();
			int count = 0;
			for (String cloum : URINE_CLOUMS) {
				sb.append(cloum);
				sb.append(":");
				sb.append(getCursorString(cursor, cloum));
				if (++count % 5 == 0)
					sb.append("\n");
				else
					sb.append(" ");
			}
			line[1] = sb.toString();
			int status = Integer.valueOf(getCursorString(cursor,
					WebService.STATUS_CODE));
			if (status == WebService.OK)
				line[2] = UPLOAD_YES;
			else
				line[2] = UPLOAD_NO;
			line[3] = getCursorString(cursor, DataOpenHelper.SYS_ID);
			line[4] = Tables.TABLE_URINE;
			result.add(line);
		}
		cursor.close();
		updateData(result);
	}

	private void updataBpDatas() {
		List<String[]> result = new ArrayList<String[]>();
		Cursor cursor = query(Tables.TABLE_BP);
		while (cursor.moveToNext()) {
			String[] line = new String[5];
			line[0] = getCursorString(cursor, Tables.TIME);
			String sdp = getCursorString(cursor, Tables.SBP);
			String ddp = getCursorString(cursor, Tables.DBP);
			String pulse = getCursorString(cursor, Tables.PULSE);
			StringBuilder sb = new StringBuilder("收缩压：");
			sb.append(sdp);
			sb.append(" 舒张压：");
			sb.append(ddp);
			sb.append(" 脉率：");
			sb.append(pulse);
			line[1] = sb.toString();
			int status = Integer.valueOf(getCursorString(cursor,
					WebService.STATUS_CODE));
			if (status == WebService.OK)
				line[2] = UPLOAD_YES;
			else
				line[2] = UPLOAD_NO;
			line[3] = getCursorString(cursor, DataOpenHelper.SYS_ID);
			line[4] = Tables.TABLE_BP;
			result.add(line);
		}
		cursor.close();
		updateData(result);
	}

	/***
	 * 查询某个表的所有数据
	 * 
	 * @param talble
	 */
	private Cursor query(String table) {
		Cursor cursor = dbService.query(table, DataOpenHelper.SYS_ID + ">= ?",
				new String[] { "0" });
		return cursor;
	}

	private static final String UPLOAD_YES = "已上传";
	private static final String UPLOAD_NO = "未上传";

	private void getComonDatas(String table, String dataCloum) {
		List<String[]> result = new ArrayList<String[]>();
		Cursor cursor = query(table);
		while (cursor.moveToNext()) {
			String[] line = new String[5];
			line[0] = getCursorString(cursor, Tables.TIME);
			line[1] = getCursorString(cursor, dataCloum);
			if (line[1].length() > 10)
				line[1] = line[1].substring(0, 10) + "...";
			int status = Integer.valueOf(getCursorString(cursor,
					WebService.STATUS_CODE));
			if (status == WebService.OK)
				line[2] = UPLOAD_YES;
			else
				line[2] = UPLOAD_NO;
			line[3] = getCursorString(cursor, DataOpenHelper.SYS_ID);
			line[4] = table;
			result.add(line);
		}
		cursor.close();
		updateData(result);
	}

	/**
	 * 提醒更新数据
	 * 
	 * @param result
	 */
	private void updateData(List<String[]> result) {
		datas.clear();
		datas.add(title);
		datas.addAll(result);
		mAdapter.notifyDataSetChanged();
	}

	/***
	 * 封装游标的奇葩方法
	 * 
	 * @param cursor
	 * @param cloumn
	 * @return
	 */
	protected String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

	/***
	 * 设置button的选中效果
	 * 
	 * @param id
	 */
	private void setButtonState(int id) {
		for (int each : ids) {
			if (each == id)
				findViewById(each).setSelected(true);
			else
				findViewById(each).setSelected(false);
		}
	}

}
