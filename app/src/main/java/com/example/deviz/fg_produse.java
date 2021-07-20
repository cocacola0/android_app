package com.example.deviz;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class fg_produse extends Fragment
{
    Button b_adauga, b_sterge, b_modifica;
    ListView lst_view;
    ArrayList<data_class_produs> list;
    ProduseAdapter adapter;
    View previous_selected;
    int selected_index = -1;

    public fg_produse() {
        // Required empty public constructor
    }

    public static fg_produse newInstance() {
        return new fg_produse();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View r_view = inflater.inflate(R.layout.fg_produse, container, false);

        lst_view(r_view);
        b_adauga(r_view);
        b_modifica(r_view);
        b_sterge(r_view);

        return r_view;
    }

    private void b_adauga(View r_view)
    {
        b_adauga = r_view.findViewById(R.id.btn_adauga_produse);

        b_adauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).start_fg("adauga_produs");
            }
        });
    }

    private void b_modifica(View r_view)
    {
        b_modifica = r_view.findViewById(R.id.btn_modifica_produse);

        b_modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(selected_index != -1)
                {
                    ((MainActivity)getActivity()).start_fg_with_args("modifica_produs",list.get(selected_index));
                }
                else
                {
                    Toast.makeText(getContext(), "Nici un produs selectat!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void b_sterge(View r_view)
    {
        b_sterge = r_view.findViewById(R.id.btn_sterge_produse);

        b_sterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(selected_index != -1)
                {
                    MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "produse");
                    boolean result = db_handler.delete_product_data(list.get(selected_index));

                    if(result)
                    {
                        list.remove(selected_index);
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Nici un produs selectat!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void lst_view(View r_view)
    {
        lst_view = r_view.findViewById(R.id.lst_prod_produse);

        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "produse");

        list = db_handler.get_items_from_table();
        adapter = new ProduseAdapter(getContext(), list);

        lst_view.setAdapter(adapter);

        lst_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(previous_selected != null)
                    previous_selected.setBackgroundColor(Color.parseColor("#FFFFFF"));

                if(previous_selected == view)
                {
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    previous_selected = null;
                    selected_index = -1;
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#ADD8E6"));
                    previous_selected = view;
                    selected_index = position;
                }

                return true;
            }
        });
    }
}