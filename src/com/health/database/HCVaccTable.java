package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class HCVaccTable {
	//表名关键字
	public static final String TABLE_NAME = "tableName";
	//用户Id
	public static final String USER_ID = "userId";
	//建表时间
	public static final String CREATE_DATE = "createDate";
	//名称
	public static final String VACC_NAME = "name";
	//接种日期
	public static final String VACC_DATE = "date";
	//接种机构
	public static final String VACC_COM = "company";
	
	//建表描述
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