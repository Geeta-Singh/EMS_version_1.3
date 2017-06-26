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
import java.util.Date;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyzerStatusReportFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TableLayout tl;
    TableRow tr;
    ImageButton to, from;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    int flag = 0, flagForcheck = 0;
    TextView Parameter, A_PPM, t1;
    private EditText fromText, totext;
    Spinner spinner;
    String Selecteddevice;
    String fromd, tod,PDFString,file="";
    FloatingActionButton save,share;
    String Devices,Devids;
   //Constructor
    public AnalyzerStatusReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analyzerstatusreport, container, false);
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

    public boolean check(String user, String pwd) {
        if (user.equalsIgnoreCase("") || pwd.equalsIgnoreCase("")) {
            return false;
        } else return true;
    }



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
           // Toast.makeText(getActivity(),"Device no found",Toast.LENGTH_LONG).show();
        }
        return  categories;
    }

    public void onPause(){

        super.onPause();
    }
    public void onResume(){
        super.onResume();
        setRetainInstance(true);
    }
    CardView cv,cv2,cv3,cv4;
    public void onStart() {
        super.onStart();
        try {
            sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
            PDFString = null;
            cv = (CardView) getView().findViewById(R.id.card_view11);
            cv.setVisibility(View.INVISIBLE);
            cv2 = (CardView) getView().findViewById(R.id.card_1);
            cv2.setVisibility(View.INVISIBLE);
       /* cv3=(CardView)getView().findViewById(R.id.card_2);
        cv3.setVisibility(View.INVISIBLE);
        cv4=(CardView)getView().findViewById(R.id.card_3);
        cv4.setVisibility(View.INVISIBLE);*/
            save = (FloatingActionButton) getView().findViewById(R.id.savePdf);
            share = (FloatingActionButton) getView().findViewById(R.id.share);
            // SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
            Devices = sharedPreferences.getString("device", null);
            Devids= sharedPreferences.getString("devid",null);
            to = (ImageButton) getView().findViewById(R.id.imageButton2);

            to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 1;
                   /* flag = 1;
                    if (flagForcheck == 0)

                    {
                        Snackbar snackbar;
                        //Initializing snackbar using Snacbar.make() method
                        snackbar = Snackbar.make(getView(), "Please Enter Start date", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                        //Displaying the snackbar using the show method()
                        snackbar.show();// Toast.makeText(getActivity(), "Please First enter start date", Toast.LENGTH_LONG).show();

                    } else {*/
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                  //  }
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

            spinner = (Spinner) getView().findViewById(R.id.spinner);

            // Spinner click listener
            spinner.setOnItemSelectedListener(this);

            // Spinner Drop down elements
            List<String> categories = new ArrayList<String>();
            categories = getDevice(Devices);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);

            if(  sharedPreferences.getString("devices", null)==null){
                System.out.println("@   no data 5678888*&&^^ & * * ");
            }
            else {
               // System.out.println("hu lalalalalalalalalal back from graph  ");
                try {
                /*SessionManager   sessionManagerp = new SessionManager(getActivity());
                sessionManagerp.setTableContaintcurrent(null);*/
                    System.out.println("FECTCHED DEVICE ID  :"+sharedPreferences.getString("devices", null));
                    ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
                    int spinnerPosition = myAdap.getPosition(sharedPreferences.getString("devices",null));
                    spinner.setSelection(spinnerPosition);
                    System.out.print("spinner "+spinner.getSelectedItem()+ " pos "+spinnerPosition);
                    cv.setVisibility(View.VISIBLE);
                    cv2.setVisibility(View.VISIBLE);
                }catch (NullPointerException n){
                    n.printStackTrace();
                }
            }

            tl = (TableLayout) getView().findViewById(R.id.maintable);

            t1 = (TextView) getView().findViewById(R.id.t1);


            // tl.removeAllViews();


          /*  System.out.print("inside on start of device condition");
            System.out.println("K(KIIIIIinsise device");*/


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
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                        dateFrom = formatter.parse(fromd);
                        dateTo=formatter.parse(tod);

                          //  Toast.makeText(getActivity(),dateTo+"   ..."+dateFrom, Toast.LENGTH_LONG).show();
                         //  System.out.print(dateTo+" im date for u  ..."+dateFrom);
                           /* if(dateFrom.compareTo(dateTo)<0){
                                Toast.makeText(getActivity(), "inside before date", Toast.LENGTH_LONG).show();
                            }*/
                            if(dateFrom.before(dateTo)||dateFrom.equals(dateTo)) {
                                if (isConnected()) {
                                    //Toast.makeText(getActivity(), "you are connected", Toast.LENGTH_LONG).show();
                                    pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
                                    // pdialog.setCancelable(true);
                                    new HttpAsyncTask().execute();
                                } else {
                                    Snackbar snackbar;
                                    snackbar = Snackbar.make(getView(), "Please check internet connection", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                }
                            }
                            else{
                                    Snackbar snackbar;
                                    snackbar = Snackbar.make(getView(), "Kindly choose Fromdate before Todate ", Snackbar.LENGTH_LONG);
                                    View snackBarView = snackbar.getView();
                                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                                    snackbar.show();
                            }
                        }else {


                            Snackbar snackbar;
                            //Initializing snackbar using Snacbar.make() method
                            snackbar = Snackbar.make(getView(), "Please enter date", Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                            //Displaying the snackbar using the show method()
                            snackbar.show();
                            //  Toast.makeText(getActivity(), "Please  enter  date", Toast.LENGTH_LONG).show();
                        }
                    } catch (NullPointerException e) {
                        Snackbar snackbar;
                        //Initializing snackbar using Snacbar.make() method
                        snackbar = Snackbar.make(getView(), "Please enter both dates", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                        //Displaying the snackbar using the show method()
                        snackbar.show();
                        //  Toast.makeText(getActivity(), "Please  enter both date", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }/*catch (ParseException e) {
                        e.printStackTrace();
                    }*/
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
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Session_Management(String device)
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionForCurrent(device);
    }

    int devidSpinner;
    ProgressDialog pdialog=null;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Selecteddevice = parent.getItemAtPosition(position).toString();
        devidSpinner=position;
       // System.out.println(item + "  sekectedgfdfgd  " + Selecteddevice);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //****************************Asyn task ***************************************
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i = 0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/
            return analyzerreport();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("summary data  :" + status);
            cv.setVisibility(View.INVISIBLE);
            cv2.setVisibility(View.INVISIBLE);

            try {
               // JSONObject j=new JSONObject(status);
             //   String resulr=j.get("err").toString();
               // System.out.print("\n datata "+resulr);
                if (status.equalsIgnoreCase("{\"err\":\"Devid, stime or etime is missing.\"}")) {
                   // Toast.makeText(getActivity(), "failed status : " + status, Toast.LENGTH_LONG).show();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"failed status : " + status, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
                    //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                    pdialog.dismiss();
                } else if(status.equalsIgnoreCase(null)){
                    MapFragment main=new MapFragment();
                    android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, main);
                    ft.commit();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Please check internet connection", Snackbar.LENGTH_LONG);
                    //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                }
                else if(status.equalsIgnoreCase("Invalid")){
                    pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                 //   new HttpForLogout().execute();
                    sessionManager.logoutUser();
                }else if(status.equalsIgnoreCase("[]")){

                    tl.removeAllViews();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255,0,150,136)); //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                }else {
                    t1.setText("");

                    tl.removeAllViews();
                   /* save.setVisibility(View.INVISIBLE);
                    share.setVisibility(View.INVISIBLE);*/
                   // Toast.makeText(getActivity(), "" + status, Toast.LENGTH_LONG).show();
                    t1.setText("Summary of Device from " + fromd + " To " + tod);
                    addHeaders();
                    addDataTable(status);
                    Session_Management(Selecteddevice);
                    pdialog.dismiss();
                    save.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
             //    cv4.setVisibility(View.VISIBLE);
                    cv.setVisibility(View.VISIBLE);
                    cv2.setVisibility(View.VISIBLE);
                  //   cv3.setVisibility(View.VISIBLE);
                    cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                    cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                 //   cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));

                }
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    String token;SharedPreferences sharedPreferences;
    public String analyzerreport() {
        Log.d("UPDATE METHOD CALLED", "HI I M IN UPDATE");
        InputStream inputStream = null;
        String res = "";
        String result = null;
        try {
           //  sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
            token = sharedPreferences.getString("token", null);
          //  System.out.println("token :" + token);
            String url=sharedPreferences.getString("url", null);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // "http://220.227.124.134:8070/smartcity/gassensor/devicesummary"
            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url+"/devicesummary");

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            List<String> devidList;
            devidList=getDevice(Devids);
         //   System.out.print("devid list");
            /*for(int i=0;i<devidList.size();i++){
                System.out.println(devidList.get(i));
            }*/

          //  System.out.print(" api caling "+ devidSpinner);
            jsonObject2.accumulate("devid", devidList.get(devidSpinner));

          // jsonObject2.accumulate("devid", Selecteddevice);
            String fromd = fromText.getText().toString() + " 00:00:01";
            String tod = totext.getText().toString() + " 23:59:59";
           // System.out.println("date is " + fromd + "    date 2" + tod);
            long epoch1 = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(fromd).getTime();
            long epoch2 = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(tod).getTime();
            System.out.println("tim is " + epoch1 + "    tim 2" + epoch2);
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

            System.out.print(inputStream);
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

    //***************************** date picket*****************************


    public void TOSetDate(int year, int month, int day) {
        toDateEtxt = (EditText) getView().findViewById(R.id.To);
        String d = null, m = null;
        if (day <= 9)
            d = "0" + day;
        else d = "" + day;
        // fromDateEtxt.setText(year + "-" + month + "-" +"0"+ day);
        if (month <= 9)
            m = "0" + month;
        else m = "" + month;
        toDateEtxt.setText(year + "/" + m + "/" + d);
        // toDateEtxt.setText(year + "-" + month + "-" + day);
        // Toast.makeText(getActivity(), "you selected to date" + toDateEtxt.getText(), Toast.LENGTH_LONG).show();

    }


    public void populateSetDate(int year, int month, int day) {
        fromDateEtxt = (EditText) getView().findViewById(R.id.editText1);
        String d, m;
        if (day < 9) d = "0" + day;
        else d = "" + day;
        // fromDateEtxt.setText(year + "-" + month + "-" +"0"+ day);
        if (month < 9)
            m = "0" + month;
        else
            m = "" + month;

        fromDateEtxt.setText(year + "/" + m + "/" + d);


        // Toast.makeText(getActivity(),"you selected from date"+fromDateEtxt.getText(),Toast.LENGTH_LONG).show();
        flagForcheck = 1;
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
           /* return new DatePickerDialog(getActivity(), this, yy, mm, dd);*/
            DatePickerDialog dialog=new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            if (flag == 0) populateSetDate(yy, mm + 1, dd);
            else TOSetDate(yy, mm + 1, dd);

        }

    }
    public void addHeaders(){

        tr = new TableRow(getActivity());
        // tr.setBackgroundColor(Color.argb(196,18,86,136));
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView Date = new TextView(getActivity());
        Date.setText("Qcode");
        Date.setTextColor(Color.WHITE);
        Date.setTextSize(12);
        Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Date.setPadding(5, 5, 5, 0);
        Date.setBackgroundResource(R.drawable.tableh);//cell.png);
        tr.addView(Date);  // Adding textView to tablerow.

        TextView Time = new TextView(getActivity());
        Time.setText("duration (min)");
        Time.setBackgroundResource(R.drawable.tableh);
        Time.setTextSize(12);
        Time.setTextColor(Color.WHITE);
        Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Time.setPadding(5, 5, 5, 0);
        tr.addView(Time);  // Adding textView to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        // we are adding two textviews for the divider because we have two columns
        // QCODE.setVisibility(View.VISIBLE);


    }

    public void addDataTable(String status)
    {
        System.out.print("Add data in table");
        try{

            JSONObject jsonObject = new JSONObject(status);
            if(jsonObject.length()==0){
                tl.removeAllViews();
                Snackbar snackbar;
                 snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
                //Displaying the snackbar using the show method()
                snackbar.show();
            }
            file=jsonObject.get("file").toString();
            JSONArray list = (JSONArray) jsonObject.get("list");
            if(list.length()==0){
                tl.removeAllViews();
                     Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
                //Displaying the snackbar using the show method()
                snackbar.show();
            }
            for(int i=0;i<list.length();i++) {

                JSONObject object = list.getJSONObject(i);

               // System.out.print("\n jsonobject  " + jsonObject + "\n object   " + object + "\n length " + jsonObject.length());

               // JSONObject value = (JSONObject) object.get("exceed");


                String min =  object.get("QCode").toString();
                String max =  object.get("duration").toString();
                //  ArrayList<String> paramt = stringToken(param.toString());

              //  System.out.println("param " + min + " \n" + value+ "     \n     "+max);

                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));


                Parameter = new TextView(getActivity());
                Parameter.setText("    "+min);
                Parameter.setTextColor(Color.argb(255, 0, 150, 136));
                Parameter.setTextSize(10);
                Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Parameter.setPadding(5, 5, 5, 0);
                Parameter.setBackgroundResource(R.drawable.tabler);
                tr.addView(Parameter);  // Adding textView to tablerow.

                A_PPM = new TextView(getActivity());
                A_PPM.setText("      "+max);
                A_PPM.setTextColor(Color.argb(255, 0, 150, 136));
                A_PPM.setTextSize(10);
                A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_PPM.setPadding(5, 5, 5, 0);
                A_PPM.setBackgroundResource(R.drawable.tabler);
                tr.addView(A_PPM);  // Adding textView to tablerow.

                // CO = new TextView[N];

                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.print("index out of bound");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void Session_forSharePDf(String fileName){
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionFileNamedevice(fileName);
    }
    int shareFalg=0;
    public void createPDF  () {
        /*https_val = https://220.227.124.134:8081;


            inUrl =  https_val+"/smartcity/gassensor/getpdf?token="+token+"&file="+pdfname;*/
        if (file == "") {
            shareFalg=0;
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Please generate Analyser status report", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        } else {
            shareFalg=1;
            Session_forSharePDf(file);
            String url=sharedPreferences.getString("url", null);
            String inURL = url+"/getpdf?token=" + token + "&file=" + file;
           // String inURL = "http://220.227.124.134:8070/smartcity/gassensor/getpdf?token=" + token + "&file=" + file;
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

        fileName=sharedPreferences.getString("filenamedevice", null);
        System .out.print("\n  in current file name"+fileName);
        if (fileName == null) {
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Please download Analyser status report pdf", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        } else {
            Toast.makeText(getActivity(),"Last downloaded file "+fileName,Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), "Error: Cannot open or share created PDF report.", Toast.LENGTH_SHORT).show();
            }

        /*String shareBody = "http//:facebook.com";

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CPCP Current Data Report");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));*/

        }
    }
}
