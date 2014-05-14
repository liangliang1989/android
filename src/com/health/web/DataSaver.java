package com.health.web;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

import com.health.database.DatabaseService;
import com.health.database.Tables;

/**
 * 保存数据工具,保存数据到后台和本地数据库
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-5-13 下午2:37:28
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
	 * 先上传数据到后台，然后将values，连同上传成功与否的状态插入到数据库中；
	 * 同时para也插入数据库，以备下次重新上传。
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
		// 移除表名
		values.remove(Tables.TABLE_NAME);
		values.put(WebService.STATUS_CODE, status);
		// 上传到后台的完整参数,以备下次重新上传
		values.put(Tables.UPLOAD_PARA, uploadPara.toString());
		dbService.insert(table, values);
		return result;
	}
}
