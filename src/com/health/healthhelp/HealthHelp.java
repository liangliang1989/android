package com.health.healthhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.younext.R;

import com.health.BaseActivity;
import com.health.chronicdisease.GlycuresisTure;
import com.health.chronicdisease.GlycuresisTureList;
import com.health.chronicdisease.HypertensionTure;
import com.health.chronicdisease.HypertensionTureList;

public class HealthHelp extends BaseActivity {
	private Button hypbtn;
	private Button glybtn;
	private Intent intent;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_help);
		findView();
		setOnClick();
	}
	private void findView(){
		hypbtn=(Button)findViewById(R.id.btn_hypertension);
		glybtn=(Button)findViewById(R.id.btn_glycuresis);
	}
	private void setOnClick(){
		hypbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(HealthHelp.this, HypertensionTureList.class);
				startActivity(intent);
			}
		});
glybtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(HealthHelp.this, GlycuresisTureList.class);
				startActivity(intent);
			}
		});
	}
}
