package com.example.android_skripsi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RateResolusi extends AppCompatActivity {
    Button rateEval1, rateEval2, rateEval3;
    TextView btnSubmitReviewResolusi;
    RatingBar ratingBar;
    LinearLayout linearLayout;
    EditText teksFeedback;

    Bundle bundle;

    ArrayList<String> arrRateEval = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_resolusi);

        rateEval1 = (Button) findViewById(R.id.rateEval1);
        rateEval2 = (Button) findViewById(R.id.rateEval2);
        rateEval3 = (Button) findViewById(R.id.rateEval3);

        teksFeedback = (EditText) findViewById(R.id.teksFeedback);

        //handle eval rating option display (according stars review)
        ratingBar = (RatingBar) findViewById(R.id.ratingResolusi);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                linearLayout = (LinearLayout) findViewById(R.id.layoutRateEval);
                linearLayout.setVisibility(View.VISIBLE);

                if(rating>3){
                    arrRateEval.clear();
                    rateEval1.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval1.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
                    rateEval2.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval2.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
                    rateEval3.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval3.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);

                    rateEval1.setText("Waktu respon cepat");
                    rateEval2.setText("Kejelasan resolusi aspirasi");
                    rateEval3.setText("Menjawab aspirasi");
                }
                else{
                    arrRateEval.clear();
                    rateEval1.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval1.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
                    rateEval2.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval2.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
                    rateEval3.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval3.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);

                    rateEval1.setText("Waktu respon lambat");
                    rateEval2.setText("Ketidakjelasan resolusi aspirasi");
                    rateEval3.setText("Tidak menjawab aspirasi");
                }

            }
        });

        btnSubmitReviewResolusi = (TextView) findViewById(R.id.btnSubmitReviewResolusi);
        btnSubmitReviewResolusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = getIntent().getExtras();

                String detailRating = "";
                for(int i=0; i<arrRateEval.size(); i++){
                    if(i==arrRateEval.size()-1){
                        detailRating+=arrRateEval.get(i);
                    }
                    else{
                        detailRating+=arrRateEval.get(i);
                        detailRating+=",";
                    }
                }

                TaskServer taskServer = new TaskServer();
                taskServer.setCallMode("insertFeedbackResolusi");
                taskServer.setActivityCaller(RateResolusi.this);
                taskServer.setIdResolusiAspirasi(bundle.getString("idResolusiAspirasi"));
                taskServer.setTeksFeedbackResolusi(teksFeedback.getText().toString());
                taskServer.setRatingFeedbackResolusi(ratingBar.getRating()+"");
                taskServer.setDetailRateEval(detailRating);
                taskServer.callApi();
            }
        });

        //set click event for eval rating option
        rateEval1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateEval1.getCurrentTextColor()==Color.WHITE){
                    rateEval1.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval1.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);

                    //remove from array eval rating
                    for(int i=0;i<arrRateEval.size();i++){
                        if(arrRateEval.get(i).equals(rateEval1.getText().toString())){
                            arrRateEval.remove(i);
                        }
                    }
                }
                else{
                    rateEval1.setTextColor(Color.WHITE);
                    rateEval1.setBackgroundResource(R.drawable.buttonshapelessradiusblue);

                    if(arrRateEval.size()>0){
                        //handling duplicate item
                        int counter=0;
                        for(int i=0; i<arrRateEval.size(); i++){
                            if(arrRateEval.get(i).equals(rateEval1.getText().toString())){
                                counter++;
                            }
                        }

                        if(counter==0){
                            arrRateEval.add(rateEval1.getText().toString());
                        }
                    }
                    else{
                        arrRateEval.add(rateEval1.getText().toString());
                    }
                }
//                rateEval2.setTextColor(Color.parseColor("#01AB6D"));
//                rateEval2.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
//                rateEval3.setTextColor(Color.parseColor("#01AB6D"));
//                rateEval3.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
            }
        });

        rateEval2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateEval2.getCurrentTextColor()==Color.WHITE){
                    rateEval2.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval2.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);

                    for(int i=0;i<arrRateEval.size();i++){
                        if(arrRateEval.get(i).equals(rateEval2.getText().toString())){
                            arrRateEval.remove(i);
                        }
                    }
                }
                else{
                    rateEval2.setTextColor(Color.WHITE);
                    rateEval2.setBackgroundResource(R.drawable.buttonshapelessradiusblue);

                    if(arrRateEval.size()>0){
                        //handling duplicate item
                        int counter=0;
                        for(int i=0; i<arrRateEval.size(); i++){
                            if(arrRateEval.get(i).equals(rateEval2.getText().toString())){
                                counter++;
                            }
                        }

                        if(counter==0){
                            arrRateEval.add(rateEval2.getText().toString());
                        }
                    }
                    else{
                        arrRateEval.add(rateEval2.getText().toString());
                    }
                }

//                rateEval1.setTextColor(Color.parseColor("#01AB6D"));
//                rateEval1.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
//                rateEval3.setTextColor(Color.parseColor("#01AB6D"));
//                rateEval3.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
            }
        });

        rateEval3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateEval3.getCurrentTextColor()==Color.WHITE){
                    rateEval3.setTextColor(Color.parseColor("#01AB6D"));
                    rateEval3.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);

                    for(int i=0;i<arrRateEval.size();i++){
                        if(arrRateEval.get(i).equals(rateEval3.getText().toString())){
                            arrRateEval.remove(i);
                        }
                    }
                }
                else{
                    rateEval3.setTextColor(Color.WHITE);
                    rateEval3.setBackgroundResource(R.drawable.buttonshapelessradiusblue);

                    if(arrRateEval.size()>0){
                        //handling duplicate item
                        int counter=0;
                        for(int i=0; i<arrRateEval.size(); i++){
                            if(arrRateEval.get(i).equals(rateEval3.getText().toString())){
                                counter++;
                            }
                        }

                        if(counter==0){
                            arrRateEval.add(rateEval3.getText().toString());
                        }
                    }
                    else {
                        arrRateEval.add(rateEval3.getText().toString());
                    }

                }

//                rateEval2.setTextColor(Color.parseColor("#01AB6D"));
//                rateEval2.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
//                rateEval1.setTextColor(Color.parseColor("#01AB6D"));
//                rateEval1.setBackgroundResource(R.drawable.buttonshaperadiuslightblue);
            }
        });

    }
}
