package com.example.android_skripsi.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.Provider.ProviderHistory;
import com.example.android_skripsi.R;

import java.util.ArrayList;

public class AdapterContentStatus extends RecyclerView.Adapter<AdapterContentStatus.imageHolder2> {
    private ArrayList<ProviderHistory> arrProviderHistory;
    Context mcontext;

    public AdapterContentStatus(ArrayList<ProviderHistory> serie) {
        arrProviderHistory = serie;
    }

    @Override
    public AdapterContentStatus.imageHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_content_status, viewGroup, false);
        return new AdapterContentStatus.imageHolder2(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContentStatus.imageHolder2 holder, int i) {
        ProviderHistory item = arrProviderHistory.get(i);
        holder.choose(item);
    }

    @Override
    public int getItemCount() {
        return arrProviderHistory.size();
    }

    public class imageHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtContentStatus, txtContentTglStatus;
        private ProviderHistory contentStatus;
        private ImageView imgView;

        public imageHolder2(View v) {
            super(v);
            txtContentStatus  = (TextView) v.findViewById(R.id.txtContentStatus);
            txtContentTglStatus  = (TextView) v.findViewById(R.id.txtContentTglStatus);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
        }

        public void choose(ProviderHistory mserie) {
            contentStatus = mserie;
            txtContentStatus.setText(contentStatus.getNamaStatusAspirasi());
            txtContentTglStatus.setText(contentStatus.getTglStatusAspirasi());
        }
    }
}
