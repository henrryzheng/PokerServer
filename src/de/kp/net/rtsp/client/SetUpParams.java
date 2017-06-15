package de.kp.net.rtsp.client;

public class SetUpParams {
	
	private int gameType = -1;
	private int peopleNum = -1;
	private int broadcastType = -1;
	private int playType = -1;
	private int ev = 0;
	
	public SetUpParams () {
		
	}
	
	public SetUpParams(int gameType, int peopleNum, int broadcastType, int playType,int ev) {
		this.gameType = gameType;
		this.peopleNum = peopleNum;
		this.broadcastType = broadcastType;
		this.playType = playType;
		this.ev = ev;
	}
	
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public int getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}
	public int getBroadcastType() {
		return broadcastType;
	}
	public void setBroadcastType(int broadcastType) {
		this.broadcastType = broadcastType;
	}
	public int getPlayType() {
		return playType;
	}
	public void setPlayType(int playType) {
		this.playType = playType;
	}
	
	public int getEV() {
		return ev;
	}
	
	public void setEV(int ev){
		this.ev = ev;
	}
	

}
