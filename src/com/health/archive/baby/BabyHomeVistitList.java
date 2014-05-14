package com.health.archive.baby;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.younext.R;

import com.health.archive.ArchiveMain;
import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.database.DataOpenHelper;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.ThreeCloumAdpter;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;

@SuppressLint("NewApi")

public class BabyHomeVistitList extends Fragment {

	private View cView;
	private List<String[]> datas;
	private static final String[] title = new String[] { "���", "����ʱ��", "����ҽ��" };
	// ���½����־
	private static final int LOAD_CONTENT = 0x10;
	// ȷ�ϰ�ť
	private static final int FRESH = 0x11;
	// ����ɹ�
	private static final int SAVE_OK = 0x12;
	// ����ʧ��
	private static final int SAVE_ERROE = 0x13;
	public static final String HANDLER = "Handler";

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
				// T.showShort(getActivity(), "����ʧ��");
				break;
			default:
				break;
			}
		}

	};
	private DatabaseService dbService;
	private ThreeCloumAdpter adapter;
	private ListView listview;

	
	@SuppressLint("NewApi")
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
				String[] line = (String[]) parent.getItemAtPosition(position);
				jump2detail(line[0]);
			}
		});
	}

	/**
	 * ��ת�Ĳ鿴����
	 * 
	 * @param string
	 */
	protected void jump2detail(String sysId) {
		jump(sysId);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void jump(String sysId) {
		Intent intent = new Intent(getActivity(), BabyHomeVistit.class);
		intent.putExtra(BabyHomeVistit.SYS_ID, sysId);
		// intent.putExtra(HANDLER, handler);		
		startActivityForResult(intent, BabyHomeVistit.REQUEST_FRESH);
	}

	protected void addNewVisteTable() {
		jump("-1");
	}

	private List<String[]> getDatas() {
		List<String[]> datas = new ArrayList<String[]>();
		datas.add(title);
		return datas;
	}

	private void clearAndAddTitile() {
		datas.clear();
		datas.add(title);
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
	 * �����ݿ��л�ȡ��¼
	 * 
	 * @return
	 */
	private List<String[]> getFromDb() {
		List<String[]> content = new ArrayList<String[]>();
		Cursor cursor = dbService.query(BabyTable.baby_table,
				BabyTable.serial_id, Tables.getSerialId());
		L.i("getFromDb cursor.getCount()", cursor.getCount() + "");
		while (cursor.moveToNext()) {
			String[] line = new String[3];
			line[0] = getCursorString(cursor, DataOpenHelper.SYS_ID);
			line[1] = getCursorString(cursor, BabyTable.visit_date);
			line[2] = getCursorString(cursor, BabyTable.visit_doctor);
			content.add(line);
		}
		cursor.close();
		return content;
	}

	/***
	 * ��װ�α�����ⷽ��
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
		ArchiveMain.getInstance().setTitle("��������ͥ���Ӽ�¼��");
		ArchiveMain.getInstance().setButtonText("���");
		return cView;
	}

	/**
	 * ���շ���ʱ������Ա�����б�
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.i("onActivityResult", requestCode + "");
		switch (requestCode) {
		case BabyHomeVistit.REQUEST_FRESH:
			addContent();
			break;
		}
	}
}
