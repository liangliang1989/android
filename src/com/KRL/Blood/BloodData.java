package com.KRL.Blood;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class BloodData implements Parcelable {
	private int sys;
	private int mean;
	private int plus;
	private int dia;
	private long utc;
	private boolean realPressuredata;
	private int pressure;
	public final static byte BLOODDATA_SYS = 1;
	public final static byte BLOODDATA_MEAN = 2;
	public final static byte BLOODDATA_PLUSE = 3;
	public final static byte BLOODDATA_DIA = 4;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setBloodPressure(int pre) {
		realPressuredata = true;
		pressure = pre;
	}

	public void setBloodData(int sys, int mean, int plus, int dia) {
		this.sys = sys;
		this.mean = mean;
		this.plus = plus;
		this.dia = dia;
		utc = System.currentTimeMillis();
		realPressuredata = false;
	}

	public boolean isRealPressure() {
		return realPressuredata;
	}

	public int getRealPressureData() {
		return pressure;
	}

	public int getBloodData(byte datatype) {
		switch (datatype) {
		case BLOODDATA_DIA: {
			return dia;
		}
		case BLOODDATA_MEAN: {
			return mean;
		}
		case BLOODDATA_PLUSE: {
			return plus;
		}
		case BLOODDATA_SYS: {
			return sys;
		}
		default:
			return 0;
		}
	}

	public long getUtc() {
		return utc;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putInt("sys", sys);
		bundle.putInt("mean", mean);
		bundle.putInt("plus", plus);
		bundle.putInt("dia", dia);
		bundle.putInt("pressure", pressure);
		bundle.putBoolean("pressuredata", realPressuredata);
		bundle.putLong("utc", utc);
		dest.writeBundle(bundle);

	}

	public static final Parcelable.Creator<BloodData> CREATOR = new Parcelable.Creator<BloodData>() {

		@Override
		public BloodData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Bundle bundle = source.readBundle();
			BloodData blooddata = new BloodData();
			blooddata.dia = bundle.getInt("dia");
			blooddata.mean = bundle.getInt("mean");
			blooddata.plus = bundle.getInt("plus");
			blooddata.pressure = bundle.getInt("pressure");
			blooddata.sys = bundle.getInt("sys");
			blooddata.realPressuredata = bundle.getBoolean("realPressuredata");
			blooddata.utc = bundle.getLong("utc");
			return blooddata;
		}

		@Override
		public BloodData[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}

	};
}
