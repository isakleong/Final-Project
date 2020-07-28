package com.example.android_skripsi;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.Adapter.AdapterFotoAspirasi;
import com.example.android_skripsi.Adapter.AdapterFotoAspirasiEdited;
import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.TaskServer.TaskServer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditAspirasi extends AppCompatActivity {
    public static String biroUnitSelectedEdited;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private final int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public static ArrayList<ProviderFotoAspirasi> arrProvImgRemark = new ArrayList<>();
    public static AdapterFotoAspirasiEdited adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
    public static ArrayList<String> hasilImageRemarksEdited = new ArrayList<>();
    public static ArrayList<String> pathImageEdited = new ArrayList<>();

    public static RecyclerView rvImageAspirasiEdited;
    public static ImageView btnUploadImageEdited;

    Bundle bundle;

    TextView contenTeksAspirasiEdited;
    CheckBox contentAnonim;
    private Integer anonim = -1;

    //array for manage data image aspirasi (delete, add, edit)
    public static ArrayList<String> idImageDeleted = new ArrayList<String>();
    public static ArrayList<String> idImageEdited = new ArrayList<String>();

    public static Map<String, String> mapImageBefore = new HashMap<String, String>();
    public static Map<String, String> mapImageAfter = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aspirasi);

        //get bundle data -- set the properties
        bundle = getIntent().getExtras();
        contenTeksAspirasiEdited = (TextView) findViewById(R.id.teksAspirasiEdited);
        contenTeksAspirasiEdited.setText(bundle.getString("teksAspirasiEdit"));

        contentAnonim = (CheckBox) findViewById(R.id.checkAnonimEdited);
        if(bundle.getInt("anonimEdit")==0){
            anonim = 0;
            contentAnonim.setChecked(true);
        }
        else{
            anonim = 1;
            contentAnonim.setChecked(false);
        }


        Spinner spinner = (Spinner) findViewById(R.id.spinnerShowBiroUnitEdited);
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

        Integer selectedItem=0;
        for(int i=0; i<arrStr.size(); i++){
            if(arrStr.get(i).equals(bundle.getString("biroUnitEdit"))){
                selectedItem=i;
                break;
            }
        }

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedItem);

        //set onclick item listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                biroUnitSelectedEdited = parent.getItemAtPosition(position)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //content image aspirasi edited
        rvImageAspirasiEdited = (RecyclerView) findViewById(R.id.rvImageAspirasiEdited);
        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvImageAspirasiEdited.setLayoutManager(mLinearLayoutManager);

        btnUploadImageEdited = (ImageView) findViewById(R.id.btnUploadImageEdited);
        btnUploadImageEdited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditAspirasi.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(EditAspirasi.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    AdapterFotoAspirasiEdited imgAdapt = (AdapterFotoAspirasiEdited) rvImageAspirasiEdited.getAdapter();
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

        TextView btnEditAspirasi = (TextView) findViewById(R.id.btnEditAspirasi);
        btnEditAspirasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data foto aspirasi
                hasilImageRemarksEdited.clear();
                rvImageAspirasiEdited.setItemViewCacheSize(arrProvImgRemark.size());
                for (int i = 0; i < adpImg.getItemCount(); i++) {
                    View view = rvImageAspirasiEdited.getChildAt(i);
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
                    hasilImageRemarksEdited.add(encodedString + "---" + txtremarks + "---"
                            +arrProvImgRemark.get(i).getIdImageRemarks()+"");
                }

                TaskServer taskServer = new TaskServer();
                taskServer.setCallMode("editAspirasi");
                taskServer.setActivityCaller(EditAspirasi.this);
                taskServer.setTeksAspirasi(contenTeksAspirasiEdited.getText()+"");
                taskServer.setBiroUnit(biroUnitSelectedEdited);
                taskServer.setIdAspirasi(bundle.getString("idAspirasi"));
                taskServer.setAnonym(anonim);
                taskServer.callApi();
            }
        });


        TaskServer taskServer = new TaskServer();
        taskServer.setCallMode("getFotoAspirasiEdited");
        taskServer.setActivityCaller(EditAspirasi.this);
        taskServer.setIdAspirasi(bundle.getString("idAspirasi"));
        taskServer.callApi();

    }

    public void anonimClickedEdited(View v){
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            anonim=0;
        }
        else{
            anonim=1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(arrProvImgRemark.size() >= 5){btnUploadImageEdited.setEnabled(false);}
        else {btnUploadImageEdited.setEnabled(true);}
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
                            pathImageEdited.add(path);
                            Log.e("Error","Path" + path);
                            if(bitmap.getWidth() <= 1000 && bitmap.getHeight() <= 1000){
                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                //resize image
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
                                bitmaps.add(resized);
                                ProviderFotoAspirasi provJob = new ProviderFotoAspirasi();
                                provJob.setIdImageRemarks(0+"");
                                provJob.setImageBitmap(resized);
                                provJob.setImagePath(path);
                                arrProvImgRemark.add(provJob);
                                adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
                                rvImageAspirasiEdited.setAdapter(adpImg);
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
                                provJob.setIdImageRemarks(0+"");
                                provJob.setImageBitmap(resized);
                                provJob.setImagePath(path);
                                arrProvImgRemark.add(provJob);
                                adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
                                rvImageAspirasiEdited.setAdapter(adpImg);
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
                                provJob.setIdImageRemarks(0+"");
                                provJob.setImageBitmap(resized);
                                provJob.setImagePath(path);
                                arrProvImgRemark.add(provJob);
                                adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
                                rvImageAspirasiEdited.setAdapter(adpImg);
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
                pathImageEdited.add(path);

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
                        provJob.setIdImageRemarks(0+"");
                        provJob.setImageBitmap(resized);
                        provJob.setImagePath(path);
                        arrProvImgRemark.add(provJob);
                        adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
                        rvImageAspirasiEdited.setAdapter(adpImg);
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
                        provJob.setIdImageRemarks(0+"");
                        provJob.setImageBitmap(resized);
                        provJob.setImagePath(path);
                        arrProvImgRemark.add(provJob);
                        adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
                        rvImageAspirasiEdited.setAdapter(adpImg);
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
                        provJob.setIdImageRemarks(0+"");
                        provJob.setImageBitmap(resized);
                        provJob.setImagePath(path);
                        arrProvImgRemark.add(provJob);
                        adpImg = new AdapterFotoAspirasiEdited(arrProvImgRemark);
                        rvImageAspirasiEdited.setAdapter(adpImg);
                        Log.e("Error","Array Provider Job " + arrProvImgRemark.size());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
