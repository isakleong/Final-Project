package com.example.android_skripsi;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.Adapter.AdapterFotoAspirasi;
import com.example.android_skripsi.Adapter.AdapterFotoResolusi;
import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.Provider.ProviderFotoResolusi;
import com.example.android_skripsi.TaskServer.TaskServer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class InsertResolusiAspirasi extends AppCompatActivity {
    public static ImageView btnUploadImageResolusi;

    private final int PICK_IMAGE_MULTIPLE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    ArrayList<ProviderFotoResolusi> arrProvImgRemarkResolusi = new ArrayList<>();
    AdapterFotoResolusi adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
    public static ArrayList<String> hasilImageRemarksResolusi = new ArrayList<>();
    public static ArrayList<String> pathImageResolusi = new ArrayList<>();
    ArrayList<Bitmap> bitmaps = new ArrayList<>();

    ScrollView scv;
    RecyclerView rvFotoResolusiAspirasi;
    EditText teksResolusiAspirasi;
    TextView btnSubmitResolusiAspirasi;
    String tglResolusiAspirasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_resolusi_aspirasi);

        scv = (ScrollView) findViewById(R.id.nsv);
        scv.pageScroll(View.FOCUS_UP);

        ImageView imgPrevious = (ImageView) findViewById(R.id.imagePrevious);
        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //foto resolusi aspirasi
        rvFotoResolusiAspirasi = (RecyclerView) findViewById(R.id.rvImageResolusiAspirasi);
        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvFotoResolusiAspirasi.setLayoutManager(mLinearLayoutManager);

        btnUploadImageResolusi = (ImageView) findViewById(R.id.btnUploadImageResolusi);
        btnUploadImageResolusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(InsertResolusiAspirasi.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(InsertResolusiAspirasi.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    AdapterFotoResolusi imgAdapt = (AdapterFotoResolusi) rvFotoResolusiAspirasi.getAdapter();
                    if(imgAdapt!=null) {
                        arrProvImgRemarkResolusi = imgAdapt.getAllData();
                    }
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                }
            }
        });

        btnSubmitResolusiAspirasi = (TextView) findViewById(R.id.btnSubmitResolusiAspirasi);
        teksResolusiAspirasi = (EditText) findViewById(R.id.teksResolusi);

        btnSubmitResolusiAspirasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data foto resolusi aspirasi
                hasilImageRemarksResolusi.clear();
                rvFotoResolusiAspirasi.setItemViewCacheSize(arrProvImgRemarkResolusi.size());
//                Toast.makeText(InputAspirasi.this, "INI "+adpImg.getItemCount(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < adpImg.getItemCount(); i++) {
                    View view = rvFotoResolusiAspirasi.getChildAt(i);
                    EditText remarks  = (EditText) view.findViewById(R.id.txtRemarks);
                    ImageView imgView = (ImageView) view.findViewById(R.id.imgView);
                    String txtremarks;
                    if(remarks.getText().toString().equals("")){
                        txtremarks = " ";
                    }
                    else {
                        txtremarks = remarks.getText().toString();
                    }
                    //foto aspirasi converted to string base 64
                    imgView.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
                    Bitmap bitmap           = drawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    hasilImageRemarksResolusi.add(encodedString + "---" + txtremarks);
                }

                Bundle bundle = getIntent().getExtras();
                Integer idAspirasi = bundle.getInt("idAspirasi");

                //call api
                TaskServer taskServer = new TaskServer();
                taskServer.setCallMode("insertResolusiAspirasi");
                taskServer.setContext(getApplicationContext());
                taskServer.setActivityCaller(InsertResolusiAspirasi.this);
                taskServer.setTeksResolusiAspirasi(teksResolusiAspirasi.getText()+"");
                taskServer.setIdAspirasi(idAspirasi+"");
                taskServer.callApi();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(arrProvImgRemarkResolusi.size() >= 3){btnUploadImageResolusi.setEnabled(false);}
        else {btnUploadImageResolusi.setEnabled(true);}
    }

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
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK) {

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                int max = 5 - arrProvImgRemarkResolusi.size();
                if (clipData.getItemCount() > max) {
                    Toast.makeText(getApplicationContext(), "Please Retry , Maximum of Selected Picture is 5", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        //image path
                        String path = GlobalActivity.getPath(getApplicationContext(), imageUri);
                        try {
                            InputStream is = getApplicationContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap  = BitmapFactory.decodeStream(is);
                            pathImageResolusi.add(path);
                            Log.e("Error","Path" + path);
                            if(bitmap.getWidth() <= 1000 && bitmap.getHeight() <= 1000){
                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoResolusi provJob = new ProviderFotoResolusi();
                                provJob.setImageBitmapResolusi(resized);
                                provJob.setImagePathResolusi(path);
                                arrProvImgRemarkResolusi.add(provJob);
                                adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
                                rvFotoResolusiAspirasi.setAdapter(adpImg);
                                Log.e("Error","Array Provider Job " + arrProvImgRemarkResolusi.size());
                            }
                            else if(bitmap.getWidth() < bitmap.getHeight()){
                                int comp = 100/(bitmap.getHeight()/500);
                                int w = bitmap.getWidth()*comp/100;
                                int h = bitmap.getHeight()*comp/100;
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoResolusi provJob = new ProviderFotoResolusi();
                                provJob.setImageBitmapResolusi(resized);
                                provJob.setImagePathResolusi(path);
                                arrProvImgRemarkResolusi.add(provJob);
                                adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
                                rvFotoResolusiAspirasi.setAdapter(adpImg);
                                Log.e("Error","Array Provider Job " + arrProvImgRemarkResolusi.size());
                            }
                            else if(bitmap.getWidth() > bitmap.getHeight()){
                                int comp = 100/(bitmap.getWidth()/500);
                                int w = bitmap.getWidth()*comp/100;
                                int h = bitmap.getHeight()*comp/100;
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoResolusi provJob = new ProviderFotoResolusi();
                                provJob.setImageBitmapResolusi(resized);
                                provJob.setImagePathResolusi(path);
                                arrProvImgRemarkResolusi.add(provJob);
                                adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
                                rvFotoResolusiAspirasi.setAdapter(adpImg);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                Uri imageUri = data.getData();
                //image path
                String path = GlobalActivity.getPath(getApplicationContext(), imageUri);
                pathImageResolusi.add(path);

                try {
                    InputStream is = getApplicationContext().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if(bitmap.getWidth() <= 1000 && bitmap.getHeight() <= 1000){
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        //resize image
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                        bitmaps.add(resized);
                        ProviderFotoResolusi provJob = new ProviderFotoResolusi();
                        provJob.setImageBitmapResolusi(resized);
                        provJob.setImagePathResolusi(path);
                        arrProvImgRemarkResolusi.add(provJob);
                        adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
                        rvFotoResolusiAspirasi.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemarkResolusi.size());
                    }
                    else if(bitmap.getWidth() < bitmap.getHeight()){
                        int comp = 100/(bitmap.getHeight()/500);
                        int w = bitmap.getWidth()*comp/100;
                        int h = bitmap.getHeight()*comp/100;
                        //resize image
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                        bitmaps.add(resized);
                        ProviderFotoResolusi provJob = new ProviderFotoResolusi();
                        provJob.setImageBitmapResolusi(resized);
                        provJob.setImagePathResolusi(path);
                        arrProvImgRemarkResolusi.add(provJob);
                        adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
                        rvFotoResolusiAspirasi.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemarkResolusi.size());
                    }
                    else if(bitmap.getWidth() > bitmap.getHeight()){
                        int comp = 100/(bitmap.getWidth()/500);
                        int w = bitmap.getWidth()*comp/100;
                        int h = bitmap.getHeight()*comp/100;
                        //resize image
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                        bitmaps.add(resized);
                        ProviderFotoResolusi provJob = new ProviderFotoResolusi();
                        provJob.setImageBitmapResolusi(resized);
                        provJob.setImagePathResolusi(path);
                        arrProvImgRemarkResolusi.add(provJob);
                        adpImg = new AdapterFotoResolusi(arrProvImgRemarkResolusi);
                        rvFotoResolusiAspirasi.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemarkResolusi.size());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
