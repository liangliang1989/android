package com.health.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
/***
 * ѡ�����ڵ�textview
 * @author jiqunpeng
 *
 * ����ʱ�䣺2014-4-9 ����2:04:36
 */
public class DateEditText extends EditText {
	private Context context;

	public DateEditText(Context context) {
		super(context);
		this.context = context;
	}

	public DateEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public DateEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	private void showDateDailog() {
		new DateSelectDialog(context, new DateSelectDialog.DialogTask() {

			@Override
			public void process(String date) {				
				setText(date);
			}

		}).show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		showDateDailog();
		return false;

	}
}
