package com.bydesign.ems1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.bydesign.ems1.services.SharedVariables;

public class SplashActivity extends Activity {

    int progress = 0;
    int progressStatus = 0;
    ProgressBar pb;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pb=(ProgressBar)findViewById(R.id.progressbar);


        new Thread(new Runnable(){

            @Override
            public void run() {
               /* while(progress<10){
                    progressStatus=doSomeWork();
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }

                    });

                }*/
                if(SharedVariables.getUserName(SplashActivity.this).length() == 0)
                {
                    Intent i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finish(); // call Login Activity
                }
                else
                {
                    Intent i=new Intent(getApplicationContext(),navigationdrawer.class);
                    startActivity(i);
                    finish();// Stay at the current activity.
                }


            }

            private int doSomeWork() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ++progress;
            }

        }).start();
        /*Intent i=new Intent(getApplicationContext(),Login.class);
        startActivity(i);
        finish();*/



       /* pb=(ProgressBar)findViewById(R.id.progressbar);


        new Thread(new Runnable(){

            @Override
            public void run() {
                while(progress<10){
                    progressStatus=doSomeWork();
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }

                    });

                }
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }

            private int doSomeWork() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ++progress;
            }

        }).start();
    }*/
    }


}
