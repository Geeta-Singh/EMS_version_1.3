package com.bydesign.ems1.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bydesign.ems1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class share extends Fragment {


    public share() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        openFolder();
        return view;
    }

     /*   else if(status.equalsIgnoreCase("Invalid")){
            pdialog.dismiss();
            Snackbar snackbar;
            snackbar = Snackbar.make(getView(),"Invalid USer", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 0, 150, 136));
            snackbar.show();
            SessionManager sessionManager = new SessionManager(getActivity());
            new HttpForLogout().execute();
            sessionManager.logoutUser();
        }
        class HttpForLogout extends AsyncTask<String,Void ,String> {

            @Override
            protected String doInBackground(String... params) {
                return logoutSession() ;
            }

            protected void onPostExecute(String status){
                System.out.println("#@$Logout api called"+status);

            }
        }*/
   /* public String logoutSession(){
        String status="";

        InputStream inputStream = null;
        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            String url=sharedPreferences.getString("url", null);
            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url+"/logout");

            String token=sharedPreferences.getString("token",null);
            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token",token);

            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("json "+jsonObject2);
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

        return view;
    }*/
    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download");
        intent.setDataAndType(uri, "text/csv");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }
}


