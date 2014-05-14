package com.health.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 
 * ����scrollview��ʹ�õ�listview
 *  copy from
 * http://www.apkbus.com/android-161576-1-1.html
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-8 ����7:46:08
 */
public class ListViewForScrollView extends ListView {
	public ListViewForScrollView(Context context) {
		super(context);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	/**
	 * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
