package com.health.archive.baby;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

/***
 * 三到六岁的小朋友
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-22 下午3:02:19
 */
public class SixOldChildTable {
	public static final String table = "sixold_table";
	public static final String id_card = "id_card";
	public static final String serial_id = "serial_id";
	public static final String age = "age";
	public static final String visit_date = "visit_date";
	public static final String weight = "weight";
	public static final String height = "height";
	public static final String growth_assess = "growth_assess";
	public static final String version = "version";
	public static final String hear = "hear";
	public static final String tooth_num = "tooth_num";
	public static final String decayed_t_num = "decayed_t_num";
	public static final String heart_hear = "heart_hear";
	public static final String abdomen_touch = "abdomen_touch";
	public static final String HGB = "HGB";
	public static final String seak_state = "seak_state";
	public static final String transfer_advise = "transfer_advise";
	public static final String transfer_reason = "transfer_reason";
	public static final String transfer_org = "transfer_org";
	public static final String guide = "guide";
	public static final String TCM = "TCM";
	public static final String next_date = "next_date";
	public static final String visit_doctor = "visit_doctor";

	public static Map<String, String> sixoldChildTable() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, table);
		map.put(id_card, "varchar20");
		map.put(serial_id, "varchar20");
		map.put(age, "varchar20");
		map.put(visit_date, "varchar20");
		map.put(weight, "varchar20");
		map.put(height, "varchar20");
		map.put(hear, "varchar20");
		map.put(tooth_num, "varchar20");
		map.put(decayed_t_num, "varchar20");
		map.put(heart_hear, "varchar20");
		map.put(abdomen_touch, "varchar20");
		map.put(HGB, "varchar20");
		map.put(growth_assess, "varchar20");
		map.put(seak_state, "varchar20");
		map.put(transfer_advise, "varchar20");
		map.put(transfer_reason, "varchar20");
		map.put(transfer_org, "varchar20");
		map.put(guide, "varchar20");
		map.put(TCM, "varchar20");
		map.put(next_date, "varchar20");
		map.put(visit_doctor, "varchar20");
		return map;
	}

	public static final Map<String, Integer> cloumIdmap = Collections
			.unmodifiableMap(new HashMap<String, Integer>() {

				private static final long serialVersionUID = -8896139986212203908L;
				{

					put(serial_id, R.id.sixold_serial_id);
					put(age, R.id.sixold_age);
					put(visit_date, R.id.sixold_visit_date);
					put(weight, R.id.sixold_weight);
					put(height, R.id.sixold_height);
					put(hear, R.id.sixold_hear);
					put(tooth_num, R.id.sixold_decayed_t_num);
					put(decayed_t_num, R.id.sixold_decayed_t_num);
					put(heart_hear, R.id.sixold_heart_hear);
					put(abdomen_touch, R.id.sixold_abdomen_touch);
					put(HGB, R.id.sixold_HGB);
					put(growth_assess, R.id.sixold_growth_assess);
					put(seak_state, R.id.sixold_seak_state);
					put(transfer_advise, R.id.sixold_transfer_advise);
					put(guide, R.id.sixold_guide);
					put(TCM, R.id.sixold_TCM);
					put(next_date, R.id.sixold_next_date);
					put(visit_doctor, R.id.sixold_visit_doctor);
				}

			});

}
