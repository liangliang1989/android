package com.health.measurement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.younext.R;

import com.KRL.Ui.MeasureOnKRLBP;
import com.KRL.Ui.MeasureOnKRLECG;
import com.KRL.Ui.MeasureOnSelf;
import com.health.BaseActivity;
import com.health.util.VersionControler;
import com.health.util.VersionControler.V;

public class Measurement extends BaseActivity {
	Button homeButton;

	Button gluButton;
	Button boBotton;
	Button bpButton;
	Button ecgButton;// �ĵ簴ť
	Button baixibaoButton;// ��ϸ����ť
	Button tempButton;// ���°�ť
	Button uaButton;// ���ᰴť
	Button cholButton;// �ܵ��̴���ť
	Button urineButton;// ��Һ������ť
	Button deviceMeasureButton;
	Button handinputButton;
	int userid;
	String username;
	OnClickListener onClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measurement);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			userid = extra.getInt("userid");
			username = extra.getString("username");
		}
		handinputButton = (Button) findViewById(R.id.test_handbtn);
		deviceMeasureButton = (Button) this.findViewById(R.id.test_machinebtn);
		deviceMeasureButton.setSelected(true);
		handinputButton.setSelected(false);
		homeButton = (Button) findViewById(R.id.test_homebutton);
		boBotton = (Button) findViewById(R.id.test_xueyang);
		gluButton = (Button) findViewById(R.id.test_xuetang);
		ecgButton = (Button) findViewById(R.id.test_ecg);
		baixibaoButton = (Button) findViewById(R.id.test_baixibao);
		tempButton = (Button) this.findViewById(R.id.test_tiwen);
		uaButton = (Button) this.findViewById(R.id.test_niaosuan);
		cholButton = (Button) this.findViewById(R.id.test_zongdanguchun);
		urineButton = (Button) this.findViewById(R.id.test_niaoyifenxi);
		bpButton = (Button) findViewById(R.id.test_bp);
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (VersionControler.version == V.KRL)
					krl(v);
				else
					pingantong(v);
			}

			private void pingantong(View v) {
				Intent intent = new Intent();
				if (v == homeButton) {
					Measurement.this.finish();
				} else if (v == bpButton || v == boBotton || v == tempButton) {
					intent.setClass(Measurement.this, MeasureOnPC300.class);
					// intent.setClass(Measurement.this,
					// MainActivity.class);
					startActivityForResult(intent, 1);
				} else if (v == ecgButton) {
					intent.setClass(Measurement.this, MeasureOnKRLECG.class);
					startActivityForResult(intent, 1);
				} else if (v == handinputButton) {

					intent.setClass(Measurement.this, HandInputMeasure.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					Log.v("handinputbtn", "press");
					startActivityForResult(intent, 1);
				} else if (v == gluButton || uaButton == v || cholButton == v) {
					intent.setClass(Measurement.this, MeasureGlucose.class);
					startActivityForResult(intent, 1);
				} else if (urineButton == v) {
					intent.setClass(Measurement.this, MeasureUrine.class);
					startActivityForResult(intent, 1);
				} else if (baixibaoButton == v) {
					intent.setClass(Measurement.this, MeasureWbc.class);
					startActivityForResult(intent, 1);
				}
			}

			/**
			 * krlҪ�ӵ�����
			 * 
			 * @param v
			 */
			private void krl(View v) {
				Intent intent = new Intent();
				if (v == homeButton) {
					Measurement.this.finish();
				} else if (v == boBotton) {
					intent.setClass(Measurement.this, MeasureOnSelf.class);
					intent.putExtra("MeasureType", "BloodOxygen");
					startActivityForResult(intent, 1);
				} else if (v == tempButton) {
					intent.setClass(Measurement.this, MeasureOnSelf.class);
					intent.putExtra("MeasureType", "Temperature");
					startActivityForResult(intent, 1);
				} else if (v == bpButton) {
					intent.setClass(Measurement.this, MeasureOnKRLBP.class);
					startActivityForResult(intent, 1);
				} else if (v == ecgButton) {
					intent.setClass(Measurement.this, MeasureOnKRLECG.class);
					// intent.setClass(Measurement.this,
					// MainActivity.class);
					startActivityForResult(intent, 1);
				} else if (v == handinputButton) {
					intent.setClass(Measurement.this, HandInputMeasure.class);
					intent.putExtra("userid", userid);
					intent.putExtra("username", username);
					Log.v("handinputbtn", "press");
					startActivityForResult(intent, 1);
				} else if (v == gluButton || uaButton == v || cholButton == v) {
					intent.setClass(Measurement.this, MeasureGlucose.class);
					startActivityForResult(intent, 1);
				} else if (urineButton == v) {
					intent.setClass(Measurement.this, MeasureUrine.class);
					startActivityForResult(intent, 1);
				} else if (baixibaoButton == v) {
					intent.setClass(Measurement.this, MeasureWbc.class);
					startActivityForResult(intent, 1);
				}
			}

		};
		homeButton.setOnClickListener(onClickListener);
		boBotton.setOnClickListener(onClickListener);
		handinputButton.setOnClickListener(onClickListener);
		gluButton.setOnClickListener(onClickListener);
		bpButton.setOnClickListener(onClickListener);
		tempButton.setOnClickListener(onClickListener);
		uaButton.setOnClickListener(onClickListener);
		cholButton.setOnClickListener(onClickListener);
		urineButton.setOnClickListener(onClickListener);
		ecgButton.setOnClickListener(onClickListener);
		baixibaoButton.setOnClickListener(onClickListener);
		if (VersionControler.version == V.KRL)
			baixibaoButton.setVisibility(View.GONE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Measurement.this.finish();
			}
			break;
		default:
			break;
		}
	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Measurement.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
