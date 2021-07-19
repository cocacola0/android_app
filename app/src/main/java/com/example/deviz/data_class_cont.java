package com.example.deviz;

import android.content.ContentValues;

public class data_class_cont
{
    String denumire, cif, reg_com, cap_social, localitate, judet, adresa, cod_postal, telefon, email, cont_bancar, banca, cota_tva, tip_tva;
    boolean platitor_tva;

    public data_class_cont(String denumire, String cif, String reg_com, boolean platitor_tva, String cap_social,String localitate, String judet, String adresa, String cod_postal, String telefon, String email, String cont_bancar, String banca, String cota_tva, String tip_tva)
    {
        this.denumire = denumire;
        this.cif = cif;
        this.reg_com = reg_com;
        this.platitor_tva = platitor_tva;
        this.cap_social = cap_social;
        this.localitate = localitate;
        this.judet = judet;
        this.adresa = adresa;
        this.cod_postal = cod_postal;
        this.telefon = telefon;
        this.email = email;
        this.cont_bancar = cont_bancar;
        this.banca = banca;
        this.cota_tva = cota_tva;
        this.tip_tva = tip_tva;
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

    public String getCap_social() {
        return cap_social;
    }

    public void setCap_social(String cap_social) {
        this.cap_social = cap_social;
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

    public String getCod_postal() {
        return cod_postal;
    }

    public void setCod_postal(String cod_postal) {
        this.cod_postal = cod_postal;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCont_bancar() {
        return cont_bancar;
    }

    public void setCont_bancar(String cont_bancar) {
        this.cont_bancar = cont_bancar;
    }

    public String getBanca() {
        return banca;
    }

    public void setBanca(String banca) {
        this.banca = banca;
    }

    public String getCota_tva() {
        return cota_tva;
    }

    public void setCota_tva(String cota_tva) {
        this.cota_tva = cota_tva;
    }

    public String getTip_tva() {
        return tip_tva;
    }

    public void setTip_tva(String tip_tva) {
        this.tip_tva = tip_tva;
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

        item.put("denumire", getDenumire());
        item.put("cif", getCif());
        item.put("reg_com", getReg_com());
        item.put("plat_tva", isPlatitor_tva());
        item.put("capital_social", getCap_social());
        item.put("localitate", getLocalitate());
        item.put("judet", getJudet());
        item.put("adresa", getAdresa());
        item.put("cod_postal", getCod_postal());
        item.put("telefon", getTelefon());
        item.put("email", getEmail());
        item.put("cont_bancar", getCont_bancar());
        item.put("banca", getBanca());
        item.put("cota_tva", getCota_tva());
        item.put("tip_tva", getTip_tva());

        return item;
    }
}
