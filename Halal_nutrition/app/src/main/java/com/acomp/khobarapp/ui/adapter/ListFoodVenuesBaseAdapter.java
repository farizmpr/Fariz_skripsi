package com.acomp.khobarapp.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.items.HalalVenuesDetailFragment;
import com.acomp.khobarapp.ui.items.VenuesFoodGalleryDetailFragment;
import com.acomp.khobarapp.ui.items.VenuesFoodGalleryFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListFoodVenuesBaseAdapter extends RecyclerView.Adapter<ListFoodVenuesBaseAdapter.FoodVenuesViewHolder> {
    private static ArrayList<AttachmentModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;
    private static VenuesModel venuesModel;
    String url = null;

    public ListFoodVenuesBaseAdapter(FragmentActivity c, ArrayList<AttachmentModel> arrayList, VenuesModel venuesModels) {
        context = c;
        searchArrayList = arrayList;
        venuesModel = venuesModels;
    }

    public void addListNews(FragmentActivity c, ArrayList<AttachmentModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    @Override
    public FoodVenuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menuphotos, parent, false);
        return new FoodVenuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodVenuesViewHolder holder, int position) {
        AttachmentModel object = searchArrayList.get(position);

        url = object.getUrl();
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        if (url != null) {
            Picasso.get().load(url).into(holder.image);
        } else {
            url = "https://via.placeholder.com/468x250?text=No+Image";
            Picasso.get().load(url).into(holder.image);
        }
        holder.layVenuesFoodPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VenuesFoodGalleryDetailFragment venuesFoodGalleryFragment = new VenuesFoodGalleryDetailFragment();
                venuesFoodGalleryFragment.setVenuesModel(venuesModel);
                venuesFoodGalleryFragment.positionIndex = position;
//                venuesFoodGalleryFragment.gallery.setSelection(position);
//                venuesFoodGalleryFragment.viewPager.setCurrentItem(position);
                fragmentTransaction.replace(R.id.fragment_content, venuesFoodGalleryFragment);
                fragmentTransaction.commit();
//                venuesFoodGalleryFragment.updateUI(position);
            }

        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class FoodVenuesViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        ImageView image;
        LinearLayout layVenuesFoodPhoto;

        FoodVenuesViewHolder(View itemView) {
            super(itemView);
            layVenuesFoodPhoto = (LinearLayout) itemView.findViewById(R.id.layVenuesFoodPhoto);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
