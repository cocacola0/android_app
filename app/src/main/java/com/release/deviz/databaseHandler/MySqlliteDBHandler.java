package com.release.deviz.databaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.release.deviz.dataClasses.data_class_client;
import com.release.deviz.dataClasses.data_class_cont;
import com.release.deviz.dataClasses.data_class_delegat;
import com.release.deviz.dataClasses.data_class_extended_produs;
import com.release.deviz.dataClasses.data_class_facturi;
import com.release.deviz.dataClasses.data_class_produs;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MySqlliteDBHandler extends SQLiteOpenHelper
{
    final private SQLiteDatabase db;
    private sqlCommands commands;
    private Context c;
    String db_name;

    public MySqlliteDBHandler(Context context, String db_name)
    {
        super(context, db_name, null, 1);

        this.db_name = db_name;

        db = this.getWritableDatabase();
        commands = new sqlCommands();
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        commands = new sqlCommands();

        switch (db_name)
        {
            case "produse":
                db.execSQL(commands.CREATE_TABLE_PRODUSE);
                break;
            case "clienti":
                db.execSQL(commands.CREATE_TABLE_CLIENTI);
                break;
            case "cont":
                db.execSQL(commands.CREATE_TABLE_CONT);
                break;
            case "delegati":
                db.execSQL(commands.CREATE_TABLE_DELEGATI);
                break;
            case "facturi":
                db.execSQL(commands.CREATE_TABLE_FACTURI);
                break;
            default:
                throw new RuntimeException("Bad command to create a table!");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        switch (db_name)
        {
            case "produse":
                db.execSQL(commands.DROP_TABLE_PRODUSE);
                break;
            case "clienti":
                db.execSQL(commands.DROP_TABLE_CLIENTI);
                break;
            case "cont":
                db.execSQL(commands.DROP_TABLE_CONT);
                break;
            case "delegati":
                db.execSQL(commands.DROP_TABLE_DELEGATI);
                break;
            case "facturi":
                db.execSQL(commands.DROP_TABLE_FACTURI);
                break;
            default:
                throw new RuntimeException("Bad command to update a table!");
        }
    }

    public boolean insert_data(data_class_produs produs)
    {
        ContentValues item = produs.get_cv();

        long result = db.insert(commands.TABLE_PRODUSE, null, item);

        return result != -1;
    }

    public boolean insert_data(data_class_client client)
    {
        ContentValues item = client.get_cv();

        long result = db.insert(commands.TABLE_CLIENTI, null, item);

        return result != -1;
    }

    public boolean insert_data(data_class_cont cont)
    {
        ContentValues item = cont.get_cv();

        long result = db.insert(commands.TABLE_CONT, null, item);

        return result != -1;
    }

    public boolean insert_data(data_class_delegat delegat)
    {
        ContentValues item = delegat.get_cv();

        long result = db.insert(commands.TABLE_DELEGATI, null, item);

        return result != -1;
    }

    public boolean insert_data(data_class_facturi facturi)
    {
        ContentValues item = facturi.get_cv();

        long result = db.insert(commands.TABLE_FACTURI, null, item);

        return result != -1;
    }

    public boolean delete_data(data_class_produs produs)
    {
        long result = db.delete("produse", "cod=?", new String[]{produs.getCod()});

        return result != -1;
    }

    public boolean delete_data(data_class_facturi facturi)
    {
        long result = db.delete(commands.TABLE_FACTURI, "nume_fisier=?", new String[]{facturi.getNume_fisier()});

        return result != -1;
    }

    public boolean delete_data(data_class_client client)
    {
        long result = db.delete(commands.TABLE_CLIENTI, "cif=?", new String[]{client.getCif()});

        return result != -1;
    }

    public boolean modify_data(data_class_produs old_prod, data_class_produs new_prod)
    {
        long result = -1;

        if(old_prod == new_prod)
            return true;

        Cursor c = db.rawQuery(commands.GET_TABLE_PRODUSE_BY_COD,new String[]{old_prod.getCod()}, null);
        ContentValues new_item = new_prod.get_cv();

        if(c.getCount() > 0)
        {
            result = db.update(commands.TABLE_PRODUSE, new_item, "cod=?", new String[]{old_prod.getCod()});
        }

        return result != -1;
    }

    public boolean modify_data(data_class_client old_client, data_class_client new_client)
    {
        long result = -1;

        if(old_client == new_client)
        {
            return true;
        }

        ContentValues new_item = new_client.get_cv();

        Cursor c = db.rawQuery(commands.GET_TABLE_CLIENTI_BY_CIF,new String[]{old_client.getCif()}, null);

        if(c.getCount() > 0)
        {
            result = db.update(commands.TABLE_CLIENTI, new_item, "cif=?", new String[]{old_client.getCif()});
        }

        return result != -1;
    }

    public boolean modify_data(data_class_cont old_prod, data_class_cont new_prod)
    {
        long result = -1;

        if(old_prod == new_prod)
        {
            return true;
        }

        ContentValues new_item = new_prod.get_cv();

        Cursor c = db.rawQuery(commands.GET_TABLE_CONT,null, null);

        if(c.getCount() > 0)
        {
            result = db.update(commands.TABLE_CONT, new_item, "cif=?", new String[]{old_prod.getCif()});
        }

        return result != -1;
    }

    public boolean modify_data(data_class_delegat old_prod, data_class_delegat new_prod)
    {
        long result = -1;

        if(old_prod == new_prod)
        {
            return true;
        }

        ContentValues new_item = new_prod.get_cv();

        Cursor c = db.rawQuery(commands.GET_TABLE_DELEGATI,null, null);

        if(c.getCount() > 0)
        {
            result = db.update(commands.TABLE_DELEGATI, new_item, "cnp_delegat=?", new String[]{old_prod.getCnp()});
        }

        return result != -1;
    }

    public ArrayList<data_class_extended_produs> get_extended_items_from_table_produse()
    {
        ArrayList<data_class_extended_produs> list = new ArrayList<>();

        data_class_extended_produs item;

        Cursor c = db.rawQuery(commands.GET_TABLE_PRODUSE,null, null);

        if (c.moveToFirst())
        {
            do
            {
                String nume = (c.getString(c.getColumnIndex("nume")));
                String cod = (c.getString(c.getColumnIndex("cod")));
                float pret = (c.getFloat(c.getColumnIndex("pret")));

                Bitmap img;

                if(c.getBlob(c.getColumnIndex("img"))!= null)
                    img = convert_bytearr_to_bitmap(c.getBlob(c.getColumnIndex("img")));
                else
                    img = null;

                item = new data_class_extended_produs(new data_class_produs(nume, cod, pret, img),1);

                list.add(item);
            }while(c.moveToNext());
        }

        return list;
    }

    public ArrayList<data_class_produs> get_items_from_table_produse()
    {
        ArrayList<data_class_produs> list = new ArrayList<>();

        data_class_produs item;

        Cursor c = db.rawQuery(commands.GET_TABLE_PRODUSE,null, null);

        if (c.moveToFirst())
        {
            do
            {
                String nume = (c.getString(c.getColumnIndex("nume")));
                String cod = (c.getString(c.getColumnIndex("cod")));
                float pret = (c.getFloat(c.getColumnIndex("pret")));

                Bitmap img;

                if(c.getBlob(c.getColumnIndex("img"))!= null)
                    img = convert_bytearr_to_bitmap(c.getBlob(c.getColumnIndex("img")));
                else
                    img = null;

                item = new data_class_produs(nume, cod, pret, img);
                list.add(item);
            }while(c.moveToNext());
        }

        return list;
    }

    public ArrayList<data_class_client> get_items_from_table_clients()
    {
        ArrayList<data_class_client> list = new ArrayList<>();
        data_class_client item;

        Cursor c = db.rawQuery(commands.GET_TABLE_CLIENTI,null, null);

        if (c.moveToFirst())
        {
            do
            {
                String denumire = (c.getString(c.getColumnIndex("denumire")));
                String cif = (c.getString(c.getColumnIndex("cif")));
                String reg_com = (c.getString(c.getColumnIndex("reg_com")));
                boolean plat_tva = (c.getInt(c.getColumnIndex("plat_tva")) > 0);
                String localitate = (c.getString(c.getColumnIndex("localitate")));
                String judet = (c.getString(c.getColumnIndex("judet")));
                String adresa = (c.getString(c.getColumnIndex("adresa")));
                String email = (c.getString(c.getColumnIndex("email")));
                String pers_contact = (c.getString(c.getColumnIndex("pers_contact")));
                String telefon = (c.getString(c.getColumnIndex("telefon")));

                item = new data_class_client(denumire, cif, reg_com, plat_tva, localitate, judet, adresa, email, pers_contact, telefon);
                list.add(item);
            }while(c.moveToNext());
        }
        return list;
    }

    public data_class_cont get_cont_from_table()
    {
        Cursor c = db.rawQuery(commands.GET_TABLE_CONT,null, null);

        if (c.moveToFirst())
        {

            String denumire = (c.getString(c.getColumnIndex("denumire")));
            String cif = (c.getString(c.getColumnIndex("cif")));
            String reg_com = (c.getString(c.getColumnIndex("reg_com")));
            boolean plat_tva = (c.getInt(c.getColumnIndex("plat_tva")) > 0);
            String capital_social = (c.getString(c.getColumnIndex("capital_social")));
            String localitate = (c.getString(c.getColumnIndex("localitate")));
            String judet = (c.getString(c.getColumnIndex("judet")));
            String adresa = (c.getString(c.getColumnIndex("adresa")));
            String cod_postal = (c.getString(c.getColumnIndex("cod_postal")));
            String telefon = (c.getString(c.getColumnIndex("telefon")));
            String email = (c.getString(c.getColumnIndex("email")));
            String cont_bancar = (c.getString(c.getColumnIndex("cont_bancar")));
            String banca = (c.getString(c.getColumnIndex("banca")));
            String cota_tva = (c.getString(c.getColumnIndex("cota_tva")));
            String tip_tva = (c.getString(c.getColumnIndex("tip_tva")));


            return new data_class_cont(denumire, cif, reg_com, plat_tva, capital_social, localitate, judet, adresa,cod_postal,telefon, email,cont_bancar, banca, cota_tva, tip_tva);
        }
        return null;
    }

    public data_class_delegat get_delegat_from_table()
    {
        Cursor c = db.rawQuery(commands.GET_TABLE_DELEGATI,null, null);

        if (c.moveToFirst())
        {
            String nume = (c.getString(c.getColumnIndex("nume_delegat")));
            String cnp = (c.getString(c.getColumnIndex("cnp_delegat")));
            String ci = (c.getString(c.getColumnIndex("ci_delegat")));
            String mij_transport = (c.getString(c.getColumnIndex("mij_transport")));
            String nr_delegat = (c.getString(c.getColumnIndex("nr_delegat")));


            return new data_class_delegat(nume, cnp, ci, mij_transport, nr_delegat);
        }
        return null;
    }

    public ArrayList<data_class_facturi> get_facturi_from_table()
    {
        ArrayList<data_class_facturi> list = new ArrayList<>();
        data_class_facturi item;

        Cursor c = db.rawQuery(commands.GET_TABLE_FACTURI, null, null);

        if (c.moveToFirst())
        {
            do {
                String nume = (c.getString(c.getColumnIndex("nume")));
                String val = (c.getString(c.getColumnIndex("val")));
                String nume_fisier = (c.getString(c.getColumnIndex("nume_fisier")));
                boolean factura = (c.getInt(c.getColumnIndex("factura")) > 0);

                item = new data_class_facturi(nume, val, nume_fisier, factura);
                list.add(item);
            }while (c.moveToNext());
        }

        return list;
    }

    public boolean check_bool_shared_pref(String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);

        return sharedPref.getBoolean(key, false);
    }

    public void put_bool_shared_pref(String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);

        sharedPref.edit().putBoolean(key, true).apply();
    }

    private Bitmap convert_bytearr_to_bitmap(byte[] img)
    {
         return BitmapFactory.decodeByteArray(img, 0, img.length);
    }
}
