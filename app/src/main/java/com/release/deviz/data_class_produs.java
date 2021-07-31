package com.release.deviz;

import android.graphics.Bitmap;

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

    public Bitmap getResizedBitmap(int maxSize) {

        int width = img.getWidth();
        int height = img.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(img, 40, 36, true);

        //Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
       // profileImage.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
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
