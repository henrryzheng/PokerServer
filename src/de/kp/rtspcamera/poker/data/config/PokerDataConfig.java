package de.kp.rtspcamera.poker.data.config;

import de.kp.rtspcamera.poker.data.PokerComponent;

/**
 * Created by Administrator on 2017/1/4.
 */
public class PokerDataConfig {

    public static final PokerComponent[] pokerComponents = {
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_A),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_2),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_3),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_4),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_5),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_6),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_7),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_8),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_9),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_10),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_J),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_Q),
            new PokerComponent(PokerColor.FANGPIAN,PokerNum.POKER_K),
            
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_A),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_2),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_3),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_4),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_5),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_6),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_7),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_8),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_9),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_10),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_J),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_Q),
            new PokerComponent(PokerColor.MEIHUA,PokerNum.POKER_K),
            
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_A),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_2),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_3),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_4),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_5),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_6),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_7),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_8),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_9),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_10),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_J),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_Q),
            new PokerComponent(PokerColor.HONGTAO,PokerNum.POKER_K),
            
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_A),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_2),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_3),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_4),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_5),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_6),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_7),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_8),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_9),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_10),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_J),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_Q),
            new PokerComponent(PokerColor.HEITAO,PokerNum.POKER_K),
            
            new PokerComponent(PokerColor.NONE,PokerNum.POKER_XIAOWANG),
            new PokerComponent(PokerColor.NONE,PokerNum.POKER_DAWANG),
            
            new PokerComponent(PokerColor.NONE,PokerNum.POKER_AD),
            
            
    };


    public class PokerColor{
        public static final int NONE = 0;
        public static final int FANGPIAN = 1;
        public static final int MEIHUA = 2;
        public static final int HONGTAO = 3;
        public static final int HEITAO = 4;
    }

    public class PokerNum{
    	public static final int POKER_2 = 2;
    	public static final int POKER_3 = 3;
    	public static final int POKER_4 = 4;
    	public static final int POKER_5 = 5;
    	public static final int POKER_6 = 6;
    	public static final int POKER_7 = 7;
    	public static final int POKER_8 = 8;
    	public static final int POKER_9 = 9;
    	public static final int POKER_10 = 10;
    	public static final int POKER_J = 11;
    	public static final int POKER_Q = 12;
    	public static final int POKER_K = 13;
    	public static final int POKER_A = 14;
    	public static final int POKER_XIAOWANG = 100;
    	public static final int POKER_DAWANG = 101;
    	public static final int POKER_AD = 102;
    	
    }

}
