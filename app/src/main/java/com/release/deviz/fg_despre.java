package com.release.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class fg_despre extends Fragment {

    public fg_despre() {
        // Required empty public constructor
    }

    public static fg_despre newInstance() {
        return new fg_despre();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).change_toolbar(R.string.despre);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View r_view = inflater.inflate(R.layout.fg_despre, container, false);

        return r_view;
    }
}