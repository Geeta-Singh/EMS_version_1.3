package com.bydesign.ems1;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by user on 7/18/2016.
 */

public class AlarmService extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    Context contx;
    JSONArray Faulty;
    JSONArray No_Info;
    JSONArray Working;
    JSONArray CalibrationMode;
    JSONArray Maintenance;
    private String[] xData;
    private float[] yData;
    static  int flag=0;
    static ArrayList<String> oldStates;
    // int id = 100;
    static Notification notification;
    // static Context context;//=getApplicationContext();
    static NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
       // System.out.print("inside alarm manager ");
        sharedPreferences = context.getSharedPreferences("EMS", Context.MODE_PRIVATE);
        contx=context;
      //  Toast.makeText(context,"service running", Toast.LENGTH_LONG).show();
        System.out.println("Notification called fron alarm manager");
        new HttpAsyncTaskback().execute();
        // new DeviceConditionFragment.HttpAsyncTask().execute();
        //   Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
    }
    public class HttpAsyncTaskback extends AsyncTask<String, Void, String> {
        int i = 0;
        String result;

        @Override
        protected String doInBackground(String... urls) {
            result = deviceConditionback();
            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("device condtion notification:" + status);

            try {
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    //  Toast.makeText(,"Error : Few parameters are missing", Toast.LENGTH_LONG).show();
                }
                else if(status.equalsIgnoreCase("Invalid")){
                    // unregisterReceiver(br);

                } else {

                    sendNotification(status);

                }
                //  Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String deviceConditionback() {
        System.out.println("Inside Device Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        try {

            String token = sharedPreferences.getString("token", null);
            String URL=sharedPreferences.getString("url", null);
            // = sharedPreferences.getString("url", null);
           // System.out.println("tokenntokenn in navigation bar: :" + token  +URL);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(URL+"/devicecondition");//"http://220.227.124.134:8070/smartcity/gassensor/devicecondition"
            String sid = "All",did="All",org="All";
            String details;
            String json = "";
           /* details= sharedPreferences.getString("deviceDetails", null);
            System.out.print(" \n@#$ sedice details"+details);

            try{
                if(details!=null) {
                    StringTokenizer st = new StringTokenizer(details, ",");

                    int i = 0;
                    while (st.hasMoreTokens()) {
                        //String h=st.nextToken();
                        if (i == 0)
                            sid = st.nextToken();
                        if (i == 1)
                            org = st.nextToken();
                        if (i == 2)
                            did = st.nextToken();
                        i++;
                        System.out.print(" \n sid " + sid + "       did  " + did + "        org" + org);
                    }
                }

            }catch (NullPointerException e){
                System.out.print(" \n sid " + sid + "       did  " + did + "        org" + org);
                e.printStackTrace();

            }*/
          //  System.out.print(" \n sid "+sid+"       did  "+did+"        org"+org);

            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            jsonObject2.accumulate("sid", sid);
            jsonObject2.accumulate("did", did);
            jsonObject2.accumulate("orgid", org);
            jsonObject2.accumulate("devtype", "CEMS");


            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
         //   System.out.print(" device bace json " + jsonObject2);

            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            System.out.println("F22222" + inputStream);
            result = convertInputStreamToString(inputStream);
        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();
        } catch (Exception e) {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();


        }
        return result;

    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification(String message) {
        System.out.println("Notification");
        notificationManager = (NotificationManager) contx.getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
        Intent notificationIntent;
        notificationIntent = new Intent(contx,navigationdrawer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(contx, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(contx);
        builder.setAutoCancel(true);
        builder.setTicker("Notification ");
        builder.setContentTitle("EMS Notification");
        builder.setContentText("Alert Device Condition ");
        builder.setSmallIcon(R.drawable.icon2);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
        builder.build();
        String msgText = message;
        notification = new Notification.BigTextStyle(builder).bigText(msgText).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
       // System.out.print("\n System.currentTimeMillis()"+System.currentTimeMillis()+notification);
        notificationManager.notify((int) System.currentTimeMillis(), notification);//(int)System.currentTimeMillis()

    }

   // Collection<String> tempWorkingList = new ArrayList(Arrays.asList("a", "b", "d", "e", "f", "gg", "h"));

    public void sendNotification(String data) throws JSONException {

        Collection<String> workingList =new ArrayList(Arrays.asList());
        JSONObject job = new JSONObject(data);
        Faulty = job.getJSONArray("faList");
        No_Info = job.getJSONArray("no_infoList");
        Working = job.getJSONArray("noList");
        Maintenance = job.getJSONArray("mmList");
        CalibrationMode = job.getJSONArray("cmList");


        String message = "";
        String temp;
        for(int i=0;i<Working.length();i++)
        {

            temp=Working.getJSONObject(i).getString("dname")+" "+Working.getJSONObject(i).getString("sid")+" "+Working.getJSONObject(i).getString("orgid")+" working";
            workingList.add(temp);

        }


        for(int i=0;i<Faulty.length();i++)
        {

            temp=Faulty.getJSONObject(i).getString("dname")+" "+Faulty.getJSONObject(i).getString("sid")+" "+Faulty.getJSONObject(i).getString("orgid")+" fa";
            workingList.add(temp);

        }
        for(int i=0;i<No_Info.length();i++)
        {

            temp=No_Info.getJSONObject(i).getString("dname")+" "+No_Info.getJSONObject(i).getString("sid")+" "+No_Info.getJSONObject(i).getString("orgid")+" noinfo";
            workingList.add(temp);

        }
        for(int i=0;i<Maintenance.length();i++)
        {

            temp=Maintenance.getJSONObject(i).getString("dname")+" "+Maintenance.getJSONObject(i).getString("sid")+" "+Maintenance.getJSONObject(i).getString("orgid")+" mm";
            workingList.add(temp);

        }
        for(int i=0;i<CalibrationMode.length();i++)
        {

            temp=CalibrationMode.getJSONObject(i).getString("dname")+" "+CalibrationMode.getJSONObject(i).getString("sid")+" "+CalibrationMode.getJSONObject(i).getString("orgid")+" cm";
            workingList.add(temp);

        }

        List<String> WL = new ArrayList<String>(workingList);
       // List<String> tWL = new ArrayList<String>(tempWorkingList);

       /* WL.removeAll(tempWorkingList);
        tWL.removeAll(workingList);
*/
        //Tokenzing String for getting Divice Name/Analyzer name

        String device="";
        ArrayList<String>deviceList = new ArrayList<>();
        ArrayList<String>currentStateList = new ArrayList<>();
        ArrayList<String>stateList = new ArrayList<>();
        ArrayList<String>orgList = new ArrayList<>();
        for(int i = 0;i<WL.size();i++) {
            StringTokenizer st = new StringTokenizer(WL.get(i)," ");
            while(st.hasMoreTokens()){
                device = st.nextToken();
                deviceList.add(device);
                break;
            }

        }

        //Tokenzing String for getting current Mode
        String cl="";
        for(int i = 0;i<WL.size();i++) {
            StringTokenizer st = new StringTokenizer(WL.get(i), " ");
            while (st.hasMoreTokens()) {
                cl=st.nextToken();
            }
            currentStateList.add(cl);
        }

        //Tokenzing String for getting Divice Organisation
        String org="";
        for(int i = 0;i<WL.size();i++) {
            int flag=2;
            StringTokenizer st = new StringTokenizer(WL.get(i), " ");
            while (st.hasMoreTokens()&&flag>=0) {
                org=st.nextToken();
                flag--;

            }
            System.out.println("\n @#$%org "+WL.get(i));
            orgList.add(org);
        }

        //Tokenzing String for getting Divice State
        String state="";
        for(int i = 0;i<WL.size();i++) {
            int flag=1;
            StringTokenizer st = new StringTokenizer(WL.get(i), " ");
            while (st.hasMoreTokens()&&flag>=0) {
                state=st.nextToken();
                flag--;
            }
            stateList.add(state);
        }

        if(flag==0){

            oldStates=currentStateList;
         //   System.out.print("\n Old list in flag"+oldStates);
            for(int i = 0;i<WL.size();i++) {
                if(currentStateList.get(i).equalsIgnoreCase("noinfo")) {

                    message = "\nDevice has shut down. "+"\n Analyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i) ;
                    System.out.println(message);
                    createNotification(message);
                }
                else if(currentStateList.get(i).equalsIgnoreCase("cm")){

                    message="\nDevice is performing calibration. "+" \n Analyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i) ;
                    System.out.println(message);
                    createNotification(message);
                }

                else if(currentStateList.get(i).equalsIgnoreCase("faulty")){
                    message="\nDevice has turned faulty."+" \nAnalyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i);
                    System.out.println(message);
                    createNotification(message);
                }
            }
            flag++;

        }else{
            for(int i=0;i<WL.size();i++){
                  if(oldStates.get(i).equalsIgnoreCase(currentStateList.get(i)))  {

                  }else{
                      if(currentStateList.get(i).equalsIgnoreCase("noinfo")) {

                          message = "\nDevice has shut down. "+"\n Analyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i) ;
                          System.out.println(message);
                          createNotification(message);
                      }
                      else if(currentStateList.get(i).equalsIgnoreCase("cm")){

                          message="\nDevice is performing calibration. "+" \n Analyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i) ;
                          System.out.println(message);
                          createNotification(message);
                      }

                      else if(currentStateList.get(i).equalsIgnoreCase("faulty")){
                          message="\nDevice has turned faulty."+" \nAnalyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i);
                          System.out.println(message);
                          createNotification(message);
                      }
                  }
            }
            oldStates=currentStateList;
           // System.out.print("\n Old list in else part "+oldStates+"\n"+currentStateList);
        }

       /* if(WL.size()>0){

            tempWorkingList=workingList;
            for(int i = 0;i<WL.size();i++) {
                if(currentStateList.get(i).equalsIgnoreCase("noinfo")) {

                    message = "\nDevice has shut down. "+"\n Analyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i) ;
                    System.out.println(message);
                    createNotification(message);
                }
                else if(currentStateList.get(i).equalsIgnoreCase("cm")){

                    message="\nDevice is performing calibration. "+" \n Analyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i) ;
                    System.out.println(message);
                    createNotification(message);
                }

                else if(currentStateList.get(i).equalsIgnoreCase("faulty")){
                    message="\nDevice has turned faulty."+" \nAnalyzer Name: " +deviceList.get(i)+ "\n State: " +stateList.get(i)+ "  Organisation: " +orgList.get(i);
                    System.out.println(message);
                    createNotification(message);
                }
            }
            System.out.println(":'(");

        }
        else{
            System.out.println("i m here ");

        }

        if(tWL.size()>0){

        }*/



    }
}
