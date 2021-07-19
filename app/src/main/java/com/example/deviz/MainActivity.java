package com.example.deviz;

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
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout d;
    NavigationView n;
    public ActionBarDrawerToggle toggle;
    Toolbar t;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request_permissions();

        setup_drawer_layout();
        start_fg("acasa");
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

    protected void start_fg(String fg_name)
    {
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
                transaction.replace(R.id.layout_frame, fg_facturi.class, null);
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

            case "furnizori":
                transaction.replace(R.id.layout_frame, fg_furnizori.class, null);
                change_toolbar(R.string.furnizori);
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
            default:
                transaction.replace(R.id.layout_frame, fg_acasa.class, null);
                change_toolbar(R.string.app_name);
        }

        transaction.commit();
    }

    protected void start_fg_with_args(String fg_name, data_class_produs prod)
    {
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

        transaction.commit();
    }

    protected void start_fg_with_args(String fg_name, data_class_client client)
    {
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

        transaction.commit();
    }

    public void change_toolbar(int txt_resource)
    {
        TextView txt_toolbar = findViewById(R.id.txt_toolbar);
        txt_toolbar.setText(getResources().getText(txt_resource));
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