package com.health.database;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人基本信息表
 * @author liangliang
 *
 */
public class PersonalBasicInforTable {
	//表名关键字
	public static final String TABLE_NAME = "tableName";
	//编号
	public static final String USER_ID = "userId";
	//姓名
	public static final String NAME = "name";
	//性别
	public static final String GENDER = "gender";
	//出生日期
	public static final String BIRTH = "birth";
	//身份证号
	public static final String ID_NUMBER = "idNumber";
	//工作单位
	public static final String COMPANY = "company";
	//本人电话
	public static final String SELF_PHONE = "selfPhone";
	//联系人姓名
	public static final String CONTACT_NAME = "contactName";
	//联系人电话
	public static final String CONTACT_PHONE = "contactPhone";
	//常住类型
	public static final String PERMANENT_TYPE = "permanentType";
	//民族
	public static final String NATION = "nation";
	//血型
	public static final String BLOOD_TYPE = "bloodType";
	//RH阴性
	public static final String RH_TYPE = "RHType";
	//文化程度
	public static final String EDUCATION_DEGREE = "educationDegree";
	//职业
	public static final String JOB = "job";
	//婚姻状况
	public static final String MARITALSTATUS = "maritalStatus";
	//医疗费用支付方式
	public static final String METHOD_OF_PAYMENT = "methodOfPayment";
	//药物过敏史
	public static final String ALLERGIC_HISTORY = "allergicHistory";
	//暴露史
	public static final String EXPOSE_HISTORY = "exposeHistory";
	//疾病
	public static final String ILLNESS = "illness";
	//手术
	public static final String OPERATION = "operation";
	//外伤
	public static final String TRAUMA = "trauma";
	//输血
	public static final String TRANSFUSION = "transfusion";
	//家族史 父亲
	public static final String FATHER_HISTORY = "fatherHistory";
	//家族史 母亲
	public static final String MOTHER_HISTORY = "motherHistory";
	//家族史 兄弟姐妹
	public static final String BROTHERS_SISTERS_HISTORY = "brothersSistersHistory";
	//家族史 子女
	public static final String CHILDREN_HISTORY = "childrenHistory";
	//遗传病
	public static final String GENOPATHY = "genopathy";
	//残疾情况
	public static final String DEFORMITY_STATE = "deformityState";
	//厨房排风设施
	public static final String AIR_EXHAUST = "airExhaust";
	//燃料类型
	public static final String FULE_TYPE = "fuleType";
	//饮水
	public static final String WATER_TYPE = "waterType";
	//厕所
	public static final String TOILET_TYPE = "toiletType";
	//禽畜栏
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
