package com.health.archive.vaccinate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import cn.younext.R;

import com.health.archive.ArchiveMain;
import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.archive.vaccinate.DialogVaccEdit.ResultTask;
import com.health.database.DatabaseService;
import com.health.database.Tables;
import com.health.util.L;
import com.health.util.ListViewForScrollView;
import com.health.util.T;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;

/***
 * ������ֿ�
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-8 ����10:45:56
 */
public class Vaccinate extends Fragment {
	private ListViewForScrollView vaccListView;
	private VaccinateAdapter adapter;
	private LayoutInflater inflater;
	private List<String[]> datas;

	// ��������fragment�ϵ�button�����ڿ����޸ĺͱ���
	private Button editHelpBtn;

	// ������ʾ���棬�����ᵽ���������Ҫʱ���������·�����ɫ��
	private ScrollView bodySv;

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

	public static final String OTHER = "��������";
	private static final String[] VACCS = new String[] { "�Ҹ�����", "������", "��������",
			"�ٰ�������", "��������", "�������", "����������", "��������", "��������", "AȺ��������",
			"A+CȺ��������", "���ԣ�������������", "�����������", "�׸μ���������", "�׸��������" };
	// ���ִ���
	private static final Integer[] TIMES = { 3, 1, 4, 4, 1, 1, 2, 1, 2, 2, 2,
			2, 4, 1, 2, 1 };

	public Vaccinate() {
		setRetainInstance(true);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case POSITIVE:
				saveToDb();// ���浽���ݿ�
				T.showShort(getActivity(), "����ɹ�");
				break;
			case SAVE_OK:
				switchLock();
				break;
			case SAVE_ERROE:
				T.showShort(getActivity(), "����ʧ��");
				break;
			default:
				break;
			}
		}

	};

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("ȷ�ϱ����޸���");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.obtainMessage(POSITIVE).sendToTarget();
			}
		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initView() {
		dbService = new DatabaseService(getActivity());
		cView = inflater.inflate(R.layout.health_archivel_accinate, null);
		rootView.addView(cView);
		findViewById(cView);
		// initDate()������ʵ���˴��£�����֮�Ǹ���������ݣ����ص�ʱ�����¼�������
		datas = initDatas();
		adapter = new VaccinateAdapter(getActivity(), datas);
		vaccListView.setAdapter(adapter);
		setListener(vaccListView);
		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				if (lock)// �����޸�״̬ʱ,�����Ի����ȷ�ϲű����޸�,������״̬
					dialog();
				else
					switchLock();

			}
		});

	}

	private void switchLock() {
		lock = !lock;
		ArchiveMain.getInstance().setLock(lock);
		setButtonText();
	}

	/***
	 * ����id����һ�ѵĿؼ���ʼ��
	 * 
	 * @param view
	 */
	private void findViewById(View view) {
		vaccListView = (ListViewForScrollView) view
				.findViewById(R.id.vaccinate_list_view);
		bodySv = (ScrollView) view.findViewById(R.id.table_body_sv);
		editHelpBtn = (Button) view.findViewById(R.id.edit_help_button);
	}

	private void setButtonText() {
		if (lock) {
			ArchiveMain.getInstance().setButtonText("����");
			editHelpBtn.setVisibility(View.GONE);
			bodySv.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		} else {
			ArchiveMain.getInstance().setButtonText("�޸�");
			editHelpBtn.setVisibility(View.VISIBLE);
			bodySv.setBackgroundColor(getResources().getColor(
					R.color.shallow_blue));
		}
	}

	private void setListener(ListView listView) {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String[] item = (String[]) parent.getItemAtPosition(position);
				startEditVacc(item);
				return true;
			}
		});

	}

	final ResultTask resultTask = new ResultTask() {

		@Override
		public void process(String[] item) {
			if (item[0].equals(OTHER)) {
				String[] other = new String[item.length];
				other[0] = OTHER;
				other[1] = "1";
				datas.add(other);
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void cancel() {
		}

	};

	private void startEditVacc(String[] item) {
		Intent serverIntent = new Intent(getActivity(), DialogVaccEdit.class);
		DialogVaccEdit.setResultTask(resultTask);
		DialogVaccEdit.setEditItem(item);
		getActivity().startActivity(serverIntent);
	}

	/***
	 * �����ݿ��ж�ȡ��ʼ�����棬������ݿ����޼�¼���ʼ��һЩ�����������
	 * 
	 * @return
	 */
	private List<String[]> initDatas() {

		List<String[]> datas = new ArrayList<String[]>();

		String[] title = { "����", "����", "��������", "���ֲ�λ", "��������", "����ҽ��", "��ע" };
		datas.add(title);

		if (!initHeadFromDb()) {// �����ݿ��м��ر�ͷʧ��,Ҳ˵�����ݿ���û�������¼
			initToDB();
			for (int i = 0; i < VACCS.length; i++) {
				for (int j = 0; j < TIMES[i]; j++) {
					String[] content = new String[title.length];
					content[0] = VACCS[i];
					content[1] = String.valueOf(j + 1);
					datas.add(content);
				}
			}

		} else {// ���ر�ͷ�ɹ�,˵�����ݿ���ҲӦ���������¼
			datas.addAll(getRecordFromDb());// ���������¼
		}
		return datas;

	}

	/***
	 * �����ݿ��ж�ȡ��ͷ��Ϣ,���½���
	 * 
	 * @return �����½���ɹ�����true�������ݿ����޼�¼ʱ����false��
	 */
	private boolean initHeadFromDb() {
		Cursor cursor = dbService.query(VaccTables.vacc_head_table,
				VaccTables.serial_id, Tables.getSerialId());
		if (cursor.getCount() == 0) {// ���ݿ����޼�¼
			L.i("initHeadFromDb ", "���ݿ����޼�¼");
			return false;
		}
		cursor.moveToNext();
		setTextFromCursor(cursor, VaccTables.serial_id, R.id.serial_id);
		setTextFromCursor(cursor, VaccTables.guardian, R.id.guardian_et);
		setTextFromCursor(cursor, VaccTables.relation, R.id.relation_et);
		setTextFromCursor(cursor, VaccTables.phone, R.id.guardian_phone_et);
		setTextFromCursor(cursor, VaccTables.addr, R.id.addr_et);
		setTextFromCursor(cursor, VaccTables.register_addr, R.id.r_addr_et);
		setTextFromCursor(cursor, VaccTables.in_time, R.id.intime_det);
		setTextFromCursor(cursor, VaccTables.out_time, R.id.outtime_det);
		setTextFromCursor(cursor, VaccTables.out_reason, R.id.out_reason_det);
		setTextFromCursor(cursor, VaccTables.abnormal_history, R.id.abn_his_et);
		setTextFromCursor(cursor, VaccTables.vacc_taboo, R.id.vacc_taboo_det);
		setTextFromCursor(cursor, VaccTables.infection_history,
				R.id.infection_det);
		setTextFromCursor(cursor, VaccTables.add_date, R.id.add_date_det);
		setTextFromCursor(cursor, VaccTables.add_person, R.id.add_person_et);
		cursor.close();
		return true;
	}

	/***
	 * ��sql�α������ı�����
	 * 
	 * @param cursor
	 * @param cloumn
	 * @param editTextId
	 */
	private void setTextFromCursor(Cursor cursor, String cloumn, int editTextId) {
		String text = getCursorString(cursor, cloumn);
		setText(editTextId, text);
	}

	/**
	 * ����id��Ӧ��EditText��stringΪtext
	 * 
	 * @param id
	 * @param text
	 */
	private void setText(int id, String text) {
		if (text != null)
			((EditText) cView.findViewById(id)).setText(text);
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

	/***
	 * �����ݿ��л�ȡ�����¼
	 * 
	 * @return
	 */
	private List<String[]> getRecordFromDb() {
		List<String[]> result = new ArrayList<String[]>();
		Cursor cursor = dbService.query(VaccTables.vacc_record_table,
				VaccTables.serial_id, Tables.getSerialId());
		if (cursor.getCount() == 0) {// ���ݿ����޼�¼
			return result;
		}
		while (cursor.moveToNext()) {
			String[] line = new String[7];
			line[0] = getCursorString(cursor, VaccTables.vacc_kind);
			line[1] = getCursorString(cursor, VaccTables.vacc_time);
			line[2] = getCursorString(cursor, VaccTables.vacc_date);
			line[3] = getCursorString(cursor, VaccTables.vacc_part);
			line[4] = getCursorString(cursor, VaccTables.vacc_serial);
			line[5] = getCursorString(cursor, VaccTables.vacc_doctor);
			line[6] = getCursorString(cursor, VaccTables.vacc_note);
			result.add(line);
		}
		cursor.close();
		return result;
	}

	/***
	 * ����ʼ�����ݲ������ݿ�
	 */
	private void initToDB() {
		// �����ͷ������Ϣ
		ContentValues headContent = new ContentValues();
		headContent.put(VaccTables.serial_id, Tables.getSerialId());
		dbService.insert(VaccTables.vacc_head_table, headContent);

		// ����ǰ��ɾ��,�����ظ�
		dbService.delete(VaccTables.vacc_record_table, VaccTables.serial_id,
				Tables.getSerialId());
		// ���������¼
		ContentValues recordContent = new ContentValues();
		recordContent.put(VaccTables.serial_id, Tables.getSerialId());
		int rowNum = 0;
		for (int i = 0; i < VACCS.length; i++) {
			for (int j = 0; j < TIMES[i]; j++) {
				recordContent.put(VaccTables.row_num, rowNum);
				recordContent.put(VaccTables.vacc_kind, VACCS[i]);
				recordContent.put(VaccTables.vacc_time, j + 1);
				dbService.insert(VaccTables.vacc_record_table, recordContent);
				rowNum++;
			}
		}
	}

	/**
	 * �������ݵ����ݿ�
	 */
	private void saveToDb() {
		L.i("saveToDb:", "saveToDb");
		// ���±�ͷ������Ϣ
		ContentValues headContent = getHeadContent();
		dbService.update(VaccTables.vacc_head_table, VaccTables.serial_id,
				Tables.getSerialId(), headContent);

		for (int i = 1; i < datas.size(); i++) {
			ContentValues recoredContent = getRecordContent(datas.get(i));
			dbService.update(VaccTables.vacc_record_table, VaccTables.serial_id
					+ "=? and " + VaccTables.row_num + "=?", new String[] {
					Tables.getSerialId(), String.valueOf(i - 1) },
					recoredContent);
		}
		handler.obtainMessage(SAVE_OK).sendToTarget();
	}

	/***
	 * ������е����ݴ����k-v��ContentValues��
	 * 
	 * @param line
	 * @param rowNum
	 * @return
	 */
	private ContentValues getRecordContent(String[] line) {
		ContentValues content = new ContentValues();
		// content.put(VaccTables.row_num, rowNum);
		content.put(VaccTables.vacc_kind, line[0]);
		content.put(VaccTables.vacc_time, line[1]);
		content.put(VaccTables.vacc_date, line[2]);
		content.put(VaccTables.vacc_part, line[3]);
		content.put(VaccTables.vacc_serial, line[4]);
		content.put(VaccTables.vacc_doctor, line[5]);
		content.put(VaccTables.vacc_note, line[6]);
		return content;
	}

	/***
	 * ��ȥ��ͷ����
	 * 
	 * @return
	 */
	private ContentValues getHeadContent() {
		ContentValues content = new ContentValues();
		putValue(content, VaccTables.serial_id, R.id.serial_id);
		putValue(content, VaccTables.guardian, R.id.guardian_et);
		putValue(content, VaccTables.relation, R.id.relation_et);
		putValue(content, VaccTables.phone, R.id.guardian_phone_et);
		putValue(content, VaccTables.addr, R.id.addr_et);
		putValue(content, VaccTables.register_addr, R.id.r_addr_et);
		putValue(content, VaccTables.in_time, R.id.intime_det);
		putValue(content, VaccTables.out_time, R.id.outtime_det);
		putValue(content, VaccTables.out_reason, R.id.out_reason_det);
		putValue(content, VaccTables.abnormal_history, R.id.abn_his_et);
		putValue(content, VaccTables.vacc_taboo, R.id.vacc_taboo_det);
		putValue(content, VaccTables.infection_history, R.id.infection_det);
		putValue(content, VaccTables.add_date, R.id.add_date_det);
		putValue(content, VaccTables.add_person, R.id.add_person_et);
		return content;
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
