package com.example.deviz;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProduseAdapter extends BaseAdapter
{
    final private Context context;
    final private ArrayList<data_class_produs> list;

    public ProduseAdapter(Context context, ArrayList<data_class_produs> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        ImageView img_prod = convertView.findViewById(R.id.imgv_row_produs);
        TextView txt_nume = convertView.findViewById(R.id.txt_row_nume);
        TextView txt_cod = convertView.findViewById(R.id.txt_row_cod);
        TextView txt_pret = convertView.findViewById(R.id.txt_row_pret);

        data_class_produs current_item = list.get(position);

        img_prod.setImageBitmap(current_item.getImg());
        txt_nume.setText(current_item.getNume());
        txt_cod.setText(current_item.getCod());
        txt_pret.setText(Float.toString(current_item.getPret()));

        return convertView;
    }
}
