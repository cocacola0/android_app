package com.release.deviz.dataClasses;

import android.content.ContentValues;

public class data_class_facturi
{
    private String nume, val, nume_fisier;
    private boolean factura;

    public data_class_facturi(String nume, String val, String nume_fisier, boolean factura)
    {
        this.nume = nume;
        this.val = val;
        this.nume_fisier = nume_fisier;
        this.factura = factura;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getNume_fisier() {
        return nume_fisier;
    }

    public void setNume_fisier(String nume_fisier) {
        this.nume_fisier = nume_fisier;
    }

    public ContentValues get_cv()
    {
        ContentValues item = new ContentValues();

        item.put("nume", getNume());
        item.put("val", getVal());
        item.put("nume_fisier", getNume_fisier());
        item.put("factura", isFactura());

        return item;
    }

    public boolean isFactura() {
        return factura;
    }

    public void setFactura(boolean factura) {
        this.factura = factura;
    }
}
