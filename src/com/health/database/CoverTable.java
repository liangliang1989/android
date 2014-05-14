package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class CoverTable {
	// 表名
	public static final String TABLE_NAME = "tableName";
	
	//档案封面的10个属性
	public static final String NAME = "name";
	public static final String USER_ID = "userId";
	public static final String CURRENT_ADDRESS = "curAddress";
	public static final String HUJI_ADRESS = "hujiAddress";
	public static final String PHONE = "phone";
	public static final String STREET_NAME = "streetName";
	public static final String COUNTRY_NAME = "countryName";
	public static final String JIANDANGCOM = "jiandangcom";
	public static final String JIANDANGPERSON = "jiandangPerson";
	public static final String DOCTOR = "doctor";
	public static final String DATE = "date";
	
	public static Map<String, String> archiveCover() {
		Map<String, String> coverMap = new HashMap<String, String> ();
		coverMap.put(TABLE_NAME, "COVER");
		coverMap.put(USER_ID, "varchar(18)");
		coverMap.put(NAME, "varchar(14)");
		coverMap.put(CURRENT_ADDRESS,"varchar(50)");
		coverMap.put(HUJI_ADRESS,"varchar(50)");
		coverMap.put(PHONE,"varchar(50)");
		coverMap.put(STREET_NAME,"varchar(50)");
		coverMap.put(COUNTRY_NAME,"varchar(50)");
		coverMap.put(JIANDANGCOM ,"varchar(50)");
		coverMap.put(JIANDANGPERSON,"varchar(50)");
		coverMap.put(DOCTOR,"varchar(50)");
		coverMap.put(DATE,"varchar(50)");
		
		return coverMap;
	}
}
