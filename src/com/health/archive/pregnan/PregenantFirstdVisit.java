package com.health.archive.pregnan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import cn.younext.R;

import com.health.archive.ArchiveMain;
import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.archive.VisitBaseActivity;
import com.health.archive.vaccinate.DialogVaccEdit;
import com.health.archive.vaccinate.DialogVaccEdit.ResultTask;
import com.health.archive.vaccinate.VaccTables;
import com.health.archive.vaccinate.VaccinateAdapter;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.ListViewForScrollView;
import com.health.util.T;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;

/**
 * 孕妇产前第一次检查
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-5-12 下午5:06:40
 */
public class PregenantFirstdVisit extends Fragment {

	private ListViewForScrollView vaccListView;
	private VaccinateAdapter adapter;
	private LayoutInflater inflater;
	private List<String[]> datas;

	// 浮在整个fragment上的button，用于控制修改和保存
	private Button editHelpBtn;


	// 布局根界面，把它搞进来是为了在onCreateView中迅速返回，以免卡顿
	private RelativeLayout rootView;
	// 当前布局内容
	private View cView;

	// 控制菜单的锁
	private boolean lock = false;
	// 数据库
	private DatabaseService dbService;
	// 更新界面标志
	private static final int FRESH_UI = 0x10;
	// 确认按钮
	private static final int POSITIVE = 0x11;
	// 保存成功
	private static final int SAVE_OK = 0x12;
	// 保存失败
	private static final int SAVE_ERROE = 0x13;

	

	public PregenantFirstdVisit() {
		setRetainInstance(true);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				//initView();
				break;
			case POSITIVE:
				//saveToDb();// 保存到数据库
				T.showShort(getActivity(), "保存成功");
				break;
			case SAVE_OK:
				//switchLock();
				break;
			case SAVE_ERROE:
				T.showShort(getActivity(), "保存失败");
				break;
			default:
				break;
			}
		}

	};

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = new RelativeLayout(getActivity());
		// 让后台线程告诉界面去更新,这里立即返回一个跟view
		new BackGroundThread(new BackGroundTask() {

			@Override
			public void process() {
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.obtainMessage(FRESH_UI).sendToTarget();
			}
		}).start();
		// 标题还是先写上,不然一偏空白感觉不好
		ArchiveMain.getInstance().setTitle("预防接种卡");
		ArchiveMain.getInstance().setButtonText("修改");
		return rootView;
	}

	
	/***
	 * 往values加入id对应的EditText的string
	 * 
	 * @param values
	 * @param key
	 * @param id
	 */
	private void putValue(ContentValues values, String key, int id) {
		String text = getEtString(id);
		if (text != null)
			values.put(key, text);
	}

	/***
	 * 获取EditText的文本
	 * 
	 * @param id
	 * @return
	 */
	private String getEtString(int id) {
		return ((EditText) cView.findViewById(id)).getText().toString();
	}

}
