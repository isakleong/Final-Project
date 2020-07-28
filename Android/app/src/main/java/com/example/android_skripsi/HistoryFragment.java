package com.example.android_skripsi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android_skripsi.Adapter.AdapterHistory;
import com.example.android_skripsi.TaskServer.TaskServer;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class HistoryFragment extends Fragment {
    View v;
    public static TextView txtNotProcess,txtInProcess,txtCompleted,txtKet,txtOops;
    ImageView imgView;
    public static RecyclerView rvHistory;
    public static Drawable img;

    public static ProgressBar progressBarShowHistory;
    public static View MainprogressOverlay;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_history, container, false);

//        MainprogressOverlay = (View) v.findViewById(R.id.progress_overlay);
        progressBarShowHistory = (ProgressBar) v.findViewById(R.id.spin_kit_history);
        Sprite doubleBounce = new ThreeBounce();
        progressBarShowHistory.setIndeterminateDrawable(doubleBounce);

        txtNotProcess = (TextView) v.findViewById(R.id.txtNotProcess);
        txtInProcess = (TextView) v.findViewById(R.id.txtInProcess);
        txtCompleted = (TextView) v.findViewById(R.id.txtCompleted);

        //drawable underline blue
        img = getContext().getResources().getDrawable( R.drawable.icongaris);
        img.setBounds( 0, 0, 200, 10 );

        rvHistory = (RecyclerView) v.findViewById(R.id.rvHistory);
        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getContext(), 1);
        rvHistory.setLayoutManager(mLinearLayoutManager);

        txtKet  = (TextView) v.findViewById(R.id.txtKet);
        txtOops = (TextView) v.findViewById(R.id.txtOops);
        imgView = (ImageView) v.findViewById(R.id.imgView);

        onGoing();

        txtNotProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNotProcess.setCompoundDrawables(null,null,null,img);
                txtInProcess.setCompoundDrawables(null,null,null,null);
                txtCompleted.setCompoundDrawables(null,null,null,null);

                rvHistory.setAdapter(null);
                HistoryFragment.progressBarShowHistory.setVisibility(View.VISIBLE);
//                animateView(MainprogressOverlay, View.VISIBLE, 0.4f, 200);


                //call api
                TaskServer taskServer = new TaskServer();
                taskServer.setActivityCaller(getActivity());
                taskServer.setCallMode("getNotProcessHistory");
                taskServer.setStatusAspirasi(0+"");
                taskServer.callApi();
            }
        });

        txtInProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInProcess.setCompoundDrawables(null,null,null,img);
                txtNotProcess.setCompoundDrawables(null,null,null,null);
                txtCompleted.setCompoundDrawables(null,null,null,null);

                rvHistory.setAdapter(null);
                HistoryFragment.progressBarShowHistory.setVisibility(View.VISIBLE);
//                animateView(MainprogressOverlay, View.VISIBLE, 0.4f, 200);

                //call api
                TaskServer taskServer = new TaskServer();
                taskServer.setActivityCaller(getActivity());
                taskServer.setCallMode("getInProcessHistory");
                taskServer.setStatusAspirasi(1+"");
                taskServer.callApi();
            }
        });

        txtCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCompleted.setCompoundDrawables(null,null,null,img);
                txtInProcess.setCompoundDrawables(null,null,null,null);
                txtNotProcess.setCompoundDrawables(null,null,null,null);

                rvHistory.setAdapter(null);
                HistoryFragment.progressBarShowHistory.setVisibility(View.VISIBLE);
//                animateView(MainprogressOverlay, View.VISIBLE, 0.4f, 200);

                //call api
                TaskServer taskServer = new TaskServer();
                taskServer.setActivityCaller(getActivity());
                taskServer.setCallMode("getCompletedProcessHistory");
                taskServer.setStatusAspirasi(2+"");
                taskServer.callApi();
            }
        });

        return v;
    }

    public void onGoing(){
        txtNotProcess.setCompoundDrawables(null,null,null,img);
        txtInProcess.setCompoundDrawables(null,null,null, null);
        txtCompleted.setCompoundDrawables(null,null,null,null);
    }


    @Override
    public void onResume(){
        super.onResume();
//        txtNotProcess.setCompoundDrawables(null,null,null,img);
//        txtInProcess.setCompoundDrawables(null,null,null, null);
//        txtCompleted.setCompoundDrawables(null,null,null,null);

    }

    @Override
    public void onPause() {
        super.onPause();
//        txtNotProcess.setCompoundDrawables(null,null,null,img);
//        txtInProcess.setCompoundDrawables(null,null,null, null);
//        txtCompleted.setCompoundDrawables(null,null,null,null);
    }
}
