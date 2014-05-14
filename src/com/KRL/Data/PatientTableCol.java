package com.KRL.Data;

import android.provider.BaseColumns;
public class PatientTableCol implements BaseColumns
{
	// �ֶ���
	public static final String	ACCOUNT					= "account";			// �ʺ�
	public static final String	CHECKSTATIONID			= "checkstationid";	// ���վID
	public static final String	DEPARTMENT				= "department";		// ���ſ���
	public static final String	NAME					= "name";				// ����
	public static final String	SEX						= "sex";				// �Ա�
	public static final String	AGE						= "age";				// ����
	public static final String	WEIGHT					= "weight";			// ����
	public static final String	HEIGHT					= "height";			// ���
	public static final String	OUTPATIENTNUMBER		= "outpatientnumber";	// �����
	public static final String	HOSPITALNUMBER			= "hospitalnumber";	// סԺ��
	public static final String	BEDNUMBER				= "bednumber";			// ����
	public static final String	SYMPTOM					= "symptom";			// ֢״
	public static final String	PHONE					= "phone";				// �绰
	public static final String	ADDRESS					= "address";			// ��ַ
	public static final String	POSTCODE				= "postcode";			// �ʱ�
	public static final String	CREATEDTM				= "createdtm";			// ����ʱ��
	public static final String	PERSONREMARK			= "personremark";		// ��Ա��ע
	public static final String	RELATIVEPATH			= "relativepath";		// ���·��
	public static final String	FILENAME				= "filename";			// �ļ�����
	public static final String	FILELENGTH				= "filelength";		// �ļ�����
	public static final String	FILEDESP				= "filedesp";			// �ļ�����
	public static final String	OCCURDTM				= "occurdtm";			// ����ʱ��
	public static final String	HANDLESTATE				= "handlestate";		// ����״̬
	public static final String	STARTHANDLEDTM			= "starthandledtm";	// ���洦��ʼʱ��
	public static final String	ENDHANDLEDTM			= "endhandledtm";		// ���洦�����ʱ��
	public static final String	DIAGNOSE				= "diagnose";			// ���
	public static final String	DATAPATH				= "datapath";			// ����ȫ·��
	public static final String	DATAREMARK				= "dataremark";		// ��ע
	public static final String	HANDLEID				= "handleid";			// ����ID
	public static final String	DEL						= "del";				// ɾ����ʶ
	public static final String	PERSONDATAID			= "persondataid";		// ��������Ա����ID,������Ϊ��Ա����ͬ����ʶ
	public static final String	DATASYNCFLAG			= "datasyncflag";		// ����ͬ����ʶ,0δͬ��,1�Ѿ�ͬ��
	public static final String	STATESYNCFLAG			= "statesyncflag";		// ״̬ͬ����ʶ,0�ü�¼�����߲���δ�ύ,1�Ѿ��ύ
	public static final String	DATASTATE				= "datastate";			// ����״̬:0δ�ɼ�,1�Ѿ��ɼ�,2��Ҫ�ز�,3�Ѿ��ز�
	public static final String	PERSONRECORDDTM			= "personrecorddtm";	// ��Ϣͬ��ʱ��
	public static final String	SAMPLINGSATE			= "samplingstate";		// ���ݲɼ�״̬
	public static final String	REPORTCOLLECTDTM		= "reportcollectdtm";	// �ϴ������ļ���ʱ��
																				// �ֶ�����
	public static final int		_ID_COL					= 0;
	public static final int		ACCOUNT_COL				= 1;
	public static final int		CHECKSTATIONID_COL		= 2;
	public static final int		DEPARTMENT_COL			= 3;
	public static final int		NAME_COL				= 4;
	public static final int		SEX_COL					= 5;
	public static final int		AGE_COL					= 6;
	public static final int		WEIGHT_COL				= 7;
	public static final int		HEIGHT_COL				= 8;
	public static final int		OUTPATIENTNUMBER_COL	= 9;
	public static final int		HOSPITALNUMBER_COL		= 10;
	public static final int		BEDNUMBER_COL			= 11;
	public static final int		SYMPTOM_COL				= 12;
	public static final int		PHONE_COL				= 13;
	public static final int		ADDRESS_COL				= 14;
	public static final int		POSTCODE_COL			= 15;
	public static final int		CREATEDTM_COL			= 16;
	public static final int		PERSONREMARK_COL		= 17;
	public static final int		RELATIVEPATH_COL		= 18;
	public static final int		DATAPATH_COL			= 19;
	public static final int		FILENAME_COL			= 20;
	public static final int		FILELENGTH_COL			= 21;
	public static final int		FILEDESP_COL			= 22;
	public static final int		OCCURDTM_COL			= 23;
	public static final int		HANDLESTATE_COL			= 24;
	public static final int		STARTHANDLEDTM_COL		= 25;
	public static final int		ENDHANDLEDTM_COL		= 26;
	public static final int		DIAGNOSE_COL			= 27;
	public static final int		DATAREMARK_COL			= 28;
	public static final int		HANDLEID_COL			= 29;
	public static final int		DEL_COL					= 30;
	public static final int		PERSONDATAID_COL		= 31;
	public static final int		DATASYNCFLAG_COL		= 32;
	public static final int		STATESYNCFLAG_COL		= 33;
	public static final int		DATASTATE_COL			= 34;
	public static final int		PERSONRECORDDTM_COL		= 35;
	public static final int		SAMPLINGSATE_COL		= 36;
	public static final int		REPORTCOLLECTDTM_COL	= 37;
}
