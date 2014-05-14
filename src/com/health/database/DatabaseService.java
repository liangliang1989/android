package com.health.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.health.util.L;

public class DatabaseService {
	private static final String TAG = "DatabaseService";
	private DataOpenHelper dbOpenHelper;

	public DatabaseService(Context context) {
		super();
		this.dbOpenHelper = new DataOpenHelper(context);
	}

	/**
	 * ����id����ͱ�������ѯ��¼
	 * 
	 * @param idCard
	 * @param tableDesc
	 * @return
	 */
	public List<Map<String, String>> select(String idCard,
			Map<String, String> tableDesc) {
		String tableName = tableDesc.get(Tables.TABLE_NAME);
		tableDesc.remove(Tables.TABLE_NAME);
		StringBuilder selectBuilder = new StringBuilder("select * from ");
		selectBuilder.append(tableName);
		selectBuilder.append(" where ");
		selectBuilder.append(Tables.CARDNO);
		selectBuilder.append(" = ?");
		Set<String> attrSet = tableDesc.keySet();
		String[] selectAttrs = attrSet.toArray(new String[0]);
		String[] keys = { idCard };
		return select(selectBuilder.toString(), keys, selectAttrs);
	}

	/**
	 * �����¼
	 * 
	 * @param tableDesc������
	 * @param ValueMap����ֵ
	 */
	public void insert(Map<String, String> tableDesc,
			Map<String, String> ValueMap) {
		String tableName = tableDesc.get(Tables.TABLE_NAME);
		tableDesc.remove(Tables.TABLE_NAME);
		Set<String> attrSet = tableDesc.keySet();
		String[] insertAttrs = attrSet.toArray(new String[0]);
		String[] insertValue = new String[insertAttrs.length];
		StringBuilder askBuilder = new StringBuilder("values(");// �ʺŹ��죬ռλ��
		StringBuilder sqlBuilder = new StringBuilder("insert into ");
		sqlBuilder.append(tableName);
		sqlBuilder.append("(");
		for (int i = 0; i < insertAttrs.length; i++) {
			sqlBuilder.append(insertAttrs[i]);
			sqlBuilder.append(",");
			askBuilder.append("?,");
			insertValue[i] = ValueMap.get(insertAttrs[i]);
		}
		String insertSql = sqlBuilder.toString();
		insertSql = insertSql.substring(0, insertSql.length() - 1);// ɾ�����һ������
		insertSql += ")";
		String asks = askBuilder.toString();
		asks = asks.substring(0, asks.length() - 1);// ɾ�����һ������
		insertSql += asks + ")";
		Log.i(TAG, insertSql);
		insert(insertSql, insertValue);
	}

	/**
	 * �������������ݿ���ȡ����
	 * 
	 * @param selectSql
	 * @param keys
	 * @param attrs
	 * @return
	 */
	private List<Map<String, String>> select(String selectSql, String[] keys,
			String[] attrs) {
		Log.i(TAG, selectSql + Arrays.toString(keys));
		SQLiteDatabase findDb = dbOpenHelper.getReadableDatabase();
		Cursor cursor = findDb.rawQuery(selectSql, keys);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			for (String attr : attrs)
				map.put(attr, cursor.getString(cursor.getColumnIndex(attr)));
			result.add(map);
		}
		return result;
	}

	/**
	 * ���ݲ������Ͳ���ֵ�����ݿ����������
	 * 
	 * @param insertSql
	 * @param insertValue
	 */
	private void insert(String insertSql, String[] insertValue) {
		Log.i(TAG, insertSql + Arrays.toString(insertValue));
		SQLiteDatabase insertDb = dbOpenHelper.getWritableDatabase();
		try {
			insertDb.execSQL(insertSql, insertValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		insertDb.close();
		// insertDb.insert(table, nullColumnHack,
		// values)
	}

	/***
	 * ��table������column����ֵΪvalue�ļ�¼
	 * 
	 * @param table
	 * @param cloumn
	 * @param value
	 * @return
	 */
	public Cursor query(String table, String cloumn, String value) {
		L.i(TAG, "(query)" + table + ": " + cloumn + "=" + value);
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(table);
		sql.append(" where ");
		sql.append(cloumn);
		sql.append(" = ?");
		SQLiteDatabase queryDb = dbOpenHelper.getReadableDatabase();
		Cursor cursor = queryDb
				.rawQuery(sql.toString(), new String[] { value });
		return cursor;
	}

	/**
	 * ����һ��������ֵ��ѯ��¼, ����ʾ��query("TABLE", *
	 * "sysId=? and id_card=?",{"1","320"})
	 * 
	 * @param table
	 * @param where
	 * @param para
	 * @return
	 */
	public Cursor query(String table, String where, String[] para) {
		L.i(TAG, "(query)" + table + ": " + where + "=" + Arrays.toString(para));
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(table);
		sql.append(" where ");
		sql.append(where);
		SQLiteDatabase queryDb = dbOpenHelper.getReadableDatabase();
		Cursor cursor = queryDb.rawQuery(sql.toString(), para);
		return cursor;
	}

	/***
	 * 
	 * @param table
	 * @param values
	 * @return ������к�
	 */
	public long insert(String table, ContentValues values) {
		L.i(TAG, "(insert)" + table + ": " + values.toString());
		SQLiteDatabase insertDb = dbOpenHelper.getWritableDatabase();
		long rawId = insertDb.insert(table, null, values);
		insertDb.close();
		L.i(TAG, "(inserted) rawId:" + rawId);
		return rawId;
	}

	/***
	 * ɾ��table����cloumn��ֵΪvalues�ļ�¼
	 * 
	 * @param table
	 * @param cloumn
	 * @param value
	 * @return
	 */
	public int delete(String table, String cloumn, String value) {
		L.i(TAG, "(delete)" + table + ": " + cloumn + ":" + value);
		SQLiteDatabase deleteDb = dbOpenHelper.getWritableDatabase();
		int raws = deleteDb.delete(table, cloumn + " = ?",
				new String[] { value });
		deleteDb.close();
		L.i(TAG, "(deleted) raws:" + raws);
		return raws;
	}

	/***
	 * ��������
	 * 
	 * @param table
	 * @param cloumnҪ�����е�key
	 * @param valueҪ���µ���value
	 * @param values
	 */
	public int update(String table, String cloumn, String value,
			ContentValues values) {
		L.i(TAG, "(update)" + table + ": " + cloumn + ":" + value + " "
				+ values);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int rows = db.update(table, values, cloumn + "=?",
				new String[] { value });
		db.close();
		L.i(TAG, "(update) raws:" + rows);
		return rows;
	}

	/***
	 * 
	 * @param table
	 * @param where
	 *            �����Ӿ�
	 * @param para
	 *            �����Ӿ���ռλ����Ӧ�Ĳ���
	 * @param values
	 */
	public int update(String table, String where, String[] para,
			ContentValues values) {
		L.i(TAG,
				"(update)" + table + ": " + where + ":" + Arrays.toString(para)
						+ " " + values);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int rows = db.update(table, values, where, para);
		db.close();
		L.i(TAG, "(update) raws:" + rows);
		return rows;
	}

	/***
	 * ɾ����
	 * 
	 * @param table
	 */
	public void delateTale(String table) {
		L.i(TAG, "(delateTale)" + table);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("drop table " + table);
		db.close();
	}

}
