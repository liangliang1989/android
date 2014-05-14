package com.health.database;

import java.util.HashMap;
import java.util.Map;

public class HealthCheckTable {
	//表名
	public static final String TABLE_NAME = "tableName";
	//姓名
	public static final String NAME = "name";
	//用户编号
	public static final String USER_ID = "userId";
	//体检表编号
	public static final String SERIAL_ID = "serialId";
	//体检日期
	public static final String CHECK_DATE = "checkDate";
	//责任医生
	public static final String DOCTOR_NAME = "doctorName";
	//症状
	public static final String SYMTOM = "symtom";
	//体温
	public static final String TEMPERATURE = "temperature";
	//脉率
	public static final String PULSE_RATE = "pulseRate";
	//呼吸频率
	public static final String  BREATHING_RATE = "breathingRate";
	//左侧血压
	public static final String LEFT_BLOOD_PRESSURE = "leftBloodPressure";
	//右侧血压
	public static final String RIGHT_BLOOD_PRESSURE = "rightBloodPressure";
	//身高
	public static final String HEIGHT = "height";
	//体重
	public static final String WEIGHT = "weight";
	//腰围
	public static final String WAISTLINE = "waistline";
	//体质指数
	public static final String BMI = "BMI";
	//老年人健康状态自我评估
	public static final String HEATH_SELFASSESSMENT = "healthState";
	//老年人生活自理能力自我评估
	public static final String ABILITY_OF_SELF_SELFASSESSMENT = "abilityOfSelf";
	//老年人认知能力
	public static final String COGNITIVE_ABILITY = "cognitiveAbility";
	//老年人情感状态
	public static final String EMOTIONAL_STATE = "emotionalStates";
	//锻炼频率
	public static final String EXERCISE_FREQUENCY = "exerciseFrequency";
	//每次锻炼时间
	public static final String TIME_PER_EXERCISE = "timePerExercise";
	//坚持锻炼时间
	public static final String TIME_OF_KEEP_EXERCISING = "timeOfKeepExercise";
	//锻炼方式
	public static final String EXERCISE_METHOD = "methodOfExercise";
	//饮食习惯
	public static final String EATING_HABITS = "eatingHabits";
	//吸烟状况
	public static final String SMOKE_STATE = "smokeState";
	//日吸烟量
	public static final String SMOKE_PER_DAY = "smokeOfOneDay";
	//开始吸烟年龄
	public static final String AGE_OF_START_SMOKING = "ageOfStartSmoking";
	//戒烟年龄
	public static final String AGE_OF_STOP_SMOKING = "ageOfStopSmoking";
	//饮酒频率
	public static final String FREQUENCY_OF_DRINKING = "freOfDrinking";
	//日饮酒量
	public static final String DRINK_PER_DAY = "drinkPerDay";
	//是否戒酒
	public static final String WHETHER_STOP_DRINKING = "whetherStopDrinking";
	//开始饮酒年龄
	public static final String AGE_OF_START_DRINKING = "ageOfStartDrinking";
	//近一年内是否饮酒
	public static final String WHETHER_DRINKING_RECENT = "whetherDrinkingRecently";
	//饮酒种类
	public static final String KINDS_OF_DRINKS = "kindsOfDrinks";
	//职业病危害因素接触史
	public static final String HARMFUL_FACTORS_IN_JOB = "harmfulFactorsInJob";
	//口唇
	public static final String LIPS = "lips";
	//齿列
	public static final String TOOTH = "tooth";
	//咽部
	public static final String PHARYNGEAL = "pharyngeal";
	//左眼实例
	public static final String LEFT_EYESIGHT = "leftEyesight";
	//右眼视力
	public static final String RIGHT_EYESIGHT = "rightEyesight";
	//左眼矫正视力
	public static final String LEFT_CVA = "leftCVA";
	//右眼矫正视力
	public static final String RIGHT_CVA = "rightCVA";
	//听力
	public static final String AUDITION = "audition";
	//运动功能
	public static final String ABILITY_OF_EXERCISE = "abilityOfExercise";
	//眼底
	public static final String BOTTOM_OF_EYE = "bottomOfEye";
	//皮肤
	public static final String SKIN = "skin";
	//巩膜
	public static final String SCLERA = "sclera";
	//淋巴结
	public static final String LYMPHONODUS = "lymphonodus";
	//桶状胸
	public static final String LUNGS_TONGHUANGXIONG = "tongzhuangxiong";
	//呼吸音
	public static final String LUNGS_BREATHE_SOUND = "huxiyin";
	//罗  音
	public static final String LUNGS_luoyin = "luoyin";
	//心率
	public static final String HEART_RATE = "heartRate";
	//心律
	public static final String ARRHYTHMIA = "arrhythmia";
	//杂音
	public static final String HEART_NOISE = "heartNoise";
	//压痛
	public static final String ABDOMEN_YATONG = "abdomenYatong";
	//包块
	public static final String ABDOMEN_BAOKUAI = "abdomenBaokuai";
	//肝大
	public static final String ABDOMEN_GANDA = "abdomenGanda";
	//脾大
	public static final String ABDOMEN_PIDA = "abdomenPida";
	//移动性浊音
	public static final String ABDOMEN_ZHUOYIN = "abdomenZhuoyin";
	//下肢水肿
	public static final String EDEMA_LOWER_EXTREMITY = "edemaLowerExtremity";
	//足背动脉搏动
	public static final String DORSALIS_PEDIS_ARTERY_PULSE = "dorsalisPedisArteryPulse";
	//肛门指诊
	public static final String DRE = "DRE";
	//乳腺
	public static final String BREAST = "breast";
	//外阴
	public static final String VULVA = "vulva";
	//阴道
	public static final String VAGINA = "vagina";
	//宫颈
	public static final String CERVICAL = "cervical";
	//宫体
	public static final String CORPUS = "corpus";
	//附件
	public static final String ADNEXA = "adnexa";
	//其他
	public static final String BODY_OTHERS = "bodyOthers";
	//血红蛋白
	public static final String HAEMOGLOBIN = "haemoglobin";
	//白细胞
	public static final String HEMAMEBA = "hemameba";
	//血小板
	public static final String PLATELET = "platelet";
	//其他
	public static final String BLOOD_ROUTINE_OTHERS = "bloodRoutineOthers";
	//尿蛋白
	public static final String URINE_PROTEIN = "urineProtein";
	//尿糖
	public static final String UGLU = "urineGucose";
	//尿酮体
	public static final String URINE_ACETONE_BODY = "urineAcetoneBody";
	//尿潜血
	public static final String URINE_OCCULT_BLOOD = "urineOccultBlood";
	//其他
	public static final String URINE_ROUTINE_OTHER = "urineRoutineOthers";
	//空腹血糖
	public static final String FBG = "FBG";
	//血糖单位
	public static final String BLOOD_SUGAR_UNIT = "bloodSugarUnit";
	//心电图
	public static final String  ELECTROCARDIOGRAM = "electrocardiogram";
	//尿微量白蛋白
	public static final String MICROALBUMINURIA = "microalbuminuria";
	//大便潜血
	public static final String SED_OCCULT_BLOOD = "sedOccultBlood";
	//糖化血红蛋白
	public static final String GLYCOSYLATED_HEMOGLOBIN = "glycosylatedHemoglobin";
	//乙型肝炎表面抗原
	public static final String HBsAg = "HBsAg";
	//血清谷丙转氨酶
	public static final String SGPT = "SGPT";
	//血清谷草转氨酶
	public static final String SERUM_GLU_OXAL_TRANS = "serGluOxalTrans";
	//白蛋白
	public static final String LUNGS_ALBUMIN = "lungsAlbumin";
	//总胆红素
	public static final String LUNGS_TBil = "lungsTBil";
	//结合胆红素
	public static final String LUNGS_CONJ_BILIRUBIN = "lungsConjBilirubin";
	//血清肌酐
	public static final String SERUM_CREATININE = "serumCreatinine";
	//血尿素氮
	public static final String BLOOD_UREA_NITROGEN = "bloodUreaNitrogen";
	//血钾浓度
	public static final String POTASSIUM_CONCENTRATION = "potassiumConcentration";
	//血钠浓度
	public static final String SERUM_SODIUM_CONCENTRATION = "serumSodiumConcentration";
	//总胆固醇
	public static final String TOTAL_CHOLESTEROL = "totalCholesterol";
	//甘油三酯
	public static final String TRIGLYCERIDE = "triglyceride";
	//血清低密度脂蛋白胆固醇
	public static final String LDLC = "LDLC";
	//血清高密度脂蛋白胆固醇
	public static final String HDLC = "HDLC";
	//胸部X线片
	public static final String CHEST_X_RAY = "chestX_ray";
	//B超
	public static final String B_ULTRASOUND = "BUltrasound";
	//宫颈涂片
	public static final String CERVICAL_SMEAR = "cervicalSmear";
	//辅助检查室之其他
	public static final String HELP_CHECK_OTHER = "helpCheckOther";
	//平和质
	public static final String MILD_PHYSICAL = "mildPhysical";
	//气虚质
	public static final String FAINT_PHYSICAL = "faintPhysical";
	//阳虚质
	public static final String YANG_PHYSICAL = "yangPhysical";
	//阴虚质
	public static final String	YIN_PHYSICAL = "yinPhysical";
	//痰湿质
	public static final String PHLEGM_DAMPNESS_PHYSICAL = "phlegmDampnessPhysical";
	//湿热质
	public static final String DAMPNESS_HEAT_PHYSICAL = "dampnessHeatPhysical";
	//血瘀质
	public static final String BLOOD_STASIS_PHYSICAL = "bloodStasisPhysical";
	//气郁质
	public static final String QI_YU_PHYSICAL = "qiYuPhysical";
	//特秉质
	public static final String GRASP_PHYSICAL = "graspPhysical";
	//脑血管疾病
	public static final String CEREBROVASCULAR_DISEASES = "cerebrovascularDiseases";
	//肾脏疾病
	public static final String KIDNEY_DISEASE = "kidneyDisease";
	//心脏疾病
	public static final String CARDIAC_DISEASE = "cardiacDisease";
	//血管疾病
	public static final String VASCULAR_DISEASE = "vascularDisease";
	//眼部疾病
	public static final String OCULOPATHY = "oculopathy";
	//神经系统疾病
	public static final String NERVE_SYSTEM_DISEASE = "nerveSystemDisease";
	//其他系统疾病
	public static final String OTHER_SYSTEMS_DISEASE = "otherSystemsDisease";
	//健康评价
	public static final String HEALTH_EVALUATION = "healthEvaluation";
	//健康指导
	public static final String HEALTH_GUIDANCE = "healthGuidance";
	//危险因素控制
	public static final String RISK_FACTORS_FOR_CONTROL = "riskFactorsControl";
	
	//建表描述
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
