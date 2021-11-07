package com.release.deviz.databaseHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefferencesHandler
{
    private SharedPreferences sharedPref;
    Context c;

    public SharedPrefferencesHandler(Context context)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public boolean check_bool(String key)
    {
        return sharedPref.getBoolean(key, false);
    }

    public int check_int(String key)
    {
        return sharedPref.getInt(key, 1);
    }

    public void put_bool(String key)
    {
        sharedPref.edit().putBoolean(key, true).apply();
    }

    public void put_int(String key, int value)
    {
        sharedPref.edit().putInt(key,value).apply();
    }

    public void iterate_int(String key)
    {
        sharedPref.edit().putInt(key,check_int(key) + 1).apply();
    }
}
