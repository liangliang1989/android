package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class HospHisTable {
	//�����ؼ���
	public static final String TABLE_NAME = "tableName";
	//�û�Id
	public static final String USER_ID = "userId";
	//����ʱ��
	public static final String CREATE_DATE = "createDate";
	//��/��Ժ����
	public static final String DATE_OF_IN_AND_OUT = "date";
	//ԭ��
	public static final String REASONS = "reasons";
	//ҽ�ƻ�������
	public static final String MHO = "organization";
	//������
	public static final String PIN = "patientsNumber";
	
	//��������
	public static Map<String, String> hosHisTable() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(TABLE_NAME, "HosHis");
		map.put(CREATE_DATE, "varchar(50)");
		map.put(USER_ID, "varchar(50)");
		map.put(DATE_OF_IN_AND_OUT, "varchar(50)");
		map.put(REASONS, "varchar(50)");
		map.put(MHO, "varchar(50)");
		map.put(PIN, "varchar(50)");
		
		return map;
	}
}
