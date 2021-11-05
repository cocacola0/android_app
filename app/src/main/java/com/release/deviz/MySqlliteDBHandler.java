package com.release.deviz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MySqlliteDBHandler extends SQLiteOpenHelper
{
    String TABLE_PRODUCTS = "produse";
    String TABLE_CLIENTS = "clienti";
    String TABLE_CONT = "cont";
    String TABLE_DELEGATI = "delegati";
    String TABLE_FACTURI = "facturi";

    String CREATE_TABLE_PRODUCTS = "CREATE TABLE produse(nume text NOT NULL PRIMARY KEY,cod text,pret REAL,img BLOB);";
    String CREATE_TABLE_CLIENTS = "CREATE TABLE clienti(denumire text PRIMARY KEY,cif text ,reg_com text ,plat_tva BOOLEAN,localitate text,judet text, adresa text, email text, pers_contact text, telefon text);";
    String CREATE_TABLE_CONT = "CREATE TABLE cont(denumire text PRIMARY KEY,cif text, reg_com text, plat_tva BOOLEAN ,capital_social text, localitate text, judet text, adresa text, cod_postal text, telefon text, email text,cont_bancar text, banca text, cota_tva text, tip_tva text);";
    String CREATE_TABLE_DELEGATI = "CREATE TABLE delegati(nume_delegat text PRIMARY KEY,cnp_delegat text,ci_delegat text, mij_transport text, nr_delegat text);";
    String CREATE_TABLE_FACTURI = "CREATE TABLE facturi(nume text NOT NULL,val text NOT NULL,nume_fisier text PRIMARY KEY, factura BOOLEAN);";


    String DROP_TABLE_PRODUCTS = "DROP TABLE if exists " + TABLE_PRODUCTS;
    String DROP_TABLE_CUSTOMERS = "DROP TABLE if exists " + TABLE_CLIENTS;
    String DROP_TABLE_CONT = "DROP TABLE if exists " + TABLE_CONT;
    String DROP_TABLE_DELEGATI = "DROP TABLE if exists " + TABLE_DELEGATI;
    String DROP_TABLE_FACTURI = "DROP TABLE if exists " + TABLE_FACTURI;

    String GET_TABLE_PRODUCTS = "SELECT * from " + TABLE_PRODUCTS;
    String GET_TABLE_CLIENTS = "SELECT * from " + TABLE_CLIENTS;
    String GET_TABLE_CONT = "SELECT * from " + TABLE_CONT;
    String GET_TABLE_DELEGATI = "SELECT * from " + TABLE_DELEGATI;
    String GET_TABLE_FACTURI = "SELECT * from " + TABLE_FACTURI;

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
        db.execSQL(CREATE_TABLE_CONT);
        db.execSQL(CREATE_TABLE_DELEGATI);
        db.execSQL(CREATE_TABLE_FACTURI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(db_name.equals("produse"))
            db.execSQL(DROP_TABLE_PRODUCTS);
        else
            if(db_name.equals("clienti"))
                db.execSQL(DROP_TABLE_CUSTOMERS);
            else
                if(db_name.equals("cont"))
                    db.execSQL(DROP_TABLE_CONT);
                else
                    if(db_name.equals("delegati"))
                        db.execSQL(DROP_TABLE_DELEGATI);
                    else
                        if(db_name.equals("facturi"))
                            db.execSQL(DROP_TABLE_FACTURI);
    }

    public boolean insert_product_data(data_class_produs produs)
    {
        ContentValues item = new ContentValues();
        item.put("nume", produs.getNume());
        item.put("cod", produs.getCod());
        item.put("pret", produs.getPret());

        if(produs.getImg()!=null)
            item.put("img", convert_bitmap_to_bytearr(produs.getImg()));
        else
            item.put("img", (byte[]) null);

        long result = db.insert("produse", null, item);

        return result != -1;
    }

    public boolean insert_client_data(data_class_client client)
    {
        ContentValues item = client.get_cv();

        long result = db.insert(TABLE_CLIENTS, null, item);

        return result != -1;
    }

    public boolean insert_cont_data(data_class_cont cont)
    {
        ContentValues item = cont.get_cv();

        long result = db.insert(TABLE_CONT, null, item);

        return result != -1;
    }

    public boolean insert_delegat_data(data_class_delegat delegat)
    {
        ContentValues item = delegat.get_cv();

        long result = db.insert(TABLE_DELEGATI, null, item);

        return result != -1;
    }

    public boolean insert_facturi_data(data_class_facturi facturi)
    {
        ContentValues item = facturi.get_cv();

        long result = db.insert(TABLE_FACTURI, null, item);

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

    public boolean delete_facturi_data(data_class_facturi facturi)
    {
        long result = db.delete(TABLE_FACTURI, "nume_fisier=?", new String[]{facturi.getNume_fisier()});

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
        long result = -1;

        if(old_prod == new_prod)
        {
            return true;
        }

        ContentValues new_item = new_prod.get_cv();

        Cursor c = db.rawQuery(GET_TABLE_CLIENTS_BY_CIF,new String[]{old_prod.getCif()}, null);

        if(c.getCount() > 0)
        {
            result = db.update(TABLE_CLIENTS, new_item, "cif=?", new String[]{old_prod.getCif()});
        }

        return result != -1;
    }

    public boolean modify_cont_data(data_class_cont old_prod, data_class_cont new_prod)
    {
        long result = -1;

        if(old_prod == new_prod)
        {
            return true;
        }

        ContentValues new_item = new_prod.get_cv();

        Cursor c = db.rawQuery(GET_TABLE_CONT,null, null);

        if(c.getCount() > 0)
        {
            result = db.update(TABLE_CONT, new_item, "cif=?", new String[]{old_prod.getCif()});
        }

        return result != -1;
    }

    public boolean modify_delegat_data(data_class_delegat old_prod, data_class_delegat new_prod)
    {
        long result = -1;

        if(old_prod == new_prod)
        {
            return true;
        }

        ContentValues new_item = new_prod.get_cv();

        Cursor c = db.rawQuery(GET_TABLE_DELEGATI,null, null);

        if(c.getCount() > 0)
        {
            result = db.update(TABLE_DELEGATI, new_item, "cnp_delegat=?", new String[]{old_prod.getCnp()});
        }

        return result != -1;
    }

    public boolean check_account_exists()
    {
        Cursor c = db.rawQuery(GET_TABLE_CONT,null, null);

        if(c.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean check_delegat_exists()
    {
        Cursor c = db.rawQuery(GET_TABLE_DELEGATI,null, null);

        if(c.getCount() > 0)
            return true;
        else
            return false;
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

    public data_class_cont get_cont_from_table()
    {
        Cursor c = db.rawQuery(GET_TABLE_CONT,null, null);

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
        Cursor c = db.rawQuery(GET_TABLE_DELEGATI,null, null);

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

        Cursor c = db.rawQuery(GET_TABLE_FACTURI, null, null);

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
