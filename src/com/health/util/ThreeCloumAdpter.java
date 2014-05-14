package com.health.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.younext.R;

/**
 * ��ʾ�����ı��������������õ�һ��Ϊ�����У������������в�ͬ
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-12-20 ����3:10:17
 */
public class ThreeCloumAdpter extends BaseAdapter {
	private static final int LENGHT = 3;
	private List<String[]> datas;
	private Context context;

	public ThreeCloumAdpter(Context context, List<String[]> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		String[] line = datas.get(position);
		// ֻ��Ϊ��ʱ�Ź���
		if (convertView == null) {
			holder = new Holder();
			holder.dataET = new TextView[LENGHT];
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_item_all_record, null);
			holder.dataET[0] = (TextView) convertView
					.findViewById(R.id.list_data_1);
			holder.dataET[1] = (TextView) convertView
					.findViewById(R.id.list_data_2);
			holder.dataET[2] = (TextView) convertView
					.findViewById(R.id.list_data_3);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.record_layout);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		for (int i = 0; i < LENGHT; i++) {
			holder.dataET[i].setText(line[i]);
		}
		if (position == 0) {
			holder.layout.setBackgroundResource(R.color.record_title);
		} else {
			// �����������ı���
			holder.layout.setBackgroundResource(R.drawable.button_selector);
		}
		return convertView;
	}

	/***
	 * �����ݴ�����
	 * 
	 * @author jiqunpeng
	 * 
	 *         ����ʱ�䣺2013-12-20 ����3:11:21
	 */
	class Holder {
		TextView[] dataET;// ��ʾ������
		RelativeLayout layout;// �趨����
	}
}
