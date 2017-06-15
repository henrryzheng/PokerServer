package de.kp.rtspcamera.poker.data;

import de.kp.rtspcamera.poker.data.config.PokerDataConfig;


/**
 * Created by Administrator on 2017/1/4.
 */
public class PokerComponent {
    private int pokerColor;
    private int pokerNum;

    public PokerComponent(int color,int num){
        pokerColor = color;
        pokerNum = num;
    }
    
    public int getPokerColor(){
    	return pokerColor;
    }
    
    public int getPokerNum(){
    	return pokerNum;
    }
}