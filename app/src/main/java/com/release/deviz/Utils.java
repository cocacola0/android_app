package com.release.deviz;

import android.content.Context;
import android.widget.Toast;

public class Utils
{
    public Context c;

    public Utils(Context c)
    {
        this.c = c;
    }

    boolean check_string_non_empty(String s, String tag)
    {
        if(s.equals(""))
        {
            Toast.makeText(c, "Introduceți " + tag, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    boolean check_float(String s, String tag)
    {
        float ret;

        try
        {
            ret = Float.parseFloat(s);
        }
        catch(Exception e)
        {
            Toast.makeText(c, tag + " incorect!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
