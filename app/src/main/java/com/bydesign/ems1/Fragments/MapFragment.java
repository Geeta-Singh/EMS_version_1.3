package com.bydesign.ems1.Fragments;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bydesign.ems1.AlarmService;
import com.bydesign.ems1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private GoogleMap mMap;
    private PendingIntent pendingIntent;

    private AlarmManager manager;

    public MapFragment() {
        // Required empty public constructor
    }

    private SupportMapFragment fragment;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_map, container, false);
        Intent alarmIntent = new Intent(getActivity(), AlarmService.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());


      /*  manager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
        context=getActivity();
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);*/

        if (map == null) {
            map = fragment.getMap();
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("latlong", null);
            System.out.println("\nmap data :" + token);
            try {
                if(token.isEmpty()){}
                else{
                JSONArray jsonArray = new JSONArray(token);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    String location = object.get("name").toString();
                    Double min = Double.parseDouble( object.get("lat").toString());
                    Double max = Double.parseDouble( object.get("long").toString());
                    map.addMarker(new MarkerOptions().position(new LatLng(min, max)).title(location));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.052404,72.909823), 4.3f));//19.052404","long":"72.909823
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                  //  mMap.setMyLocationEnabled(true);
                } else {
                    // Show rationale and request permission.
                }}
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (ClassCastException e){
                e.printStackTrace();
            }catch (NumberFormatException e){
                e.printStackTrace();
            }



        }
    }


  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            *///}
       // }
}