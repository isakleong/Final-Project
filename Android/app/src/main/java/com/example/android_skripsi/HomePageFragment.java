package com.example.android_skripsi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.Slider.FragmentSlider;
import com.example.android_skripsi.Slider.SliderIndicator;
import com.example.android_skripsi.Slider.SliderPagerAdapter;
import com.example.android_skripsi.Slider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment{
    View v;
    RecyclerView rvCategoryJob;
    CardView cv;

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_page, container, false);
//        rvCategoryJob = (RecyclerView) v.findViewById(R.id.rvCategoryJob);
//        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getContext(), 3);
//        rvCategoryJob.setLayoutManager(mLinearLayoutManager);

//        TaskJob tJ = new TaskJob();
//        tJ.setActivity(getActivity());
//        tJ.setMode("getCategory");
//        tJ.doTask();

        sliderView    = (SliderView) v.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) v.findViewById(R.id.pagesContainer);
        cv            = (CardView) v.findViewById(R.id.cardClickAspirasi);

        //click input aspirasi button
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InputAspirasi.class);
                startActivity(intent);
            }
        });
//        setupSlider();

        return v;
    }



    private void setupSlider() {
        sliderView.setDurationScroll(800);
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-1.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));

        ArrayList<Integer> images=new ArrayList<Integer>(){{
            add(R.drawable.carousel);
            add(R.drawable.carousel2);
            add(R.drawable.carousel3);
            add(R.drawable.carousel);
            add(R.drawable.carousel2);
            add(R.drawable.carousel3);
        }};
        List<Fragment> fragments = new ArrayList<>();
        for(int i=0;i<images.size();i++){
            fragments.add(FragmentSlider.newInstance(images.get(i)));
        }
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-1.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));

        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getContext(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }
    @Override
    public void onResume() {
        super.onResume();

    }
}