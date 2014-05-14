package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class HCVaccTable {
	//�����ؼ���
	public static final String TABLE_NAME = "tableName";
	//�û�Id
	public static final String USER_ID = "userId";
	//����ʱ��
	public static final String CREATE_DATE = "createDate";
	//����
	public static final String VACC_NAME = "name";
	//��������
	public static final String VACC_DATE = "date";
	//���ֻ���
	public static final String VACC_COM = "company";
	
	//��������
	public static Map<String, String> VaccTableDesc() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(TABLE_NAME, "VaccTable");
		map.put(CREATE_DATE, "varchar(50)");
		map.put(USER_ID, "varchar(50)");
		map.put(VACC_NAME, "varchar(50)");
		map.put(VACC_DATE, "varchar(50)");
		map.put(VACC_COM, "varchar(50)");
		
		return map;
	}
}