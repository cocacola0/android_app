package com.release.deviz.databaseHandler;

public class sqlCommands
{
    final String TABLE_PRODUSE = "produse";
    final String TABLE_CLIENTI = "clienti";
    final String TABLE_CONT = "cont";
    final String TABLE_DELEGATI = "delegati";
    final String TABLE_FACTURI = "facturi";

    String CREATE_TABLE_PRODUSE = "CREATE TABLE " + TABLE_PRODUSE + "(nume text NOT NULL PRIMARY KEY,cod text,pret REAL,img BLOB);";
    String CREATE_TABLE_CLIENTI = "CREATE TABLE " + TABLE_CLIENTI + "(denumire text PRIMARY KEY,cif text ,reg_com text ,plat_tva BOOLEAN,localitate text,judet text, adresa text, email text, pers_contact text, telefon text);";
    String CREATE_TABLE_CONT = "CREATE TABLE " + TABLE_CONT + "(denumire text PRIMARY KEY,cif text, reg_com text, plat_tva BOOLEAN ,capital_social text, localitate text, judet text, adresa text, cod_postal text, telefon text, email text,cont_bancar text, banca text, cota_tva text, tip_tva text);";
    String CREATE_TABLE_DELEGATI = "CREATE TABLE " + TABLE_DELEGATI + "(nume_delegat text PRIMARY KEY,cnp_delegat text,ci_delegat text, mij_transport text, nr_delegat text);";
    String CREATE_TABLE_FACTURI = "CREATE TABLE " + TABLE_FACTURI + "(nume text NOT NULL,val text NOT NULL,nume_fisier text PRIMARY KEY, factura BOOLEAN);";


    String DROP_TABLE_PRODUSE = "DROP TABLE if exists " + TABLE_PRODUSE;
    String DROP_TABLE_CLIENTI = "DROP TABLE if exists " + TABLE_CLIENTI;
    String DROP_TABLE_CONT = "DROP TABLE if exists " + TABLE_CONT;
    String DROP_TABLE_DELEGATI = "DROP TABLE if exists " + TABLE_DELEGATI;
    String DROP_TABLE_FACTURI = "DROP TABLE if exists " + TABLE_FACTURI;

    String GET_TABLE_PRODUSE = "SELECT * from " + TABLE_PRODUSE;
    String GET_TABLE_CLIENTI = "SELECT * from " + TABLE_CLIENTI;
    String GET_TABLE_CONT = "SELECT * from " + TABLE_CONT;
    String GET_TABLE_DELEGATI = "SELECT * from " + TABLE_DELEGATI;
    String GET_TABLE_FACTURI = "SELECT * from " + TABLE_FACTURI;

    String GET_TABLE_PRODUSE_BY_COD = "SELECT * from " + TABLE_PRODUSE + " WHERE cod=?";
    String GET_TABLE_CLIENTI_BY_CIF = "SELECT * from " + TABLE_CLIENTI + " WHERE cif=?";


}
