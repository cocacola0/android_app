package com.example.deviz;

import android.content.ContentValues;

public class data_class_client
{
    private String denumire, cif, reg_com, localitate, judet, adresa, email, pers_contact, telefon;
    private boolean platitor_tva;

    public data_class_client(String denumire, String cif, String reg_com, boolean platitor_tva, String localitate, String judet, String adresa, String email, String pers_contact, String telefon)
    {
        this.denumire = denumire;
        this.cif = cif;
        this.reg_com = reg_com;
        this.platitor_tva = platitor_tva;
        this.localitate = localitate;
        this.judet = judet;
        this.adresa = adresa;
        this.email = email;
        this.pers_contact = pers_contact;
        this.telefon = telefon;
    }


    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }


    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getReg_com() {
        return reg_com;
    }

    public void setReg_com(String reg_com) {
        this.reg_com = reg_com;
    }

    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPers_contact() {
        return pers_contact;
    }

    public void setPers_contact(String pers_contact) {
        this.pers_contact = pers_contact;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public boolean isPlatitor_tva() {
        return platitor_tva;
    }

    public void setPlatitor_tva(boolean platitor_tva) {
        this.platitor_tva = platitor_tva;
    }

    public ContentValues get_cv()
    {
        ContentValues item = new ContentValues();

        item.put("denumire", denumire);
        item.put("cif", cif);
        item.put("reg_com", reg_com);
        item.put("plat_tva", platitor_tva);
        item.put("localitate", localitate);
        item.put("judet", judet);
        item.put("adresa", adresa);
        item.put("email",email);
        item.put("pers_contact", pers_contact);
        item.put("telefon", telefon);

        return item;
    }

    public int size_of()
    {
        return 10;
    }
}
