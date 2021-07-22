package com.example.deviz;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class data_class_produs
{
    private String nume, cod;
    private float pret;

    private Bitmap img;
    private int img_height = 100;
    private int img_width = 75;

    public data_class_produs(String nume, String cod, float pret, Bitmap img)
    {
        this.nume = nume;
        this.cod = cod;
        this.pret = pret;   
        this.img = img;

        //lower_resolution();
    }

    Bitmap get_Img_lower_resolution()
    {
        int orig_w = img.getWidth();
        int orig_h = img.getHeight();
        
        return Bitmap.createScaledBitmap(img, orig_w*img_height/orig_h, img_height, false);
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
}
