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
 * �и���ǰ��һ�μ��
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-5-12 ����5:06:40
 */
public class PregenantFirstdVisit extends Fragment {

	private ListViewForScrollView vaccListView;
	private VaccinateAdapter adapter;
	private LayoutInflater inflater;
	private List<String[]> datas;

	// ��������fragment�ϵ�button�����ڿ����޸ĺͱ���
	private Button editHelpBtn;


	// ���ָ����棬�����������Ϊ����onCreateView��Ѹ�ٷ��أ����⿨��
	private RelativeLayout rootView;
	// ��ǰ��������
	private View cView;

	// ���Ʋ˵�����
	private boolean lock = false;
	// ���ݿ�
	private DatabaseService dbService;
	// ���½����־
	private static final int FRESH_UI = 0x10;
	// ȷ�ϰ�ť
	private static final int POSITIVE = 0x11;
	// ����ɹ�
	private static final int SAVE_OK = 0x12;
	// ����ʧ��
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
				//saveToDb();// ���浽���ݿ�
				T.showShort(getActivity(), "����ɹ�");
				break;
			case SAVE_OK:
				//switchLock();
				break;
			case SAVE_ERROE:
				T.showShort(getActivity(), "����ʧ��");
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
		// �ú�̨�̸߳��߽���ȥ����,������������һ����view
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
		// ���⻹����д��,��Ȼһƫ�հ׸о�����
		ArchiveMain.getInstance().setTitle("Ԥ�����ֿ�");
		ArchiveMain.getInstance().setButtonText("�޸�");
		return rootView;
	}

	
	/***
	 * ��values����id��Ӧ��EditText��string
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
	 * ��ȡEditText���ı�
	 * 
	 * @param id
	 * @return
	 */
	private String getEtString(int id) {
		return ((EditText) cView.findViewById(id)).getText().toString();
	}

}
