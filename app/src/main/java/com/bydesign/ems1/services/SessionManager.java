package com.bydesign.ems1.services;

/**
 * Created by PARIKSHIT on 1/19/2016.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;

import com.bydesign.ems1.AlarmService;
import com.bydesign.ems1.Login;



public class SessionManager {
        // Shared Preferences
        SharedPreferences pref;

        // Editor for Shared preferences
        Editor editor;

        // Context
        Context _context;

        // Shared pref mode
        int PRIVATE_MODE = 0;

        // Sharedpref file name
        private static final String PREF_NAME = "EMS";

        // All Shared Preferences Keys
        public static final String KEY_DEVICECURRENT = "currentDevice";
        private static final String IS_LOGIN = "IsLoggedIn";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_USERLEVEL = "userlevel";
        public static final String KEY_TOKEN = "token";
        public static final  String KEY_LATONG="latlong";
        public static final  String KEY_States="state";
        public static final String KEY_DEVICE="device";
        public static final String KEY_DEVICES="devices";
        public static final String KEY_DEVID="devid";
        public static final String KEY_URL="url";
        public static final String KEY_Sts="sts";
        public static final String KEY_Ets="ets";
        public static final String KEY_gran="gran";
        public static final String KEY_option="option";
        public static final String KEY_devtype="devtype";
        public static final String KEY_DETAILS="deviceDetails";
        public static final String KEY_FILENAME="filename";
        public static final String KEY_FILENAMEHistory="filenamehistory";
        public static final String KEY_FILENAMEAvg="filenameavg";
        public static final String KEY_FILENAMEData="filenamedata";
        public static final String KEY_FILENAMEdevice="filenamedevice";
        public static final String KEY_TABLEGRAPHDATA="tablegraphsession";
        public static final String KEY_TABLEGRAPHDATACurrent="tablegraphcurrent";
        public static final String KEY_TABLEGRAPHDATAAvg="tablegraphavg";
        public static final String KEY_FLAGE="tableflag";
            public static final String KEY_temp="temp";
    /* **************************sessioon  variable for avg data******************************/

    public static final String KEY_startT="stime";
    public static final String KEY_endT="etime";
    public static final String KEY_avgDevice="avgDevice";
    public static final String KEY_geanu="avgGranu";

    /************************ sessioon  variable for historical data*******************************/
    public static final String KEY_startHT="shtime";
   // public static final String KEY_endHT="ehtime";
    public static final String KEY_hisDevice="hisDevice";
  //  public static final String KEY_geanu="avgGranu";



        public static final String KEY_DEV="selectedDevice";
    // Constructor
    Context contxt;
        public SessionManager(Context context){
            this._context = context;
            contxt=context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }


    public void setTableContaint(String token){
        editor.putString(KEY_TABLEGRAPHDATA, token);
        editor.commit();
    }

    public void setTemparari(String data){
        editor.putString(KEY_temp, data);
        editor.commit();
    }
/* ******************************************    session methods for historical data***********************************/
    public void setDevicehis(String device){
        editor.putString(KEY_hisDevice, device);
        editor.commit();
    }

    public void setstartHTime(String stime){
        editor.putString(KEY_startHT, stime);
        editor.commit();
    }


    /* ******************************************    session methods for avg data***********************************/
    public void setDeviceAvg(String device){
        editor.putString(KEY_avgDevice, device);
        editor.commit();
    }

    public void setstartTime(String stime){
        editor.putString(KEY_startT, stime);
        editor.commit();
    }

    public void setendTime(String etime){
        editor.putString(KEY_endT, etime);
        editor.commit();
    }
    public void setgran(int gran){
        editor.putInt(KEY_geanu,gran);
        editor.commit();
    }

  /*  public void setdevicehistory(String dev){
        editor.putString(KEY_DEV, dev);
        editor.commit();
    }*/
    public void setTableContaintcurrent(String token){
        editor.putString(KEY_TABLEGRAPHDATACurrent, token);
        editor.commit();
    }

    public void setTableContaintavg(String token){
        editor.putString(KEY_TABLEGRAPHDATAAvg, token);
        editor.commit();
    }

    public void setCurrentDevice(String token){
        editor.putString(KEY_DEVICECURRENT, token);
        editor.commit();
    }

    //Creating login session
        public void createLoginSession(int userlevel,String username,String token,String latlong,String url,String devtype){
            // Storing login value as TRUE
            editor.putBoolean(IS_LOGIN, true);

            editor.putString(KEY_devtype,devtype);
            // Storing vlaues  in pref
            editor.putString(KEY_USERNAME, username);
            editor.putInt(KEY_USERLEVEL, userlevel);
            editor.putString(KEY_TOKEN,token);

            editor.putString(KEY_States,latlong);
            editor.putString(KEY_URL,url);
            // commit changes
            editor.commit();
        }

    public void addFlage(int flag){
        editor.putInt(KEY_FLAGE, flag);
        editor.commit();
    }
   // public static final String KEY_DEVICE="device";
    public void addSessionData(String device){
        editor.putString(KEY_DEVICE, device);
        editor.commit();
    }

    public void addSessionDevid(String devid){
        editor.putString(KEY_DEVID, devid);
        editor.commit();
    }
    public void addSessionFilehistory(String device){
        editor.putString(KEY_FILENAMEHistory, device);
        editor.commit();
    }

    public void addSessionFileNameAvg(String device){
        editor.putString(KEY_FILENAMEAvg, device);
        editor.commit();
    }

    public void addSessionFileNameData(String device){
        editor.putString(KEY_FILENAMEData, device);
        editor.commit();
    }

    public void addSessionFileNamedevice(String device){
        editor.putString(KEY_FILENAMEdevice, device);
        editor.commit();
    }

    public void addSessionFileName(String device){
        editor.putString(KEY_FILENAME, device);
        editor.commit();
    }


    public void addSessionForMap(String map){
        editor.putString(KEY_LATONG, map);
        editor.commit();
    }

    public void addSessionForCurrent(String device){
        editor.putString(KEY_DEVICES, device);
        editor.commit();
    }

    public void addDeviceDetail(String device){
        editor.putString(KEY_DETAILS, device);
        editor.commit();
    }
    public void addSessionData1(String device,String sts,String ets,int gran,String para){
        System.out.print(" inside session manager "+device+sts+ets+gran+para);
        editor.putString(KEY_DEVICES, device);
        editor.putString(KEY_option, para);
        editor.putString(KEY_Sts, sts);
        editor.putString(KEY_Ets, ets);
        editor.putInt(KEY_gran, gran);
        editor.commit();
    }

    public void addDatesInSession(String sts,String ets){
        editor.putString(KEY_Sts, sts);
        editor.putString(KEY_Ets, ets);
        editor.commit();
    }
    public void cleardata(){
        editor.remove("devices");
        editor.remove("sts");
        editor.remove("ets");
        editor.remove("gran");
        editor.remove("option");
        editor.commit();
    }

    public void addDeviceType(String devType){
        editor.putString(KEY_devtype, devType);
        editor.commit();
    }
    // Clearing session details
    public void logoutUser()
    {//Context context;
        // Clearing all data from Shared Preferences
      //  editor.remove("token");
        editor.clear();
        editor.commit();


        SharedVariables.setToken(_context,"");
        ComponentName receiver = new ComponentName(contxt, AlarmService.class);

        PackageManager pm = contxt.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

   //     Toast.makeText(this, 'Disabled broadcst receiver', Toast.LENGTH_LONG).show();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity

        _context.startActivity(i);

    }

}


