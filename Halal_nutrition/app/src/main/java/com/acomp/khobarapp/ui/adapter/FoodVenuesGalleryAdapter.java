package com.acomp.khobarapp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodVenuesGalleryAdapter extends BaseAdapter {

    private FragmentActivity context;
    private static ArrayList<AttachmentModel> searchArrayList;
    private Integer type = 1;

    public FoodVenuesGalleryAdapter(FragmentActivity c, ArrayList<AttachmentModel> arrayList,Integer frType) {
        context = c;
        this.searchArrayList = arrayList;
        type = frType;
    }

    // returns the number of images
    public int getCount() {
        return searchArrayList.size();
    }

    // returns the ID of an item
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        AttachmentModel object = searchArrayList.get(position);
        // create a ImageView programmatically

        ImageView imageView = new ImageView(context);
        String url = object.getUrl();
        if (url != null) {
            Picasso.get().load(url).fit().centerInside().into(imageView);
        } else {
            Picasso.get().load("https://via.placeholder.com/468x250?text=No+Image").fit().centerInside().into(imageView);
        }
//        imageView.setImageResource(images[position]); // set image in ImageView
        if(type == 1){
            imageView.setLayoutParams(new Gallery.LayoutParams(200, 200)); // set ImageView param
        } else {
            imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return imageView;
    }
}
