package com.health.archive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.health.database.DataOpenHelper;
import com.health.database.HCVaccTable;
import com.health.database.HealthCheckTable;
import com.health.database.HospHisTable;
import com.health.database.MedicationsTable;

import cn.younext.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class HealthCheckDetailActivity extends Activity {
	private DataOpenHelper dbHelper;
	private SQLiteDatabase db;
	private final static String HEALTH_CHECK_TABLE = "HEALTHCHECK";
	private static final String HOS_HIS_TABLE = "HosHis";
	private static final String MEDI_TABLE = "MAINMEDI";
	private static final String VACC_TABLE = "VaccTable";
	
	private ListView hosHisList;
	private ListView medicineList;
	private ListView vaccList;
	
	private String createTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_check_table);
		
		hosHisList = (ListView) findViewById(R.id.lv_hospital_history);
		medicineList = (ListView) findViewById(R.id.lv_medicine_history);
		vaccList = (ListView) findViewById(R.id.lv_vacc_history);

		dbHelper = new DataOpenHelper(this);
		db = dbHelper.getWritableDatabase();

		Intent intent = getIntent();
		String checkDate = intent.getStringExtra("checkDate");
		String serialId = intent.getStringExtra("serialId");
		createTime = checkDate;
		loadData(checkDate, serialId);
	}

	private void loadData(String checkDate, String serialId) {
		Cursor cursor = db.rawQuery("select * from " + HEALTH_CHECK_TABLE
				+ " where " + HealthCheckTable.USER_ID + "=?" + " and "
				+ HealthCheckTable.SERIAL_ID + "=?" + " and "
				+ HealthCheckTable.CHECK_DATE + "=?", new String[] { "123",
				serialId, checkDate });// 暂时写死
		
		//初始化组件
		EditText etName = (EditText) findViewById(R.id.et_name);
		EditText etSerialId = (EditText) findViewById(R.id.et_serialId);
		EditText etCheckDate = (EditText) findViewById(R.id.et_check_date);
		EditText etDoctorName = (EditText) findViewById(R.id.et_doctor_name);
		EditText etSymtom = (EditText) findViewById(R.id.et_symtom);
		EditText etTemperature = (EditText) findViewById(R.id.et_temperature);
		EditText etPulseRate = (EditText) findViewById(R.id.et_pulseRate);
		EditText etBreathingRate = (EditText) findViewById(R.id.et_breathingRate);
		TextView etLeftBloodPressure = (TextView) findViewById(R.id.et_leftBloodPressure);
		TextView etRightBloodPressure = (TextView) findViewById(R.id.et_rightBloodPressure);
		EditText etHeight = (EditText) findViewById(R.id.et_height);
		EditText etWeight = (EditText) findViewById(R.id.et_weight);
		EditText etWaistline = (EditText) findViewById(R.id.et_waistline);
		EditText etBMI = (EditText) findViewById(R.id.et_BMI);
		EditText etHealthState = (EditText) findViewById(R.id.et_healthState);
		EditText etAbilityOfSelf = (EditText) findViewById(R.id.et_abilityOfSelf);
		EditText etCognitiveAbility = (EditText) findViewById(R.id.et_cognitiveAbility);
		EditText etEmotionalStates = (EditText) findViewById(R.id.et_emotionalStates);
		EditText etExerciseFrequency = (EditText) findViewById(R.id.et_exerciseFrequency);
		EditText etTimePerExercise = (EditText) findViewById(R.id.et_timePerExercise);
		EditText etTimeOfKeepExercise = (EditText) findViewById(R.id.et_timeOfKeepExercise);
		EditText etMethodOfExercise = (EditText) findViewById(R.id.et_methodOfExercise);
		EditText etEatingHabits = (EditText) findViewById(R.id.et_eatingHabits);
		EditText etSmokeState = (EditText) findViewById(R.id.et_smokeState);
		EditText etSmokeOfOneDay = (EditText) findViewById(R.id.et_smokeOfOneDay);
		EditText etAgeOfStartSmoking = (EditText) findViewById(R.id.et_ageOfStartSmoking);
		EditText etAgeOfStopSmoking = (EditText) findViewById(R.id.et_ageOfStopSmoking);
		EditText etFreOfDrinking = (EditText) findViewById(R.id.et_freOfDrinking);
		EditText etDrinkPerDay = (EditText) findViewById(R.id.et_drinkPerDay);
		EditText etWhetherStopDrinking = (EditText) findViewById(R.id.et_whetherStopDrinking);
		EditText etAgeOfStartDrinking = (EditText) findViewById(R.id.et_ageOfStartDrinking);
		EditText etWhetherDrinkingRecently = (EditText) findViewById(R.id.et_whetherDrinkingRecently);
		EditText etKindsOfDrinks = (EditText) findViewById(R.id.et_kindsOfDrinks);
		EditText etHarmfulFactorsInJob = (EditText) findViewById(R.id.et_harmfulFactorsInJob);
		EditText etLips = (EditText) findViewById(R.id.et_lips);
		EditText etTooth = (EditText) findViewById(R.id.et_tooth);
		EditText etPharyngeal = (EditText) findViewById(R.id.et_pharyngeal);
		EditText etLeftEyesight = (EditText) findViewById(R.id.et_leftEyesight);
		EditText etRightEyesight = (EditText) findViewById(R.id.et_rightEyesight);
		EditText etLeftCVA = (EditText) findViewById(R.id.et_leftCVA);
		EditText etRightCVA = (EditText) findViewById(R.id.et_rightCVA);
		EditText etAudition = (EditText) findViewById(R.id.et_audition);
		EditText etAbilityOfExercise = (EditText) findViewById(R.id.et_abilityOfExercise);
		EditText etBottomOfEye = (EditText) findViewById(R.id.et_bottomOfEye);
		EditText etSkin = (EditText) findViewById(R.id.et_skin);
		EditText etSclera = (EditText) findViewById(R.id.et_sclera);
		EditText etLymphonodus = (EditText) findViewById(R.id.et_lymphonodus);
		EditText etTongzhuangxiong = (EditText) findViewById(R.id.et_tongzhuangxiong);
		EditText etHuxiyin = (EditText) findViewById(R.id.et_huxiyin);
		EditText etLuoyin = (EditText) findViewById(R.id.et_luoyin);
		EditText etHeartRate = (EditText) findViewById(R.id.et_heartRate);
		EditText etArrhythmia = (EditText) findViewById(R.id.et_arrhythmia);
		EditText etHeartNoise = (EditText) findViewById(R.id.et_heartNoise);
		EditText etAbdomenYatong = (EditText) findViewById(R.id.et_abdomenYatong);
		EditText etAbdomenBaokuai = (EditText) findViewById(R.id.et_abdomenBaokuai);
		EditText etAbdomenGanda = (EditText) findViewById(R.id.et_abdomenGanda);
		EditText etAbdomenPida = (EditText) findViewById(R.id.et_abdomenPida);
		EditText etAbdomenZhuoyin = (EditText) findViewById(R.id.et_abdomenZhuoyin);
		EditText etEdemaLowerExtremity = (EditText) findViewById(R.id.et_edemaLowerExtremity);
		EditText etDorsalisPedisArteryPulse = (EditText) findViewById(R.id.et_dorsalisPedisArteryPulse);
		EditText etDRE = (EditText) findViewById(R.id.et_DRE);
		EditText etbreast = (EditText) findViewById(R.id.et_breast);
		EditText etvulva = (EditText) findViewById(R.id.et_vulva);
		EditText etVagina = (EditText) findViewById(R.id.et_vagina);
		EditText etCervical = (EditText) findViewById(R.id.et_cervical);
		EditText etCorpus = (EditText) findViewById(R.id.et_corpus);
		EditText etAdnexa = (EditText) findViewById(R.id.et_adnexa);
		EditText etBodyOthers = (EditText) findViewById(R.id.et_bodyOthers);
		EditText etHaemoglobin = (EditText) findViewById(R.id.et_haemoglobin);
		EditText etHemameba = (EditText) findViewById(R.id.et_hemameba);
		EditText etPlatelet = (EditText) findViewById(R.id.et_platelet);
		EditText etbloodRoutineOthers = (EditText) findViewById(R.id.et_bloodRoutineOthers);
		EditText etUrineProtein = (EditText) findViewById(R.id.et_urineProtein);
		EditText etUrineGucose = (EditText) findViewById(R.id.et_urineGucose);
		EditText etUrineAcetoneBody = (EditText) findViewById(R.id.et_urineAcetoneBody);
		EditText etUrineOccultBlood = (EditText) findViewById(R.id.et_urineOccultBlood);
		EditText etUrineRoutineOthers = (EditText) findViewById(R.id.et_urineRoutineOthers);
		EditText etFBG = (EditText) findViewById(R.id.et_FBG);
		EditText etBloodSugarUnit = (EditText) findViewById(R.id.et_bloodSugarUnit);
		EditText etElectrocardiogram = (EditText) findViewById(R.id.et_electrocardiogram);
		EditText etMicroalbuminuria = (EditText) findViewById(R.id.et_microalbuminuria);
		EditText etSedOccultBlood = (EditText) findViewById(R.id.et_SedOccultBlood);
		EditText etGlycosylatedHemoglobin = (EditText) findViewById(R.id.et_glycosylatedHemoglobin);
		EditText etHBsAg = (EditText) findViewById(R.id.et_HBsAg);
		EditText etSGPT = (EditText) findViewById(R.id.et_SGPT);
		EditText etSerGluOxalTrans = (EditText) findViewById(R.id.et_serGluOxalTrans);
		EditText etLungsAlbumin = (EditText) findViewById(R.id.et_lungsAlbumin);
		EditText etLungsTBil = (EditText) findViewById(R.id.et_lungsTBil);
		EditText etLungsConjBilirubin = (EditText) findViewById(R.id.et_lungsConjBilirubin);
		EditText etSerumCreatinine = (EditText) findViewById(R.id.et_serumCreatinine);
		EditText etBloodUreaNitrogen = (EditText) findViewById(R.id.et_bloodUreaNitrogen);
		EditText etPotassiumConcentration = (EditText) findViewById(R.id.et_potassiumConcentration);
		EditText etSerumSodiumConcentration = (EditText) findViewById(R.id.et_SerumSodiumConcentration);
		EditText etTotalCholesterol = (EditText) findViewById(R.id.et_totalCholesterol);
		EditText etTriglyceride = (EditText) findViewById(R.id.et_triglyceride);
		EditText etLDLC = (EditText) findViewById(R.id.et_LDLC);
		EditText etHDLC = (EditText) findViewById(R.id.et_HDLC);
		EditText etChestX_ray = (EditText) findViewById(R.id.et_chestX_ray);
		EditText etBUltrasound = (EditText) findViewById(R.id.et_BUltrasound);
		EditText etCervicalSmear = (EditText) findViewById(R.id.et_cervicalSmear);
		EditText etHelpCheckOther = (EditText) findViewById(R.id.et_helpCheckOther);
		EditText etMildPhysical = (EditText) findViewById(R.id.et_mildPhysical);
		EditText etFaintPhysical = (EditText) findViewById(R.id.et_faintPhysical);
		EditText etYangPhysical = (EditText) findViewById(R.id.et_yangPhysical);
		EditText etYinPhysical = (EditText) findViewById(R.id.et_yinPhysical);
		EditText etPhlegmDampnessPhysical = (EditText) findViewById(R.id.et_phlegmDampnessPhysical);
		EditText etDampnessHeatPhysical = (EditText) findViewById(R.id.et_dampnessHeatPhysical);
		EditText etBloodStasisPhysical = (EditText) findViewById(R.id.et_bloodStasisPhysical);
		EditText etQiYuPhysical = (EditText) findViewById(R.id.et_qiYuPhysical);
		EditText etGraspPhysical = (EditText) findViewById(R.id.et_graspPhysical);
		EditText etCerebrovascularDiseases = (EditText) findViewById(R.id.et_cerebrovascularDiseases);
		EditText etKidneyDisease = (EditText) findViewById(R.id.et_kidneyDisease);
		EditText etCardiacDisease = (EditText) findViewById(R.id.et_cardiacDisease);
		EditText etVascularDisease = (EditText) findViewById(R.id.et_vascularDisease);
		EditText etOculopathy = (EditText) findViewById(R.id.et_oculopathy);
		EditText etNerveSystemDisease = (EditText) findViewById(R.id.et_nerveSystemDisease);
		EditText etOtherSystemsDisease = (EditText) findViewById(R.id.et_otherSystemsDisease);
		EditText etHealthEvaluation = (EditText) findViewById(R.id.et_healthEvaluation);
		EditText etHealthGuidance = (EditText) findViewById(R.id.et_healthGuidance);
		EditText etRiskFactorsControl = (EditText) findViewById(R.id.et_riskFactorsControl);
		
		//给组建赋值
		etName.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.NAME)));
		etSerialId.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SERIAL_ID)));
		etCheckDate.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CHECK_DATE)));
		etDoctorName.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.DOCTOR_NAME)));
		etSymtom.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SYMTOM)));
		etTemperature.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.TEMPERATURE)));
		etPulseRate.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.PULSE_RATE)));
		etBreathingRate.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BREATHING_RATE)));
		etLeftBloodPressure.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LEFT_BLOOD_PRESSURE)));
		etRightBloodPressure.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.RIGHT_BLOOD_PRESSURE)));
		etHeight.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEIGHT)));
		etWeight.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.WEIGHT)));
		etWaistline.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.WAISTLINE)));
		etBMI.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BMI)));
		etHealthState.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEATH_SELFASSESSMENT)));
		etAbilityOfSelf.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABILITY_OF_SELF_SELFASSESSMENT)));
		etCognitiveAbility.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.COGNITIVE_ABILITY)));
		etEmotionalStates.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.EMOTIONAL_STATE)));
		etExerciseFrequency.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.EXERCISE_FREQUENCY)));
		etTimePerExercise.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.TIME_PER_EXERCISE)));
		etTimeOfKeepExercise.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.TIME_OF_KEEP_EXERCISING)));
		etMethodOfExercise.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.EXERCISE_METHOD)));
		etEatingHabits.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.EATING_HABITS)));
		etSmokeState.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SMOKE_STATE)));
		etSmokeOfOneDay.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SMOKE_PER_DAY)));
		etAgeOfStartSmoking.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.AGE_OF_START_SMOKING)));
		etAgeOfStopSmoking.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.AGE_OF_STOP_SMOKING)));
		etFreOfDrinking.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.FREQUENCY_OF_DRINKING)));
		etDrinkPerDay.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.DRINK_PER_DAY)));
		etWhetherStopDrinking.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.WHETHER_STOP_DRINKING)));
		etAgeOfStartDrinking.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.AGE_OF_START_DRINKING)));
		etWhetherDrinkingRecently.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.WHETHER_DRINKING_RECENT)));
		etKindsOfDrinks.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.KINDS_OF_DRINKS)));
		etHarmfulFactorsInJob.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HARMFUL_FACTORS_IN_JOB)));
		etLips.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LIPS)));
		etTooth.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.TOOTH)));
		etPharyngeal.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.PHARYNGEAL)));
		etLeftEyesight.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LEFT_EYESIGHT)));
		etRightEyesight.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.RIGHT_EYESIGHT)));
		etLeftCVA.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LEFT_CVA)));
		etRightCVA.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.RIGHT_CVA)));
		etAudition.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.AUDITION)));
		etAbilityOfExercise.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABILITY_OF_EXERCISE)));
		etBottomOfEye.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BOTTOM_OF_EYE)));
		etSkin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SKIN)));
		etSclera.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SCLERA)));
		etLymphonodus.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LYMPHONODUS)));
		etTongzhuangxiong.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LUNGS_TONGHUANGXIONG)));
		etHuxiyin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LUNGS_BREATHE_SOUND)));
		etLuoyin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LUNGS_luoyin)));
		etHeartRate.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEART_RATE)));
		etArrhythmia.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ARRHYTHMIA)));
		etHeartNoise.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEART_NOISE)));
		etAbdomenYatong.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABDOMEN_YATONG)));
		etAbdomenBaokuai.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABDOMEN_BAOKUAI)));
		etAbdomenGanda.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABDOMEN_GANDA)));
		etAbdomenPida.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABDOMEN_PIDA)));
		etAbdomenZhuoyin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ABDOMEN_ZHUOYIN)));
		etEdemaLowerExtremity.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.EDEMA_LOWER_EXTREMITY)));
		etDorsalisPedisArteryPulse.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.DORSALIS_PEDIS_ARTERY_PULSE)));
		etDRE.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.DRE)));
		etbreast.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BREAST)));
		etvulva.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.VULVA)));
		etVagina.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.VAGINA)));
		etCervical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CERVICAL)));
		etCorpus.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CORPUS)));
		etAdnexa.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ADNEXA)));
		etBodyOthers.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BODY_OTHERS)));
		etHaemoglobin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HAEMOGLOBIN)));
		etHemameba.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEMAMEBA)));
		etPlatelet.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.PLATELET)));
		etbloodRoutineOthers.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BLOOD_ROUTINE_OTHERS)));
		etUrineProtein.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.URINE_PROTEIN)));
		etUrineGucose.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.UGLU)));
		etUrineAcetoneBody.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.URINE_ACETONE_BODY)));
		etUrineOccultBlood.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.URINE_OCCULT_BLOOD)));
		etUrineRoutineOthers.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.URINE_ROUTINE_OTHER)));
		etFBG.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.FBG)));
		etBloodSugarUnit.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BLOOD_SUGAR_UNIT)));
		etElectrocardiogram.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.ELECTROCARDIOGRAM)));
		etMicroalbuminuria.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.MICROALBUMINURIA)));
		etSedOccultBlood.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SED_OCCULT_BLOOD)));
		etGlycosylatedHemoglobin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.GLYCOSYLATED_HEMOGLOBIN)));
		etHBsAg.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HBsAg)));
		etSGPT.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SGPT)));
		etSerGluOxalTrans.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SERUM_GLU_OXAL_TRANS)));
		etLungsAlbumin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LUNGS_ALBUMIN)));
		etLungsTBil.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LUNGS_TBil)));
		etLungsConjBilirubin.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LUNGS_CONJ_BILIRUBIN)));
		etSerumCreatinine.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SERUM_CREATININE)));
		etBloodUreaNitrogen.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BLOOD_UREA_NITROGEN)));
		etPotassiumConcentration.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.POTASSIUM_CONCENTRATION)));
		etSerumSodiumConcentration.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.SERUM_SODIUM_CONCENTRATION)));
		etTotalCholesterol.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.TOTAL_CHOLESTEROL)));
		etTriglyceride.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.TRIGLYCERIDE)));
		etLDLC.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.LDLC)));
		etHDLC.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HDLC)));
		etChestX_ray.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CHEST_X_RAY)));
		etBUltrasound.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.B_ULTRASOUND)));
		etCervicalSmear.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CERVICAL_SMEAR)));
		etHelpCheckOther.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HELP_CHECK_OTHER)));
		etMildPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.MILD_PHYSICAL)));
		etFaintPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.FAINT_PHYSICAL)));
		etYangPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.YANG_PHYSICAL)));
		etYinPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.YIN_PHYSICAL)));
		etPhlegmDampnessPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.PHLEGM_DAMPNESS_PHYSICAL)));
		etDampnessHeatPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.DAMPNESS_HEAT_PHYSICAL)));
		etBloodStasisPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.BLOOD_STASIS_PHYSICAL)));
		etQiYuPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.QI_YU_PHYSICAL)));
		etGraspPhysical.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.GRASP_PHYSICAL)));
		etCerebrovascularDiseases.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CEREBROVASCULAR_DISEASES)));
		etKidneyDisease.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.KIDNEY_DISEASE)));
		etCardiacDisease.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.CARDIAC_DISEASE)));
		etVascularDisease.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.VASCULAR_DISEASE)));
		etOculopathy.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.OCULOPATHY)));
		etNerveSystemDisease.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.NERVE_SYSTEM_DISEASE)));
		etOtherSystemsDisease.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.OTHER_SYSTEMS_DISEASE)));
		etHealthEvaluation.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEALTH_EVALUATION)));
		etHealthGuidance.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.HEALTH_GUIDANCE)));
		etRiskFactorsControl.setText(cursor.getString(cursor.getColumnIndex(HealthCheckTable.RISK_FACTORS_FOR_CONTROL)));

	}
	
	private void loadVaccHis() {
		Cursor cursor = db.rawQuery("select * from " + VACC_TABLE + " where "
				+ HCVaccTable.USER_ID + "=?" + " and "
				+ HCVaccTable.CREATE_DATE + "=?", new String[] { "123",
				createTime });// 暂时写死

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int vaccNameCol = cursor.getColumnIndex(HCVaccTable.VACC_NAME);
		int vaccDateCol = cursor.getColumnIndex(HCVaccTable.VACC_DATE);
		int vaccComCOl = cursor.getColumnIndex(HCVaccTable.VACC_COM);

		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("vaccName", cursor.getString(vaccNameCol));
			map.put("vaccDate", cursor.getString(vaccDateCol));
			map.put("vaccCom", cursor.getString(vaccComCOl));

			list.add(map);
		}

		Log.i("list的长度", list.size() + "");
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.vacc_his_item, new String[] { "vaccName", "vaccDate",
						"vaccCom" }, new int[] { R.id.tv_vaccName,
						R.id.tv_vaccDate, R.id.tv_vaccCom });
		vaccList.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(vaccList);

	}

	private void loadMediHis() {
		Cursor cursor = db.rawQuery("select * from " + MEDI_TABLE + " where "
				+ MedicationsTable.USER_ID + "=?" + " and "
				+ MedicationsTable.CREATE_DATE + "=?", new String[] { "123",
				createTime });// 暂时写死

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int drugNameCol = cursor.getColumnIndex(MedicationsTable.DRUG_NAME);
		int usageCol = cursor.getColumnIndex(MedicationsTable.USAGE);
		int dosageCol = cursor.getColumnIndex(MedicationsTable.DOSAGE);
		int timeCol = cursor.getColumnIndex(MedicationsTable.MEDICATION_TIME);
		int comCol = cursor
				.getColumnIndex(MedicationsTable.MEDICATION_COMPLIANCE);
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("drugName", cursor.getString(drugNameCol));
			map.put("usage", cursor.getString(usageCol));
			map.put("dosage", cursor.getString(dosageCol));
			map.put("time", cursor.getString(timeCol));
			map.put("compliance", cursor.getString(comCol));
			list.add(map);
		}

		Log.i("list的长度", list.size() + "");
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.medicine_list_item, new String[] { "drugName",
						"usage", "dosage", "time", "compliance" }, new int[] {
						R.id.tv_drugName, R.id.tv_usage, R.id.tv_dosage,
						R.id.tv_time, R.id.tv_compliance });
		medicineList.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(medicineList);

	}

	// 加载数据
	private void loadHosHis() {

		Cursor cursor = db.rawQuery("select * from " + HOS_HIS_TABLE
				+ " where " + HospHisTable.USER_ID + "=?" + " and " + HospHisTable.CREATE_DATE + "=?",
				new String[] { "123", createTime });// 暂时写死

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int dateCol = cursor.getColumnIndex(HospHisTable.DATE_OF_IN_AND_OUT);
		int reasonsCol = cursor.getColumnIndex(HospHisTable.REASONS);
		int hosNameCol = cursor.getColumnIndex(HospHisTable.MHO);
		int pinCol = cursor.getColumnIndex(HospHisTable.PIN);
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", cursor.getString(dateCol));
			map.put("reasons", cursor.getString(reasonsCol));
			map.put("hosName", cursor.getString(hosNameCol));
			map.put("pin", cursor.getString(pinCol));
			list.add(map);
		}

		Log.i("list的长度", list.size() + "");
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.hos_his_list_item, new String[] { "date", "reasons",
						"hosName", "pin" }, new int[] { R.id.tv_date,
						R.id.tv_reasons, R.id.tv_hosName, R.id.tv_pin });
		hosHisList.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(hosHisList);

	}
}
