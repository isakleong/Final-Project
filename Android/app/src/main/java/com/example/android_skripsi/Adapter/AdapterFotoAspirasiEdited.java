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

import com.example.android_skripsi.EditAspirasi;
import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.R;

import java.util.ArrayList;
import java.util.Map;

public class AdapterFotoAspirasiEdited extends RecyclerView.Adapter<AdapterFotoAspirasiEdited.imageHolder> {
    private ArrayList<ProviderFotoAspirasi> arrProvImgRemark;
    Context mcontext;

    public AdapterFotoAspirasiEdited(ArrayList<ProviderFotoAspirasi> serie) {
        arrProvImgRemark = serie;
    }

    @Override
    public AdapterFotoAspirasiEdited.imageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_foto_aspirasi_edited, viewGroup, false);
        return new AdapterFotoAspirasiEdited.imageHolder(inflatedView);
    }
    @Override
    public void onBindViewHolder(final AdapterFotoAspirasiEdited.imageHolder holder,final int i) {
        final ProviderFotoAspirasi item = arrProvImgRemark.get(i);
        holder.choose(item);
        final String before = item.getImageRemarks();
        final String after;

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
//                                EditAspirasi.pathImageEdited.remove(i);
                                arrProvImgRemark.remove(i);
                                notifyDataSetChanged();
                                EditAspirasi.btnUploadImageEdited.setEnabled(true);

                                //add data deleted image to temp array
                                EditAspirasi.idImageDeleted.add(item.getIdImageRemarks());
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
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                boolean remarksChange = Math.abs(count - before) == 1;
                if(remarksChange){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ProviderFotoAspirasi cur_item = arrProvImgRemark.get(i);
                cur_item.setImageRemarks(editable.toString());
                Log.e("Di remarks ke"+i,editable.toString());

//                //add data edited image to temp array
//                EditAspirasi.idImageEdited.add(cur_item.getIdImageRemarks());
//
//                //get original remarks content from an image by id
////                String beforeEdited = EditAspirasi.idImageEdited.get(Integer.parseInt(cur_item.getIdImageRemarks()));
//
//                for (Map.Entry<String, String> entry : EditAspirasi.mapImageBefore.entrySet()) {
//                    if(entry.getKey().equals(cur_item.getIdImageRemarks())){
//                        Toast.makeText(mcontext, "INI ID EDITED "+entry.getKey(), Toast.LENGTH_SHORT).show();
//                        EditAspirasi.idImageEdited.add(entry.getKey());
//                    }
//                }

//                if(!cur_item.getImageRemarks().equals(EditAspirasi.i)){
//                    Toast.makeText(mcontext, "SEKARANG "+arrProvImgRemark.get(i).getIdImageRemarks(), Toast.LENGTH_SHORT).show();
//                }
//                else{
//
//                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrProvImgRemark.size();
    }

    public ArrayList<ProviderFotoAspirasi> getAllData(){return arrProvImgRemark;}

    public class imageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String idRemarks;
        private EditText txtRemarks;
        private ProviderFotoAspirasi providerFotoAspirasi;
        private ImageView imgView;
        private Bitmap yourbitmap;
        private ImageButton btnDelete;

        public imageHolder(View v) {
            super(v);

            txtRemarks  = (EditText) v.findViewById(R.id.txtRemarks);
            imgView     = (ImageView) v.findViewById(R.id.imgView);
            imgView.setDrawingCacheEnabled(true);
            btnDelete   = (ImageButton) v.findViewById(R.id.btnDelete);
            idRemarks   = "";
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(mcontext,providerFotoAspirasi.getIdImageRemarks(),Toast.LENGTH_SHORT).show();
//            Toast.makeText(mcontext,providerFotoAspirasi.getImagePath(),Toast.LENGTH_SHORT).show();
        }

        public void choose(ProviderFotoAspirasi mserie) {
            providerFotoAspirasi = mserie;

            imgView.setImageBitmap(providerFotoAspirasi.getImageBitmap());
            txtRemarks.setText(providerFotoAspirasi.getImageRemarks());
        }
    }

}
