package com.health.archive;

import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.database.CoverTable;
import com.health.database.DataOpenHelper;

import cn.younext.R;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 档案封面
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-4 下午10:07:58
 */
public class ArchiveCover extends Fragment {
	private EditText name;
	private EditText address;
	private EditText hujiAddress;
	private EditText phone;
	private EditText streetName;
	private EditText countryName;
	private EditText jiandangCom;
	private EditText jiandangPerson;
	private EditText doctorName;
	private EditText date;
	
	private SQLiteDatabase db;
	private Cursor cursor;
	private boolean isExist;
	private static final String TABLE_NAME = "COVER";
	public ArchiveCover() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ArchiveMain.getInstance().setTitle("居民健康档案");
		ArchiveMain.getInstance().setButtonText("保存");
		View v = inflater.inflate(R.layout.health_archivel_cover, null);
		//变量初始化
		findView(v);
		db = new DataOpenHelper(getActivity()).getWritableDatabase();
		cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + CoverTable.USER_ID + "=?" ,
				new String[] {"123"}); //写死
		if (cursor.moveToNext()) {
			isExist = true;
			fillTable();
		}
		
		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				ContentValues values = getValues();
				if (isExist) {
					int result = db.update(TABLE_NAME, values, CoverTable.USER_ID + "=?",
							new String[] {"123"});
					if (result == -1) {
						Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_LONG).show();
						Log.i("更新信息", values.toString());
					} else 
						Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG).show();
				} else {
					int result = (int)db.insert(TABLE_NAME, null, values);
					if (result == -1) {
						Toast.makeText(getActivity(), "插入失败", Toast.LENGTH_LONG).show();
						Log.i("插入信息", values.toString());
					} else {
						Toast.makeText(getActivity(), "插入成功", Toast.LENGTH_LONG).show();
					}
				}
				
			}
		});
		
		return v;
	}
	
	private void findView(View v) {
		name = (EditText) v.findViewById(R.id.et_name);
		address = (EditText) v.findViewById(R.id.et_address);
		hujiAddress = (EditText) v.findViewById(R.id.et_huji_address);
		phone = (EditText) v.findViewById(R.id.et_phone);
		streetName = (EditText) v.findViewById(R.id.et_street_name);
		countryName = (EditText) v.findViewById(R.id.et_country_name);
		jiandangCom = (EditText) v.findViewById(R.id.et_jiandang_com);
		jiandangPerson = (EditText) v.findViewById(R.id.et_jiandang_person);
		doctorName = (EditText) v.findViewById(R.id.et_doctor);
		date = (EditText) v.findViewById(R.id.et_date);
	}
	
	private void fillTable() {
		int nameCol = cursor.getColumnIndex(CoverTable.NAME);
		int addressCol = cursor.getColumnIndex(CoverTable.CURRENT_ADDRESS);
		int hujiAddressCol = cursor.getColumnIndex(CoverTable.HUJI_ADRESS);
		int phoneCol = cursor.getColumnIndex(CoverTable.PHONE);
		int streetNameCol = cursor.getColumnIndex(CoverTable.STREET_NAME);
		int countryNameCol = cursor.getColumnIndex(CoverTable.COUNTRY_NAME);
		int jiandangComCol = cursor.getColumnIndex(CoverTable.JIANDANGCOM);
		int jiandangPersonCol = cursor.getColumnIndex(CoverTable.JIANDANGPERSON);
		int doctorNameCol = cursor.getColumnIndex(CoverTable.DOCTOR);
		int dateCol = cursor.getColumnIndex(CoverTable.DATE);
		
		name.setText(cursor.getString(nameCol));
		address.setText(cursor.getString(addressCol));
		hujiAddress.setText(cursor.getString(hujiAddressCol));
		phone.setText(cursor.getString(phoneCol));
		streetName.setText(cursor.getString(streetNameCol));
		countryName.setText(cursor.getString(countryNameCol));
		jiandangCom.setText(cursor.getString(jiandangComCol));
		jiandangPerson.setText(cursor.getString(jiandangPersonCol));
		doctorName.setText(cursor.getString(doctorNameCol));
		date.setText(cursor.getString(dateCol));
		
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(CoverTable.NAME, name.getText().toString());
		values.put(CoverTable.CURRENT_ADDRESS, address.getText().toString());
		values.put(CoverTable.HUJI_ADRESS, hujiAddress.getText().toString());
		values.put(CoverTable.PHONE, phone.getText().toString());
		values.put(CoverTable.STREET_NAME, streetName.getText().toString());
		values.put(CoverTable.COUNTRY_NAME, countryName.getText().toString());
		values.put(CoverTable.JIANDANGCOM, jiandangCom.getText().toString());
		values.put(CoverTable.JIANDANGPERSON, jiandangPerson.getText().toString());
		values.put(CoverTable.DOCTOR, doctorName.getText().toString());
		values.put(CoverTable.DATE, date.getText().toString());
		values.put(CoverTable.USER_ID, "123"); //需要更改
		return values;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", 4);
	}
	
}