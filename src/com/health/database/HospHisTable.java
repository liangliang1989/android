package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class HospHisTable {
	//表名关键字
	public static final String TABLE_NAME = "tableName";
	//用户Id
	public static final String USER_ID = "userId";
	//建表时间
	public static final String CREATE_DATE = "createDate";
	//入/出院日期
	public static final String DATE_OF_IN_AND_OUT = "date";
	//原因
	public static final String REASONS = "reasons";
	//医疗机构名称
	public static final String MHO = "organization";
	//病案号
	public static final String PIN = "patientsNumber";
	
	//建表描述
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
