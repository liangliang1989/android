package com.health.archive.baby;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

/***
 * 一到两岁的小屁孩
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-4-22 下午3:02:19
 */
public class TwoOldChildTable {
	public static final String table = "twoold_table";
	public static final String id_card = "id_card";
	public static final String serial_id = "serial_id";
	public static final String age = "age";
	public static final String visit_date = "visit_date";
	public static final String weight = "weight";
	public static final String height = "height";
	public static final String face = "face";
	public static final String skin = "skin";
	public static final String bregma_size_a = "bregma_size_a";
	public static final String bregma_size_b = "bregma_size_b";
	public static final String bregma_state = "bregma_state";
	public static final String eye = "eye";
	public static final String ear = "ear";
	public static final String hear = "hear";
	public static final String tooth_num = "tooth_num";
	public static final String decayed_t_num = "decayed_t_num";
	public static final String heart_hear = "heart_hear";
	public static final String abdomen_touch = "abdomen_touch";
	public static final String limbs = "limbs";
	public static final String rickets_feature = "rickets_feature";
	public static final String HGB = "HGB";
	public static final String v_d = "v_d";
	public static final String growth_assess = "growth_assess";
	public static final String seak_state = "seak_state";
	public static final String transfer_advise = "transfer_advise";
	public static final String transfer_reason = "transfer_reason";
	public static final String transfer_org = "transfer_org";
	public static final String guide = "guide";
	public static final String TCM = "TCM";
	public static final String next_date = "next_date";
	public static final String visit_doctor = "visit_doctor";

	public static Map<String, String> twoOldChildTable() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, table);
		map.put(id_card, "varchar20");
		map.put(serial_id, "varchar20");
		map.put(age, "varchar20");
		map.put(visit_date, "varchar20");
		map.put(weight, "varchar20");
		map.put(height, "varchar20");
		map.put(face, "varchar20");
		map.put(skin, "varchar20");
		map.put(bregma_size_a, "varchar20");
		map.put(bregma_size_b, "varchar20");
		map.put(bregma_state, "varchar20");
		map.put(eye, "varchar20");
		map.put(ear, "varchar20");
		map.put(hear, "varchar20");
		map.put(tooth_num, "varchar20");
		map.put(decayed_t_num, "varchar20");
		map.put(heart_hear, "varchar20");
		map.put(abdomen_touch, "varchar20");
		map.put(limbs, "varchar20");
		map.put(rickets_feature, "varchar20");
		map.put(HGB, "varchar20");
		map.put(v_d, "varchar20");
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

					put(serial_id, R.id.twoold_serial_id);
					put(age, R.id.twoold_age);
					put(visit_date, R.id.twoold_visit_date);
					put(weight, R.id.twoold_weight);
					put(height, R.id.twoold_height);
					put(face, R.id.twoold_face);
					put(skin, R.id.twoold_skin);
					put(bregma_size_a, R.id.twoold_bregma_size_a);
					put(bregma_size_b, R.id.twoold_bregma_size_b);
					put(bregma_state, R.id.twoold_bregma_state);
					put(eye, R.id.twoold_eye);
					put(ear, R.id.twoold_ear);
					put(hear, R.id.twoold_hear);
					put(tooth_num, R.id.twoold_decayed_t_num);
					put(decayed_t_num, R.id.twoold_decayed_t_num);
					put(heart_hear, R.id.twoold_heart_hear);
					put(abdomen_touch, R.id.twoold_abdomen_touch);
					put(limbs, R.id.twoold_limbs);
					put(rickets_feature, R.id.twoold_rickets_feature);
					put(HGB, R.id.twoold_HGB);
					put(v_d, R.id.twoold_v_d);
					put(growth_assess, R.id.twoold_growth_assess);
					put(seak_state, R.id.twoold_seak_state);
					put(transfer_advise, R.id.twoold_transfer_advise);
					put(guide, R.id.twoold_guide);
					put(TCM, R.id.twoold_TCM);
					put(next_date, R.id.twoold_next_date);
					put(visit_doctor, R.id.twoold_visit_doctor);
				}

			});

}
