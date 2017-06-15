package de.kp.rtspcamera.poker.utils;

import java.util.HashMap;
import java.util.Vector;

import de.kp.rtspcamera.MyApplication;
import de.kp.rtspcamera.R;
import de.kp.rtspcamera.RtspNativeCodecsCamera;
import de.kp.rtspcamera.poker.data.config.PokerDataConfig;
import de.kp.rtspcamera.poker.data.config.PokerDataConfig.PokerColor;
import de.kp.rtspcamera.poker.data.config.PokerDataConfig.PokerNum;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class SoundUtils {
	
	public static final int KEY_POKER_TYPE_FANGPIAN = PokerColor.FANGPIAN;
	public static final int KEY_POKER_TYPE_MEIHUA   = PokerColor.MEIHUA;
	public static final int KEY_POKER_TYPE_HONGTAO  = PokerColor.HONGTAO;
	public static final int KEY_POKER_TYPE_HEITAO   = PokerColor.HEITAO;
	
	public static final int KEY_POKER_A             = PokerNum.POKER_A * 10;
	public static final int KEY_POKER_2             = PokerNum.POKER_2 * 10;
	public static final int KEY_POKER_3             = PokerNum.POKER_3 * 10;
	public static final int KEY_POKER_4             = PokerNum.POKER_4 * 10;
	public static final int KEY_POKER_5             = PokerNum.POKER_5 * 10;
	public static final int KEY_POKER_6             = PokerNum.POKER_6 * 10;
	public static final int KEY_POKER_7             = PokerNum.POKER_7 * 10;
	public static final int KEY_POKER_8             = PokerNum.POKER_8 * 10;
	public static final int KEY_POKER_9             = PokerNum.POKER_9 * 10;
	public static final int KEY_POKER_10            = PokerNum.POKER_10 * 10;
	public static final int KEY_POKER_J             = PokerNum.POKER_J * 10;
	public static final int KEY_POKER_Q             = PokerNum.POKER_Q * 10;
	public static final int KEY_POKER_K             = PokerNum.POKER_K * 10;
	
	public static final int KEY_POKER_XIAOWANG      = PokerNum.POKER_XIAOWANG * 10;
	public static final int KEY_POKER_DAWANG        = PokerNum.POKER_DAWANG * 10;
	
	public static final int KEY_POKER_AD            = PokerNum.POKER_AD * 10;
	
	public static final int KEY_POKER_SORT_BASE     = 10000;
	public static final int KEY_POKER_SORT_1        = KEY_POKER_SORT_BASE + 1;
	public static final int KEY_POKER_SORT_2        = KEY_POKER_SORT_BASE + 2;
	public static final int KEY_POKER_SORT_3        = KEY_POKER_SORT_BASE + 3;
	public static final int KEY_POKER_SORT_4        = KEY_POKER_SORT_BASE + 4;
	public static final int KEY_POKER_SORT_5        = KEY_POKER_SORT_BASE + 5;
	public static final int KEY_POKER_SORT_6        = KEY_POKER_SORT_BASE + 6;
	public static final int KEY_POKER_SORT_7        = KEY_POKER_SORT_BASE + 7;
	public static final int KEY_POKER_SORT_8        = KEY_POKER_SORT_BASE + 8;
	public static final int KEY_POKER_SORT_9        = KEY_POKER_SORT_BASE + 9;
	public static final int KEY_POKER_SORT_10        = KEY_POKER_SORT_BASE + 10;
	public static final int KEY_POKER_SORT_11        = KEY_POKER_SORT_BASE + 11;
	public static final int KEY_POKER_SORT_12        = KEY_POKER_SORT_BASE + 12;
	public static final int KEY_POKER_SORT_13        = KEY_POKER_SORT_BASE + 13;
	public static final int KEY_POKER_SORT_14        = KEY_POKER_SORT_BASE + 14;
	public static final int KEY_POKER_SORT_15        = KEY_POKER_SORT_BASE + 15;
	public static final int KEY_POKER_SORT_16        = KEY_POKER_SORT_BASE + 16;
	public static final int KEY_POKER_SORT_17        = KEY_POKER_SORT_BASE + 17;
	public static final int KEY_POKER_SORT_18        = KEY_POKER_SORT_BASE + 18;
	public static final int KEY_POKER_SORT_19        = KEY_POKER_SORT_BASE + 19;
	public static final int KEY_POKER_SORT_20        = KEY_POKER_SORT_BASE + 20;
	
	
	private final String TAG = "SoundUtils";	
		    
    private AudioManager mAudioManager;  
    
    private Handler mHandler = new Handler();  
  
    private Vector<Integer> mKillSoundQueue = new Vector<Integer>();  
  
  
    private long delay = 80;  
  
    private long seperateTime = 80;  
    
    private final long DEFAULT_DELAY = 80;  
    
    private final long DEFAULT_SEPERATETIME = 100;
    
    private final long HUASE_DELAY = 80;  
    //
    private final long HUASE_SEPERATETIME = 700;
    
    private final long MULTI_DELAY = 1000;  
    //
    private final long MULTI_SEPERATETIME = 200;
  
    private float rate = 1.0f;  
  
    private String locale;  
  
    private static SoundUtils _instance;  
    
    private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundData;
	private Context mContext;
	private float streamVolume = 1.0f;
	
	private HandlerThread handlerThread;

	private Handler threadHandler;
	
	private static final int MESSAGE_PLAY_SOUND_SINGLE = 1;
	private static final int MESSAGE_PLAY_SOUND_MULTI = 2;
	private static final int MESSAGE_init_SOUND = 3;
	private final String PLAYSOUNDSTRING = "playsound";
	
  
    /** 
     * Requests the instance of the Sound SoundUtils and creates it if it does not 
     * exist. 
     *  
     * @return Returns the single instance of the SoundUtils 
     */  
    public static synchronized SoundUtils getInstance() {  
        if (_instance == null)  
            _instance = new SoundUtils();  
        return _instance;  
    }  
  
    /** 
     *  
     */  
    private SoundUtils() {  
    }  
  
    /** 
     * Initialises the storage for the sounds 
     *  
     * @param theContext The Application context 
     */  
    
	//初始化声音
	public void initSound(Context context) {
        Log.d(TAG, "initSound ");
		mContext = context;
		mSoundData = new HashMap<Integer, Integer>();
		mSoundPool = new SoundPool(1,  
                AudioManager.STREAM_MUSIC, 0);   
		mAudioManager = (AudioManager) mContext  
                .getSystemService(Context.AUDIO_SERVICE);   
		streamVolume 	= mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume 	= streamVolume/mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		//
		handlerThread = new HandlerThread("pokersound");
//		handlerThread.setPriority(Thread.MAX_PRIORITY);
		handlerThread.start();
		threadHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
//				Log.d(TAG, "handleMessage msg.what = " + msg.what);
				switch (msg.what) {
					case MESSAGE_PLAY_SOUND_SINGLE:
						playSound(msg.arg1);
						break;
					case MESSAGE_PLAY_SOUND_MULTI:
						Bundle b = msg.getData();
						String arrayString = b.getString(PLAYSOUNDSTRING);
						if(arrayString != null){
							String[] stringArray = arrayString.split("-");
							int[] ints = new int[stringArray.length - 1];
							for(int i = 0; i< stringArray.length - 1; i++){
								ints[i] = Integer.parseInt(stringArray[i+1]);
							}
							try {
								playMutilSounds(ints);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						break;
					case MESSAGE_init_SOUND:
						loadSoundRes();
					default:
						break;
				}
			}
		};
		
		Message msg = threadHandler.obtainMessage();
		msg.what = MESSAGE_init_SOUND;
		msg.sendToTarget();
		
    }
	
	public void onDestroy(){
		handlerThread.quit();
		cleanup();
	}
	
	public void setDelay(int delay){
		this.delay = delay;
	}
	
	public void setSeperateTime(int seperateTime){
		this.seperateTime = seperateTime;
	}
	
	private void loadSoundRes(){
		//通过load方法加载指定音频流，并将返回的音频ID放入mSoundData中
				mSoundData.put(KEY_POKER_TYPE_FANGPIAN, mSoundPool.load(mContext, R.raw.poker_type_fangpian, 1));
				mSoundData.put(KEY_POKER_TYPE_MEIHUA, mSoundPool.load(mContext, R.raw.poker_type_meihua, 1));
				mSoundData.put(KEY_POKER_TYPE_HONGTAO, mSoundPool.load(mContext, R.raw.poker_type_hongtao, 1));
				mSoundData.put(KEY_POKER_TYPE_HEITAO, mSoundPool.load(mContext, R.raw.poker_type_heitao, 1));
				
				mSoundData.put(KEY_POKER_A, mSoundPool.load(mContext, R.raw.poker_1, 1));
				mSoundData.put(KEY_POKER_2, mSoundPool.load(mContext, R.raw.poker_2, 1));
				mSoundData.put(KEY_POKER_3, mSoundPool.load(mContext, R.raw.poker_3, 1));
				mSoundData.put(KEY_POKER_4, mSoundPool.load(mContext, R.raw.poker_4, 1));
				mSoundData.put(KEY_POKER_5, mSoundPool.load(mContext, R.raw.poker_5, 1));
				mSoundData.put(KEY_POKER_6, mSoundPool.load(mContext, R.raw.poker_6, 1));
				mSoundData.put(KEY_POKER_7, mSoundPool.load(mContext, R.raw.poker_7, 1));
				mSoundData.put(KEY_POKER_8, mSoundPool.load(mContext, R.raw.poker_8, 1));
				mSoundData.put(KEY_POKER_9, mSoundPool.load(mContext, R.raw.poker_9, 1));
				mSoundData.put(KEY_POKER_10, mSoundPool.load(mContext, R.raw.poker_10, 1));
				mSoundData.put(KEY_POKER_J, mSoundPool.load(mContext, R.raw.poker_j, 1));
				mSoundData.put(KEY_POKER_Q, mSoundPool.load(mContext, R.raw.poker_q, 1));
				mSoundData.put(KEY_POKER_K, mSoundPool.load(mContext, R.raw.poker_k, 1));
				
				
				mSoundData.put(KEY_POKER_XIAOWANG, mSoundPool.load(mContext, R.raw.poker_xiaowang, 1));
				mSoundData.put(KEY_POKER_DAWANG, mSoundPool.load(mContext, R.raw.poker_dawang, 1));
				
				mSoundData.put(KEY_POKER_AD, mSoundPool.load(mContext, R.raw.poker_a, 1));
				
				mSoundData.put(KEY_POKER_SORT_1, mSoundPool.load(mContext, R.raw.poker_sort_1, 1));
				mSoundData.put(KEY_POKER_SORT_2, mSoundPool.load(mContext, R.raw.poker_sort_2, 1));
				mSoundData.put(KEY_POKER_SORT_3, mSoundPool.load(mContext, R.raw.poker_sort_3, 1));
				mSoundData.put(KEY_POKER_SORT_4, mSoundPool.load(mContext, R.raw.poker_sort_4, 1));
				mSoundData.put(KEY_POKER_SORT_5, mSoundPool.load(mContext, R.raw.poker_sort_5, 1));
				mSoundData.put(KEY_POKER_SORT_6, mSoundPool.load(mContext, R.raw.poker_sort_6, 1));
				mSoundData.put(KEY_POKER_SORT_7, mSoundPool.load(mContext, R.raw.poker_sort_7, 1));
				mSoundData.put(KEY_POKER_SORT_8, mSoundPool.load(mContext, R.raw.poker_sort_8, 1));
				mSoundData.put(KEY_POKER_SORT_9, mSoundPool.load(mContext, R.raw.poker_sort_9, 1));
				mSoundData.put(KEY_POKER_SORT_10, mSoundPool.load(mContext, R.raw.poker_sort_10, 1));
				mSoundData.put(KEY_POKER_SORT_11, mSoundPool.load(mContext, R.raw.poker_sort_11, 1));
				mSoundData.put(KEY_POKER_SORT_12, mSoundPool.load(mContext, R.raw.poker_sort_12, 1));
				mSoundData.put(KEY_POKER_SORT_13, mSoundPool.load(mContext, R.raw.poker_sort_13, 1));
				mSoundData.put(KEY_POKER_SORT_14, mSoundPool.load(mContext, R.raw.poker_sort_14, 1));
				mSoundData.put(KEY_POKER_SORT_15, mSoundPool.load(mContext, R.raw.poker_sort_15, 1));
				mSoundData.put(KEY_POKER_SORT_16, mSoundPool.load(mContext, R.raw.poker_sort_16, 1));
				mSoundData.put(KEY_POKER_SORT_17, mSoundPool.load(mContext, R.raw.poker_sort_17, 1));
				mSoundData.put(KEY_POKER_SORT_18, mSoundPool.load(mContext, R.raw.poker_sort_18, 1));
				mSoundData.put(KEY_POKER_SORT_19, mSoundPool.load(mContext, R.raw.poker_sort_19, 1));
				mSoundData.put(KEY_POKER_SORT_20, mSoundPool.load(mContext, R.raw.poker_sort_20, 1));
	}
	

  
      
    /** 
     * play the sound loaded to the SoundPool by the key we set 
     * @param key  the key in the map 
     */  
    private void playSound(int key) {  
    	
        streamVolume = mAudioManager  
                .getStreamVolume(AudioManager.STREAM_MUSIC);  
        streamVolume = streamVolume  
                / mAudioManager  
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
//        Log.d(TAG, "playSound " + key);
        int soundId = mSoundPool.play(  
                mSoundData.get(key), streamVolume,  
                streamVolume, 1, 0, rate);  
        //sleep for a while for SoundPool play   
        try {
			Thread.sleep(seperateTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        mKillSoundQueue.add(soundId);  
        // schedule the current sound to stop after set milliseconds  
        mHandler.postDelayed(new Runnable() {  
            public void run() {  
                if (!mKillSoundQueue.isEmpty()) {  
                    mSoundPool.stop(mKillSoundQueue  
                            .firstElement());  
                }  
            }  
        }, delay);  
  
    }  
      
    /** 
     *  
     * @param key  the key in the map 
     */  
    private void playLoopedSound(String key) {  
  
        streamVolume = mAudioManager  
                .getStreamVolume(AudioManager.STREAM_MUSIC);  
        streamVolume = streamVolume  
                / mAudioManager  
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
          
        int soundId = mSoundPool.play(  
        		mSoundData.get(key), streamVolume,  
                streamVolume, 1, -1, rate);  
        
        mKillSoundQueue.add(soundId);  
        // schedule the current sound to stop after set milliseconds  
        mHandler.postDelayed(new Runnable() {  
            public void run() {  
                if (!mKillSoundQueue.isEmpty()) {  
                    mSoundPool.stop(mKillSoundQueue  
                            .firstElement());  
                }  
            }  
        }, delay);  
    }  
      
  /** 
   * play the sounds have loaded in SoundPool  
   * @param keys the files key stored in the map 
   * @throws InterruptedException 
   */  
    private void playMutilSounds(final int keys[])  
            throws InterruptedException {  
    	 streamVolume = mAudioManager  
                 .getStreamVolume(AudioManager.STREAM_MUSIC);  
         streamVolume = streamVolume  
                 / mAudioManager  
                         .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		        for (int key : keys) {  
//		            Log.d(TAG, "playMutilSounds " + key);  
		            if (mSoundData.containsKey(key)) {  
		                int soundId = mSoundPool.play(  
		                        mSoundData.get(key),  
		                        streamVolume, streamVolume, 1, 0,  
		                        rate);  
		                //sleep for a while for SoundPool play   
		                try {
							Thread.sleep(seperateTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
		                mKillSoundQueue.add(soundId);  
		            }  
		        }  
		  
		        // schedule the current sound to stop after set milliseconds  
		        mHandler.postDelayed(new Runnable() {  
		            public void run() {  
		                if (!mKillSoundQueue.isEmpty()) {  
		                    mSoundPool.stop(mKillSoundQueue  
		                            .firstElement());  
		                }  
		            }  
		        }, delay);
          
  
    }  
    
  
    /** 
     * Stop a Sound 
     *  
     * @param index - index of the sound to be stopped 
     */  
    public void stopSound(int index) {  
        mSoundPool.stop(mSoundData.get(index));  
    }  
    
    public void stopAllSound(){
//    	Log.d(TAG,"stopAllSound");
    	threadHandler.removeMessages(MESSAGE_PLAY_SOUND_SINGLE);
    	threadHandler.removeMessages(MESSAGE_PLAY_SOUND_MULTI);
    }
  
    /** 
     * Deallocates the resources and Instance of SoundManager 
     */  
    public void cleanup() {  
        mSoundPool.release();  
        mSoundPool = null;  
        mSoundData.clear();  
        mAudioManager.unloadSoundEffects();  
        _instance = null;  
  
    }  
  
    
  
    /** 
     * set the speed of soundPool 
     *  
     * @param i i<0 means slow i= 0 means normal i>0 means fast 
     */  
    public void setVoiceSpeed(int i) {  
        if (i > 0) {  
            rate = 1.2f;  
        }  
        else if (i < 0) {  
            rate = 0.8f;  
        }  
        else {  
            rate = 1.0f;  
        }  
  
    }  
  
    /** 
     * set the delay after one number's sound have played 
     *  
     * @param i i<0 means short i= 0 means normal i>0 means long 
     */  
    public void setVoiceDelay(int i) {  
        if (i > 0) {  
            seperateTime = 700;  
        }  
        else if (i < 0) {  
            seperateTime = 400;  
        }  
        else {  
            seperateTime = 500;  
        }  
  
    }  
  
    public String getLocale() {  
        return locale;  
    }  
  
    public void setLocale(String locale) {  
        this.locale = locale;  
    } 
    
	
    public void playSoundColorAndNum(int pokerColor, int pokerNum){
//		Log.d(TAG,"playSoundColorAndNum pokerColor = " + pokerColor + " pokerNum = " + pokerNum);
		int pokerNumKey = pokerNum * 10;
		if(pokerColor<5 && pokerColor>0){
			int[] playInts = {pokerColor,pokerNumKey};
			playMultiSoundsEx(playInts,false);
		}
		else{
			playSingleSoundEx(pokerNumKey);
		}	
	}
	
	
	public void playSoundColorAndNum(int pokerResultFromeJni){
		
		int pokerColor = PokerDataConfig.pokerComponents[pokerResultFromeJni].getPokerColor();
		int pokerNum = PokerDataConfig.pokerComponents[pokerResultFromeJni].getPokerNum();
		
//		Log.d(TAG,"playSoundColorAndNum pokerColor = " + pokerColor + " pokerNum = " + pokerNum);
		int pokerNumKey = pokerNum * 10;
		if(pokerColor<5 && pokerColor>0){
			int[] playInts = {pokerColor,pokerNumKey};
			playMultiSoundsEx(playInts,false);
		}
		else{
			playSingleSoundEx(pokerNumKey);
		}	
	}

	public void playSoundNum(int pokerResultFromeJni){
		int pokerNum = PokerDataConfig.pokerComponents[pokerResultFromeJni].getPokerNum();
//		Log.d(TAG,"playSoundNum pokerNum = " + pokerNum);
		int pokerNumKey = pokerNum * 10;
		delay = DEFAULT_DELAY;
		seperateTime = DEFAULT_SEPERATETIME;
		playSingleSoundEx(pokerNumKey);
	}
	
	public void playMultiSoundsEx(int[] playInts, boolean isEx){
		Log.d(TAG,"playMultiSoundsEx");
		if(isEx){
			delay = MULTI_DELAY;
			seperateTime = MULTI_SEPERATETIME;
		}
		else{
			delay = HUASE_DELAY;
			seperateTime = HUASE_SEPERATETIME;
		}
		String bundleString = "";
		for(int i = 0; i< playInts.length; i++) {
			bundleString += "-"+playInts[i];
		}
		Message msg = threadHandler.obtainMessage();
//		Message msg=new Message();        
		Bundle b = new Bundle();
		b.putString(PLAYSOUNDSTRING, bundleString);
		msg.setData(b);
		msg.what = MESSAGE_PLAY_SOUND_MULTI;
		msg.sendToTarget();
//		threadHandler.sendMessage(msg);
		Log.d(TAG,"playMultiSoundsEx bundleString = " + bundleString);
	}
	
	public void playSingleSoundEx(int playInt){
		delay = DEFAULT_DELAY;
		seperateTime = DEFAULT_SEPERATETIME;
		Message msg = threadHandler.obtainMessage();
//		Message msg=new Message();    
		msg.what = MESSAGE_PLAY_SOUND_SINGLE;
		msg.arg1 = playInt;
		msg.sendToTarget();
//		threadHandler.sendMessage(msg);
		
	}
}
