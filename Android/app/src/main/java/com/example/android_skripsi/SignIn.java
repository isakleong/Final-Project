package com.example.android_skripsi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignIn extends AppCompatActivity {
    public static String emailSignIn, passwordSignIn, nama_lengkap, nrp, gender, tgl_lahir, tempat_lahir, alamat;
    public static Integer idUserCivitas, idUserBiroUnit, idUser;
    public static byte[] profilePictureByteArray;
    public static Bitmap profilePictureBitmap;

    public static String checkNotif;

    public static Integer signInMode; // 0--student, 1--biro/unit

    public static ProgressBar prg;

    public static String TokenFCM;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInMode = 0;

        final EditText inputEmail = findViewById(R.id.email_signin);
        final EditText inputPassword = findViewById(R.id.password_signin);
        final RadioGroup radioGroup = findViewById(R.id.userClass);
        prg = (ProgressBar) findViewById(R.id.progressBar);

//        bundle = getIntent().getExtras();
//        checkNotif = bundle.getString("pushnotif");
//        Log.e("INI CHECK NOTIF ", checkNotif);

        Button buttonLogin = findViewById(R.id.button_signin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputEmail.getText().toString().isEmpty()) {
                    inputEmail.setError("Email cannot empty");
                } else if (inputPassword.getText().toString().isEmpty()) {
                    inputPassword.setError("Password cannot empty");
                } else {
                  //process here
                    emailSignIn = inputEmail.getText()+"";
                    passwordSignIn = inputPassword.getText()+"";

                    //get fcm token for handling notification
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("TOKEN FAILED", "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();

                                    // Log and toast
                                    Log.d("TOKEN SUCCESS ", token);
                                    TokenFCM = token;

                                    TaskServer taskServer = new TaskServer();
                                    taskServer.setCallMode("userLogin");
                                    taskServer.setActivityCaller(SignIn.this);
                                    taskServer.setToken_user(TokenFCM);
                                    taskServer.callApi();

                                    //update user's fcm token to database
//                                    TaskServer taskServer = new TaskServer();
//                                    taskServer.setCallMode("updateToken");
//                                    taskServer.setActivityCaller(SignIn.this);
//                                    taskServer.setToken_user(TokenFCM);
//                                    taskServer.setId_user(idUser+"");
//                                    taskServer.callApi();
                                }
                            });
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which role user was login
        switch(view.getId()) {
            case R.id.radio_student:
                if (checked)
                    signInMode = 0;
                    break;
            case R.id.radio_biro_unit:
                if (checked)
                    signInMode = 1;
                    break;
        }
    }
}
