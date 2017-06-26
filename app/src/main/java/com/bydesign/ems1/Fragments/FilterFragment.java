package com.bydesign.ems1.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bydesign.ems1.R;
import com.bydesign.ems1.navigationdrawer;
import com.bydesign.ems1.services.SessionManager;

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
import java.util.List;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    CardView cv,cv1,cv2,cv3;
    Spinner devT,state,dist,org;
    SharedPreferences sharedPreferences;
    String Devices,token,url;
    String  LatLongState;
    Button filter;
    ProgressDialog pdialog=null;
    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity(). getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public String getDeviceType(String device ){
        List<String> categories = new ArrayList<String>();
        String h = "";
        try {

            System.out.print("inside getdevice"+ device);
            StringTokenizer st = new StringTokenizer(device, "[]");
            int t = 0;
            while (st.hasMoreTokens()) {
                 h=h+ ","+st.nextToken();
                //categories.add(h);
                System.out.print("type token value  h bhi h line me :) "+h);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"Device not found", Snackbar.LENGTH_LONG);
            //Displaying the snackbar using the show method()
            snackbar.show();
            //  Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return  h;
    }

    public String getStateId(String device ){
        List<String> categories = new ArrayList<String>();
        String h = "";
        try {

            System.out.print("inside getdevice"+ device);
            StringTokenizer st = new StringTokenizer(device, "+");

            while (st.hasMoreTokens()) {
                h= st.nextToken();
                System.out.print("state id :) "+h);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"State id not found", Snackbar.LENGTH_LONG);
            //Displaying the snackbar using the show method()
            snackbar.show();
            //  Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return  h;
    }


    public List<String> getDevice(String device ){
        List<String> categories = new ArrayList<String>();
        try {

            String devtypt=getDeviceType(device);
            System.out.print("inside getdevice"+ devtypt);
            StringTokenizer st = new StringTokenizer(devtypt, "\",");
            int t = 0;
            while (st.hasMoreTokens()) {
                String h=st.nextToken();
                categories.add(h);
                System.out.print("  token value " + categories+" h bhi h line me :) "+h);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"Device not found", Snackbar.LENGTH_LONG);
            //Displaying the snackbar using the show method()
            snackbar.show();
            //  Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return  categories;
    }

    public void onStart(){
        super.onStart();
        cv=(CardView)getView().findViewById(R.id.card_view);
        cv1=(CardView)getView().findViewById(R.id.card_view1);
        cv2=(CardView)getView().findViewById(R.id.card_view3);
        cv3=(CardView)getView().findViewById(R.id.card_view4);

        filter=(Button)getView().findViewById(R.id.filter);
        cv1.setVisibility(View.INVISIBLE);
        cv2.setVisibility(View.INVISIBLE);
        cv3.setVisibility(View.INVISIBLE);

        devT=(Spinner)getView().findViewById(R.id.DevT_Spinner);
        state=(Spinner)getView().findViewById(R.id.State_spinner);
        dist=(Spinner)getView().findViewById(R.id.Dis_spinner);
        org=(Spinner)getView().findViewById(R.id.Ord_spinner);


        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        Devices = sharedPreferences.getString("devtype", null);
        token= sharedPreferences.getString("token", null);
        url=sharedPreferences.getString("url", null);
        LatLongState = sharedPreferences.getString("state", null);
        System.out.println("in filters \n token" + token + " device " + Devices + " url" + url + "\n");

        // *************************devtype spinner******************
        devT.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories=getDevice(Devices);
        System.out.println(":categori for device type" + categories + " posoioti 0   " + categories.get(0));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devT.setAdapter(dataAdapter);
        System.out.println(": out filter");

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
                new HttpAsyncTaskDeviceConditin().execute();
            }else{
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Kindly check internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
    List<String>sAsid=new ArrayList<String>();
    List<String>dAdid=new ArrayList<String>();
    List<String>oAoid=new ArrayList<String>();
    String selectStates,selectDevType,selectDist,selectOrg,selestSid,selectDid,selectOid,StatusofDistrict;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;



        if (isConnected()) {
            if(spinner.getId() == R.id.DevT_Spinner)
            {
                selectDevType = parent.getItemAtPosition(position).toString();
                sAsid=  getStates();
                System.out.println("  sekectdev type  " + selectDevType + "   pos : "+position+" sid array "+sAsid);
             if(position==1||position==2) {
                 Snackbar snackbar;
                 //Initializing snackbar using Snacbar.make() method
                 snackbar = Snackbar.make(getView(), "you can select only CEMS device type", Snackbar.LENGTH_LONG);
                 View snackBarView = snackbar.getView();
                 snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                 //Displaying the snackbar using the show method()
                 snackbar.show();
             }
                cv1.setVisibility(View.VISIBLE);
            }
            if(spinner.getId() == R.id.State_spinner)
            {
                selectStates = parent.getItemAtPosition(position).toString();
                System.out.println("  sekectstate " + selectStates + "   pos : " + position + "sid array " + sAsid);
                if(position==0){
                    selestSid="All";
                    System.out.println("select all device state");
                    cv2.setVisibility(View.INVISIBLE);
                    cv3.setVisibility(View.INVISIBLE);
                  //  new HttpAsyncTaskDeviceConditin().execute();
                }
                else{
                    String sisd= sAsid.get(position-1);
                    System.out.println( "  sekectstate "+selectStates+"   pos : "+position+ sisd);
                    selestSid= getStateId(sisd);
                    new HttpAsyncTaskSid().execute();
                    cv2.setVisibility(View.VISIBLE);

                }

                System.out.println("  sekectstate " + selectStates + "   pos : " + position + " sid posostion "/*+sAsid.get(position)+" sid : "+selestSid*/);

            }
            if(spinner.getId() == R.id.Dis_spinner)
            {

                selectDist = parent.getItemAtPosition(position).toString();
                System.out.println("  sekectdist " + selectDist + "   pos : " + position);
                if(position==0){
                    selectDid="All";
                    cv3.setVisibility(View.INVISIBLE);
                   // new HttpAsyncTaskDeviceConditin().execute();
                }
                else{
                    String sisd= dAdid.get(position-1);
                    System.out.println( "  sekectditrict "+selectDist+"   pos : "+position+ sisd);
                    selectDid= getStateId(sisd);
                    oAoid= getOrg();
                    cv3.setVisibility(View.VISIBLE);
                   // new HttpAsyncTaskSid().execute();
                }

            }
            if(spinner.getId() == R.id.Ord_spinner)
            {
                selectOrg = parent.getItemAtPosition(position).toString();
                System.out.println( "  sekect org  "+selectOrg+"   pos : "+position);
                if(position==0){
                    selectOid="All";

                }
                else{
                    String sisd= oAoid.get(position-1);
                    System.out.println( "  sekectditrict "+selectOrg+"   pos : "+position+ sisd);
                    selectOid= getStateId(sisd);
                   // getOrg();
                    // new HttpAsyncTaskSid().execute();
                }
              //  new HttpAsyncTaskDeviceConditin().execute();
            }

        }else{
            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), "Please check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public List<String> getStates()  {
        List<String> stateAndSid=new ArrayList<String>();
        try {

            List<String> categories = new ArrayList<String>();
            categories.add("All States");
            JSONArray jsonArray = new JSONArray(LatLongState);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                String location = object.get("state").toString();
                String sid=object.get("sid").toString();
                stateAndSid.add(location+"+"+sid);
                categories.add(location);
            }
            state.setOnItemSelectedListener(this);
            System.out.println(":categori for device state" + categories + " posoioti 0   " + categories.get(0));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            state.setAdapter(dataAdapter);
            System.out.println(": out state");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return stateAndSid;
    }

    public List<String> getDistrict(String status){
        List<String> distAndDid=new ArrayList<String>();
        try{
            JSONArray jsonArray=new JSONArray(status);
            List<String> categories = new ArrayList<String>();
            categories.add("All District");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=  jsonArray.getJSONObject(i);
                String Dist=jsonObject.get("name").toString();
                String Did=jsonObject.get("did").toString();
                distAndDid.add(Dist+"+"+Did);
                categories.add(Dist);
            }
            dist.setOnItemSelectedListener(this);
            System.out.println(":categori for device district" + categories + " posoioti 0   " + categories.get(0));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dist.setAdapter(dataAdapter);
            System.out.println(": out district");
        }catch (JSONException e){
            e.printStackTrace();
        }

        return distAndDid;
    }

    public List<String> getOrg(){
        List<String> orgAndoid=new ArrayList<String>();
        List<String> categories = new ArrayList<String>();
        categories.add("All Organization");
        try {
            JSONArray jsonArray = new JSONArray(StatusofDistrict);
                       for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                           if(jsonObject.get("did").toString().equalsIgnoreCase( selectDid)){
                               JSONArray orgList=jsonObject.getJSONArray("orglist");
                               for(int j=0;j<orgList.length();j++){
                                   JSONObject json=orgList.getJSONObject(j);
                                   String org = json.get("name").toString();
                                   String orgid = json.get("orgid").toString();
                                   orgAndoid.add(org + "+" + orgid);

                                   categories.add(org);
                                   System.out.println(": list for org " + categories);
                               }
                               break;
                           }

                        }

            org.setOnItemSelectedListener(this);
            System.out.println(":categori for device organi" + categories + " posoioti 0   " + categories.get(0));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            org.setAdapter(dataAdapter);
            System.out.println(": out organization");
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return orgAndoid;
    }
    //++++++++++++++++++++++++++ device condition api call+++++++++++++++++++++++++++++++++++++++

    //****************************
    private class HttpAsyncTaskSid extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/

            String resul=  FilterAsyn(url + "/district");
            System.out.print(resul+"    inside background ");
            return resul;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("filter  status :" + status);

            try {
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), ""+status, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
                else if(status.equalsIgnoreCase("{}")){
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136)); //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                }else if(status.equalsIgnoreCase("Invalid")){
                   pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                  //  new HttpForLogout().execute();
                    sessionManager.logoutUser();
                }else {
                    dAdid=getDistrict(status);
                    StatusofDistrict=status;
                    pdialog.dismiss();
                   /* Snackbar snackbar;

                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), "Filter Applied Successfully", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();*/
                }
                //  Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String FilterAsyn(String url) {
        System.out.println("Inside Device Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        try {

            System.out.println("tokenn :"+token);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            System.out.print("url  "+url);

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            jsonObject2.accumulate("sid", selestSid);
          /*  jsonObject2.accumulate("did", "All");
            jsonObject2.accumulate("orgid", "All");
            jsonObject2.accumulate("devtype", "CEMS");*/


            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("\njson " + jsonObject2);

            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // httpPost.setHeader("Content-Length", se.getContentLength()+"");

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
    private class HttpAsyncTaskDeviceConditin extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/

            String resul=  FilterAsyndevice(url + "/devicecondition");
            System.out.print(resul+"    inside background ");
            return resul;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("filter  status :" + status);
         //   Toast.makeText(getActivity(),"return value is : "+status, Toast.LENGTH_LONG).show();
            try {
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    pdialog.dismiss();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), ""+status, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
                else {

                    addListItemToSession(status);
                    MapFragment main=new MapFragment();
                    android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, main);
                    ft.commit();
                    navigationdrawer nv=new navigationdrawer();
                    nv.toolbar.setTitle("Analyzers Across India");
                    pdialog.dismiss();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), "Fliter applied successfully", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
                //  Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    SessionManager sessionManager1;


    public void Session_Management1(String device)
    {
        //Session Manager
        sessionManager1 = new SessionManager(getActivity());
        sessionManager1.addDeviceDetail(device);
    }
    public String FilterAsyndevice(String url) {
        System.out.println("Inside Device Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        try {

            System.out.println("tokenn :"+token);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            System.out.print("url  "+url);

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            jsonObject2.accumulate("sid", selestSid);

         if( selestSid.equalsIgnoreCase("All"))
         {
             System.out.print("\n for filter "+selestSid);
             jsonObject2.accumulate("did", "All");
             jsonObject2.accumulate("orgid", "All");
             Session_Management1(selestSid + "," + "All" + "," + "All");
         }
         else  if(selectDid.equalsIgnoreCase("All"))
             {
                 jsonObject2.accumulate("did", "All");
                 jsonObject2.accumulate("orgid", "All");
                 Session_Management1(selestSid + "," + "All" + "," + "All");
             }
         else{
                 jsonObject2.accumulate("did", selectDid);
                 if(selectOid.equalsIgnoreCase("All")){
                     jsonObject2.accumulate("orgid", "All");
                     Session_Management1(selestSid+","+selectOid+","+selectDid);
                 }
                 else {
                     jsonObject2.accumulate("orgid", selectOid);
                     Session_Management1(selestSid+","+selectOid+","+selectDid);
                 }
             }


            jsonObject2.accumulate("devtype", "CEMS");


          //  Session_Management1(selestSid+","+selectOid+","+selectDid);
            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("\njson object for filter " + jsonObject2);

            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // httpPost.setHeader("Content-Length", se.getContentLength()+"");

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
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    SessionManager sessionManager;
    String SessionDevice="",SessionDevid="";
    public void Session_ManagementforMap(String map)
    {
        //Session Manager
        SessionManager   sessionManager1 = new SessionManager(getActivity());
        sessionManager1.addSessionForMap(map);
    }
    public void Session_Management(String device)
    {
        //Session Manager
        sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionData(device);
    }
    private void addListItemToSession(String status) {

        try {
            SessionDevice="";
            SessionDevid="";
            System.out.println("inside device@@@@ condition" +status);
            JSONObject job = new JSONObject(status);
            JSONArray JOB1 = job.getJSONArray("noList");
            String orgList = job.getString("orglist");
            Session_ManagementforMap(orgList);
            // System.out.p

            System.out.println("working id" + JOB1 + "\nmap sata" + orgList);
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
                System.out.print("sssss  "+SessionDevice);
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
                System.out.print("no_info device   "+device);
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
            SessionManager   sessionManager1 = new SessionManager(getActivity());
            sessionManager1.addSessionDevid(SessionDevid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
