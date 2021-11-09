package com.release.deviz.dataClasses;

import java.lang.reflect.Array;
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
}
