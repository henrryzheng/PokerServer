package de.kp.net.rtp.viewer;

import com.orangelabs.core.ims.protocol.rtp.media.MediaSample;

public interface OnConnectListener {

	public void onConnect();
//	public void onSpsPpsResult(byte[] sps, byte[] pps);
	public void onReceiveResult(int[] result);
	public void onFrameData(MediaSample sample);
	public void onPlayerStop();
}
