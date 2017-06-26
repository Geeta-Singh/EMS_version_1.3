package com.bydesign.ems1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bydesign.ems1.Fragments.OOM;
import com.bydesign.ems1.model.Token;
import com.bydesign.ems1.services.SessionManager;
import com.bydesign.ems1.services.SharedVariables;

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

public class Login extends AppCompatActivity {
    EditText username,password,otp;
    String user, pwd,url;
    Button relogin,login;
    TextInputLayout card;
    String devType,States;
   // SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        otp=(EditText)findViewById(R.id.otp);
        otp.setVisibility(View.INVISIBLE);
        relogin=(Button)findViewById(R.id.relogin);
        relogin.setVisibility(View.INVISIBLE);
        card=(TextInputLayout)findViewById(R.id.card__otp);
        card.setVisibility(View.INVISIBLE);
        login=(Button)findViewById(R.id.login);
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
      //  ConnectionCheck();
    }
    public void onBackPressed(){
       // super.onBackPressed();
     //   finish();
      //  System.exit(0);
        moveTaskToBack(true);


    }

    public void onDestroy(){
        super.onDestroy();
        finish();
    }
    ProgressDialog progressDialog = null;


    /*
    *
    * Login button click
    *
    * */
    public void Login(View view) {
        if (isConnected()) {

            username = (EditText) findViewById(R.id.user);
            password = (EditText) findViewById(R.id.pwd);

            try {
                user = username.getText().toString();
                pwd = password.getText().toString();
                System.out.println(" login credential user: " + user + "  pwd :" + pwd);
                if(check(user,pwd)) {

                    progressDialog = ProgressDialog.show(Login.this, "", "Authenticating...", true);

                    new HttpAsyncTask().execute();
                }
                else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getCurrentFocus(),"  Username and password are mandatory", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                      snackbar.show();
                   // progressDialog.dismiss();
                }

            } catch (NullPointerException e) {
                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(),"   Username and password are mandatory", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
                e.printStackTrace();
            }
       }
        else {

            Snackbar snackbar;
            snackbar = Snackbar.make(view,"Please check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }

    String Otp;
    public void ReLogin(View view){
        if (isConnected()) {
            //  Toast.makeText(getApplicationContext(),"you are connected",Toast.LENGTH_LONG).show();

            try {
                Otp=otp.getText().toString();
                user = username.getText().toString();
                pwd = password.getText().toString();
                System.out.println("user: " + user + "  pwd :" + pwd);
                if(checkRelogin(user, pwd, Otp)) {
                    progressDialog = ProgressDialog.show(Login.this, "", "Authenticating...", true);
                    //Creating a snackbar object

                    new HttpAsyncTaskRelogin().execute();
                }
                else {
                    Snackbar snackbar;

                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getCurrentFocus(),"  Username and password are mandatory", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                    // Toast.makeText(getApplicationContext(), "Username and password are mandatory ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            } catch (NullPointerException e) {
                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getCurrentFocus(),"   Username and password are mandatory", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                //Displaying the snackbar using the show method()
                snackbar.show();
                //   Toast.makeText(getApplicationContext(), "Username and password are mandatory ", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else {

            Snackbar snackbar;
            snackbar = Snackbar.make(view,"Please check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
            // Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_LONG).show();
        }
    }

    public boolean check(String user,String pwd){
        if(user.equalsIgnoreCase("")||pwd.equalsIgnoreCase("")){
            return false;
        }
        else return true;
    }

    public boolean checkRelogin(String user,String pwd,String otp){
        if(user.equalsIgnoreCase("")||pwd.equalsIgnoreCase("")||otp.equalsIgnoreCase("")){
            return false;
        }
        else return true;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
            System.out.print("\nLogin api calling");
            return Update("http://220.227.124.134:8070/smartcity/gassensor/app/login",user,pwd);

        }

       // http://220.227.124.134:8070/smartcity/gassensor/getavgdata
       // https://pollution-monitor.com/smartcity/gassensor/login
       // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String status) {

            System.out.println("*************** login responce   " + status.toString());
            if(status.equalsIgnoreCase("Unauthorized")){
                Snackbar snackbar;
                 snackbar = Snackbar.make(getCurrentFocus(),"    "     +status+" user \n Please enter valid user name and password", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
                progressDialog.dismiss();
            }
            else if(status.equalsIgnoreCase(null)||status.isEmpty()){

                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(),"Please check internet connection", Snackbar.LENGTH_LONG);
                snackbar.show();
                progressDialog.dismiss();
            }
            else if(status.equalsIgnoreCase("Another user is already logged in. OTP has been sent to your registered email-id, please confirm it to log-in.")){

                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(),"Another user is already logged in.\nOTP has been sent to your registered email-id,\n please confirm it to log-in", Snackbar.LENGTH_LONG);
                snackbar.show();
                card.setVisibility(View.VISIBLE);
                otp.setVisibility(View.VISIBLE);
                relogin.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
            }
            else{
                setParameters(status);
                progressDialog.dismiss();

            }

        }
    }

    public void setParameters(String status){
        try {
           // System.out.print("*************** setParameter in session call from login ***************************");
            JSONObject jsonObject=new JSONObject(status);
            String token= (String) jsonObject.get("token");
            System.out.println(" set session for token   " + token);
            String latlogn=jsonObject.getString("latlong");
            url="http://220.227.124.134:8070/smartcity/gassensor";
            Token newToken=new Token();
            newToken.setToken(token);
            newToken.setUrl(url);
            System.out.print("token for token calss"+newToken.getToken()+newToken.getUrl());
          //  token.setToken()
            SharedVariables.setToken(getApplicationContext(), token);
            int level=(token.charAt(9));
            JSONArray js= (JSONArray) jsonObject.get("deviceTypes");
           // System.out.println("device type  " + js);
            Session_Management(level, user, token, latlogn, url, js.toString());
            //****device api***************************
            new HttpAsyncTaskDevice().execute();
          //  System.out.println("level   " + level);



        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getCurrentFocus(),"  Not Connected to server ", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        }

    }

    private class HttpAsyncTaskRelogin extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/
            return reLogin("http://220.227.124.134:8070/smartcity/gassensor/app/relogin",user,pwd);

        }
        // http://220.227.124.134:8070/smartcity/gassensor/getavgdata
        //https://pollution-monitor.com/smartcity/gassensor/login
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {

            System.out.println("******** relogin responce  " + status.toString());
            if(status.equalsIgnoreCase("Unauthorized")){
                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(),"    "     +status+" user \n Please enter valid user name and password", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
                progressDialog.dismiss();
            }
            else if(status.equalsIgnoreCase(null)||status.isEmpty()){

                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(),"Please check internet connection", Snackbar.LENGTH_LONG);
                snackbar.show();
                progressDialog.dismiss();

            } else if(status.equalsIgnoreCase("Invalid OTP.")){

                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(),"Invalid OTP.", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
                progressDialog.dismiss();
            }
          //  Invalid OTP.
            else{
                setParameters(status);
             //   progressDialog.dismiss();
                //Toast.makeText(getBaseContext(),"Please wait ",Toast.LENGTH_LONG).show();
            }

        }
    }


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    public void Session_Management(int userlevel,  String username, String token,String lat,String url,String devtype)
    {
        //Session Manager
        sessionManager = new SessionManager(this);
        sessionManager.createLoginSession(userlevel,username,token,lat,url,devtype);
    }

    public String Update(String url,String user,String pwd){
        Log.d("UPDATE METHOD CALLED", "HI I M IN login");
        InputStream inputStream = null;

        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("username",user);
            jsonObject2.accumulate("password",pwd );

            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
        //    System.out.print("************* login json "+jsonObject2+"*******************");
            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity120
            httpPost.setEntity(se);
         //   System.out.print("*************  till  http host *******************");
            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");



          //  System.out.print("*************  till  http header *******************");
            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

        //    System.out.print("*************  till  http execute *******************"+httpResponse);
            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

        //    System.out.print("\n************************** inputstream  *******************");
            //converted inputstream to string
            return convertInputStreamToString(inputStream);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "{}";
            // process execption
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
            // process execption
        } catch (Exception e)
        {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
            return "{}";

        }


    }


    public String reLogin(String url,String user,String pwd){
        Log.d("UPDATE METHOD CALLED", "HI I M IN relogin");
        InputStream inputStream = null;
        String res = "";
        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("username",user);
            jsonObject2.accumulate("password",pwd );
            jsonObject2.accumulate("otp",Otp );
            //{"username":"username","password":"pwd"}
            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("json "+jsonObject2);
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
            return convertInputStreamToString(inputStream);
        } catch (Exception e)
        {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
            return "{}";

        }


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



    //++++++++++++++++++++++++++ device condition api call+++++++++++++++++++++++++++++++++++++++

    //****************************
    private class HttpAsyncTaskDevice extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/

            String resul=  deviceCondition("http://220.227.124.134:8070/smartcity/gassensor/devicecondition");
          //  System.out.print(resul+"    inside background of login device condition ");
            return resul;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println(" Device condition login status :" + status);

            try {
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getCurrentFocus(), ""+status, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                } else {
                    //Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
                    addListItemToSession(status);
                    Intent i = new Intent(getApplicationContext(), navigationdrawer.class);
                    startActivity(i);
                    progressDialog.dismiss();

                }
                //  Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String deviceCondition(String url) {
        System.out.println("Inside loginn device condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;
        SharedPreferences sharedPreferences = this.getSharedPreferences("EMS", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        try {

            System.out.println("tokenn :"+token);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            jsonObject2.accumulate("sid", "All");
            jsonObject2.accumulate("did", "All");
            jsonObject2.accumulate("orgid", "All");
            jsonObject2.accumulate("devtype", "CEMS");


            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("json " + jsonObject2);

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
         //   System.out.println("F22222"+inputStream );
            result = convertInputStreamToString(inputStream);
        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();}
        catch (Exception e) {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
        }
        return result;

    }



    SessionManager sessionManager;
    String SessionDevice="",SessionDevid="";
    public void Session_ManagementforMap(String map)
    {
        //Session Manager
        SessionManager   sessionManager1 = new SessionManager(this);
        sessionManager1.addSessionForMap(map);
    }

    public void Session_Management(String device)
    {
        //Session Manager
        sessionManager = new SessionManager(this);
        sessionManager.addSessionData(device);
        sessionManager.addDeviceDetail("All,All,All");
    }
    private void addListItemToSession(String status) {

        try {
            SessionDevice="";
            SessionDevid="";
         //   System.out.println("inside device@@@@ condition" +status);
            JSONObject job = new JSONObject(status);
            JSONArray JOB1 = job.getJSONArray("noList");
            String orgList = job.getString("orglist");
            Session_ManagementforMap(orgList);
           // System.out.p

          //  System.out.println("working id"+JOB1+"\nmap sata"+orgList);
            for(int i=0;i<JOB1.length();i++) {
                JSONObject obj=JOB1.getJSONObject(i);
                // String tp=obj.getString("ts");
                String device =obj.getString("dname");
                String devid=obj.getString("devid");
                if(i==0){
                    SessionDevid=devid;
                    SessionDevice=device;
                }
                else {
                    SessionDevice = device + "," + SessionDevice;
                    SessionDevid=devid+","+SessionDevid;
                }
              //  System.out.print("sssss  "+SessionDevice);
            }
//no_infoList

            JSONArray JOB2 = job.getJSONArray("no_infoList");
         //   System.out.println("working id"+JOB2);
            for(int i=0;i<JOB2.length();i++) {
                JSONObject obj=JOB2.getJSONObject(i);


                String device =obj.getString("dname");//+":"+obj.getString("dname");
                String devid=obj.getString("devid");

                SessionDevice=device+","+SessionDevice;
                SessionDevid=devid+","+SessionDevid;
              //  System.out.print("no_info device   "+device);
            }

            JSONArray JOB3 = job.getJSONArray("faList");
        //    System.out.println("working id"+JOB3);
            for(int i=0;i<JOB3.length();i++) {
                JSONObject obj=JOB3.getJSONObject(i);

                String device =obj.getString("dname");
                String devid=obj.getString("devid");

                SessionDevice=device+","+SessionDevice;
                SessionDevid=devid+","+SessionDevid;

             //   System.out.print("sssss  "+SessionDevice);
            }

            JSONArray JOB4 = job.getJSONArray("cmList");
            System.out.println("working id"+JOB4);
            for(int i=0;i<JOB4.length();i++) {
                JSONObject obj=JOB4.getJSONObject(i);


                String device =obj.getString("devid");


                SessionDevice=device+","+SessionDevice;

              //  System.out.print("sssss  "+SessionDevice);
            }

            JSONArray JOB5 = job.getJSONArray("mmList");
          //  System.out.println("working id"+JOB5);
            for(int i=0;i<JOB5.length();i++) {
                JSONObject obj=JOB5.getJSONObject(i);


                String device =obj.getString("devid");


                SessionDevice=device+","+SessionDevice;

             //   System.out.print("sssss  "+SessionDevice);
            }
            Session_Management(SessionDevice);
            SessionManager   sessionManager1 = new SessionManager(this);
            sessionManager1.addSessionDevid(SessionDevid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
