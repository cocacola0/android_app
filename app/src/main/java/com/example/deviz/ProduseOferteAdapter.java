package com.example.deviz;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProduseOferteAdapter extends BaseAdapter {
    final private Context context;
    final private ArrayList<data_class_produs> list;

    public ProduseOferteAdapter(Context context, ArrayList<data_class_produs> list)
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

        TextView txt_nume = convertView.findViewById(R.id.of_prod_row_nume);
        TextView txt_cod = convertView.findViewById(R.id.of_prod_row_cod);
        TextView txt_pret = convertView.findViewById(R.id.of_prod_row_pret);
        TextView txt_buc = convertView.findViewById(R.id.of_prod_row_buc);

        ImageView img_prod = convertView.findViewById(R.id.of_prod_row_img);
        ImageView img_minus = convertView.findViewById(R.id.of_prod_img_minus);
        ImageView img_add = convertView.findViewById(R.id.of_prod_img_add);

        data_class_produs current_item = list.get(position);

        txt_nume.setText(current_item.getNume());
        txt_cod.setText(current_item.getCod());
        txt_pret.setText(Float.toString(current_item.getPret()));
        txt_buc.setText("1");

        img_prod.setImageBitmap(current_item.getImg());

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String new_txt = Integer.toString(Integer.parseInt(txt_buc.getText().toString())-1);

                txt_buc.setText(new_txt);

                if(new_txt.equals("0"))
                    ((ListView) parent).performItemClick(v, position, img_minus.getId());
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_buc.setText(Integer.toString(Integer.parseInt(txt_buc.getText().toString())+1));
            }
        });

        return convertView;
    }
}
