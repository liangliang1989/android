package com.health.chronicdisease;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.younext.R;

import com.health.archive.baby.BabyHomeVistit;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.ThreeCloumAdpter;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;

public class HypertensionTureList extends Activity {
	
	private DatabaseService dbService;
	private Context context;
	private ListView listview;
	private Button addbtn;
	private List<String[]> datas;
	private ThreeCloumAdpter adapter;
	private String[] cloumns;
	
	// 更新界面标志
		private static final int LOAD_CONTENT = 0x10;
		// 确认按钮
		private static final int FRESH = 0x11;
		// 保存成功
		private static final int SAVE_OK = 0x12;
		// 保存失败
		private static final int SAVE_ERROE = 0x13;
		//public static final String HANDLER = "Handler";

		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LOAD_CONTENT:
					List<String[]> content = (List<String[]>) msg.obj;
					clearAndAddTitile();
					datas.addAll(content);
					adapter.notifyDataSetChanged();
					break;
				case FRESH:
					addContent();
					break;
				case SAVE_OK:
					// switchLock();
					break;
				case SAVE_ERROE:
					// T.showShort(getActivity(), "保存失败");
					break;
				default:
					break;
				}
			}

		};
	
	private static final String[] content_title = new String[] { "编号", "访问时间",
	"访问医生" };
	@Override
	 protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hypertensionturelist);
		context=this;
		initView();
	}
	
	private void initView() {
		dbService = new DatabaseService(context);
		addbtn=(Button)findViewById(R.id.hypertension_btn_add);
		listview = (ListView) findViewById(R.id.hypertension_listview);
		datas = getDatas();
		adapter = new ThreeCloumAdpter(context, datas);
		listview.setAdapter(adapter);
		addContent();
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String[] line = (String[]) parent.getItemAtPosition(position);
				jump2detail(line[0]);
			}
		});
		addbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump("-1");
			}
		});
	}
	
	private void clearAndAddTitile() {
		datas.clear();
		datas.add(content_title);
	}

	
	private void addContent() {
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				List<String[]> contents = getFromDb();
				handler.obtainMessage(LOAD_CONTENT, contents).sendToTarget();
			}

		}).start();
	}
	
	private List<String[]> getDatas() {
		List<String[]> datas = new ArrayList<String[]>();
		datas.add(content_title);
		return datas;
	}
	protected void jump2detail(String sysId) {
		jump(sysId);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void jump(String sysId) {
		Intent intent = new Intent(context, HypertensionTure.class);
		intent.putExtra(HypertensionTure.SYS_ID, sysId);
		// intent.putExtra(HANDLER, handler);

		startActivityForResult(intent, HypertensionTure.REQUEST_FRESH);
	}

	private List<String[]> getFromDb() {
		List<String[]> content = new ArrayList<String[]>();
		Cursor cursor = dbService.query(HypertensionTable.HYPERTENSION_TABLE, HypertensionTable.SERIAL_ID,
				Tables.getSerialId());
		L.i("getFromDb cursor.getCount()", cursor.getCount() + "");
		while (cursor.moveToNext()) {
			String[] line = new String[3];
			line[0] = getCursorString(cursor, DataOpenHelper.SYS_ID);
			line[1] = getCursorString(cursor, HypertensionTable.REPORT_DATE);
			line[2] = getCursorString(cursor, HypertensionTable.TUREDOCTOR);
			content.add(line);
		}
		cursor.close();
		return content;
	}
	
	private String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.i("onActivityResult", requestCode + "");
		switch (requestCode) {
		case HypertensionTure.REQUEST_FRESH:
			addContent();
			break;
		}
	}

}
