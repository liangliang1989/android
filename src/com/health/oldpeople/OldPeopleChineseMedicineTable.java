package com.health.oldpeople;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.younext.R;

import com.health.database.Tables;

public class OldPeopleChineseMedicineTable {
	public  static final String OLDPEOPLETABLE="oldpeople_table";
	public  static final String PATIENT="patient_name";
	public  static final String SERIALID="serial_id";
	public  static final String TITTLE1="Tittle1";
	public  static final String TITTLE2="Tittle2";
	public  static final String TITTLE3="Tittle3";
	public  static final String TITTLE4="Tittle4";
	public  static final String TITTLE5="Tittle5";
	public  static final String TITTLE6="Tittle6";
	public  static final String TITTLE7="Tittle7";
	public  static final String TITTLE8="Tittle8";
	public  static final String TITTLE9="Tittle9";
	public  static final String TITTLE10="Tittle10";
	public  static final String TITTLE11="Tittle11";
	public  static final String TITTLE12="Tittle12";
	public  static final String TITTLE13="Tittle13";
	public  static final String TITTLE14="Tittle14";
	public  static final String TITTLE15="Tittle15";
	public  static final String TITTLE16="Tittle16";
	public  static final String TITTLE17="Tittle17";
	public  static final String TITTLE18="Tittle18";
	public  static final String TITTLE19="Tittle19";
	public  static final String TITTLE20="Tittle20";
	public  static final String TITTLE21="Tittle21";
	public  static final String TITTLE22="Tittle22";
	public  static final String TITTLE23="Tittle23";
	public  static final String TITTLE24="Tittle24";
	public  static final String TITTLE25="Tittle25";
	public  static final String TITTLE26="Tittle26";
	public  static final String TITTLE27="Tittle27";
	public  static final String TITTLE28="Tittle28";
	public  static final String TITTLE29="Tittle29";
	public  static final String TITTLE30="Tittle30";
	public  static final String TITTLE31="Tittle31";
	public  static final String TITTLE32="Tittle32";
	public  static final String TITTLE33="Tittle33";
	public  static final String PHYSCIALCLASS1="physcialclass1";
	public  static final String PHYSCIALCLASS2="physcialclass2";
	public  static final String PHYSCIALCLASS3="physcialclass3";
	public  static final String PHYSCIALCLASS4="physcialclass4";
	public  static final String PHYSCIALCLASS5="physcialclass5";
	public  static final String PHYSCIALCLASS6="physcialclass6";
	public  static final String PHYSCIALCLASS7="physcialclass7";
	public  static final String PHYSCIALCLASS8="physcialclass8";
	public  static final String PHYSCIALCLASS9="physcialclass9";
	public  static final String PHYSCIALGUIDE="physcialguide";
	public static final String OLDPEOPLESERVICEDATE="oldpeopleservice_date";
	public static final String OLDPEOPLESERVICEDOCTER="oldpeopleservice_doctor";
	
	public static final Map<String, int[]> cloumIdmap = Collections
			.unmodifiableMap(new HashMap<String, int[]>() {

				private static final long serialVersionUID = -8896139986212203908L;
				{
					put(PATIENT,new int[]{0,R.id.patient_name});
					put(SERIALID,new int[]{0,R.id.serial_id});
					put(TITTLE1,new int[]{1,R.id.ratingbar1});
					put(TITTLE2,new int[]{1,R.id.ratingbar2});
					put(TITTLE3,new int[]{1,R.id.ratingbar3});
					put(TITTLE4,new int[]{1,R.id.ratingbar4});
					put(TITTLE5,new int[]{1,R.id.ratingbar5});
					put(TITTLE6,new int[]{1,R.id.ratingbar6});
					put(TITTLE7,new int[]{1,R.id.ratingbar7});
					put(TITTLE8,new int[]{1,R.id.ratingbar8});
					put(TITTLE9,new int[]{1,R.id.ratingbar9});
					put(TITTLE10,new int[]{1,R.id.ratingbar10});
					put(TITTLE11,new int[]{1,R.id.ratingbar11});
					put(TITTLE12,new int[]{1,R.id.ratingbar12});
					put(TITTLE13,new int[]{1,R.id.ratingbar13});
					put(TITTLE14,new int[]{1,R.id.ratingbar14});
					put(TITTLE15,new int[]{1,R.id.ratingbar15});
					put(TITTLE16,new int[]{1,R.id.ratingbar16});
					put(TITTLE17,new int[]{1,R.id.ratingbar17});
					put(TITTLE18,new int[]{1,R.id.ratingbar18});
					put(TITTLE19,new int[]{1,R.id.ratingbar19});
					put(TITTLE20,new int[]{1,R.id.ratingbar20});
					put(TITTLE21,new int[]{1,R.id.ratingbar21});
					put(TITTLE22,new int[]{1,R.id.ratingbar22});
					put(TITTLE23,new int[]{1,R.id.ratingbar23});
					put(TITTLE24,new int[]{1,R.id.ratingbar24});
					put(TITTLE25,new int[]{1,R.id.ratingbar25});
					put(TITTLE26,new int[]{1,R.id.ratingbar26});
					put(TITTLE27,new int[]{1,R.id.ratingbar27});
					put(TITTLE28,new int[]{1,R.id.ratingbar28});
					put(TITTLE29,new int[]{1,R.id.ratingbar29});
					put(TITTLE30,new int[]{1,R.id.ratingbar30});
					put(TITTLE31,new int[]{1,R.id.ratingbar31});
					put(TITTLE32,new int[]{1,R.id.ratingbar32});
					put(TITTLE33,new int[]{1,R.id.ratingbar33});
					put(PHYSCIALCLASS1,new int[]{0,R.id.oldpeopleservice_edt_physcialclass1});
					put(PHYSCIALCLASS2,new int[]{0,R.id.oldpeopleservice_edt_physcialclass2});
					put(PHYSCIALCLASS3,new int[]{0,R.id.oldpeopleservice_edt_physcialclass3});
					put(PHYSCIALCLASS4,new int[]{0,R.id.oldpeopleservice_edt_physcialclass4});
					put(PHYSCIALCLASS5,new int[]{0,R.id.oldpeopleservice_edt_physcialclass5});
					put(PHYSCIALCLASS6,new int[]{0,R.id.oldpeopleservice_edt_physcialclass6});
					put(PHYSCIALCLASS7,new int[]{0,R.id.oldpeopleservice_edt_physcialclass7});
					put(PHYSCIALCLASS8,new int[]{0,R.id.oldpeopleservice_edt_physcialclass8});
					put(PHYSCIALCLASS9,new int[]{0,R.id.oldpeopleservice_edt_physcialclass9});
					
					put(PHYSCIALGUIDE,new int[]{0,R.id.oldpeopleservice_mce_physcialguide});
					put(OLDPEOPLESERVICEDATE,new int[]{0,R.id.oldpeopleservice_det_reportdate});
					put(OLDPEOPLESERVICEDOCTER,new int[]{0,R.id.oldpeopleservice_reportdoctor});
				}
			});
			
				
	
	public static Map<String, String> OldPeopleChineseMedicineTable() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tables.TABLE_NAME, OLDPEOPLETABLE);
		map.put(PATIENT, "varchar(20)");
		map.put(SERIALID, "varchar(20)");
		map.put(TITTLE1, "integer");
		map.put(TITTLE2, "integer");
		map.put(TITTLE3, "integer");
		map.put(TITTLE4, "integer");
		map.put(TITTLE5, "integer");
		map.put(TITTLE6, "integer");
		map.put(TITTLE7, "integer");
		map.put(TITTLE8, "integer");
		map.put(TITTLE9, "integer");
		map.put(TITTLE10, "integer");
		map.put(TITTLE11, "integer");
		map.put(TITTLE12, "integer");
		map.put(TITTLE13, "integer");
		map.put(TITTLE14, "integer");
		map.put(TITTLE15, "integer");
		map.put(TITTLE16, "integer");
		map.put(TITTLE17, "integer");
		map.put(TITTLE18, "integer");
		map.put(TITTLE19, "integer");
		map.put(TITTLE20, "integer");
		map.put(TITTLE21, "integer");
		map.put(TITTLE22, "integer");
		map.put(TITTLE23, "integer");
		map.put(TITTLE24, "integer");
		map.put(TITTLE25, "integer");
		map.put(TITTLE26, "integer");
		map.put(TITTLE27, "integer");
		map.put(TITTLE28, "integer");
		map.put(TITTLE29, "integer");
		map.put(TITTLE30, "integer");
		map.put(TITTLE31, "integer");
		map.put(TITTLE32, "integer");
		map.put(TITTLE33, "integer");
		map.put(PHYSCIALCLASS1, "varchar(20)");
		map.put(PHYSCIALCLASS2, "varchar(20)");
		map.put(PHYSCIALCLASS3, "varchar(20)");
		map.put(PHYSCIALCLASS4, "varchar(20)");
		map.put(PHYSCIALCLASS5, "varchar(20)");
		map.put(PHYSCIALCLASS6, "varchar(20)");
		map.put(PHYSCIALCLASS7, "varchar(20)");
		map.put(PHYSCIALCLASS8, "varchar(20)");
		map.put(PHYSCIALCLASS9, "varchar(20)");
		map.put(PHYSCIALGUIDE, "varchar(100)");
		map.put(OLDPEOPLESERVICEDATE, "varchar(20)");
		map.put(OLDPEOPLESERVICEDOCTER, "varchar(20)");
		
		return map;
	}
}
