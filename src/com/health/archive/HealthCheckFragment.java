package com.health.archive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.database.CoverTable;
import com.health.database.DataOpenHelper;
import com.health.database.HCVaccTable;
import com.health.database.HealthCheckTable;
import com.health.database.HospHisTable;
import com.health.database.MedicationsTable;
import com.health.viewUtil.ChoiceEditText;

import cn.younext.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

public class HealthCheckFragment extends Fragment {
	private final String HEALTH_CHECK_TABLE = "HEALTHCHECK";
	private DataOpenHelper dbHelper;
	private SQLiteDatabase db;
	private ListView lvCheckTableList;

	public HealthCheckFragment() {
		setRetainInstance(true);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ArchiveMain.getInstance().setTitle("健康体检表");
		ArchiveMain.getInstance().setButtonText("添加");
		View v = inflater.inflate(R.layout.health_check_table_list, null);
		lvCheckTableList = (ListView) v.findViewById(R.id.lv_check_table_list);
		dbHelper = new DataOpenHelper(getActivity());
		db = dbHelper.getWritableDatabase();
		loadData();

		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				addNewCheckTable();
			}
		});
		
		lvCheckTableList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, String> m = (Map) parent
						.getItemAtPosition(position);
				String checkDate = m.get("checkDate");
				String serialId = m.get("serialId");
				Intent intent = new Intent();
				intent.putExtra("action", "detail");
				intent.putExtra("checkDate", checkDate);
				intent.putExtra("serialId", serialId);
				intent.setClass(getActivity(), HealthCheckActivity.class);
				startActivity(intent);
				
			}
		});
		

		lvCheckTableList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent,
					View view, final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("您确定要删除该体检表？");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Map<String, String> m = (Map) parent
								.getItemAtPosition(position);
						String checkDate = m.get("checkDate");
						db.delete(HEALTH_CHECK_TABLE,
								HealthCheckTable.CHECK_DATE + "=?",
								new String[] { checkDate });
						loadData();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return true;
			}

		});

		return v;
	}

	private void loadData() {
		Cursor cursor = db.rawQuery("select * from " + HEALTH_CHECK_TABLE
				+ " where " + HealthCheckTable.USER_ID + "=?",
				new String[] { "123" });// 暂时写死
		int i = 0;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int serialIdCol = cursor.getColumnIndex(HealthCheckTable.SERIAL_ID);
		int docNameCol = cursor.getColumnIndex(HealthCheckTable.DOCTOR_NAME);
		int checkDateCol = cursor.getColumnIndex(HealthCheckTable.CHECK_DATE);

		while (cursor.moveToNext()) {
			i++;
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderId", i + "");
			map.put("serialId", cursor.getString(serialIdCol));
			map.put("doctorName", cursor.getString(docNameCol));
			map.put("checkDate", cursor.getString(checkDateCol));

			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
				R.layout.check_table_list_item, new String[] { "orderId",
						"serialId", "doctorName", "checkDate" }, new int[] {
						R.id.tv_orderId, R.id.tv_serialId, R.id.tv_doctor_name,
						R.id.tv_check_date });
		lvCheckTableList.setAdapter(adapter);


	}

	protected void addNewCheckTable() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), HealthCheckActivity.class);
		intent.putExtra("action", "add");
		startActivityForResult(intent, 100);

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		long result = data.getLongExtra("result", -1);
		
		if (result < 0) {
			Toast.makeText(getActivity(), "添加失败！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_SHORT).show();
			loadData();
		}
	}

}
