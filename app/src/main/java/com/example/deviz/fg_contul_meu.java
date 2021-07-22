package com.example.deviz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class fg_contul_meu extends Fragment
{
    Button b_salveaza;
    EditText etxt_denumire, etxt_cif, etxt_reg_com, etxt_cap_social, etxt_localitate, etxt_judet, etxt_adresa, etxt_cod_postal, etxt_telefon, etxt_email, etxt_cont_bancar, etxt_banca, etxt_cota_tva, etxt_tip_tva;
    CheckBox cb_plat_tva_da, cb_plat_tva_nu;
    boolean plat_tva_da, plat_tva_nu;

    data_class_cont cont;
    boolean exists = false;

    public fg_contul_meu() {
        // Required empty public constructor
    }

    public static fg_contul_meu newInstance() {
        return new fg_contul_meu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View r_view =  inflater.inflate(R.layout.fg_contul_meu, container, false);

        if(check_if_exists())
        {
            exists = true;
            cont = get_cont();
        }

        init(r_view);
        cb_plat_tva_da(r_view);
        cb_plat_tva_nu(r_view);
        b_salveaza(r_view);

        return r_view;
    }
    private boolean check_if_exists()
    {
        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "cont");

        return db_handler.check_account_exists();
    }

    data_class_cont get_cont()
    {
        MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "cont");
        return db_handler.get_cont_from_table();
    }

    void init(View r_view)
    {
        etxt_denumire = r_view.findViewById(R.id.fg_cm_denumire);
        etxt_cif = r_view.findViewById(R.id.fg_cm_cif);
        etxt_reg_com = r_view.findViewById(R.id.fg_cm_reg_com);
        etxt_cap_social = r_view.findViewById(R.id.fg_cm_capital_social);
        etxt_localitate = r_view.findViewById(R.id.fg_cm_localitate);
        etxt_judet = r_view.findViewById(R.id.fg_cm_judetul);
        etxt_adresa = r_view.findViewById(R.id.fg_cm_adresa);
        etxt_cod_postal = r_view.findViewById(R.id.fg_cm_cod_postal);
        etxt_telefon = r_view.findViewById(R.id.fg_cm_telefon);
        etxt_email = r_view.findViewById(R.id.fg_cm_email);
        etxt_cont_bancar = r_view.findViewById(R.id.fg_cm_cont_bancar);
        etxt_banca = r_view.findViewById(R.id.fg_cm_banca);
        etxt_cota_tva = r_view.findViewById(R.id.fg_cm_cota_tva);
        etxt_tip_tva = r_view.findViewById(R.id.fg_cm_tip_tva);

        cb_plat_tva_da = r_view.findViewById(R.id.fg_cm_plat_tva_da);
        cb_plat_tva_nu = r_view.findViewById(R.id.fg_cm_plat_tva_nu);

        b_salveaza = r_view.findViewById(R.id.fg_cm_salveaza);

        if(exists)
        {
            etxt_denumire.setText(cont.getDenumire());
            etxt_cif.setText(cont.getCif());
            etxt_reg_com.setText(cont.getReg_com());
            etxt_cap_social.setText(cont.getCap_social());
            etxt_localitate.setText(cont.getLocalitate());
            etxt_judet.setText(cont.getJudet());
            etxt_adresa.setText(cont.getAdresa());
            etxt_cod_postal.setText(cont.getCod_postal());
            etxt_telefon.setText(cont.getTelefon());
            etxt_email.setText(cont.getEmail());
            etxt_cont_bancar.setText(cont.getCont_bancar());
            etxt_banca.setText(cont.getBanca());
            etxt_cota_tva.setText(cont.getCota_tva());
            etxt_tip_tva.setText(cont.getTip_tva());

            if(cont.isPlatitor_tva()) {
                cb_plat_tva_da.setChecked(true);
                plat_tva_da = true;
            }
            else {
                cb_plat_tva_nu.setChecked(true);
                plat_tva_nu = true;
            }
        }
    }

    data_class_cont get_cont_info()
    {
        String denumire = etxt_denumire.getText().toString();
        String cif = etxt_cif.getText().toString();
        String reg_com = etxt_reg_com.getText().toString();
        String cap_social = etxt_cap_social.getText().toString();
        String localitate = etxt_localitate.getText().toString();
        String judet = etxt_judet.getText().toString();
        String adresa = etxt_adresa.getText().toString();
        String cod_postal = etxt_cod_postal.getText().toString();
        String telefon = etxt_telefon.getText().toString();
        String email = etxt_email.getText().toString();
        String cont_bancar = etxt_cont_bancar.getText().toString();
        String banca = etxt_banca.getText().toString();
        String cota_tva = etxt_cota_tva.getText().toString();
        String tip_tva = etxt_tip_tva.getText().toString();

        Utils u = new Utils(getContext());

        if(u.check_string_non_empty(denumire, "denumire") && u.check_string_non_empty(cif, "cif") && u.check_string_non_empty(reg_com, "reg com") &&
            u.check_string_non_empty(cap_social, "capital social") && u.check_string_non_empty(localitate, "localitate") &&
            u.check_string_non_empty(judet, "judet") && u.check_string_non_empty(adresa, "adresa") &&
            u.check_string_non_empty(cod_postal, "cod postal") && u.check_string_non_empty(telefon, "telefon") &&
            u.check_string_non_empty(email, "email") && u.check_string_non_empty(cont_bancar, "cont") &&
            u.check_string_non_empty(banca, "banca") && u.check_string_non_empty(cota_tva, "cota tva") &&
            u.check_string_non_empty(tip_tva, "tip tva") && (plat_tva_nu || plat_tva_da))

            return new data_class_cont(denumire, cif, reg_com, plat_tva_da, cap_social, localitate, judet, adresa, cod_postal, telefon, email, cont_bancar, banca, cota_tva, tip_tva);

        return null;
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

    void b_salveaza(View r_view)
    {
        b_salveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!exists)
                    adauga_cont();
                else
                    modifica_cont();
            }
        });
    }

    void adauga_cont()
    {
        data_class_cont item = get_cont_info();

        if(item != null) {
            MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "cont");

            boolean succeded = db_handler.insert_cont_data(item);

            if (succeded) {
                Toast.makeText(getContext(), "Inserare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("delegati");

            } else
                Toast.makeText(getContext(), "Inserare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }

    void modifica_cont()
    {
        data_class_cont item = get_cont_info();

        if(item != null) {
            MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "cont");

            boolean succeded = db_handler.modify_cont_data(cont, item);

            if (succeded) {
                Toast.makeText(getContext(), "Modificare reușită!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).start_fg("acasa");

            } else
                Toast.makeText(getContext(), "Modificare nereușită!", Toast.LENGTH_SHORT).show();
        }
    }
}