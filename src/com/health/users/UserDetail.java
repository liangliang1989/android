package com.health.users;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.archive.ArchiveMain;
import com.health.bean.User;
import com.health.measurement.Measurement;
import com.health.myhealth.LocalRecord;
import com.health.myhealth.TableRecord;
import com.health.util.TwoCloumAdapter;

public class UserDetail extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail);
		initView();
	}

	private void initView() {
		ListView list = (ListView) findViewById(R.id.user_detail_list);
		List<String[]> data = new ArrayList<String[]>();
		User user = BaseActivity.getUser();
		if (user == null)// �û�������,�쳣���.
			finish();
		data.add(new String[] { "����", user.getName() });
		data.add(new String[] { "����", user.getCardNo() });
		data.add(new String[] { "�û�id", user.getCardNo() });
		data.add(new String[] { "��������", user.getBirthday() });
		data.add(new String[] { "�Ա�", user.getSex() });
		data.add(new String[] { "�ǳ�", user.getNickName() });
		data.add(new String[] { "���", user.getCustomerGuid() });
		data.add(new String[] { "����id", user.getUserGuid() });
		data.add(new String[] { "�ֻ�����", user.getMobile() });
		data.add(new String[] { "����", user.getEmail() });
		for (int i = 0; i < data.size(); i++) {
			String[] tmp = data.get(i);
			for (int j = 0; j < tmp.length; j++) {
				if (tmp[j] == null || tmp[j].length() == 0
						|| "null".equals(tmp[j]))
					data.get(i)[j] = "��";
			}

		}
		TwoCloumAdapter adapter = new TwoCloumAdapter(this, data);
		list.setAdapter(adapter);
		TextView nameTv = (TextView) findViewById(R.id.user_name_tv);
		nameTv.setText(user.getName());
	}

	/***
	 * ��ת������ʷ������¼
	 * 
	 * @param view
	 */
	public void measureHistory(View view) {
		Intent intent = new Intent(this, TableRecord.class);
		startActivity(intent);
	}

	/**
	 * ��ת�����û�����
	 * 
	 * @param view
	 */

	public void healthMeasuer(View view) {
		Intent intent = new Intent(this, Measurement.class);
		startActivity(intent);
	}
	/***
	 * ���ؼ�¼
	 * @param view
	 */
	public void localRecord(View view) {
		Intent intent = new Intent(this, LocalRecord.class);
		startActivity(intent);
	}

	/**
	 * ��ת�����û�����
	 * 
	 * @param view
	 */

	public void visitRecord(View view) {
		Intent intent = new Intent(this, VisitRecord.class);
		startActivity(intent);
	}

	/**
	 * ��ת�����û�����
	 * 
	 * @param view
	 */

	public void healthArchive(View view) {
		Intent intent = new Intent(this, ArchiveMain.class);
	//	Intent intent = new Intent(this, HealthArchive.class);
		startActivity(intent);
	}
}
