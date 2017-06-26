package com.bydesign.ems1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bydesign.ems1.Fragments.AnalyzerStatusReportFragment;
import com.bydesign.ems1.Fragments.AvgTabFragment;
import com.bydesign.ems1.Fragments.ConditionTab;
import com.bydesign.ems1.Fragments.DataExceedanceReportFragment;
import com.bydesign.ems1.Fragments.DeviceDeatailsFragment;
import com.bydesign.ems1.Fragments.FilterFragment;
import com.bydesign.ems1.Fragments.HistoricalTab;
import com.bydesign.ems1.Fragments.MapFragment;
import com.bydesign.ems1.Fragments.TabFragment;
import com.bydesign.ems1.model.Token;
import com.bydesign.ems1.services.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class navigationdrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public  static Toolbar toolbar=null;
    SharedPreferences sharedPreferences;


    NavigationView navigationView=null;
    private PendingIntent pendingIntent;

    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigationdrawer);


        Intent alarmIntent = new Intent(this, AlarmService.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //set the fragment initially

        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 60000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
      //  Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        /* notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        context=getApplicationContext();
*/
        sharedPreferences = this.getSharedPreferences("EMS", Context.MODE_PRIVATE);
        MapFragment main=new MapFragment();
        android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, main);
        ft.commit();


         toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Analyzers Across India");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
       /* View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        headerView.findViewById(R.id.navigation_header_text);*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        MapFragment main=new MapFragment();
        android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, main);
        ft.commit();
        toolbar.setTitle("Analyzers Across India");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigationdrawer, menu);
       // menu.add(0,R.menu.navigationdrawer, 0, "Add").setIcon(android.R.drawable.ic_dialog_email);
        return true;
    }

    public String logoutSession(){
        String status="";

        InputStream inputStream = null;
        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            String url=sharedPreferences.getString("url", null);
            //made POST request to the given URL
            System.out.print("\nurl in navigation "+url);
            Token newToken=new Token();
            String token= newToken.getToken();
            String URL=newToken.getUrl();
            System.out.print(" url "+URL);
            HttpPost httpPost = new HttpPost(URL+"/logout");//http://220.227.124.134:8080/smartcity/gassensor
           // sharedPreferences = this.getSharedPreferences("EMS", Context.MODE_PRIVATE);

           // String token=sharedPreferences.getString("token",null);
            System.out.print(" logout tokrn b "+token+URL+"\n"+httpPost);
            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token",token);

            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("logout json "+jsonObject2);
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

      //  return status;
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

    class HttpForLogout extends AsyncTask<String,Void ,String>{

        @Override
        protected String doInBackground(String... params) {
            return logoutSession() ;
        }

        protected void onPostExecute(String status){
            System.out.println("#@$Logout api called"+status);

        }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    public void onPause(){
        super.onPause();
    }
    public void onResume(){
        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
         automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();

      //  item.setTitle("");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(isConnected()) {
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                new HttpForLogout().execute();
                sessionManager.logoutUser();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                notificationManager.cancel(101);
                notificationManager.cancelAll();
                manager.cancel(pendingIntent);
                finish();
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_LONG).show();
            }
        }else if(id==R.id.filter){
            FilterFragment main=new FilterFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Filter");
        }

        return super.onOptionsItemSelected(item);
    }

    FloatingActionButton fabButton;
   /* @Override
    public void onDrawerSlide(View drawerView, float offset){
        fabButton.setAlpha(offset);
    }*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //)))))))))))))))))))))))))))))))))))))))))))))))))))))

        //))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            ConditionTab main=new ConditionTab();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Device/Data Condition");

        } else if (id == R.id.nav_device) {
            DeviceDeatailsFragment main=new DeviceDeatailsFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Device Details");

        } else if (id == R.id.nav_slideshow) {
            TabFragment main=new TabFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
           toolbar.setTitle("Latest Data");


        } else if (id == R.id.nav_7) {
            DataExceedanceReportFragment main = new DataExceedanceReportFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle("Data exceedance report");

        } else if (id == R.id.nav_5) {
                HistoricalTab main=new HistoricalTab();
                android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container,main);
                ft.commit();
                    toolbar.setTitle("Historical Data");


        } else if (id == R.id.nav_6) {
            AvgTabFragment main=new AvgTabFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Average Data");

        } else if (id == R.id.nav_8) {
            AnalyzerStatusReportFragment main=new AnalyzerStatusReportFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
           toolbar.setTitle("Analyzer status report ");

        }
        else if (id == R.id.nav_main) {
            MapFragment main=new MapFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
           toolbar.setTitle("Analyzers Across India");

        } /*else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download");
            intent.setDataAndType(uri, "text/csv");
            startActivity(Intent.createChooser(intent, "Open folder"));

        }*//*else if (id == R.id.nav_filter) {
            FilterFragment main=new FilterFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Filter");


        }*//*else if (id == R.id.nav_gallery) {
            AboutApp main=new AboutApp();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("About App");


        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
