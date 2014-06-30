package com.example.gcmservertest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	public static Context mContext = null;

	//푸쉬를 받기/받지 않기를 설정하기 위한 체크박스

	public CheckBox cb_setting = null;

	//푸쉬로 받은 메시지를 표현하기 위한 텍스트뷰

	public static TextView tv_msg = null;
	public static boolean flag=false;




	//public static String PROJECT_ID = "989210968479";
	public static String PROJECT_ID = "559669726585";

	@Override

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mContext = this;

		setContentView(R.layout.activity_main);

		tv_msg = (TextView)findViewById(R.id.textView1);

		cb_setting = (CheckBox)findViewById(R.id.checkBox1);

		cb_setting.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				// TODO Auto-generated method stub

				//푸쉬 받기

				if(isChecked){
					flag=true;
					Log.d("test","푸쉬 메시지를 받습니다.");

					GCMRegistrar.checkDevice(mContext);

					GCMRegistrar.checkManifest(mContext);

					if(GCMRegistrar.getRegistrationId(mContext).equals("")){

						GCMRegistrar.register(mContext, PROJECT_ID);

					}else{

						//이미 GCM 을 상요하기위해 등록ID를 구해왔음

						GCMRegistrar.unregister(mContext);

						GCMRegistrar.register(mContext, PROJECT_ID);

					}

				}

				//푸쉬 받지않기

				else{
					flag=false;
					Log.d("test","푸쉬 메시지를 받지 않습니다.");

					GCMRegistrar.unregister(mContext);

				}

			}

		});



	}



	@Override

	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		return true;

	}
}
