package com.release.deviz;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;


public class fg_clienti extends Fragment {

    Button b_adauga, b_sterge, b_modifica;
    ExpandableListView exp_lst;
    ArrayList<data_class_client> list;
    ClientiExpandableAdapter adapter;
    View previous_selected;
    int selected_index = -1;

    public fg_clienti() {
        // Required empty public constructor
    }


    public static fg_clienti newInstance() {
        return new fg_clienti();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).change_toolbar(R.string.adauga_client);
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

        exp_lst(r_view);

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

    private void b_modifica(View r_view)
    {
        b_modifica = r_view.findViewById(R.id.btn_modifica_client);

        b_modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(selected_index != -1)
                {
                    ((MainActivity)getActivity()).start_fg_with_args("modifica_client",list.get(selected_index));
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
        b_sterge = r_view.findViewById(R.id.btn_sterge_client);

        b_sterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_index != -1)
                {
                    MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "clienti");
                    boolean result = db_handler.delete_client_data(list.get(selected_index));

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

    private void exp_lst(View r_view)
    {
        exp_lst = r_view.findViewById(R.id.exp_lst_clienti);

        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "clienti");

        list = db_handler.get_items_from_table_clients();
        adapter = new ClientiExpandableAdapter(getContext(), list);

        exp_lst.setAdapter(adapter);

        exp_lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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