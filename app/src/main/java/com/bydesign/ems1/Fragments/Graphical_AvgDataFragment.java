package com.bydesign.ems1.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Graphical_AvgDataFragment extends Fragment {
    private RelativeLayout m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12;

    LinearLayout ll;
    ArrayList<BarEntry> entries;
    ArrayList<String> labels;
    private BarChart mChart;
    TableLayout tl;
    TableRow tr;
    private TextView device,gran,sts,ets;
    String token,url,Device,opt;
    Long Sts,Ets;
    int Gran;
    ProgressDialog pdialog=null;
    ImageButton Zoom_avg;
    public Graphical_AvgDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graphical__avg_data, container, false);
    }



    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity(). getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    SharedPreferences sharedPreferences;

    CardView cv,cv1,cv2,cv3,cv4,cv5,cv6,cv7,cv8,cv9,cv10,cv11,card;
    public void onStart() {
        super.onStart();
        //________________________Declaration of views_________________
        m1 = (RelativeLayout) getView().findViewById(R.id.mainLayout1);
        m2 = (RelativeLayout) getView().findViewById(R.id.mainLayout2);
        m3 = (RelativeLayout) getView().findViewById(R.id.mainLayout3);
        m4 = (RelativeLayout) getView().findViewById(R.id.mainLayout4);
        m5 = (RelativeLayout) getView().findViewById(R.id.mainLayout5);
        m6 = (RelativeLayout) getView().findViewById(R.id.mainLayout6);
        m7 = (RelativeLayout) getView().findViewById(R.id.mainLayout7);
        m8 = (RelativeLayout) getView().findViewById(R.id.mainLayout8);
        m9 = (RelativeLayout) getView().findViewById(R.id.mainLayout9);
        m10 = (RelativeLayout) getView().findViewById(R.id.mainLayout10);
        m11 = (RelativeLayout) getView().findViewById(R.id.mainLayout11);
        m12 = (RelativeLayout) getView().findViewById(R.id.mainLayout12);
     //   ll=(LinearLayout)getView().findViewById(R.id.al);
        device = (TextView) getView().findViewById(R.id.sectdevice);

        Zoom_avg= (ImageButton) getView().findViewById(R.id.zoom_avg);

        cv=(CardView)getView().findViewById(R.id.card_viewp1);
        cv1=(CardView)getView().findViewById(R.id.card_view2);
        cv2=(CardView)getView().findViewById(R.id.card_viewp3);
        cv3=(CardView)getView().findViewById(R.id.card_viewp4);
        cv4=(CardView)getView().findViewById(R.id.card_viewp5);
        cv5=(CardView)getView().findViewById(R.id.card_viewp6);
        cv6=(CardView)getView().findViewById(R.id.card_viewp7);
        cv7=(CardView)getView().findViewById(R.id.card_viewp8);
        cv8=(CardView)getView().findViewById(R.id.card_viewp9);
        cv9=(CardView)getView().findViewById(R.id.card_viewp10);
        cv10=(CardView)getView().findViewById(R.id.card_viewp11);
        cv11=(CardView)getView().findViewById(R.id.card_viewp12);
        card=(CardView)getView().findViewById(R.id.card_view);

        cv.setVisibility(View.INVISIBLE);
        cv1.setVisibility(View.INVISIBLE);
        cv2.setVisibility(View.INVISIBLE);
        cv3.setVisibility(View.INVISIBLE);
        cv4.setVisibility(View.INVISIBLE);
        cv5.setVisibility(View.INVISIBLE);
        cv6.setVisibility(View.INVISIBLE);
        cv7.setVisibility(View.INVISIBLE);
        cv8.setVisibility(View.INVISIBLE);
        cv9.setVisibility(View.INVISIBLE);
        cv10.setVisibility(View.INVISIBLE);
        cv11.setVisibility(View.INVISIBLE);
        card.setVisibility(View.INVISIBLE);

        Zoom_avg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(),"You can ZoomIn and ZoomOut to get better view", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
            }
        });

        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());

        System.out.print("inside on start of avg graph");
         sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
         token = sharedPreferences.getString("token", null);

        // url=sharedPreferences.getString("url", null);
        Device=sharedPreferences.getString("avgDevice", null);
        //Sts= sharedPreferences.getLong("sts", 0);
      //  Gran= sharedPreferences.getInt("gran", 0);
     //   Ets= sharedPreferences.getLong("ets", 0);
       // opt=sharedPreferences.getString("option", null);
      //  System.out.println("token :" + token + " url" + url + " device " + Device + "opt"+opt);
          System.out.print("\n device name in avg ggraph"+Device);
      //  new HttpAsyncTask().execute();

        try {
            if (Device==null) {

                Snackbar snackbar;
                snackbar = Snackbar.make(getView(), "Kindly enter all Details", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
            }  else  if (isConnected()) {

                String status=sharedPreferences.getString("tablegraphavg", null);

                if(status==null){

                    MapFragment main=new MapFragment();
                    android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, main);
                    ft.commit();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Please check internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();// pdialog.dismiss();
                }else if(status.equalsIgnoreCase("[]")){

                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255,0,150,136)); //Displaying the snackbar using the show method()
                    snackbar.show(); pdialog.dismiss();
                }
                else{

                    device.setText("Device :" + Device);
                    card.setVisibility(View.VISIBLE);
                    getvaluesfromJsonObject(status);
                    System.out.print("\nSESSION SET FOR GRAPHR CHANGE");
                    SessionManager sessionManagerp = new SessionManager(getActivity());
                    sessionManagerp.addFlage(1);
                    cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                    cv1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                    cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));

                }
            }

            else
            {
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(),"Internet is not available", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            System.out.print("inside on start of avg grahg  ");

           // System.out.println("K(KIIIIIinsise device");
        }catch (NullPointerException m){
            m.printStackTrace();
        }

    }

   /* public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " $"; // e.g. append a dollar-sign
        }
    }*/

    //*********************************************************************************

    public boolean check(String user,String pwd){
        if(user.equalsIgnoreCase("")||pwd.equalsIgnoreCase("")){
            return false;
        }
        else return true;
    }





    //**************************** making graph *****************************
   /* void makeGraph(ArrayList<BarEntry> entries, ArrayList<String> labels,int i,String param,String alarm){
        TextView limit1=new TextView(getActivity());
        limit1.setText("---");
        limit1.setTextColor( Color.RED);
        BarDataSet dataset = new BarDataSet(entries, "# "+param+" and \n "+alarm+" # threshold value ");*/

    void makeGraph(ArrayList<BarEntry> entries, ArrayList<String> labels,int i,String param,String alarm,String unit){
       /* ImageView limit1=new ImageView(getActivity());
        limit1.setImageDrawable(R.drawable.limit);*/
        BarDataSet dataset = new BarDataSet(entries, " "+param+" ("+unit+")  , Threshold value: "+alarm);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.argb(255, 124, 204, 241));
        Zoom_avg.setVisibility(View.VISIBLE);
        dataset.setColors(colors);
        BarData data = new BarData(labels, dataset);

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mf = new DecimalFormat("###,###,##0.00");
                return mf.format(value);
            }
        });

        System.out.println(" check geeta :)"+dataset);
        if(i==0){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m1.getLayoutParams();
            params.height = 550;
            cv.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m1.addView(mChart);}
        else  if(i==1){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m2.getLayoutParams();
            params.height = 550;
            cv1.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m2.addView(mChart);
        }
        else  if(i==2){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m3.getLayoutParams();
            params.height = 550;
            cv2.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m3.addView(mChart);
        }
        else  if(i==3){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m4.getLayoutParams();
            params.height =550;
            cv3.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m4.addView(mChart);
        }
        else  if(i==4){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m5.getLayoutParams();
            params.height = 550;
            cv4.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m5.addView(mChart);
        }
        else  if(i==5){
            mChart = new BarChart(getActivity());
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m6.getLayoutParams();
            params.height = 550;
            cv5.setVisibility(View.VISIBLE);
            m6.addView(mChart);
        }else  if(i==6){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m7.getLayoutParams();
            params.height = 550;

            cv6.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m7.addView(mChart);
        }else  if(i==7){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m8.getLayoutParams();
            params.height = 550;
            cv7.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m8.addView(mChart);
        }else  if(i==8){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m9.getLayoutParams();
            params.height =550;
            cv8.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m9.addView(mChart);
        }else  if(i==9){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m10.getLayoutParams();
            params.height =550;
            cv9.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m10.addView(mChart);
        }
        else  if(i==10){
            cv10.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m11.getLayoutParams();
            params.height =550;
            mChart = new BarChart(getActivity());
            m11.addView(mChart);
        }
        else  if(i==11){
            cv11.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m12.getLayoutParams();
            params.height =550;
            mChart = new BarChart(getActivity());
            m12.addView(mChart);
        }
        mChart.setData(data);
        mChart.animateY(5000);

        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);


        //---------------------line thresold----------------------------


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        YAxis yl = mChart.getAxisLeft();
        //   yl.setAxisMaxValue(Float.parseFloat(alarm) + 5);
        yl.setTextColor(Color.WHITE);
        yl.setTextSize(8f);
        yl.setAxisMinValue(0);
        yl.setDrawGridLines(false);
        yl.setDrawAxisLine(true);
        yl.setStartAtZero(true);
        // yl.setAxisMaxValue(Float.parseFloat(alarm) );

        yl.setDrawLabels(true); // no axis labels
           /* yl.setDrawAxisLine(false); // no axis line
           yl.setDrawGridLines(false); */// no grid lines
//            yl.setDrawZeroLine(true); // draw a zero line
        mChart.getAxisRight().setEnabled(true); // no right axis


        LimitLine ll = new LimitLine(Float.parseFloat(alarm), "");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setTextColor(Color.BLUE);
        ll.setTextSize(10f);
        yl.addLimitLine(ll);
        // **************** end thresold line ***********************************


    }



    void getvaluesfromJsonObject(String status){
        //  JSONArray jsonObject = null;

        try
        {

            JSONObject jsonObject=new JSONObject(status);

            if(jsonObject.length()==0){
                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
                //Displaying the snackbar using the show method()
                snackbar.show();
             //   m1.addView(null);
                m1.removeAllViews();
                m2.removeAllViews();;
                m3.removeAllViews();
                m4.removeAllViews();
                m5.removeAllViews();
                m6.removeAllViews();
                m7.removeAllViews();
                m8.removeAllViews();
                m9.removeAllViews();
                m10.removeAllViews();
                m11.removeAllViews();
                m12.removeAllViews();
            }

                System.out.print("\n jsonobject  " + jsonObject + "\n object   \n length " + jsonObject.length() + "\n");

                JSONArray value = (JSONArray) jsonObject.get("data");
                JSONArray param = (JSONArray) jsonObject.get("param");
                JSONArray alarmArray = (JSONArray) jsonObject.get("alm2");
                JSONArray unitArray=(JSONArray) jsonObject.get("units");

            if(value.length()==0){
                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255,149,241,232));
                //Displaying the snackbar using the show method()
                snackbar.show();
                //   m1.addView(null);
                m1.removeAllViews();
                m2.removeAllViews();;
                m3.removeAllViews();
                m4.removeAllViews();
                m5.removeAllViews();
                m6.removeAllViews();
                m7.removeAllViews();
                m8.removeAllViews();
                m9.removeAllViews();
                m10.removeAllViews();
                m11.removeAllViews();
                m12.removeAllViews();
            }

                for(int p=0;p<param.length();p++) {
                    labels = new ArrayList<String>();
                    entries = new ArrayList<>();
                    System.out.println(param.get(p));
                    for (int k = 0; k < value.length(); k++) {
                        JSONObject valueobject = value.getJSONObject(k);
                        String param1 = valueobject.get(param.get(p).toString().toUpperCase()).toString();
                        Long ts1 = (Long)valueobject.get("sts");
                        String ts = "";
                        String timestamp=new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(ts1));
                        StringTokenizer st = new StringTokenizer(timestamp," ");
                        while (st.hasMoreTokens()) {
                            ts=st.nextToken();
                        }
                        entries.add(new BarEntry(Float.parseFloat(param1),k));
                      //  System.out.println(" value of param  " + Float.parseFloat(param1));
                        labels.add(ts);
                     //  System.out.println("param " + param1 + "   ts " + ts + "  k  " + k + "\n");
                        System.out.print("\n parameter value in string" + param + "\n parameter value in float" + Float.parseFloat(param1));
                    }
                   // System.out.println("inside make graph geeta :)");

                    entries.add(new BarEntry(Float.parseFloat(alarmArray.get(p).toString()), value.length() + 1));
                    labels.add("");
                    makeGraph(entries,labels,p,param.get(p).toString(),alarmArray.get(p).toString(),unitArray.get(p).toString());
                  //  System.out.println("outside make graph geeta :)");
                }

                // }

            } catch (JSONException e) {

            Snackbar snackbar;
            snackbar = Snackbar.make(getView(),"Data is not available", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
            snackbar.show();
            e.printStackTrace();
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),"Out of memory", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255,0,150,136));
            //Displaying the snackbar using the show method()
            snackbar.show();
        }
        }


}
