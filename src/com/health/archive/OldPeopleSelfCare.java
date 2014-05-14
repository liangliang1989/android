package com.health.archive;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.younext.R;

import com.health.database.DataOpenHelper;
import com.health.database.Tables;

public class OldPeopleSelfCare extends Fragment {
	// ���� meal
	private RadioGroup mMealRG;
	private RadioButton mMealRG0;
	private RadioButton mMealRG1;
	private RadioButton mMealRG2;	
	private TextView mMealEstimateTV;
	private int mealRate = -1;
	
	// ��ϴ wash
	private RadioGroup mWashRG;
	private RadioButton mWashRG0;
	private RadioButton mWashRG1;
	private RadioButton mWashRG2;
	private RadioButton mWashRG3;
	private TextView mWashEstimateTV;
	private int washRate = -1;
	
	// ���� dress
	private RadioGroup mDressRG;
	private RadioButton mDressRG0;
	private RadioButton mDressRG1;
	private RadioButton mDressRG2;	
	private TextView mDressEstimateTV;
	private int dressRate = -1;
	
	// ��� toilet
	private RadioGroup mToiletRG;
	private RadioButton mToiletRG0;
	private RadioButton mToiletRG1;
	private RadioButton mToiletRG2;
	private RadioButton mToiletRG3;	
	private TextView mToiletEstimateTV;
	private int toiletRate = -1;
	
	// �  exercise
	private RadioGroup mExerciseRG;
	private RadioButton mExerciseRG0;
	private RadioButton mExerciseRG1;
	private RadioButton mExerciseRG2;
	private RadioButton mExerciseRG3;
	private TextView mExerciseEstimateTV;
	private int exerciseRate = -1;

	// ������
	private int totalRate;
	private TextView mTotalEstimateTV;
	private String totalEstimate = null;
	private Button mShowEstimatBtn;
	
	// ��������
	private ContentValues cv = new ContentValues();
	SQLiteDatabase db;
	private String table = "oldPeopleSelfCareEstimate";
	private Button saveTableBtn;
	
	public OldPeopleSelfCare() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		View v = inflater.inflate(R.layout.table_old_person_self_care, null);
		findView(v);
		
		db = new DataOpenHelper(getActivity()).getWritableDatabase();
		
		// ��ѯ������¼�������������
		Cursor cursor = db.rawQuery("select * from oldPeopleSelfCareEstimate " +
				"where user_id = ?", new String[] {"123"}); // ****************need to modify****************

		fillTable(cursor);
		
		cv.put(Tables.USER_ID, "123"); // ****************need to modify****************		
		
		mShowEstimatBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (mealRate >= 0 && washRate >= 0 && dressRate >= 0 
						&& toiletRate >= 0 && exerciseRate >= 0) {
					totalRate = mealRate + washRate + dressRate + toiletRate + exerciseRate;
					
					if (totalRate < 4)
						totalEstimate = totalRate + "\t������";
					else if (totalRate < 9)
						totalEstimate = totalRate + "\t�������";
					else if (totalRate < 19)
						totalEstimate = totalRate + "\t�ж�����";
					else 
						totalEstimate = totalRate + "\t��������";	
					mTotalEstimateTV.setText(totalEstimate);
				} else {
					Toast toast = Toast.makeText(getActivity(), 
							"����δ��������������������", Toast.LENGTH_LONG);
					toast.show();
				}				
			}
		});
		
		// ���� meal��ʹ�ò;߽���������ڡ��׽������ʵȻ
		
		mMealRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.meal_rg0:
					mMealEstimateTV.setText("���֣�0\t������");
					mealRate = 0;
					cv.put(Tables.MEAL, mMealRG0.getText().toString());
					break;
				case R.id.meal_rg1:
					mMealEstimateTV.setText("���֣�3\t�ж�����");
					mealRate = 3;
					cv.put(Tables.MEAL, mMealRG1.getText().toString());
					break;
				case R.id.meal_rg2:
					mMealEstimateTV.setText("���֣�5\t��������");
					mealRate = 5;
					cv.put(Tables.MEAL, mMealRG2.getText().toString());
					break;
				default:
					break;
				}	
			}
		});
		
		// ��ϴ wash����ͷ��ϴ����ˢ��������ϴ��Ȼ
		
		mWashRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.wash_rg0:
					mWashEstimateTV.setText("���֣�0\t������");
					washRate = 0;
					cv.put(Tables.WASH, mWashRG0.getText().toString());
					break;
				case R.id.wash_rg1:
					mWashEstimateTV.setText("���֣�1\t�������");
					washRate = 1;
					cv.put(Tables.WASH, mWashRG1.getText().toString());
					break;
				case R.id.wash_rg2:
					mWashEstimateTV.setText("���֣�3\t�ж�����");
					washRate = 3;
					cv.put(Tables.WASH, mWashRG2.getText().toString());
					break;
				case R.id.wash_rg3:
					mWashEstimateTV.setText("���֣�7\t��������");
					washRate = 7;
					cv.put(Tables.WASH, mWashRG3.getText().toString());
					break;
				default:
					break;
				}				
			}
		});
		
		// ���� dress�����¿㡢���ӡ�Ь�ӵȻ
		
		mDressRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.dress_rg0:
					mDressEstimateTV.setText("���֣�0\t������");
					dressRate = 0;
					cv.put(Tables.DRESS, mDressRG0.getText().toString());
					break;
				case R.id.dress_rg1:
					mDressEstimateTV.setText("���֣�3\t�ж�����");
					dressRate = 3;
					cv.put(Tables.DRESS, mDressRG1.getText().toString());
					break;
				case R.id.dress_rg2:
					mDressEstimateTV.setText("���֣�5\t��������");
					dressRate = 5;
					cv.put(Tables.DRESS, mDressRG2.getText().toString());
					break;
				default:
					break;
				}
				
			}
		});
		
		// ��� toilet��С�㡢���Ȼ���Կ�
		
		mToiletRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.toilet_rg0:
					mToiletEstimateTV.setText("���֣�0\t������");
					toiletRate = 0;
					cv.put(Tables.TOILET, mToiletRG0.getText().toString());
					break;
				case R.id.toilet_rg1:
					mToiletEstimateTV.setText("���֣�1\t�������");
					toiletRate = 1;		
					cv.put(Tables.TOILET, mToiletRG1.getText().toString());
					break;
				case R.id.toilet_rg2:
					mToiletEstimateTV.setText("���֣�5\t�ж�����");
					toiletRate = 5;					
					cv.put(Tables.TOILET, mToiletRG2.getText().toString());
					break;
				case R.id.toilet_rg3:
					mToiletEstimateTV.setText("���֣�10\t��������");
					toiletRate = 10;				
					cv.put(Tables.TOILET, mToiletRG3.getText().toString());
					break;
				default:
					break;
				}				
			}
		});
		
		// �  exercise��վ�����������ߡ�����¥�ݡ�����
		
		mExerciseRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.exercise_rg0:
					mExerciseEstimateTV.setText("���֣�0\t������");
					exerciseRate = 0;
					cv.put(Tables.EXERCISE, mExerciseRG0.getText().toString());
					break;
				case R.id.exercise_rg1:
					mExerciseEstimateTV.setText("���֣�1\t�������");
					exerciseRate = 1;
					cv.put(Tables.EXERCISE, mExerciseRG1.getText().toString());
					break;
				case R.id.exercise_rg2:
					mExerciseEstimateTV.setText("���֣�5\t�ж�����");
					exerciseRate = 5;
					cv.put(Tables.EXERCISE, mExerciseRG2.getText().toString());
					break;
				case R.id.exercise_rg3:
					mExerciseEstimateTV.setText("���֣�10\t��������");
					exerciseRate = 10;
					cv.put(Tables.EXERCISE, mExerciseRG3.getText().toString());
					break;
				default:
					break;
				}				
			}
		});
		
		saveTableBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (mealRate >= 0 && washRate >= 0 && dressRate >= 0 
						&& toiletRate >= 0 && exerciseRate >= 0) {
					
					if (totalEstimate == null) {
						totalRate = mealRate + washRate + dressRate + toiletRate + exerciseRate;					
						if (totalRate < 4)
							totalEstimate = totalRate + "\t������";
						else if (totalRate < 9)
							totalEstimate = totalRate + "\t�������";
						else if (totalRate < 19)
							totalEstimate = totalRate + "\t�ж�����";
						else 
							totalEstimate = totalRate + "\t��������";
					}
						
					cv.put(Tables.TOTAL_ESTIMATE, totalEstimate);					
					
					if (db.insert(table, null, cv) == -1)
						Toast.makeText(getActivity(), "����ʧ��", Toast.LENGTH_LONG).show();
					else {
						Toast.makeText(getActivity(), "����ɹ�", Toast.LENGTH_LONG).show();
						Log.i("����", cv.valueSet().toString());
					}
				} else {
					Toast toast = Toast.makeText(getActivity(), 
							"����δ��������������������", Toast.LENGTH_LONG);
					toast.show();
				}
				
			}
		});
		
		return v;
	}
	
	private void findView(View v) {
		mMealRG0 = (RadioButton) v.findViewById(R.id.meal_rg0);
		mMealRG1 = (RadioButton) v.findViewById(R.id.meal_rg1);
		mMealRG2 = (RadioButton) v.findViewById(R.id.meal_rg2);
		
		mWashRG0 = (RadioButton) v.findViewById(R.id.wash_rg0);
		mWashRG1 = (RadioButton) v.findViewById(R.id.wash_rg1);
		mWashRG2 = (RadioButton) v.findViewById(R.id.wash_rg2);
		mWashRG3 = (RadioButton) v.findViewById(R.id.wash_rg3);
		
		mDressRG0 = (RadioButton) v.findViewById(R.id.dress_rg0);
		mDressRG1 = (RadioButton) v.findViewById(R.id.dress_rg1);
		mDressRG2 = (RadioButton) v.findViewById(R.id.dress_rg2);	
		
		mToiletRG0 = (RadioButton) v.findViewById(R.id.toilet_rg0);
		mToiletRG1 = (RadioButton) v.findViewById(R.id.toilet_rg1);
		mToiletRG2 = (RadioButton) v.findViewById(R.id.toilet_rg2);
		mToiletRG3 = (RadioButton) v.findViewById(R.id.toilet_rg3);
		
		mExerciseRG0 = (RadioButton) v.findViewById(R.id.exercise_rg0);
		mExerciseRG1 = (RadioButton) v.findViewById(R.id.exercise_rg1);
		mExerciseRG2 = (RadioButton) v.findViewById(R.id.exercise_rg2);
		mExerciseRG3 = (RadioButton) v.findViewById(R.id.exercise_rg3);
		
		saveTableBtn = (Button) v.findViewById(R.id.save_table_btn);
		mTotalEstimateTV = (TextView) v.findViewById(R.id.total_estimate_tv);
		mShowEstimatBtn = (Button) v.findViewById(R.id.show_estimate_btn);
		mMealRG = (RadioGroup)v.findViewById(R.id.meal_rg);
		mMealEstimateTV = (TextView)v.findViewById(R.id.meal_estimate_tv);
		mWashRG = (RadioGroup)v.findViewById(R.id.wash_rg);
		mWashEstimateTV = (TextView) v.findViewById(R.id.wash_estimate_tv);
		mDressRG = (RadioGroup) v.findViewById(R.id.dress_rg);
		mDressEstimateTV = (TextView) v.findViewById(R.id.dress_estimate_tv);
		mToiletRG = (RadioGroup) v.findViewById(R.id.toilet_rg);
		mToiletEstimateTV = (TextView) v.findViewById(R.id.toilet_estimate_tv);
		mExerciseEstimateTV = (TextView) v.findViewById(R.id.exercise_estimate_tv);
		mExerciseRG = (RadioGroup) v.findViewById(R.id.exercise_rg);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", 4);
	}
	
	private void fillTable(Cursor cursor) {
		if (cursor.moveToNext()) {
			int mealCol = cursor.getColumnIndex(Tables.MEAL);
			int washCol = cursor.getColumnIndex(Tables.WASH);
			int dressCol = cursor.getColumnIndex(Tables.DRESS);
			int toiletCol = cursor.getColumnIndex(Tables.TOILET);
			int exerciseCol = cursor.getColumnIndex(Tables.EXERCISE);
			int totalEstimateCol = cursor.getColumnIndex(Tables.TOTAL_ESTIMATE);
			
			
			// meal
			if (mealCol != -1) {
				switch (cursor.getString(mealCol).charAt(0) - 'A') {
				case 0:
					mMealRG0.setChecked(true);
					break;
				case 1:
					mMealRG1.setChecked(true);
					break;
				case 2:
					mMealRG2.setChecked(true);
					break;			
				default:
					break;
				}
			}
			
			// wash
			if (washCol != -1) {
				switch (cursor.getString(washCol).charAt(0) - 'A') {
				case 0:
					mWashRG0.setChecked(true);
					break;
				case 1:
					mWashRG1.setChecked(true);
					break;
				case 2:
					mWashRG2.setChecked(true);
					break;
				case 3:
					mWashRG3.setChecked(true);
					break;
				default:
					break;
				}
			}
			
			// dress
			if (dressCol != -1) {
				switch (cursor.getString(dressCol).charAt(0) - 'A') {
				case 0:
					mDressRG0.setChecked(true);
					break;
				case 1:
					mDressRG1.setChecked(true);
					break;
				case 2:
					mDressRG2.setChecked(true);
					break;
				default:
					break;
				}
			}
			
			// toilet
			if (toiletCol != -1) {
				switch (cursor.getString(toiletCol).charAt(0) - 'A') {
				case 0:
					mToiletRG0.setChecked(true);
					break;
				case 1:
					mToiletRG1.setChecked(true);
					break;
				case 2:
					mToiletRG2.setChecked(true);
					break;
				case 3:
					mToiletRG3.setChecked(true);
					break;			
				default:
					break;
				}
			}
			
			// exercise
			if (exerciseCol != -1) {
				switch (cursor.getString(exerciseCol).charAt(0) - 'A') {
				case 0:
					mExerciseRG0.setChecked(true);
					break;
				case 1:
					mExerciseRG1.setChecked(true);
					break;
				case 2:
					mExerciseRG2.setChecked(true);
					break;
				case 3:
					mExerciseRG3.setChecked(true);
					break;			
				default:
					break;
				}
			}
			
			// totalEstimate
			if (totalEstimateCol != -1) {
				mTotalEstimateTV.setText(cursor.getString(totalEstimateCol));
			}
		}
	}
}
