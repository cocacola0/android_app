package com.release.deviz.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.release.deviz.MainActivity;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.R;
import com.release.deviz.adapters.RapoarteAdapter;
import com.release.deviz.dataClasses.data_class_facturi;

import java.io.IOException;
import java.util.ArrayList;

public class fg_rapoarte extends Fragment
{
    ListView lst_view;
    ArrayList<data_class_facturi> list;
    RapoarteAdapter adapter;

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

        return r_view;
    }

    void open_pdf(data_class_facturi factura) throws IOException {
        ((MainActivity)getActivity()).start_fg_with_args("pdf_viewer",factura);
    }

    void lst_view(View r_view)
    {
        lst_view = r_view.findViewById(R.id.rapoarte_lista_facturi);

        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "facturi");

        list = db_handler.get_facturi_from_table();
        adapter = new RapoarteAdapter(getContext(), list);

        lst_view.setAdapter(adapter);

        lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    open_pdf(list.get(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}