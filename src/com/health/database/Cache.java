package com.health.database;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.health.BaseActivity;
import com.health.bean.Group;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * �������ݵ�����SharedPreferences
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2013-10-25 ����10:22:42
 */
public class Cache {
	
	public static final String KRLBP = "KrlBp";
	
	public static final String PC300 = "PC300";
	public static final String BENECHECK = "BeneCheck";// �ٽ�Ѫ���豸
	public static final String GMPUA = "GmpUa";// ��Һ������

	private static final String USER_ID = "user_id";
	public static final String ITEM = "ITEM";
	public static final String BP = "bp";
	public static final String TEMP = "temp";
	public static final String BO = "bo";
	public static final String GLU = "glu";
	public static final String UA = "ua";
	public static final String CHOL = "chol";
	public static final String URINE = "urine";
	private static final String GROUP_ID = "group_id";
	private static final String CONTAC_NAMES = "names";
	private static final String CONTACT = "Contact";
	public static final String KRLECG = "KRLECG";
	public static final String NICKNAME = "nickname";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String SEX = "sex";
	public static final String PHONE = "phone";
	public static final String ADDRESS = "address";
	public static final String TOKEN = "token";
	Context context;
	private SharedPreferences sharedPrefrences;
	private Editor editor;

	public Cache(Context context) {
		this.context = context;
		sharedPrefrences = context.getSharedPreferences("padhealth",
				Context.MODE_PRIVATE);
		editor = sharedPrefrences.edit();
	}

	//
	// /**
	// * ���浱ǰ�û�id
	// *
	// * @param idCard
	// */
	// public void saveUserId(String idCard) {
	// editor.putString(USER_ID, idCard);
	// editor.commit();// �ύ
	// }
	//
	// /**
	// * ��ȡ��ǰ�û�id
	// *
	// * @return
	// */
	// public String getUserId() {
	// return sharedPrefrences.getString(USER_ID,
	// null);
	// }

	/**
	 * �����豸��ַ
	 * 
	 * @param device
	 * @param address
	 */
	public void saveDeviceAddress(String device, String address) {
		editor.putString(device, address);
		editor.commit();// �ύ
	}

	/**
	 * ��ȡ�豸�ĵ�ַ
	 * 
	 * @param device
	 * @return
	 */
	public String getDeviceAddress(String device) {
		return sharedPrefrences.getString(device, null);
	}

	/**
	 * ���浱ǰ�û���item��Ŀ��������
	 * 
	 * @param item
	 * @param dataMap
	 */
	public void saveItem(String item, Map<String, String> dataMap) {
		JSONObject json = new JSONObject(dataMap);
		saveItem(item, json);
	}

	/***
	 * ���浱ǰ�û�item��������
	 * 
	 * @param item
	 * @param json
	 */
	public void saveItem(String item, JSONObject json) {
		String id = BaseActivity.getUser().getCardNo();
		editor.putString(id + item, json.toString());
		editor.commit();// �ύ

	}

	/**
	 * ��ȡ����ĵ�ǰ�û��Ĳ�������
	 * 
	 * @param item
	 * @return
	 */
	public JSONObject getItem(String item) {
		String id = BaseActivity.getUser().getCardNo();
		String itemString = sharedPrefrences.getString(id + item, null);
		if (null == itemString)
			return null;
		try {
			return new JSONObject(itemString);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * ���浱ǰ�û���Ϣ
	 * 
	 * @param userInfo
	 */
	public void saveUserInfo(JSONObject userInfo) {
		editor.putString("userInfo", userInfo.toString());
		editor.commit();// �ύ
	}

	/***
	 * ���浱ǰ�û���Ϣ
	 * 
	 * @param userInfo
	 */
	public JSONObject getUserInfo() {
		JSONObject json = new JSONObject();
		try {
			json = new JSONObject(sharedPrefrences.getString("userInfo", "{}"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/***
	 * ��ȡ��ǰ�û�����
	 * 
	 * @return
	 */
	public String getUserName() {
		String userName = "";
		JSONObject json = getUserInfo();
		try {
			userName = json.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userName;

	}

	/***
	 * ���������
	 * 
	 * @param count
	 */
	public void saveGroupId(String count) {
		editor.putString(GROUP_ID, count);
		editor.commit();// �ύ
	}

	/**
	 * ��ȡ��ǰ�û�id
	 * 
	 * @return
	 */
	public String getGroupId() {
		return sharedPrefrences.getString(GROUP_ID, null);
	}

	/***
	 * ���������û�������
	 * 
	 * @param count
	 */
	public boolean saveContactNames(String names) {
		editor.putString(CONTAC_NAMES, names);
		return editor.commit();// �ύ
	}

	public String getContactNames() {
		return sharedPrefrences.getString(CONTAC_NAMES, null);
	}

	/***
	 * ������ϵ�������͵绰
	 * 
	 * @param name
	 * @param phone
	 */
	public boolean saveContact(String name, String phone) {
		editor.putString(CONTACT + name, phone);
		return editor.commit();// �ύ
	}

	/**
	 * ����������ȡ�绰
	 * 
	 * @param name
	 * @return
	 */
	public String getContact(String name) {
		return sharedPrefrences.getString(CONTACT + name, null);
	}

	public boolean saveGroup(Group g) {
		editor.putString(NICKNAME, g.getNickName());
		editor.putString(USERNAME, g.getUserName());
		editor.putString(ADDRESS, g.getAddress());
		editor.putString(TOKEN, g.getToken());
		editor.putString(PASSWORD, g.getPassword());
		editor.putString(SEX, g.getSex());
		editor.putString(PHONE, g.getPhone());
		return editor.commit();

	}

	public Group getGroup() {
		return new Group(sharedPrefrences.getString(NICKNAME, null),
				sharedPrefrences.getString(USERNAME, null),
				sharedPrefrences.getString(TOKEN, null),
				sharedPrefrences.getString(PASSWORD, null));
	}

}
