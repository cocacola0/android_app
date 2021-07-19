package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class fg_facturi extends Fragment {
    public fg_facturi() {
        // Required empty public constructor
    }

    public static fg_facturi newInstance() {
        return new fg_facturi();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_facturi, container, false);
    }
}