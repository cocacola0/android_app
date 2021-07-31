package com.release.deviz;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class fg_rapoarte extends Fragment
{
    Button b_adauga, b_sterge, b_modifica;
    ListView lst_view;
    ArrayList<data_class_facturi> list;
    RapoarteAdapter adapter;
    View previous_selected;
    int selected_index = -1;

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).change_toolbar(R.string.rapoarte);
    }


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
        View r_view = inflater.inflate(R.layout.fg_rapoarte, container, false);

        lst_view(r_view);
        b_sterge(r_view);

        return r_view;
    }

    void open_pdf(data_class_facturi factura)
    {

    }

    void lst_view(View r_view)
    {
        lst_view = r_view.findViewById(R.id.rapoarte_lista_facturi);

        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "facturi");

        list = db_handler.get_facturi_from_table();
        adapter = new RapoarteAdapter(getContext(), list);

        lst_view.setAdapter(adapter);

        lst_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                open_pdf(list.get(position));
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

                if(view.getId() == R.id.btn_rapoarte)
                {
                    open_pdf(list.get(position));
                }

                return true;
            }
        });
    }

    void b_sterge(View r_view)
    {
        Button b_sterge = r_view.findViewById(R.id.btn_rapoarte_sterge);

        b_sterge.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selected_index != -1)
                {
                    MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "facturi");
                    boolean result = db_handler.delete_facturi_data(list.get(selected_index));

                    if(result)
                    {
                        list.remove(selected_index);
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Nici o factură selectată!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}