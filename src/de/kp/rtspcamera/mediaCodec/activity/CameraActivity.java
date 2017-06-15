package de.kp.rtspcamera.mediaCodec.activity;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import de.kp.net.rtsp.RtspConstants;
import de.kp.net.rtsp.server.RtspServer;
import de.kp.rtspcamera.MediaConstants;
import de.kp.rtspcamera.MyApplication;
import de.kp.rtspcamera.R;
import de.kp.rtspcamera.RtspNativeCodecsCamera;
import de.kp.rtspcamera.mediaCodec.camera.PeerCamera;
import de.kp.rtspcamera.mediaCodec.camera.preview.CameraTextureView;
import de.kp.rtspcamera.mediaCodec.socket.RtspRecorder;
import de.kp.rtspcamera.mediaCodec.util.DisplayUtil;
import de.kp.rtspcamera.poker.PokerController;
import de.kp.rtspcamera.poker.algo.AlgoFactory;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import de.kp.rtspcamera.poker.utils.SettingUtils;
import de.kp.rtspcamera.poker.utils.SoundUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.LogRecord;

/**
 * CameraActivity.java
 * <p/>
 * Created by x.q. on 2016/12/30.
 * <p/>
 * Copyright (c) 2016 x.q.
 */

public class CameraActivity extends Activity {
	private static final String TAG = "peer";
	CameraTextureView textureView = null;
	ImageButton shutterBtn;
	private RtspServer streamer;
	private WifiManager wifiManager; 

//	RtspRecorder recorder;
	private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
        	Log.d("xxx", "handleMessage exit");
        	RtspRecorder.getInstance().close();
        	if (streamer != null)
    			streamer.stop();
    		streamer = null;
            System.exit(0);
        }

    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_camera);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		PokerController.startPokerDetectionThread();
		initUI();
		initViewParams();
		//获取wifi管理服务  
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        setWifiApEnabled(true); // faild on telephone, work on mini tv
		textureView.setAlpha(1.0f);
		shutterBtn.setOnClickListener(new BtnListeners());
		SoundUtils.getInstance().initSound(this);
		AlgoFactory.getInstance().createAlgo().clearPlayedArray();
		readPrefence();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
	
		// starts the RTSP Server
		try {

			// initialize video encoder to be used
			// for SDP file generation

			if (streamer == null) {
				streamer = new RtspServer(RtspConstants.SERVER_PORT, RtspConstants.VideoEncoder.Mediacodec_ENCODER);
				new Thread(streamer).start();
			}

			Log.d(TAG, "RtspServer started");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "RtspServer started faield : "+e);
		}

		RtspRecorder.getInstance().open();
		RtspRecorder.getInstance().start();
		super.onResume();

	}
	
	private void readPrefence(){
		PokerCustomerSetting.gameType = SettingUtils.getGameType(MyApplication.getAppContext());
		PokerCustomerSetting.peopleNums = SettingUtils.getPeopleNum(MyApplication.getAppContext());
		PokerCustomerSetting.broadcastType = SettingUtils.getBroadcatType(MyApplication.getAppContext());
		PokerCustomerSetting.playType = SettingUtils.getPlayType(MyApplication.getAppContext());
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		// stop RTSP server	
		onExit();
		super.onPause();
		
		//recorder.close();
	}

	private void initUI(){
		textureView = (CameraTextureView)findViewById(R.id.camera_textureview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
	}
	private void initViewParams(){
		LayoutParams params = textureView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		float previewRate = DisplayUtil.getScreenRate(this); //姒涙﹢鏁撻弬銈嗗閸忋劑鏁撻弬銈嗗闁跨喍鑼庨幉瀣闁跨喐鏋婚幏鐑筋暕闁跨喐鏋婚幏锟�		textureView.setLayoutParams(params,previewRate);

		LayoutParams p2 = shutterBtn.getLayoutParams();
		p2.width = DisplayUtil.dip2px(this, 80);
		p2.height = DisplayUtil.dip2px(this, 80);
		shutterBtn.setLayoutParams(p2);	

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundUtils.getInstance().onDestroy();
//        ServerSocketHelper.getInstance().stopServer();
    }

 // wifi热点开关  
    public boolean setWifiApEnabled(boolean enabled) {  
        if (enabled) { // disable WiFi in any case  
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi  
            wifiManager.setWifiEnabled(false);  
        }  
        try {  
        	WifiConfiguration apConfig = new WifiConfiguration();
        	final String account = "testwifi";
        	final String password = "test1234";
            //配置热点的名称(可以在名字后面加点随机数什么的)  
            apConfig.SSID = account;  
            //配置热点的密码  
            apConfig.preSharedKey=password; 
            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            apConfig.hiddenSSID =true ;
                //通过反射调用设置热点  
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(CameraActivity.this, "连接账号：" + account + ",密码是：" + password, Toast.LENGTH_LONG).show();//提示信息接收方要连接的热点账号和密码

				}
				
			});            return (Boolean) method.invoke(wifiManager, apConfig, enabled);  

        } catch (Exception e) {  
        	Log.e(TAG, "error e = "+e.toString());
        	this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(CameraActivity.this, "开启热点失败" , Toast.LENGTH_LONG).show();//提示信息接收方要连接的热点账号和密码
				}
				
			});            
        	return false;  
        }  
    } 
    
    private void onExit() {
    	Log.d(TAG, "onExit ---");
		PeerCamera.getInstance().doStopCamera();
    	RtspRecorder.getInstance().stop();
		Toast.makeText(CameraActivity.this, "正在断开客户端连接" , Toast.LENGTH_LONG).show();
		mHandler.sendEmptyMessageDelayed(0, 1000);
    }
	

	private class BtnListeners implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			switch(v.getId()){
//			case R.id.btn_shutter:
//				System.exit(0);
//				break;
//			default:break;
//			}
			if (v == shutterBtn) {
//				recorder.close();
//				RtspRecorder.getInstance().close();
//				System.exit(0);
				onExit();
			}
		}

	}

}
