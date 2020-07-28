package com.example.android_skripsi.Slider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android_skripsi.R;

public class FragmentSlider extends Fragment {

    private static final String ARG_PARAM1 = "params";

    private String imageUrls;

    public FragmentSlider() {
    }

//    public static FragmentSlider newInstance(String params) {
//        FragmentSlider fragment = new FragmentSlider();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, params);
//        fragment.setArguments(args);
//        return fragment;
//    }
    int imageid;
    // static method to create the MyImageSlider Fragment containing image
    public  static FragmentSlider newInstance(int id)
    {
        FragmentSlider slider = new FragmentSlider();
        Bundle b = new Bundle();
        b.putInt("imageid", id);
        slider.setArguments(b);
        return slider;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageid=getArguments().getInt("imageid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageUrls = getArguments().getString(ARG_PARAM1);
        View view = inflater.inflate(R.layout.fragment_slider_item, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.img);
//        Glide.with(getActivity())
//                .load(imageUrls)
//                .load(R.drawable.image_slider_1)
//                .into(img);
        img.setImageResource(imageid);
        return view;
    }
}
