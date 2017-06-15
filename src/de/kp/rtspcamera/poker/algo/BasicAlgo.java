package de.kp.rtspcamera.poker.algo;

import java.util.ArrayList;
import java.util.HashMap;

import de.kp.rtspcamera.MyApplication;
import de.kp.rtspcamera.poker.algo.AlgoFactory.onResultListener;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting.BroadcastType;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting.PlayType;
import de.kp.rtspcamera.poker.utils.SettingUtils;
import de.kp.rtspcamera.poker.utils.SoundUtils;
import de.kp.rtspcamera.poker.utils.Utils;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

public class BasicAlgo {
	private final String TAG = "BasicAlgo";
	public int mPreNewestCard = -1;
//	public int mRealNumOfPeople;
	public int mNumOfPeople;
	public int[][] mPokerResults;
	public int[][] mAlgoPlayerResults;	
	
	private byte[] result;
	
	private int newestCardSameCount = 0;
	
	public int maxPokerNumPerPerson = 0;
	
	public int preBroadcastType = -1;
	
	public ArrayList<Integer> playedPokers = new ArrayList<Integer>();
	
	public onResultListener listener;
	
	public void doAlgo(int numOfPeople, int[][] pokerResults){
		mNumOfPeople = numOfPeople;
		
		
		mPokerResults = pokerResults;
		//
//		if (PokerCustomerSetting.peopleNums == 0){
//			mRealNumOfPeople = mNumOfPeople;
//		}
//		else{
//			mRealNumOfPeople = PokerCustomerSetting.peopleNums;
//		}
//		Log.v(TAG,"doAlgo mRealNumOfPeople = " + mRealNumOfPeople);
//		Log.v(TAG,"doAlgo gameType = " + PokerCustomerSetting.gameType + " peopleNums = " + 
//				+ PokerCustomerSetting.peopleNums+ " broadcastType = " + PokerCustomerSetting.broadcastType+
//				" playType = " + PokerCustomerSetting.playType);
		if(preBroadcastType != PokerCustomerSetting.broadcastType){
			preBroadcastType = PokerCustomerSetting.broadcastType;
			SoundUtils.getInstance().stopAllSound();
		}

		
	}
	
	public void clearPlayedArray(){
		playedPokers.clear();
//		mRealNumOfPeople = 0;
	}
	
	public void setListener(onResultListener listener){
    	this.listener = listener;
    	result = new byte[8];
    }
	
	public void playDanPai(int newestCard){
		if(isNeedServerPlaySound()||isNeedCustomerPlaySound()){
			playSoundByMethod(newestCard);
		}
	}
	
	public void playSoundByMethod(int playInt){
		Log.v(TAG,"playSoundByMethod playInt = "+playInt);
		if(playInt != mPreNewestCard){
			playDanPaiSound(playInt);
			mPreNewestCard = playInt;
		}

	}
	
	public boolean isPokerDealCompleted(){
		return false;
	}
	
	private void playDanPaiSound(int pokerIndex){
		Log.v(TAG,"playDanPaiSound");
		if(pokerIndex>54 || pokerIndex<0){
			return;
		}
		if(isNeedServerPlaySound()&&isNeedPlayDanPai()){
			if(PokerCustomerSetting.broadcastType == BroadcastType.DANPAI_HUASE_ZIFU){
				
				SoundUtils.getInstance().playSoundColorAndNum(pokerIndex);
			}
			else if(PokerCustomerSetting.broadcastType == BroadcastType.DANPAI_ZIFU){
				
				SoundUtils.getInstance().playSoundNum(pokerIndex);
			}
		}
		else{
			SoundUtils.getInstance().stopAllSound();
		}
		
		if (isNeedCustomerPlaySound() && isNeedPlayDanPai()){
			byte[] result = new byte[4 + 4];
			result[0] = -1;
			result[1] = -10;
			result[2] = -100;
			if(PokerCustomerSetting.broadcastType == BroadcastType.DANPAI_HUASE_ZIFU){
				result[3] = BroadcastType.DANPAI_HUASE_ZIFU;
			}
			else if(PokerCustomerSetting.broadcastType == BroadcastType.DANPAI_ZIFU){
				result[3] = BroadcastType.DANPAI_ZIFU;
			}
			for (int j = 0; j < 4; j++){
				result[4+j] = Utils.intToBytes(pokerIndex)[j];
			}
			if(listener != null){
				listener.resultBack(result);
			}
		}
		
		
	}
	
	public boolean isNeedServerPlaySound(){
		boolean ret = false;
		if(PokerCustomerSetting.playType == PlayType.LENS_H8 || PokerCustomerSetting.playType == PlayType.PHONE_LENS_BOTH){
			ret = true;
		}
		return ret;
	}
	
	public boolean isNeedCustomerPlaySound(){
		boolean ret = false;
		if(PokerCustomerSetting.playType == PlayType.PHONE || PokerCustomerSetting.playType == PlayType.PHONE_LENS_BOTH){
			ret = true;
		}
		return ret;
	}
	
	public boolean isNeedPlayDanPai(){
		boolean ret = false;
		if(PokerCustomerSetting.broadcastType == BroadcastType.DANPAI_HUASE_ZIFU || PokerCustomerSetting.broadcastType == BroadcastType.DANPAI_ZIFU){
			ret = true;
		}
		return ret;
	}
	
	public boolean isNeedPlayResult(){
		boolean ret = false;
		if(PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_EVERYONE || PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_FIRST_SECOND
				|| PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_ZUIDA
				|| PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_ZUIDA_TYPE){
			ret = true;
		}
		return ret;
	}
}
