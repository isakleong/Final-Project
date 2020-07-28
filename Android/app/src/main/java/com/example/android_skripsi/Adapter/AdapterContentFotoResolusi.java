package com.example.android_skripsi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.Provider.ProviderFotoResolusi;
import com.example.android_skripsi.R;

import java.util.ArrayList;

public class AdapterContentFotoResolusi extends RecyclerView.Adapter<AdapterContentFotoResolusi.imageHolder2> {
    private ArrayList<ProviderFotoResolusi> arrProviderFotoResolusi;
    Context mcontext;

    public AdapterContentFotoResolusi(ArrayList<ProviderFotoResolusi> serie) {
        arrProviderFotoResolusi = serie;
    }

    @Override
    public AdapterContentFotoResolusi.imageHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_content_foto_resolusi, viewGroup, false);
        return new AdapterContentFotoResolusi.imageHolder2(inflatedView);
    }

    @Override
    public void onBindViewHolder(AdapterContentFotoResolusi.imageHolder2 holder, int i) {
        ProviderFotoResolusi item = arrProviderFotoResolusi.get(i);
        holder.choose(item);
    }

    @Override
    public int getItemCount() {
        return arrProviderFotoResolusi.size();
    }

    public class imageHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtResolusiFotoSummary;
        private ProviderFotoResolusi contentFotoResolusi;
        private ImageView imgView;

        public imageHolder2(View v) {
            super(v);
            txtResolusiFotoSummary  = (TextView) v.findViewById(R.id.txtResolusiFotoSummary);
            imgView     = (ImageView) v.findViewById(R.id.imgView);
            imgView.setDrawingCacheEnabled(true);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
        }

        public void choose(ProviderFotoResolusi mserie) {
            contentFotoResolusi = mserie;
            imgView.setImageBitmap(contentFotoResolusi.getImageBitmapResolusi());
            txtResolusiFotoSummary.setText(contentFotoResolusi.getImageRemarksResolusi());
        }
    }
}
