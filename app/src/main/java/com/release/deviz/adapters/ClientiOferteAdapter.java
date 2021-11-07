package com.release.deviz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_client;

import java.util.ArrayList;

public class ClientiOferteAdapter extends BaseAdapter
{
    final private Context context;
    final private ArrayList<data_class_client> list;

    public ClientiOferteAdapter(Context context, ArrayList<data_class_client> list)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.oferte_client_row, parent, false);

        TextView txt_nume = convertView.findViewById(R.id.of_cl_row_nume);
        TextView txt_oras = convertView.findViewById(R.id.of_cl_row_oras);
        TextView txt_judet = convertView.findViewById(R.id.of_cl_row_judet);
        ImageView img = convertView.findViewById(R.id.of_cl_img);

        data_class_client current_item = list.get(position);

        txt_nume.setText(current_item.getDenumire());
        txt_oras.setText(current_item.getLocalitate());
        txt_judet.setText(current_item.getJudet());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ((ListView) parent).performItemClick(v, position, img.getId()); // Let the event be handled in onItemClick()
            }
        });

        return convertView;
    }
}
