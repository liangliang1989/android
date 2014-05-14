package com.health.archive.vaccinate;

import java.util.HashMap;
import java.util.Map;

import com.health.database.Tables;

public class VaccTables {
	// ���֤��
	public static final String id_card = "id_card";
	// ���ֿ���ͷ
	public static final String vacc_head_table = "Vacc_head";
	public static final String serial_id = "serial_id";
	public static final String guardian = "relation";
	public static final String relation = "relation";
	public static final String phone = "phone";
	public static final String addr = "addr";
	public static final String register_addr = "register_addr";
	public static final String in_time = "in_time";
	public static final String out_time = "out_time";
	public static final String out_reason = "out_reason";
	public static final String abnormal_history = "abnormal_history";
	public static final String vacc_taboo = "vacc_taboo";
	public static final String infection_history = "infection_history";
	public static final String add_date = "add_date";
	public static final String add_person = "add_person";

	// ���ֿ���¼��
	public static final String vacc_record_table = "Vacc_record";
	public static final String vacc_kind = "vacc_kind";
	public static final String vacc_time = "vacc_time";
	public static final String vacc_date = "vacc_date";
	public static final String vacc_part = "vacc_part";
	public static final String vacc_serial = "vacc_serial";
	public static final String vacc_doctor = "vacc_doctor";
	public static final String vacc_note = "vacc_note";
	public static String row_num = "row_num";

	

	/***
	 * ������ֿ���¼��Ϣ��
	 * 
	 * @return
	 */
	public static Map<String, String> vaccRecordTable() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, vacc_record_table);
		map.put(id_card, "char(18)");
		map.put(row_num, "integer");// �к�
		map.put(serial_id, "varchar(20)");// ���
		map.put(vacc_kind, "varchar(20)");// ����
		map.put(vacc_time, "integer");// ����
		map.put(vacc_date, "date");// ��������
		map.put(vacc_part, "varchar(20)");// ���ֲ�λ
		map.put(vacc_serial, "varchar(20)");// ��������
		map.put(vacc_doctor, "varchar(20)");// ����ҽ��
		map.put(vacc_note, "varchar(100)");// ��ע
		return map;
	}

	/***
	 * ������ֿ���ͷ��Ϣ
	 * 
	 * @return
	 */
	public static Map<String, String> vaccHeadTable() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, vacc_head_table);
		map.put(id_card, "char(18)");
		map.put(serial_id, "varchar(20)");// ���
		map.put(guardian, "varchar(20)");// �໤������
		map.put(relation, "varchar(20)");// ���ͯ��ϵ
		map.put(phone, "varchar(20)");// ��ϵ�绰
		map.put(addr, "varchar(100)");// ��ͥ��סַ
		map.put(register_addr, "varchar(100)");// ������ַ
		map.put(in_time, "date");// Ǩ��ʱ��
		map.put(out_time, "date");// Ǩ��ʱ��
		map.put(out_reason, "varchar(100)");// Ǩ��ԭ��
		map.put(abnormal_history, "varchar(100)");// �����쳣��Ӧʷ
		map.put(vacc_taboo, "varchar(100)");// ���ֽ���
		map.put(infection_history, "varchar(100)");// ��Ⱦ��ʷ
		map.put(add_date, "date");// ��������
		map.put(add_person, "varchar(20)");// ������
		return map;
	}

}
