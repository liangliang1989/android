package com.health.database;

import java.util.HashMap;
import java.util.Map;

/**
 * ���˻�����Ϣ��
 * @author liangliang
 *
 */
public class PersonalBasicInforTable {
	//�����ؼ���
	public static final String TABLE_NAME = "tableName";
	//���
	public static final String USER_ID = "userId";
	//����
	public static final String NAME = "name";
	//�Ա�
	public static final String GENDER = "gender";
	//��������
	public static final String BIRTH = "birth";
	//���֤��
	public static final String ID_NUMBER = "idNumber";
	//������λ
	public static final String COMPANY = "company";
	//���˵绰
	public static final String SELF_PHONE = "selfPhone";
	//��ϵ������
	public static final String CONTACT_NAME = "contactName";
	//��ϵ�˵绰
	public static final String CONTACT_PHONE = "contactPhone";
	//��ס����
	public static final String PERMANENT_TYPE = "permanentType";
	//����
	public static final String NATION = "nation";
	//Ѫ��
	public static final String BLOOD_TYPE = "bloodType";
	//RH����
	public static final String RH_TYPE = "RHType";
	//�Ļ��̶�
	public static final String EDUCATION_DEGREE = "educationDegree";
	//ְҵ
	public static final String JOB = "job";
	//����״��
	public static final String MARITALSTATUS = "maritalStatus";
	//ҽ�Ʒ���֧����ʽ
	public static final String METHOD_OF_PAYMENT = "methodOfPayment";
	//ҩ�����ʷ
	public static final String ALLERGIC_HISTORY = "allergicHistory";
	//��¶ʷ
	public static final String EXPOSE_HISTORY = "exposeHistory";
	//����
	public static final String ILLNESS = "illness";
	//����
	public static final String OPERATION = "operation";
	//����
	public static final String TRAUMA = "trauma";
	//��Ѫ
	public static final String TRANSFUSION = "transfusion";
	//����ʷ ����
	public static final String FATHER_HISTORY = "fatherHistory";
	//����ʷ ĸ��
	public static final String MOTHER_HISTORY = "motherHistory";
	//����ʷ �ֵܽ���
	public static final String BROTHERS_SISTERS_HISTORY = "brothersSistersHistory";
	//����ʷ ��Ů
	public static final String CHILDREN_HISTORY = "childrenHistory";
	//�Ŵ���
	public static final String GENOPATHY = "genopathy";
	//�м����
	public static final String DEFORMITY_STATE = "deformityState";
	//�����ŷ���ʩ
	public static final String AIR_EXHAUST = "airExhaust";
	//ȼ������
	public static final String FULE_TYPE = "fuleType";
	//��ˮ
	public static final String WATER_TYPE = "waterType";
	//����
	public static final String TOILET_TYPE = "toiletType";
	//������
	public static final String ANIMAL_HOME = "animalHome";
	
	public static Map<String, String> archiveBasicInfor() {
		Map<String, String> basicInforMap = new HashMap<String, String> ();
		basicInforMap.put(TABLE_NAME, "BASICINFOR");
		basicInforMap.put(USER_ID, "varchar(50)");
		basicInforMap.put(NAME, "varchar(50)");
		basicInforMap.put(GENDER, "varchar(50)");
		basicInforMap.put(BIRTH, "varchar(50)");
		basicInforMap.put(ID_NUMBER, "varchar(50)");
		basicInforMap.put(COMPANY, "varchar(50)");
		basicInforMap.put(SELF_PHONE, "varchar(50)");
		basicInforMap.put(CONTACT_NAME, "varchar(50)");
		basicInforMap.put(CONTACT_PHONE, "varchar(50)");
		basicInforMap.put(PERMANENT_TYPE, "varchar(50)");
		basicInforMap.put(NATION, "varchar(50)");
		basicInforMap.put(BLOOD_TYPE, "varchar(50)");
		basicInforMap.put(RH_TYPE, "varchar(50)");
		basicInforMap.put(EDUCATION_DEGREE, "varchar(50)");
		basicInforMap.put(JOB, "varchar(50)");
		basicInforMap.put(MARITALSTATUS, "varchar(50)");
		basicInforMap.put(METHOD_OF_PAYMENT, "varchar(50)");
		basicInforMap.put(ALLERGIC_HISTORY, "varchar(50)");
		basicInforMap.put(EXPOSE_HISTORY, "varchar(50)");
		basicInforMap.put(ILLNESS, "varchar(50)");
		basicInforMap.put(OPERATION, "varchar(50)");
		basicInforMap.put(TRAUMA, "varchar(50)");
		basicInforMap.put(TRANSFUSION, "varchar(50)");
		basicInforMap.put(FATHER_HISTORY, "varchar(50)");
		basicInforMap.put(MOTHER_HISTORY, "varchar(50)");
		basicInforMap.put(BROTHERS_SISTERS_HISTORY, "varchar(50)");
		basicInforMap.put(CHILDREN_HISTORY, "varchar(50)");
		basicInforMap.put(GENOPATHY, "varchar(50)");
		basicInforMap.put(DEFORMITY_STATE, "varchar(50)");
		basicInforMap.put(AIR_EXHAUST, "varchar(50)");
		basicInforMap.put(FULE_TYPE, "varchar(50)");
		basicInforMap.put(WATER_TYPE, "varchar(50)");
		basicInforMap.put(TOILET_TYPE, "varchar(50)");
		basicInforMap.put(ANIMAL_HOME, "varchar(50)");
		
		return basicInforMap;
	}
}
