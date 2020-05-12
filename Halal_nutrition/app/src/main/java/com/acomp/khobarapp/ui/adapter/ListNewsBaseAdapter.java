package com.acomp.khobarapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.NewsModel;

import java.util.ArrayList;

public class ListNewsBaseAdapter extends BaseAdapter {
    private static ArrayList<NewsModel> searchArrayList;

    private LayoutInflater mInflater;

    public ListNewsBaseAdapter(Context context, ArrayList<NewsModel> results) {
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
            convertView = mInflater.inflate(R.layout.news_regular, null);
            holder = new ViewHolder();
            holder.txtTitleRow = (TextView) convertView.findViewById(R.id.tv_title);
            holder.txtStrDate = (TextView) convertView.findViewById(R.id.tv_source);
//            holder.btnRemoveCertificate = (ImageView) convertView.findViewById(R.id.btnRemoveCertificate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitleRow.setText(searchArrayList.get(position).getTitle());
        holder.txtStrDate.setText(searchArrayList.get(position).getStrDate());
        View finalConvertView = convertView;


        return finalConvertView;
    }



    static class ViewHolder {
        TextView txtTitleRow;
        TextView txtStrDate;
//        ImageView btnRemoveCertificate;
    }
}
