package de.kp.rtspcamera.poker.algo;

import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting.GameType;

public class AlgoFactory {
	
	private static AlgoFactory instance = new AlgoFactory();
	private onResultListener listener;
	private AlgoFactory(){
		
	}
	
	public static AlgoFactory getInstance(){
		return instance;
	}
	
	public BasicAlgo createAlgo(){
		switch(PokerCustomerSetting.gameType){
			case GameType.JINHUA_JINHUABIG:
				ZhaJinHuaAlgo.getInstance().setJinHuaBig(true);
				ZhaJinHuaAlgo.getInstance().setListener(listener);
				return ZhaJinHuaAlgo.getInstance();
			case GameType.JINHUA_SHUNZIBIG:
				ZhaJinHuaAlgo.getInstance().setJinHuaBig(false);
				ZhaJinHuaAlgo.getInstance().setListener(listener);
				return ZhaJinHuaAlgo.getInstance();
			default:
				return new BasicAlgo();
		}
	}
	
	public void setListener (onResultListener l) {
		this.listener = l;
	}
	
	public interface onResultListener {
		
		public void resultBack (byte[] result);
	}
	
	
}
