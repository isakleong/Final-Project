package com.example.android_skripsi;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.Adapter.AdapterFotoAspirasi;
import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.TaskServer.TaskServer;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class InputAspirasi extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    ArrayList<ProviderFotoAspirasi> arrProvImgRemark = new ArrayList<>();
    AdapterFotoAspirasi adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
    public static ArrayList<String> hasilImageRemarks = new ArrayList<>();
    public static ArrayList<String> pathImage = new ArrayList<>();
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    RecyclerView rvFotoAspirasi;
    ScrollView scv;
    public static ImageView btnUploadImage;
    private final int PICK_IMAGE_MULTIPLE = 1;

    public static String biroUnitSelected, idBiroUnit;

    EditText teksAspirasi;
    TextView btnSubmitAspirasi;

    private Integer anonim = -1;

    public static ProgressBar progressBarInputAspirasi;
    public static View progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_aspirasi);

        scv = (ScrollView) findViewById(R.id.nsv);
        scv.pageScroll(View.FOCUS_UP);

        ImageView imgPrevious = (ImageView) findViewById(R.id.imagePrevious);
        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //foto aspirasi
        rvFotoAspirasi = (RecyclerView) findViewById(R.id.rvImageAspirasi);
        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvFotoAspirasi.setLayoutManager(mLinearLayoutManager);

        btnUploadImage = (ImageView) findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(InputAspirasi.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(InputAspirasi.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    AdapterFotoAspirasi imgAdapt = (AdapterFotoAspirasi) rvFotoAspirasi.getAdapter();
                    if(imgAdapt!=null) {
                        arrProvImgRemark = imgAdapt.getAllData();
                    }
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinnerShowBiroUnit);
        //set spinner scrollable
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(700);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        final ArrayList<String> arrStr = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.show_biro_unit)));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                arrStr) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont = ResourcesCompat.getFont(getContext(), R.font.roboto_medium);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);

                return v;
            }
            @Override
            public int getCount() {
                return (arrStr.size() - 1);
            }
        };

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(arrStr.size() - 1);

        //set onclick item listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                biroUnitSelected = parent.getItemAtPosition(position)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmitAspirasi = (TextView) findViewById(R.id.btnSubmitAspirasi);
        teksAspirasi = (EditText) findViewById(R.id.teksAspirasi);


        progressOverlay = (View) findViewById(R.id.progress_overlay);
        progressBarInputAspirasi = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ThreeBounce();
        progressBarInputAspirasi.setIndeterminateDrawable(doubleBounce);

        btnSubmitAspirasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateView(progressOverlay, View.VISIBLE, 0.4f, 200);

                //data foto aspirasi
                hasilImageRemarks.clear();
                rvFotoAspirasi.setItemViewCacheSize(arrProvImgRemark.size());
//                Toast.makeText(InputAspirasi.this, "INI "+adpImg.getItemCount(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < adpImg.getItemCount(); i++) {
                    View view = rvFotoAspirasi.getChildAt(i);
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
                    hasilImageRemarks.add(encodedString + "---" + txtremarks);
                }

                //call api
//                TaskServer taskServer = new TaskServer();
//                taskServer.setCallMode("insertAspirasi");
//                taskServer.setActivityCaller(InputAspirasi.this);
//                taskServer.setTeksAspirasi(teksAspirasi.getText()+"");
//                taskServer.setBiroUnit(biroUnitSelected);
//                taskServer.setAnonym(anonim);
//                taskServer.callApi();

                TaskServer taskServer = new TaskServer();
                taskServer.setCallMode("getModelPredict");
                taskServer.setActivityCaller(InputAspirasi.this);
                taskServer.setTeksAspirasi(teksAspirasi.getText()+"");
                taskServer.setBiroUnit(biroUnitSelected);
                taskServer.setAnonym(anonim);
                taskServer.callApi();

            }
        });

    }

    public static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }

    public void anonimClicked(View v){
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            anonim=0;
        }
        else{
            anonim=1;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(arrProvImgRemark.size() >= 3){btnUploadImage.setEnabled(false);}
        else {btnUploadImage.setEnabled(true);}
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK) {


            ClipData clipData = data.getClipData();
            if (clipData != null) {
                int max = 5 - arrProvImgRemark.size();
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
                            pathImage.add(path);
                            Log.e("Error","Path" + path);
                            if(bitmap.getWidth() <= 1000 && bitmap.getHeight() <= 1000){
                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                                provJob.setImageBitmap(resized);
                                provJob.setImagePath(path);
                                arrProvImgRemark.add(provJob);
                                adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
                                rvFotoAspirasi.setAdapter(adpImg);
                                Log.e("Error","Array Provider Job " + arrProvImgRemark.size());
                            }
                            else if(bitmap.getWidth() < bitmap.getHeight()){
                                int comp = 100/(bitmap.getHeight()/500);
                                int w = bitmap.getWidth()*comp/100;
                                int h = bitmap.getHeight()*comp/100;
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                                provJob.setImageBitmap(resized);
                                provJob.setImagePath(path);
                                arrProvImgRemark.add(provJob);
                                adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
                                rvFotoAspirasi.setAdapter(adpImg);
                                Log.e("Error","Array Provider Job " + arrProvImgRemark.size());
                            }
                            else if(bitmap.getWidth() > bitmap.getHeight()){
                                int comp = 100/(bitmap.getWidth()/500);
                                int w = bitmap.getWidth()*comp/100;
                                int h = bitmap.getHeight()*comp/100;
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                                provJob.setImageBitmap(resized);
                                provJob.setImagePath(path);
                                arrProvImgRemark.add(provJob);
                                adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
                                rvFotoAspirasi.setAdapter(adpImg);
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
                pathImage.add(path);

                try {
                    InputStream is = getApplicationContext().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if(bitmap.getWidth() <= 1000 && bitmap.getHeight() <= 1000){
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        //resize image
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                        bitmaps.add(resized);
                        ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                        provJob.setImageBitmap(resized);
                        provJob.setImagePath(path);
                        arrProvImgRemark.add(provJob);
                        adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
                        rvFotoAspirasi.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemark.size());
                    }
                    else if(bitmap.getWidth() < bitmap.getHeight()){
                        int comp = 100/(bitmap.getHeight()/500);
                        int w = bitmap.getWidth()*comp/100;
                        int h = bitmap.getHeight()*comp/100;
                        //resize image
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                        bitmaps.add(resized);
                        ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                        provJob.setImageBitmap(resized);
                        provJob.setImagePath(path);
                        arrProvImgRemark.add(provJob);
                        adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
                        rvFotoAspirasi.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemark.size());
                    }
                    else if(bitmap.getWidth() > bitmap.getHeight()){
                        int comp = 100/(bitmap.getWidth()/500);
                        int w = bitmap.getWidth()*comp/100;
                        int h = bitmap.getHeight()*comp/100;
                        //resize image
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                        bitmaps.add(resized);
                        ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                        provJob.setImageBitmap(resized);
                        provJob.setImagePath(path);
                        arrProvImgRemark.add(provJob);
                        adpImg = new AdapterFotoAspirasi(arrProvImgRemark);
                        rvFotoAspirasi.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemark.size());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //handle aspirasi submission
//            btnSubmitAspirasi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //get data id biro / unit selected
//                    Toast.makeText(InputAspirasi.this, "heee", Toast.LENGTH_SHORT).show();
//                    TaskServer taskServer = new TaskServer();
//                    taskServer.setCallMode("getBiroUnitSelected");
//                    taskServer.setContext(getApplicationContext());
//                    taskServer.setActivityCaller(InputAspirasi.this);
//                    taskServer.callApi();
//
//                    //data foto aspirasi
//                    hasilImageRemarks.clear();
//                    rvFotoAspirasi.setItemViewCacheSize(arrProvImgRemark.size());
//                    for (int i = 0; i < adpImg.getItemCount(); i++) {
//                        Log.e("counter foto ", "masuk "+i);
//                        View view = rvFotoAspirasi.getChildAt(i);
//                        EditText remarks  = (EditText) view.findViewById(R.id.txtRemarks);
//                        ImageView imgView = (ImageView) view.findViewById(R.id.imgView);
//                        String txtremarks;
//                        if(remarks.getText().toString().equals("")){
//                            txtremarks = " ";
//                        }
//                        else {
//                            txtremarks = remarks.getText().toString();
//                        }
//                        //foto aspirasi converted to string base 64
//                        imgView.invalidate();
//                        BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
//                        Bitmap bitmap           = drawable.getBitmap();
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                        byte[] byteArray = byteArrayOutputStream .toByteArray();
//
//                        String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                        hasilImageRemarks.add(encodedString + "---" + txtremarks);
//                    }
//
//                    String fotoAspirasi = "";
//                    String detailFotoAspirasi = "";
//
//                    for(int i=0;i<hasilImageRemarks.size();i++){
//                        String[] parts = hasilImageRemarks.get(i).split("---");
//
//                        fotoAspirasi+="data:image/jpeg;base64,";
//                        if(i==hasilImageRemarks.size()-1){
//                            fotoAspirasi+=parts[0];
//                            detailFotoAspirasi+=parts[1];
//                        }
//                        else{
//                            fotoAspirasi+=parts[0];
//                            detailFotoAspirasi+=parts[1];
//                        }
//                        detailFotoAspirasi+="||";
//                    }
//
//                    //call api
//                    taskServer.setCallMode("insertAspirasi");
//                    taskServer.setContext(getApplicationContext());
//                    taskServer.setActivityCaller(InputAspirasi.this);
//                    taskServer.setTeksAspirasi(teksAspirasi.getText()+"");
//                    taskServer.setFotoAspirasi(fotoAspirasi);
//                    taskServer.setDetailFotoAspirasi(detailFotoAspirasi);
//                    taskServer.setBiroUnit(biroUnitSelected);
////                    taskServer.callApi();
//
//                }
//            });

        }
    }
}
