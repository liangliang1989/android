package com.health.archive;

import static com.health.archive.VisitBaseActivity.FRESH_UI;
import static com.health.archive.VisitBaseActivity.SAVE_ERROE;
import static com.health.archive.VisitBaseActivity.SAVE_OK;
import cn.younext.R;

import com.health.archive.baby.oneold.OneOldChilldVistit;
import com.health.database.DatabaseService;
import com.health.util.T;
import com.health.viewUtil.ChoiceEditText;

import android.R.integer;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;

/***
 * ���ʱ�Ļ��࣬��װ��һЩ��Ҫ���õļ�������
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-4-24 ����5:17:03
 */
public abstract class VisitBaseActivity extends Activity {
	protected DatabaseService dbService;
	public static final int REQUEST_FRESH = 0x0010;
	// �кŵ�id��Ϊ-1��ʾ�������½�
	public final static String SYS_ID = "sys_id";
	protected static final String NEW = "-1";
	protected String sysId = NEW;
	// ���½����־
	protected static final int FRESH_UI = 0x10;
	// ȷ�ϰ�ť
	protected static final int POSITIVE = 0x11;
	// ����ɹ�
	protected static final int SAVE_OK = 0x12;
	// ����ʧ��
	protected static final int SAVE_ERROE = 0x13;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FRESH_UI:
				initView();
				break;
			case SAVE_OK:
				T.showShort(VisitBaseActivity.this, "����ɹ�");
				setState(true);
				finish();
				break;
			case SAVE_ERROE:
				T.showShort(VisitBaseActivity.this, "����ʧ��");
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dbService = new DatabaseService(this);
		// �����ڲ鿴�����½�,0��ʾ�½�,1��ʾ�鿴
		sysId = getIntent().getStringExtra(SYS_ID);
	}

	abstract protected void initView();

	abstract protected void setState(boolean b);

	/***
	 * ��sql�α������ı�����
	 * 
	 * @param cursor
	 * @param cloumn
	 * @param editTextId
	 */
	protected void setTextFromCursor(Cursor cursor, String cloumn,
			int editTextId) {
		String text = getCursorString(cursor, cloumn);
		setText(editTextId, text);
	}

	protected void setTextFromCursor(Cursor cursor, String cloumn,
			int[] editTextIdandClass) {

		if (editTextIdandClass[0] == 0) {
			String text = getCursorString(cursor, cloumn);
			setText(editTextIdandClass[1], text);
		} else {
			float num = cursor.getInt(cursor.getColumnIndex(cloumn));
			((RatingBar) findViewById(editTextIdandClass[1])).setRating(num);
		}
	}

	/**
	 * ����id��Ӧ��EditText��stringΪtext
	 * 
	 * @param id
	 * @param text
	 */
	protected void setText(int id, String text) {
		if (text != null)
			((EditText) findViewById(id)).setText(text);
	}

	/***
	 * ��װ�α�����ⷽ��
	 * 
	 * @param cursor
	 * @param cloumn
	 * @return
	 */
	protected String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

	protected void setChoiceEditText(int id, String[] items, String editableItem) {
		ChoiceEditText cet = (ChoiceEditText) findViewById(id);
		cet.setFixItems(items);
		if (editableItem != null)
			cet.setEditableItem(editableItem);
	}

	protected String getEditTextString(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

}
