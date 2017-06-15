package de.kp.rtspcamera.poker.data;

public class PokerCustomerSetting {
	public static int gameType = 0;
	public static int peopleNums = 0;
	public static int broadcastType = 0;
	public static int playType = 0;
	
	
	public class GameType {
		public static final int JINHUA_JINHUABIG = 0;
		public static final int JINHUA_SHUNZIBIG = 1;
	}
	
	public class PeopleNums {
		public static final int PEOPLE_AUTO = 0;
		public static final int PEOPLE_1 = 1;
		public static final int PEOPLE_2 = 2;
		public static final int PEOPLE_3 = 3;
		public static final int PEOPLE_4 = 4;
		public static final int PEOPLE_5 = 5;
		public static final int PEOPLE_6 = 6;
		public static final int PEOPLE_7 = 7;
		public static final int PEOPLE_8 = 8;
		public static final int PEOPLE_9 = 9;
		public static final int PEOPLE_10 = 10;
		public static final int PEOPLE_11 = 11;
		public static final int PEOPLE_12 = 12;
		public static final int PEOPLE_13 = 13;
		public static final int PEOPLE_14 = 14;
		public static final int PEOPLE_15 = 15;
		public static final int PEOPLE_16 = 16;
		public static final int PEOPLE_17 = 17;
		public static final int PEOPLE_18 = 18;
		
		
	}
	
	public class BroadcastType {
		public static final int DANPAI_HUASE_ZIFU = 0;
		public static final int DANPAI_ZIFU = 1;
		public static final int DAXIAO_ZUIDA = 2;
		public static final int DAXIAO_ZUIDA_TYPE = 3;
		public static final int DAXIAO_FIRST_SECOND = 4;
		public static final int DAXIAO_EVERYONE = 5;
	}

	public class PlayType {
		public static final int PHONE = 0;
		public static final int LENS_H8 = 1;
		public static final int PHONE_LENS_BOTH = 2;
		public static final int PHONE_LENS_NO = 3;
	}

}
