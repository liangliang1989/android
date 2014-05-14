package com.health.database;

import java.util.HashMap;
import java.util.Map;

import com.health.archive.baby.BabyTable;
import com.health.archive.baby.TwoOldChildTable;
import com.health.archive.baby.oneold.OneOldChildTable;
import com.health.archive.vaccinate.VaccTables;
import com.health.chronicdisease.GlycuresisTable;
import com.health.chronicdisease.HypertensionTable;
import com.health.oldpeople.OldPeopleChineseMedicineTable;
import com.health.web.WebService;

public class Tables {

	// ����
	public static final String TABLE_NAME = "tableName";

	public static final String USER_ID = "user_id";
	public static final String NAME = "name";
	public static final String SERIAL_ID = "serial_id";

	// ÿ�����е��ĸ�����
	public static final String CARDNO = "cardNo";
	public static final String TIME = "checkTime";
	public static final String DEVICENAME = "deviceName";
	public static final String DEVICEMAC = "deviceMac";
	// pc300 5��
	public static final String PULSE = "pulse";
	public static final String TEMP = "temp";
	public static final String DBP = "dbp";
	public static final String SBP = "sbp";
	public static final String BO = "so";
	// �ٽ� 3��
	public static final String GLU = "glu";
	public static final String UA = "ua";
	public static final String CHOL = "chol";
	// ��ϸ��
	public static final String WBC = "wbc";
	// ��Һ11��
	public static final String LEU = "leu";
	public static final String BLD = "bld";
	public static final String PH = "ph";
	public static final String PRO = "pro";
	public static final String UBG = "ubg";
	public static final String NIT = "nit";
	public static final String SG = "sg";
	public static final String KET = "ket";
	public static final String BIL = "bil";
	public static final String UGLU = "glu";
	public static final String VC = "vc";

	public static final String ECG = "ecg";
	public static final String ECG_KRL = "ecgKrl";
	public static final String ECG_SRC = "ecgSrc";
	public static final String ECG_PDF = "ecgPdf";

	// ������������������������
	public static final String MEAL = "meal";
	public static final String WASH = "wash";
	public static final String DRESS = "dress";
	public static final String TOILET = "toilet";
	public static final String EXERCISE = "exercise";
	public static final String TOTAL_ESTIMATE = "total_estimate";

	// ���Ծ��񼲲����߸�����Ϣ�����
	public static final String GUARDIAN_NAME = "guardian_name";
	public static final String RELATION_WITH_GUARDIAN = "relation_with_guardian";
	public static final String GUARDIAN_ADDRESS = "guardian_address";
	public static final String GUARDIAN_TELEPHONE = "guardian_telephone";
	public static final String VILLAGE_COMMITTEE_LINKMAN = "village_committee_linkman";
	public static final String VILLAGE_COMMITTEE_TELEPHONE = "village_committee_telephone";
	public static final String INFORMED_CONSENT = "informed_consent";
	public static final String INFORMED_CONSENT_SIGNATURE = "informed_consent_signature";
	public static final String INFORMED_CONSENT_SIGNATURE_DATE = "informed_consent_signature_date";
	public static final String INITIAL_ONSET_DATE = "initial_onset_date";
	public static final String SYMPTOMS_OF_PAST = "symptoms_of_past";
	public static final String OUTPATIENT = "outpatient";
	public static final String INITIAL_DRUG_TREATMENT_DATE = "initial_drug_treatment_date";
	public static final String HOSPITAL_NUM = "hospital_num";
	public static final String CURRENT_DIAGNOSIS_RESULT = "current_diagnosis_result";
	public static final String CURRENT_DIAGNOSIS_HOSPITAL = "current_diagnosis_hospital";
	public static final String CURRENT_DIAGNOSIS_DATE = "current_diagnosis_date";
	public static final String LAST_TREATMENT = "last_treatment";
	public static final String INFO_MILD_DISTURBANCES_NUM = "info_mild_disturbances_num";
	public static final String INFO_MAKE_TROUBLE_NUM = "info_make_trouble_num";
	public static final String INFO_MAKE_DISASTER_NUM = "info_make_disaster_num";
	public static final String INFO_HURT_HIMSELF_NUM = "info_hurt_himself_num";
	public static final String INFO_ATTEMPTED_SUICIDE_NUM = "info_attempted_suicide_num";
	public static final String INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY = "info_no_impact_on_family_and_society";
	public static final String INFO_SHUT_CASE = "info_shut_case";
	public static final String ECONOMIC_CONDITION = "economic_condition";
	public static final String SPECIALIST_ADVICE = "specialist_advice";
	public static final String INFO_FILL_DATE = "info_fill_date";
	public static final String INFO_DOCTOR_SIGNATURE = "info_doctor_signature";

	// ���Ծ��񼲲�������÷����¼��
	public static final String FOLLOWUP_DATE = "followup_date";
	public static final String RISK = "risk";
	public static final String CURRENT_SYMPTOM = "current_symptom";
	public static final String INSIGHT = "insight";
	public static final String SLEEP_SITUATION = "sleep_situation";
	public static final String DIET_SITUATION = "diet_situation";
	public static final String SELF_CARE = "self_care";
	public static final String HOURSE_WORK = "hourse_wrok";
	public static final String PRODUCTIVE_WORK = "productive_work";
	public static final String LEARNING_ABILITY = "learning_ability";
	public static final String INTERPERSONAL = "interpersonal";
	public static final String FOLLOWUP_MILD_DISTURBANCES_NUM = "followup_mild_disturbances_num";
	public static final String FOLLOWUP_MAKE_TROUBLE_NUM = "followup_make_trouble_num";
	public static final String FOLLOWUP_MAKE_DISASTER_NUM = "followup_make_disaster_num";
	public static final String FOLLOWUP_HURT_HIMSELF_NUM = "followup_hurt_himself_num";
	public static final String FOLLOWUP_ATTEMPTED_SUICIDE_NUM = "followup_attempted_suicide_num";
	public static final String FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY = "followup_no_impact_on_family_and_society";
	public static final String FOLLOWUP_SHUT_CASE = "followup_shut_case";
	public static final String FOLLOWUP_HOSPITALIZATION = "followup_hospitalization";
	public static final String LAST_DATE_TO_DISCHARGE = "last_date_to_discharge";
	public static final String LAB_TEST = "lab_test";
	public static final String MEDICATION_COMPLIANCE = "medication_compliance";
	public static final String DRUG_ADVERSE_REACTION = "drug_adverse_reaction";
	public static final String TREATMENT_EFFECT = "treatment_effect";
	public static final String WHETHER_TO_REFERRAL = "whether_to_referral";
	public static final String REFERRAL_REASON = "referral_reason";
	public static final String WHERE_TO_REFERRAL = "where_to_referral";
	public static final String DRUG_1 = "drug_1";
	public static final String DRUG_USAGE_1 = "drug_usage_1";
	public static final String DOSE_1 = "dose_1";
	public static final String DRUG_2 = "drug_2";
	public static final String DRUG_USAGE_2 = "drug_usage_2";
	public static final String DOSE_2 = "dose_2";
	public static final String DRUG_0 = "drug_0";
	public static final String DRUG_USAGE_0 = "drug_usage_0";
	public static final String DOSE_0 = "dose_0";
	public static final String REHABILITATION_MEASURE = "rehabilitation_measure";
	public static final String THIS_FOLLOWUP_CLASSIFICATION = "this_followup_classification";
	public static final String NEXT_FOLLOWUP_DATE = "next_followup_date";
	public static final String FOLLOWUP_DOCTOR_SIGNATURE = "followup_doctor_signature";

	/**
	 * һ�ѱ���
	 */
	public static final String TABLE_PULSE = "PULSE";
	public static final String TABLE_TEMP = "TEMP";
	public static final String TABLE_BP = "BP";
	public static final String TABLE_BO = "BO";
	public static final String TABLE_GLU = "GLU";
	public static final String TABLE_UA = "UA";
	public static final String TABLE_CHOL = "CHOL";
	public static final String TABLE_URINE = "URINE";
	public static final String TABLE_WBC = "WBC";
	public static final String TABLE_ECG = "ECG";

	// ����������Դ,�豸������д����
	public static final String FROM_TYPE = "FROM_TYPE";
	public static final String FROM_DEVICE = "1";
	public static final String FROM_INPUT = "0";
	// ÿ�����ݶ������ϴ��ļ�¼,�Ա��´��ϴ�
	public static final String UPLOAD_PARA = "UPLOAD_PARA";

	public Map<String, String> pulseTable() {
		Map<String, String> pulseMap = defaultAttrs();
		pulseMap.put(TABLE_NAME, TABLE_PULSE);
		pulseMap.put(PULSE, "integer");// ����

		return pulseMap;
	}

	public Map<String, String> tempTable() {
		Map<String, String> tempMap = defaultAttrs();
		tempMap.put(TABLE_NAME, TABLE_TEMP);
		tempMap.put(TEMP, "integer");// ����
		return tempMap;
	}

	public Map<String, String> bpTable() {
		Map<String, String> bpMap = defaultAttrs();
		bpMap.put(TABLE_NAME, TABLE_BP);
		bpMap.put(DBP, "integer");// ����ѹ
		bpMap.put(SBP, "integer");// ����ѹ
		bpMap.put(PULSE, "integer");// ����
		return bpMap;
	}

	public Map<String, String> boTable() {
		Map<String, String> boMap = defaultAttrs();
		boMap.put(TABLE_NAME, TABLE_BO);
		boMap.put(BO, "integer");// Ѫ��
		return boMap;
	}

	public Map<String, String> gluTable() {
		Map<String, String> gluMap = defaultAttrs();
		gluMap.put(TABLE_NAME, TABLE_GLU);
		gluMap.put(GLU, "integer");// Ѫ��
		return gluMap;
	}

	public Map<String, String> uaTable() {
		Map<String, String> uaMap = defaultAttrs();
		uaMap.put(TABLE_NAME, TABLE_UA);
		uaMap.put(UA, "integer");// ����
		return uaMap;
	}

	public Map<String, String> cholTable() {
		Map<String, String> cholMap = defaultAttrs();
		cholMap.put(TABLE_NAME, TABLE_CHOL);
		cholMap.put(CHOL, "integer");// �ܵ��̴�
		return cholMap;
	}

	/**
	 * ��Һ11��
	 * 
	 * @return
	 */
	public Map<String, String> urineTable() {
		Map<String, String> urineMap = defaultAttrs();
		urineMap.put(TABLE_NAME, TABLE_URINE);
		urineMap.put(LEU, "integer");//
		urineMap.put(BLD, "integer");//
		urineMap.put(PH, "integer");//
		urineMap.put(PRO, "integer");//
		urineMap.put(UBG, "integer");//
		urineMap.put(NIT, "integer");//
		urineMap.put(SG, "integer");//
		urineMap.put(KET, "integer");//
		urineMap.put(BIL, "integer");//
		urineMap.put(GLU, "integer");//
		urineMap.put(VC, "integer");//
		return urineMap;
	}

	public Map<String, String> wbcTable() {
		Map<String, String> cholMap = defaultAttrs();
		cholMap.put(TABLE_NAME, TABLE_WBC);
		cholMap.put(WBC, "integer");// �ܵ��̴�
		return cholMap;
	}

	/**
	 * ÿ�����е�����
	 * 
	 * @return
	 */

	public Map<String, String> defaultAttrs() {
		Map<String, String> defaultMap = new HashMap<String, String>();
		defaultMap.put(CARDNO, "varchar(20)");// �˺�
		// 2013-08-08 16:32:05
		defaultMap.put(TIME, "varchar(25)");
		defaultMap.put(DEVICENAME, "varchar(20)");// ������Դ�豸����
		defaultMap.put(DEVICEMAC, "varchar(20)");// �豸����mac��ַ
		defaultMap.put(WebService.STATUS_CODE, "integer");// �ϴ�״̬
		defaultMap.put(FROM_TYPE, "integer");// ������Դ
		defaultMap.put(UPLOAD_PARA, "varchar(2100)");// �ϴ�������jsonobject
		return defaultMap;
	}

	/**
	 * �ĵ�
	 * 
	 * @return
	 */
	public Map<String, String> ecgTable() {
		Map<String, String> ecgMap = defaultAttrs();
		ecgMap.put(TABLE_NAME, TABLE_ECG);
		ecgMap.put(ECG, "varchar(2000)");// ��������ַ�����ʽ����
											// [1,1,2,...]
		return ecgMap;
	}

	public Map<String, String> oldPeopleSelfCareEstimateTable() {
		Map<String, String> oldPeopleMap = new HashMap<String, String>();
		oldPeopleMap.put(TABLE_NAME, "oldPeopleSelfCareEstimate");
		oldPeopleMap.put(USER_ID, "char(18)");
		oldPeopleMap.put(MEAL, "varchar(255)");
		oldPeopleMap.put(WASH, "varchar(255)");
		oldPeopleMap.put(DRESS, "varchar(255)");
		oldPeopleMap.put(TOILET, "varchar(255)");
		oldPeopleMap.put(EXERCISE, "varchar(255)");
		oldPeopleMap.put(TOTAL_ESTIMATE, "varchar(255)");
		return oldPeopleMap;
	}

	/**
	 * ���Ծ��񼲲����߸�����Ϣ�����
	 * 
	 * @return
	 */
	public Map<String, String> infoSupplementaryOfSevereMentalIllness() {
		Map<String, String> infoSuppMap = new HashMap<String, String>();
		infoSuppMap.put(TABLE_NAME, "infoSuppOfSevereMentalIllness");
		infoSuppMap.put(USER_ID, "char(18)");
		infoSuppMap.put(NAME, "varchar(8)");
		infoSuppMap.put(SERIAL_ID, "char(20) UNIQUE");
		infoSuppMap.put(GUARDIAN_NAME, "varchar(8)");
		infoSuppMap.put(RELATION_WITH_GUARDIAN, "varchar(8)");
		infoSuppMap.put(GUARDIAN_ADDRESS, "varchar(255)");
		infoSuppMap.put(GUARDIAN_TELEPHONE, "varchar(20)");
		infoSuppMap.put(VILLAGE_COMMITTEE_LINKMAN, "varchar(8)");
		infoSuppMap.put(VILLAGE_COMMITTEE_TELEPHONE, "varchar(20)");
		infoSuppMap.put(INFORMED_CONSENT, "varchar(16)");
		infoSuppMap.put(INFORMED_CONSENT_SIGNATURE, "varchar(8)");
		infoSuppMap.put(INFORMED_CONSENT_SIGNATURE_DATE, "varchar(15)");
		infoSuppMap.put(INITIAL_ONSET_DATE, "varchar(15)");
		infoSuppMap.put(SYMPTOMS_OF_PAST, "varchar(255)");
		infoSuppMap.put(OUTPATIENT, "varchar(20)");
		infoSuppMap.put(INITIAL_DRUG_TREATMENT_DATE, "varchar(15)");
		infoSuppMap.put(HOSPITAL_NUM, "varchar(3)");
		infoSuppMap.put(CURRENT_DIAGNOSIS_RESULT, "varchar(255)");
		infoSuppMap.put(CURRENT_DIAGNOSIS_HOSPITAL, "varchar(255)");
		infoSuppMap.put(CURRENT_DIAGNOSIS_DATE, "varchar(15)");
		infoSuppMap.put(LAST_TREATMENT, "varchar(10)");
		infoSuppMap.put(INFO_MILD_DISTURBANCES_NUM, "varchar(3)");
		infoSuppMap.put(INFO_MAKE_TROUBLE_NUM, "varchar(3)");
		infoSuppMap.put(INFO_MAKE_DISASTER_NUM, "varchar(3)");
		infoSuppMap.put(INFO_HURT_HIMSELF_NUM, "varchar(3)");
		infoSuppMap.put(INFO_ATTEMPTED_SUICIDE_NUM, "varchar(3)");
		infoSuppMap.put(INFO_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "varchar(1)");
		infoSuppMap.put(INFO_SHUT_CASE, "varchar(20)");
		infoSuppMap.put(ECONOMIC_CONDITION, "varchar(30)");
		infoSuppMap.put(SPECIALIST_ADVICE, "varchar(255)");
		infoSuppMap.put(INFO_FILL_DATE, "varchar(15)");
		infoSuppMap.put(INFO_DOCTOR_SIGNATURE, "varcahr(8)");
		return infoSuppMap;
	}

	public Map<String, String> followupRecordOfSevereMentalIllness() {
		Map<String, String> follRecoMap = new HashMap<String, String>();
		follRecoMap.put(TABLE_NAME, "followupRecordOfSevereMentalIllness");
		follRecoMap.put(USER_ID, "char(18)");
		follRecoMap.put(NAME, "varchar(8)");
		follRecoMap.put(SERIAL_ID, "char(20) UNIQUE");
		follRecoMap.put(FOLLOWUP_DATE, "varchar(15)");
		follRecoMap.put(RISK, "char(1)");
		follRecoMap.put(CURRENT_SYMPTOM, "varchar(255)");
		follRecoMap.put(INSIGHT, "varchar(15)");
		follRecoMap.put(SLEEP_SITUATION, "varchar(10)");
		follRecoMap.put(DIET_SITUATION, "varchar(10)");
		follRecoMap.put(SELF_CARE, "varchar(10)");
		follRecoMap.put(HOURSE_WORK, "varchar(10)");
		follRecoMap.put(PRODUCTIVE_WORK, "varchar(12)");
		follRecoMap.put(LEARNING_ABILITY, "varchar(10)");
		follRecoMap.put(INTERPERSONAL, "varchar(10)");
		follRecoMap.put(FOLLOWUP_MILD_DISTURBANCES_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_MAKE_TROUBLE_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_MAKE_DISASTER_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_HURT_HIMSELF_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_ATTEMPTED_SUICIDE_NUM, "varchar(3)");
		follRecoMap.put(FOLLOWUP_NO_IMPACT_ON_FAMILY_AND_SOCIETY, "char(1)");
		follRecoMap.put(FOLLOWUP_SHUT_CASE, "varchar(15)");
		follRecoMap.put(FOLLOWUP_HOSPITALIZATION, "varchar(30)");
		follRecoMap.put(LAST_DATE_TO_DISCHARGE, "varchar(20)");
		follRecoMap.put(LAB_TEST, "varchar(255)");
		follRecoMap.put(MEDICATION_COMPLIANCE, "varchar(10)");
		follRecoMap.put(DRUG_ADVERSE_REACTION, "varchar(255)");
		follRecoMap.put(TREATMENT_EFFECT, "varchar(10)");
		follRecoMap.put(WHETHER_TO_REFERRAL, "varchar(5)");
		follRecoMap.put(REFERRAL_REASON, "varchar(255)");
		follRecoMap.put(WHERE_TO_REFERRAL, "varchar(255)");
		follRecoMap.put(DRUG_1, "varchar(255)");
		follRecoMap.put(DRUG_USAGE_1, "varchar(255)");
		follRecoMap.put(DOSE_1, "varchar(255)");
		follRecoMap.put(DRUG_2, "varchar(255)");
		follRecoMap.put(DRUG_USAGE_2, "varchar(255)");
		follRecoMap.put(DOSE_2, "varchar(255)");
		follRecoMap.put(DRUG_0, "varchar(255)");
		follRecoMap.put(DRUG_USAGE_0, "varchar(255)");
		follRecoMap.put(DOSE_0, "varchar(255)");
		follRecoMap.put(REHABILITATION_MEASURE, "varchar(255)");
		follRecoMap.put(THIS_FOLLOWUP_CLASSIFICATION, "varchar(15)");
		follRecoMap.put(NEXT_FOLLOWUP_DATE, "varchar(20)");
		follRecoMap.put(FOLLOWUP_DOCTOR_SIGNATURE, "varchar(8)");
		return follRecoMap;
	}

	/***
	 * ������ֿ���¼��Ϣ��
	 * 
	 * @return
	 */
	public Map<String, String> vaccRecordTable() {
		return VaccTables.vaccRecordTable();
	}

	/***
	 * ������ֿ���ͷ��Ϣ
	 * 
	 * @return
	 */
	public Map<String, String> vaccHeadTable() {
		return VaccTables.vaccHeadTable();
	}

	/***
	 * ��������ͥ��ü�¼��
	 * 
	 * @return
	 */
	public Map<String, String> babyVisitTable() {
		return BabyTable.babyVisitTable();
	}

	public Map<String, String> hypertensionTable() {
		return HypertensionTable.HypertensionTable();
	}

	public Map<String, String> glycuresisTable() {
		return GlycuresisTable.GlycuresisTable();
	}

	public Map<String, String> oldpeoplechinesemedicalTable() {
		return OldPeopleChineseMedicineTable.OldPeopleChineseMedicineTable();
	}

	/***
	 * һ��һ�¶�ͯ���ʱ�
	 * 
	 * @return
	 */
	public Map<String, String> oneOldChildTable() {
		return OneOldChildTable.oneOldChildTable();
	}

	/***
	 * һ�������ͯ���ʱ�
	 * 
	 * @return
	 */
	public Map<String, String> twoOldChildTable() {
		return TwoOldChildTable.twoOldChildTable();
	}

	public static String getSerialId() {
		return "400800-1878";
	}
}
