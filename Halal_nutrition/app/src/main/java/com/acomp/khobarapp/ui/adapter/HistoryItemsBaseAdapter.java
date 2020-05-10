package com.acomp.khobarapp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;

import java.util.ArrayList;

public class HistoryItemsBaseAdapter extends BaseAdapter {
    private static ArrayList<ItemsModel> searchArrayList;

    private LayoutInflater mInflater;

    public HistoryItemsBaseAdapter(Context context, ArrayList<ItemsModel> results) {
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
            convertView = mInflater.inflate(R.layout.list_history_item, null);
            holder = new ViewHolder();
            holder.txtTitleRow = (TextView) convertView.findViewById(R.id.historyItemsTitle);
            holder.txtSubtitleRow = (TextView) convertView.findViewById(R.id.historyItemsSubtitle);
            holder.lblStatus = (TextView) convertView.findViewById(R.id.lblStatus);
            holder.txtOrganizationName = (TextView) convertView.findViewById(R.id.organizationName);
            holder.txtIngredientName = (TextView) convertView.findViewById(R.id.ingredientName);
            holder.btnDownUp = (ImageView) convertView.findViewById(R.id.historyItemsArrowBtn);
            holder.expandableView = (LinearLayout) convertView.findViewById(R.id.expandableView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (searchArrayList.get(position).getName() != null) {
            holder.txtTitleRow.setText(searchArrayList.get(position).getName());
        }
        if (searchArrayList.get(position).getManufacture() != null) {
            holder.txtSubtitleRow.setText(searchArrayList.get(position).getManufacture());
        }
        holder.txtOrganizationName.setText(searchArrayList.get(position).getOrganization());
        holder.txtIngredientName.setText(searchArrayList.get(position).getIngredient());
//        Log.d("ITEMDTO", searchArrayList.get(position).getStatus());
        Integer statusItems = Integer.parseInt(searchArrayList.get(position).getStatus());
        if (statusItems == 0) {
            holder.lblStatus.setTextColor(Color.parseColor("#FFCA28"));
            Drawable d = parent.getResources().getDrawable(R.drawable.border_warning);
            holder.lblStatus.setBackground(d);
            holder.lblStatus.setText("Waiting");
        } else if (statusItems == 2) {
            holder.lblStatus.setTextColor(Color.parseColor("#EF5350"));
            Drawable ds = parent.getResources().getDrawable(R.drawable.border_danger);
            holder.lblStatus.setBackground(ds);
            holder.lblStatus.setText("Disapproved");
        }
        holder.expandableView.setVisibility(View.GONE);
//        String lblStatus = holder.lblStatus.getText();
//        holder.txtSubtitleRow.setText(searchArrayList.get(position).getManufacture());
        View finalConvertView = convertView;
        holder.btnDownUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Integer vis = holder.btnDownUp.getVisibility();
                if (holder.expandableView.getVisibility() == View.VISIBLE) {
                    Drawable ds = parent.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    holder.btnDownUp.setBackground(ds);
                    holder.expandableView.setVisibility(View.GONE);
                } else {
                    Drawable ds = parent.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    holder.btnDownUp.setBackground(ds);
                    holder.expandableView.setVisibility(View.VISIBLE);
                }
//                searchArrayList.remove(position);
//                notifyDataSetChanged();
            }

        });

        return finalConvertView;
    }


    static class ViewHolder {
        TextView txtTitleRow;
        TextView txtSubtitleRow;
        TextView txtIngredientName;
        TextView txtOrganizationName;
        TextView lblStatus;
        ImageView btnDownUp;
        LinearLayout expandableView;
    }
}
