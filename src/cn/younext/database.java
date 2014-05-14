package cn.younext;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import cn.younext.DatabaseHelper;

public class database {
	/* �������ж������������ִ�У�Ҳ���Ը���緵��һ��DB��ʵ���������ȥ���� */
	private SQLiteDatabase db;
	private static DatabaseHelper dbhelper;

	public database(Context ctx, String dbname) {
		dbhelper = new DatabaseHelper(ctx, dbname, null, 1);
		db = dbhelper.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	/* ���صõ������ݿ��ʵ�� */
	public SQLiteDatabase getDatabase() {
		return db;
	}

	/* �����������Ա�����������ɾ�������ɾ����¼�����¼�¼ */

	/* ������ݱ� */
	public void creatTable(String TABLE_NAME, String createSQL) {
		String strend = "DROP TABLE IF EXISTS " + TABLE_NAME;
		try {
			db.execSQL(strend);
			db.execSQL(createSQL);
		} catch (SQLException e) {

		}
	}

	/* ɾ�����ݱ� */
	public void deleteTable(String TABLE_NAME) {
		String strend = "DROP TABLE IF EXISTS " + TABLE_NAME;
		try {
			db.execSQL(strend);
		} catch (SQLException e) {

		}
	}

	/* ���һ����¼,��ȷ��ӷ���1�����򷵻�-1 */
	public int insertRecord(String SQL) {
		int i = 1;
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			i = -1;
		}
		return i;
	}

	/* ��ȷɾ����¼�ͷ���1�����򷵻�-1 */
	public int deleteRecord(String SQL) {
		int i = 1;
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			i = -1;
		}
		return i;
	}

	/* ��ѯ��¼��� */
	public Cursor queryRecord(String SQL) {
		Cursor c;
		c = db.rawQuery(SQL, null);
		return c;
	}

	/* ���¼�¼,�ɹ�����1�����򷵻�-1 */
	public int updateTable(String SQL) {
		int i = 1;
		
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			i = -1;
		}
		return i;
	}


}
