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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricalData_Table extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinner2;
    String selectGranu;

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
    CardView cardGraph;
    String fromd,tod,PDFString;
    String tablegraph="table";

    String devicename;
    public HistoricalData_Table() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historicaldata__table, container, false);
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
            e.printStackTrace();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Device not found", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
          //  Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return  categories;
    }



    FloatingActionButton save,share;



long table_date;
    CardView cv;
    public void onStart() {
        super.onStart();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());


        save=(FloatingActionButton)getView().findViewById(R.id.savePdf);
        share=(FloatingActionButton)getView().findViewById(R.id.share);
        cv=(CardView)getView().findViewById(R.id.card_view5);
        cv.setVisibility(View.INVISIBLE);

        tl = (TableLayout) getView().findViewById(R.id.maintable);
        th = (TableLayout) getView().findViewById(R.id.maintableH);

       // System.out.print("inside on start of historical condition");

        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        Devices = sharedPreferences.getString("device", null);
        Devids= sharedPreferences.getString("devid", null);

        List<String> categories = new ArrayList<String>();
        categories=getDevice(Devices);

        spinner = (Spinner)getView(). findViewById(R.id.spinner);
        spinner2 = (Spinner)getView(). findViewById(R.id.s1);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        // ################### table graph data #################################

        tablegraph=sharedPreferences.getString("tablegraphsession", null);
        devicename=sharedPreferences.getString("hisDevice",null);
        System.out.println("@Table and graphe data "+tablegraph+" \n   *  "+sharedPreferences.getString("devices", null) +devicename);

        if(  tablegraph==null && devicename==null ){
            System.out.println("@   no data 5678888*&&^^ & * * ");

        }

        else{
           // System.out.println("hu lalalalalalalalalal back from graph  ");

            System.out.println("FECTCHED DEVICE ID  :"+devicename);
            ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdap.getPosition(devicename);
            spinner.setSelection(spinnerPosition);
            System.out.print("spinner " + spinner.getSelectedItem() + " pos " + spinnerPosition);
            fromText = (EditText) getView().findViewById(R.id.editText1);
            fromText.setText(sharedPreferences.getString("shtime", null));
            addHeaders(tablegraph);
            addDataTable(tablegraph);
            cv.setVisibility(View.VISIBLE);
        }

//*************************************************************************



        Button summay=(Button)getView().findViewById(R.id.show);
        summay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromText = (EditText) getView().findViewById(R.id.editText1);
                /*fromText = (EditText) getView().findViewById(R.id.editText1);
                totext = (EditText) getView().findViewById(R.id.To);*/
                try {
                    fromd = fromText.getText().toString();
                    //tod = totext.getText().toString();

                    if (check(fromd)) {
                        if (isConnected()) {

                            pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
                            new HttpAsyncTask().execute();


                        } else {
                            Snackbar snackbar;
                            snackbar = Snackbar.make(getView(), "Please check internet connection", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                    } else {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(getView(), "Please  enter  date", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                        snackbar.show();
                    }
                } catch (NullPointerException e) {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(), "Please enter date", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    e.printStackTrace();
                }
            }
        });



        from=(ImageButton)getView().findViewById(R.id.imageButton1);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
              createPDF();
            }
        });
        share=(FloatingActionButton)getView().findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                shareFile();
            }
        });
    }


    public boolean check(String user){
        if(user.equalsIgnoreCase("")){
            return false;
        }
        else return true;
    }
    ProgressDialog pdialog=null;
    int devidSpinner;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        devidSpinner=position;
        Selecteddevice=parent.getItemAtPosition(position).toString();
       // System.out.println(item + "  sekectedgfdfgd  " + Selecteddevice + "   pos : " + position);


    }

    String file="";
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //****************************
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {

            return historicaldata();

        }

        protected  void onPreExecute(){

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("Historical data  :" + status);
           // Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
            try{
                if(status.equalsIgnoreCase("stime / etime / devid or option is missing.")){
                   // Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), ""+status, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                    pdialog.dismiss();
                }
                else if(status.equalsIgnoreCase(null)){
                    MapFragment main=new MapFragment();
                    android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, main);
                    ft.commit();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Please check internet connection", Snackbar.LENGTH_LONG);
                    //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                } else if(status.equalsIgnoreCase("Error : Invalid user.")) {

                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), ""+status, Snackbar.LENGTH_LONG);
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                    pdialog.dismiss();
                }
                else if(status.equalsIgnoreCase("invalid")){
                    pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                  //  new HttpForLogout().execute();
                    sessionManager.logoutUser();
                }else if(status.equalsIgnoreCase("[]")){
                    tl.removeAllViews();

                    th.removeAllViews();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255,0,150,136)); //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                } else {
                      tl.removeAllViews();

                    th.removeAllViews();
                    SessionManager sessionManagerp = new SessionManager(getActivity());
                    sessionManagerp.setTableContaint(status);
                   // sessionManagerp.addSessionForCurrent(Selecteddevice);
                   // sessionManagerp.addFlage(0);
                    sessionManagerp.setDevicehis(Selecteddevice);
                    sessionManagerp.setstartHTime(fromText.getText().toString());
                    System.out.println("historical session data "+Selecteddevice+fromText.getText().toString());

                    addHeaders(status);
                    addDataTable(status);
                    //Session_Management(Selecteddevice);

                    pdialog.dismiss();
                    save.setVisibility(View.VISIBLE);
                    cv.setVisibility(View.VISIBLE);

                    cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));

                    share.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


   /* public void Session_Management(String device)
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionForCurrent(device);

    }*/

    String token;
    SharedPreferences sharedPreferences;
    public String historicaldata() {
        Log.d("UPDATE METHOD CALLED", "HI I M IN UPDATE");
        InputStream inputStream = null;
        String res = "";
        String result = null;
        try {

            token = sharedPreferences.getString("token", null);
            //"http://220.227.124.134:8070/smartcity/gassensor/report/history"
            String url=sharedPreferences.getString("url", null);
            System.out.println("token :"+token+" url "+url);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url+"/report/history");

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);

            List<String> devidList;
            devidList=getDevice(Devids);
           // System.out.print("devid list");

          //  System.out.print(" api caling "+ devidSpinner);
            jsonObject2.accumulate("devid", devidList.get(devidSpinner));


            jsonObject2.accumulate("option", "NO");

            String fromd=fromText.getText().toString()+" 00:00:01";
            String tod=fromText.getText().toString()+" 23:59:59";

        //    System.out.println("date is "+fromd+"    date 2 "+tod);
            long epoch1 = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(fromd).getTime() ;
            long epoch2 = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(tod).getTime() ;

          // System.out.println("tim is " + epoch1 + "    tim 2" + epoch2);
            jsonObject2.accumulate("stime", epoch1);
            jsonObject2.accumulate("etime", epoch2);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }


  /*  public void Session_Management(String device,String sts,String ets,int gran,String para)
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionData1(device, sts, ets, gran, para);
    }
*/
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

       /* TableGraphData tgd= new TableGraphData();
        tgd.setDeviceNAme("geeta singh rajput");
        tgd.setTableDetails(status);*/
        System.out.println(" in history device name from model" );

        try {

            JSONArray jsonObject = new JSONArray(status);
        JSONObject object=jsonObject.getJSONObject(0);
        JSONArray param = (JSONArray) object.get("param");
        JSONArray unit= ( JSONArray) object.get("units");
            file=object.get("file").toString();
        ArrayList<String>paramt=stringToken(param.toString());
        ArrayList<String>units=stringToken(unit.toString());
        // System.out.println( "\n hi geeta  " + unit+"\n hi seeta "+object+"\n para  "+param+"\n value"+value);


        tr = new TableRow(getActivity());
        // tr.setBackgroundColor(Color.argb(196,18,86,136));
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView Date = new TextView(getActivity());
        Date.setText("Date");
        Date.setTextColor(Color.WHITE);
        Date.setTextSize(12);
        Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Date.setPadding(5, 5, 5, 0);
        Date.setBackgroundResource(R.drawable.tableh);//cell.png);
        tr.addView(Date);  // Adding textView to tablerow.


        TextView Time = new TextView(getActivity());
        Time.setText("Time");
        Time.setBackgroundResource(R.drawable.tableh);
        Time.setTextSize(12);
        Time.setTextColor(Color.WHITE);
        Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Time.setPadding(5, 5, 5, 0);
        tr.addView(Time);  // Adding textView to tablerow.


        for(int row=0;row<paramt.size();row++) {
            TextView  Co = new TextView(getActivity());
            System.out.print(" param :" + paramt.get(row) + "\n" + units.get(row));
            Co.setText("   " + param.get(row) + "\n" + "   " + unit.get(row));
            Co.setTextColor(Color.WHITE);
            Co.setTextSize(12);
            Co.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Co.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Co.setPadding(5, 5, 5, 0);
            Co.setBackgroundResource(R.drawable.tableh);
            tr.addView(Co);

        }

        A_PPM = new TextView(getActivity());
        A_PPM.setText("  Qcode ");
        A_PPM.setTextColor(Color.WHITE);
        A_PPM.setTextSize(12);
        A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        A_PPM.setPadding(5, 5, 5, 0);
        A_PPM.setBackgroundResource(R.drawable.tableh);
        tr.addView(A_PPM);


        // Add the TableRow to the TableLayout
        th.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        // we are adding two textviews for the divider because we have two columns
        // QCODE.setVisibility(View.VISIBLE);

    } catch (JSONException e) {
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);

            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        e.printStackTrace();
    }
}

    public void addDataTable(String status)
    {
        System.out.print("Add data in table");
        try{

            JSONArray jsonObject = new JSONArray(status);
            JSONObject object=jsonObject.getJSONObject(0);
            JSONArray value = (JSONArray) object.get("values");
              JSONArray param = (JSONArray) object.get("param");
            JSONArray alarm = (JSONArray) object.get("alarm");
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
                /** Create a TableRow dynamically **/
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                // System.out.print(" \nts:" + tp + "   " + i);
                //********************* time and date*******************

                JSONObject obj=value.getJSONObject(row);
                String tp=obj.getString("ts");
                Long ds=Long.parseLong(tp);
                //System.out.print("string is "+tp+"    long is "+ds);
                String [] dt=new String [2];
                String timestamp=new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (ds));

                int i=0;
                StringTokenizer st = new StringTokenizer(timestamp," ");
                while (st.hasMoreTokens()) {
                    dt[i]=(st.nextToken());
                    //    System.out.print("\ndate time... : "+dt[i]);
                    i++;
                }
                //System.out.print("................inside while of history......................\n");

                //******************************************
                /** Creating a TextView to add to the row **/
                Date = new TextView(getActivity());
                Date.setText("  " + dt[0] + " ");
                Date.setTextColor(Color.argb(255, 0, 150, 136));
                Date.setTextSize(10);
                Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Date.setPadding(5, 5, 5, 0);
                Date.setBackgroundResource(R.drawable.tabler);
                tr.addView(Date);  // Adding textView to tablerow.
                PDFString=PDFString+" "+dt[0];
                /** Creating another textview **/
                Time = new TextView(getActivity());
                Time.setText("   " + dt[1] + " ");
                Time.setTextColor(Color.argb(255, 0, 150, 136));
                Time.setTextSize(10);
                //  Time.setBackgroundResource(R.drawable.cell);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Time.setPadding(5, 5, 5, 0);

                Time.setBackgroundResource(R.drawable.tabler);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Time); // Adding textView to tablerow.
                PDFString=PDFString+"  "+dt[1];
                for(int col=0;col<paramt.size();col++) {
                    String val=obj.getString(paramt.get(col).toUpperCase());
                    /** Creating a TextView to add to the row **/
                    Parameter = new TextView(getActivity());
                    Parameter.setText("   " + val);
                    //  System.out.println(" alarm  " + Double.parseDouble(alarm.get(col).toString()) + "            " + Double.parseDouble(val));
                    if (Double.parseDouble(val) > Double.parseDouble(alarm.get(col).toString())) {
                        Parameter.setTextColor(Color.RED);
                    }
                    else
                        Parameter.setTextColor(Color.argb(255, 0, 150, 136));

                    Parameter.setTextSize(10);
                    Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    Parameter.setPadding(5, 5, 5, 0);
                    Parameter.setBackgroundResource(R.drawable.tabler);
                    tr.addView(Parameter);
                    PDFString=PDFString+"  "+val;
                    // Adding textView to tablerow.
                }

                A_PPM = new TextView(getActivity());
                A_PPM.setText("   " + obj.getString("QCode") );
                A_PPM.setTextColor(Color.argb(255, 0, 150, 136));
                A_PPM.setTextSize(10);
                A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_PPM.setPadding(5, 5, 5, 0);
                A_PPM.setBackgroundResource(R.drawable.tabler);
                tr.addView(A_PPM);
                PDFString=PDFString+" "+obj.getString("QCode")+"\n\n";
                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
            //Displaying the snackbar using the show method()
            snackbar.show();
            pdialog.dismiss();
        }catch  (IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.print("index out of bound");
            pdialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
            pdialog.dismiss();
        }
       // System.out.println(" @#$%^&      out side add data in to table");

      //  cardGraph.setVisibility(View.VISIBLE);
    }
    int shareFalg=0;
    public void Session_forSharePDf(String fileName){
        //Session Manager
        SessionManager sessionManager1 = new SessionManager(getActivity());
        sessionManager1.addSessionFilehistory(fileName);
    }
    public void createPDF  () {
        /*https_val = https://220.227.124.134:8081;


            inUrl =  https_val+"/smartcity/gassensor/getpdf?token="+token+"&file="+pdfname;*/
        if (file == "") {
            shareFalg=0;
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Please generate historical data", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        } else {
            shareFalg=1;
            token = sharedPreferences.getString("token", null);
            Session_forSharePDf(file);
            String url=sharedPreferences.getString("url", null);
            String inURL = url+"/getpdf?token=" + token + "&file=" + file;
//            System.out.println(" in create file name " + file + "   url " + inURL);
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

        try{
        fileName=sharedPreferences.getString("filenamehistory", null);
        System .out.print("\n  in current file name"+fileName);
        if (fileName == null) {
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Please Download historical data file", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        } else {
            Toast.makeText(getActivity(), "Last downloaded file " + fileName, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
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
                snackbar = Snackbar.make(getView(), "File no share", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                //Displaying the snackbar using the show method()
                snackbar.show();//  Toast.makeText(getActivity(), "Error: Cannot open or share created PDF report.", Toast.LENGTH_SHORT).show();
            }
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
