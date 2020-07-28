package com.example.android_skripsi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;

import javax.net.ssl.KeyManager;

public class SplashScreen extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (preferences.contains("isUserLogin")){
            Bundle bundle = getIntent().getExtras();
            if (bundle!=null) {
                String id_aspirasi = bundle.getString("id_aspirasi");
                String status_aspirasi= bundle.getString("status_aspirasi");

                //get detail history based on notification
                TaskServer taskServer = new TaskServer();
                taskServer.setCallMode("getDetailNotification");
                taskServer.setActivityCaller(SplashScreen.this);
                taskServer.setIdAspirasi(id_aspirasi);
                taskServer.setId_user("1");
                taskServer.setStatusAspirasi(status_aspirasi);
                taskServer.callApi();

            }

            else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2500);
            }

//            try{
//                Bundle bundle = getIntent().getExtras();
//                if (bundle!=null) {
//                    if(bundle.getString("id_aspirasi")!=null){
////                        setNotificationRoute(bundle);
//
//                        String id_aspirasi = bundle.getString("id_aspirasi");
//                        String status_aspirasi= bundle.getString("status_aspirasi");
//
//                        //get detail history based on notification
//                        TaskServer taskServer = new TaskServer();
//                        taskServer.setCallMode("getDetailNotification");
//                        taskServer.setActivityCaller(SplashScreen.this);
//                        taskServer.setIdAspirasi(id_aspirasi);
//                        taskServer.setId_user("1");
//                        taskServer.setStatusAspirasi(status_aspirasi);
//                        taskServer.callApi();
//
//                    }
//                    else{
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }, 2500);
//                    }
//                }
//
//                else {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }, 2500);
//                }
//
//            } catch (Exception e){
//
//            }
        }

        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, SignIn.class);
                    startActivity(intent);
                    finish();
                }
            }, 2500);
        }
    }

    private void  setNotificationRoute(Bundle extras) {

        String id_aspirasi = extras.getString("id_aspirasi");
        String status_aspirasi= extras.getString("status_aspirasi");

        //get detail history based on notification
        TaskServer taskServer = new TaskServer();
        taskServer.setCallMode("getDetailNotification");
        taskServer.setActivityCaller(SplashScreen.this);
        taskServer.setIdAspirasi(id_aspirasi);
        taskServer.setId_user("1");
        taskServer.setStatusAspirasi(status_aspirasi);
        taskServer.callApi();

//        finish();

    }

}
