package com.release.deviz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_client;

import java.util.ArrayList;

public class ClientiExpandableAdapter extends BaseExpandableListAdapter
{
    final private Context context;
    private ArrayList<data_class_client> list = new ArrayList<>();

    public ClientiExpandableAdapter(Context c, ArrayList<data_class_client> list)
    {
        this.context = c;

        this.list = list;
    }

    @Override
    public int getGroupCount()
    {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return list.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.client_row, parent, false);

        TextView txt_denumire = convertView.findViewById(R.id.row_client_denumire);
        TextView txt_localitate = convertView.findViewById(R.id.row_client_localitate);
        TextView txt_judet = convertView.findViewById(R.id.row_client_judet);

        data_class_client current_item = list.get(groupPosition);

        txt_denumire.setText(current_item.getDenumire());
        txt_localitate.setText(current_item.getLocalitate());
        txt_judet.setText(current_item.getJudet());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.client_row_info, parent, false);

        TextView txt_denumire = convertView.findViewById(R.id.exp_row_client_denumire);
        TextView txt_cif = convertView.findViewById(R.id.exp_row_client_cif);
        TextView txt_reg_com = convertView.findViewById(R.id.exp_row_client_reg_com);
        TextView txt_platitor_tva = convertView.findViewById(R.id.exp_row_client_platitor_tva);
        TextView txt_localitate = convertView.findViewById(R.id.exp_row_client_localitate);
        TextView txt_judet = convertView.findViewById(R.id.exp_row_client_judet);
        TextView txt_adresa = convertView.findViewById(R.id.exp_row_client_adresa);
        TextView txt_email = convertView.findViewById(R.id.exp_row_client_email);
        TextView txt_pers_contact = convertView.findViewById(R.id.exp_row_client_pers_contact);
        TextView txt_telefon = convertView.findViewById(R.id.exp_row_client_telefon);

        data_class_client current_item = list.get(groupPosition);

        txt_denumire.setText(current_item.getDenumire());
        txt_cif.setText(current_item.getCif());
        txt_reg_com.setText(current_item.getReg_com());

        if(current_item.isPlatitor_tva())
            txt_platitor_tva.setText("da");
        else
            txt_platitor_tva.setText("nu");

        txt_localitate.setText(current_item.getLocalitate());
        txt_judet.setText(current_item.getJudet());
        txt_adresa.setText(current_item.getAdresa());
        txt_email.setText(current_item.getEmail());
        txt_pers_contact.setText(current_item.getPers_contact());
        txt_telefon.setText(current_item.getTelefon());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
