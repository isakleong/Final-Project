package com.example.android_skripsi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailHistory extends AppCompatActivity {

    TextView contentNamaBiroUnit, contentTeksAspirasi, contentTglAspirasi, contentDetailFotoAspirasi;
    RecyclerView rvContentFotoAspirasi, rvContentStatusAspirasi, rvContentFotoBiroUnitHist;
    ImageView contentPhotoAvatarBiroUnit;

    String idAspirasiEdited;

    CardView cardView;

    Bundle bundle;

    private Integer anonim = -1;

    public static String idResolusiAspirasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        ImageView imgPrevious = (ImageView) findViewById(R.id.imagePrevious);
        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHistory.this, MainActivity.class);
                startActivity(intent);
            }
        });

        contentNamaBiroUnit = (TextView) findViewById(R.id.contentNamaBiroUnit);
        contentTeksAspirasi = (TextView) findViewById(R.id.contentTeksAspirasi);
        contentTglAspirasi = (TextView) findViewById(R.id.contentTglAspirasi);
        rvContentFotoAspirasi = (RecyclerView) findViewById(R.id.rvContentFotoAspirasi);
        rvContentStatusAspirasi = (RecyclerView) findViewById(R.id.rvContentStatus);
        rvContentFotoBiroUnitHist = (RecyclerView) findViewById(R.id.rvContentFotoBiroUnitHist);
        contentDetailFotoAspirasi = (TextView) findViewById(R.id.contentDetailFotoAspirasi);
//        contentPhotoAvatarBiroUnit = (ImageView) findViewById(R.id.contentPhotoAvatarBiroUnit);

        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(this, 1);
        rvContentFotoAspirasi.setLayoutManager(mLinearLayoutManager);

        GridLayoutManager mLinearLayoutManager1 = new GridLayoutManager(this, 1);
        rvContentStatusAspirasi.setLayoutManager(mLinearLayoutManager1);

        GridLayoutManager mLinearLayoutManager2 = new GridLayoutManager(this, 1);
        rvContentFotoBiroUnitHist.setLayoutManager(mLinearLayoutManager2);


        //get data from history item
        bundle = getIntent().getExtras();

        if(bundle.getString("contentNamaBiroUnit")==null){
            contentNamaBiroUnit.setText("To Be Decided");
//            contentPhotoAvatarBiroUnit.setImageResource(R.drawable.question);
        }
        else{
            contentNamaBiroUnit.setText(bundle.getString("contentNamaBiroUnit"));
//            Bitmap bmp = BitmapFactory.decodeByteArray(bundle.getByteArray("contentFotoBiroUnit"), 0,
//                    bundle.getByteArray("contentFotoBiroUnit").length);
//            contentPhotoAvatarBiroUnit.setImageBitmap(bmp);
        }
        idAspirasiEdited = bundle.getInt("idAspirasi")+"";
        contentTeksAspirasi.setText(bundle.getString("contentTeksAspirasi"));
        contentTglAspirasi.setText("Tanggal buat: "+bundle.getString("contentTglAspirasi"));

        //get foto aspirasi
        TaskServer taskServer = new TaskServer();
        taskServer.setCallMode("getFotoAspirasi");
        taskServer.setActivityCaller(DetailHistory.this);
        taskServer.setIdAspirasi(bundle.getInt("idAspirasi")+"");
        taskServer.callApi();

        //get foto biro unit det hist
        TaskServer taskServer_ = new TaskServer();
        taskServer_.setCallMode("getFotoBiroUnit");
        taskServer_.setActivityCaller(DetailHistory.this);
        taskServer_.setIdBiroUnit(bundle.getString("idUserBiroUnit"));
        taskServer_.setModeFotoBiroUnit("history");
        taskServer_.callApi();

        //get status name -- content status
        TaskServer taskServer1 = new TaskServer();
        taskServer1.setCallMode("getStatusTracking");
        taskServer1.setActivityCaller(DetailHistory.this);
        taskServer1.setIdAspirasi(bundle.getInt("idAspirasi")+"");
        taskServer1.callApi();

        //checking user class

        //user civitas login
        if(SignIn.signInMode==0){
            CardView cardViewDataFeedbackResolusi = (CardView) findViewById(R.id.cardViewDataFeedbackResolusi);
            cardViewDataFeedbackResolusi.setVisibility(View.GONE);

            //checking status aspirasi -- set card is invisible or not
            //set card edit/delete aspriasi visible only for data not processed
            if(bundle.getInt("idStatusAspirasi")==1){
                cardView = (CardView) findViewById(R.id.cardViewEditAspirasi);
                cardView.setVisibility(View.VISIBLE);

                //edit aspirasi
                TextView editAspirasi = (TextView) findViewById(R.id.txtEditAspirasi);
                editAspirasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundleEdit = new Bundle();
                        bundleEdit.putString("idAspirasi", idAspirasiEdited);
                        bundleEdit.putString("teksAspirasiEdit", contentTeksAspirasi.getText()+"");
                        bundleEdit.putString("biroUnitEdit", contentNamaBiroUnit.getText()+"");

                        //true anonym
                        if(bundle.getInt("anonym")==0){
                            bundleEdit.putInt("anonimEdit", 0);
                        }
                        else{
                            bundleEdit.putInt("anonimEdit", 1);
                        }

                        Intent intent = new Intent(DetailHistory.this, EditAspirasi.class);
                        intent.putExtras(bundleEdit);
                        startActivity(intent);
                    }
                });

                //delete aspirasi
                TextView deleteAspirasi = (TextView) findViewById(R.id.txtDeleteAspirasi);
                deleteAspirasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskServer taskServer2 = new TaskServer();
                        taskServer2.setCallMode("deleteAspirasi");
                        taskServer2.setActivityCaller(DetailHistory.this);
                        taskServer2.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                        taskServer2.callApi();
                    }
                });
            }

            //set card (feedback/close) aspriasi visible only for data process is completed
            else if(bundle.getInt("idStatusAspirasi")==5){
                cardView = (CardView) findViewById(R.id.cardViewCompleted);
                cardView.setVisibility(View.VISIBLE);

                //get data resolusi
                final TaskServer taskServer2 = new TaskServer();
                taskServer2.setCallMode("getDetailResolusi");
                taskServer2.setActivityCaller(DetailHistory.this);
                taskServer2.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                taskServer2.callApi();

                //handle feedback from user civitas
                TextView rateResolusi = (TextView) findViewById(R.id.txtRateResolusi);
                rateResolusi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundleRate = new Bundle();
                        bundleRate.putString("idResolusiAspirasi", taskServer2.getIdResolusiAspirasi());

                        Intent intent = new Intent(DetailHistory.this, RateResolusi.class);
                        intent.putExtras(bundleRate);
                        startActivity(intent);
                    }
                });
            }

            else if(bundle.getInt("idStatusAspirasi")==6){
                cardView = (CardView) findViewById(R.id.cardViewCompleted);
                cardView.setVisibility(View.VISIBLE);

                //get data resolusi
                final TaskServer taskServer2 = new TaskServer();
                taskServer2.setCallMode("getDetailResolusi");
                taskServer2.setActivityCaller(DetailHistory.this);
                taskServer2.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                taskServer2.callApi();

                TextView rateResolusi = (TextView) findViewById(R.id.txtRateResolusi);
                rateResolusi.setClickable(false);

                rateResolusi.setText("Resolusi Aspirasi Sudah Ditinjau");
                rateResolusi.setTypeface(Typeface.DEFAULT_BOLD);
                rateResolusi.setTextColor(Color.parseColor("#808080"));
            }
        }

        //user biro unit login
        else{
            CardView cardViewDataBiroUnit = (CardView) findViewById(R.id.cardViewDataBiroUnit);
            cardViewDataBiroUnit.setVisibility(View.GONE);

            //checking status aspirasi -- set card is invisible or not

            //set card process aspriasi visible only for tab not processed
            if(bundle.getInt("idStatusAspirasi")==3){
                cardView = (CardView) findViewById(R.id.cardViewProcess);
                cardView.setVisibility(View.VISIBLE);

                //process aspirasi from user civitas
                TextView processAspirasi = (TextView) findViewById(R.id.txtProcessAspirasi);
                processAspirasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //get tgl status
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        //insert detail status aspirasi
                        TaskServer taskServer = new TaskServer();
                        taskServer.setCallMode("insertDetailStatusAspirasi");
                        taskServer.setActivityCaller(DetailHistory.this);
                        //4 == aspirasi sedang diproses biro atau unit terkait
                        taskServer.setIdStatusAspirasi(4);
                        taskServer.setTglStatusAspirasi(currentDateandTime);
                        taskServer.callApi();

                        TaskServer taskServer2 = new TaskServer();
                        taskServer2.setCallMode("updateStatusAspirasi");
                        taskServer2.setActivityCaller(DetailHistory.this);
                        taskServer2.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                        //update status aspirasi to 1 -- change into tab in process
                        taskServer2.setStatusAspirasi(1+"");
                        taskServer2.callApi();

                        Toast.makeText(DetailHistory.this, "Data aspirasi berhasil diproses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailHistory.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            //set card resolusi visible or not
            else if(bundle.getInt("idStatusAspirasi")==4){
                cardView = (CardView) findViewById(R.id.cardViewResolusi);
                cardView.setVisibility(View.VISIBLE);

                //edit aspirasi
                TextView resolusiAspirasi = (TextView) findViewById(R.id.txtResolusiAspirasi);
                resolusiAspirasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //insert resolusi process
                        Bundle bundleResolusi = new Bundle();
                        bundleResolusi.putInt("idAspirasi", bundle.getInt("idAspirasi"));

                        Intent intent = new Intent(DetailHistory.this, InsertResolusiAspirasi.class);
                        intent.putExtras(bundleResolusi);
                        startActivity(intent);

                        TaskServer taskServer2 = new TaskServer();
                        taskServer2.setCallMode("updateStatusAspirasi");
                        taskServer2.setActivityCaller(DetailHistory.this);
                        taskServer2.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                        //update status aspirasi to 1 -- change into tab in process
                        taskServer2.setStatusAspirasi(2+"");
                        taskServer2.callApi();
                    }
                });
            }

            //set data feedback resolusi visible or not
            else if(bundle.getInt("idStatusAspirasi")==6){
                cardView = (CardView) findViewById(R.id.cardViewDataFeedbackResolusi);
                cardView.setVisibility(View.VISIBLE);

                //get data resolusi
                final TaskServer taskServer3 = new TaskServer();
                taskServer3.setCallMode("getDetailResolusi");
                taskServer3.setActivityCaller(DetailHistory.this);
                taskServer3.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                taskServer3.callApi();

                //get data feedback resolusi
                TaskServer taskServer4 = new TaskServer();
                taskServer4.setCallMode("getFeedbackResolusiAspirasi");
                taskServer4.setActivityCaller(DetailHistory.this);
                taskServer4.setIdAspirasi(bundle.getInt("idAspirasi")+"");
                taskServer4.callApi();
            }
        }



    }

}
