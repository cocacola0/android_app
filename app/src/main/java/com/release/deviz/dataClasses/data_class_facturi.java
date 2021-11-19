package com.release.deviz.dataClasses;

import android.content.ContentValues;
import java.util.ArrayList;

public class data_class_facturi
{
    private String nume, val, nume_fisier;
    private boolean factura;

    private ArrayList<data_class_extended_produs> list;

    private util_functions_produs utils;

    public data_class_facturi(String nume, String val, String nume_fisier, boolean factura, ArrayList<data_class_extended_produs> list)
    {
        utils = new util_functions_produs();

        this.nume = nume;
        this.val = val;
        this.nume_fisier = nume_fisier;
        this.factura = factura;
        this.list = list;
    }

    public data_class_facturi(String nume, String val, String nume_fisier, boolean factura, String str_list){
        utils = new util_functions_produs();

        this.nume = nume;
        this.val = val;
        this.nume_fisier = nume_fisier;
        this.factura = factura;
        this.list = utils.string_to_array(str_list);
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

    public String getNume_fisier() {
        return nume_fisier;
    }

    public ArrayList<data_class_extended_produs> getList()
    {
        return list;
    }

    public ContentValues get_cv(){
        util_functions_produs utils = new util_functions_produs();

        ContentValues item = new ContentValues();

        item.put("nume", getNume());
        item.put("val", getVal());
        item.put("nume_fisier", getNume_fisier());
        item.put("factura", isFactura());
        item.put("produse", utils.array_to_string(getList()));

        return item;
    }

    public boolean isFactura() {
        return factura;
    }

    public void setFactura(boolean factura) {
        this.factura = factura;
    }
}
