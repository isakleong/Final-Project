package com.example.android_skripsi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.Provider.ProviderFotoBiroUnit;
import com.example.android_skripsi.R;

import java.util.ArrayList;

public class AdapterContentFotoBiroUnit extends RecyclerView.Adapter<AdapterContentFotoBiroUnit.imageHolder2> {
    private ArrayList<ProviderFotoBiroUnit> providerFotoBiroUnit;
    Context mcontext;

    public AdapterContentFotoBiroUnit(ArrayList<ProviderFotoBiroUnit> serie) {
        providerFotoBiroUnit = serie;
    }

    @Override
    public AdapterContentFotoBiroUnit.imageHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_content_foto_biro_unit, viewGroup, false);
        return new AdapterContentFotoBiroUnit.imageHolder2(inflatedView);
    }

    @Override
    public void onBindViewHolder(AdapterContentFotoBiroUnit.imageHolder2 holder, int i) {
        ProviderFotoBiroUnit item = providerFotoBiroUnit.get(i);
        holder.choose(item);

    }

    @Override
    public int getItemCount() {
        return providerFotoBiroUnit.size();
    }

    public class imageHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProviderFotoBiroUnit contentFotoBiroUnit;
        private ImageView imgView;

        public imageHolder2(View v) {
            super(v);
            imgView     = (ImageView) v.findViewById(R.id.imgView);
            imgView.setDrawingCacheEnabled(true);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(mcontext, "click", Toast.LENGTH_LONG);
        }

        public void choose(ProviderFotoBiroUnit mserie) {
            contentFotoBiroUnit = mserie;
            imgView.setImageBitmap(contentFotoBiroUnit.getImageBitmap());
        }
    }
}
