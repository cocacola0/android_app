package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fg_rapoarte extends Fragment {
    public fg_rapoarte() {
        // Required empty public constructor
    }

    public static fg_rapoarte newInstance() {
        return new fg_rapoarte();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fg_rapoarte, container, false);
    }
}