package com.health.archive;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.younext.R;

import com.health.util.T;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * ����������
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-4 ����5:07:18
 */
public class ArchiveMain extends SlidingFragmentActivity implements
		OnClickListener {

	private Fragment mContent;

	private static ArchiveMain instance;
	private TextView titleTv;
	private Button barButton;

	private boolean lock = false;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		initSlidingMenu(savedInstanceState);
		setActionBar();
		instance = this;
	}

	public static ArchiveMain getInstance() {
		return instance;
	}

	@SuppressLint("NewApi")
	private void setActionBar() {
		ActionBar ab = getActionBar();
		if (ab != null) {
			ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			View view = LayoutInflater.from(this).inflate(
					R.layout.layout_title_bar, null);
			titleTv = (TextView) view.findViewById(R.id.title_bar_name);
			barButton = (Button) view.findViewById(R.id.title_bar_btn);
			barButton.setOnClickListener(this);
			Button cButton = (Button) view
					.findViewById(R.id.title_bar_menu_btn);
			cButton.setOnClickListener(this);
			ab.setCustomView(view);
			ab.setDisplayHomeAsUpEnabled(true);
			// setSlidingActionBarEnabled(false);
		}
	}

	/**
	 * ��ʼ�������˵�
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// ��������״̬��Ϊ����õ�ColorFragment������ʵ����ColorFragment
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new ArchiveCover();

		// ��������ͼ����
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// ���û����˵���ͼ����
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();

		// ���û����˵�������ֵ
		SlidingMenu sMenu = getSlidingMenu();
		sMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sMenu.setShadowWidthRes(R.dimen.shadow_width);
		sMenu.setShadowDrawable(R.drawable.shadow);
		sMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sMenu.setFadeDegree(0.35f);

	}

	/**
	 * �л�Fragment��Ҳ���л���ͼ������
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 * �˵���ť����¼���ͨ�����ActionBar��Homeͼ�갴ť���򿪻����˵�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.title_bar_menu_btn:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ����Fragment��״̬
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	// �����޸�button����Ӧ�¼�
	private ActionBarEditable actionBarEdit;

	public interface ActionBarEditable {
		/***
		 * ���buttonʱ���Ĺ���
		 */
		public void processOnButton();
	}

	public void setActionBarEdit(ActionBarEditable edit) {
		actionBarEdit = edit;
	}

	/***
	 * ���ñ���
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		titleTv.setText(title);
	}

	/***
	 * ���ð�ť������
	 * 
	 * @param text
	 */
	public void setButtonText(String text) {
		barButton.setText(text);
	}

	/**
	 * ���ò˵��Ŀ���
	 * 
	 * @param isLock
	 */
	public void setLock(boolean isLock) {
		lock = isLock;
		getSlidingMenu().setSlidingEnabled(!lock);
	}

	private long lastClick = System.currentTimeMillis();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_btn:
			if (actionBarEdit != null)
				actionBarEdit.processOnButton();
			break;
		case R.id.title_bar_menu_btn:
			if (!lock)
				toggle();
			else{
				T.showShort(this, "�������޸ģ����ȱ��棡");
//				long c = System.currentTimeMillis();
//				if(c-lastClick<1000)
//					setLock(false);
//				lastClick = c;
				
			}
			break;
		default:
			break;
		}
	}
}
