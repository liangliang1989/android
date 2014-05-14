package com.health.viewUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class customEditText extends EditText{
	private Context context;
	public customEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public customEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public customEditText(Context context) {
		super(context);
		this.context = context;
	}
}
