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
	// GCM에 정상적으로 등록되었을경우 발생하는 메소드

	@Override
	protected void onRegistered(Context arg0, String arg1) {

		Log.d("test", "등록ID:" + arg1);
		paramValue=arg1;
		new ConnectHttp().execute();

	}

	// GCM에 해지하였을경우 발생하는 메소드
	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.d("test", "해지ID:" + arg1);
	}

	// GCM이 메시지를 보내왔을때 발생하는 메소드
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

	// 오류를 핸들링하는 메소드
	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d("test", arg1);
	}

	// 서비스 생성자
	public GCMIntentService() {
		super(MainActivity.PROJECT_ID);
		Log.d("test", "GCM서비스 생성자 실행");
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
				Toast.makeText(MainActivity.mContext, "수신 메시지 : "+gcm_msg, 3000).show();	
			}
		}
	};
	
	private class ConnectHttp extends AsyncTask<Void, Void, Void>{

		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				// 휴대폰의 고유 아이디 얻어오기 - Regid의 중복을 막기위해
				TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String phoneid = telManager.getDeviceId();
				
				//읽어온 데이터를 하나의 스트링으로 만들어서 output
				String data = paramName + "=" + paramValue + "&phoneid=" + phoneid;
				//Url로 접속후 데이터를 넘김
				Url =new URL(url);
				urlconn = Url.openConnection();
				//데이터를 보내기위한 설정
				urlconn.setDoOutput(true);
				//스트림을 통하여 데이터를 보냄
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