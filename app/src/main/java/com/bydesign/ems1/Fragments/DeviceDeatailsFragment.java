package com.bydesign.ems1.Fragments;

/**
 * Created by Parikshit Sharma on 4/1/2016.
 */

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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.ems1.R;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceDeatailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //variables

    TextView SrNo ;
    TextView  State;
    TextView  DevName;
    TextView  ManuId;
    TextView  Desc;
    TextView Organisation;
    TextView  District;
    TextView  DevType;
    String Devices,Devids;

    TableRow tr;
    String url;
    String token;
    ProgressDialog pdialog;
    CardView cv2,cv3,cv4,cv5,cv6,cv7,cv8,cv9;
    public DeviceDeatailsFragment() {
        //Default constructor...
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_devicedetails, container, false);
        return v;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    SharedPreferences sharedPreferences;
    public void onStart() {
        super.onStart();
        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        Devices = sharedPreferences.getString("device", null);
        Devids= sharedPreferences.getString("devid",null);
        token = sharedPreferences.getString("token", null);
        url=sharedPreferences.getString("url", null);
        //-------------------------------------------------------------------

      //  tl = (TableLayout) getView().findViewById(R.id.devicedetail);

        // tl2 = (TableLayout) getView().findViewById(R.id.maintable123);
        // tl2.removeAllViews();

        System.out.print("inside on start of device condition");
      /*  if (isConnected()) {

                try {
                    new HttpAsyncTask().execute().get(3000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(getView(), "Time out Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

        } else {
            Snackbar snackbar = Snackbar.make(getView(), "Please Check Internet Connection", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
*/
//
        cv2=(CardView)getView().findViewById(R.id.card_view2);
        cv2.setVisibility(View.INVISIBLE);
        cv3=(CardView)getView().findViewById(R.id.card_view3);
        cv3.setVisibility(View.INVISIBLE);
        cv4=(CardView)getView().findViewById(R.id.card_view4);
        cv4.setVisibility(View.INVISIBLE);
        cv5=(CardView)getView().findViewById(R.id.card_view5);
        cv5.setVisibility(View.INVISIBLE);
        cv6=(CardView)getView().findViewById(R.id.card_view6);
        cv6.setVisibility(View.INVISIBLE);
        cv7=(CardView)getView().findViewById(R.id.card_view7);
        cv7.setVisibility(View.INVISIBLE);
        cv8=(CardView)getView().findViewById(R.id.card_view8);
        cv8.setVisibility(View.INVISIBLE);
        cv9=(CardView)getView().findViewById(R.id.card_view9);
        cv9.setVisibility(View.INVISIBLE);

        Spinner spinner = (Spinner) getView().findViewById(R.id.devid);
        SrNo =(TextView) getView().findViewById(R.id.srno);
        State =(TextView) getView().findViewById(R.id.state);
        DevName = (TextView) getView().findViewById(R.id.devname);
        ManuId=(TextView) getView().findViewById(R.id.mfgid);
        Desc = (TextView) getView().findViewById(R.id.desc);
        Organisation= (TextView) getView().findViewById(R.id.org);
        DevType= (TextView) getView().findViewById(R.id.devtype);
        District= (TextView) getView().findViewById(R.id.district);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories=getDevice(Devices);
        // categories.add("abc");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);}
    String item="";
    int devidSpinner;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        System.out.println("item selected in device details "+item);
        // tl2.removeAllViews();
        SrNo.setText("");
        Desc.setText("");
        DevName.setText("");
        ManuId.setText("");
        State.setText("");
        District.setText("");
        DevType.setText("");
        Organisation.setText("");
        devidSpinner=position;

        if (isConnected()) {
            //Toast.makeText(getActivity(), "you are connected", Toast.LENGTH_LONG).show();
            pdialog = ProgressDialog.show(getContext(), "", "Loding data...", true);
            new HttpAsyncTask().execute();
        } else {
            Snackbar snackbar = Snackbar.make(getView(), "Please Check Internet Connection", Snackbar.LENGTH_LONG);
            snackbar.show();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public List<String> getDevice(String device ){
        List<String> categories = new ArrayList<String>();
        try{

            StringTokenizer st = new StringTokenizer(device, ",");
            int t=0;
            while (st.hasMoreTokens()) {
                categories.add(st.nextToken());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"Please select device from device condition",Toast.LENGTH_LONG).show();
        }
        return  categories;
    }
    //****************************
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/

            String resul=  DeviceDetail(url+"/getdeviceinfo");
            System.out.print(resul+"    inside background ");
            return resul;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("device condtion :" + status);

            try {
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    pdialog.dismiss();
                    Toast.makeText(getActivity(), "Error : Few parameters are missing", Toast.LENGTH_LONG).show();
                }
                else if(status.equalsIgnoreCase("[]")){
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136)); //Displaying the snackbar using the show method()
                    snackbar.show();
                    pdialog.dismiss();
                }else if(status.equalsIgnoreCase("invalid")){
                    pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                   // new HttpForLogout().execute();
                    sessionManager.logoutUser();
                }else {

                    addHeaders(status);
                    addDataTable(status);
                    pdialog.dismiss();
                }
                //  Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String DeviceDetail(String url){
        System.out.println("Inside Device Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        // Devices = sharedPreferences.getString("device",null);
        // Log.d("aqw",Devices);
        try {

            System.out.println("token :"+token+" "+Devices);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            List<String> devidList;
            devidList=getDevice(Devids);
            System.out.print("devid list");
            /*for(int i=0;i<devidList.size();i++){
                System.out.println(devidList.get(i));
            }*/

            System.out.print(" api caling "+ devidSpinner);
            jsonObject2.accumulate("devid", devidList.get(devidSpinner));

            //jsonObject2.accumulate("devid", item);



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
            // httpPost.setHeader("Content-Length", se.getContentLength()+"");


            // httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString("rat#1:rat".getBytes(), Base64.NO_WRAP));
            // httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString("rat#1:rat".getBytes(), Base64.DEFAULT));

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            System.out.println("F22222"+inputStream );
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

    public void addHeaders(String data){
        JSONArray job;
        JSONObject unit;
        try {

            job = new JSONArray(data);
            unit = job.getJSONObject(0);
            if (unit.getJSONObject("values").has("sr_num")) {
                SrNo.setText(unit.getJSONObject("values").getString("sr_num"));

            }
            if (unit.getJSONObject("values").has("state")) {
                State.setText(unit.getJSONObject("values").getString("fullSt"));

            }
            if (unit.getJSONObject("values").has("did")) {
                DevName.setText(unit.getJSONObject("values").getString("did"));

            }
            if (unit.getJSONObject("values").has("desc")) {
                Desc.setText(unit.getJSONObject("values").getString("desc"));

            }
            if (unit.getJSONObject("values").has("dist")) {
                District.setText(unit.getJSONObject("values").getString("fullDist"));

            }
            if (unit.getJSONObject("values").has("devtype")) {
                DevType.setText(unit.getJSONObject("values").getString("devtype"));

            }
            if (unit.getJSONObject("values").has("mf_id")) {
                ManuId.setText(unit.getJSONObject("values").getString("mf_id"));

            }
            if (unit.getJSONObject("values").has("orgid")) {
                Organisation.setText(unit.getJSONObject("values").getString("orgid"));

            }

            cv2.setVisibility(View.VISIBLE);
            cv3.setVisibility(View.VISIBLE);
            cv4.setVisibility(View.VISIBLE);
            cv5.setVisibility(View.VISIBLE);
            cv6.setVisibility(View.VISIBLE);
            cv7.setVisibility(View.VISIBLE);
            cv8.setVisibility(View.VISIBLE);
            cv9.setVisibility(View.VISIBLE);


            cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv3.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv4.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv5.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv6.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv7.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv8.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv9.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

        } catch (JSONException e) {
            e.printStackTrace();

            //Toast.makeText(getActivity(),"Json Exception",Toast.LENGTH_LONG).show();
        }

    }
    public void addDataTable(String data){

    }
}
