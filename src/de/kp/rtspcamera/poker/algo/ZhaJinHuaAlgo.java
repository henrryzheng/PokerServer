package de.kp.rtspcamera.poker.algo;

import java.util.HashMap;

import de.kp.rtspcamera.MyApplication;
import de.kp.rtspcamera.poker.algo.AlgoFactory.onResultListener;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting.BroadcastType;
import de.kp.rtspcamera.poker.data.config.PokerDataConfig;
import de.kp.rtspcamera.poker.utils.SettingUtils;
import de.kp.rtspcamera.poker.utils.SoundUtils;
import de.kp.rtspcamera.poker.utils.Utils;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * Created by Administrator on 2017/1/4.
 */
public class ZhaJinHuaAlgo extends BasicAlgo {
	
    private final String TAG = "ZhajinhuaAlgo";
    
    private final int NUM_POKER_EVERY_PALYER = 3;
    
    private final int NUM_RESULT = 10;
    
    private boolean isJinHuaBig = true;
    
    private static ZhaJinHuaAlgo instance = new ZhaJinHuaAlgo();
    
    
    
    private ZhaJinHuaAlgo(){
    	maxPokerNumPerPerson = NUM_POKER_EVERY_PALYER;
    }
    
    public static ZhaJinHuaAlgo getInstance(){
    	return instance;
    }
    
    
    
    public void setJinHuaBig(boolean isJinHuaBig){
    	this.isJinHuaBig = isJinHuaBig;
    }




	@Override
	public void doAlgo(int numOfPeople,
			int[][] pokerResults) {
		// TODO Auto-generated method stub
		super.doAlgo(numOfPeople, pokerResults);
		
//		if (isPokerDealCompleted()){
		
			Log.v(TAG,"isPokerDealCompleted = true");
			long startTime = System.currentTimeMillis();
			compareReadyPokers();
			long endTime = System.currentTimeMillis();
			Log.i(TAG, "compareReadyPokers time = "+Integer.toString((int)(endTime-startTime)) + "ms");
//		}
//		else{
//			Log.v(TAG,"isPokerDealCompleted = false");
//		}
//		
    	
	}

	private void compareReadyPokers(){
		mAlgoPlayerResults = new int[mNumOfPeople][NUM_RESULT];
		for (int i = 0; i < mNumOfPeople; i++) {
			mAlgoPlayerResults[i] = doAlgoImpl(i + 1);
		}
		
		for(int i =0;i<mNumOfPeople;i++){
			for(int j =0;j<NUM_RESULT;j++){
				Log.v(TAG,"mAlgoPlayerResults["+i+"]"+"["+j+"]="+mAlgoPlayerResults[i][j]);
			}
		}
		
		sortByPlayerResult();
	}
	
	private void sortByPlayerResult() {
		// TODO Auto-generated method stub
		int[] mTempPlayerResult = new int[mNumOfPeople];
		for(int i =0; i<mNumOfPeople;i++)
			for(int j = mNumOfPeople-1; j>i;j--){
				Log.v(TAG, "i = "+i+"  j = "+j);
				if (sortBy2Player(j, j-1)==1){
					mTempPlayerResult = mAlgoPlayerResults[j];
					mAlgoPlayerResults[j]=mAlgoPlayerResults[j-1];
					mAlgoPlayerResults[j-1]=mTempPlayerResult;
					
				}
			}
		
		int[] finalResult = new int[mNumOfPeople];
		for(int i=0;i<mNumOfPeople;i++){
			Log.v(TAG,"排序"+i+" = "+mAlgoPlayerResults[i][NUM_RESULT-1]);
			Log.v(TAG,"排序 牌型"+i+" = "+mAlgoPlayerResults[i][0]);
			finalResult[i] = mAlgoPlayerResults[i][NUM_RESULT-1];
		}
		
		//playSound
		if (isNeedServerPlaySound() && isNeedPlayResult()){
			if(PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_ZUIDA){
				SoundUtils.getInstance().playSingleSoundEx(SoundUtils.KEY_POKER_SORT_BASE + finalResult[0]);
			}
			else if (PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_ZUIDA_TYPE){
				SoundUtils.getInstance().playSingleSoundEx(SoundUtils.KEY_POKER_SORT_BASE + finalResult[0]);
				//TODO
			}
			else if (PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_FIRST_SECOND){
				if(mNumOfPeople>1){
					int[] playInts = {SoundUtils.KEY_POKER_SORT_BASE + finalResult[0],SoundUtils.KEY_POKER_SORT_BASE + finalResult[1]};
					SoundUtils.getInstance().playMultiSoundsEx(playInts,true);
				}
				else{
					SoundUtils.getInstance().playSingleSoundEx(SoundUtils.KEY_POKER_SORT_BASE + finalResult[0]);
				}
			}
			else if (PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_EVERYONE){
				int[] playInts = new int[mNumOfPeople];
				for(int i = 0; i< mNumOfPeople; i++){
					playInts[i] = SoundUtils.KEY_POKER_SORT_BASE + finalResult[i];
//					Log.v(TAG,"DAXIAO_EVERYONE playSound"+i+" = "+finalResult[i]);
				}
				SoundUtils.getInstance().playMultiSoundsEx(playInts,true);
			}
		}
		else{
			SoundUtils.getInstance().stopAllSound();
		}
		
		//sendResult
		if (listener != null && isNeedCustomerPlaySound() && isNeedPlayResult()) {

			if(PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_ZUIDA){
				byte[] result = new byte[4 + 4];
				result[0] = -1;
				result[1] = -10;
				result[2] = -100;
				result[3] = BroadcastType.DAXIAO_ZUIDA;
				for (int j = 0; j < 4; j++){
					result[4+j] = Utils.intToBytes(finalResult[0])[j];
				}
				listener.resultBack(result);
			}
			else if (PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_ZUIDA_TYPE){
				byte[] result = new byte[8*4 + 4];
				result[0] = -1;
				result[1] = -10;
				result[2] = -100;
				result[3] = BroadcastType.DAXIAO_ZUIDA_TYPE;
				for (int i = 0; i < 4; i++){
					result[4+i] = Utils.intToBytes(finalResult[0])[i];
				}
				for(int i = 0; i < 7; i++){
					for (int j = 0; j < 4; j++){
						result[4*(i+2)+j] = Utils.intToBytes(mAlgoPlayerResults[0][i])[j];
					}
				}
				
				listener.resultBack(result);
			}
			else if (PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_FIRST_SECOND){
				byte[] result = new byte[2*4 + 4];
				result[0] = -1;
				result[1] = -10;
				result[2] = -100;
				result[3] = BroadcastType.DAXIAO_FIRST_SECOND;
				for (int i = 0; i< 2; i++){
					for (int j = 0; j < 4; j++){
						result[4*(i+1)+j] = Utils.intToBytes(finalResult[i])[j];
					}
				}
				listener.resultBack(result);
			}
			else if (PokerCustomerSetting.broadcastType == BroadcastType.DAXIAO_EVERYONE){
				byte[] result = new byte[finalResult.length*4 + 4];
				result[0] = -1;
				result[1] = -10;
				result[2] = -100;
				result[3] = BroadcastType.DAXIAO_EVERYONE;
				for (int i = 0; i< finalResult.length; i++){
					for (int j = 0; j < 4; j++){
						result[4*(i+1)+j] = Utils.intToBytes(finalResult[i])[j];
					}
					Log.v(TAG,"DAXIAO_EVERYONE"+i+" = "+finalResult[i]);
				}
				listener.resultBack(result);
			}
		}
	}
	
	//diff
	private int sortBy2Player(int playId1, int playId2) {

		int resultBy2Player = 0;
		// 通过种类比较顺序，如果种类不同
		if (mAlgoPlayerResults[playId1][0] > mAlgoPlayerResults[playId2][0]) {
			resultBy2Player = 1;
			return resultBy2Player;
		}
		if (mAlgoPlayerResults[playId1][0] < mAlgoPlayerResults[playId2][0]) {
			resultBy2Player = 0;
			return resultBy2Player;
		}

		// 如果种类相同，比较大小
		// 1。如果是豹子6
		if (mAlgoPlayerResults[playId1][0] == 6) {
			if(mAlgoPlayerResults[playId1][3]>=mAlgoPlayerResults[playId2][3])
				resultBy2Player = 1;
			else
				resultBy2Player = 0;
			return resultBy2Player;
		}
		//2.如果是顺金5
		if (mAlgoPlayerResults[playId1][0] == 5) {
			//特殊情况，都为123,比较花色
			if((mAlgoPlayerResults[playId1][1]==2&&mAlgoPlayerResults[playId1][2]==3&&mAlgoPlayerResults[playId1][3]==14)
					&&(mAlgoPlayerResults[playId2][1]==2&&mAlgoPlayerResults[playId2][2]==3&&mAlgoPlayerResults[playId2][3]==14)){
				if(mAlgoPlayerResults[playId1][4]>mAlgoPlayerResults[playId1 - 2][4]){
					resultBy2Player = 1;
				}
				else
					resultBy2Player = 0;
				return resultBy2Player;
			}
			//特殊情况，有一个为123
			if(mAlgoPlayerResults[playId1][1]==2&&mAlgoPlayerResults[playId1][2]==3&&mAlgoPlayerResults[playId1][3]==14){
				resultBy2Player = 0;
				return resultBy2Player;
			}
			if(mAlgoPlayerResults[playId2][1]==2&&mAlgoPlayerResults[playId2][2]==3&&mAlgoPlayerResults[playId2][3]==14){
				resultBy2Player = 1;
				return resultBy2Player;
			}
			//如果顺子一样,比较花色
			if((mAlgoPlayerResults[playId1][1]==mAlgoPlayerResults[playId2][1])
					&&(mAlgoPlayerResults[playId1][2]==mAlgoPlayerResults[playId2][2])
					&&(mAlgoPlayerResults[playId1][3]==mAlgoPlayerResults[playId2][3])){
				if(mAlgoPlayerResults[playId1][4]>mAlgoPlayerResults[playId2][4]){
					resultBy2Player=1;
				}
				else
					resultBy2Player=0;
				return resultBy2Player;
			}
			//如果顺子不一样,比较最大值
			if(mAlgoPlayerResults[playId1][3]>mAlgoPlayerResults[playId2][3])
				resultBy2Player=1;
			else
				resultBy2Player=0;
			return resultBy2Player;
		}
		
		//3.如果是同花4
		if (mAlgoPlayerResults[playId1][0] == 4) {
			
			if(mAlgoPlayerResults[playId1][3] > mAlgoPlayerResults[playId2][3])
				resultBy2Player=1;
			else if(mAlgoPlayerResults[playId1][3] < mAlgoPlayerResults[playId2][3])
				resultBy2Player=0;
			else{
				if(mAlgoPlayerResults[playId1][2] > mAlgoPlayerResults[playId2][2])
					resultBy2Player=1;
				else if(mAlgoPlayerResults[playId1][2] < mAlgoPlayerResults[playId2][2])
					resultBy2Player=0;
				else{
					if(mAlgoPlayerResults[playId1][1] > mAlgoPlayerResults[playId2][1])
						resultBy2Player=1;
					else if(mAlgoPlayerResults[playId1][1] < mAlgoPlayerResults[playId2][1])
						resultBy2Player=0;
					else{
						if(mAlgoPlayerResults[playId1][4] > mAlgoPlayerResults[playId2][4])
							resultBy2Player=1;
						else
							resultBy2Player=0;
					}
						
				}
			}
			return resultBy2Player;
		}
		
		//4.如果是顺子3
		if (mAlgoPlayerResults[playId1][0] == 3) {
			//特殊情况，都为123,比较花色
			if((mAlgoPlayerResults[playId1][1]==2&&mAlgoPlayerResults[playId1][2]==3&&mAlgoPlayerResults[playId1][3]==14)
					&&(mAlgoPlayerResults[playId2][1]==2&&mAlgoPlayerResults[playId2][2]==3&&mAlgoPlayerResults[playId2][3]==14)){
				if(mAlgoPlayerResults[playId1][6]>mAlgoPlayerResults[playId1 - 2][6]){
					resultBy2Player = 1;
				}
				else
					resultBy2Player = 0;
				return resultBy2Player;
			}
			//特殊情况，有一个为123
			if(mAlgoPlayerResults[playId1][1]==2&&mAlgoPlayerResults[playId1][2]==3&&mAlgoPlayerResults[playId1][3]==14){
				resultBy2Player = 0;
				return resultBy2Player;
			}
			if(mAlgoPlayerResults[playId2][1]==2&&mAlgoPlayerResults[playId2][2]==3&&mAlgoPlayerResults[playId2][3]==14){
				resultBy2Player = 1;
				return resultBy2Player;
			}
			//正常情况，从大比较到小
			if(mAlgoPlayerResults[playId1][3] > mAlgoPlayerResults[playId2][3])
				resultBy2Player=1;
			else if(mAlgoPlayerResults[playId1][3] < mAlgoPlayerResults[playId2][3])
				resultBy2Player=0;
			else{
				if(mAlgoPlayerResults[playId1][2] > mAlgoPlayerResults[playId2][2])
					resultBy2Player=1;
				else if(mAlgoPlayerResults[playId1][2] < mAlgoPlayerResults[playId2][2])
					resultBy2Player=0;
				else{
					if(mAlgoPlayerResults[playId1][1] > mAlgoPlayerResults[playId2][1])
						resultBy2Player=1;
					else if(mAlgoPlayerResults[playId1][1] < mAlgoPlayerResults[playId2][1])
						resultBy2Player=0;
					else{
						if(mAlgoPlayerResults[playId1][6] > mAlgoPlayerResults[playId2][6])
							resultBy2Player=1;
						else
							resultBy2Player=0;
					}
						
				}
			}
			return resultBy2Player;
		}
		//5，如果是对子2
		if (mAlgoPlayerResults[playId1][0] == 2) {
			if(mAlgoPlayerResults[playId1][7]>mAlgoPlayerResults[playId2][7])
				resultBy2Player=1;
			else if(mAlgoPlayerResults[playId1][7]<mAlgoPlayerResults[playId2][7])
				resultBy2Player=0;
			else{
				if(mAlgoPlayerResults[playId1][8]>mAlgoPlayerResults[playId2][8])
					resultBy2Player=1;
				else if(mAlgoPlayerResults[playId1][8]<mAlgoPlayerResults[playId2][8])
					resultBy2Player=0;
				else{
					if(mAlgoPlayerResults[playId1][6] > mAlgoPlayerResults[playId2][6])
						resultBy2Player=1;
					else
						resultBy2Player=0;
				}
					
			}
			return resultBy2Player;
		}
		//1.如果是单张1
		if (mAlgoPlayerResults[playId1][0] == 1) {
			if(mAlgoPlayerResults[playId1][3] > mAlgoPlayerResults[playId2][3])
				resultBy2Player=1;
			else if(mAlgoPlayerResults[playId1][3] < mAlgoPlayerResults[playId2][3])
				resultBy2Player=0;
			else{
				if(mAlgoPlayerResults[playId1][2] > mAlgoPlayerResults[playId2][2])
					resultBy2Player=1;
				else if(mAlgoPlayerResults[playId1][2] < mAlgoPlayerResults[playId2][2])
					resultBy2Player=0;
				else{
					if(mAlgoPlayerResults[playId1][1] > mAlgoPlayerResults[playId2][1])
						resultBy2Player=1;
					else if(mAlgoPlayerResults[playId1][1] < mAlgoPlayerResults[playId2][1])
						resultBy2Player=0;
					else{
						if(mAlgoPlayerResults[playId1][6] > mAlgoPlayerResults[playId2][6])
							resultBy2Player=1;
						else
							resultBy2Player=0;
					}
						
				}
			}
			return resultBy2Player;
		}
		
		return resultBy2Player;
	}

	private int[] doAlgoImpl(int playerId) {
        // TODO Auto-generated method stub
		int curIndex = playerId - 1;
		int[] dataNum = {0,0,0};
		int[] dataColor =  {0,0,0,0};
		for (int i = 0; i< NUM_POKER_EVERY_PALYER; i++){
			dataNum[i] = PokerDataConfig.pokerComponents[mPokerResults[curIndex][i]].getPokerNum();
			dataColor[i] = PokerDataConfig.pokerComponents[mPokerResults[curIndex][i]].getPokerColor();
		}
		
        //1，牌型，2，小点数，3，中点数  4.大点数  5.小点数花色，6中点数花色 7大点数花色 8对子点数,9对子剩余牌点数10.玩家id
        //1.牌型
        //6代表豹子
        //5代表顺金
        //4代表金花
        //3代表顺子
        //2代表对子
        //1代表单张
		
		
        int[] style = {0,0,0,0,0,0,0,0,0,playerId};


        int tempNum = 0;
        int tmepColor = 0;

        Log.v(TAG, " before playerId = " + playerId + "  dataNum0 = " + dataNum[0]
                + "  dataNum1 = " + dataNum[1] + "  dataNum2 = " + dataNum[2]
                + "\n  dataColor0 = " + dataColor[0] + "  dataColor1 = " + dataColor[1] + "  dataColor2 = " + dataColor[2]);
        for(int i =dataNum.length; i>0;i--)
            for(int j = 0; j<i-1;j++){
                //Log.v("ZhaJinHua", "i = "+i+"  j = "+j);
                if (dataNum[j]>dataNum[j+1]){
                    tempNum = dataNum[j];
                    dataNum[j]=dataNum[j+1];
                    dataNum[j+1]=tempNum;

                    tmepColor = dataColor[j];
                    dataColor[j]=dataColor[j+1];
                    dataColor[j+1]=tmepColor;


                }
            }

        Log.v(TAG, " after playerId = "+playerId+"  dataNum0 = "+dataNum[0]
                +"  dataNum1 = "+dataNum[1]+"  dataNum2 = "+dataNum[2]
                +"\n  dataColor0 = "+dataColor[0]+"  dataColor1 = "+dataColor[1]+"  dataColor2 = "+dataColor[2]);

        style[0]=0;
        style[1]=dataNum[0];
        style[2]=dataNum[1];
        style[3]=dataNum[2];
        style[4]=dataColor[0];
        style[5]=dataColor[1];
        style[6]=dataColor[2];

        // 豹子
        int sameNumCont = 1;
        for (int i = 0; i < dataNum.length-1; i++) {
            if (dataNum[i] == dataNum[i + 1]) {
                sameNumCont++;
            }
        }
        if (sameNumCont >= 3) {
            style[0] = 6;
            return style;
        }
        //顺金
        sameNumCont= 1;
        int sameColorCont=1;
        for (int i = 0; i < dataNum.length-1; i++) {
            if(dataNum[i]+1 == dataNum[i + 1]){
                sameNumCont++;
            }
        }

        for (int i = 0; i < dataNum.length-1; i++) {
            if(dataColor[i]== dataColor[i + 1]){
                sameColorCont++;
            }
        }

        if (sameNumCont >= 3||(dataNum[0]==2&&dataNum[1]==3&&dataNum[2]==14)) {
            if(sameColorCont>=3){//顺子+花色相同，为顺金
                style[0] = 5;
                return style;
            }
            else{//顺子+花色不同，为顺子或者拖拉机
            	if (isJinHuaBig){
            		style[0] = 3;
            	}
            	else{
            		style[0] = 4;
            	}
                return style;
            }


        }
        else{//非顺子
            if(sameColorCont>=3){//花色相同，为金花
            	if (isJinHuaBig){
            		style[0] = 4;
            	}
            	else{
            		style[0] = 3;
            	}
                
                return style;
            }
            //对子
            if(dataNum[0]==dataNum[1]){
                style[0]=2;
                style[7]=dataNum[0];
                style[8]=dataNum[2];
                return style;
            }
            if(dataNum[1]==dataNum[2]){
                style[0]=2;
                style[7]=dataNum[2];
                style[8]=dataNum[0];
                return style;
            }

        }
        //单牌
        style[0]=1;
        return style;
    }
}
