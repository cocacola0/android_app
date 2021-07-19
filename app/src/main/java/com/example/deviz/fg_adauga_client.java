package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fg_adauga_client extends Fragment
{
    Button b_adauga_client;

    public fg_adauga_client() {
        // Required empty public constructor
    }

    public static fg_adauga_client newInstance(){
        return new fg_adauga_client();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View r_view = inflater.inflate(R.layout.fg_adauga_client, container, false);

        b_adauga_client(r_view);

        return r_view;
    }

    void b_adauga_client(View r_view)
    {
        b_adauga_client = r_view.findViewById(R.id.btn_adauga_client);


    }
}