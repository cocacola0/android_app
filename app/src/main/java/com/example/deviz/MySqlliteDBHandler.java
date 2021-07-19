package com.example.deviz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.sql.Blob;
import java.util.ArrayList;

public class MySqlliteDBHandler extends SQLiteOpenHelper
{
    String TABLE_PRODUCTS = "produse";
    String TABLE_CLIENTS = "clienti";

    String CREATE_TABLE_PRODUCTS = "CREATE TABLE produse(nume text NOT NULL,cod text PRIMARY KEY,pret REAL,img BLOB NOT NULL);";
    String CREATE_TABLE_CLIENTS = "CREATE TABLE clienti(denumire text NOT NULL,cif text PRIMARY KEY,reg_com text NOT NULL,plat_tva BOOLEAN,localitate text NOT NULL,judet text NOT NULL, adresa text NOT NULL, email text NOT NULL, pers_contact text NOT NULL, telefon text NOT NULL);";

    String DROP_TABLE_PRODUCTS = "DROP TABLE if exists " + TABLE_PRODUCTS;
    String DROP_TABLE_CUSTOMERS = "DROP TABLE if exists " + TABLE_CLIENTS;

    String GET_TABLE_PRODUCTS = "SELECT * from " + TABLE_PRODUCTS;
    String GET_TABLE_CLIENTS = "SELECT * from " + TABLE_CLIENTS;

    String GET_TABLE_PRODUCTS_BY_COD = "SELECT * from " + TABLE_PRODUCTS + " WHERE cod=?";
    String GET_TABLE_CLIENTS_BY_CIF = "SELECT * from " + TABLE_CLIENTS + " WHERE cif=?";
    SQLiteDatabase db;
    String db_name;

    public MySqlliteDBHandler(Context context, String db_name)
    {
        super(context, db_name, null, 1);
        db = this.getWritableDatabase();
        this.db_name = db_name;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_CLIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(db_name.equals("produse"))
            db.execSQL(DROP_TABLE_PRODUCTS);
        else
            if(db_name.equals("clienti"))
                db.execSQL(DROP_TABLE_CUSTOMERS);
    }

    public boolean insert_product_data(data_class_produs produs)
    {
        ContentValues item = new ContentValues();
        item.put("nume", produs.getNume());
        item.put("cod", produs.getCod());
        item.put("pret", produs.getPret());
        item.put("img", convert_bitmap_to_bytearr(produs.getImg()));

        long result = db.insert("produse", null, item);

        return result != -1;
    }

    public boolean insert_client_data(data_class_client client)
    {
        ContentValues item = client.get_cv();

        long result = db.insert(TABLE_CLIENTS, null, item);

        return result != -1;
    }

    public boolean delete_product_data(data_class_produs produs)
    {
        long result = db.delete("produse", "cod=?", new String[]{produs.getCod()});

        return result != -1;

    }

    public boolean delete_client_data(data_class_client produs)
    {
        long result = db.delete(TABLE_CLIENTS, "cif=?", new String[]{produs.getCif()});

        return result != -1;
    }

    public boolean modify_product_data(data_class_produs old_prod, data_class_produs new_prod)
    {
        long result = -1;
        if(old_prod == new_prod)
            return true;

        ContentValues new_item = new ContentValues();
        new_item.put("nume", new_prod.getNume());
        new_item.put("cod", new_prod.getCod());
        new_item.put("pret", new_prod.getPret());
        new_item.put("img", convert_bitmap_to_bytearr(new_prod.getImg()));

        Cursor c = db.rawQuery(GET_TABLE_PRODUCTS_BY_COD,new String[]{old_prod.getCod()}, null);

        if(c.getCount() > 0)
        {
            result = db.update("produse", new_item, "cod=?", new String[]{old_prod.getCod()});
        }

        return result != -1;
    }

    public boolean modify_client_data(data_class_client old_prod, data_class_client new_prod)
    {
        Log.d("DB", "ARRIVED");
        long result = -1;

        if(old_prod == new_prod)
        {
            Log.d("DB", "Equal");
            return true;
        }


        ContentValues new_item = new_prod.get_cv();

        Cursor c = db.rawQuery(GET_TABLE_CLIENTS_BY_CIF,new String[]{old_prod.getCif()}, null);

        if(c.getCount() > 0)
        {
            Log.d("DB", ">0");
            result = db.update(TABLE_CLIENTS, new_item, "cif=?", new String[]{old_prod.getCif()});
        }
        else
            Log.d("DB", "<0");

        return result != -1;
    }


    public ArrayList<data_class_produs> get_items_from_table()
    {
        ArrayList<data_class_produs> list = new ArrayList<>();

        data_class_produs item;

        Cursor c = db.rawQuery(GET_TABLE_PRODUCTS,null, null);

        if (c.moveToFirst())
        {
            do
            {
                String nume = (c.getString(c.getColumnIndex("nume")));
                String cod = (c.getString(c.getColumnIndex("cod")));
                float pret = (c.getFloat(c.getColumnIndex("pret")));

                Bitmap img = convert_bytearr_to_bitmap(c.getBlob(c.getColumnIndex("img")));

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

        Cursor c = db.rawQuery(GET_TABLE_CLIENTS,null, null);

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
    private byte[] convert_bitmap_to_bytearr(Bitmap img)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private Bitmap convert_bytearr_to_bitmap(byte[] img)
    {
         return BitmapFactory.decodeByteArray(img, 0, img.length);
    }
}
