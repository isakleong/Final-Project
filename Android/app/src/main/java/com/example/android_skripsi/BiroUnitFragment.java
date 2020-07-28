package com.example.android_skripsi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;


/**
 * A simple {@link Fragment} subclass.
 */
public class BiroUnitFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View v;
    RecyclerView rvBiroUnit;

    public BiroUnitFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_biro_unit, container, false);
        return v;

        //get data list of biro unit

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
