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
import java.sql.Blob;
import java.util.ArrayList;

public class MySqlliteDBHandler extends SQLiteOpenHelper
{
    String TABLE_PRODUCTS = "produse";
    String CREATE_TABLE_PRODUCTS = "CREATE TABLE produse(nume text NOT NULL,cod text PRIMARY KEY,pret REAL,img BLOB NOT NULL);";
    String DROP_TABLE_PRODUCTS = "DROP TABLE if exists produse";
    String GET_TABLE_PRODUCTS = "SELECT * from produse";
    String GET_TABLE_PRODUCTS_BY_COD = "SELECT * from produse WHERE cod=?";

    SQLiteDatabase db;

    public MySqlliteDBHandler(Context context, String db_name)
    {
        super(context, db_name, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_TABLE_PRODUCTS);
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

    public boolean delete_product_data(data_class_produs produs)
    {
        long result = db.delete("produse", "cod=?", new String[]{produs.getCod()});

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
