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
import com.release.deviz.dataClasses.data_class_produs;

import java.util.ArrayList;

public class ProduseOferteAdapter extends BaseAdapter {
    final private Context context;
    final private ArrayList<data_class_produs> list;
    final private ArrayList<Integer> bucati;
    final private boolean factura;

    ArrayList<TextView> produse = new ArrayList<>();

    TextView txt_buc;

    public ProduseOferteAdapter(Context context, ArrayList<data_class_produs> list, ArrayList<Integer> bucati, boolean factura)
    {
        this.context = context;
        this.list = list;
        this.bucati = bucati;
        this.factura = factura;
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
        txt_buc = convertView.findViewById(R.id.of_prod_row_buc);

        ImageView img_prod = convertView.findViewById(R.id.of_prod_row_img);
        ImageView img_minus = convertView.findViewById(R.id.of_prod_img_minus);
        ImageView img_add = convertView.findViewById(R.id.of_prod_img_add);

        data_class_produs current_item = list.get(position);

        txt_nume.setText(current_item.getNume());
        txt_cod.setText(current_item.getCod());
        txt_pret.setText(Float.toString(current_item.getPret()));
        txt_buc.setText(Integer.toString(bucati.get(position)));

        img_prod.setImageBitmap(current_item.getImg());

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int nr_buc = Integer.valueOf(txt_buc.getText().toString());
                ((ListView) parent).performItemClick(v, position, img_minus.getId());
                //if(nr_buc > 1)
                   // txt_buc.setText(Integer.toString(nr_buc - 1));
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //int nr_buc = Integer.valueOf(txt_buc.getText().toString());
                ((ListView) parent).performItemClick(v, position, img_add.getId());
               // txt_buc.setText(Integer.toString(nr_buc + 1));
            }
        });

        return convertView;
    }
}
