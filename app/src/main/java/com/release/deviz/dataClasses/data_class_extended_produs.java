package com.release.deviz.dataClasses;

import android.graphics.Bitmap;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.release.deviz.dataClasses.data_class_produs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/*
Extends data_class_produs with nr_buc
 */
public class data_class_extended_produs extends data_class_produs implements Serializable
{
    final private data_class_produs produs;
    private int nr_buc;

    public data_class_extended_produs(data_class_produs produs, int nr_buc)
    {
        super(produs);

        this.produs = produs;
        this.nr_buc = nr_buc;
    }

    public int getNr_buc()
    {
        return nr_buc;
    }

    public void setNr_buc(int new_value)
    {
        nr_buc = new_value;
    }
    public float getPretCuTva()
    {
        float pret_cu_tva = nr_buc * produs.getPret();

        return roundFloatDecimals(pret_cu_tva);
    }

    public float getPretFaraTva()
    {
        float pret_fara_tva = 100 * nr_buc * produs.getPret() / 119;

        return roundFloatDecimals(pret_fara_tva);
    }

    public float getTva()
    {
        return roundFloatDecimals(getPretCuTva() - roundFloatDecimals(getPretFaraTva()));
    }

    public Image getImagine() throws IOException, BadElementException
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        Bitmap bmp_img = produs.getResizedBitmap(10);

        Image logo = null;

        if(bmp_img != null)
        {
            bmp_img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            logo = Image.getInstance(stream.toByteArray());

            logo.setWidthPercentage(80);
            logo.scaleToFit(38, 38);
        }

        return logo;
    }

    //Round float to 2 decimals
    private float roundFloatDecimals(float number)
    {
        number *= 100;
        number = (float) ((int) number);
        number /= 100;

        return number;
    }
}
