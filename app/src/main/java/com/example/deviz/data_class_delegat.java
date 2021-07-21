package com.example.deviz;

import android.content.ContentValues;

public class data_class_delegat {
    String nume, cnp, ci, mij_transport, nr;

    public data_class_delegat(String nume, String cnp, String ci, String mij_transport, String nr)
    {
        this.nume = nume;
        this.cnp = cnp;
        this.ci = ci;
        this.mij_transport = mij_transport;
        this.nr = nr;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getMij_transport() {
        return mij_transport;
    }

    public void setMij_transport(String mij_transport) {
        this.mij_transport = mij_transport;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public ContentValues get_cv()
    {
        ContentValues item = new ContentValues();

        item.put("nume_delegat", getNume());
        item.put("cnp_delegat", getCnp());
        item.put("ci_delegat", getCi());
        item.put("mij_transport", getMij_transport());
        item.put("nr_delegat", getNr());

        return item;
    }
}
