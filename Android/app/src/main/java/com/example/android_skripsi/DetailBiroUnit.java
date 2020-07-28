package com.example.android_skripsi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;

public class DetailBiroUnit extends AppCompatActivity {
    TextView txtDetailNamaBiroUnit, txtDetailContact, txtRating, contentInformasiJobDesc;
    RatingBar iconRating;
    public static ImageView detail_photo_avatar;

    public static byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_biro_unit);

//        detail_photo_avatar       = (ImageView) findViewById(R.id.detail_photo_avatar);
        txtDetailNamaBiroUnit = (TextView) findViewById(R.id.txtDetailNamaBiroUnit);
        txtDetailContact = (TextView) findViewById(R.id.txtDetailContact);
        txtRating = (TextView) findViewById(R.id.txtRating);
        contentInformasiJobDesc = (TextView) findViewById(R.id.contentInformasiJobDesc);
        iconRating = (RatingBar) findViewById(R.id.iconRating);

        Bundle b = getIntent().getExtras();
        txtDetailNamaBiroUnit.setText(b.getString("nama_biro_unit")+"");
        txtDetailContact.setText(b.getString("contact_biro_unit")+"");
        txtRating.setText(b.getString("rating_biro_unit")+"");
        contentInformasiJobDesc.setText(b.getString("info_job_desc")+"");
        Float rating = Float.parseFloat(txtRating.getText()+"");
        iconRating.setRating(rating);

        //get profile picture biro unit
        TaskServer taskServer = new TaskServer();
        taskServer.setActivityCaller(DetailBiroUnit.this);
        taskServer.setCallMode("getFotoBiroUnit");
        taskServer.setIdBiroUnit(b.getString("id_user_biro_unit"));
        taskServer.setModeFotoBiroUnit("profile");
        taskServer.callApi();

//        Bitmap bmp = BitmapFactory.decodeByteArray(b.getByteArray("detail_photo_avatar"), 0,
//                b.getByteArray("detail_photo_avatar").length);
//        detail_photo_avatar.setImageBitmap(bmp);

    }
}
