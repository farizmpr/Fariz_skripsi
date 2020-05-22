package com.acomp.khobarapp.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.acomp.khobarapp.model.AttachmentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailFoodPagerAdapter extends PagerAdapter {
    private static ArrayList<AttachmentModel> searchArrayList;
    String url = null;
    private FragmentActivity context;
    public DetailFoodPagerAdapter(FragmentActivity fm, ArrayList<AttachmentModel> arrayList) {
        context = fm;
        this.searchArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return searchArrayList.size();
    }
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        AttachmentModel object = searchArrayList.get(position);
        url = object.getUrl();
        if (url == null) {
            url = "https://via.placeholder.com/468x250?text=No+Image";
        }
        ImageView imageView = new ImageView(context);
        collection.addView(imageView);
        Picasso.get().load(url).into(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


}
