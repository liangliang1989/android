package com.health.archive.baby;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.younext.R;

import com.health.archive.ArchiveMain;
import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.archive.baby.oneold.OneOldChildTable;
import com.health.archive.VisitBaseActivity;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.ThreeCloumAdpter;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;

public class VistitList extends Fragment {

	private View cView;
	private List<String[]> datas;
	private static final String[] content_title = new String[] { "编号", "访问时间",
			"访问医生" };
	// 更新界面标志
	private static final int LOAD_CONTENT = 0x10;
	// 确认按钮
	private static final int FRESH = 0x11;
	// 保存成功
	private static final int SAVE_OK = 0x12;
	// 保存失败
	private static final int SAVE_ERROE = 0x13;
	public static final String HANDLER = "Handler";

	private Class<?> toActivity;
	private String title;
	private String[] cloumns;
	private String table;
	public final static String TITLE = "title";
	public final static String TO_CTIVITY = "toActivity";
	public final static String CLOUMNS = "cloumns";
	public final static String TABLES = "tables";

	@Override
	public void setArguments(Bundle bundle) {
		String className = (String) bundle.get(TO_CTIVITY);
		try {
			this.toActivity = (Class<?>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			L.e("VistitList", e.toString());
		}
		this.title = bundle.getString(TITLE);
		this.cloumns = bundle.getStringArray(CLOUMNS);
		this.table = bundle.getString(TABLES); 
	}

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
	private DatabaseService dbService;
	private ThreeCloumAdpter adapter;
	private ListView listview;

	private void initView() {
		dbService = new DatabaseService(getActivity());
		listview = (ListView) cView.findViewById(R.id.list_view);
		datas = getDatas();
		adapter = new ThreeCloumAdpter(getActivity(), datas);
		listview.setAdapter(adapter);
		addContent();
		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				addNewVisteTable();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					String[] line = (String[]) parent
							.getItemAtPosition(position);
					jump2detail(line[0]);
				}
			}
		});
	}

	/**
	 * 跳转的查看详情
	 * 
	 * @param string
	 */
	protected void jump2detail(String sysId) {
		jump(sysId);
	}

	private void jump(String sysId) {
		Intent intent = new Intent(getActivity(), toActivity);
		intent.putExtra(VisitBaseActivity.SYS_ID, sysId);
		startActivityForResult(intent, VisitBaseActivity.REQUEST_FRESH);
	}

	protected void addNewVisteTable() {
		jump("-1");
	}

	private List<String[]> getDatas() {
		List<String[]> datas = new ArrayList<String[]>();
		datas.add(content_title);
		return datas;
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

	/***
	 * 从数据库中获取记录
	 * 
	 * @return
	 */
	private List<String[]> getFromDb() {
		List<String[]> content = new ArrayList<String[]>();
		Cursor cursor = dbService.query(table, OneOldChildTable.serial_id,
				Tables.getSerialId());
		L.i("getFromDb cursor.getCount()", cursor.getCount() + "");
		while (cursor.moveToNext()) {
			String[] line = new String[3];
			line[0] = getCursorString(cursor, cloumns[0]);
			line[1] = getCursorString(cursor, cloumns[1]); 
			line[2] = getCursorString(cursor, cloumns[2]);
			content.add(line);
		}
		cursor.close();
		return content;
	}

	/***
	 * 封装游标的奇葩方法
	 * 
	 * @param cursor
	 * @param cloumn
	 * @return
	 */
	private String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cView = inflater.inflate(R.layout.baby_home_v_list, null);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				initView();
			}
		}, 500);
		ArchiveMain.getInstance().setTitle(title);
		ArchiveMain.getInstance().setButtonText("添加");
		return cView;
	}

	/**
	 * 接收返回时的命令，以便更新列表
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.i("onActivityResult", requestCode + "");
		switch (requestCode) {
		case VisitBaseActivity.REQUEST_FRESH:
			addContent();
			break;
		}
	}
}
