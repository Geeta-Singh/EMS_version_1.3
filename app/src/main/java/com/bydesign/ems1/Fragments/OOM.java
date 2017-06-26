package com.bydesign.ems1.Fragments;

import android.app.Activity;

import java.io.IOException;

public class OOM extends Activity {
    public static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            if(ex.getClass().equals(OutOfMemoryError.class))
            {
                try {
                    android.os.Debug.dumpHprofData("/sdcard/dump.hprof");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
        }
    }}