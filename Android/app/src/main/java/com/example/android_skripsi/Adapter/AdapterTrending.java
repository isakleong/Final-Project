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
import com.example.android_skripsi.Provider.ProviderTrending;
import com.example.android_skripsi.R;

import java.util.ArrayList;

public class AdapterTrending extends RecyclerView.Adapter<AdapterTrending.vendorHolder> {
    private ArrayList<ProviderTrending> arrProviderTrending;
    Context mcontext;

    public static String idTrending,teksTrending,biroUnitTrending;

    int row_index = -1;

    private AdapterTrending.OnItemClick mCallback;

    public AdapterTrending(ArrayList<ProviderTrending> serie) {
        arrProviderTrending = serie;
    }

    public AdapterTrending(Context context, ArrayList<ProviderTrending> serie, AdapterTrending.OnItemClick onItemClick) {
        this.arrProviderTrending = serie;
        this.mcontext = context;
        this.mCallback = onItemClick;
    }

    public interface OnItemClick {
        void onClick(String idTrending, String teksTrending, String biroUnitTrending);
    }

    @Override
    public AdapterTrending.vendorHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mcontext = viewGroup.getContext();
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_trending, viewGroup, false);
        return new AdapterTrending.vendorHolder(inflatedView, this);
    }
    @Override
    public void onBindViewHolder(AdapterTrending.vendorHolder holder, int i) {
        String mCurrent = arrProviderTrending.get(i).getTeksTrending();
        ProviderTrending item = arrProviderTrending.get(i);
        holder.choose(item);
        holder.txtTrending.setText(mCurrent);
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
        return arrProviderTrending.size();
    }

    public class vendorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtTrending,txtBiroUnitTrending;
        private ProviderTrending jobProvider;
        final AdapterTrending mAdapter;
        private String itemClicked;

        public vendorHolder(View v, AdapterTrending adapter) {
            super(v);
            txtTrending                        = (TextView) v.findViewById(R.id.txtTrending);
            txtBiroUnitTrending                = (TextView) v.findViewById(R.id.txtBiroUnitTrending);
            this.mAdapter = adapter;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(mcontext,jobProvider.getIdDetailProduct(),Toast.LENGTH_SHORT).show();
            int mPosition       = getLayoutPosition();
            row_index           = mPosition;
            teksTrending        = arrProviderTrending.get(mPosition).getTeksTrending();
            biroUnitTrending    = arrProviderTrending.get(mPosition).getBiroUnitTrending();
            mCallback.onClick(idTrending,teksTrending,biroUnitTrending);
            mAdapter.notifyDataSetChanged();
        }

        public void choose(ProviderTrending mserie) {
            jobProvider = mserie;
            txtTrending.setText(jobProvider.getTeksTrending());
            txtBiroUnitTrending.setText(jobProvider.getBiroUnitTrending());

        }

        public String getItemClicked() {
            return itemClicked;
        }

        public void setItemClicked(String itemClicked) {
            this.itemClicked = itemClicked;
        }
    }

}
