package com.acomp.khobarapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.ui.account.AddItemsFragment;

import java.util.ArrayList;

public class MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<CertificateRowModel> searchArrayList;

    private LayoutInflater mInflater;

    public MyCustomBaseAdapter(Context context, ArrayList<CertificateRowModel> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.add_row_certificate, null);
            holder = new ViewHolder();
            holder.txtTitleRow = (TextView) convertView.findViewById(R.id.titleRowCertificate);
            holder.txtTypeRow = (TextView) convertView.findViewById(R.id.typeRowCertificate);
            holder.btnRemoveCertificate = (ImageView) convertView.findViewById(R.id.btnRemoveCertificate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitleRow.setText(searchArrayList.get(position).getTitle());
        holder.txtTypeRow.setText("Tipe : "+searchArrayList.get(position).getType());
        View finalConvertView = convertView;
        holder.btnRemoveCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArrayList.remove(position);
                notifyDataSetChanged();
            }

        });

        return finalConvertView;
    }



    static class ViewHolder {
        TextView txtTitleRow;
        TextView txtTypeRow;
        ImageView btnRemoveCertificate;
    }
}
