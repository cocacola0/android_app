package com.example.deviz;

import android.graphics.Bitmap;

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
