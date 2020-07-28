package com.example.android_skripsi.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContentFotoAspirasi extends RecyclerView.Adapter<AdapterContentFotoAspirasi.imageHolder2> {
    private ArrayList<ProviderFotoAspirasi> providerFotoAspirasi;
    Context mcontext;

    public AdapterContentFotoAspirasi(ArrayList<ProviderFotoAspirasi> serie) {
        providerFotoAspirasi = serie;
    }

    @Override
    public AdapterContentFotoAspirasi.imageHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_content_foto_aspirasi, viewGroup, false);
        return new AdapterContentFotoAspirasi.imageHolder2(inflatedView);
    }

    @Override
    public void onBindViewHolder(AdapterContentFotoAspirasi.imageHolder2 holder, int i) {
        ProviderFotoAspirasi item = providerFotoAspirasi.get(i);
        holder.choose(item);

    }

    @Override
    public int getItemCount() {
        return providerFotoAspirasi.size();
    }

    public class imageHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtRemarks;
        private ProviderFotoAspirasi contentFotoAspirasi;
        private ImageView imgView;

        public imageHolder2(View v) {
            super(v);
            txtRemarks  = (TextView) v.findViewById(R.id.txtRemarksSummary);
            imgView     = (ImageView) v.findViewById(R.id.imgView);
            imgView.setDrawingCacheEnabled(true);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(mcontext, "click", Toast.LENGTH_LONG);
        }

        public void choose(ProviderFotoAspirasi mserie) {
            contentFotoAspirasi = mserie;
            imgView.setImageBitmap(contentFotoAspirasi.getImageBitmap());
            txtRemarks.setText(contentFotoAspirasi.getImageRemarks());
        }
    }
}
