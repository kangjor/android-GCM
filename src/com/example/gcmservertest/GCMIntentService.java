package com.example.gcmservertest;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	String gcm_msg = null;

	String paramName="regid";
	String paramValue;
	String hashKey = null;
	URL Url;
	URLConnection urlconn;
	String url = "http://210.109.31.31:9090/Android12_GCMServer/AndroidIdReg.jsp";
	// GCM�� ���������� ��ϵǾ������ �߻��ϴ� �޼ҵ�

	@Override
	protected void onRegistered(Context arg0, String arg1) {

		Log.d("test", "���ID:" + arg1);
		paramValue=arg1;
		new ConnectHttp().execute();

	}

	// GCM�� �����Ͽ������ �߻��ϴ� �޼ҵ�
	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.d("test", "����ID:" + arg1);
	}

	// GCM�� �޽����� ���������� �߻��ϴ� �޼ҵ�
	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		 String temp = arg1.getStringExtra("test");
		 try {
			gcm_msg = URLDecoder.decode(temp,"euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showMessage();
	}

	// ������ �ڵ鸵�ϴ� �޼ҵ�
	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d("test", arg1);
	}

	// ���� ������
	public GCMIntentService() {
		super(MainActivity.PROJECT_ID);
		Log.d("test", "GCM���� ������ ����");
	}

	public void showMessage() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(MainActivity.flag){
				Toast.makeText(MainActivity.mContext, "���� �޽��� : "+gcm_msg, 3000).show();	
			}
		}
	};
	
	private class ConnectHttp extends AsyncTask<Void, Void, Void>{

		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				// �޴����� ���� ���̵� ������ - Regid�� �ߺ��� ��������
				TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String phoneid = telManager.getDeviceId();
				
				//�о�� �����͸� �ϳ��� ��Ʈ������ ���� output
				String data = paramName + "=" + paramValue + "&phoneid=" + phoneid;
				//Url�� ������ �����͸� �ѱ�
				Url =new URL(url);
				urlconn = Url.openConnection();
				//�����͸� ���������� ����
				urlconn.setDoOutput(true);
				//��Ʈ���� ���Ͽ� �����͸� ����
				OutputStream out = urlconn.getOutputStream();
				out.write(data.getBytes("UTF-8"));
				out.flush();
				
				InputStream in = urlconn.getInputStream();
				
				in.close();
				out.close();				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		
	}


}