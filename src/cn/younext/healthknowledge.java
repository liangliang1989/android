package cn.younext;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class healthknowledge extends Activity{
	Button homeBtn;
	Button healthknowledge_canshuBtn;
	Button healthknowledge_richangBtn;
	Button healthknowledge_jinjiBtn;
	TextView txt;
	OnClickListener btnClick;
	int userid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthknowledge);
        homeBtn = (Button)findViewById(R.id.healthknowledge_homeBtn);
        healthknowledge_canshuBtn=(Button)findViewById(R.id.healthknowledge_canshuBtn);
        healthknowledge_richangBtn=(Button)findViewById(R.id.healthknowledge_richangBtn);
        healthknowledge_jinjiBtn=(Button)findViewById(R.id.healthknowledge_jinjiBtn);
        btnClick= new OnClickListener(){

    		@Override
    		public void onClick(View v) {

        		if(v==homeBtn){
        		healthknowledge.this.finish();	
        		}
        		else if(v==healthknowledge_canshuBtn){
        		txt=(TextView)findViewById(R.id.healthknowledge_text);
        		txt.setText("��������\n" +
        				"Boys and girls,don't give up babyyou know? babauh uh dreams come true yeuh uh uh �����Լ�Ϊ��ʵ������ �����uh uh uh �����Ļ�");
        		txt.showContextMenu();
        		}
        		else if(v==healthknowledge_richangBtn){
            		txt=(TextView)findViewById(R.id.healthknowledge_text);
            		txt.setText("�ճ�����"); 		
        		}
        		else if(v==healthknowledge_jinjiBtn){
            		txt=(TextView)findViewById(R.id.healthknowledge_text);
            		txt.setText("��������");
        		}
        		else{  			
        		}	
    		}
        };
        homeBtn.setOnClickListener(btnClick);
        healthknowledge_canshuBtn.setOnClickListener(btnClick);
        healthknowledge_richangBtn.setOnClickListener(btnClick);
        healthknowledge_jinjiBtn.setOnClickListener(btnClick);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        	   
        	healthknowledge.this.finish();
		

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
