package com.release.deviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.release.deviz.dataClasses.data_class_client;
import com.release.deviz.dataClasses.data_class_facturi;
import com.release.deviz.dataClasses.data_class_produs;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.databaseHandler.SharedPrefferencesHandler;
import com.release.deviz.fragments.fg_abonament;
import com.release.deviz.fragments.fg_acasa;
import com.release.deviz.fragments.fg_adauga_client;
import com.release.deviz.fragments.fg_adauga_produs;
import com.release.deviz.fragments.fg_clienti;
import com.release.deviz.fragments.fg_contul_meu;
import com.release.deviz.fragments.fg_delegati;
import com.release.deviz.fragments.fg_despre;
import com.release.deviz.fragments.fg_oferte;
import com.release.deviz.fragments.fg_pagina_inceput;
import com.release.deviz.fragments.fg_pdf_viewer;
import com.release.deviz.fragments.fg_produse;
import com.release.deviz.fragments.fg_rapoarte;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    public DrawerLayout d;
    NavigationView n;
    Toolbar t;
    String PREF_FILE= "MyPref";
    boolean inactive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request_permissions();

        setup_drawer_layout();

        SharedPrefferencesHandler shared_pref_handler = new SharedPrefferencesHandler(getApplicationContext());
        if(!shared_pref_handler.check_bool("cont"))
            start_fg("pagina_inceput");
        else
        {
            String pattern = "MM/dd/yyyy";

            String cur_date = new SimpleDateFormat(pattern).format(new Date());
            String end_date = this.getSharedPreferences(PREF_FILE, 0).getString("sub_date",cur_date);

            Utils u = new Utils(getApplicationContext());

            if(u.check_date_greater(cur_date, end_date))
                start_fg("acasa");
            else
            {
                start_fg("abonament");
                inactive = true;
            }
        }

        img_home();
    }

    protected void setup_drawer_layout()
    {
        d = findViewById(R.id.drawer_layout);
        n = findViewById(R.id.nav_view);

        n.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);

                int id = item.getItemId();

                if(id==R.id.menu_contul_meu)
                    start_fg("contul_meu");

                else
                    if(id==R.id.menu_abonament)
                        start_fg("abonament");
                    else
                        if(id==R.id.menu_despre)
                            start_fg("despre");

                d.closeDrawers();
                return true;
            }
        });

        t = findViewById(R.id.layout_toolbar);
        setSupportActionBar(t);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        d.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }

    protected void img_home()
    {
        ImageView home = findViewById(R.id.img_home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_fg("acasa");
            }
        });
    }

    protected void request_permissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    public void start_fg(String fg_name)
    {
        if(inactive)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(false);

        switch (fg_name)
        {
            case "oferte":
                transaction.replace(R.id.layout_frame, fg_oferte.class, null);
                change_toolbar(R.string.oferte);
                break;

            case "facturi":
                transaction.replace(R.id.layout_frame, new fg_oferte(true), null);
                change_toolbar(R.string.facturi);
                break;

            case "produse":
                transaction.replace(R.id.layout_frame, fg_produse.class, null);
                change_toolbar(R.string.produse);
                break;

            case "rapoarte":
                transaction.replace(R.id.layout_frame, fg_rapoarte.class, null);
                change_toolbar(R.string.rapoarte);
                break;

            case "clienti":
                transaction.replace(R.id.layout_frame, fg_clienti.class, null);
                change_toolbar(R.string.clienti);
                break;

            case "adauga_produs":
                transaction.replace(R.id.layout_frame, fg_adauga_produs.class, null);
                change_toolbar(R.string.adauga_produs);
                break;

            case "adauga_client":
                transaction.replace(R.id.layout_frame, fg_adauga_client.class, null);
                change_toolbar(R.string.adauga_client);
                break;

            case "contul_meu":
                transaction.replace(R.id.layout_frame, fg_contul_meu.class, null);
                change_toolbar(R.string.contul_meu);
                break;

            case "delegati":
                transaction.replace(R.id.layout_frame, fg_delegati.class, null);
                change_toolbar(R.string.delegati);
                break;

            case "abonament":
                transaction.replace(R.id.layout_frame, fg_abonament.class, null);
                change_toolbar(R.string.abonament);
                break;

            case "pagina_inceput":
                transaction.replace(R.id.layout_frame, fg_pagina_inceput.class, null);
                change_toolbar(R.string.app_name);
                break;

            case "despre":
                transaction.replace(R.id.layout_frame, fg_despre.class, null);
                change_toolbar(R.string.despre);
                break;

            default:
                transaction.replace(R.id.layout_frame, fg_acasa.class, null);
                change_toolbar(R.string.app_name);
        }

        transaction.addToBackStack("current-123").commit();
    }

    public void start_fg_with_args(String fg_name, data_class_produs prod)
    {
        if(inactive)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(false);

        if(fg_name.equals("modifica_produs"))
        {
            transaction.replace(R.id.layout_frame, new fg_adauga_produs(prod), null);
            change_toolbar(R.string.modifica_produs);
        }
        else
        {
            transaction.replace(R.id.layout_frame, fg_acasa.class, null);
            change_toolbar(R.string.app_name);
        }

        transaction.addToBackStack(null).commit();
    }

    public void start_fg_with_args(String fg_name, data_class_client client)
    {
        if(inactive)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(false);

        if(fg_name.equals("modifica_client"))
        {
            transaction.replace(R.id.layout_frame, new fg_adauga_client(client), null);
            change_toolbar(R.string.modifica_client);
        }
        else
        {
            transaction.replace(R.id.layout_frame, fg_acasa.class, null);
            change_toolbar(R.string.app_name);
        }

        transaction.addToBackStack(null).commit();
    }

    public void start_fg_with_args(String fg_name, data_class_facturi factura)
    {
        if(inactive)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(false);

        if(fg_name.equals("pdf_viewer"))
        {
            transaction.replace(R.id.layout_frame, new fg_pdf_viewer(factura), null);
            change_toolbar(R.string.pdf_viewer);
        }
        else
        {
            transaction.replace(R.id.layout_frame, fg_acasa.class, null);
            change_toolbar(R.string.app_name);
        }

        transaction.addToBackStack(null).commit();
    }

    public void change_toolbar(int txt_resource)
    {
        TextView txt_toolbar = findViewById(R.id.txt_toolbar);
        txt_toolbar.setText(getResources().getText(txt_resource));
    }

    public void setActive()
    {
        this.inactive = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==android.R.id.home) {
            d.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}