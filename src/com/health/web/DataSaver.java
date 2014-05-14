package com.health.web;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

import com.health.database.DatabaseService;
import com.health.database.Tables;

/**
 * �������ݹ���,�������ݵ���̨�ͱ������ݿ�
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-5-13 ����2:37:28
 */
public class DataSaver {
	public static final String PATH = "path";
	public static final String PARA = "para";

	// private Context mContext;
	private DatabaseService dbService;

	public DataSaver(Context context) {
		// mContext = context;
		dbService = new DatabaseService(context);
	}

	/***
	 * ���ϴ����ݵ���̨��Ȼ��values����ͬ�ϴ��ɹ�����״̬���뵽���ݿ��У�
	 * ͬʱparaҲ�������ݿ⣬�Ա��´������ϴ���
	 * 
	 * @param values
	 * @param uploadPara
	 * @return
	 * @throws Exception
	 */
	public JSONObject uploadAndSave2db(ContentValues values,
			JSONObject uploadPara) throws Exception {
		String path = uploadPara.getString(PATH);
		JSONObject para = uploadPara.getJSONObject(PARA);
		JSONObject result = WebService.postConenction(para, path);
		int status = result.getInt(WebService.STATUS_CODE);
		String table = values.getAsString(Tables.TABLE_NAME);
		// �Ƴ�����
		values.remove(Tables.TABLE_NAME);
		values.put(WebService.STATUS_CODE, status);
		// �ϴ�����̨����������,�Ա��´������ϴ�
		values.put(Tables.UPLOAD_PARA, uploadPara.toString());
		dbService.insert(table, values);
		return result;
	}
}
