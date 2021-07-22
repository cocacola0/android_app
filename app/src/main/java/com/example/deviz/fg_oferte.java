package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import java.util.Calendar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    Button b_trimite;

    Boolean cl_first = false, pr_first = false;
    Boolean factura = false;

    String discount;

    public fg_oferte() {
        // Required empty public constructor
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

        init(r_view);
        get_data_from_db();

        init_lst_cl();
        init_lst_pr();

        init_spinner_cl();
        init_spinner_pr();

        b_trimite();

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
        MySqlliteDBHandler db_handler;

        db_handler = new MySqlliteDBHandler(getContext(), "produse");
        produse = db_handler.get_items_from_table();

        db_handler = new MySqlliteDBHandler(getContext(), "clienti");
        clienti = db_handler.get_items_from_table_clients();

        clienti_nume = get_clienti_nume();
        produse_nume = get_produse_nume();
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
        if(!curr_clienti.contains(clienti.get(pos))) {
            curr_clienti.add(clienti.get(pos));
            clienti_adapter.notifyDataSetChanged();
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
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);

        adapter.add("Selectează");
        adapter.addAll(clienti_nume);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_clienti.setAdapter(adapter);

        sp_clienti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position != 0)
                    insert_item_in_client_listview(position-1);

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
                    curr_clienti.remove(position);
                    clienti_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    void init_lst_pr()
    {
        curr_produse = new ArrayList<data_class_produs>();
        produse_adapter = new ProduseOferteAdapter(getContext(), curr_produse, factura);

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
                    if(view.getId() == R.id.of_prod_img_add)
                    {
                        bucati.set(position, nr_buc+1);
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
                if(curr_clienti.size() == 0)
                    Toast.makeText(getContext(), "Introduceți produse!" , Toast.LENGTH_SHORT).show();
                else
                    if(curr_produse.size() == 0)
                        Toast.makeText(getContext(), "Introduceți clienti!" , Toast.LENGTH_SHORT).show();
                    else
                        for(data_class_client client:curr_clienti)
                        {
                            Facturare fac = new Facturare(getContext(), client, curr_produse, bucati);
                            fac.get_oferta_pdf(factura);

                            ((MainActivity)getActivity()).start_fg("acasa");
                        }
            }
        });
    }
}