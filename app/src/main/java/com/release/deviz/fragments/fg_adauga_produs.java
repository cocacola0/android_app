package com.release.deviz.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

import com.release.deviz.MainActivity;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.R;
import com.release.deviz.Utils;
import com.release.deviz.dataClasses.data_class_produs;
import com.release.deviz.databaseHandler.SharedPrefferencesHandler;

public class fg_adauga_produs extends Fragment
{
    EditText etxt_nume, etxt_cod, etxt_pret;
    Button b_adauga_produs, b_incarca;
    ImageView img_produs;

    MySqlliteDBHandler sql_db_handler;
    SharedPrefferencesHandler shared_pref_handler;

    private static final int GALLERY_REQUEST_CODE = 123;
    data_class_produs prod;
    boolean modifica = false;

    public fg_adauga_produs()
    {
        // Required empty public constructor
    }

    public fg_adauga_produs(data_class_produs prod)
    {
        this.prod = prod;
        this.modifica = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).change_toolbar(R.string.adauga_produs);
    }

    public static fg_adauga_produs newInstance() {
        return new fg_adauga_produs();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View r_view = inflater.inflate(R.layout.fg_adauga_produs, container, false);

        sql_db_handler = new MySqlliteDBHandler(getContext(), "produse");
        shared_pref_handler = new SharedPrefferencesHandler(getContext());

        b_incarca = r_view.findViewById(R.id.txt_incarca_imagine);
        b_adauga_produs = r_view.findViewById(R.id.btn_adauga_produs);

        img_produs = r_view.findViewById(R.id.imgw_prod);

        etxt_nume = r_view.findViewById(R.id.etxt_nume);
        etxt_cod = r_view.findViewById(R.id.etxt_cod);
        etxt_pret = r_view.findViewById(R.id.etxt_pret);

        if(modifica)
        {
            etxt_nume.setText(prod.getNume());
            etxt_cod.setText(prod.getCod());
            etxt_pret.setText(Float.toString(prod.getPret()));
            img_produs.setImageBitmap(prod.getImg());

            b_adauga_produs.setText("Modifică");
        }

        b_incarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                select_image_from_gallery();
            }
        });

        b_adauga_produs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!modifica)
                    adauga_produs();
                else
                    modifica_produs();
            }
        });


        return r_view;
    }

    public static Bitmap getScaledwonBitmap(Bitmap srcBmp, int deisredWidth, int desiredHeight) {

        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, srcBmp.getWidth(), srcBmp.getHeight()),
                new RectF(0, 0, deisredWidth, desiredHeight),
                Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.getWidth(), srcBmp.getHeight(), matrix, true);

    }

    private data_class_produs get_produs_info()
    {
        String nume = etxt_nume.getText().toString();
        String cod = etxt_cod.getText().toString();
        String str_pret = etxt_pret.getText().toString();
        Bitmap bitmap;
        float pret;

        Utils u = new Utils(getContext());

        if(u.check_string_non_empty(nume,"nume") && u.check_string_non_empty(str_pret,"pret") && u.check_float(str_pret, "pret"))
        {
            BitmapDrawable drawable;

            if(img_produs.getDrawable() != null)
            {
                drawable = (BitmapDrawable) img_produs.getDrawable();
                bitmap = drawable.getBitmap();

                bitmap = getScaledwonBitmap(bitmap, 1080, 2160);
            }
            else
                bitmap = null;

            pret = Float.parseFloat(str_pret);

            return new data_class_produs(nume, cod, pret, bitmap);
        }
        else
            return null;
    }

    private void adauga_produs()
    {
        data_class_produs item = get_produs_info();

        if(item != null) {
            boolean succeded = sql_db_handler.insert_data(item);

            if (succeded) {
                show_toast("Inserare reușită!");
                shared_pref_handler.put_bool("produse");
                ((MainActivity) getActivity()).start_fg("produse");

            } else
                show_toast("Inserare nereușită!");
        }
    }


    private void modifica_produs()
    {
        data_class_produs item = get_produs_info();

        if(item != null)
        {
            boolean succeded = sql_db_handler.modify_data(prod, item);

            if (succeded) {
                show_toast("Modificare reușită!");
                ((MainActivity) getActivity()).start_fg("produse");

            } else
                show_toast("Modificare nereușită!");

        }
    }
    private void select_image_from_gallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Selectează o imagine"),GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
            Uri returnUri = data.getData();
            Bitmap bitmapImage = null;
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            img_produs.setImageBitmap(bitmapImage);
        }
    }

    public void show_toast(String message)
    {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}