package com.health.chronicdisease;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.health.database.Tables;

import cn.younext.R;

public class HypertensionTable {
	public static final String HYPERTENSION_TABLE = "hypertension_table";
	public static final String PATIENT="patient_name";
	public static final String SERIAL_ID = "serial_id";
	public static final String TURE_METHOD = "ture_method";
	public static final String REPORT_DATE="hypertension_reportdate";
	public static final String SIGN = "sign";
	public static final String BLOOD_PRESURE = "blood_presure";
	public static final String WEIGHT = "weight";
	public static final String INDEX = "physcial_index";
	public static final String HEART_RATE = "heart_rate";
	public static final String SIGN_OTHER = "sign_other";
	public static final String SMOKING = "smoking";
	public static final String DRINK = "drinking";
	public static final String SPORTWEEK = "sportweek";
	public static final String SPORTDAY = "sportday";
	public static final String SALT = "salt";
	public static final String MENTAL_ADJUST = "mental_adjust";
	public static final String OBEY_DOCTOR = "obey_doctor";
	public static final String AUXIEXAMINE = "auxi_examine";
	public static final String DRUGOBEY = "drug_obey";
	public static final String DRUGREACTION = "drug_reaction";
	public static final String TURECLASS = "tureclass";
	public static final String DRUGCONTENT = "drug_content";
	public static final String REFERRAL_REASON = "referral_reason";
	public static final String REFERRAL_DEPART = "referral_department";
	public static final String NEXTTUREDATE = "nextturedate";
	public static final String TUREDOCTOR = "turedoctor";

	public static final Map<String, Integer> cloumIdmap = Collections
			.unmodifiableMap(new HashMap<String, Integer>() {

				private static final long serialVersionUID = -8896139986212203908L;
				{
					put(PATIENT, R.id.hypertension_turename);
					put(SERIAL_ID, R.id.hypertension_turenum);
					put(REPORT_DATE, R.id.hypertension_reportdate);
					put(TURE_METHOD, R.id.hypertension_turemethod);
					put(SIGN, R.id.hypertension_symps);
					put(BLOOD_PRESURE, R.id.hypertension_edt_bloodpresure);
					put(WEIGHT, R.id.hypertension_edt_weight);
					put(INDEX, R.id.hypertension_edt_index);
					put(HEART_RATE, R.id.hypertension_edt_heartrate);
					put(SIGN_OTHER, R.id.hypertension_edt_signother);
					put(SMOKING, R.id.hypertension_edt_smoking);
					put(DRINK, R.id.hypertension_edt_drinking);
					put(SPORTWEEK, R.id.hypertension_edt_sport1);
					put(SPORTDAY, R.id.hypertension_edt_sport2);
					put(SALT, R.id.sce_salt);
					put(MENTAL_ADJUST, R.id.hypertension_sce_mentaladjust);
					put(OBEY_DOCTOR, R.id.hypertension_sce_obey);
					put(AUXIEXAMINE, R.id.edt_Auxiliary_examination);
					put(DRUGOBEY, R.id.hypertension_sce_drugobey);
					put(DRUGREACTION, R.id.hypertension_scedt_drugreaction);
					put(TURECLASS, R.id.hypertension_sce_tureclass);
					put(DRUGCONTENT, R.id.hypertension_druglist);
					put(REFERRAL_REASON, R.id.hypertension_referral_reason);
					put(REFERRAL_DEPART, R.id.hypertension_referral_department);
					put(NEXTTUREDATE, R.id.hypertension_det_nextturedate);
					put(TUREDOCTOR, R.id.hypertension_edt_turedoctor);
				}
			});

	public static Map<String, String> HypertensionTable() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, HYPERTENSION_TABLE);
		map.put(PATIENT, "varchar(10)");
		map.put(SERIAL_ID, "varchar(18)");
		map.put(REPORT_DATE, "varchar(50)");
		map.put(TURE_METHOD, "varchar(10)");
		map.put(SIGN, "varchar(10)");
		map.put(BLOOD_PRESURE, "varchar(10)");
		map.put(WEIGHT, "varchar(10)");
		map.put(INDEX, "varchar(10)");
		map.put(HEART_RATE, "varchar(10)");
		map.put(SIGN_OTHER, "text");
		map.put(SMOKING, "varchar(10)");
		map.put(DRINK, "varchar(10)");
		map.put(SPORTWEEK, "varchar(10)");
		map.put(SPORTDAY, "varchar(10)");
		map.put(SALT, "varchar(10)");
		map.put(MENTAL_ADJUST, "varchar(10)");
		map.put(OBEY_DOCTOR, "varchar(10)");
		map.put(AUXIEXAMINE, "text");
		map.put(DRUGOBEY, "varchar(20)");
		map.put(DRUGREACTION, "varchar(100)");
		map.put(TURECLASS, "varchar(50)");
		map.put(DRUGCONTENT, "text");
		map.put(REFERRAL_REASON, "text");
		map.put(REFERRAL_DEPART, "varchar(50)");
		map.put(NEXTTUREDATE, "varchar(50)");
		map.put(TUREDOCTOR, "varchar(50)");
		return map;

	}
}
