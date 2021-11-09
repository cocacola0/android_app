package com.release.deviz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_extended_produs;

import java.util.ArrayList;

/*
Adapter for Produse in spinner in fragment Oferta/Factura
 */
public class ProduseOferteAdapter extends BaseAdapter
{
    final private Context context;
    final private ArrayList<data_class_extended_produs> list;

    public ProduseOferteAdapter(Context context, ArrayList<data_class_extended_produs> list)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.oferte_produs_row, parent, false);

        ImageView img_prod = convertView.findViewById(R.id.of_prod_row_img);

        TextView txt_nume = convertView.findViewById(R.id.of_prod_row_nume);
        TextView txt_cod = convertView.findViewById(R.id.of_prod_row_cod);
        TextView txt_pret = convertView.findViewById(R.id.of_prod_row_pret);

        EditText etxt_buc = convertView.findViewById(R.id.of_prod_row_buc);

        ImageView img_delete = convertView.findViewById(R.id.of_prod_delete);

        data_class_extended_produs current_item = list.get(position);

        img_prod.setImageBitmap(current_item.getImg());
        txt_nume.setText(current_item.getNume());
        txt_cod.setText(current_item.getCod());
        txt_pret.setText(Float.toString(current_item.getPret()));
        if(current_item.getNr_buc() == 1)
            etxt_buc.setHint(String.valueOf(current_item.getNr_buc()));
        else
            etxt_buc.setText(String.valueOf(current_item.getNr_buc()));


        etxt_buc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String nr_bucati = etxt_buc.getText().toString();

                if(!nr_bucati.isEmpty()) {
                    list.get(position).setNr_buc(Integer.parseInt(nr_bucati));
                    etxt_buc.setText(nr_bucati);
                }
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, img_delete.getId()); // Let the event be handled in onItemClick()
            }
        });

        return convertView;
    }
}