package com.example.android_skripsi.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android_skripsi.InputAspirasi;
import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.R;

import java.util.ArrayList;

public class AdapterFotoAspirasi extends RecyclerView.Adapter<AdapterFotoAspirasi.imageHolder> {
    private ArrayList<ProviderFotoAspirasi> arrProvImgRemark;
    Context mcontext;

    public AdapterFotoAspirasi(ArrayList<ProviderFotoAspirasi> serie) {
        arrProvImgRemark = serie;
    }

    @Override
    public AdapterFotoAspirasi.imageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_foto_aspirasi, viewGroup, false);
        return new AdapterFotoAspirasi.imageHolder(inflatedView);
    }
    @Override
    public void onBindViewHolder(final AdapterFotoAspirasi.imageHolder holder,final int i) {
        ProviderFotoAspirasi item = arrProvImgRemark.get(i);
        holder.choose(item);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mcontext);
                builder1.setMessage("Delete ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                InputAspirasi.pathImage.remove(i);
                                arrProvImgRemark.remove(i);
                                notifyDataSetChanged();
                                InputAspirasi.btnUploadImage.setEnabled(true);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        holder.txtRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ProviderFotoAspirasi cur_item = arrProvImgRemark.get(i);
                cur_item.setImageRemarks(editable.toString());
                Log.e("Di remarks ke"+i,editable.toString());
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrProvImgRemark.size();
    }

    public ArrayList<ProviderFotoAspirasi> getAllData(){return arrProvImgRemark;}

    public class imageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText txtRemarks;
        private ProviderFotoAspirasi jobProvider;
        private ImageView imgView;
        private Bitmap yourbitmap;
        private ImageButton btnDelete;

        public imageHolder(View v) {
            super(v);

            txtRemarks  = (EditText) v.findViewById(R.id.txtRemarks);
            imgView     = (ImageView) v.findViewById(R.id.imgView);
            imgView.setDrawingCacheEnabled(true);
            btnDelete   = (ImageButton) v.findViewById(R.id.btnDelete);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(mcontext,jobProvider.getImagePath(),Toast.LENGTH_SHORT).show();
        }

        public void choose(ProviderFotoAspirasi mserie) {
            jobProvider = mserie;

            imgView.setImageBitmap(jobProvider.getImageBitmap());
            txtRemarks.setText(jobProvider.getImageRemarks());
        }
    }

}
