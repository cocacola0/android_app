package com.release.deviz.dataClasses;

import android.content.ContentValues;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class data_class_produs
{
    private String nume, cod;
    private float pret;

    private Bitmap img;

    public data_class_produs(String nume, String cod, float pret, Bitmap img)
    {
        this.nume = nume;
        this.cod = cod;
        this.pret = pret;   
        this.img = img;

    }

    public data_class_produs(data_class_produs produs)
    {
        this.nume = produs.nume;
        this.cod = produs.cod;
        this.pret = produs.pret;
        this.img = produs.img;
    }

    public Bitmap getResizedBitmap(int maxSize) {

        int width;
        int height;

        try{
            width=img.getWidth();
            height=img.getHeight();
        }
        catch (Exception e)
        {
            return null;
        }

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(img, 40, 36, true);
    }

    public String getNume()
    {
        return nume;
    }

    public String getCod()
    {
        return cod;
    }

    public float getPret()
    {
        return pret;
    }

    public Bitmap getImg()
    {
        return img;
    }

    public void setNume(String nume)
    {
        this.nume = nume;
    }

    public void setCod(String cod)
    {
        this.cod = cod;
    }

    public void setPret(float pret)
    {
        this.pret = pret;
    }

    public void setImg(Bitmap img)
    {
        this.img = img;
    }

    public ContentValues get_cv()
    {
        ContentValues item = new ContentValues();

        item.put("nume", getNume());
        item.put("cod", getCod());
        item.put("pret", getPret());

        if(getImg()!=null)
            item.put("img", convert_bitmap_to_bytearr(getImg()));
        else
            item.put("img", (byte[]) null);

        return item;
    }

    private byte[] convert_bitmap_to_bytearr(Bitmap img)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
