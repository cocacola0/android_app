package com.release.deviz.dataClasses;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class util_functions_produs
{
    public ArrayList<String> getListNume_prod(ArrayList<data_class_extended_produs> list)
    {
        ArrayList<String> list_nume = new ArrayList<>();

        for(int i = 0; i < list.size(); i++)
            list_nume.add(list.get(i).getNume());

        return list_nume;
    }

    public ArrayList<String> getListNume_client(ArrayList<data_class_client> list)
    {
        ArrayList<String> list_nume = new ArrayList<>();

        for(int i = 0; i < list.size(); i++)
            list_nume.add(list.get(i).getDenumire());

        return list_nume;
    }

    public String array_to_string(ArrayList<data_class_extended_produs> list)
    {
        return new Gson().toJson(list);
    }

    public ArrayList<data_class_extended_produs> string_to_array(String str)
    {
        Type type = new TypeToken<ArrayList<data_class_extended_produs>>() {}.getType();
        return new Gson().fromJson(str, type);
    }
}
