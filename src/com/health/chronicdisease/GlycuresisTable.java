package com.health.chronicdisease;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

public class GlycuresisTable {
	public static final String GLYCURESIS_TABLE = "glycuresis_table";
	public static final String PATIENT="patient_name";
	public static final String SERIAL_ID = "serial_id";
	public static final String TURE_METHOD = "ture_method";
	public static final String REPORT_DATE="glycuresis_reportdate";
	public static final String SIGN = "sign";
	public static final String BLOOD_PRESURE = "blood_presure";
	public static final String WEIGHT = "weight";
	public static final String INDEX = "physical_index";
	public static final String PULSE = "pulse";
	public static final String SIGN_OTHER = "sign_other";
	public static final String SMOKING = "smoking";
	public static final String DRINK = "drinking";
	public static final String SPORTWEEK = "sportweek";
	public static final String SPORTDAY = "sportday";
	public static final String FOOD = "food";
	public static final String MENTAL_ADJUST = "mental_adjust";
	public static final String GLUCOSE="glucose";
	public static final String HEMOGLOBIN="hemoglobin";
	
	public static final String EXAMDATE="exam_date";
	public static final String OBEY_DOCTOR = "obey_doctor";
	public static final String AUXIEXAMINE = "auxi_examine";
	public static final String DRUGOBEY = "drug_obey";
	public static final String DRUGREACTION = "drug_reaction";
	public static final String RHG="rhg";
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
					put(PATIENT, R.id.glycuresis_patientname);
					put(SERIAL_ID, R.id.glycuresis_turenum);
					put(REPORT_DATE, R.id.glycuresis_reportdate);
					put(TURE_METHOD, R.id.glycuresis_turemethod);
					put(SIGN, R.id.glycuresis_symps);
					put(BLOOD_PRESURE, R.id.glycuresis_edt_bloodpresure);
					put(WEIGHT, R.id.glycuresis_edt_weight);
					put(INDEX, R.id.glycuresis_edt_index);
					put(PULSE, R.id.glycuresis_sce_pulse);
					put(SIGN_OTHER, R.id.glycuresis_edt_signother);
					put(SMOKING, R.id.glycuresis_edt_smoking);
					put(DRINK, R.id.glycuresis_edt_drinking);
					put(SPORTWEEK, R.id.glycuresis_edt_sport1);
					put(SPORTDAY, R.id.glycuresis_edt_sport2);
					put(FOOD, R.id.edt_food);
					put(MENTAL_ADJUST, R.id.glycuresis_sce_mentaladjust);
					put(OBEY_DOCTOR, R.id.glycuresis_sce_obey);
					put(GLUCOSE, R.id.glycuresis_edt_sugar);
					put(HEMOGLOBIN,R.id.glycuresis_edt_hemoglobin);
					put(EXAMDATE,R.id.glycuresis_edt_examinedate);
					put(AUXIEXAMINE, R.id.glycuresis_edt_examineother);
					put(DRUGOBEY, R.id.glycuresis_sce_medicalobey);
					put(DRUGREACTION, R.id.glycuresis_scedt_drugreaction);
					put(RHG, R.id.glycuresis_sce_rhg);
					put(TURECLASS, R.id.glycuresis_sce_tureclass);
					put(DRUGCONTENT, R.id.glycuresis_druglist);
					put(REFERRAL_REASON, R.id.glycuresis_referral_reason);
					put(REFERRAL_DEPART, R.id.glycuresis_referral_department);
					put(NEXTTUREDATE, R.id.glycuresis_det_nextturedate);
					put(TUREDOCTOR, R.id.glycuresis_edt_turedoctor);
				}
			});

	public static Map<String, String> GlycuresisTable() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, GLYCURESIS_TABLE);
		map.put(PATIENT, "varchar(10)");
		map.put(SERIAL_ID, "varchar(18)");
		map.put(REPORT_DATE, "varchar(50)");
		map.put(TURE_METHOD, "varchar(10)");
		map.put(SIGN, "varchar(10)");
		map.put(BLOOD_PRESURE, "varchar(10)");
		map.put(WEIGHT, "varchar(10)");
		map.put(INDEX, "varchar(10)");
		map.put(PULSE, "varchar(10)");
		map.put(SIGN_OTHER, "text");
		map.put(SMOKING, "varchar(10)");
		map.put(DRINK, "varchar(10)");
		map.put(SPORTWEEK, "varchar(10)");
		map.put(SPORTDAY, "varchar(10)");
		map.put(FOOD, "varchar(10)");
		map.put(GLUCOSE, "varchar(10)");
		map.put(HEMOGLOBIN,"varchar(10)");
		map.put(RHG, "varchar(10)");
		map.put(EXAMDATE,  "varchar(50)");
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
