package cn.younext;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.health.BaseActivity;
import com.health.util.Constants;

public class MyCalendar extends BaseActivity {

	Button homeBtn;
	Button drug;

	Button xuanzhongBtn1;
	Button xuanzhongBtn2;
	Button xuanzhongBtn3;
	Button xuanzhongBtn4;
	Button xuanzhongBtn5;

	Button clockBtn1;
	Button clockBtn2;
	Button clockBtn3;
	Button clockBtn4;
	Button clockBtn5;

	EditText medical_edit1;
	EditText medical_edit2;
	EditText medical_edit3;
	EditText medical_edit4;
	EditText medical_edit5;

	TextView medical_spinnertext1;
	TextView medical_spinnertext2;
	TextView medical_spinnertext3;
	TextView medical_spinnertext4;
	TextView medical_spinnertext5;

	Spinner medical_spinner1;
	Spinner medical_spinner2;
	Spinner medical_spinner3;
	Spinner medical_spinner4;
	Spinner medical_spinner5;

	int spinnerid1;
	int spinnerid2;
	int spinnerid3;
	int spinnerid4;
	int spinnerid5;

	DatabaseHelper helper;
	SQLiteDatabase db;
	Cursor c;
	Cursor c1;

	final int intervalTime = 86400000;

	OnClickListener btnClick;

	TextView user;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_medical);


		user = (TextView) findViewById(R.id.calendar_medical_user);
		user.setText(getString(R.string.myhealth_Welcome) +BaseActivity.getUser().getCardNo());

		homeBtn = (Button) findViewById(R.id.calendar_medical_homeBtn);
		drug = (Button) findViewById(R.id.calendar_drug);

		xuanzhongBtn1 = (Button) findViewById(R.id.calendar_medical_xuanzhongBtn1);
		xuanzhongBtn2 = (Button) findViewById(R.id.calendar_medical_xuanzhongBtn2);
		xuanzhongBtn3 = (Button) findViewById(R.id.calendar_medical_xuanzhongBtn3);
		xuanzhongBtn4 = (Button) findViewById(R.id.calendar_medical_xuanzhongBtn4);
		xuanzhongBtn5 = (Button) findViewById(R.id.calendar_medical_xuanzhongBtn5);

		clockBtn1 = (Button) findViewById(R.id.calendar_clockBtn1);
		clockBtn2 = (Button) findViewById(R.id.calendar_clockBtn2);
		clockBtn3 = (Button) findViewById(R.id.calendar_clockBtn3);
		clockBtn4 = (Button) findViewById(R.id.calendar_clockBtn4);
		clockBtn5 = (Button) findViewById(R.id.calendar_clockBtn5);

		medical_edit1 = (EditText) findViewById(R.id.calendar_medical_edittext1);
		medical_edit2 = (EditText) findViewById(R.id.calendar_medical_edittext2);
		medical_edit3 = (EditText) findViewById(R.id.calendar_medical_edittext3);
		medical_edit4 = (EditText) findViewById(R.id.calendar_medical_edittext4);
		medical_edit5 = (EditText) findViewById(R.id.calendar_medical_edittext5);

		medical_spinnertext1 = (TextView) findViewById(R.id.calendar_medical_spinnertext1);
		medical_spinnertext2 = (TextView) findViewById(R.id.calendar_medical_spinnertext2);
		medical_spinnertext3 = (TextView) findViewById(R.id.calendar_medical_spinnertext3);
		medical_spinnertext4 = (TextView) findViewById(R.id.calendar_medical_spinnertext4);
		medical_spinnertext5 = (TextView) findViewById(R.id.calendar_medical_spinnertext5);

		medical_spinner1 = (Spinner) findViewById(R.id.calendar_medical_spinner1);
		medical_spinner2 = (Spinner) findViewById(R.id.calendar_medical_spinner2);
		medical_spinner3 = (Spinner) findViewById(R.id.calendar_medical_spinner3);
		medical_spinner4 = (Spinner) findViewById(R.id.calendar_medical_spinner4);
		medical_spinner5 = (Spinner) findViewById(R.id.calendar_medical_spinner5);

		helper = new DatabaseHelper(MyCalendar.this,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		db = helper.getWritableDatabase();
		c = db.query(DatabaseHelper.ALARMTABLE, null, null, null, null, null,
				DatabaseHelper.ID + " asc");

		c.moveToNext();
		initialize(this, c, xuanzhongBtn1, medical_edit1, clockBtn1,
				medical_spinner1, medical_spinnertext1, "1");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn2, medical_edit2, clockBtn2,
				medical_spinner2, medical_spinnertext2, "2");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn3, medical_edit3, clockBtn3,
				medical_spinner3, medical_spinnertext3, "3");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn4, medical_edit4, clockBtn4,
				medical_spinner4, medical_spinnertext4, "4");
		c.moveToNext();
		initialize(this, c, xuanzhongBtn5, medical_edit5, clockBtn5,
				medical_spinner5, medical_spinnertext5, "5");

		db.close();

		btnClick = new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (v == homeBtn) {
					alarmTimeSet(MyCalendar.this, "1", 1, medical_edit1);
					alarmTimeSet(MyCalendar.this, "2", 2, medical_edit2);
					alarmTimeSet(MyCalendar.this, "3", 3, medical_edit3);
					alarmTimeSet(MyCalendar.this, "4", 4, medical_edit4);
					alarmTimeSet(MyCalendar.this, "5", 5, medical_edit5);
					
					MyCalendar.this.finish();

					// /////////////////////����edittext����
				} else if (v == drug) {
					alarmTimeSet(MyCalendar.this, "1", 1, medical_edit1);
					alarmTimeSet(MyCalendar.this, "2", 2, medical_edit2);
					alarmTimeSet(MyCalendar.this, "3", 3, medical_edit3);
					alarmTimeSet(MyCalendar.this, "4", 4, medical_edit4);
					alarmTimeSet(MyCalendar.this, "5", 5, medical_edit5);

					
					Intent i = new Intent();
					i.setClass(MyCalendar.this, calendar_drug.class);
					i.putExtra("userid", BaseActivity.getGroup().getUserName());
					i.putExtra("username", BaseActivity.getGroup().getNickName());
					// /////////////////////����edittext����

					startActivityForResult(i, 1);

				} else if (v == xuanzhongBtn1) {
					xuanzhongAct(MyCalendar.this, xuanzhongBtn1, "1", 1);
					

				} else if (v == xuanzhongBtn2) {
					xuanzhongAct(MyCalendar.this, xuanzhongBtn2, "2", 2);
				} else if (v == xuanzhongBtn3) {
					xuanzhongAct(MyCalendar.this, xuanzhongBtn3, "3", 3);
				} else if (v == xuanzhongBtn4) {
					xuanzhongAct(MyCalendar.this, xuanzhongBtn4, "4", 4);
				} else if (v == xuanzhongBtn5) {
					xuanzhongAct(MyCalendar.this, xuanzhongBtn5, "5", 5);
				} else if (v == clockBtn1) {
					timeSet(MyCalendar.this, "1", clockBtn1);

					

				} else if (v == clockBtn2) {
					timeSet(MyCalendar.this, "2", clockBtn2);
				} else if (v == clockBtn3) {
					timeSet(MyCalendar.this, "3", clockBtn3);
				} else if (v == clockBtn4) {
					timeSet(MyCalendar.this, "4", clockBtn4);
				} else if (v == clockBtn5) {
					timeSet(MyCalendar.this, "5", clockBtn5);
				}

				else {
				}
			}
		};
		homeBtn.setOnClickListener(btnClick);
		drug.setOnClickListener(btnClick);
		xuanzhongBtn1.setOnClickListener(btnClick);
		xuanzhongBtn2.setOnClickListener(btnClick);
		xuanzhongBtn3.setOnClickListener(btnClick);
		xuanzhongBtn4.setOnClickListener(btnClick);
		xuanzhongBtn5.setOnClickListener(btnClick);
		clockBtn1.setOnClickListener(btnClick);
		clockBtn2.setOnClickListener(btnClick);
		clockBtn3.setOnClickListener(btnClick);
		clockBtn4.setOnClickListener(btnClick);
		clockBtn5.setOnClickListener(btnClick);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				MyCalendar.this.finish();
			}
			break;
		default:
			break;
		}
	}

	public static void initialize(Context context, Cursor c,
			Button xuanzhongBtn, EditText medical_edit, Button clockBtn,
			Spinner medical_spinner, final TextView medical_spinnertext,
			final String id) {
		if (c.getInt(1) == 0) {
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhongqianbg));
		} else {
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhonghoubg));
		}
		medical_edit.setText(c.getString(2));
		final int spinnerid = c.getInt(3);

		clockBtn.setText(format(c.getInt(4)) + ":" + format(c.getInt(5)));

		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		final SQLiteDatabase db = helper.getWritableDatabase();

		final Cursor c1 = db.query(DatabaseHelper.ALARMZHOUQITABLE, null, null,
				null, null, null, "_id asc", null);// �û����ֲ�ѯ
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
				android.R.layout.simple_spinner_item, c1,
				new String[] { DatabaseHelper.ZHOUQI },
				new int[] { R.id.text1 });
		adapter.setDropDownViewResource(R.layout.main_spinner_dropdown);

		medical_spinner.setAdapter(adapter);
		medical_spinner.setSelection(spinnerid - 1);

		medical_spinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						arg0.setVisibility(View.VISIBLE);
						// spinnerid = arg2+1;
						// Log.v("userid",
						// String.valueOf(userid));
						c1.moveToPosition(arg2);
						Log.v("arg2", String.valueOf(arg2));

						medical_spinnertext.setText(c1.getString(1));
						Cursor cursor = db.query(DatabaseHelper.ALARMTABLE,
								null, DatabaseHelper.ID + "=" + id, null, null,
								null, DatabaseHelper.ID + " asc");
						cursor.moveToNext();
						ContentValues values = new ContentValues();
						values.put(DatabaseHelper.SPINNER, arg2 + 1);// keyΪ�ֶ�����valueΪֵ
						db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
								new String[] { id });
						Log.v("shuju", "shuju");

						// /////////////////////��������

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method
						// stub

					}

				}

				);

	}

	public static void xuanzhongAct(Context context, Button xuanzhongBtn,
			String id, int alarmid) {
		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID
				+ "=" + id, null, null, null, DatabaseHelper.ID + " asc");
		c.moveToNext();
		if (c.getInt(1) == 0) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.XUANZHONG, 1);// keyΪ�ֶ�����valueΪֵ
			db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
					new String[] { id });
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhonghoubg));
			// ///////////////��������
		} else {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.XUANZHONG, 0);// keyΪ�ֶ�����valueΪֵ
			db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
					new String[] { id });
			xuanzhongBtn.setBackgroundDrawable(context.getResources()
					.getDrawable(R.drawable.calendar_xuanzhongqianbg));
			// //////////////////ȡ������
			Intent intent = new Intent(context, CallAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, alarmid,
					intent, 0);
			AlarmManager am;
			am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
			am.cancel(sender);
		}
		db.close();

	}

	public static void timeSet(final Context context, final String id,
			final Button clockBtn) {
		// ��ȡʱ����������
		final Calendar alarm = Calendar.getInstance();
		alarm.setTimeInMillis(System.currentTimeMillis());
		int mHour = alarm.get(Calendar.HOUR_OF_DAY);
		int mMinute = alarm.get(Calendar.MINUTE);
		new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub

				DatabaseHelper helper = new DatabaseHelper(context,
						DatabaseHelper.DATABASE_NAME, null,
						DatabaseHelper.Version);
				SQLiteDatabase db = helper.getWritableDatabase();

				Cursor c = db.query(DatabaseHelper.ALARMTABLE, null,
						DatabaseHelper.ID + "=" + id, null, null, null,
						DatabaseHelper.ID + " asc");
				c.moveToNext();
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.HOUR, hourOfDay);// keyΪ�ֶ�����valueΪֵ
				values.put(DatabaseHelper.MINUTE, minute);
				db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
						new String[] { id });
				clockBtn.setText(format(hourOfDay) + ":" + format(minute));
				db.close();
			}
		}, mHour, mMinute, true).show();
	}

	public static void alarmTimeSet(final Context context, String id,
			int alarmid, EditText medical_edit) {
		// TODO Auto-generated method stub

		DatabaseHelper helper = new DatabaseHelper(context,
				DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.Version);
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID
				+ "=" + id, null, null, null, DatabaseHelper.ID + " asc");
		c.moveToNext();
		if (c.getInt(1) == 0) {

		} else {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.TEXT, medical_edit.getText().toString());// keyΪ�ֶ�����valueΪֵ
			db.update(DatabaseHelper.ALARMTABLE, values, "_id=?",
					new String[] { id });
			db.close();
			db = helper.getWritableDatabase();
			c = db.query(DatabaseHelper.ALARMTABLE, null, DatabaseHelper.ID
					+ "=" + id, null, null, null, DatabaseHelper.ID + " asc");
			c.moveToNext();

			int intervalTime = 86400000;
			int intervalweek = 86400000 * 7;
			Calendar alarm = Calendar.getInstance();
			Calendar currentTime = Calendar.getInstance();
			currentTime.setTimeInMillis(System.currentTimeMillis());

			// alarm.setTimeInMillis(System.currentTimeMillis());
			Log.v("week", String.valueOf(alarm.get(Calendar.DAY_OF_WEEK)));

			alarm.set(Calendar.HOUR_OF_DAY, c.getInt(4));
			alarm.set(Calendar.MINUTE, c.getInt(5));
			alarm.set(Calendar.SECOND, 0);
			alarm.set(Calendar.MILLISECOND, 0);
			Intent intent = new Intent(context, CallAlarm.class);
			intent.putExtra("text", c.getString(2));
			intent.putExtra("tishi",
					context.getString(R.string.calendar_medical_tishi));
			PendingIntent sender = PendingIntent.getBroadcast(context, alarmid,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am;
			am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
			if (c.getInt(3) != 1 && c.getInt(3) != 8) {
				alarm.set(Calendar.DAY_OF_WEEK, c.getInt(3));
				if (currentTime.get(Calendar.DAY_OF_WEEK) > c.getInt(3)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == c.getInt(3)
						&& currentTime.get(Calendar.HOUR_OF_DAY) > c.getInt(4)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == c.getInt(3)
						&& currentTime.get(Calendar.HOUR_OF_DAY) == c.getInt(4)
						&& currentTime.get(Calendar.MINUTE) > c.getInt(5)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis(), intervalweek, sender);
				}

			} else if (c.getInt(3) == 1) {
				if (currentTime.get(Calendar.HOUR_OF_DAY) > c.getInt(4)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalTime,
							intervalTime, sender);
				} else if (currentTime.get(Calendar.HOUR_OF_DAY) == c.getInt(4)
						&& currentTime.get(Calendar.MINUTE) > c.getInt(5)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalTime,
							intervalTime, sender);
				} else {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis(), intervalTime, sender);
				}

			} else {
				alarm.set(Calendar.DAY_OF_WEEK, 1);
				if (currentTime.get(Calendar.DAY_OF_WEEK) > 1) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == 1
						&& currentTime.get(Calendar.HOUR_OF_DAY) > c.getInt(4)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else if (currentTime.get(Calendar.DAY_OF_WEEK) == 1
						&& currentTime.get(Calendar.HOUR_OF_DAY) == c.getInt(4)
						&& currentTime.get(Calendar.MINUTE) > c.getInt(5)) {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis() + intervalweek,
							intervalweek, sender);
				} else {
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							alarm.getTimeInMillis(), intervalweek, sender);
				}

			}

		}
		db.close();

	}

	private static String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			alarmTimeSet(MyCalendar.this, "1", 1, medical_edit1);
			alarmTimeSet(MyCalendar.this, "2", 2, medical_edit2);
			alarmTimeSet(MyCalendar.this, "3", 3, medical_edit3);
			alarmTimeSet(MyCalendar.this, "4", 4, medical_edit4);
			alarmTimeSet(MyCalendar.this, "5", 5, medical_edit5);

			MyCalendar.this.finish();

			// /////////////////////����edittext����

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
