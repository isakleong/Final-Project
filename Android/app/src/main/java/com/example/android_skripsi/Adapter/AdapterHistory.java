package com.example.android_skripsi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.DetailHistory;
import com.example.android_skripsi.MainActivity;
import com.example.android_skripsi.Provider.ProviderHistory;
import com.example.android_skripsi.R;
import com.example.android_skripsi.SignIn;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.historyJobHolder> {
    private ArrayList<ProviderHistory> arrProviderHistory;
    Context mcontext;

    public AdapterHistory(ArrayList<ProviderHistory> serie) {
        arrProviderHistory = serie;
    }

    @Override
    public AdapterHistory.historyJobHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_history, viewGroup, false);
        return new AdapterHistory.historyJobHolder(inflatedView);
    }
    @Override
    public void onBindViewHolder(AdapterHistory.historyJobHolder holder, int i) {
        ProviderHistory item = arrProviderHistory.get(i);
        holder.choose(item);
    }
    @Override
    public int getItemCount() {
        return arrProviderHistory.size();
    }

    public class historyJobHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTglAspirasi,txtBiroUnit,txtStatus,txtDetail, headerBiroUnit;
        private ProviderHistory providerHistory;
        private ImageView photoVendor;

        public historyJobHolder(View v) {
            super(v);

            txtStatus = (TextView) v.findViewById(R.id.txtStatus);
            txtTglAspirasi = (TextView) v.findViewById(R.id.txtTglAspirasi);
//            txtTglStatus = (TextView) v.findViewById(R.id.txtTglstatus);
            txtBiroUnit = (TextView) v.findViewById(R.id.txtBiroUnit);
            txtDetail   = (TextView) v.findViewById(R.id.headerDetail);
            headerBiroUnit = (TextView) v.findViewById(R.id.headerBiroUnit);

            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Bundle b   = new Bundle();
            b.putString("teksAspirasi", providerHistory.getTeksAspirasi());
            b.putString("biroUnit", providerHistory.getNamaBiroUnit());
            b.putString("tglAspirasi", providerHistory.getTglAspirasi());
            b.putString("tglStatusAspirasi", providerHistory.getTglStatusAspirasi());
//            b.putByteArray("fotoAspirasiBitmap",providerHistory.getFotoAspirasiByteArray());
            b.putInt("idStatusAspirasi", providerHistory.getIdStatusAspirasi());
            b.putInt("idAspirasi", providerHistory.getIdAspirasi());

//            if (providerHistory.getStatus() == 1){
//                Intent intent = new Intent(mcontext, JobSummaryActivity.class);
//                intent.putExtras(b);
//                mcontext.startActivity(intent);
//            }
        }

        public void choose(ProviderHistory mserie) {
            providerHistory = mserie;

            txtTglAspirasi.setText("Waktu aspirasi: " + providerHistory.getTglAspirasi());

            //check if user civitas login
            if(SignIn.signInMode == 0){
                headerBiroUnit.setText("Biro / Unit: ");
                if(providerHistory.getNamaBiroUnit()!=null && !providerHistory.getNamaBiroUnit().isEmpty()){
                    txtBiroUnit.setText(providerHistory.getNamaBiroUnit());
                }
                else{
                    txtBiroUnit.setText("TBD (To be decided)");
                }
            }

            //check if user biro unit login
           else{
                headerBiroUnit.setText("Pengirim: ");

                //check if anonym sender is true
                //anonym is true
                if(providerHistory.getAnonym()==0){
                    txtBiroUnit.setText("Anonymous");
                }
                //anonym is false
                else{
                    txtBiroUnit.setText(providerHistory.getNamaUserCivitas());
                }
            }
            Integer bytearray = 0;

//            if(providerHistory.getBytearray()!= null){
//                bytearray = providerHistory.getBytearray().length;
//            }
//            Bitmap bmp = BitmapFactory.decodeByteArray(providerHistory.getBytearray(), 0, bytearray);
//            photoVendor.setImageBitmap(bmp);


            if(providerHistory.getIdStatusAspirasi() == 1){
                txtStatus.setText("Menunggu untuk diproses");
            }
            else if(providerHistory.getIdStatusAspirasi() == 3){
                txtStatus.setText("Diterima oleh biro/unit untuk diproses");
            }
            else if(providerHistory.getIdStatusAspirasi() == 4){
                txtStatus.setText("Sedang diproses biro/unit terkait");
            }
            else if(providerHistory.getIdStatusAspirasi() == 5){
                txtStatus.setText("Aspirasi telah selesai diproses");
            }
            else if(providerHistory.getIdStatusAspirasi() == 6 || providerHistory.getIdStatusAspirasi() == 7){
                txtStatus.setText("Resolusi Aspirasi Telah Ditinjau");
            }

            txtDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b   = new Bundle();
                    b.putInt("idAspirasi", providerHistory.getIdAspirasi());
                    b.putInt("idStatusAspirasi", providerHistory.getIdStatusAspirasi());
                    b.putString("contentTeksAspirasi", providerHistory.getTeksAspirasi());
                    b.putString("contentTglAspirasi", providerHistory.getTglAspirasi());
//                    b.putString("contentTglStatus", providerHistory.getTglStatusAspirasi());
                    b.putString("idUserBiroUnit", providerHistory.getIdBiroUnit()+"");
//                    b.putByteArray("contentFotoBiroUnit", providerHistory.getFotoBiroUnitbytearray());
                    b.putString("contentNamaBiroUnit", providerHistory.getNamaBiroUnit());
                    b.putInt("anonym", providerHistory.getAnonym());

                    Intent intent = new Intent(mcontext, DetailHistory.class);
                    intent.putExtras(b);
                    mcontext.startActivity(intent);

//
//                    if (jobProvider.getStatus() == 1){
//                        Intent intent = new Intent(mcontext, JobSummaryActivity.class);
//                        intent.putExtras(b);
//                        mcontext.startActivity(intent);
//                    }
//                    else if (jobProvider.getStatus() == 5){
//                        // dalam perjalanan
//                        Intent intent = new Intent(mcontext, PerjalananUser.class);
//                        b.putString("modeQR","qrPerjalanan");
//                        intent.putExtras(b);
//                        mcontext.startActivity(intent);
//                    }
//                    else if (jobProvider.getStatus() == 7){
//                        // dalam perjalanan
//                        Intent intent = new Intent(mcontext, JobSummaryActivity.class);
//                        intent.putExtras(b);
//                        mcontext.startActivity(intent);
//                    }
//                    else if (jobProvider.getStatus() == 3){
//                        // sedang di proses / pesanan diterima, dll
//                        Intent intent = new Intent(mcontext, JobSummaryActivity.class);
//                        intent.putExtras(b);
//                        mcontext.startActivity(intent);
//                    }
//                    else if (jobProvider.getStatus() == 10 || jobProvider.getStatus() == 11){
//                        // pengerjaan selesai / barang sudah dikirim
//                        Intent intent = new Intent(mcontext, JobSummaryActivity.class);
//                        intent.putExtras(b);
//                        mcontext.startActivity(intent);
//                    }
//                    else if (jobProvider.getStatus() == 13){
//                        // barang telah sampai
//                        TaskJob tJob = new TaskJob();
//                        tJob.setActivity((Activity)mcontext);
//                        tJob.setMode("getJobSummary");
//                        tJob.setIdUser(MainActivity.username);
//                        tJob.setIdJob(jobProvider.getIdJob());
//                        tJob.setIdTransaction(jobProvider.getIdTransaction());
//                        tJob.setJobStatus(13);
//                        tJob.doTask();
//                    }
//                    else if (jobProvider.getStatus() == 16){
//                        // ada komplain
//                        TaskJob tJob = new TaskJob();
//                        tJob.setActivity((Activity)mcontext);
//                        tJob.setMode("getJobSummary");
//                        tJob.setIdUser(MainActivity.username);
//                        tJob.setIdJob(jobProvider.getIdJob());
//                        tJob.setIdTransaction(jobProvider.getIdTransaction());
//                        tJob.setJobStatus(16);
//                        tJob.doTask();
//                    }
                }
            });
        }
    }
}
