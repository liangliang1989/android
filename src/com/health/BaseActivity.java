package com.health;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.health.bean.Group;
import com.health.bean.Session;
import com.health.bean.User;
import com.health.database.Cache;

public class BaseActivity extends Activity {
	protected Context context;
	protected static Cache cache;
	protected String userName;
	//private static Group group = null;
	private static User user = null;
	private static Session groupSession = null;// ������¼ʱ��session

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		cache = new Cache(context);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void setGroup(Group g) {
		
		cache.saveGroup(g);
	}

	public static Group getGroup() {
		return cache.getGroup();
	}

	public static void setUser(User u) {
		user = u;
	}

	public static User getUser() {
		return user;
	}

	public static void setSession(Session s) {
		groupSession = s;
	}

	public static Session getSession() {
		return groupSession;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ����Ϊ����
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
}
