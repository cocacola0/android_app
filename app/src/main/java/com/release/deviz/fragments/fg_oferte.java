package com.release.deviz.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.adapters.ProduseOferteAdapter;
import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_client;
import com.release.deviz.dataClasses.data_class_cont;
import com.release.deviz.dataClasses.data_class_delegat;
import com.release.deviz.dataClasses.data_class_produs;
import com.release.deviz.dataClasses.data_class_produs_pdf;
import com.release.deviz.databaseHandler.SharedPrefferencesHandler;
import com.release.deviz.generateDocument.Facturare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class fg_oferte extends Fragment
{
    Spinner sp_clienti, sp_produse;
    ListView lst_clienti, lst_produse;

    ArrayList<data_class_client> clienti, curr_clienti;
    ArrayList<data_class_produs> produse, curr_produse;

    ArrayList<String> clienti_nume, produse_nume;
    ArrayList<Integer> bucati = new ArrayList<>();

    ClientiOferteAdapter clienti_adapter;
    ProduseOferteAdapter produse_adapter;
    ArrayAdapter<CharSequence> cl_sp_adapter;

    SharedPrefferencesHandler shared_pref_handler;
    MySqlliteDBHandler sql_db_handler;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View r_view = inflater.inflate(R.layout.fg_oferte, container, false);

        shared_pref_handler = new SharedPrefferencesHandler(getContext());

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
        produse = sql_db_handler.get_items_from_table_produse();

        sql_db_handler = new MySqlliteDBHandler(getContext(), "clienti");
        clienti = sql_db_handler.get_items_from_table_clients();

        clienti_nume = get_clienti_nume();
        produse_nume = get_produse_nume();
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

    ArrayList<String> get_clienti_nume()
    {
        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<clienti.size(); i++)
            list.add(clienti.get(i).getDenumire());

        return list;
    }

    ArrayList<String> get_produse_nume()
    {
        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<produse.size(); i++)
            list.add(produse.get(i).getNume());

        return list;
    }

    void insert_item_in_client_listview(int pos)
    {
        if(pos==0)
            return;

        String nume = cl_sp_adapter.getItem(pos).toString();
        int position = pos;
        for(int i=0; i<clienti.size(); i++)
            if(nume.equals(clienti.get(i).getDenumire())) {
                position = i;
                break;
            }

        if(!curr_clienti.contains(clienti.get(position))) {
            curr_clienti.add(clienti.get(position));
            clienti_adapter.notifyDataSetChanged();

            cl_sp_adapter.remove(clienti.get(position).getDenumire());
            sp_clienti.setSelection(0);
            cl_sp_adapter.notifyDataSetChanged();
        }
    }

    void insert_item_in_products_listview(int pos)
    {
        if(!curr_produse.contains(produse.get(pos))) {
            bucati.add(1);
            curr_produse.add(produse.get(pos));
            produse_adapter.notifyDataSetChanged();
        }
    }

    void init_spinner_cl()
    {
        cl_sp_adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);

        cl_sp_adapter.add("Selectează");
        cl_sp_adapter.addAll(clienti_nume);
        cl_sp_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_clienti.setAdapter(cl_sp_adapter);

        sp_clienti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                insert_item_in_client_listview(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void init_spinner_pr()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

        adapter.add("Selectează");
        adapter.addAll(produse_nume);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_produse.setAdapter(adapter);

        sp_produse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    insert_item_in_products_listview(position-1);

                sp_produse.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void init_lst_cl()
    {
        curr_clienti = new ArrayList<data_class_client>();
        clienti_adapter = new ClientiOferteAdapter(getContext(), curr_clienti);

        lst_clienti.setAdapter(clienti_adapter);

        lst_clienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(view.getId() == R.id.of_cl_img)
                {
                    cl_sp_adapter.add(curr_clienti.get(position).getDenumire());
                    curr_clienti.remove(position);
                }

                clienti_adapter.notifyDataSetChanged();
            }
        });
    }

    void init_lst_pr()
    {
        curr_produse = new ArrayList<data_class_produs>();
        produse_adapter = new ProduseOferteAdapter(getContext(), curr_produse, bucati, factura);
        lst_produse.setAdapter(produse_adapter);

        lst_produse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int nr_buc = bucati.get(position);

                if(view.getId() == R.id.of_prod_img_minus)
                {
                    if(nr_buc == 1)
                    {
                        bucati.remove(position);
                        curr_produse.remove(position);
                        produse_adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        bucati.set(position, nr_buc-1);
                    }
                }
                else
                    if(view.getId() == R.id.of_prod_img_add) {
                        bucati.set(position, nr_buc + 1);
                    }

                produse_adapter.notifyDataSetChanged();
            }
        });
    }

    void b_trimite()
    {
        b_trimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(curr_clienti.size() == 0)
                {
                    Toast.makeText(getContext(), "Introduceți produse!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(curr_produse.size() == 0)
                {
                    Toast.makeText(getContext(), "Introduceți clienti!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<data_class_produs_pdf> list_produse = new ArrayList<>();

                for(int i = 0; i<curr_produse.size(); i++)
                    list_produse.add(new data_class_produs_pdf(curr_produse.get((i)),bucati.get(i)));

                for(data_class_client client:curr_clienti)
                {
                    launch_factura(list_produse, client);
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

    void launch_factura(ArrayList<data_class_produs_pdf> list_produse, data_class_client client)
    {
        sql_db_handler = new MySqlliteDBHandler(getContext(), "cont");
        data_class_cont cont = sql_db_handler.get_cont_from_table();
        String doc_name = get_name_from_current_time();

        Facturare f;
        if(!factura){
            f = new Facturare(getContext(), doc_name, client, cont, list_produse);
        }
        else {
            sql_db_handler = new MySqlliteDBHandler(getContext(), "delegati");
            data_class_delegat delegat = sql_db_handler.get_delegat_from_table();
            int nr_factura = shared_pref_handler.check_int("nr_factura");

            f = new Facturare(getContext(), doc_name, client, cont, delegat, list_produse, nr_factura);

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