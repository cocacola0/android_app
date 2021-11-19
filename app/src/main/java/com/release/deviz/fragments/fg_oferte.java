package com.release.deviz.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.release.deviz.adapters.ClientiOferteAdapter;
import com.release.deviz.MainActivity;
import com.release.deviz.dataClasses.util_functions_produs;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.adapters.ProduseOferteAdapter;
import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_client;
import com.release.deviz.dataClasses.data_class_cont;
import com.release.deviz.dataClasses.data_class_delegat;
import com.release.deviz.dataClasses.data_class_produs;
import com.release.deviz.dataClasses.data_class_extended_produs;
import com.release.deviz.databaseHandler.SharedPrefferencesHandler;
import com.release.deviz.generateDocument.Facturare;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class fg_oferte extends Fragment
{
    Spinner sp_clienti, sp_produse;
    ListView lst_clienti, lst_produse;

    ArrayList<data_class_client> free_clienti, used_clienti;
    ArrayList<data_class_extended_produs> free_produse, used_produse;

    ClientiOferteAdapter clienti_adapter;
    ProduseOferteAdapter produse_adapter;

    ArrayAdapter<String> sp_adapter_clienti;
    ArrayAdapter<String> sp_adapter_produse;

    SharedPrefferencesHandler shared_pref_handler;
    MySqlliteDBHandler sql_db_handler;

    util_functions_produs util_produs;

    Button b_trimite;

    Boolean factura = false;

    public fg_oferte() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if(factura)
            ((MainActivity)getActivity()).change_toolbar(R.string.facturi);
        else
            ((MainActivity)getActivity()).change_toolbar(R.string.oferte);
    }

    public fg_oferte(boolean facturi)
    {
        this.factura = facturi;
    }

    public static fg_oferte newInstance() {
        return new fg_oferte();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
            if(savedInstanceState.containsKey("argument"))
                Log.d("Argument check", savedInstanceState.getString("argument"));
            else
                Log.d("Argument check", "missing");


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("argument","argument");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View r_view = inflater.inflate(R.layout.fg_oferte, container, false);

        shared_pref_handler = new SharedPrefferencesHandler(getContext());
        util_produs = new util_functions_produs();

        if(check_exits()) {
            init(r_view);
            get_data_from_db();

            init_lst_cl();
            init_lst_pr();

            init_spinner_cl();
            init_spinner_pr();

            b_trimite();
        }

        return r_view;
    }

    void init(View r_view)
    {
        sp_clienti = r_view.findViewById(R.id.fg_of_sp_clienti);
        sp_produse = r_view.findViewById(R.id.fg_of_sp_produse);

        lst_clienti = r_view.findViewById(R.id.fg_of_list_clienti);
        lst_produse = r_view.findViewById(R.id.fg_of_list_produse);

        b_trimite = r_view.findViewById(R.id.fg_of_btn_trimite);

        if(factura) {
            b_trimite.setText("Trimite factură");
        }
    }

    void get_data_from_db()
    {
        sql_db_handler = new MySqlliteDBHandler(getContext(), "produse");
        free_produse = sql_db_handler.get_extended_items_from_table_produse();

        sql_db_handler = new MySqlliteDBHandler(getContext(), "clienti");
        free_clienti = sql_db_handler.get_items_from_table_clients();
    }

    boolean check_exits()
    {
        if(!shared_pref_handler.check_bool("cont"))
        {
            Toast.makeText(getContext(), "Introdu date cont!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).start_fg("contul_meu");

            return false;
        }

        if(!shared_pref_handler.check_bool("delegati"))
        {
            Toast.makeText(getContext(), "Introdu date delegat!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).start_fg("delegati");

            return false;
        }

        if(!shared_pref_handler.check_bool("produse"))
        {
            Toast.makeText(getContext(), "Introdu produse!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).start_fg("produse");

            return false;
        }

        if(!shared_pref_handler.check_bool("clienti"))
        {
            Toast.makeText(getContext(), "Introdu clienti!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).start_fg("clienti");

            return false;
        }
        return true;
    }

    void insert_item_in_clienti_listview(int pos)
    {
        data_class_client new_client = free_clienti.get(pos);

        if(!used_clienti.contains(new_client))
        {
            used_clienti.add(free_clienti.get(pos));
            free_clienti.remove(pos);

            clienti_adapter.notifyDataSetChanged();
        }
    }

    void insert_item_in_products_listview(int pos)
    {
        data_class_extended_produs new_prod = free_produse.get(pos);

        if(!used_produse.contains(new_prod))
        {
            used_produse.add(free_produse.get(pos));
            free_produse.remove(pos);

            produse_adapter.notifyDataSetChanged();
        }
    }

    void init_spinner_cl()
    {
        sp_adapter_clienti = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

        sp_adapter_clienti.add("Selectează");
        sp_adapter_clienti.addAll(util_produs.getListNume_client(free_clienti));
        sp_adapter_clienti.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        sp_clienti.setAdapter(sp_adapter_clienti);

        sp_clienti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position != 0)
                {
                    sp_adapter_clienti.remove(free_clienti.get(position - 1).getDenumire());
                    sp_adapter_clienti.notifyDataSetChanged();

                    insert_item_in_clienti_listview(position - 1);

                    sp_clienti.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void init_spinner_pr()
    {
        sp_adapter_produse = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

        sp_adapter_produse.add("Selectează");
        sp_adapter_produse.addAll(util_produs.getListNume_prod(free_produse));
        sp_adapter_produse.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        sp_produse.setAdapter(sp_adapter_produse);

        sp_produse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view != null) {
                    if (position != 0)
                    {
                        sp_adapter_produse.remove(free_produse.get(position - 1).getNume());
                        sp_adapter_produse.notifyDataSetChanged();

                        insert_item_in_products_listview(position - 1);

                        sp_produse.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void init_lst_cl()
    {
        used_clienti = new ArrayList<data_class_client>();
        clienti_adapter = new ClientiOferteAdapter(getContext(), used_clienti);

        lst_clienti.setAdapter(clienti_adapter);

        lst_clienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(view.getId() == R.id.of_cl_img)
                {
                    data_class_client client = used_clienti.get(position);

                    sp_adapter_clienti.insert(client.getDenumire(), 1);
                    free_clienti.add(client);

                    used_clienti.remove(position);

                    clienti_adapter.notifyDataSetChanged();
                    sp_adapter_clienti.notifyDataSetChanged();
                }
            }
        });
    }

    void init_lst_pr()
    {
        used_produse = new ArrayList<data_class_extended_produs>();

        produse_adapter = new ProduseOferteAdapter(getContext(), used_produse);

        lst_produse.setAdapter(produse_adapter);

        lst_produse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(view.getId() == R.id.of_prod_delete)
                {
                    data_class_extended_produs prod = used_produse.get(position);

                    sp_adapter_produse.insert(prod.getNume(), 1);
                    free_produse.add(prod);

                    used_produse.remove(position);

                    produse_adapter.notifyDataSetChanged();
                    sp_adapter_produse.notifyDataSetChanged();
                }
            }
        });
    }

    void b_trimite()
    {
        b_trimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(used_clienti.size() == 0)
                {
                    Toast.makeText(getContext(), "Introduceți clienti!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(used_produse.size() == 0)
                {
                    Toast.makeText(getContext(), "Introduceți produse!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(data_class_client client:used_clienti)
                {
                    try {
                        launch_factura(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    String get_name_from_current_time()
    {
        String pattern = "dd_MM_yyyy_hh_mm_ss";

        if(factura)
            return "factura_" + new SimpleDateFormat(pattern).format(new Date()) + ".pdf";
        else
            return "oferta_" + new SimpleDateFormat(pattern).format(new Date()) + ".pdf";

    }

    void launch_factura(data_class_client client) throws IOException {
        sql_db_handler = new MySqlliteDBHandler(getContext(), "cont");
        data_class_cont cont = sql_db_handler.get_cont_from_table();
        String doc_name = get_name_from_current_time();

        Facturare f;
        if(!factura){
            f = new Facturare(getContext(), doc_name, client, cont, used_produse);
        }
        else {
            sql_db_handler = new MySqlliteDBHandler(getContext(), "delegati");
            data_class_delegat delegat = sql_db_handler.get_delegat_from_table();
            int nr_factura = shared_pref_handler.check_int("nr_factura");

            f = new Facturare(getContext(), doc_name, client, cont, delegat, used_produse, nr_factura);

            shared_pref_handler.iterate_int("nr_factura");
        }

        //Save to DB
        sql_db_handler = new MySqlliteDBHandler(getContext(), "facturi");

        if(sql_db_handler.insert_data(f.output_factura_info))
            ((MainActivity)getActivity()).start_fg_with_args("pdf_viewer", f.output_factura_info);
        else
            Toast.makeText(getContext(), "eroare!", Toast.LENGTH_SHORT).show();

    }
}