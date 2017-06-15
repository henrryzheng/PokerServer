package de.kp.rtspcamera.receiver;

import de.kp.rtspcamera.RtspNativeCodecsCamera;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootupReceiver extends BroadcastReceiver{
	 private final String TAG = "BootupReceiver";
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 Log.d(TAG, "onReceive");
		 //better delay some time.//
		 try {
			 Thread.sleep(2000);
		 } catch (InterruptedException e) {
		 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 Intent i = new Intent(context, RtspNativeCodecsCamera.class);
		 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 context.startActivity(i);
	}
}
