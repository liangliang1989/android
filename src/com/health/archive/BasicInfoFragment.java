package com.health.archive;

import com.health.archive.ArchiveMain.ActionBarEditable;
import com.health.database.CoverTable;
import com.health.database.DataOpenHelper;
import com.health.database.PersonalBasicInforTable;
import com.health.viewUtil.ChoiceEditText;

import cn.younext.R;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BasicInfoFragment extends Fragment {
	private View v;
	private EditText nameEditText;
	private EditText userIdEditText;
	private EditText genderEditText;
	private EditText birthEditText;
	private EditText idNumberEditText;
	private EditText companyEditText;
	private EditText selfPhoneEditText;
	private EditText contactNameEditText;
	private EditText contactPhoneEditText;
	private EditText permanentTypeEditText;
	private EditText nationEditText;
	private EditText bloodTypeEditText;
	private EditText rHTypeEditText;
	private EditText maritalStatusEditText;
	private EditText educationDegreeEditText;
	private EditText jobsEditText;
	private EditText paymentTypeEditText;
	private EditText allergicHistoryEditText;
	private EditText exposeHistoryEditText;
	private EditText illnessEditText;
	private EditText operationEditText;
	private EditText traumaEditText;
	private EditText transfusionEditText;
	private EditText fatherHistoryEditText;
	private EditText motherHistoryEditText;
	private EditText brotherSisterHistoryEditText;
	private EditText childernHistoryEditText;
	private EditText genopathyEditText;
	private EditText deformityStateEditText;
	private EditText airExhaustEditText;
	private EditText fuleTypeEditText;
	private EditText waterTypeEditText;
	private EditText toiletTypeEditText;
	private EditText animalHomeEditText;
	
	private SQLiteDatabase db;
	private Cursor cursor;
	private boolean isExist;
	private static final String TABLE_NAME = "BASICINFOR";

	public BasicInfoFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.health_archivel_basic_infor, null);
		ArchiveMain.getInstance().setTitle("���˻�����Ϣ");
		ArchiveMain.getInstance().setButtonText("����");
		findView();
		initView();
		
		db = new DataOpenHelper(getActivity()).getWritableDatabase();
		cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + PersonalBasicInforTable.USER_ID + "=?" ,
				new String[] {"123"}); //д��
		if (cursor.moveToNext()) {
			isExist = true;
			fillTable();
		}
		
		ArchiveMain.getInstance().setActionBarEdit(new ActionBarEditable() {

			@Override
			public void processOnButton() {
				ContentValues values = getValues();
				if (isExist) {
					int result = db.update(TABLE_NAME, values, PersonalBasicInforTable.USER_ID + "=?",
							new String[] {"123"});
					if (result == -1) {
						Toast.makeText(getActivity(), "����ʧ��", Toast.LENGTH_LONG).show();
						Log.i("������Ϣ", values.toString());
					} else 
						Toast.makeText(getActivity(), "���³ɹ�", Toast.LENGTH_LONG).show();
				} else {
					int result = (int)db.insert(TABLE_NAME, null, values);
					if (result == -1) {
						Toast.makeText(getActivity(), "����ʧ��", Toast.LENGTH_LONG).show();
						Log.i("������Ϣ", values.toString());
					} else {
						Toast.makeText(getActivity(), "����ɹ�", Toast.LENGTH_LONG).show();
					}
				}

			}
		});
		
		
		return v;
	}
	
	private void findView() {
		nameEditText = (EditText) v.findViewById(R.id.et_name);
		userIdEditText = (EditText) v.findViewById(R.id.et_userId);
		genderEditText = (EditText) v.findViewById(R.id.set_gender);
		birthEditText = (EditText) v.findViewById(R.id.et_birth);
		idNumberEditText = (EditText) v.findViewById(R.id.et_idNUmber);
		companyEditText = (EditText) v.findViewById(R.id.et_company);
		selfPhoneEditText = (EditText) v.findViewById(R.id.et_selfPhone);
		contactNameEditText = (EditText) v.findViewById(R.id.et_contactName);
		contactPhoneEditText = (EditText) v.findViewById(R.id.et_contactPhone);
		permanentTypeEditText = (EditText) v.findViewById(R.id.set_permanentType);
		nationEditText = (EditText) v.findViewById(R.id.set_nation);
		bloodTypeEditText = (EditText) v.findViewById(R.id.set_bloodType);
		rHTypeEditText = (EditText) v.findViewById(R.id.set_RHType);
		maritalStatusEditText = (EditText) v.findViewById(R.id.set_maritalStatus);
		educationDegreeEditText = (EditText) v.findViewById(R.id.set_educationDegree);
		jobsEditText = (EditText) v.findViewById(R.id.set_jobs);
		paymentTypeEditText = (EditText) v.findViewById(R.id.met_paymentType);
		allergicHistoryEditText = (EditText) v.findViewById(R.id.met_allergicHistory);
		exposeHistoryEditText = (EditText) v.findViewById(R.id.met_exposeHistory);
		illnessEditText = (EditText) v.findViewById(R.id.met_illness);
		operationEditText = (EditText) v.findViewById(R.id.set_operation);
		traumaEditText = (EditText) v.findViewById(R.id.set_trauma);
		transfusionEditText = (EditText) v.findViewById(R.id.set_transfusion);
		fatherHistoryEditText = (EditText) v.findViewById(R.id.met_fatherHistory);
		motherHistoryEditText = (EditText) v.findViewById(R.id.met_motherHistory);
		brotherSisterHistoryEditText = (EditText) v.findViewById(R.id.met_brothersSistersHistory);
		childernHistoryEditText = (EditText) v.findViewById(R.id.met_childrenHistory);
		genopathyEditText = (EditText) v.findViewById(R.id.set_genopathy);
		deformityStateEditText = (EditText) v.findViewById(R.id.set_deformityState);
		airExhaustEditText = (EditText) v.findViewById(R.id.set_airExhaust);
		fuleTypeEditText = (EditText) v.findViewById(R.id.set_fuleType);
		waterTypeEditText = (EditText) v.findViewById(R.id.set_waterType);
		toiletTypeEditText = (EditText) v.findViewById(R.id.set_toiletType);
		animalHomeEditText = (EditText)v.findViewById(R.id.set_animalHome);
		
		
	}

	protected void initView() {
		setChoiceEditText(R.id.set_gender, new String[] { "0.δ֪���Ա�", "1.��",
				"2.Ů", "3.δ˵�����Ա�" }, null);
		setChoiceEditText(R.id.set_permanentType, new String[] { "0.����",
				"1.�ǻ���" }, null);
		setChoiceEditText(R.id.set_nation, new String[] { "0.����" }, "�������壺");
		setChoiceEditText(R.id.set_bloodType, new String[] { "0.A��", "1.B��",
				"2.O��", "3.AB��", "4.����" }, null);
		setChoiceEditText(R.id.set_RHType,
				new String[] { "0.��", "1.��", "2.����" }, null);
		setChoiceEditText(R.id.set_educationDegree, new String[] { "0.��ä������ä",
				"1.Сѧ", "2.����", "3.����/��У/��ר", "4.��ѧ���Ƽ�����" }, null);
		setChoiceEditText(R.id.set_jobs, new String[] {
				"0.���һ��ء���Ⱥ��֯����ҵ����ҵ��λ������", "1.רҵ������Ա", "2.������Ա���й���Ա",
				"3.��ҵ������ҵ��Ա", "4.ũ���֡������桢ˮ��ҵ������Ա", "5.�����������豸������Ա���й���Ա",
				"6.����", "7.��������������ҵ��Ա" }, null);
		setChoiceEditText(R.id.set_maritalStatus, new String[] { "0.δ��",
				"1.�ѻ�", "2.ɥż", "3.���", "4.δ˵���Ļ���״��" }, null);
		setChoiceEditText(R.id.met_paymentType, new String[] { "0.����ְ������ҽ�Ʊ���",
				"1.����������ҽ�Ʊ���", "2.����ũ�����ҽ��", "3.ƶ������", "4.��ҵҽ�Ʊ���", "5.ȫ����",
				"6.ȫ�Է�" }, "7.������");
		setChoiceEditText(R.id.met_illness, new String[] { "0.��", "1.��Ѫѹ",
				"2.����", "3.���Ĳ�", "4.���������Էμ���", "5.��������", "6.������", "7.���Ծ��񼲲�",
				"8.��˲�", "9.����", "10.����������Ⱦ��", "11.ְҵ��" }, "14.������");
		setChoiceEditText(R.id.met_exposeHistory, new String[] { "0.��",
				"1.��ѧƷ", "2.����", "3.����" }, null);
		setChoiceEditText(R.id.met_allergicHistory, new String[] { "0.��",
				"1.��ù��", "2.�ǰ�", "3.��ù��" }, "4.������");
		setChoiceEditText(R.id.set_operation, new String[] { "0.��" }, "1.�У�ԭ����");
		setChoiceEditText(R.id.set_trauma, new String[] { "0.��" }, "1.�У�ԭ����");
		setChoiceEditText(R.id.set_transfusion, new String[] { "0.��" },
				"1.�У�ԭ����");
		setChoiceEditText(R.id.met_fatherHistory, new String[] { "0.��",
				"1.��Ѫѹ", "2.����", "3.���Ĳ�", "4.���������Էμ���", "5.��������", "6.������",
				"7.���Ծ��񼲲�", "8.��˲�", "9.����", "10.�������", "11.����" }, null);
		setChoiceEditText(R.id.met_motherHistory, new String[] { "0.��",
				"1.��Ѫѹ", "2.����", "3.���Ĳ�", "4.���������Էμ���", "5.��������", "6.������",
				"7.���Ծ��񼲲�", "8.��˲�", "9.����", "10.�������", "11.����" }, null);
		setChoiceEditText(R.id.met_brothersSistersHistory, new String[] {
				"0.��", "1.��Ѫѹ", "2.����", "3.���Ĳ�", "4.���������Էμ���", "5.��������",
				"6.������", "7.���Ծ��񼲲�", "8.��˲�", "9.����", "10.�������", "11.����" },
				null);
		setChoiceEditText(R.id.met_childrenHistory, new String[] { "0.��",
				"1.��Ѫѹ", "2.����", "3.���Ĳ�", "4.���������Էμ���", "5.��������", "6.������",
				"7.���Ծ��񼲲�", "8.��˲�", "9.����", "10.�������", "11.����" }, null);
		setChoiceEditText(R.id.set_genopathy, new String[] { "0.��" },
				"1.�У���������");
		setChoiceEditText(R.id.set_deformityState, new String[] { "0.�޲м�",
				"1.�����м�", "2.�����м�", "3.����м�", "4.֫��м�", "5.�����м�", "6.����м�" },
				"7.�����м���");
		setChoiceEditText(R.id.set_airExhaust, new String[]{"0.��", "1.���̻�", "2.������", "3.�̴�"}, null);
		setChoiceEditText(R.id.set_fuleType, new String[]{"0.Һ����", "1.ú", "2.��Ȼ��", "3.����", "4.���", "5.����"}, null);
		setChoiceEditText(R.id.set_waterType, new String[]{"0.����ˮ", "1.���������˵�ˮ", "2.��ˮ", "3.�Ӻ�ˮ","4.��ˮ", "5.����"}, null);
		setChoiceEditText(R.id.set_toiletType, new String[]{"0.��������", "1.һ��������ʽ", "2.��Ͱ", "3.¶����", "4.�������"}, null);
		setChoiceEditText(R.id.set_animalHome, new String[]{"0.����", "1.����", "2.����"}, null);
	}
	
	private void fillTable() {
		int nameCol = cursor.getColumnIndex(PersonalBasicInforTable.NAME);
		int userIdCol = cursor.getColumnIndex(PersonalBasicInforTable.USER_ID);
		int genderCol = cursor.getColumnIndex(PersonalBasicInforTable.GENDER);
		int birthCol = cursor.getColumnIndex(PersonalBasicInforTable.BIRTH);
		int idNumberCol = cursor.getColumnIndex(PersonalBasicInforTable.ID_NUMBER);
		int companyCol = cursor.getColumnIndex(PersonalBasicInforTable.COMPANY);
		int selfPhoneCol = cursor.getColumnIndex(PersonalBasicInforTable.SELF_PHONE);
		int contactNameCol = cursor.getColumnIndex(PersonalBasicInforTable.CONTACT_NAME);
		int contactPhoneCol = cursor.getColumnIndex(PersonalBasicInforTable.CONTACT_PHONE);
		int permanentTypeCol = cursor.getColumnIndex(PersonalBasicInforTable.PERMANENT_TYPE);
		int nationCol = cursor.getColumnIndex(PersonalBasicInforTable.NATION);
		int bloodTypeCol = cursor.getColumnIndex(PersonalBasicInforTable.BLOOD_TYPE);
		int rhTypeCol = cursor.getColumnIndex(PersonalBasicInforTable.RH_TYPE);
		int maritalStatusCol = cursor.getColumnIndex(PersonalBasicInforTable.MARITALSTATUS);
		int educationDegreeCol = cursor.getColumnIndex(PersonalBasicInforTable.EDUCATION_DEGREE);
		int jobsCol = cursor.getColumnIndex(PersonalBasicInforTable.JOB);
		int paymentCol = cursor.getColumnIndex(PersonalBasicInforTable.METHOD_OF_PAYMENT);
		int allergicHistoryCol = cursor.getColumnIndex(PersonalBasicInforTable.ALLERGIC_HISTORY);
		int exposeHistoryCol = cursor.getColumnIndex(PersonalBasicInforTable.EXPOSE_HISTORY);
		int illnessCol = cursor.getColumnIndex(PersonalBasicInforTable.ILLNESS);
		int operationCol = cursor.getColumnIndex(PersonalBasicInforTable.OPERATION);
		int traumaCol = cursor.getColumnIndex(PersonalBasicInforTable.TRAUMA);
		int transCol = cursor.getColumnIndex(PersonalBasicInforTable.TRANSFUSION);
		int fatherCol = cursor.getColumnIndex(PersonalBasicInforTable.FATHER_HISTORY);
		int motherCol = cursor.getColumnIndex(PersonalBasicInforTable.MOTHER_HISTORY);
		int broSisCol = cursor.getColumnIndex(PersonalBasicInforTable.BROTHERS_SISTERS_HISTORY);
		int childrenCol = cursor.getColumnIndex(PersonalBasicInforTable.CHILDREN_HISTORY);
		int genopathyCol = cursor.getColumnIndex(PersonalBasicInforTable.GENOPATHY);
		int deformityStateCol = cursor.getColumnIndex(PersonalBasicInforTable.DEFORMITY_STATE);
		int airExhaustCol = cursor.getColumnIndex(PersonalBasicInforTable.AIR_EXHAUST);
		int fuleTypeCol = cursor.getColumnIndex(PersonalBasicInforTable.FULE_TYPE);
		int waterTypeCol = cursor.getColumnIndex(PersonalBasicInforTable.WATER_TYPE);
		int toiletCol = cursor.getColumnIndex(PersonalBasicInforTable.TOILET_TYPE);
		int animalHomeCol = cursor.getColumnIndex(PersonalBasicInforTable.ANIMAL_HOME);
		
		nameEditText.setText(cursor.getString(nameCol));
		userIdEditText.setText(cursor.getString(userIdCol));
		genderEditText.setText(cursor.getString(genderCol));
		birthEditText.setText(cursor.getString(birthCol));
		idNumberEditText.setText(cursor.getString(idNumberCol));
		companyEditText.setText(cursor.getString(companyCol));
		selfPhoneEditText.setText(cursor.getString(selfPhoneCol));
		contactNameEditText.setText(cursor.getString(contactNameCol));
		contactPhoneEditText.setText(cursor.getString(contactPhoneCol));
		permanentTypeEditText.setText(cursor.getString(permanentTypeCol));
		nationEditText.setText(cursor.getString(nationCol));
		bloodTypeEditText.setText(cursor.getString(bloodTypeCol));
		rHTypeEditText.setText(cursor.getString(rhTypeCol));
		maritalStatusEditText.setText(cursor.getString(maritalStatusCol));
		educationDegreeEditText.setText(cursor.getString(educationDegreeCol));
		jobsEditText.setText(cursor.getString(jobsCol));
		paymentTypeEditText.setText(cursor.getString(paymentCol));
		allergicHistoryEditText.setText(cursor.getString(allergicHistoryCol));
		exposeHistoryEditText.setText(cursor.getString(exposeHistoryCol));
		illnessEditText.setText(cursor.getString(illnessCol));
		operationEditText.setText(cursor.getString(operationCol));
		traumaEditText.setText(cursor.getString(traumaCol));
		transfusionEditText.setText(cursor.getString(transCol));
		fatherHistoryEditText.setText(cursor.getString(fatherCol));
		motherHistoryEditText.setText(cursor.getString(motherCol));
		brotherSisterHistoryEditText.setText(cursor.getString(broSisCol));
		childernHistoryEditText.setText(cursor.getString(childrenCol));
		genopathyEditText.setText(cursor.getString(genopathyCol));
		deformityStateEditText.setText(cursor.getString(deformityStateCol));
		airExhaustEditText.setText(cursor.getString(airExhaustCol));
		fuleTypeEditText.setText(cursor.getString(fuleTypeCol));
		waterTypeEditText.setText(cursor.getString(waterTypeCol));
		toiletTypeEditText.setText(cursor.getString(toiletCol));
		animalHomeEditText.setText(cursor.getString(animalHomeCol));
 }
	
	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(PersonalBasicInforTable.NAME, nameEditText.getText().toString());
		values.put(PersonalBasicInforTable.USER_ID, userIdEditText.getText().toString());
		values.put(PersonalBasicInforTable.GENDER, genderEditText.getText().toString());
		values.put(PersonalBasicInforTable.BIRTH, birthEditText.getText().toString());
		values.put(PersonalBasicInforTable.ID_NUMBER, idNumberEditText.getText().toString());
		values.put(PersonalBasicInforTable.COMPANY, companyEditText.getText().toString());
		values.put(PersonalBasicInforTable.SELF_PHONE, selfPhoneEditText.getText().toString());
		values.put(PersonalBasicInforTable.CONTACT_NAME, contactNameEditText.getText().toString());
		values.put(PersonalBasicInforTable.CONTACT_PHONE, contactPhoneEditText.getText().toString());
		values.put(PersonalBasicInforTable.PERMANENT_TYPE, permanentTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.NATION, nationEditText.getText().toString());
		values.put(PersonalBasicInforTable.BLOOD_TYPE, bloodTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.RH_TYPE, rHTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.MARITALSTATUS, maritalStatusEditText.getText().toString());
		values.put(PersonalBasicInforTable.EDUCATION_DEGREE, educationDegreeEditText.getText().toString());
		values.put(PersonalBasicInforTable.JOB, jobsEditText.getText().toString());
		values.put(PersonalBasicInforTable.METHOD_OF_PAYMENT, paymentTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.ALLERGIC_HISTORY, allergicHistoryEditText.getText().toString());
		values.put(PersonalBasicInforTable.EXPOSE_HISTORY, exposeHistoryEditText.getText().toString());
		values.put(PersonalBasicInforTable.ILLNESS, illnessEditText.getText().toString());
		values.put(PersonalBasicInforTable.OPERATION, operationEditText.getText().toString());
		values.put(PersonalBasicInforTable.TRAUMA, traumaEditText.getText().toString());
		values.put(PersonalBasicInforTable.TRANSFUSION, transfusionEditText.getText().toString());
		values.put(PersonalBasicInforTable.FATHER_HISTORY, fatherHistoryEditText.getText().toString());
		values.put(PersonalBasicInforTable.MOTHER_HISTORY, motherHistoryEditText.getText().toString());
		values.put(PersonalBasicInforTable.BROTHERS_SISTERS_HISTORY, brotherSisterHistoryEditText.getText().toString());
		values.put(PersonalBasicInforTable.CHILDREN_HISTORY, childernHistoryEditText.getText().toString());
		values.put(PersonalBasicInforTable.GENOPATHY, genopathyEditText.getText().toString());
		values.put(PersonalBasicInforTable.DEFORMITY_STATE, deformityStateEditText.getText().toString());
		values.put(PersonalBasicInforTable.AIR_EXHAUST, airExhaustEditText.getText().toString());
		values.put(PersonalBasicInforTable.FULE_TYPE, fuleTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.WATER_TYPE, waterTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.TOILET_TYPE, toiletTypeEditText.getText().toString());
		values.put(PersonalBasicInforTable.ANIMAL_HOME, animalHomeEditText.getText().toString());
		return values;
	}

	protected void setChoiceEditText(int id, String[] items, String editableItem) {
		ChoiceEditText cet = (ChoiceEditText) v.findViewById(id);
		cet.setFixItems(items);
		if (editableItem != null)
			cet.setEditableItem(editableItem);
	}

}