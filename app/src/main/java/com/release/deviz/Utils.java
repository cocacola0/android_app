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

    public boolean check_string_non_empty(String s, String tag)
    {
        if(s.equals(""))
        {
            Toast.makeText(c, "Introduceți " + tag, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean check_float(String s, String tag)
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

    public boolean check_date_greater(String date1, String date2)
    {
        String[] cur_date_ = date1.split("/");
        String[] end_date_ = date2.split("/");

        int cm = Integer.parseInt(cur_date_[0]);
        int cd = Integer.parseInt(cur_date_[1]);
        int cy = Integer.parseInt(cur_date_[2]);

        int em = Integer.parseInt(end_date_[0]);
        int ed = Integer.parseInt(end_date_[1]);
        int ey = Integer.parseInt(end_date_[2]);

        if(cy < ey || (cy == ey && cm < em) || (cy == ey && cm == em && cd < ed))
            return true;
        else
            return false;
    }
}
