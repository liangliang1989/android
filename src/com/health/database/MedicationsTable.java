package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class MedicationsTable {
	//表名关键字
	public static final String TABLE_NAME = "tableName";
	//用户Id
	public static final String USER_ID = "userId";
	//建表时间
	public static final String CREATE_DATE = "createDate";
	//药物名称
	public static final String DRUG_NAME = "drugName";
	//用法
	public static final String USAGE = "usage";
	//用量
	public static final String DOSAGE = "dosage";
	//用药时间
	public static final String MEDICATION_TIME = "medicationTime";
	//服药依从性
	public static final String MEDICATION_COMPLIANCE = "medication_compliance";
	
	//建表描述
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
