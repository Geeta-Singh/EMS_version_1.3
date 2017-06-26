package com.bydesign.ems1.Fragments;

/*
* Created by Parikshit Sharma
*
* */
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.ems1.R;
import com.bydesign.ems1.navigationdrawer;
import com.bydesign.ems1.services.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataConditionFragment extends Fragment{
    //Variables
    private RelativeLayout mainLayout;
    private PieChart mChart;
    SessionManager sessionManager;
    JSONArray Red;
    JSONArray No_Info;
    JSONArray Green;
    JSONArray CalibrationMode;
    JSONArray Yellow;
    private String[] xData;
    private float[] yData;
    TextView TotalDevices;
    TextView RedDevices;
    TextView GreenDevices;
    TextView YellowDevices;
    TextView NoInfoDevices;
    TextView Calibration;
    TableLayout tl;
    TableRow tr;
    Timer autoUpdate;
    String token;
    HttpAsyncTask BackgroundLoopTask;
    ProgressDialog pdialog;
    String url;
    CardView cv1,cv2,cv3,cv4,cv5,cv6,cv7;

    // we're going to display pie chart for conditions of all active devices

    public DataConditionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_datacondition, container, false);
        return v;
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
    List<String> mAnimals;
    //OnStart Method Implementation....
    SharedPreferences sharedPreferences;
    public void onStart() {
        super.onStart();

        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());


        tl = (TableLayout) getView().findViewById(R.id.maintable);
        TotalDevices = (TextView) getView().findViewById(R.id.total);
        GreenDevices=(TextView) getView().findViewById(R.id.green);
        YellowDevices =(TextView) getView().findViewById(R.id.yellow);
        RedDevices = (TextView) getView().findViewById(R.id.red);
        Calibration = (TextView) getView().findViewById(R.id.calibration);
        NoInfoDevices= (TextView) getView().findViewById(R.id.noinfo);
        cv1= (CardView) getView().findViewById(R.id.card_view1);
        cv1.setVisibility(View.INVISIBLE);
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


        System.out.print("inside on start of device condition");

        if (isConnected()) {
            pdialog = ProgressDialog.show(getContext(), "", "Please wait...", true);
            pdialog.setCancelable(true);
          //  new HttpAsyncTask().execute();
            //  Toast.makeText(getActivity(), "you are connected", Toast.LENGTH_LONG).show();
            BackgroundLoopTask= new HttpAsyncTask();
            try {
                new HttpAsyncTask().execute();
              //  BackgroundLoopTask.execute().get(1000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                pdialog.dismiss();
                Snackbar snackbar = Snackbar.make(getView(), "Please Check Internet Connection", Snackbar.LENGTH_LONG);
                snackbar.show();
                e.printStackTrace();
            }


            BackgroundLoopTask.cancel(true);
        }
        else{
            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), "Please check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();


        }

        //Shared preferences  ...
        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        url = sharedPreferences.getString("url", null);


        //Pie chart declaration ...
        mainLayout = (RelativeLayout) getView().findViewById(R.id.mainLayoutdata);
        mChart = new PieChart(getActivity());
        // add pie chart to main layout
        mainLayout.addView(mChart);
        mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // configure pie chart
        mChart.setUsePercentValues(false);
        mChart.setDescription("");

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(false);
        mChart.setHoleRadius(55f);
        mChart.setTransparentCircleRadius(61f);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                // display msg when value selected
                try {
                    String string = new String();
                    if (e == null)
                        return;

                    else if(e.getXIndex()==0){
                        mAnimals = new ArrayList<String>();
                        for(int i=0;i<Red.length();i++)
                        {

                            string+="\n"+"DEVICE NAME:  "+Red.getJSONObject(i).getString("dname");
                            //System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));

                            mAnimals.add(Red.getJSONObject(i).getString("dname"));
                        }

                        dialigBox();//   Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    }
                    else if(e.getXIndex()==1){
                        mAnimals = new ArrayList<String>();
                        for(int i=0;i<Green.length();i++)
                        {

                            string+="\n"+"DEVICE NAME:  "+Green.getJSONObject(i).getString("dname");
                            // System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));
                          //  mAnimals = new ArrayList<String>();
                            mAnimals.add(Green.getJSONObject(i).getString("dname"));
                        }
                        dialigBox();// Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    }

                    else if(e.getXIndex()==2){
                        mAnimals = new ArrayList<String>();
                        for(int i=0;i<Yellow.length();i++)
                        {

                            string+="\n"+"DEVICE NAME:  "+Yellow.getJSONObject(i).getString("dname");
                            //.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));
                           // mAnimals = new ArrayList<String>();
                            mAnimals.add(Yellow.getJSONObject(i).getString("dname"));
                        }

                        dialigBox();//  Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    }

                    else if(e.getXIndex()==3){
                        mAnimals = new ArrayList<String>();
                        for(int i=0;i<No_Info.length();i++)
                        {

                            string+="\n"+"DEVICE NAME: "+No_Info.getJSONObject(i).getString("dname");
                            // System.out.println(No_Info.getJSONObject(i).getString("sid") + "  " + No_Info.getJSONObject(i).getString("did"));
                          //  mAnimals = new ArrayList<String>();
                            mAnimals.add(No_Info.getJSONObject(i).getString("dname"));
                        }

                        dialigBox();// Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    }

                    else if(e.getXIndex()==4){
                        mAnimals = new ArrayList<String>();
                        for(int i=0;i<CalibrationMode.length();i++)
                        {

                            string+="\n"+"DEVICE NAME:  "+CalibrationMode.getJSONObject(i).getString("dname");
                            // System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));
                          //  mAnimals = new ArrayList<String>();
                            mAnimals.add(CalibrationMode.getJSONObject(i).getString("dname"));
                        }
                        dialigBox();//Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();

                }

            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
    }

  //Adding data to the pie chart...
    private void addData(String data) throws JSONException {
        JSONObject job = new JSONObject(data);
        Red = job.getJSONArray("rlist");
        No_Info = job.getJSONArray("no_infoList");
        Green = job.getJSONArray("glist");
        Yellow = job.getJSONArray("ylist");
        CalibrationMode = job.getJSONArray("cmlist");
        yData = new float[]{Float.parseFloat(job.getString("r")), Float.parseFloat(job.getString("g")),Float.parseFloat(job.getString("y")), Float.parseFloat(job.getString("no_info")),Float.parseFloat(job.getString("cm"))};
       // xData = new String[]{"Red", "Green","Yellow", "No Info", "Calibration"};
            xData = new String[]{"", "","", "", ""};
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < yData.length; i++) {


            yVals1.add(new Entry(yData[i], i));
            xVals.add(xData[i]);



        }

        for(int i=0;i<xVals.size();i++){
            System.out.println("\n XVALs   "+xVals.get(i)+"\n"+" Yvals   "+yVals1.get(i));
        }
        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.argb(255,194,22,13));
        colors.add(Color.argb(255,0,230,118));
        colors.add(Color.argb(255, 255, 170, 0));
        colors.add(Color.argb(255,150,63,79));
        colors.add(Color.argb(255,112,92,215));





        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData dataV;
        dataV = new PieData(xVals, dataSet);
        dataV.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mf = new DecimalFormat();
                return mf.format(value);
            }
        });
        dataV.setValueTextSize(11f);
        dataV.setValueTextColor(Color.WHITE);

        mChart.setData(dataV);
        System.out.print("\n data is " + dataV);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();

    }
    /*
    *
    ***************************** NOtification for current data*****************************
    *
    * */
    public void checkRedDevice(String data){
        JSONObject job = null;
        String notiMsg="";
        try {
            job = new JSONObject(data);
            int r=job.getInt("r");
            Red = job.getJSONArray("rlist");
            System.out.print("no red devices out side if  "+Red+r);
            if(r==0){
                System.out.print("no red devices "+Red+r);

            }else{
                for(int i=0;i<Red.length();i++)
                {

                    notiMsg+="\n"+"DEVICE NAME:  "+Red.getJSONObject(i).getString("dname");
                    //System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));

                   // mAnimals.add(Red.getJSONObject(i).getString("dname"));
                }
                callNotification(notiMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    static NotificationManager notificationManager;
    int id = 100;
    static Notification notification;
    static Context context;//=getApplicationContext();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void callNotification(String message) {
        System.out.println("Notification");
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
        Intent notificationIntent;
        notificationIntent = new Intent(getActivity(),navigationdrawer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setAutoCancel(true);
        builder.setTicker("Notification ");
        builder.setContentTitle("EMS Notification");
        builder.setContentText("Alert Current data ");
        builder.setSmallIcon(R.drawable.icon2);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
        builder.build();
        String msgText = "Alert :" + message + "\n  Devices having Data higher then threshold ";
        notification = new Notification.BigTextStyle(builder)
                .bigText(msgText).build();
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify((int)System.currentTimeMillis(), notification);//(int)System.currentTimeMillis()

    }

    public void dialigBox(){
     //   String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};
        try {

            final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle("Device IDs").setIcon(R.drawable.details);
            dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.addSessionForCurrent(Animals[item].toString());
                    System.out.println("SELECTED DEVICE ID :"+Animals[item].toString());
                    TabFragment main = new TabFragment();
                    navigationdrawer nv=new navigationdrawer();
                    nv.toolbar.setTitle("Latest Data");
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, main);
                    ft.commit();
                    String selectedText = Animals[item].toString();  //Selected item in listview
                }
            });

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //Create alert dialog object via builder
            AlertDialog alertDialogObject = dialogBuilder.create();
            //Show the dialog
            alertDialogObject.show();


        }
        catch (NullPointerException n){
            n.printStackTrace();
        }
    }


    //Sending request to server for data...
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i=0;
        String resul="";
        @Override
        protected String doInBackground(String... urls) {
            resul=  dataCondition(url + "/dataCondition");
            return resul;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("data condtion :" + status);

            try {
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    Toast.makeText(getActivity(), "Error : Few parameters are missing", Toast.LENGTH_LONG).show();
                    pdialog.dismiss();
                }else if(status.equalsIgnoreCase("Invalid")){
                    pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                   // new HttpForLogout().execute();
                    sessionManager.logoutUser();
                } else {
                    addDataTable(status);
                    addData(status);
                    checkRedDevice(status);
                    pdialog.dismiss();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String dataCondition(String url) {
        System.out.println("Inside Data Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        try {

            System.out.println("token :"+token +url);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String sid = "All",did="All",org="All";
            String details;
            String json = "";
           /* details= sharedPreferences.getString("deviceDetails", null);
            System.out.print(" \n@#$ sedice details"+details);*/

            try{
                details= sharedPreferences.getString("deviceDetails", null);
                System.out.print(" \n@#$ sedice details"+details);

                if(!details.equalsIgnoreCase(null)) {
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
               // pdialog.dismiss();
                e.printStackTrace();
            }
            System.out.print(" \n sid "+sid+"       did  "+did+"        org"+org);

            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            jsonObject2.accumulate("sid", sid);
            jsonObject2.accumulate("did", did);
            jsonObject2.accumulate("orgid", org);
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
            // httpPost.setHeader("Content-Length", se.getContentLength()+"");

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            System.out.println("F22222"+inputStream );
            result = convertInputStreamToString(inputStream);
        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();} catch (Exception e) {
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

    //*****************************
    JSONObject job;
    public void addDataTable(String data) throws JSONException {
        System.out.print("Add data in table");
        job = new JSONObject(data);

        try{

            TotalDevices.setText(" " + job.getString("total") + " ");
            RedDevices.setText(" " + job.getString("r") + " ");
            GreenDevices.setText(" " + job.getString("g") + " ");
            YellowDevices.setText(job.getString("y"));
            NoInfoDevices.setText(" " + job.getString("no_info") + " ");
            Calibration.setText("" + job.getString("cm") + " ");


            cv1.setVisibility(View.VISIBLE);
            cv2.setVisibility(View.VISIBLE);
            cv3.setVisibility(View.VISIBLE);
            cv4.setVisibility(View.VISIBLE);
            cv5.setVisibility(View.VISIBLE);
            cv6.setVisibility(View.VISIBLE);
            cv7.setVisibility(View.VISIBLE);
            cv1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));
            cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv3.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv4.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv5.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv6.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv7.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_in));
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch  (IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.print("index out of bound");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e)
        {e.printStackTrace();}
    }
    //***************************** NOTIFIcation********************************

    int count=0;
/*PDF ENDED*/
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {


                System.out.print("inside loop data condition"+count);
                count++;
                BackgroundLoopTask = new HttpAsyncTask();
                BackgroundLoopTask.execute();

            }
        }, 0, 120000); // updates each 120 secs
    }
}
