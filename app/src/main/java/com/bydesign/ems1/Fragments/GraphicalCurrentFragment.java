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
import android.widget.Spinner;
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
public class GraphicalCurrentFragment extends Fragment {

    LinearLayout contentnView;
    TableLayout tl, tg,th;
    TableRow tr;
    private RelativeLayout m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12;
    LinearLayout l1;
    ArrayList<BarEntry> entries;
    ArrayList<String> labels;
    private BarChart mChart;
    Spinner spinner;
    String Selecteddevice;
    LinearLayout linearLayout;
    private ImageButton Zoom_current;
    ProgressDialog pdialog=null;
    public GraphicalCurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_graphical_current, container, false);

        return v;
    }
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity(). getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    String Devices;

    LinearLayout ll;
  //  ProgressDialog pdialog=null;
    CardView cv,cv1,cv2,cv3,cv4,cv5,cv6,cv7,cv8,cv9,cv10,cv11,card;

    public void onStart() {
        super.onStart();
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

        card=(CardView)getView().findViewById(R.id.card_view1);
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
       /* cv12.setVisibility(View.INVISIBLE);
        cv.setVisibility(View.INVISIBLE);*/

        Zoom_current= (ImageButton) getView().findViewById(R.id.zoom_current);
        Zoom_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(), "You can ZoomIn and ZoomOut to get better view", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                snackbar.show();
            }
        });
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        Devices = sharedPreferences.getString("devices",null);
        System.out.print("DEvice in garaph"+Devices);
        TextView text=(TextView)getView().findViewById(R.id.textdevice);

        text.setText("Selected Device : " + Devices);

        card.setVisibility(View.VISIBLE);


       //  pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
       // pdialog.setCancelable(true);
       // new HttpAsyncTask().execute();
        String status=sharedPreferences.getString("tablegraphcurrent", null);
        System.out.println("hi graph device" + Devices+ " Status "+status);

        if(Devices==null || status==null){

        }
        else {
            if (status.equalsIgnoreCase("[]")) {
                MapFragment main = new MapFragment();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container, main);
                ft.commit();
                Snackbar snackbar;
                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(), "Data is not available", Snackbar.LENGTH_LONG);
                //Displaying the snackbar using the show method()
                snackbar.show();
                //  pdialog.dismiss();
            } else {
                pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
                getvaluesfromJsonObject(status);
                pdialog.dismiss();

                //System.out.println("========    "+status);
                System.out.print("\nSESSION SET FOR GRAPHR CHANGE");
                SessionManager sessionManagerp = new SessionManager(getActivity());
                sessionManagerp.addFlage(1);
                //  pdialog.dismiss();
                cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                cv1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));

            }

        }




        //*********************************************************************************

    }




    //**************************** making graph *****************************
    void makeGraph(ArrayList<BarEntry> entries, ArrayList<String> labels,int i,String param,String alarm,int size,String unit){
       /* ImageView limit1=new ImageView(getActivity());
        limit1.setImageDrawable(R.drawable.limit);*/
        BarDataSet dataset = new BarDataSet(entries, " "+param+" ("+unit+")  , Threshold value: "+alarm);
        ArrayList<Integer> colors = new ArrayList<Integer>();


            colors.add(Color.argb(255, 124, 204, 241));

               // colors.add(Color.argb(255, 0, 0, 0));
        dataset.setColors(colors);
        BarData data = new BarData(labels, dataset);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mf = new DecimalFormat("###,###,##0.00");
                return mf.format(value);
            }
        });
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());

        if(i==0){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m1.getLayoutParams();
            params.height = 600;
            cv.setVisibility(View.VISIBLE);
            mChart = new BarChart(getActivity());
            m1.addView(mChart);}
        else  if(i==1){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m2.getLayoutParams();
            params.height = 600;
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
        mChart.animate();//.getHighestVisibleXIndex();
        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);


        //---------------------line thresold----------------------------

       /* XAxis xAxis = mChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(false);*/
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
        JSONArray jsonObject = null;
        JSONArray paramArray = null;JSONArray alarmArray=null;
        JSONArray unitArray;
        try
        {
            jsonObject = new JSONArray(status);

            if(jsonObject.length() ==0){
                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),"Data is not available.", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
                //Displaying the snackbar using the show method()
                snackbar.show();
                //   Toast.makeText(getActivity(),"Data is not available",Toast.LENGTH_LONG).show();
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
            for(int i=0;i<jsonObject.length();i++) {

                JSONObject object = jsonObject.getJSONObject(i);

                System.out.print("\n jsonobject  " + jsonObject + "\n object   " + object + "\n length " + jsonObject.length()+"\n");

                JSONArray value = (JSONArray) object.get("values");

                if(value.length()==0){
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),"Data is not available.", Snackbar.LENGTH_LONG);
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
                else{
                    paramArray = (JSONArray) object.get("param");
                    alarmArray = (JSONArray) object.get("alarm");
                    unitArray= (JSONArray) object.get("units");
                    for(int p=0;p<paramArray.length();p++) {
                        labels = new ArrayList<String>();
                        entries = new ArrayList<>();
                    /* entries.add(new BarEntry(100,0));
                     labels.add("00:00:00");*/
                        System.out.println(paramArray.get(p));
                        for (int k = 0; k < value.length(); k++) {
                            JSONObject valueobject = value.getJSONObject(k);
                            String param = valueobject.get(paramArray.get(p).toString().toUpperCase()).toString();
                            Long ts1 = (Long)valueobject.get("ts");
                            String ts = "";
                            String timestamp=new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(ts1));


                            StringTokenizer st = new StringTokenizer(timestamp," ");
                            while (st.hasMoreTokens()) {
                                ts=st.nextToken();
                                //    System.out.print("\ndate time... : "+dt[i]);

                            }
                            // entries.add(new BarEntry(4f, 0));
                            entries.add(new BarEntry(Float.parseFloat(param),k));
                            System.out.print("\n parameter value in string" + param + "\n parameter value in float" + Float.parseFloat(param));
                          //  System.out.println(" value of param  " + Float.parseFloat(param));
                            labels.add(ts);
                          //  System.out.println("param " + param + "   ts " + ts + "  k  " + k + "\n");

                        }
                        entries.add(new BarEntry(Float.parseFloat(alarmArray.get(p).toString()),value.length()+1 ));
                        labels.add("");
                        makeGraph(entries,labels,p,paramArray.get(p).toString(),alarmArray.get(p).toString(),value.length(),unitArray.get(p).toString());
                        System.out.println();
                    }

                }
            }

        } catch (JSONException e) {
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
