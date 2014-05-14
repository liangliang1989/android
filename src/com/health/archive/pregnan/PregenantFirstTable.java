package com.health.archive.pregnan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

/***
 * 孕妇第一次产前检查
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-5-12 下午4:47:36
 */
public class PregenantFirstTable {
	public static final String table = "pregenant_first_talbe";
	public static final String id_card = "id_card";
	public static final String serial_id = "serial_id";
	public static final String fill_date = "fill_date";
	public static final String pregnan_week = "pregnan_week";
	public static final String age = "age";
	public static final String husband_name = "husband_name";
	public static final String husband_age = "husband_age";
	public static final String husband_phone = "husband_phone";
	public static final String pregnan_times = "pregnan_times";
	public static final String birth_times = "birth_times";
	public static final String last_mense = "last_mense";
	public static final String due_date = "due_date";
	public static final String history_disease = "history_disease";
	public static final String family_disease = "family_disease";
	public static final String women_operation_his = "women_operation_his";
	public static final String pregnan_his1 = "pregnan_his1";// 孕产史5个
	public static final String pregnan_his2 = "pregnan_his2";
	public static final String pregnan_his3 = "pregnan_his3";
	public static final String pregnan_his4 = "pregnan_his4";
	public static final String pregnan_his5 = "pregnan_his5";
	public static final String height = "height";
	public static final String weight = "weight";
	public static final String tizhi = "tizhi";
	public static final String bp = "bp";
	public static final String heart = "heart";
	public static final String lung = "lung";
	public static final String vulva = "vulva";
	public static final String vagina = "vagina";// 阴道
	public static final String cervix = "cervix";// 宫颈
	public static final String uterus = "uterus";// 子宫
	public static final String accessory = "accessory";
	public static final String BR = "BR";// 血常规
	public static final String UR = "UR";// UR
	public static final String ABO = "bp";
	public static final String blood_type_ABO = "blood_type_ABO";
	public static final String blood_type_Rh = "blood_type_Rh";
	public static final String glu = "glu";
	public static final String liver = "liver";
	public static final String kidney = "kidney";
	public static final String vaginal_discharge = "vaginal_discharge";
	public static final String hepatitis_b = "hepatitis_b";
	public static final String VDRL = "VDRL";
	public static final String HIV = "HIV";
	public static final String B_ultrasound = "B_ultrasound";
	public static final String guide = "guide";
	public static final String total_apprise = "total_apprise";
	public static final String transfer_advise = "transfer_advise";
	public static final String transfer_reason = "transfer_reason";
	public static final String transfer_org = "transfer_org";
	public static final String TCM = "TCM";
	public static final String next_date = "next_date";
	public static final String visit_doctor = "visit_doctor";

	public static Map<String, String> sixoldChildTable() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, table);
		map.put(id_card, "varchar20");
		map.put(id_card, "varchar(20)");
		map.put(serial_id, "varchar(20)");
		map.put(fill_date, "varchar(20)");
		map.put(pregnan_week, "varchar(20)");
		map.put(age, "varchar(20)");
		map.put(husband_name, "varchar(20)");
		map.put(husband_age, "varchar(20)");
		map.put(husband_phone, "varchar(20)");
		map.put(pregnan_times, "varchar(20)");
		map.put(birth_times, "varchar(20)");
		map.put(last_mense, "varchar(20)");
		map.put(due_date, "varchar(20)");
		map.put(history_disease, "varchar(20)");
		map.put(family_disease, "varchar(20)");
		map.put(women_operation_his, "varchar(20)");
		map.put(pregnan_his1, "varchar(20)");// 孕产史5个
		map.put(pregnan_his2, "varchar(20)");
		map.put(pregnan_his3, "varchar(20)");
		map.put(pregnan_his4, "varchar(20)");
		map.put(pregnan_his5, "varchar(20)");
		map.put(height, "varchar(20)");
		map.put(weight, "varchar(20)");
		map.put(tizhi, "varchar(20)");
		map.put(bp, "varchar(20)");
		map.put(heart, "varchar(20)");
		map.put(lung, "varchar(20)");
		map.put(vulva, "varchar(20)");
		map.put(vagina, "varchar(20)");// 阴道
		map.put(cervix, "varchar(20)");// 宫颈
		map.put(uterus, "varchar(20)");// 子宫
		map.put(accessory, "varchar(20)");
		map.put(BR, "varchar(20)");// 血常规
		map.put(UR, "varchar(20)");// UR
		map.put(ABO, "varchar(20)");
		map.put(blood_type_ABO, "varchar(20)");
		map.put(blood_type_Rh, "varchar(20)");
		map.put(glu, "varchar(20)");
		map.put(liver, "varchar(20)");
		map.put(kidney, "varchar(20)");
		map.put(vaginal_discharge, "varchar(20)");
		map.put(hepatitis_b, "varchar(20)");
		map.put(VDRL, "varchar(20)");
		map.put(HIV, "varchar(20)");
		map.put(B_ultrasound, "varchar(20)");
		map.put(guide, "varchar(20)");
		map.put(total_apprise, "varchar(20)");
		map.put(transfer_advise, "varchar(20)");
		map.put(transfer_reason, "varchar(20)");
		map.put(transfer_org, "varchar(20)");
		map.put(TCM, "varchar(20)");
		map.put(next_date, "varchar(20)");
		map.put(visit_doctor, "varchar(20)");
		return map;
	}

	public static final Map<String, Integer> cloumIdmap = Collections
			.unmodifiableMap(new HashMap<String, Integer>() {

				private static final long serialVersionUID = -8896139986212203908L;
				{
					//
					// put(id_card, R.id.pregnant_f_);
					// put(serial_id,
					// R.id.pregnant_f_);
					// put(fill_date,
					// R.id.pregnant_f_);
					// put(pregnan_week,
					// R.id.pregnant_f_);
					// put(age, R.id.pregnant_f_);
					// put(husband_name,
					// R.id.pregnant_f_);
					// put(husband_age,
					// R.id.pregnant_f_);
					// put(husband_phone,
					// R.id.pregnant_f_);
					// put(pregnan_times,
					// R.id.pregnant_f_);
					// put(birth_times,
					// R.id.pregnant_f_);
					// put(last_mense,
					// R.id.pregnant_f_);
					// put(due_date, R.id.pregnant_f_);
					// put(history_disease,
					// R.id.pregnant_f_);
					// put(family_disease,
					// R.id.pregnant_f_);
					// put(women_operation_his,
					// R.id.pregnant_f_);
					// put(pregnan_his1,
					// R.id.pregnant_f_);// 孕产史5个
					// put(pregnan_his2,
					// R.id.pregnant_f_);
					// put(pregnan_his3,
					// R.id.pregnant_f_);
					// put(pregnan_his4,
					// R.id.pregnant_f_);
					// put(pregnan_his5,
					// R.id.pregnant_f_);
					// put(height, R.id.pregnant_f_);
					// put(weight, R.id.pregnant_f_);
					// put(tizhi, R.id.pregnant_f_);
					// put(bp, R.id.pregnant_f_);
					// put(heart, R.id.pregnant_f_);
					// put(lung, R.id.pregnant_f_);
					// put(vulva, R.id.pregnant_f_);
					// put(vagina, R.id.pregnant_f_);//
					// 阴道
					// put(cervix, R.id.pregnant_f_);//
					// 宫颈
					// put(uterus, R.id.pregnant_f_);//
					// 子宫
					// put(accessory,
					// R.id.pregnant_f_);
					// put(BR, R.id.pregnant_f_);// 血常规
					// put(UR, R.id.pregnant_f_);// UR
					// put(ABO, R.id.pregnant_f_);
					// put(blood_type_ABO,
					// R.id.pregnant_f_);
					// put(blood_type_Rh,
					// R.id.pregnant_f_);
					// put(glu, R.id.pregnant_f_);
					// put(liver, R.id.pregnant_f_);
					// put(kidney, R.id.pregnant_f_);
					// put(vaginal_discharge,
					// R.id.pregnant_f_);
					// put(hepatitis_b,
					// R.id.pregnant_f_);
					// put(VDRL, R.id.pregnant_f_);
					// put(HIV, R.id.pregnant_f_);
					// put(B_ultrasound,
					// R.id.pregnant_f_);
					// put(guide, R.id.pregnant_f_);
					// put(total_apprise,
					// R.id.pregnant_f_);
					// put(transfer_advise,
					// R.id.pregnant_f_);
					// put(transfer_reason,
					// R.id.pregnant_f_);
					// put(transfer_org,
					// R.id.pregnant_f_);
					// put(TCM, R.id.pregnant_f_);
					// put(next_date,
					// R.id.pregnant_f_);
					// put(visit_doctor,
					// R.id.pregnant_f_);
				}

			});

}
