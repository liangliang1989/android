package com.health.alarm;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import cn.younext.R;

public class AlarmActivity extends ListActivity implements
		android.view.View.OnClickListener {

	ImageButton newButton;
	ListView mathAlarmListView;
	AlarmListAdapter alarmListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_activity);

		newButton = (ImageButton) findViewById(R.id.button_new);
		newButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					newButton.setBackgroundColor(getResources().getColor(
							R.color.blue));
					break;
				case MotionEvent.ACTION_UP:
					v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					Intent newAlarmIntent = new Intent(AlarmActivity.this,
							AlarmPreferencesActivity.class);
					startActivity(newAlarmIntent);
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_CANCEL:
					newButton.setBackgroundColor(getResources().getColor(
							android.R.color.transparent));
					break;
				}
				return true;
			}
		});

		mathAlarmListView = (ListView) findViewById(android.R.id.list);

		mathAlarmListView.setLongClickable(true);
		mathAlarmListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
						final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
						Builder dialog = new AlertDialog.Builder(AlarmActivity.this);						
						dialog.setTitle("ɾ������");
						dialog.setMessage("ȷ��ɾ����������?");
						dialog.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								alarmListAdapter.getAlarms().remove(alarm);
								alarmListAdapter.notifyDataSetChanged();

								Database.init(AlarmActivity.this);
								Database.deleteEntry(alarm);

								AlarmActivity.this
										.callMathAlarmScheduleService();
							}
						});
						dialog.setNegativeButton("ȡ��", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						
						dialog.show();
						
						return true;
					}
				});

		callMathAlarmScheduleService();
	}

	private void callMathAlarmScheduleService() {
		Intent mathAlarmServiceIntent = new Intent(AlarmActivity.this,
				AlarmServiceBroadcastReciever.class);
		sendBroadcast(mathAlarmServiceIntent, null);
	}

	@Override
	protected void onPause() {
		// setListAdapter(null);
		Database.deactivate();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			alarmListAdapter = new AlarmListAdapter(this);
		} else {
			alarmListAdapter = (AlarmListAdapter) data;
		}

		this.setListAdapter(alarmListAdapter);

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return alarmListAdapter;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
		Intent intent = new Intent(AlarmActivity.this,
				AlarmPreferencesActivity.class);
		intent.putExtra("alarm", alarm);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkBox_alarm_active) {
			CheckBox checkBox = (CheckBox) v;
			Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox
					.getTag());
			alarm.setAlarmActive(checkBox.isChecked());
			Database.update(alarm);
			AlarmActivity.this.callMathAlarmScheduleService();
			if (checkBox.isChecked()) {
				Toast.makeText(AlarmActivity.this,
						alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG)
						.show();
			}
		}

	}
}
