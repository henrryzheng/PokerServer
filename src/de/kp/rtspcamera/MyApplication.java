package de.kp.rtspcamera;

import de.kp.rtspcamera.poker.PokerController;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mAppContext;
    
    static 
	   {
	       String libname = "Poker";
	       try
	       {
	           System.loadLibrary(libname);
	       }
	       catch(UnsatisfiedLinkError unsatisfiedlinkerror) { }
	   }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
       
    }

    public static Context getAppContext(){
        return mAppContext;
    }
}
