package com.example.deviz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RapoarteAdapter extends BaseAdapter
{
    final private Context context;
    final private ArrayList<data_class_facturi> list;

    public RapoarteAdapter(Context context, ArrayList<data_class_facturi> list)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.rapoarte_row, parent, false);

        TextView txt_nume = convertView.findViewById(R.id.row_rapoarte_nume);
        TextView txt_val = convertView.findViewById(R.id.row_rapoarte_valoare);
        TextView txt_factura = convertView.findViewById(R.id.row_rapoarte_factura);


        data_class_facturi current_item = list.get(position);

        txt_nume.setText(current_item.getNume());
        txt_val.setText(current_item.getVal());

        if(current_item.isFactura())
            txt_factura.setText("Factură");
        else
            txt_factura.setText("Ofertă");

        return convertView;
    }
}
