package de.kp.rtspcamera;

import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import de.kp.net.rtp.recorder.RtspVideoRecorder;
import de.kp.net.rtsp.RtspConstants;
import de.kp.net.rtsp.server.RtspServer;
import de.kp.rtspcamera.poker.PokerController;
import de.kp.rtspcamera.poker.algo.AlgoFactory;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import de.kp.rtspcamera.poker.utils.SettingUtils;
import de.kp.rtspcamera.poker.utils.SoundUtils;

public class RtspNativeCodecsCamera extends Activity {

	private String TAG = "RTSPNativeCamera";

	// default RTSP command port is 554
//	private int SERVER_PORT = 8080;
	
	private WifiManager wifiManager;  

	//private RtspVideoRecorder outgoingPlayer;

	private SurfaceView mCameraPreview;
	private SurfaceHolder previewHolder;

	private Camera camera;

	private boolean inPreview = false;
	private boolean cameraConfigured = false;

	private int mPreviewWidth = MediaConstants.PreviewWidth;
	private int mPreviewHeight = MediaConstants.PreviewHeight;

	private RtspServer streamer;
	
	 private static Context context = null;
	
	 public static Context getContext() {
		 return context;
	 }
	 
	 private Handler mHandler = new Handler(){

	        @Override
	        public void handleMessage(Message msg) {
	        	Log.d("xxx", "handleMessage exit");
	        	RtspVideoRecorder.getInstance().close();
	        	if (streamer != null)
	    			streamer.stop();
	    		streamer = null;
	            System.exit(0);
	        }

	    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//获取wifi管理服务  
		PokerController.startPokerDetectionThread();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        setWifiApEnabled(true);
		setContentView(R.layout.cameranativecodecs);
		/*
		 * Camera preview initialization
		 */
		mCameraPreview = (SurfaceView) findViewById(R.id.smallcameraview);
		previewHolder = mCameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//		outgoingPlayer = new RtspVideoRecorder("h264");
		RtspVideoRecorder.getInstance().open();
		
		SoundUtils.getInstance().initSound(this);
		AlgoFactory.getInstance().createAlgo().clearPlayedArray();
		readPrefence();
	}
	
	private void readPrefence(){
		PokerCustomerSetting.gameType = SettingUtils.getGameType(MyApplication.getAppContext());
		PokerCustomerSetting.peopleNums = SettingUtils.getPeopleNum(MyApplication.getAppContext());
		PokerCustomerSetting.broadcastType = SettingUtils.getBroadcatType(MyApplication.getAppContext());
		PokerCustomerSetting.playType = SettingUtils.getPlayType(MyApplication.getAppContext());
	}
    
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
			onExit();
		}
 		return super.onKeyDown(keyCode, event);
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
					Toast.makeText(RtspNativeCodecsCamera.this, "连接账号：" + account + ",密码是：" + password, Toast.LENGTH_LONG).show();//提示信息接收方要连接的热点账号和密码

				}
				
			});            return (Boolean) method.invoke(wifiManager, apConfig, enabled);  

        } catch (Exception e) {  
        	Log.e(TAG, "error e = "+e.toString());
        	this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(RtspNativeCodecsCamera.this, "开启热点失败" , Toast.LENGTH_LONG).show();//提示信息接收方要连接的热点账号和密码
				}
				
			});            
        	return false;  
        }  
    } 
	
    private void onExit() {
    	Log.d(TAG, "onExit ---");
    	RtspVideoRecorder.getInstance().stop();
		Toast.makeText(RtspNativeCodecsCamera.this, "正在断开客户端连接" , Toast.LENGTH_LONG).show();
		mHandler.sendEmptyMessageDelayed(0, 1000);
    }
    
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		

		// starts the RTSP Server

		try {

			// initialize video encoder to be used
			// for SDP file generation
			RtspConstants.VideoEncoder rtspVideoEncoder = (MediaConstants.H264_CODEC == true) ? RtspConstants.VideoEncoder.H264_ENCODER
					: RtspConstants.VideoEncoder.H263_ENCODER;

			if (streamer == null) {
				streamer = new RtspServer(RtspConstants.SERVER_PORT, rtspVideoEncoder);
				new Thread(streamer).start();
			}

			Log.d(TAG, "RtspServer started");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Camera initialization
		 */
		//camera = Camera.open();
		camera = null;
		try {
			camera = Camera.open();
			Log.i(TAG, "Camera open over....");

		}catch (Exception e){
			Log.e(TAG, " do open camera failed : " + e);
			Toast.makeText(this, "摄像头被占用，关闭摄像头重新打开应用!!!",
					Toast.LENGTH_LONG).show();
		}

		super.onResume();

	}

	@Override
	public void onPause() {

		// stop RTSP server
//		if (streamer != null)
//			streamer.stop();
//		streamer = null;
		onExit();
		super.onPause();
	}

	/*
	 * SurfaceHolder callback triple
	 */
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		/*
		 * Created state: - Open camera - initial call to startPreview() - hook
		 * PreviewCallback() on it, which notifies waiting thread with new
		 * preview data - start thread
		 * 
		 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.
		 * SurfaceHolder )
		 */
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "surfaceCreated");

		}

		/*
		 * Changed state: - initiate camera preview size, set
		 * camera.setPreviewDisplay(holder) - subsequent call to startPreview()
		 * 
		 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.
		 * SurfaceHolder , int, int, int)
		 */
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			Log.d(TAG, "surfaceChanged");
			initializePreview(w, h);
			startPreview();
		}

		/*
		 * Destroy State: Take care on release of camera
		 * 
		 * @see
		 * android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
		 * SurfaceHolder)
		 */
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d(TAG, "surfaceDestroyed");

			if (inPreview) {
				camera.stopPreview();
			}
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
			
			// stop captureThread
			onExit();

			inPreview = false;
			cameraConfigured = false;

		}
	};


	/**
	 * This method checks availability of camera and preview
	 * 
	 * @param width
	 * @param height
	 */
	private void initializePreview(int width, int height) {
		Log.d(TAG, "initializePreview");

		if (camera != null && previewHolder.getSurface() != null) {
			try {
				// provide SurfaceView for camera preview
				camera.setPreviewDisplay(previewHolder);

			} catch (Throwable t) {
				Log.e(TAG, "Exception in setPreviewDisplay()", t);
			}

			if (!cameraConfigured) {

				Camera.Parameters parameters = camera.getParameters();
				for(int i = 0; i < parameters.getSupportedPictureSizes().size(); i++){
					Log.d(TAG, "preview size i = " + i + " width = " + parameters.getSupportedPictureSizes().get(i).width
							+ " height = "+parameters.getSupportedPictureSizes().get(i).height);
				}
				parameters.setPreviewFormat(ImageFormat.NV21);
				parameters.setPreviewSize(mPreviewWidth, mPreviewHeight);
				camera.setParameters(parameters);
				camera.setDisplayOrientation(90);
				cameraConfigured = true;

			}
		}
	}

	private void startPreview() {
		Log.d(TAG, "startPreview");

		if (cameraConfigured && camera != null) {

			// activate onPreviewFrame()
			// camera.setPreviewCallback(cameraPreviewCallback);
			camera.setPreviewCallback(RtspVideoRecorder.getInstance());
			
			// start captureThread
			RtspVideoRecorder.getInstance().start();

			camera.startPreview();
			inPreview = true;

		}
	}
	
	public boolean isReady() {
		return this.inPreview;
	}

}
