package com.release.deviz.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.release.deviz.MainActivity;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_delegat;
import com.release.deviz.databaseHandler.SharedPrefferencesHandler;


public class fg_delegati extends Fragment {
    Button b_salveaza;
    EditText etxt_nume_del, etxt_cnp_del, etxt_ci_del, etxt_mij_transport_del, etxt_nr_transport;

    SharedPrefferencesHandler shared_pref_handler;
    MySqlliteDBHandler sql_db_handler;

    data_class_delegat delegat;
    boolean exists = false;

    public fg_delegati() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).change_toolbar(R.string.delegati);
    }

    public static fg_delegati newInstance(String param1, String param2) {
        return new fg_delegati();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View r_view =  inflater.inflate(R.layout.fg_delegati, container, false);

        shared_pref_handler = new SharedPrefferencesHandler(getContext());
        sql_db_handler = new MySqlliteDBHandler(getContext(), "delegati");

        if(shared_pref_handler.check_bool("delegati"))
        {
            exists = true;
            delegat = sql_db_handler.get_delegat_from_table();
        }

        init(r_view);
        b_salveaza(r_view);

        return r_view;
    }

    void init(View r_view)
    {
        etxt_nume_del = r_view.findViewById(R.id.fg_del_nume);
        etxt_cnp_del = r_view.findViewById(R.id.fg_del_cnp);
        etxt_ci_del = r_view.findViewById(R.id.fg_del_ci_delegat);
        etxt_mij_transport_del = r_view.findViewById(R.id.fg_del_mij_transport);
        etxt_nr_transport = r_view.findViewById(R.id.fg_del_nr_transport);

        b_salveaza = r_view.findViewById(R.id.fg_del_salveaza);

        if(exists)
        {
            etxt_nume_del.setText(delegat.getNume());
            etxt_cnp_del.setText(delegat.getCnp());
            etxt_ci_del.setText(delegat.getCi());
            etxt_mij_transport_del.setText(delegat.getMij_transport());
            etxt_nr_transport.setText(delegat.getNr());
        }
    }

    data_class_delegat get_delegat_info()
    {
        String nume_del = etxt_nume_del.getText().toString();
        String cnp_del = etxt_cnp_del.getText().toString();
        String ci_del = etxt_ci_del.getText().toString();
        String mij_transport = etxt_mij_transport_del.getText().toString();
        String nr_transport = etxt_nr_transport.getText().toString();

        return new data_class_delegat(nume_del, cnp_del, ci_del, mij_transport, nr_transport);
    }

    void b_salveaza(View r_view)
    {
        b_salveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!exists) {
                    adauga_delegat();
                    ((MainActivity) getActivity()).start_fg("abonament");
                }
                else
                    modifica_delegat();
            }
        });
    }

    void adauga_delegat()
    {
        data_class_delegat item = get_delegat_info();

        if(item != null) {
            boolean succeded = sql_db_handler.insert_data(item);

            if (succeded) {
                Toast.makeText(getContext(), "Inserare reușită!", Toast.LENGTH_SHORT).show();

                shared_pref_handler.put_bool("delegati");
                ((MainActivity) getActivity()).start_fg("acasa");

            } else
                Toast.makeText(getContext(), "Inserare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

    void modifica_delegat()
    {
        data_class_delegat item = get_delegat_info();

        if(item != null) {
            boolean succeded = sql_db_handler.modify_data(delegat, item);

            if (succeded) {
                Toast.makeText(getContext(), "Modificare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("acasa");

            } else
                Toast.makeText(getContext(), "Modificare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

}