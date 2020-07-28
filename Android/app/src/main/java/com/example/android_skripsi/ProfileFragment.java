package com.example.android_skripsi;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_skripsi.TaskServer.TaskServer;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    View v;
    public static ImageView imgProfilePicture;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView txtNamaLengkap = (TextView) v.findViewById(R.id.txtNamaLengkap);
        TextView txtNrp = (TextView) v.findViewById(R.id.txtNRP);
        TextView txtTglLahir = (TextView) v.findViewById(R.id.txtTglLahir);
        TextView txtAlamat = (TextView) v.findViewById(R.id.txtAlamat);

        txtNamaLengkap.setText(SignIn.idUser+"");
        txtNrp.setText(SignIn.nrp);
        txtTglLahir.setText(SignIn.tempat_lahir+", "+SignIn.tgl_lahir);
        txtAlamat.setText(SignIn.alamat);

        imgProfilePicture = (ImageView) v.findViewById(R.id.imgProfilePicture);
        imgProfilePicture.setImageBitmap(SignIn.profilePictureBitmap);

        TextView txtLogout = (TextView) v.findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("isUserLogin");
                editor.remove("idUser");
                editor.remove("nama_lengkap");
                editor.remove("nrp");
                editor.remove("tempat_lahir");
                editor.remove("tgl_lahir");
                editor.remove("alamat");
                editor.commit();

                getActivity().finish();

                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
            }
        });

        return v;

    }

}
