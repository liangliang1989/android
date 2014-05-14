package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class HealthCheckTable {
	//����
	public static final String TABLE_NAME = "tableName";
	//����
	public static final String NAME = "name";
	//�û����
	public static final String USER_ID = "userId";
	//������
	public static final String SERIAL_ID = "serialId";
	//�������
	public static final String CHECK_DATE = "checkDate";
	//����ҽ��
	public static final String DOCTOR_NAME = "doctorName";
	//֢״
	public static final String SYMTOM = "symtom";
	//����
	public static final String TEMPERATURE = "temperature";
	//����
	public static final String PULSE_RATE = "pulseRate";
	//����Ƶ��
	public static final String  BREATHING_RATE = "breathingRate";
	//���Ѫѹ
	public static final String LEFT_BLOOD_PRESSURE = "leftBloodPressure";
	//�Ҳ�Ѫѹ
	public static final String RIGHT_BLOOD_PRESSURE = "rightBloodPressure";
	//���
	public static final String HEIGHT = "height";
	//����
	public static final String WEIGHT = "weight";
	//��Χ
	public static final String WAISTLINE = "waistline";
	//����ָ��
	public static final String BMI = "BMI";
	//�����˽���״̬��������
	public static final String HEATH_SELFASSESSMENT = "healthState";
	//��������������������������
	public static final String ABILITY_OF_SELF_SELFASSESSMENT = "abilityOfSelf";
	//��������֪����
	public static final String COGNITIVE_ABILITY = "cognitiveAbility";
	//���������״̬
	public static final String EMOTIONAL_STATE = "emotionalStates";
	//����Ƶ��
	public static final String EXERCISE_FREQUENCY = "exerciseFrequency";
	//ÿ�ζ���ʱ��
	public static final String TIME_PER_EXERCISE = "timePerExercise";
	//��ֶ���ʱ��
	public static final String TIME_OF_KEEP_EXERCISING = "timeOfKeepExercise";
	//������ʽ
	public static final String EXERCISE_METHOD = "methodOfExercise";
	//��ʳϰ��
	public static final String EATING_HABITS = "eatingHabits";
	//����״��
	public static final String SMOKE_STATE = "smokeState";
	//��������
	public static final String SMOKE_PER_DAY = "smokeOfOneDay";
	//��ʼ��������
	public static final String AGE_OF_START_SMOKING = "ageOfStartSmoking";
	//��������
	public static final String AGE_OF_STOP_SMOKING = "ageOfStopSmoking";
	//����Ƶ��
	public static final String FREQUENCY_OF_DRINKING = "freOfDrinking";
	//��������
	public static final String DRINK_PER_DAY = "drinkPerDay";
	//�Ƿ���
	public static final String WHETHER_STOP_DRINKING = "whetherStopDrinking";
	//��ʼ��������
	public static final String AGE_OF_START_DRINKING = "ageOfStartDrinking";
	//��һ�����Ƿ�����
	public static final String WHETHER_DRINKING_RECENT = "whetherDrinkingRecently";
	//��������
	public static final String KINDS_OF_DRINKS = "kindsOfDrinks";
	//ְҵ��Σ�����ؽӴ�ʷ
	public static final String HARMFUL_FACTORS_IN_JOB = "harmfulFactorsInJob";
	//�ڴ�
	public static final String LIPS = "lips";
	//����
	public static final String TOOTH = "tooth";
	//�ʲ�
	public static final String PHARYNGEAL = "pharyngeal";
	//����ʵ��
	public static final String LEFT_EYESIGHT = "leftEyesight";
	//��������
	public static final String RIGHT_EYESIGHT = "rightEyesight";
	//���۽�������
	public static final String LEFT_CVA = "leftCVA";
	//���۽�������
	public static final String RIGHT_CVA = "rightCVA";
	//����
	public static final String AUDITION = "audition";
	//�˶�����
	public static final String ABILITY_OF_EXERCISE = "abilityOfExercise";
	//�۵�
	public static final String BOTTOM_OF_EYE = "bottomOfEye";
	//Ƥ��
	public static final String SKIN = "skin";
	//��Ĥ
	public static final String SCLERA = "sclera";
	//�ܰͽ�
	public static final String LYMPHONODUS = "lymphonodus";
	//Ͱ״��
	public static final String LUNGS_TONGHUANGXIONG = "tongzhuangxiong";
	//������
	public static final String LUNGS_BREATHE_SOUND = "huxiyin";
	//��  ��
	public static final String LUNGS_luoyin = "luoyin";
	//����
	public static final String HEART_RATE = "heartRate";
	//����
	public static final String ARRHYTHMIA = "arrhythmia";
	//����
	public static final String HEART_NOISE = "heartNoise";
	//ѹʹ
	public static final String ABDOMEN_YATONG = "abdomenYatong";
	//����
	public static final String ABDOMEN_BAOKUAI = "abdomenBaokuai";
	//�δ�
	public static final String ABDOMEN_GANDA = "abdomenGanda";
	//Ƣ��
	public static final String ABDOMEN_PIDA = "abdomenPida";
	//�ƶ�������
	public static final String ABDOMEN_ZHUOYIN = "abdomenZhuoyin";
	//��֫ˮ��
	public static final String EDEMA_LOWER_EXTREMITY = "edemaLowerExtremity";
	//�㱳��������
	public static final String DORSALIS_PEDIS_ARTERY_PULSE = "dorsalisPedisArteryPulse";
	//����ָ��
	public static final String DRE = "DRE";
	//����
	public static final String BREAST = "breast";
	//����
	public static final String VULVA = "vulva";
	//����
	public static final String VAGINA = "vagina";
	//����
	public static final String CERVICAL = "cervical";
	//����
	public static final String CORPUS = "corpus";
	//����
	public static final String ADNEXA = "adnexa";
	//����
	public static final String BODY_OTHERS = "bodyOthers";
	//Ѫ�쵰��
	public static final String HAEMOGLOBIN = "haemoglobin";
	//��ϸ��
	public static final String HEMAMEBA = "hemameba";
	//ѪС��
	public static final String PLATELET = "platelet";
	//����
	public static final String BLOOD_ROUTINE_OTHERS = "bloodRoutineOthers";
	//�򵰰�
	public static final String URINE_PROTEIN = "urineProtein";
	//����
	public static final String UGLU = "urineGucose";
	//��ͪ��
	public static final String URINE_ACETONE_BODY = "urineAcetoneBody";
	//��ǱѪ
	public static final String URINE_OCCULT_BLOOD = "urineOccultBlood";
	//����
	public static final String URINE_ROUTINE_OTHER = "urineRoutineOthers";
	//�ո�Ѫ��
	public static final String FBG = "FBG";
	//Ѫ�ǵ�λ
	public static final String BLOOD_SUGAR_UNIT = "bloodSugarUnit";
	//�ĵ�ͼ
	public static final String  ELECTROCARDIOGRAM = "electrocardiogram";
	//��΢���׵���
	public static final String MICROALBUMINURIA = "microalbuminuria";
	//���ǱѪ
	public static final String SED_OCCULT_BLOOD = "sedOccultBlood";
	//�ǻ�Ѫ�쵰��
	public static final String GLYCOSYLATED_HEMOGLOBIN = "glycosylatedHemoglobin";
	//���͸��ױ��濹ԭ
	public static final String HBsAg = "HBsAg";
	//Ѫ��ȱ�ת��ø
	public static final String SGPT = "SGPT";
	//Ѫ��Ȳ�ת��ø
	public static final String SERUM_GLU_OXAL_TRANS = "serGluOxalTrans";
	//�׵���
	public static final String LUNGS_ALBUMIN = "lungsAlbumin";
	//�ܵ�����
	public static final String LUNGS_TBil = "lungsTBil";
	//��ϵ�����
	public static final String LUNGS_CONJ_BILIRUBIN = "lungsConjBilirubin";
	//Ѫ�弡��
	public static final String SERUM_CREATININE = "serumCreatinine";
	//Ѫ���ص�
	public static final String BLOOD_UREA_NITROGEN = "bloodUreaNitrogen";
	//Ѫ��Ũ��
	public static final String POTASSIUM_CONCENTRATION = "potassiumConcentration";
	//Ѫ��Ũ��
	public static final String SERUM_SODIUM_CONCENTRATION = "serumSodiumConcentration";
	//�ܵ��̴�
	public static final String TOTAL_CHOLESTEROL = "totalCholesterol";
	//��������
	public static final String TRIGLYCERIDE = "triglyceride";
	//Ѫ����ܶ�֬���׵��̴�
	public static final String LDLC = "LDLC";
	//Ѫ����ܶ�֬���׵��̴�
	public static final String HDLC = "HDLC";
	//�ز�X��Ƭ
	public static final String CHEST_X_RAY = "chestX_ray";
	//B��
	public static final String B_ULTRASOUND = "BUltrasound";
	//����ͿƬ
	public static final String CERVICAL_SMEAR = "cervicalSmear";
	//���������֮����
	public static final String HELP_CHECK_OTHER = "helpCheckOther";
	//ƽ����
	public static final String MILD_PHYSICAL = "mildPhysical";
	//������
	public static final String FAINT_PHYSICAL = "faintPhysical";
	//������
	public static final String YANG_PHYSICAL = "yangPhysical";
	//������
	public static final String	YIN_PHYSICAL = "yinPhysical";
	//̵ʪ��
	public static final String PHLEGM_DAMPNESS_PHYSICAL = "phlegmDampnessPhysical";
	//ʪ����
	public static final String DAMPNESS_HEAT_PHYSICAL = "dampnessHeatPhysical";
	//Ѫ����
	public static final String BLOOD_STASIS_PHYSICAL = "bloodStasisPhysical";
	//������
	public static final String QI_YU_PHYSICAL = "qiYuPhysical";
	//�ر���
	public static final String GRASP_PHYSICAL = "graspPhysical";
	//��Ѫ�ܼ���
	public static final String CEREBROVASCULAR_DISEASES = "cerebrovascularDiseases";
	//���༲��
	public static final String KIDNEY_DISEASE = "kidneyDisease";
	//���༲��
	public static final String CARDIAC_DISEASE = "cardiacDisease";
	//Ѫ�ܼ���
	public static final String VASCULAR_DISEASE = "vascularDisease";
	//�۲�����
	public static final String OCULOPATHY = "oculopathy";
	//��ϵͳ����
	public static final String NERVE_SYSTEM_DISEASE = "nerveSystemDisease";
	//����ϵͳ����
	public static final String OTHER_SYSTEMS_DISEASE = "otherSystemsDisease";
	//��������
	public static final String HEALTH_EVALUATION = "healthEvaluation";
	//����ָ��
	public static final String HEALTH_GUIDANCE = "healthGuidance";
	//Σ�����ؿ���
	public static final String RISK_FACTORS_FOR_CONTROL = "riskFactorsControl";
	
	//��������
	public static Map<String, String> healthCheckTableDesc() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(TABLE_NAME, "HEALTHCHECK");
		map.put(USER_ID, "varchar(50)");
		map.put(SERIAL_ID, "varchar(50)");
		map.put(NAME, "varchar(50)");
		map.put(CHECK_DATE, "varchar(50)");
		map.put(DOCTOR_NAME, "varchar(50)");
		map.put(SYMTOM, "varchar(50)");
		map.put(TEMPERATURE, "varchar(50)");
		map.put(PULSE_RATE, "varchar(50)");
		map.put(BREATHING_RATE, "varchar(50)");
		map.put(LEFT_BLOOD_PRESSURE, "varchar(50)");
		map.put(RIGHT_BLOOD_PRESSURE, "varchar(50)");
		map.put(HEIGHT, "varchar(50)");
		map.put(WEIGHT, "varchar(50)");
		map.put(WAISTLINE, "varchar(50)");
		map.put(BMI, "varchar(50)");
		map.put(HEATH_SELFASSESSMENT, "varchar(50)");
		map.put(ABILITY_OF_SELF_SELFASSESSMENT, "varchar(50)");
		map.put(COGNITIVE_ABILITY, "varchar(50)");
		map.put(EMOTIONAL_STATE, "varchar(50)");
		map.put(EXERCISE_FREQUENCY, "varchar(50)");
		map.put(TIME_PER_EXERCISE, "varchar(50)");
		map.put(TIME_OF_KEEP_EXERCISING, "varchar(50)");
		map.put(EXERCISE_METHOD, "varchar(50)");
		map.put(EATING_HABITS, "varchar(50)");
		map.put(SMOKE_STATE, "varchar(50)");
		map.put(SMOKE_PER_DAY, "varchar(50)");
		map.put(AGE_OF_START_SMOKING, "varchar(50)");
		map.put(AGE_OF_STOP_SMOKING, "varchar(50)");
		map.put(FREQUENCY_OF_DRINKING, "varchar(50)");
		map.put(DRINK_PER_DAY, "varchar(50)");
		map.put(WHETHER_STOP_DRINKING, "varchar(50)");
		map.put(AGE_OF_START_DRINKING, "varchar(50)");
		map.put(WHETHER_DRINKING_RECENT, "varchar(50)");
		map.put(KINDS_OF_DRINKS, "varchar(50)");
		map.put(HARMFUL_FACTORS_IN_JOB, "varchar(50)");
		map.put(LEFT_EYESIGHT, "varchar(50)");
		map.put(RIGHT_EYESIGHT, "varchar(50)");
		map.put(LEFT_CVA, "varchar(50)");
		map.put(RIGHT_CVA, "varchar(50)");
		map.put(AUDITION, "varchar(50)");
		map.put(ABILITY_OF_EXERCISE, "varchar(50)");
		map.put(BOTTOM_OF_EYE, "varchar(50)");
		map.put(SKIN, "varchar(50)");
		map.put(SCLERA, "varchar(50)");
		map.put(LYMPHONODUS, "varchar(50)");
		map.put(LUNGS_TONGHUANGXIONG, "varchar(50)");
		map.put(LUNGS_BREATHE_SOUND, "varchar(50)");
		map.put(LUNGS_luoyin, "varchar(50)");
		map.put(HEART_RATE, "varchar(50)");
		map.put(ARRHYTHMIA, "varchar(50)");
		map.put(HEART_NOISE, "varchar(50)");
		map.put(ABDOMEN_YATONG, "varchar(50)");
		map.put(ABDOMEN_BAOKUAI, "varchar(50)");
		map.put(ABDOMEN_GANDA, "varchar(50)");
		map.put(ABDOMEN_PIDA, "varchar(50)");
		map.put(ABDOMEN_ZHUOYIN, "varchar(50)");
		map.put(EDEMA_LOWER_EXTREMITY, "varchar(50)");
		map.put(DORSALIS_PEDIS_ARTERY_PULSE, "varchar(50)");
		map.put(DRE, "varchar(50)");
		map.put(BREAST, "varchar(50)");
		map.put(VULVA, "varchar(50)");
		map.put(VAGINA, "varchar(50)");
		map.put(CERVICAL, "varchar(50)");
		map.put(CORPUS, "varchar(50)");
		map.put(ADNEXA, "varchar(50)");
		map.put(BODY_OTHERS, "varchar(50)");
		map.put(HAEMOGLOBIN, "varchar(50)");
		map.put(HEMAMEBA, "varchar(50)");
		map.put(PLATELET, "varchar(50)");
		map.put(BLOOD_ROUTINE_OTHERS, "varchar(50)");
		map.put(URINE_PROTEIN, "varchar(50)");
		map.put(UGLU, "varchar(50)");
		map.put(URINE_ACETONE_BODY, "varchar(50)");
		map.put(URINE_OCCULT_BLOOD, "varchar(50)");
		map.put(URINE_ROUTINE_OTHER, "varchar(50)");
		map.put(FBG, "varchar(50)");
		map.put(ELECTROCARDIOGRAM, "varchar(50)");
		map.put(MICROALBUMINURIA, "varchar(50)");
		map.put(SED_OCCULT_BLOOD, "varchar(50)");
		map.put(GLYCOSYLATED_HEMOGLOBIN, "varchar(50)");
		map.put(HBsAg, "varchar(50)");
		map.put(SGPT, "varchar(50)");
		map.put(SERUM_GLU_OXAL_TRANS, "varchar(50)");
		map.put(LUNGS_ALBUMIN, "varchar(50)");
		map.put(LUNGS_TBil, "varchar(50)");
		map.put(LUNGS_CONJ_BILIRUBIN, "varchar(50)");
		map.put(SERUM_CREATININE, "varchar(50)");
		map.put(BLOOD_UREA_NITROGEN, "varchar(50)");
		map.put(POTASSIUM_CONCENTRATION, "varchar(50)");
		map.put(SERUM_SODIUM_CONCENTRATION, "varchar(50)");
		map.put(TOTAL_CHOLESTEROL, "varchar(50)");
		map.put(TRIGLYCERIDE, "varchar(50)");
		map.put(LDLC, "varchar(50)");
		map.put(HDLC, "varchar(50)");
		map.put(CHEST_X_RAY, "varchar(50)");
		map.put(B_ULTRASOUND, "varchar(50)");
		map.put(CERVICAL_SMEAR, "varchar(50)");
		map.put(HELP_CHECK_OTHER, "varchar(50)");
		map.put(MILD_PHYSICAL, "varchar(50)");
		map.put(FAINT_PHYSICAL, "varchar(50)");
		map.put(YANG_PHYSICAL, "varchar(50)");
		map.put(YIN_PHYSICAL, "varchar(50)");
		map.put(PHLEGM_DAMPNESS_PHYSICAL, "varchar(50)");
		map.put(DAMPNESS_HEAT_PHYSICAL, "varchar(50)");
		map.put(BLOOD_STASIS_PHYSICAL, "varchar(50)");
		map.put(QI_YU_PHYSICAL, "varchar(50)");
		map.put(GRASP_PHYSICAL, "varchar(50)");
		map.put(CEREBROVASCULAR_DISEASES, "varchar(50)");
		map.put(KIDNEY_DISEASE, "varchar(50)");
		map.put(CARDIAC_DISEASE, "varchar(50)");
		map.put(VASCULAR_DISEASE, "varchar(50)");
		map.put(OCULOPATHY, "varchar(50)");
		map.put(NERVE_SYSTEM_DISEASE, "varchar(50)");
		map.put(OTHER_SYSTEMS_DISEASE, "varchar(50)");
		map.put(HEALTH_EVALUATION, "varchar(50)");
		map.put(HEALTH_GUIDANCE, "varchar(50)");
		map.put(RISK_FACTORS_FOR_CONTROL, "varchar(50)");
		map.put(LIPS, "varchar(50)");
		map.put(TOOTH, "varchar(50)");
		map.put(PHARYNGEAL, "varchar(50)");
		map.put(BLOOD_SUGAR_UNIT, "varchar(50)");
		
		return map;
	}
}
