package com.example.deviz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request_permissions();

        start_fg("acasa");

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

        switch (fg_name)
        {
            case "modifica_produs":
                transaction.replace(R.id.layout_frame, new fg_adauga_produs(prod), null);
                change_toolbar(R.string.modifica_produs);
                break;

            default:
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

        switch (fg_name)
        {
            case "modifica_client":
                transaction.replace(R.id.layout_frame, new fg_adauga_client(client), null);
                change_toolbar(R.string.modifica_client);
                break;

            default:
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
}