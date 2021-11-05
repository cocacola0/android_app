package com.release.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class fg_delegati extends Fragment {
    Button b_salveaza;
    EditText etxt_nume_del, etxt_cnp_del, etxt_ci_del, etxt_mij_transport_del, etxt_nr_transport;

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

        if(check_if_exists())
        {
            exists = true;
            delegat = get_delegat();
        }

        init(r_view);
        b_salveaza(r_view);

        return r_view;
    }

    private boolean check_if_exists()
    {
        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "delegati");

        return db_handler.check_delegat_exists();
    }

    data_class_delegat get_delegat()
    {
        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "delegati");
        return db_handler.get_delegat_from_table();
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

        /*
        Utils u = new Utils(getContext());

        if(u.check_string_non_empty(nume_del, "Nume delegat") && u.check_string_non_empty(cnp_del, "CNP delegat")
            && u.check_string_non_empty(ci_del, "CI delegat") && u.check_string_non_empty(mij_transport, "Mijloc transport") &&
            u.check_string_non_empty(nr_transport, "Nr Transport"))
            return new data_class_delegat(nume_del, cnp_del, ci_del, mij_transport, nr_transport);
        return null;*/
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
            MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "delegati");

            boolean succeded = db_handler.insert_delegat_data(item);

            if (succeded) {
                Toast.makeText(getContext(), "Inserare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("acasa");

            } else
                Toast.makeText(getContext(), "Inserare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

    void modifica_delegat()
    {
        data_class_delegat item = get_delegat_info();

        if(item != null) {
            MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "delegati");

            boolean succeded = db_handler.modify_delegat_data(delegat, item);

            if (succeded) {
                Toast.makeText(getContext(), "Modificare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("acasa");

            } else
                Toast.makeText(getContext(), "Modificare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

}