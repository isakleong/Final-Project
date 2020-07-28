package com.example.android_skripsi;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.android_skripsi.SignIn.idUserCivitas;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
            ,MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101
            ,MY_PERMISSIONS_REQUEST_CAMERA = 102;

    public static String username;
    public static Integer pagination;
    public static TextView navigationToolbar;

    public static ProgressBar progressBarShowHistory;
//    public static View MainprogressOverlay;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationToolbar = (TextView) findViewById(R.id.navigation_toolbar);

//        MainprogressOverlay = (View) findViewById(R.id.progress_overlay);
//        progressBarShowHistory = (ProgressBar) findViewById(R.id.spin_kit_history);
//        Sprite doubleBounce = new ThreeBounce();
//        progressBarShowHistory.setIndeterminateDrawable(doubleBounce);

        if(getIntent().hasExtra("username")) {
            username = getIntent().getExtras().getString("username");
            Log.e("Username",username);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new HomePageViewPager(getSupportFragmentManager(), MainActivity.this));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#01AB6D"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    navigationToolbar.setText("Home");
                }
                else if(tab.getPosition() == 1){
                    navigationToolbar.setText("History");

                    HistoryFragment.rvHistory.setAdapter(null);
                    HistoryFragment.progressBarShowHistory.setVisibility(View.VISIBLE);
                    HistoryFragment.txtNotProcess.setCompoundDrawables(null,null,null,HistoryFragment.img);
                    HistoryFragment.txtInProcess.setCompoundDrawables(null,null,null, null);
                    HistoryFragment.txtCompleted.setCompoundDrawables(null,null,null,null);

                    TaskServer taskServer = new TaskServer();
                    taskServer.setActivityCaller(MainActivity.this);
                    taskServer.setCallMode("getNotProcessHistory");
                    taskServer.setId_user(1+"");
                    taskServer.setStatusAspirasi(0+"");
                    taskServer.callApi();

                }
                else if(tab.getPosition() == 2){
                    navigationToolbar.setText("Trending");

                    TaskServer taskServer = new TaskServer();
                    taskServer.setActivityCaller(MainActivity.this);
                    taskServer.setCallMode("getTrending");
                    taskServer.callApi();


                }
                else if(tab.getPosition() == 3){
                    navigationToolbar.setText("Biro / Unit");

                    TaskServer taskServer = new TaskServer();
                    taskServer.setCallMode("getBiroUnit");
                    taskServer.setActivityCaller(MainActivity.this);
                    taskServer.callApi();
                }
                else{
                    navigationToolbar.setText("Profile");
                }
                tab.getIcon().setColorFilter(Color.parseColor("#01AB6D"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#374957"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int[] image = {
                R.drawable.homepage,
                R.drawable.historypage,
                R.drawable.trendingpage,
                R.drawable.birounitpage,
                R.drawable.profilepage
        };

        for (int i=0;i<image.length;i++)
        {
            tabLayout.getTabAt(i).setIcon(image[i]);
        }


        KeyboardVisibilityEvent.setEventListener(
                MainActivity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            tabLayout.setVisibility(View.GONE);
                        } else {
                            tabLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });

        permissionReadExternalStorage();
        //deleteFile();
        //createFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        Boolean getLogin = preferences.getBoolean("isUserLogin", true);
        SignIn.idUser = preferences.getInt("idUser", 0);
        SignIn.nama_lengkap = preferences.getString("nama_lengkap","");
        SignIn.nrp = preferences.getString("nrp", "");
        SignIn.tempat_lahir = preferences.getString("tempat_lahir", "");
        SignIn.tgl_lahir = preferences.getString("tgl_lahir", "");
        SignIn.alamat = preferences.getString("alamat", "");
        SignIn.signInMode = preferences.getInt("sign_in_mode", 0);

        //get profile picture
        TaskServer taskServer = new TaskServer();
        taskServer.setCallMode("getProfilePicture");
        taskServer.setActivityCaller(MainActivity.this);
        taskServer.setId_user(SignIn.idUser+"");
        taskServer.callApi();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", true);
        editor.putInt("idUser", SignIn.idUser);
        editor.putString("nama_lengkap", SignIn.nama_lengkap);
        editor.putString("nrp", SignIn.nrp);
        editor.putString("tempat_lahir", SignIn.tempat_lahir);
        editor.putString("tgl_lahir", SignIn.tgl_lahir);
        editor.putString("alamat", SignIn.alamat);
        editor.putInt("sign_in_mode", SignIn.signInMode);
        editor.commit();

        //get profile picture
        TaskServer taskServer = new TaskServer();
        taskServer.setCallMode("getProfilePicture");
        taskServer.setActivityCaller(MainActivity.this);
        taskServer.setId_user(SignIn.idUser+"");
        taskServer.callApi();
    }

//    public void permissionReadContacts(){
//        if (ContextCompat.checkSelfPermission(getBaseContext(),
//                Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.READ_CONTACTS)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//            }
//        } else {
//            // Permission has already been granted
//        }
//    }
    public void permissionReadExternalStorage(){
        if (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            // Permission has already been granted
        }
    }
    public void permissionCamera(){
        if (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            // Permission has already been granted
        }
    }

//    public void createFile(){
//        //write to internal storage
//        FileOutputStream fos = null;
//        try {
//            //delete
//            File file = new File(getFilesDir() + "/" + "tempcontact.txt");
//            if(!file.exists()){
//                Log.e("Error","Buat File");
//                file.createNewFile();
//            }
//            else{ Log.e("Error","Tidak Buat File"); }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    public void deleteFile(){
//        //write to internal storage
//        FileOutputStream fos = null;
//        try {
//            //delete
//            File file = new File(getFilesDir() + "/" + "tempcontact.txt");
//            if(file.exists()){
//                file.delete();
//            }
//            file.createNewFile();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permissionReadExternalStorage();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionReadExternalStorage();
                }
                return;
            }
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    permissionCamera();
//
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    permissionCamera();
//                }
//                return;
//            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permissionCamera();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionCamera();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //delete token fcm
//            FCMHandler fcmHandler = new FCMHandler();
//            fcmHandler.disableFCM();

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
