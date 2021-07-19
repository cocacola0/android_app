package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class fg_clienti extends Fragment {

    Button b_adauga, b_sterge, b_modifica;

    public fg_clienti() {
        // Required empty public constructor
    }


    public static fg_clienti newInstance() {
        return new fg_clienti();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View r_view = inflater.inflate(R.layout.fg_clienti, container, false);

        b_adauga(r_view);
        b_modifica(r_view);
        b_sterge(r_view);

        return r_view;
    }

    private void b_adauga(View r_view)
    {
        b_adauga = r_view.findViewById(R.id.btn_adauga_client);

        b_adauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).start_fg("adauga_client");
            }
        });
    }

    void b_modifica(View r_view)
    {

    }

    void b_sterge(View r_view)
    {

    }
}