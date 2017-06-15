package de.kp.rtspcamera.mediaCodec.camera.preview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import de.kp.rtspcamera.mediaCodec.camera.PeerCamera;
import de.kp.rtspcamera.mediaCodec.socket.RtspRecorder;

/**
 * CameraActivity.java
 * <p/>
 * Created by x.q. on 2016/12/30.
 * <p/>
 * Copyright (c) 2016 x.q.
 */

public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener, PeerCamera.CamOpenOverCallback {
	private static final String TAG = "peer/TextureView";
	Context mContext;
	SurfaceTexture mSurface;
	float mPreviewRate = -1f;
	public CameraTextureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.setSurfaceTextureListener(this);
	}
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureAvailable...");
		mSurface = surface;
		PeerCamera.getInstance().doOpenCamera(this);
	}
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureDestroyed...");
//		PeerCamera.getInstance().doStopCamera();
//		RtspRecorder.getInstance().close();
		
		return true;
	}
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureSizeChanged...");
	}
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureUpdated...");
	}

	public SurfaceTexture getTextureSurface(){
		return mSurface;
	}

	public void setLayoutParams(ViewGroup.LayoutParams params, float previewRate) {
		this.mPreviewRate = previewRate;
		super.setLayoutParams(params);
	}


	@Override
	public void cameraHasOpened() {

		if (mSurface == null) {

			Log.e(TAG, "cameraHasOpened mSurface is null");
			return;
		}
		PeerCamera.getInstance().doStartPreview(mSurface, mPreviewRate);
	}
}
