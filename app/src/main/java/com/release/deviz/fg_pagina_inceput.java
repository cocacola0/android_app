package com.release.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fg_pagina_inceput extends Fragment {
    Button btn_creeaza_cont, btn_despre;

    public fg_pagina_inceput() {
        // Required empty public constructor
    }

    public static fg_pagina_inceput newInstance()
    {
        return new fg_pagina_inceput();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View r_view = inflater.inflate(R.layout.fg_pagina_inceput, container, false);

        btn_creeaza_cont = r_view.findViewById(R.id.btn_fg_incp_creeaza_cont);
        btn_despre = r_view.findViewById(R.id.btn_fg_incp_despre);

        btn_creeaza_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainActivity)getActivity()).start_fg("contul_meu");
            }
        });


        btn_despre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).start_fg("despre");
            }
        });

        return r_view;
    }
}