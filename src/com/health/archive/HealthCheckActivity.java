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
import com.health.viewUtil.ChoiceEditText;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import cn.younext.R;

public class HealthCheckActivity extends Activity {
	private ListView hosHisList;
	private ListView medicineList;
	private ListView vaccList;
	private DataOpenHelper dbHelper;
	private SQLiteDatabase db;
	private static final String HOS_HIS_TABLE = "HosHis";
	private static final String MEDI_TABLE = "MAINMEDI";
	private static final String VACC_TABLE = "VaccTable";
	private static final String HEALTH_CHECK_TABLE = "HEALTHCHECK";
	private EditText etCheckdate;
	private String createTime;
	private String action;
	private String serialId;
	private int resultCode = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_check_table);

		Intent intent = getIntent();
		action = intent.getStringExtra("action");
		serialId = intent.getStringExtra("serialId");
		hosHisList = (ListView) findViewById(R.id.lv_hospital_history);
		etCheckdate = (EditText) findViewById(R.id.et_check_date);

		medicineList = (ListView) findViewById(R.id.lv_medicine_history);
		vaccList = (ListView) findViewById(R.id.lv_vacc_history);
		dbHelper = new DataOpenHelper(this);
		db = dbHelper.getWritableDatabase();

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("填写您的血压");
		LayoutInflater inflater1 = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout tl = (TableLayout) inflater1.inflate(
				R.layout.health_check_bloodpre, null);
		final EditText shuzhang = (EditText) tl.findViewById(R.id.et_shuzhang);
		final EditText shousuo = (EditText) tl.findViewById(R.id.et_shousuo);
		builder.setView(tl);
		provideData();

		final TextView leftBloodPre = (TextView) findViewById(R.id.et_leftBloodPressure);
		final TextView rightBloodPre = (TextView) findViewById(R.id.et_rightBloodPressure);
		leftBloodPre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						leftBloodPre.setText(shuzhang.getText() + "/"
								+ shousuo.getText());

					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		rightBloodPre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				LayoutInflater inflater = (LayoutInflater) HealthCheckActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				TableLayout tl = (TableLayout) inflater.inflate(
						R.layout.health_check_bloodpre, null);
				builder.setView(tl);
				builder.setTitle("填写您的血压");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						rightBloodPre.setText(shuzhang.getText() + "/"
								+ shousuo.getText());
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		Button addHospitalHis = (Button) findViewById(R.id.btn_add_hospital_history);
		addHospitalHis.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) HealthCheckActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.be_hospitalized_dialog, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				builder.setView(ll);

				builder.setTitle("填写住院信息");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String date1 = ((EditText) ll
								.findViewById(R.id.et_in_hospital_date))
								.getText().toString();
						String date2 = ((EditText) ll
								.findViewById(R.id.et_out_hospital_date))
								.getText().toString();
						String date = date1 + "/" + date2;
						String reasons = ((EditText) ll
								.findViewById(R.id.et_in_hospital_rensons))
								.getText().toString();
						String hosName = ((EditText) ll
								.findViewById(R.id.et_hospital_name)).getText()
								.toString();
						String pin = ((EditText) ll
								.findViewById(R.id.et_case_no)).getText()
								.toString();
						ContentValues values = new ContentValues();
						values.put(HospHisTable.USER_ID, "123");
						values.put(HospHisTable.MHO, hosName);
						values.put(HospHisTable.DATE_OF_IN_AND_OUT, date);
						values.put(HospHisTable.PIN, pin);
						values.put(HospHisTable.REASONS, reasons);
						values.put(HospHisTable.CREATE_DATE, createTime);
						if (pin != null && pin.length() != 0) {

							long result = db
									.insert(HOS_HIS_TABLE, null, values);
							if (result > 0) {
								Toast.makeText(HealthCheckActivity.this,
										"添加成功！", Toast.LENGTH_SHORT).show();
							} else {
								Log.i("插入调试信息", values.toString());
							}
							loadHosHis();
						}

					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();

			}
		});

		hosHisList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent,
					View view, final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				builder.setMessage("您确定要删除该条记录？");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Map<String, String> m = (Map) parent
								.getItemAtPosition(position);
						String pin = m.get("pin");
						Log.i("PIN", pin);
						db.delete(HOS_HIS_TABLE, HospHisTable.PIN + "=?",
								new String[] { pin });
						loadHosHis();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return true;
			}

		});

		Button addMedicine = (Button) findViewById(R.id.btn_add_medicine);
		addMedicine.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) HealthCheckActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.add_medicine_dialog, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				builder.setView(ll);
				builder.setTitle("填写药物使用情况");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String drugName = ((EditText) ll
								.findViewById(R.id.et_drugName)).getText()
								.toString();
						String usage = ((EditText) ll
								.findViewById(R.id.et_usage)).getText()
								.toString();
						String dosage = ((EditText) ll
								.findViewById(R.id.et_dosage)).getText()
								.toString();
						String time = ((EditText) ll.findViewById(R.id.et_time))
								.getText().toString();
						String compliance = ((EditText) ll
								.findViewById(R.id.et_compliance)).getText()
								.toString();
						ContentValues values = new ContentValues();
						values.put(MedicationsTable.USER_ID, "123");
						values.put(MedicationsTable.DRUG_NAME, drugName);
						values.put(MedicationsTable.USAGE, usage);
						values.put(MedicationsTable.DOSAGE, dosage);
						values.put(MedicationsTable.MEDICATION_TIME, time);
						values.put(MedicationsTable.MEDICATION_COMPLIANCE,
								compliance);
						values.put(MedicationsTable.CREATE_DATE, createTime);
						if (drugName != null && drugName.length() != 0) {
							long result = db.insert(MEDI_TABLE, null, values);
							if (result > 0) {
								Toast.makeText(HealthCheckActivity.this,
										"添加成功！", Toast.LENGTH_SHORT).show();
							} else {
								Log.i("插入调试信息", values.toString());
							}
							loadMediHis();
						}

					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		medicineList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent,
					View view, final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				builder.setMessage("您确定要删除该条记录？");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Map<String, String> m = (Map) parent
								.getItemAtPosition(position);
						String drugName = m.get("drugName");
						Log.i("drugName", drugName);
						db.delete(MEDI_TABLE,
								MedicationsTable.DRUG_NAME + "=?",
								new String[] { drugName });
						loadMediHis();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return true;
			}

		});

		Button addVaccHis = (Button) findViewById(R.id.btn_add_vacc);
		addVaccHis.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) HealthCheckActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.add_vacc_dialog, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				builder.setView(ll);
				builder.setTitle("填写接种记录");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String vaccName = ((EditText) ll
								.findViewById(R.id.et_vaccName)).getText()
								.toString();
						String vaccDate = ((EditText) ll
								.findViewById(R.id.et_vaccDate)).getText()
								.toString();
						String vaccCom = ((EditText) ll
								.findViewById(R.id.et_vaccCom)).getText()
								.toString();

						ContentValues values = new ContentValues();
						values.put(HCVaccTable.USER_ID, "123");
						values.put(HCVaccTable.VACC_NAME, vaccName);
						values.put(HCVaccTable.VACC_DATE, vaccDate);
						values.put(HCVaccTable.VACC_COM, vaccCom);
						values.put(HCVaccTable.CREATE_DATE, createTime);

						if (vaccName != null && vaccName.length() != 0) {
							long result = db.insert(VACC_TABLE, null, values);
							if (result > 0) {
								Toast.makeText(HealthCheckActivity.this,
										"添加成功！", Toast.LENGTH_SHORT).show();
							} else {
								Log.i("插入调试信息", values.toString());
							}
							loadVaccHis();
						}

					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		vaccList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent,
					View view, final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						HealthCheckActivity.this);
				builder.setMessage("您确定要删除该条记录？");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Map<String, String> m = (Map) parent
								.getItemAtPosition(position);
						String name = m.get("vaccName");
						Log.i("name", name);
						db.delete(VACC_TABLE, HCVaccTable.VACC_NAME + "=?",
								new String[] { name });
						loadVaccHis();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return true;
			}

		});

		if ("detail".equals(action)) {
			String checkDate = intent.getStringExtra("checkDate");
			Log.i("@@@@@@@@@", "checkDate=" + checkDate);
			serialId = intent.getStringExtra("serialId");
			Log.i("@@@@@@@@@", "serialId=" + serialId);
			loadDataFromDatabase(checkDate, serialId);
		}

	}

	private void loadDataFromDatabase(String checkDate, String serialId) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from " + HEALTH_CHECK_TABLE
				+ " where " + HealthCheckTable.USER_ID + "=?" + " and "
				+ HealthCheckTable.SERIAL_ID + "=?" + " and "
				+ HealthCheckTable.CHECK_DATE + "=?", new String[] { "123",
				serialId, checkDate });// 暂时写死

		Log.i("@@@@@@@@@@", "count=" + cursor.getCount());

		// 初始化组件
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

		// 给组建赋值
		while (cursor.moveToNext()) {
			etName.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.NAME)));
			etSerialId.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SERIAL_ID)));
			etCheckDate.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.CHECK_DATE)));
			etDoctorName.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.DOCTOR_NAME)));
			etSymtom.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SYMTOM)));
			etTemperature.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.TEMPERATURE)));
			etPulseRate.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.PULSE_RATE)));
			etBreathingRate.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BREATHING_RATE)));

			etLeftBloodPressure.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LEFT_BLOOD_PRESSURE)));
			etRightBloodPressure.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.RIGHT_BLOOD_PRESSURE)));

			etHeight.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEIGHT)));

			etWeight.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.WEIGHT)));
			etWaistline.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.WAISTLINE)));
			etBMI.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BMI)));
			etHealthState.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEATH_SELFASSESSMENT)));
			etAbilityOfSelf
					.setText(cursor.getString(cursor
							.getColumnIndex(HealthCheckTable.ABILITY_OF_SELF_SELFASSESSMENT)));

			etCognitiveAbility.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.COGNITIVE_ABILITY)));
			etEmotionalStates.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.EMOTIONAL_STATE)));
			etExerciseFrequency.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.EXERCISE_FREQUENCY)));
			etTimePerExercise.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.TIME_PER_EXERCISE)));

			etTimeOfKeepExercise.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.TIME_OF_KEEP_EXERCISING)));
			etMethodOfExercise.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.EXERCISE_METHOD)));
			etEatingHabits.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.EATING_HABITS)));

			etSmokeState.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SMOKE_STATE)));
			etSmokeOfOneDay.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SMOKE_PER_DAY)));
			etAgeOfStartSmoking.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.AGE_OF_START_SMOKING)));

			etAgeOfStopSmoking.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.AGE_OF_STOP_SMOKING)));
			etFreOfDrinking.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.FREQUENCY_OF_DRINKING)));
			etDrinkPerDay.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.DRINK_PER_DAY)));
			etWhetherStopDrinking.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.WHETHER_STOP_DRINKING)));
			etAgeOfStartDrinking.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.AGE_OF_START_DRINKING)));
			etWhetherDrinkingRecently.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.WHETHER_DRINKING_RECENT)));
			etKindsOfDrinks.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.KINDS_OF_DRINKS)));
			etHarmfulFactorsInJob.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HARMFUL_FACTORS_IN_JOB)));
			etLips.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LIPS)));
			etTooth.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.TOOTH)));
			etPharyngeal.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.PHARYNGEAL)));

			etLeftEyesight.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LEFT_EYESIGHT)));
			etRightEyesight.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.RIGHT_EYESIGHT)));
			etLeftCVA.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LEFT_CVA)));
			etRightCVA.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.RIGHT_CVA)));
			etAudition.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.AUDITION)));
			etAbilityOfExercise.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ABILITY_OF_EXERCISE)));
			etBottomOfEye.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BOTTOM_OF_EYE)));
			etSkin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SKIN)));
			etSclera.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SCLERA)));
			etLymphonodus.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LYMPHONODUS)));

			etTongzhuangxiong.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LUNGS_TONGHUANGXIONG)));
			etHuxiyin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LUNGS_BREATHE_SOUND)));
			etLuoyin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LUNGS_luoyin)));
			etHeartRate.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEART_RATE)));
			etArrhythmia.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ARRHYTHMIA)));
			etHeartNoise.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEART_NOISE)));
			etAbdomenYatong.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ABDOMEN_YATONG)));
			etAbdomenBaokuai.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ABDOMEN_BAOKUAI)));

			etAbdomenGanda.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ABDOMEN_GANDA)));
			etAbdomenPida.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ABDOMEN_PIDA)));
			etAbdomenZhuoyin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ABDOMEN_ZHUOYIN)));
			etEdemaLowerExtremity.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.EDEMA_LOWER_EXTREMITY)));
			etDorsalisPedisArteryPulse
					.setText(cursor.getString(cursor
							.getColumnIndex(HealthCheckTable.DORSALIS_PEDIS_ARTERY_PULSE)));
			etDRE.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.DRE)));
			etbreast.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BREAST)));

			etvulva.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.VULVA)));
			etVagina.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.VAGINA)));
			etCervical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.CERVICAL)));
			etCorpus.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.CORPUS)));
			etAdnexa.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ADNEXA)));
			etBodyOthers.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BODY_OTHERS)));
			etHaemoglobin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HAEMOGLOBIN)));
			etHemameba.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEMAMEBA)));
			etPlatelet.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.PLATELET)));
			etbloodRoutineOthers.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BLOOD_ROUTINE_OTHERS)));

			etUrineProtein.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.URINE_PROTEIN)));
			etUrineGucose.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.UGLU)));
			etUrineAcetoneBody.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.URINE_ACETONE_BODY)));
			etUrineOccultBlood.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.URINE_OCCULT_BLOOD)));
			etUrineRoutineOthers.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.URINE_ROUTINE_OTHER)));
			etFBG.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.FBG)));

			etBloodSugarUnit.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BLOOD_SUGAR_UNIT)));

			etElectrocardiogram.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.ELECTROCARDIOGRAM)));

			etMicroalbuminuria.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.MICROALBUMINURIA)));
			etSedOccultBlood.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SED_OCCULT_BLOOD)));
			etGlycosylatedHemoglobin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.GLYCOSYLATED_HEMOGLOBIN)));
			etHBsAg.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HBsAg)));
			etSGPT.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SGPT)));
			etSerGluOxalTrans.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SERUM_GLU_OXAL_TRANS)));
			etLungsAlbumin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LUNGS_ALBUMIN)));
			etLungsTBil.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LUNGS_TBil)));
			etLungsConjBilirubin.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LUNGS_CONJ_BILIRUBIN)));
			etSerumCreatinine.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.SERUM_CREATININE)));
			etBloodUreaNitrogen.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BLOOD_UREA_NITROGEN)));
			etPotassiumConcentration.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.POTASSIUM_CONCENTRATION)));
			etSerumSodiumConcentration
					.setText(cursor.getString(cursor
							.getColumnIndex(HealthCheckTable.SERUM_SODIUM_CONCENTRATION)));
			etTotalCholesterol.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.TOTAL_CHOLESTEROL)));

			etTriglyceride.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.TRIGLYCERIDE)));
			etLDLC.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.LDLC)));
			etHDLC.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HDLC)));
			etChestX_ray.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.CHEST_X_RAY)));
			etBUltrasound.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.B_ULTRASOUND)));
			etCervicalSmear.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.CERVICAL_SMEAR)));
			etHelpCheckOther.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HELP_CHECK_OTHER)));
			etMildPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.MILD_PHYSICAL)));
			etFaintPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.FAINT_PHYSICAL)));
			etYangPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.YANG_PHYSICAL)));
			etYinPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.YIN_PHYSICAL)));
			etPhlegmDampnessPhysical
					.setText(cursor.getString(cursor
							.getColumnIndex(HealthCheckTable.PHLEGM_DAMPNESS_PHYSICAL)));

			etDampnessHeatPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.DAMPNESS_HEAT_PHYSICAL)));
			etBloodStasisPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.BLOOD_STASIS_PHYSICAL)));
			etQiYuPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.QI_YU_PHYSICAL)));
			etGraspPhysical.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.GRASP_PHYSICAL)));
			etCerebrovascularDiseases
					.setText(cursor.getString(cursor
							.getColumnIndex(HealthCheckTable.CEREBROVASCULAR_DISEASES)));
			etKidneyDisease.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.KIDNEY_DISEASE)));
			etCardiacDisease.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.CARDIAC_DISEASE)));
			etVascularDisease.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.VASCULAR_DISEASE)));
			etOculopathy.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.OCULOPATHY)));
			etNerveSystemDisease.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.NERVE_SYSTEM_DISEASE)));
			etOtherSystemsDisease.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.OTHER_SYSTEMS_DISEASE)));
			etHealthEvaluation.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEALTH_EVALUATION)));
			etHealthGuidance.setText(cursor.getString(cursor
					.getColumnIndex(HealthCheckTable.HEALTH_GUIDANCE)));
			etRiskFactorsControl
					.setText(cursor.getString(cursor
							.getColumnIndex(HealthCheckTable.RISK_FACTORS_FOR_CONTROL)));

		}
		createTime = etCheckDate.getText().toString();

		loadVaccHis();
		loadMediHis();
		loadHosHis();

	}

	private void loadVaccHis() {
		if (createTime == null) {
			createTime = etCheckdate.getText().toString();
		}
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
		if (createTime == null) {
			createTime = etCheckdate.getText().toString();
		}

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
		if (createTime == null) {
			createTime = etCheckdate.getText().toString();
		}
		Cursor cursor = db.rawQuery("select * from " + HOS_HIS_TABLE
				+ " where " + HospHisTable.USER_ID + "=?" + " and "
				+ HospHisTable.CREATE_DATE + "=?", new String[] { "123",
				createTime });// 暂时写死

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

	private void provideData() {
		setChoiceEditText(R.id.et_symtom, new String[] { "0.无症状", "1.头痛",
				"2.头晕", "3.心悸", "4.胸闷", "5.胸痛", "6.慢性咳嗽", "7.咳痰 ", "8.呼吸困难",
				"9.多饮", "10.多尿", "11.体重下降", "12.乏力", "13.关节肿痛", "14.视力模糊",
				"15.手脚麻木", "17尿急", "17.尿痛", "18.便秘", "19.腹泻", "20.恶心呕吐",
				"21.眼花", "22.耳鸣", "23.乳房胀痛" }, "24.其他：");
		setChoiceEditText(R.id.et_healthState, new String[] { "1满意", "2基本满意 ",
				"3说不清楚", "4不太满意", "5不满意" }, null);
		setChoiceEditText(R.id.et_abilityOfSelf, new String[] { "1 可自理(0～3分)",
				"2轻度依赖(4～8分)", "3 中度依赖（9～18分)", "4 不能自理(≥19分)" }, null);
		setChoiceEditText(R.id.et_cognitiveAbility, new String[] { "1粗筛阴性" },
				"2粗筛阳性， 简易智力状态检查，总分:");
		setChoiceEditText(R.id.et_emotionalStates, new String[] { "1粗筛阴性" },
				"2粗筛阳性， 老年人抑郁评分检查，总分:");
		setChoiceEditText(R.id.et_exerciseFrequency, new String[] { "1每天",
				"2每周一次以上", "3偶尔", "4不锻炼" }, null);
		setChoiceEditText(R.id.et_eatingHabits, new String[] { "1荤素均衡",
				"2荤食为主", "3素食为主", "4嗜盐", "5嗜油", "6嗜糖" }, null);
		setChoiceEditText(R.id.et_smokeState, new String[] { "1从不吸烟", "2已戒烟",
				"3吸烟" }, null);
		setChoiceEditText(R.id.et_freOfDrinking, new String[] { "1从不", "2偶尔",
				"3经常", "4每天" }, null);
		setChoiceEditText(R.id.et_whetherStopDrinking, new String[] { "1为戒酒" },
				"2已戒酒，戒酒年龄：");
		setChoiceEditText(R.id.et_whetherDrinkingRecently, new String[] { "1是",
				"2否" }, null);
		setChoiceEditText(R.id.et_kindsOfDrinks, new String[] { "1白酒", "2啤酒",
				"3红酒", "4黄酒" }, "其他：");
		setChoiceEditText(R.id.et_harmfulFactorsInJob, new String[] { "1无",
				"2化学物质", "3粉尘", "4放射物质", "5物理因素" }, "6其他：");
		setChoiceEditText(R.id.et_lips, new String[] { "1红润", "2苍白", "3发绀",
				"4皲裂", "5疱疹" }, null);
		setChoiceEditText(R.id.et_tooth, new String[] { "1正常", "2缺齿", "3龋齿",
				"4义齿(假牙)" }, null);
		setChoiceEditText(R.id.et_pharyngeal, new String[] { "1无充血", "2充血",
				"3淋巴滤泡增生" }, null);
		setChoiceEditText(R.id.et_audition,
				new String[] { "1听见", "2听不清或无法听见" }, null);
		setChoiceEditText(R.id.et_abilityOfExercise, new String[] { "1可顺利完成",
				"2无法独立完成其中任何一个动作" }, null);
		setChoiceEditText(R.id.et_bottomOfEye, new String[] { "1正常" }, "2异常：");
		setChoiceEditText(R.id.et_skin, new String[] { "1正常", "2潮红", "3苍白",
				"4发绀", "5黄染", "6色素沉着" }, "7其他：");
		setChoiceEditText(R.id.et_sclera, new String[] { "1正常", "2黄染", "3充血" },
				"4其他：");
		setChoiceEditText(R.id.et_lymphonodus, new String[] { "1未触及", "2锁骨上",
				"3腋窝" }, "4其他：");
		setChoiceEditText(R.id.et_tongzhuangxiong, new String[] { "1否", "2是" },
				null);
		setChoiceEditText(R.id.et_huxiyin, new String[] { "1正常" }, "2异常：");
		setChoiceEditText(R.id.et_luoyin,
				new String[] { "1无", "2干罗音", "3湿罗音" }, "4其他：");
		setChoiceEditText(R.id.et_arrhythmia, new String[] { "1齐", "2不齐",
				"3绝对不齐" }, null);
		setChoiceEditText(R.id.et_heartNoise, new String[] { "1无" }, "2有：");
		setChoiceEditText(R.id.et_abdomenYatong, new String[] { "1无" }, "2有：");
		setChoiceEditText(R.id.et_abdomenBaokuai, new String[] { "1无" }, "2有：");
		setChoiceEditText(R.id.et_abdomenGanda, new String[] { "1无" }, "2有：");
		setChoiceEditText(R.id.et_abdomenPida, new String[] { "1无" }, "2有：");
		setChoiceEditText(R.id.et_abdomenZhuoyin, new String[] { "1无" }, "2有：");
		setChoiceEditText(R.id.et_edemaLowerExtremity, new String[] { "1无",
				"2单侧", "3双侧不对称", "4双侧对称" }, null);
		setChoiceEditText(R.id.et_dorsalisPedisArteryPulse, new String[] {
				"1未触及", "2触及双侧对称", "3触及左侧弱或消失", "4触及右侧弱或消失" }, null);
		setChoiceEditText(R.id.et_DRE, new String[] { "1未及异常", "2触痛", "3包块",
				"4前列腺异常" }, "5其他：");
		setChoiceEditText(R.id.et_breast, new String[] { "1未见异常", "2乳房切除",
				"3异常泌乳", "4乳腺包块" }, "5其他：");
		setChoiceEditText(R.id.et_vulva, new String[] { "1未见异常" }, "2异常：");
		setChoiceEditText(R.id.et_vagina, new String[] { "1未见异常" }, "2异常：");
		setChoiceEditText(R.id.et_cervical, new String[] { "1未见异常" }, "2异常：");
		setChoiceEditText(R.id.et_corpus, new String[] { "1未见异常" }, "2异常：");
		setChoiceEditText(R.id.et_adnexa, new String[] { "1未见异常" }, "2异常：");
		setChoiceEditText(R.id.et_bloodSugarUnit, new String[] { "mmol/L",
				"mg/dL" }, null);
		setChoiceEditText(R.id.et_electrocardiogram, new String[] { "1正常" },
				"2异常：");
		setChoiceEditText(R.id.et_SedOccultBlood,
				new String[] { "1阴性", "2阳性" }, null);
		setChoiceEditText(R.id.et_HBsAg, new String[] { "1阴性", "2阳性" }, null);
		setChoiceEditText(R.id.et_chestX_ray, new String[] { "1正常" }, "2异常：");
		setChoiceEditText(R.id.et_BUltrasound, new String[] { "1正常" }, "2异常：");
		setChoiceEditText(R.id.et_cervicalSmear, new String[] { "1正常" }, "2异常：");
		setChoiceEditText(R.id.et_mildPhysical, new String[] { "1是", "2基本是" },
				null);
		setChoiceEditText(R.id.et_faintPhysical, new String[] { "1是", "2倾向是" },
				null);
		setChoiceEditText(R.id.et_yangPhysical, new String[] { "1是", "2倾向是" },
				null);
		setChoiceEditText(R.id.et_yinPhysical, new String[] { "1是", "2倾向是" },
				null);
		setChoiceEditText(R.id.et_phlegmDampnessPhysical, new String[] { "1是",
				"2倾向是" }, null);
		setChoiceEditText(R.id.et_dampnessHeatPhysical, new String[] { "1是",
				"2倾向是" }, null);
		setChoiceEditText(R.id.et_bloodStasisPhysical, new String[] { "1是",
				"2倾向是" }, null);
		setChoiceEditText(R.id.et_qiYuPhysical, new String[] { "1是", "2倾向是" },
				null);
		setChoiceEditText(R.id.et_graspPhysical, new String[] { "1是", "2倾向是" },
				null);
		setChoiceEditText(R.id.et_cerebrovascularDiseases, new String[] {
				"1未发现", "2缺血性卒中", "3脑出血", "4蛛网膜下腔出血", "5短暂性脑缺血" }, "6其他：");
		setChoiceEditText(R.id.et_kidneyDisease, new String[] { "1未发现",
				"2糖尿病肾病", "3肾功能衰竭", "4急性肾炎", "5慢性肾炎" }, "6其他：");
		setChoiceEditText(R.id.et_cardiacDisease, new String[] { "1未发现",
				"2心肌梗死", "3心绞痛", "4冠状动脉血运重建", "5充血性心力衰竭", "6心前区疼痛" }, "7其他：");
		setChoiceEditText(R.id.et_vascularDisease, new String[] { "1未发现",
				"2夹层动脉瘤", "3动脉闭塞性疾病" }, "4其他：");
		setChoiceEditText(R.id.et_oculopathy, new String[] { "1未发现",
				"2视网膜出血或渗出", "3视乳头水肿", "4白内障" }, "5其他：");
		setChoiceEditText(R.id.et_nerveSystemDisease, new String[] { "1未发现" },
				"2有：");
		setChoiceEditText(R.id.et_otherSystemsDisease, new String[] { "1未发现" },
				"2有：");
		setChoiceEditText(R.id.et_healthEvaluation, new String[] { "1体检无异常" },
				"2有异常：");
		setChoiceEditText(R.id.et_healthGuidance, new String[] {
				"1纳入慢性病患者健康管理", "2建议复查", "3建议转诊" }, null);
		setChoiceEditText(R.id.et_riskFactorsControl, new String[] { "1戒烟",
				"2健康饮酒", "3饮食", "4锻炼", "5减体重", "6建议接种疫苗" }, "7其他：");
	}

	protected void setChoiceEditText(int id, String[] items, String editableItem) {
		ChoiceEditText cet = (ChoiceEditText) findViewById(id);
		cet.setFixItems(items);
		if (editableItem != null)
			cet.setEditableItem(editableItem);
	}

	public void saveCheckTable(View view) {
		if (TextUtils.isEmpty(etCheckdate.getText())) {
			Toast.makeText(this, "体检时间不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		long result;
		ContentValues values = getInputData();
		if ("add".equals(action)) {
			result = db.insert(HEALTH_CHECK_TABLE, null, values);
		} else {
			result = db.update(HEALTH_CHECK_TABLE, values,
					HealthCheckTable.USER_ID + "=? and "
							+ HealthCheckTable.SERIAL_ID + "=?", new String[] {
							"123", serialId });
		}

		Intent data = new Intent();
		data.putExtra("result", result);
		setResult(resultCode, data);
		finish();
	}

	private ContentValues getInputData() {
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

		ContentValues values = new ContentValues();
		values.put(HealthCheckTable.NAME, etName.getText().toString());
		values.put(HealthCheckTable.SERIAL_ID, etSerialId.getText().toString());
		values.put(HealthCheckTable.CHECK_DATE, etCheckDate.getText()
				.toString());
		values.put(HealthCheckTable.DOCTOR_NAME, etDoctorName.getText()
				.toString());
		values.put(HealthCheckTable.SYMTOM, etSymtom.getText().toString());
		values.put(HealthCheckTable.TEMPERATURE, etTemperature.getText()
				.toString());
		values.put(HealthCheckTable.PULSE_RATE, etPulseRate.getText()
				.toString());
		values.put(HealthCheckTable.BREATHING_RATE, etBreathingRate.getText()
				.toString());
		values.put(HealthCheckTable.LEFT_BLOOD_PRESSURE, etLeftBloodPressure
				.getText().toString());
		values.put(HealthCheckTable.RIGHT_BLOOD_PRESSURE, etRightBloodPressure
				.getText().toString());
		values.put(HealthCheckTable.HEIGHT, etHeight.getText().toString());
		values.put(HealthCheckTable.WEIGHT, etWeight.getText().toString());
		values.put(HealthCheckTable.WAISTLINE, etWaistline.getText().toString());
		values.put(HealthCheckTable.BMI, etBMI.getText().toString());
		values.put(HealthCheckTable.HEATH_SELFASSESSMENT, etHealthState
				.getText().toString());
		values.put(HealthCheckTable.ABILITY_OF_SELF_SELFASSESSMENT,
				etAbilityOfSelf.getText().toString());
		values.put(HealthCheckTable.COGNITIVE_ABILITY, etCognitiveAbility
				.getText().toString());
		values.put(HealthCheckTable.EMOTIONAL_STATE, etEmotionalStates
				.getText().toString());
		values.put(HealthCheckTable.EXERCISE_FREQUENCY, etExerciseFrequency
				.getText().toString());
		values.put(HealthCheckTable.TIME_PER_EXERCISE, etTimePerExercise
				.getText().toString());
		values.put(HealthCheckTable.TIME_OF_KEEP_EXERCISING,
				etTimeOfKeepExercise.getText().toString());
		values.put(HealthCheckTable.EXERCISE_METHOD, etMethodOfExercise
				.getText().toString());
		values.put(HealthCheckTable.EATING_HABITS, etEatingHabits.getText()
				.toString());
		values.put(HealthCheckTable.SMOKE_STATE, etSmokeState.getText()
				.toString());
		values.put(HealthCheckTable.SMOKE_PER_DAY, etSmokeOfOneDay.getText()
				.toString());
		values.put(HealthCheckTable.AGE_OF_START_SMOKING, etAgeOfStartSmoking
				.getText().toString());
		values.put(HealthCheckTable.AGE_OF_STOP_SMOKING, etAgeOfStopSmoking
				.getText().toString());
		values.put(HealthCheckTable.FREQUENCY_OF_DRINKING, etFreOfDrinking
				.getText().toString());
		values.put(HealthCheckTable.DRINK_PER_DAY, etDrinkPerDay.getText()
				.toString());
		values.put(HealthCheckTable.WHETHER_STOP_DRINKING,
				etWhetherStopDrinking.getText().toString());
		values.put(HealthCheckTable.AGE_OF_START_DRINKING, etAgeOfStartDrinking
				.getText().toString());
		values.put(HealthCheckTable.WHETHER_DRINKING_RECENT,
				etWhetherDrinkingRecently.getText().toString());
		values.put(HealthCheckTable.KINDS_OF_DRINKS, etKindsOfDrinks.getText()
				.toString());
		values.put(HealthCheckTable.HARMFUL_FACTORS_IN_JOB,
				etHarmfulFactorsInJob.getText().toString());
		values.put(HealthCheckTable.LIPS, etLips.getText().toString());
		values.put(HealthCheckTable.TOOTH, etTooth.getText().toString());
		values.put(HealthCheckTable.PHARYNGEAL, etPharyngeal.getText()
				.toString());
		values.put(HealthCheckTable.LEFT_EYESIGHT, etLeftEyesight.getText()
				.toString());
		values.put(HealthCheckTable.RIGHT_EYESIGHT, etRightEyesight.getText()
				.toString());
		values.put(HealthCheckTable.LEFT_CVA, etLeftCVA.getText().toString());
		values.put(HealthCheckTable.RIGHT_CVA, etRightCVA.getText().toString());
		values.put(HealthCheckTable.AUDITION, etAudition.getText().toString());
		values.put(HealthCheckTable.ABILITY_OF_EXERCISE, etAbilityOfExercise
				.getText().toString());
		values.put(HealthCheckTable.BOTTOM_OF_EYE, etBottomOfEye.getText()
				.toString());
		values.put(HealthCheckTable.SKIN, etSkin.getText().toString());
		values.put(HealthCheckTable.SCLERA, etSclera.getText().toString());
		values.put(HealthCheckTable.LYMPHONODUS, etLymphonodus.getText()
				.toString());
		values.put(HealthCheckTable.LUNGS_TONGHUANGXIONG, etTongzhuangxiong
				.getText().toString());
		values.put(HealthCheckTable.LUNGS_BREATHE_SOUND, etHuxiyin.getText()
				.toString());
		values.put(HealthCheckTable.LUNGS_luoyin, etLuoyin.getText().toString());
		values.put(HealthCheckTable.HEART_RATE, etHeartRate.getText()
				.toString());
		values.put(HealthCheckTable.ARRHYTHMIA, etArrhythmia.getText()
				.toString());
		values.put(HealthCheckTable.HEART_NOISE, etHeartNoise.getText()
				.toString());
		values.put(HealthCheckTable.ABDOMEN_YATONG, etAbdomenYatong.getText()
				.toString());
		values.put(HealthCheckTable.ABDOMEN_BAOKUAI, etAbdomenBaokuai.getText()
				.toString());
		values.put(HealthCheckTable.ABDOMEN_GANDA, etAbdomenGanda.getText()
				.toString());
		values.put(HealthCheckTable.ABDOMEN_PIDA, etAbdomenPida.getText()
				.toString());
		values.put(HealthCheckTable.ABDOMEN_ZHUOYIN, etAbdomenZhuoyin.getText()
				.toString());
		values.put(HealthCheckTable.EDEMA_LOWER_EXTREMITY,
				etEdemaLowerExtremity.getText().toString());
		values.put(HealthCheckTable.DORSALIS_PEDIS_ARTERY_PULSE,
				etDorsalisPedisArteryPulse.getText().toString());
		values.put(HealthCheckTable.DRE, etDRE.getText().toString());
		values.put(HealthCheckTable.BREAST, etbreast.getText().toString());
		values.put(HealthCheckTable.VULVA, etvulva.getText().toString());
		values.put(HealthCheckTable.VAGINA, etVagina.getText().toString());
		values.put(HealthCheckTable.CERVICAL, etCervical.getText().toString());
		values.put(HealthCheckTable.CORPUS, etCorpus.getText().toString());
		values.put(HealthCheckTable.ADNEXA, etAdnexa.getText().toString());
		values.put(HealthCheckTable.BODY_OTHERS, etBodyOthers.getText()
				.toString());
		values.put(HealthCheckTable.HAEMOGLOBIN, etHaemoglobin.getText()
				.toString());
		values.put(HealthCheckTable.HEMAMEBA, etHemameba.getText().toString());
		values.put(HealthCheckTable.PLATELET, etPlatelet.getText().toString());
		values.put(HealthCheckTable.BLOOD_ROUTINE_OTHERS, etbloodRoutineOthers
				.getText().toString());
		values.put(HealthCheckTable.URINE_PROTEIN, etUrineProtein.getText()
				.toString());
		values.put(HealthCheckTable.UGLU, etUrineGucose.getText().toString());
		values.put(HealthCheckTable.URINE_ACETONE_BODY, etUrineAcetoneBody
				.getText().toString());
		values.put(HealthCheckTable.URINE_OCCULT_BLOOD, etUrineOccultBlood
				.getText().toString());
		values.put(HealthCheckTable.URINE_ROUTINE_OTHER, etUrineRoutineOthers
				.getText().toString());
		values.put(HealthCheckTable.FBG, etFBG.getText().toString());
		values.put(HealthCheckTable.BLOOD_SUGAR_UNIT, etBloodSugarUnit
				.getText().toString());
		values.put(HealthCheckTable.ELECTROCARDIOGRAM, etElectrocardiogram
				.getText().toString());
		values.put(HealthCheckTable.MICROALBUMINURIA, etMicroalbuminuria
				.getText().toString());
		values.put(HealthCheckTable.SED_OCCULT_BLOOD, etSedOccultBlood
				.getText().toString());
		values.put(HealthCheckTable.GLYCOSYLATED_HEMOGLOBIN,
				etGlycosylatedHemoglobin.getText().toString());
		values.put(HealthCheckTable.HBsAg, etHBsAg.getText().toString());
		values.put(HealthCheckTable.SGPT, etSGPT.getText().toString());
		values.put(HealthCheckTable.SERUM_GLU_OXAL_TRANS, etSerGluOxalTrans
				.getText().toString());
		values.put(HealthCheckTable.LUNGS_ALBUMIN, etLungsAlbumin.getText()
				.toString());
		values.put(HealthCheckTable.LUNGS_TBil, etLungsTBil.getText()
				.toString());
		values.put(HealthCheckTable.LUNGS_CONJ_BILIRUBIN, etLungsConjBilirubin
				.getText().toString());
		values.put(HealthCheckTable.SERUM_CREATININE, etSerumCreatinine
				.getText().toString());
		values.put(HealthCheckTable.BLOOD_UREA_NITROGEN, etBloodUreaNitrogen
				.getText().toString());
		values.put(HealthCheckTable.POTASSIUM_CONCENTRATION,
				etPotassiumConcentration.getText().toString());
		values.put(HealthCheckTable.SERUM_SODIUM_CONCENTRATION,
				etSerumSodiumConcentration.getText().toString());
		values.put(HealthCheckTable.TOTAL_CHOLESTEROL, etTotalCholesterol
				.getText().toString());
		values.put(HealthCheckTable.TRIGLYCERIDE, etTriglyceride.getText()
				.toString());
		values.put(HealthCheckTable.LDLC, etLDLC.getText().toString());
		values.put(HealthCheckTable.HDLC, etHDLC.getText().toString());
		values.put(HealthCheckTable.CHEST_X_RAY, etChestX_ray.getText()
				.toString());
		values.put(HealthCheckTable.B_ULTRASOUND, etBUltrasound.getText()
				.toString());
		values.put(HealthCheckTable.CERVICAL_SMEAR, etCervicalSmear.getText()
				.toString());
		values.put(HealthCheckTable.HELP_CHECK_OTHER, etHelpCheckOther
				.getText().toString());
		values.put(HealthCheckTable.MILD_PHYSICAL, etMildPhysical.getText()
				.toString());
		values.put(HealthCheckTable.FAINT_PHYSICAL, etFaintPhysical.getText()
				.toString());
		values.put(HealthCheckTable.YANG_PHYSICAL, etYangPhysical.getText()
				.toString());
		values.put(HealthCheckTable.YIN_PHYSICAL, etYinPhysical.getText()
				.toString());
		values.put(HealthCheckTable.PHLEGM_DAMPNESS_PHYSICAL,
				etPhlegmDampnessPhysical.getText().toString());
		values.put(HealthCheckTable.DAMPNESS_HEAT_PHYSICAL,
				etDampnessHeatPhysical.getText().toString());
		values.put(HealthCheckTable.BLOOD_STASIS_PHYSICAL,
				etBloodStasisPhysical.getText().toString());
		values.put(HealthCheckTable.QI_YU_PHYSICAL, etQiYuPhysical.getText()
				.toString());
		values.put(HealthCheckTable.GRASP_PHYSICAL, etGraspPhysical.getText()
				.toString());
		values.put(HealthCheckTable.CEREBROVASCULAR_DISEASES,
				etCerebrovascularDiseases.getText().toString());
		values.put(HealthCheckTable.KIDNEY_DISEASE, etKidneyDisease.getText()
				.toString());
		values.put(HealthCheckTable.CARDIAC_DISEASE, etCardiacDisease.getText()
				.toString());
		values.put(HealthCheckTable.VASCULAR_DISEASE, etVascularDisease
				.getText().toString());
		values.put(HealthCheckTable.OCULOPATHY, etOculopathy.getText()
				.toString());
		values.put(HealthCheckTable.NERVE_SYSTEM_DISEASE, etNerveSystemDisease
				.getText().toString());
		values.put(HealthCheckTable.OTHER_SYSTEMS_DISEASE,
				etOtherSystemsDisease.getText().toString());
		values.put(HealthCheckTable.HEALTH_EVALUATION, etHealthEvaluation
				.getText().toString());
		values.put(HealthCheckTable.HEALTH_GUIDANCE, etHealthGuidance.getText()
				.toString());
		values.put(HealthCheckTable.RISK_FACTORS_FOR_CONTROL,
				etRiskFactorsControl.getText().toString());
		values.put(HealthCheckTable.USER_ID, "123");

		return values;
	}

}
