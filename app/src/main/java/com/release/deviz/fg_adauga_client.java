package com.release.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class fg_adauga_client extends Fragment
{
    Button b_adauga_client;
    EditText etxt_denumire, etxt_cif, etxt_reg_com, etxt_localitate, etxt_judet, etxt_adresa, etxt_email, etxt_pers_contact, etxt_telefon;
    CheckBox cb_plat_tva_da, cb_plat_tva_nu;
    boolean plat_tva_da, plat_tva_nu;

    data_class_client client;
    boolean modifica = false;

    public fg_adauga_client() {
        // Required empty public constructor
    }

    public fg_adauga_client(data_class_client client)
    {
        this.client = client;
        this.modifica = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).change_toolbar(R.string.adauga_client);
    }

    public static fg_adauga_client newInstance(){
        return new fg_adauga_client();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View r_view = inflater.inflate(R.layout.fg_adauga_client, container, false);

        init(r_view);
        b_adauga_client(r_view);
        cb_plat_tva_da(r_view);
        cb_plat_tva_nu(r_view);

        return r_view;
    }

    data_class_client get_client_info()
    {
        String denumire = etxt_denumire.getText().toString();
        String cif = etxt_cif.getText().toString();
        String reg_com = etxt_reg_com.getText().toString();
        String localitate = etxt_localitate.getText().toString();
        String judetul = etxt_judet.getText().toString();
        String adresa = etxt_adresa.getText().toString();
        String email = etxt_email.getText().toString();
        String pers_contact = etxt_pers_contact.getText().toString();
        String telefon = etxt_telefon.getText().toString();

        Utils u = new Utils(getContext());

        if(u.check_string_non_empty(denumire, "denumire") && u.check_string_non_empty(cif, "cif") &&
            u.check_string_non_empty(reg_com, "reg_com") && u.check_string_non_empty(localitate, "localitate") &&
            u.check_string_non_empty(judetul, "judetul") && u.check_string_non_empty(adresa, "adresa") &&
            u.check_string_non_empty(email, "email") && u.check_string_non_empty(pers_contact, "pers contact") &&
            u.check_string_non_empty(telefon, "telefon") && (plat_tva_nu || plat_tva_da))

            return new data_class_client(denumire, cif, reg_com, plat_tva_da, localitate, judetul, adresa, email, pers_contact, telefon);

        return null;
    }

    void b_adauga_client(View r_view)
    {
        b_adauga_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(!modifica)
                    adauga_client();
                else
                    modifica_client();
            }
        });
    }

    void adauga_client()
    {
        data_class_client item = get_client_info();

        if(item != null) {
            MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "clienti");

            boolean succeded = db_handler.insert_client_data(item);

            if (succeded) {
                Toast.makeText(getContext(), "Inserare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("clienti");

            } else
                Toast.makeText(getContext(), "Inserare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

    void modifica_client()
    {
        data_class_client item = get_client_info();

        if(item != null) {
            MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "clienti");
            Log.d("DB", "CALLING");
            boolean succeded = db_handler.modify_client_data(client, item);

            if (succeded) {
                Toast.makeText(getContext(), "Modificare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("clienti");

            } else
                Toast.makeText(getContext(), "Inserare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

    void cb_plat_tva_da(View r_view)
    {
        cb_plat_tva_da.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                plat_tva_da = true;
                plat_tva_nu = false;
                cb_plat_tva_nu.setChecked(false);
            }
        });
    }

    void cb_plat_tva_nu(View r_view)
    {
        cb_plat_tva_nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plat_tva_nu = true;
                plat_tva_da = false;
                cb_plat_tva_da.setChecked(false);
            }
        });
    }

    void init(View r_view)
    {
        etxt_denumire = r_view.findViewById(R.id.etxt_denumire);
        etxt_cif = r_view.findViewById(R.id.etxt_cif);
        etxt_reg_com = r_view.findViewById(R.id.etxt_reg_com);
        etxt_localitate = r_view.findViewById(R.id.etxt_localitate);
        etxt_judet = r_view.findViewById(R.id.etxt_judetul);
        etxt_adresa = r_view.findViewById(R.id.etxt_adresa);
        etxt_email = r_view.findViewById(R.id.etxt_email);
        etxt_pers_contact = r_view.findViewById(R.id.etxt_pers_contact);
        etxt_telefon = r_view.findViewById(R.id.etxt_telefon);

        cb_plat_tva_da = r_view.findViewById(R.id.plat_tva_da);
        cb_plat_tva_nu = r_view.findViewById(R.id.plat_tva_nu);

        b_adauga_client = r_view.findViewById(R.id.btn_adauga_client);

        if(modifica)
        {
            etxt_denumire.setText(client.getDenumire());
            etxt_cif.setText(client.getCif());
            etxt_reg_com.setText(client.getReg_com());
            etxt_localitate.setText(client.getLocalitate());
            etxt_judet.setText(client.getJudet());
            etxt_adresa.setText(client.getAdresa());
            etxt_email.setText(client.getEmail());
            etxt_pers_contact.setText(client.getPers_contact());
            etxt_telefon.setText(client.getTelefon());

            if(client.isPlatitor_tva()) {
                cb_plat_tva_da.setChecked(true);
                plat_tva_da = true;
            }
            else {
                cb_plat_tva_nu.setChecked(true);
                plat_tva_nu = true;
            }

            b_adauga_client.setText(R.string.modifica_client);
        }
    }
}