package com.release.deviz.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.release.deviz.MainActivity;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.R;
import com.release.deviz.adapters.RapoarteAdapter;
import com.release.deviz.dataClasses.data_class_facturi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class fg_rapoarte extends Fragment
{
    Button btn_vezi, btn_sterge, btn_trimite;
    ListView lst_view;
    ArrayList<data_class_facturi> list;
    RapoarteAdapter adapter;
    View previous_selected;
    int selected_index = -1;

    MySqlliteDBHandler sql_db_handler;

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
        b_vezi(r_view);
        b_sterge(r_view);
        b_trimite(r_view);

        return r_view;
    }

    private void b_vezi(View r_view)
    {
        btn_vezi = r_view.findViewById(R.id.btn_vezi_rapoarte);

        btn_vezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_index != -1)
                {
                    ((MainActivity)getActivity()).start_fg_with_args("pdf_viewer",list.get(selected_index));
                }
                else
                {
                    Toast.makeText(getContext(), "Nici un document selectat!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void b_sterge(View r_view)
    {
        btn_sterge = r_view.findViewById(R.id.btn_sterge_rapoarte);

        btn_sterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_index != -1)
                {
                    boolean result = sql_db_handler.delete_data(list.get(selected_index));

                    list.remove(selected_index);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getContext(), "Nici un document selectat!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void sendmail()
    {
        String full_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + list.get(selected_index).getNume_fisier();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("application/pdf");
        Uri fileUri = FileProvider.getUriForFile(getContext(), "com.myfileprovider", new File(full_path));

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

        getContext().startActivity(Intent.createChooser(shareIntent, "choose"));
    }

    private void b_trimite(View r_view)
    {
        btn_trimite = r_view.findViewById(R.id.btn_trimite_rapoarte);

        btn_trimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_index != -1)
                {
                    sendmail();
                }
                else
                {
                    Toast.makeText(getContext(), "Nici un document selectat!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void lst_view(View r_view)
    {
        lst_view = r_view.findViewById(R.id.rapoarte_lista_facturi);

        sql_db_handler = new MySqlliteDBHandler(getContext(), "facturi");

        list = sql_db_handler.get_facturi_from_table();
        adapter = new RapoarteAdapter(getContext(), list);

        lst_view.setAdapter(adapter);

        lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (previous_selected != null)
                    previous_selected.setBackgroundColor(Color.parseColor("#FFFFFF"));

                if (previous_selected == view) {
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    previous_selected = null;
                    selected_index = -1;
                } else {
                    view.setBackgroundColor(Color.parseColor("#ADD8E6"));
                    previous_selected = view;
                    selected_index = position;
                }
            }
        });
    }
}