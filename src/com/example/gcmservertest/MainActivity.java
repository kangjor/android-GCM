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

	//Ǫ���� �ޱ�/���� �ʱ⸦ �����ϱ� ���� üũ�ڽ�

	public CheckBox cb_setting = null;

	//Ǫ���� ���� �޽����� ǥ���ϱ� ���� �ؽ�Ʈ��

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

				//Ǫ�� �ޱ�

				if(isChecked){
					flag=true;
					Log.d("test","Ǫ�� �޽����� �޽��ϴ�.");

					GCMRegistrar.checkDevice(mContext);

					GCMRegistrar.checkManifest(mContext);

					if(GCMRegistrar.getRegistrationId(mContext).equals("")){

						GCMRegistrar.register(mContext, PROJECT_ID);

					}else{

						//�̹� GCM �� ����ϱ����� ���ID�� ���ؿ���

						GCMRegistrar.unregister(mContext);

						GCMRegistrar.register(mContext, PROJECT_ID);

					}

				}

				//Ǫ�� �����ʱ�

				else{
					flag=false;
					Log.d("test","Ǫ�� �޽����� ���� �ʽ��ϴ�.");

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
