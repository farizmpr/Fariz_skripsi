package com.acomp.khobarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder> {

    private Context context;
    private List<App> apps;

    public CustomAdaptor(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mName,mSize;
        ImageView mImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mSize = itemView.findViewById(R.id.size);
            mImage = itemView.findViewById(R.id.image);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_list_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        App app = apps.get(position);
        holder.mName.setText(app.getName());
        holder.mSize.setText(app.getSize()+"Size");
        holder.mImage.setImageResource(app.getImage());

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

}