package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class fg_furnizori extends Fragment {

    public fg_furnizori() {
        // Required empty public constructor
    }

    public static fg_furnizori newInstance() {
        fg_furnizori fragment = new fg_furnizori();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fg_furnizori, container, false);
    }
}