package com.health.viewUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class ChoiceEditText extends EditText {
	protected String[] items = null;
	protected Context context;
	protected String other;

	public ChoiceEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public ChoiceEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ChoiceEditText(Context context) {
		super(context);
		this.context = context;
	}

	/***
	 * ���ù̶���ѡ����
	 * 
	 * @param items
	 */
	public void setFixItems(String[] items) {
		this.items = items;
	}

	/***
	 * ���ÿ�����ӱ�ע����,��"����"
	 * 
	 * @param item
	 */
	public void setEditableItem(String item) {
		this.other = item;
	}
}
