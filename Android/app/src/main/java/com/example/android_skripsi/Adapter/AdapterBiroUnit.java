package com.example.android_skripsi.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_skripsi.Provider.ProviderBiroUnit;
import com.example.android_skripsi.R;

import java.security.Provider;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class AdapterBiroUnit extends RecyclerView.Adapter<AdapterBiroUnit.vendorHolder> {
    private ArrayList<ProviderBiroUnit> arrProviderBiroUnit;
    Context mcontext;

    public static String idBiroUnit,namaBiroUnit,emailBiroUnit,ratingBiroUnit,jobDescBiroUnit;
    public static Bitmap bitmapPhotoAvatar;
    public static byte[] bytearay;

    public static ImageView photo_avatar_vendor;
    int row_index = -1;

    private AdapterBiroUnit.OnItemClick mCallback;

    public AdapterBiroUnit(ArrayList<ProviderBiroUnit> serie) {
        arrProviderBiroUnit = serie;
    }

    public AdapterBiroUnit(Context context, ArrayList<ProviderBiroUnit> serie, AdapterBiroUnit.OnItemClick onItemClick) {
        this.arrProviderBiroUnit = serie;
        this.mcontext = context;
        this.mCallback = onItemClick;
    }

    public interface OnItemClick {
        void onClick(String idBiroUnit, String namaBiroUnit,String emailBiroUnit, String ratingBiroUnit,
                     String jobDescBiroUnit);
    }

    @Override
    public AdapterBiroUnit.vendorHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_biro_unit, viewGroup, false);
        return new AdapterBiroUnit.vendorHolder(inflatedView, this);
    }
    @Override
    public void onBindViewHolder(AdapterBiroUnit.vendorHolder holder, int i) {
        String mCurrent = arrProviderBiroUnit.get(i).getNamaBiroUnit();
        ProviderBiroUnit item = arrProviderBiroUnit.get(i);
        holder.choose(item);
        holder.txtNamaBiroUnit.setText(mCurrent);
        if(row_index == i){
            //holder.row_linearlayout.setBackgroundColor(Color.parseColor("#567845"));
            //holder.txtTypeJob.setTextColor(Color.parseColor("#ffffff"));
            //holder.txtFullName.setTypeface(Typeface.DEFAULT_BOLD);
            //holder.txtFullName.setPaintFlags(holder.txtFullName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else {
            //holder.txtTypeJob.setTextColor(Color.parseColor("#000000"));
            //holder.txtFullName.setTypeface(Typeface.DEFAULT);
            //holder.txtFullName.setPaintFlags(holder.txtFullName.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        }

    }
    @Override
    public int getItemCount() {
        return arrProviderBiroUnit.size();
    }

    public class vendorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNamaBiroUnit,txtEmailBiroUnit,txtJobDesc,txtRating;
        private ImageView photo_avatar_vendor_imgview;
        private ProviderBiroUnit jobProvider;
        final AdapterBiroUnit mAdapter;
        private String itemClicked;

        public vendorHolder(View v, AdapterBiroUnit adapter) {
            super(v);
            txtNamaBiroUnit                 = (TextView) v.findViewById(R.id.txtNamaBiroUnit);
            txtEmailBiroUnit                = (TextView) v.findViewById(R.id.txtEmailBiroUnit);
            txtRating                       = (TextView) v.findViewById(R.id.txtRatingBiroUnit);
            photo_avatar_vendor_imgview     = (ImageView) v.findViewById(R.id.photoAvatarBiroUnit);
            this.mAdapter = adapter;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(mcontext,jobProvider.getIdDetailProduct(),Toast.LENGTH_SHORT).show();
            int mPosition       = getLayoutPosition();
            row_index           = mPosition;
            idBiroUnit              = arrProviderBiroUnit.get(mPosition).getIdBiroUnit();
            namaBiroUnit            = arrProviderBiroUnit.get(mPosition).getNamaBiroUnit();
            emailBiroUnit       = arrProviderBiroUnit.get(mPosition).getEmailBiroUnit();
            ratingBiroUnit              = arrProviderBiroUnit.get(mPosition).getRatingBiroUnit();
            bitmapPhotoAvatar   = arrProviderBiroUnit.get(mPosition).getImageBitmap();
//            bytearay            = arrProviderBiroUnit.get(mPosition).getBytearray();
            jobDescBiroUnit =arrProviderBiroUnit.get(mPosition).getJobDescBiroUnit();
            mCallback.onClick(idBiroUnit,namaBiroUnit,emailBiroUnit,ratingBiroUnit,jobDescBiroUnit);
            byte[] bitmapPhotoAvatar;
            mAdapter.notifyDataSetChanged();
        }

        public void choose(ProviderBiroUnit mserie) {
            jobProvider = mserie;
            txtNamaBiroUnit.setText(jobProvider.getNamaBiroUnit());
            txtEmailBiroUnit.setText(jobProvider.getEmailBiroUnit());
            txtRating.setText(jobProvider.getRatingBiroUnit()+"/5");

            Bitmap bmp = BitmapFactory.decodeByteArray(jobProvider.getBytearray(), 0,
                    jobProvider.getBytearray().length);
            photo_avatar_vendor_imgview.setImageBitmap(bmp);

            //set dummy untuk jarak dan waktu
        }

        public String getItemClicked() {
            return itemClicked;
        }

        public void setItemClicked(String itemClicked) {
            this.itemClicked = itemClicked;
        }
    }

}