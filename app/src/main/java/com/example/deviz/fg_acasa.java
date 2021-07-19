package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fg_acasa extends Fragment
{
    Button b_oferte, b_facturi, b_produse, b_rapoarte, b_clienti, b_furnizori;

    public fg_acasa() {
        // Required empty public constructor
    }

    public static fg_acasa newInstance() {
        return new fg_acasa();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View r_view = inflater.inflate(R.layout.fg_acasa, container, false);

        b_oferte = r_view.findViewById(R.id.btn_oferte);
        b_facturi = r_view.findViewById(R.id.btn_facturi);
        b_produse = r_view.findViewById(R.id.btn_produse);
        b_rapoarte = r_view.findViewById(R.id.btn_rapoarte);
        b_clienti = r_view.findViewById(R.id.btn_clienti);
        b_furnizori = r_view.findViewById(R.id.btn_furnizori);

        b_oferte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("oferte");
            }
        });

        b_facturi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("facturi");
            }
        });

        b_produse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("produse");
            }
        });

        b_rapoarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("rapoarte");
            }
        });

        b_clienti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("clienti");
            }
        });

        b_furnizori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("furnizori");
            }
        });

        return  r_view;
    }



}