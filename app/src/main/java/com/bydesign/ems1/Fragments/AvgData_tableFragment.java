package com.bydesign.ems1.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvgData_tableFragment extends Fragment implements AdapterView.OnItemSelectedListener{



    Spinner spinner2;
  int selectGranu;
    long table_date;
    String tablegraph="table";
    TableLayout tl, tg,th;
    TableRow tr;

    ImageButton to,from;

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    int flag=0,flagForcheck=0;
    TextView Date;
    TextView Time;
    TextView Parameter,A_PPM,t1,t2,t3;
    private EditText fromText,totext;
    private RelativeLayout mainLayout;
    LinearLayout l1;
    Spinner spinner;
    String Selecteddevice;

    String fromd,tod,PDFString,file="";

    public AvgData_tableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_avg_data_table, container, false);
        return  view;
    } @Override
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
    String Devices,Devids;

    public List<String> getDevice(String device ){
        List<String> categories = new ArrayList<String>();
        try {

            StringTokenizer st = new StringTokenizer(device, ",");
            int t = 0;
            while (st.hasMoreTokens()) {
                categories.add(st.nextToken());
            }
        }catch (NullPointerException e){
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Device not found", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
            e.printStackTrace();
           // Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return  categories;
    }
    FloatingActionButton save,share;CardView cv2;

    public void onStart() {
        super.onStart();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());


        try {

            cv2 = (CardView) getView().findViewById(R.id.card_viewp);
            cv2.setVisibility(View.INVISIBLE);
            save = (FloatingActionButton) getView().findViewById(R.id.savePdf);
            share = (FloatingActionButton) getView().findViewById(R.id.share);
            tl = (TableLayout) getView().findViewById(R.id.maintable);
            th = (TableLayout) getView().findViewById(R.id.maintableH);


            sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
            Devices = sharedPreferences.getString("device", null);
            Devids= sharedPreferences.getString("devid",null);

            spinner = (Spinner) getView().findViewById(R.id.spinner);
            spinner2 = (Spinner) getView().findViewById(R.id.s1);

            spinner.setOnItemSelectedListener(this);
            List<String> categories = new ArrayList<String>();
            categories = getDevice(Devices);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner2.setOnItemSelectedListener(this);

            List<String> granu = new ArrayList<String>();
            granu.add("Hourly based");
            granu.add("8 hourly based");
            granu.add("Daily based");

            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, granu);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(dataAdapter1);


            System.out.print("inside on start of avg table");
            to = (ImageButton) getView().findViewById(R.id.imageButton2);

            to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 1;
                    /*if (flagForcheck == 0)

                    {
                        Snackbar snackbar;
                        //Initializing snackbar using Snacbar.make() method
                        snackbar = Snackbar.make(getView(), "Please enter start date", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                        //Displaying the snackbar using the show method()
                        snackbar.show();

                    } else {*/
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                   // }
                }
            });

            from = (ImageButton) getView().findViewById(R.id.imageButton1);
            from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 0;
                    DialogFragment newFragment = new SelectDateFragment();
                    newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");

                }
            });

            // ################### table graph data #################################

           //
          //  System.out.println("**************  TEmparara store data for testing ****************  " + sharedPreferences.getString("avgDevice", null));
            tablegraph=sharedPreferences.getString("tablegraphavg", null);
           /* System.out.print(" \n sts " + sharedPreferences.getString("stime", null) + " \n ets " + sharedPreferences.getString("stime", null));//sharedPreferences.getString("sts", null)
            System.out.println("@Table and graphe data avg  " + tablegraph + " \n   *  " + sharedPreferences.getString("devices", null));
            System.out.println("Selected Device First"+sharedPreferences.getString("devices", "No String !................**********************************"));
            System.out.println("Selected Device Second"+sharedPreferences.getString("devices", "No String !................**********************************"));*/
            if(tablegraph==null && (sharedPreferences.getString("avgDevice",null)==null)){

                System.out.println("@   no data 5678888*&&^^ & * * ");

            }
            else{
              //  System.out.println("hu lalalalalalalalalal back from graph  ");
                String date = sharedPreferences.getString("sts",null);
                //new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date (table_date));
               // fromText.setText("" + sharedPreferences.getString("sts",null));
                String eDate = sharedPreferences.getString("ets",null);
                //new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date (end_date));
               //totext.setText(""+sharedPreferences.getString("ets",null));
               /* System.out.print("\n******************* avg data table *************************");
                System.out.print(" start date from session" + date + "  \n end date fron session " + eDate);
                System.out.print("\n********************************************");
*/

                ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition = myAdap.getPosition(sharedPreferences.getString("avgDevice",null));
                spinner.setSelection(spinnerPosition);
           //     System.out.print("device posision " + spinnerPosition + " \n spinner " + spinner.getSelectedItem() + "     " + sharedPreferences.getString("devices", null));

               /* ArrayAdapter myAdap1 = (ArrayAdapter) spinner2.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition1 = myAdap1.getPosition(sharedPreferences.getString("gran",null));*/
                spinner2.setSelection(sharedPreferences.getInt("avgGranu", 0) - 1);
              //  System.out.print("granalutrit " + sharedPreferences.getInt("gran", 0) + " \n spinner " + spinner2.getSelectedItem());
                fromText = (EditText) getView().findViewById(R.id.editText1);
                fromText.setText(sharedPreferences.getString("stime", null));
                totext = (EditText) getView().findViewById(R.id.To);
                totext.setText(sharedPreferences.getString("etime", null));
                addHeaders(tablegraph);
                addDataTable(tablegraph);
              /*  ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition = myAdap.getPosition(sharedPreferences.getString("devices",null));
                spinner.setSelection(spinnerPosition);*/
                cv2.setVisibility(View.VISIBLE);
            }


           // System.out.println("K(KIIIIIinsise device");

            Button summay = (Button) getView().findViewById(R.id.show);
            summay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                fromText = (EditText) getView().findViewById(R.id.editText1);
                totext = (EditText) getView().findViewById(R.id.To);
                    try {
                        fromd = fromText.getText().toString();
                        tod = totext.getText().toString();
                        if (check(fromd, tod)) {
                        java.util.Date dateFrom, dateTo;
                        SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd");
                        dateFrom = formatter.parse(fromd);
                        dateTo=formatter.parse(tod);
                            if(dateFrom.before(dateTo)||dateFrom.equals(dateTo)){
                               // System.out.print("\ninside check of dates");
                                if (isConnected()) {
                                    //Toast.makeText(getActivity(), "you are connected", Toast.LENGTH_LONG).show();
                                    pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
                                    new HttpAsyncTask().execute();
                                } else {
                                    //Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_LONG).show();
                                    Snackbar snackbar;
                                    //Initializing snackbar using Snacbar.make() method
                                    snackbar = Snackbar.make(getView(), "Please check internet connection", Snackbar.LENGTH_LONG);
                               /* View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));*/
                                    //Displaying the snackbar using the show method()
                                    snackbar.show();

                                }
                            }
                            else {
                                Snackbar snackbar;
                                snackbar = Snackbar.make(getView(), "Kindly choose Fromdate before Todate ", Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                                snackbar.show();
                            }
                        } else {
                            Snackbar snackbar;
                            //Initializing snackbar using Snacbar.make() method
                            snackbar = Snackbar.make(getView(), "Please enter date", Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                            //Displaying the snackbar using the show method()
                            snackbar.show();
                            // Toast.makeText(getActivity(), "Please  enter  date", Toast.LENGTH_LONG).show();
                        }
                    } catch (NullPointerException e) {
                        Snackbar snackbar;
                        //Initializing snackbar using Snacbar.make() method
                        snackbar = Snackbar.make(getView(), "Please enter both dates", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                        //Displaying the snackbar using the show method()
                        snackbar.show();
                        //Toast.makeText(getActivity(), "Please  enter both date", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });



            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    createPDF();

                }
            });
            share = (FloatingActionButton) getView().findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    shareFile();
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    ProgressDialog pdialog=null;
    public boolean check(String user,String pwd){
        if(user.equalsIgnoreCase("")||pwd.equalsIgnoreCase("")){
            return false;
        }
        else return true;
    }
    String item="avg";
    int devidSpinner;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinner = (Spinner) parent;


        if (isConnected()) {
            if(spinner.getId() == R.id.spinner)
            {
                devidSpinner=position;
                Selecteddevice=parent.getItemAtPosition(position).toString();
               // System.out.println(item + "  sekectedgfdfgd  "+Selecteddevice+"   pos : "+position);
            }
            if(spinner.getId() == R.id.s1)
            {
                selectGranu=position+1;
                item = parent.getItemAtPosition(position).toString();
               // System.out.println(item + "  sekectedgfdfgd  "+selectGranu+"   pos : "+position);
            }

        }
        else{
           // Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
           Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"PLease check internet connection", Snackbar.LENGTH_LONG);

            //Displaying the snackbar using the show method()
            snackbar.show();

        }
    }public void Session_Management(String device)
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionForCurrent(device);
      //  System.out.println("\nadd device in session");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //****************************
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {

            return avgData();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("avg data  :" + status);
            try{
                if(status.equalsIgnoreCase("token / devid / stime or etime is missing.")){
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(), ""+status, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();//   Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
                    pdialog.dismiss();
                }
                else if(status.equalsIgnoreCase(null)){
                    MapFragment main=new MapFragment();
                    android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, main);
                    ft.commit();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Please check internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    pdialog.dismiss();

                }else if(status.equalsIgnoreCase("Invalid")){
                    pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.logoutUser();
                }
                else {

                    tl = (TableLayout) getView().findViewById(R.id.maintable);
                    th = (TableLayout) getView().findViewById(R.id.maintableH);
                    tl.removeAllViews();
                    th.removeAllViews();
                    addHeaders(status);
                    addDataTable(status);
                    Session_Management(Selecteddevice);
                    SessionManager   sessionManagerp = new SessionManager(getActivity());
                    sessionManagerp.setTableContaintavg(status);
                    sessionManagerp.addFlage(0);
                    SessionManager   sessionManagere = new SessionManager(getActivity());
                    sessionManagere.setTemparari(Selecteddevice);
                    sessionManagere.setstartTime(fromText.getText().toString());
                    sessionManagere.setendTime(totext.getText().toString());
                    sessionManagere.setDeviceAvg(Selecteddevice);
                    sessionManagere.setgran(selectGranu);


                   sessionManagere.addDatesInSession(fromText.getText().toString(), totext.getText().toString());
                 //   System.out.print("\n tabke trfg " +Selecteddevice+fromText.getText().toString()+ sharedPreferences.getString("tablegraphavg", null) + " \n temp " + sharedPreferences.getString("temp", null) + " \n sts " + sharedPreferences.getString("sts", null) + " \n ******selected  device " + sharedPreferences.getString("devices", null));
                    pdialog.dismiss();
                    save.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    cv2.setVisibility(View.VISIBLE);
                    cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String token;
    SharedPreferences sharedPreferences;
    public String avgData() {
        Log.d("UPDATE METHOD CALLED", "HI I M IN UPDATE");
        InputStream inputStream = null;
        String res = "";
        String result = null;
        try {

            token = sharedPreferences.getString("token", null);
            String url=sharedPreferences.getString("url", null);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            //"http://220.227.124.134:8070/smartcity/gassensor/getavgdata"
            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url+"/getavgdata");

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);

            List<String> devidList;
            devidList=getDevice(Devids);
          //  System.out.print("devid list");
            /*for(int i=0;i<devidList.size();i++){
                System.out.println(devidList.get(i));
            }*/

          //  System.out.print(" api caling "+ devidSpinner);
            jsonObject2.accumulate("devid", devidList.get(devidSpinner));
          //  jsonObject2.accumulate("devid", Selecteddevice);
            jsonObject2.accumulate("gran", ""+selectGranu);
           // jsonObject2.accumulate("count", "10");
            String fromd=fromText.getText().toString()+" 00:00:01";
            String tod=totext.getText().toString()+" 23:59:59";
           // System.out.println("date is "+fromd+"    date 2"+tod+"\n granu is "+selectGranu);
            long epoch1 = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(fromd).getTime() ;
            long epoch2 = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(tod).getTime() ;
          //  System.out.println("tim is " + epoch1 + "    tim 2" + epoch2);
         //   Session_Management(Selecteddevice, fromText.getText().toString(), totext.getText().toString(),selectGranu,item);
            jsonObject2.accumulate("stime", epoch1);
            jsonObject2.accumulate("etime", epoch2);
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
            //  httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString("rat#1:rat".getBytes(), Base64.DEFAULT));

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            result = convertInputStreamToString(inputStream);
        } catch (Exception e) {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
        }

        return result;

    }
    public void onPause(){

        super.onPause();
    }
    public void onResume(){
        super.onResume();
        setRetainInstance(true);
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

    //------------------------------------------session managment---------------------------------------------
    public void Session_Management(String device,String sts,String ets,int gran,String para)
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionData1(device,sts,ets,gran,para);
    }



    public ArrayList<String>stringToken(String string){
        ArrayList<String>paramt =new ArrayList<>();//String[param.length()];
        StringTokenizer st = new StringTokenizer(string, ",[,]\"");
        int t=0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            // System.out.println("\n" + token);
            paramt.add(token);

               /* S_Status = token;
                break;*/
        }
        return paramt;
    }
    //***************************** date picket*****************************



    public void TOSetDate(int year, int month, int day) {
        toDateEtxt = (EditText)getView(). findViewById(R.id.To);
        String d = null,m=null;
        if(day<=9)
            d="0"+day;
        else d=""+day;
        // fromDateEtxt.setText(year + "-" + month + "-" +"0"+ day);
        if(month<=9)
            m="0"+month;
        else m=""+month;
        toDateEtxt.setText(year + "/" + m + "/" + d);
        // toDateEtxt.setText(year + "-" + month + "-" + day);
        // Toast.makeText(getActivity(), "you selected to date" + toDateEtxt.getText(), Toast.LENGTH_LONG).show();

    }


    public void populateSetDate(int year, int month, int day) {
        fromDateEtxt = (EditText)getView().findViewById(R.id.editText1);
        String d,m;
        if(day<9) d="0"+day;
        else d=""+day;
        // fromDateEtxt.setText(year + "-" + month + "-" +"0"+ day);
        if(month<9)
            m="0"+month;
        else
            m=""+month;

        fromDateEtxt.setText(year + "/"+ m + "/" + d);


        // Toast.makeText(getActivity(),"you selected from date"+fromDateEtxt.getText(),Toast.LENGTH_LONG).show();
        flagForcheck=1;
    }
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
          /*  return new DatePickerDialog(getActivity(), this, yy, mm, dd);*/
            DatePickerDialog dialog=new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            if(flag==0) populateSetDate(yy, mm+1, dd);
            else TOSetDate(yy, mm+1, dd);

        }
    }

    //*****************************
    public void addHeaders(String status){


        try {
            /*for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                fd_id=json_data.getInt("FOOD_ID");
                fd_name=json_data.getString("FOOD_NAME");
            }*/

            JSONObject jsonObject=new JSONObject(status);
          //  JSONObject object=jsonObject.getJSONObject(0);
            // String id= (String) object.get("unit");
           JSONArray value = (JSONArray) jsonObject.get("data");
            // JSONArray param = (JSONArray) object.get("param");
            JSONArray param = (JSONArray) jsonObject.get("param");
            JSONArray unit= ( JSONArray) jsonObject.get("units");
            file=jsonObject.get("file").toString();

            ArrayList<String>paramt=stringToken(param.toString());
            ArrayList<String>units=stringToken(unit.toString());


            tr = new TableRow(getActivity());
            // tr.setBackgroundColor(Color.argb(196,18,86,136));
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            TextView Date = new TextView(getActivity());
            Date.setText("Start Date");
            Date.setTextColor(Color.WHITE);
            Date.setTextSize(12);
            Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Date.setPadding(5, 5, 5, 0);
            Date.setBackgroundResource(R.drawable.tableh);//cell.png);
            tr.addView(Date);  // Adding textView to tablerow.


             Date = new TextView(getActivity());
            Date.setText("Start Time");
            Date.setTextColor(Color.WHITE);
            Date.setTextSize(12);
            Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Date.setPadding(5, 5, 5, 0);
            Date.setBackgroundResource(R.drawable.tableh);//cell.png);
            tr.addView(Date);  // Adding textView to tablerow.


             Time = new TextView(getActivity());
            Time.setText("End Date");
            Time.setBackgroundResource(R.drawable.tableh);
            Time.setTextSize(12);
            Time.setTextColor(Color.WHITE);
            Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Time.setPadding(5, 5, 5, 0);
            tr.addView(Time);  // Adding textView to tablerow.


            Time = new TextView(getActivity());
            Time.setText("End Time");
            Time.setBackgroundResource(R.drawable.tableh);
            Time.setTextSize(12);
            Time.setTextColor(Color.WHITE);
            Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Time.setPadding(5, 5, 5, 0);
            tr.addView(Time);  // Adding textView to tablerow.
            PDFString=PDFString+"   EndTime ";

            for(int row=0;row<paramt.size();row++) {
                TextView  Co = new TextView(getActivity());
            //    System.out.print(" param :" + paramt.get(row) + "\n" + units.get(row));
                Co.setText("   " + param.get(row) + "\n" + "   " + unit.get(row));
                Co.setTextColor(Color.WHITE);
                Co.setTextSize(12);
                Co.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Co.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Co.setPadding(5, 5, 5, 0);
                Co.setBackgroundResource(R.drawable.tableh);
                tr.addView(Co);
                PDFString=PDFString+"   "+paramt.get(row)+"("+units.get(row)+")";
                // Co. removeView();// Adding textView to tablerow.
            }

            PDFString=PDFString+"\n\n\n\n\n";
            // Add the TableRow to the TableLayout
            th.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            // we are adding two textviews for the divider because we have two columns
            // QCODE.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addDataTable(String status)
    {
        System.out.print("Add data in table");
        try{
            JSONObject jsonObject=new JSONObject(status);

            JSONArray value = (JSONArray) jsonObject.get("data");


            JSONArray param = (JSONArray) jsonObject.get("param");
        //    JSONArray alarm = (JSONArray) jsonObject.get("alarm");
            ArrayList<String>paramt=stringToken(param.toString());

            if(value.length()==0){
                tl.removeAllViews();

                th.removeAllViews();

                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
                //Displaying the snackbar using the show method()
                snackbar.show();
            }
            for(int row=0;row<value.length();row++) {
                System.out.println(" data array is not null  "+value.length());
                /** Create a TableRow dynamically **/
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                // System.out.print(" \nts:" + tp + "   " + i);
                //********************* time and date*******************

                JSONObject obj=value.getJSONObject(row);
                Long stp= (Long) obj.get("sts");
                Long etp= (Long) obj.get("ets");
                //Long ds=Long.parseLong(tp);
                //System.out.print("string is "+tp+"    long is "+ds);
                String [] dt=new String [2];
                String [] dte=new String [2];
                String stimestamp=new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (stp));
                String etimestamp=new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (etp));
                int i=0;
                StringTokenizer st = new StringTokenizer(stimestamp," ");
                while (st.hasMoreTokens()) {
                    dt[i]=(st.nextToken());
                    //    System.out.print("\ndate time... : "+dt[i]);
                    i++;
                }
                i=0;
                StringTokenizer ste = new StringTokenizer(etimestamp," ");
                while (ste.hasMoreTokens()) {
                    dte[i]=(ste.nextToken());
                    //    System.out.print("\ndate time... : "+dt[i]);
                    i++;
                }
                //System.out.print("................inside while of history......................\n");

                //******************************************
                /** Creating a TextView to add to the row **/
                Date = new TextView(getActivity());
                Date.setText(" " + dt[0] + "  ");
                Date.setTextColor(Color.argb(255, 0, 150, 136));
                Date.setTextSize(12);
                Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Date.setPadding(5, 5, 5, 0);
                Date.setBackgroundResource(R.drawable.tabler);
                tr.addView(Date);  // Adding textView to tablerow.



                /** Creating another textview **/
                Time = new TextView(getActivity());
                Time.setText("   " + dt[1] + "  ");
                Time.setTextColor(Color.argb(255, 0, 150, 136));
                Time.setTextSize(12);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Time.setPadding(5, 5, 5, 0);
                Time.setBackgroundResource(R.drawable.tabler);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Time); // Adding textView to tablerow.


                Time = new TextView(getActivity());
                Time.setText(" " + dte[0] + "  ");
                Time.setTextColor(Color.argb(255, 0, 150, 136));
                Time.setTextSize(12);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Time.setPadding(5, 5, 5, 0);
                Time.setBackgroundResource(R.drawable.tabler);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Time); // Adding textView to tablerow.

                Time = new TextView(getActivity());
                Time.setText("   " + dte[1] + "  ");
                Time.setTextColor(Color.argb(255, 0, 150, 136));
                Time.setTextSize(12);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Time.setPadding(5, 5, 5, 0);
                Time.setBackgroundResource(R.drawable.tabler);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Time); // Adding textView to tablerow.

                for(int col=0;col<paramt.size();col++) {
                    String val=obj.getString(paramt.get(col).toUpperCase());//ACCEPT_CASE_INSENSITIVE_PROPERTIES
                    /** Creating a TextView to add to the row **/
                    Parameter = new TextView(getActivity());
                    Parameter.setText(" " + val);

                        Parameter.setTextColor(Color.argb(255, 0, 150, 136));

                    Parameter.setTextSize(12);
                    Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Parameter.setPadding(5, 5, 5, 0);
                    Parameter.setBackgroundResource(R.drawable.tabler);
                    tr.addView(Parameter);
                    PDFString=PDFString+"    "+val;
                    // Adding textView to tablerow.
                }

                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();//Toast.makeText(getActivity(),"No data available on this date ",Toast.LENGTH_LONG).show();
        }catch  (IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.print("index out of bound");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void Session_forSharePDf(String fileName){
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionFileNameAvg(fileName);
    }
    int shareFlag=0;
    public void createPDF  () {
        /*https_val = https://220.227.124.134:8081;


            inUrl =  https_val+"/smartcity/gassensor/getpdf?token="+token+"&file="+pdfname;*/
        if (file == "") {
            shareFlag=0;
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Please generate Avg data", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        } else {
            shareFlag=1;
            token = sharedPreferences.getString("token", null);
            Session_forSharePDf(file);
            String url=sharedPreferences.getString("url", null);
            String inURL = url+"/getpdf?token=" + token + "&file=" + file;
            System.out.println(" in create file name " + file + "   url " + inURL);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
            startActivity(browserIntent);

            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "PDF downloaded", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show(); //

        }
    }
    private void shareFile() {
        String fileName;

        try {
            fileName = sharedPreferences.getString("filenameavg", null);
            System.out.print("\n  in current file name" + fileName);
            if (fileName == null) {
                Snackbar snackbar;
                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(), "Please Download  Avg data file", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                //Displaying the snackbar using the show method()
                snackbar.show();
            } else {

                Toast.makeText(getActivity(), "Last downloaded file " + fileName, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
      /*  intent.setType("application/pdf");
        System.out.println("in share  file name " + file + "   url " );
        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
*/
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName;
                File dir = new File(path);
                Log.d("PDFCreator", "PDF Path: " + path);

                File filet = new File(dir, file);
                intent.setType("message/rfc822");
                Uri uri = Uri.fromFile(dir);
                System.out.print("userghfjdghfhdgs" + uri);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                try {
                    startActivity(Intent.createChooser(intent, "Share PDF file"));
                } catch (Exception e) {
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), "Error: Cannot open or share created PDF report.", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();  //  Toast.makeText(getActivity(), "Error: Cannot open or share created PDF report.", Toast.LENGTH_SHORT).show();
                }

        /*String shareBody = "http//:facebook.com";

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CPCP Current Data Report");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));*/

            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "File not found", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        }
    }

}
