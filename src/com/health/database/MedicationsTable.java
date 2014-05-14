package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class MedicationsTable {
	//�����ؼ���
	public static final String TABLE_NAME = "tableName";
	//�û�Id
	public static final String USER_ID = "userId";
	//����ʱ��
	public static final String CREATE_DATE = "createDate";
	//ҩ������
	public static final String DRUG_NAME = "drugName";
	//�÷�
	public static final String USAGE = "usage";
	//����
	public static final String DOSAGE = "dosage";
	//��ҩʱ��
	public static final String MEDICATION_TIME = "medicationTime";
	//��ҩ������
	public static final String MEDICATION_COMPLIANCE = "medication_compliance";
	
	//��������
	public static Map<String, String> mediTable() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(TABLE_NAME, "MAINMEDI");
		map.put(CREATE_DATE, "varchar(50)");
		map.put(USER_ID, "varchar(50)");
		map.put(DRUG_NAME, "varchar(50)");
		map.put(USAGE, "varchar(50)");
		map.put(DOSAGE, "varchar(50)");
		map.put(MEDICATION_TIME, "varchar(50)");
		map.put(MEDICATION_COMPLIANCE, "varchar(50)");
		
		return map;
	}
}
