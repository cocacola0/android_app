package com.release.deviz.dataClasses;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;

public class data_class_produs
{
    private String nume, cod;
    private float pret;

    private Bitmap img;

    static int max_img_width = 1500;
    static int max_img_height = 900;

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

        this.img = getScaledwonBitmap(produs.img);
    }

    public static Bitmap getScaledwonBitmap(Bitmap srcBmp) {
        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();

        if(height > max_img_height) {
            width = width * max_img_height / height;
            height = max_img_height;
        }

        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, srcBmp.getWidth(), srcBmp.getHeight()),
                new RectF(0, 0, width, height),
                Matrix.ScaleToFit.CENTER);

        return Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.getWidth(), srcBmp.getHeight(), matrix, true);

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
        return Bitmap.createScaledBitmap(img, 400, 360, true);
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
