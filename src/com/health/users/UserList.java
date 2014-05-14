package com.health.users;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.Logup;
import com.health.archive.ArchiveMain;
import com.health.bean.User;
import com.health.util.L;
import com.health.util.MyProgressDialog;
import com.health.util.T;
import com.health.util.ThreeCloumAdpter;
import com.health.web.BackGroundThread;
import com.health.web.BackGroundThread.BackGroundTask;
import com.health.web.WebService;
import com.health.web.WebService.StatusCode;

/***
 * �û��б�����ý�����ʾ��ǰҽ������������û���ҽ�����Դ����û�;
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-3-13 ����11:29:04
 */
public class UserList extends BaseActivity {
	private static List<String[]> datas = null;
	private static ThreeCloumAdpter mAdpter;
	private int cPage = 0;// �û��б�ǰҳ
	private MyHandler mHandler;
	private EditText queryEt;
	private static List<User> userList;
	private static MyProgressDialog mDialog;
	private static Context context;
	private static String[] title = new String[] { "����", "����", "�Ա�" };
	private boolean hasMore = false;
	private static final int PAGE_SIZE = 10;
	private static final int NO_MORE_MSG = 0x100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list);
		context = this;
		mDialog = new MyProgressDialog(context);
		userList = new ArrayList<User>();
		queryEt = (EditText) findViewById(R.id.query_et);
		ListView listView = (ListView) findViewById(R.id.user_listview);
		datas = initTitle();
		mAdpter = new ThreeCloumAdpter(context, datas);
		listView.setAdapter(mAdpter);
		mHandler = new MyHandler();
		getUserListInBack(null);
		listView.setOnItemClickListener(itemClickListener);
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (hasMore == true && position == datas.size() - 1) {
				datas.remove(position);
				mDialog.show();
				cPage++;
				getUserListInBack(null);
			} else if (position > 0) {
				User user = userList.get(position - 1);
				String cardNo = user.getCardNo();
				if (cardNo == null || cardNo.equals("null")
						|| cardNo.length() < 1) {
					Toast.makeText(context, "���û��˺Ų����ڣ����ܻ�ȡ�û���Ϣ",
							Toast.LENGTH_LONG).show();
					return;
				}
				BaseActivity.setUser(userList.get(position - 1));
				// Intent intent = new
				// Intent(UserList.this,
				// ArchiveMain.class);
				Intent intent = new Intent(UserList.this, UserDetail.class);
				startActivity(intent);
			}

		}

	};

	private static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mDialog.cancel();
			switch (msg.what) {
			case WebService.OK:
				// checkEmpty();
				mAdpter.notifyDataSetChanged();
				break;
			case WebService.ERROE:
				T.showLong(context, "�������");
				break;
			case NO_MORE_MSG:
				mAdpter.notifyDataSetChanged();
				T.showLong(context, "û�������ˣ�");
				break;
			case StatusCode.UNKNOWN_ERROR:
				T.showLong(context, "δ֪�������");
				break;
			}
		}
	}

	// private static void checkEmpty() {
	// if (datas.size() <= 1) {
	// userList.clear();
	// datas.clear();
	// datas.add(title);
	// userList.add(new User("test2",
	// "330772197805010002"));
	// }
	// }

	public static JSONObject getUserList(String doctorID, int cPage,
			String query) {
		try {
			JSONObject json = new JSONObject();
			json.put(WebService.GUID_KEY, WebService.GUID_VALUE);
			json.put(WebService.DOCTOR_NAME, BaseActivity.getGroup()
					.getUserName());
			json.put(WebService.PASS_WORD, BaseActivity.getGroup()
					.getPassword());
			JSONObject data = new JSONObject();
			data.put(WebService.PAGEINDEX, String.valueOf(cPage));
			data.put(WebService.PAGECOUNT, String.valueOf(PAGE_SIZE));
			if (query != null)
				data.put(WebService.PARAM, query);
			json.put(WebService.DATA, data);
			JSONObject result = WebService.postConenction(json,
					WebService.PATH_USER_LIST);
			return result;
		} catch (Exception e) {
			L.e("getUserList", e.getMessage());
			return null;
		}
	}

	private void getUserList(String query) {
		if (BaseActivity.getGroup() == null)
			return;
		JSONObject result = getUserList(BaseActivity.getGroup().getUserName(),
				cPage, query);

		try {
			int state = result.getInt(WebService.STATUS_CODE);
			if (WebService.OK == state) {
				// clearContent();
				JSONArray users = (JSONArray) result.get(WebService.DATA);
				if (users.length() == 0) {
					mHandler.obtainMessage(NO_MORE_MSG).sendToTarget();
					return;
				}
				if (users.length() >= PAGE_SIZE)
					hasMore = true;
				else
					hasMore = false;
				for (int i = 0; i < users.length(); i++) {
					JSONObject userJson = users.getJSONObject(i);
					String cardNo = userJson.getString(WebService.CARDNO);
					boolean help = true;
					if (help// ���ڲ�����û��cardNo���˻��ˣ���ѡ���ʱ����ʾһ��
							|| (cardNo != null && cardNo.length() > 1 && !cardNo
									.equals("null"))) {// û��cardNo���˻�����
						cardNo = "null".equals(cardNo) ? "" : cardNo;
						User user = new User(cardNo,
								userJson.getString(WebService.USERID),
								userJson.getString(WebService.BIRTHDAY),
								userJson.getString(WebService.SEX),
								userJson.getString(WebService.IMAGEURL),
								userJson.getString(WebService.EMAIL),
								userJson.getString(WebService.NICKNAME),
								userJson.getString(WebService.CUSTOMERGUID),
								userJson.getString(WebService.NAME),
								userJson.getString(WebService.USERGUID),
								userJson.getString(WebService.MOBILE));
						userList.add(user);
						String[] item = { user.getName(), user.getCardNo(),
								user.getSex() };
						datas.add(item);
					}
				}
				if (hasMore) {
					String[] loadMore = { "", "������ظ���", "" };
					datas.add(loadMore);
				}
			}
			mHandler.obtainMessage(state).sendToTarget();
		} catch (Exception e) {
			mHandler.obtainMessage(WebService.ERROE).sendToTarget();
		}

	}

	/***
	 * ��̨�̲߳�ѯ
	 * 
	 * @param query
	 */
	private void getUserListInBack(final String query) {
		new BackGroundThread(new BackGroundTask() {
			@Override
			public void process() {
				getUserList(query);
			}
		}).start();
		mDialog.show();
	}

	/***
	 * ��ʼ�����ݣ��ṩ��ͷ
	 * 
	 * @return
	 */
	private List<String[]> initTitle() {
		List<String[]> datas = new ArrayList<String[]>();
		datas.add(title);
		return datas;
	}

	/***
	 * ��ת��������û�
	 * 
	 * @param view
	 */

	public void addUser(View view) {
		Intent intent = new Intent(this, Logup.class);
		startActivity(intent);
	}

	/***
	 * ɾ�����ݣ���������
	 */
	public void clearContent() {
		String[] title = datas.get(0);
		userList.clear();
		datas.clear();
		datas.add(title);
	}

	public void searchUser(View view) {
		String query = queryEt.getText().toString();
		if (query.length() < 1) {
			query = null;
		}
		clearContent();
		mAdpter.notifyDataSetChanged();
		getUserListInBack(query);
	}
}
