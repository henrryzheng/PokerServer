package de.kp.rtspcamera.poker.utils;

import de.kp.rtspcamera.poker.data.preference.SettingPreference;
import android.content.Context;

public class SettingUtils {
	 public static void saveGameType(int position, Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        preference.save(Constants.SETTING_GAME_TYPE, position);
    }

    public static void savePeopleNum(int position, Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        preference.save(Constants.SETTING_PEOPLE_NUM, position);
    }

    public static void saveBroadcatType(int position, Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        preference.save(Constants.SETTING_BROADCAST_TYPE, position);
    }

    public static void savePlayType(int position, Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        preference.save(Constants.SETTING_PLAY_TYPE, position);
    }
    
    public static void saveEV(int position, Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        preference.save(Constants.SETTING_EV, position);
    }

    public static int getGameType(Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        return preference.getInt(Constants.SETTING_GAME_TYPE, Constants.NO_SETTING_ID);
    }

    public static int getPeopleNum(Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        return preference.getInt(Constants.SETTING_PEOPLE_NUM, Constants.NO_SETTING_ID);
    }

    public static int getBroadcatType(Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        return preference.getInt(Constants.SETTING_BROADCAST_TYPE, Constants.NO_SETTING_ID);
    }

    public static int getPlayType(Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        return preference.getInt(Constants.SETTING_PLAY_TYPE, Constants.DEFAULT_PLAYTYPE);
    }
    
    public static int getEV(Context context) {
        SettingPreference preference = new SettingPreference(context, Constants.SETTING_INFO_FILE);
        return preference.getInt(Constants.SETTING_EV, Constants.NO_SETTING_ID);
    }

}
