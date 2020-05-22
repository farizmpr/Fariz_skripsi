package com.acomp.khobarapp.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.items.HalalItemsDetailFragment;
import com.acomp.khobarapp.ui.items.HalalVenuesDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListVenuesBaseAdapter extends RecyclerView.Adapter<ListVenuesBaseAdapter.VenuesViewHolder> {
    private static ArrayList<VenuesModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;

    public ListVenuesBaseAdapter(FragmentActivity c, ArrayList<VenuesModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    public void addListNews(FragmentActivity c, ArrayList<VenuesModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    @Override
    public VenuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halal_venues_item, parent, false);
        return new VenuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenuesViewHolder holder, int position) {
        VenuesModel object = searchArrayList.get(position);

        // My example assumes CustomClass objects have getFirstText() and getSecondText() methods
        String name = object.getName();
        String foodType = object.getFoodType();
        String restaurantStatus = object.getRestaurantStatus();
        String address = object.getAddress();
        String statusHalalText = "Halal";
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        holder.txtTitleRow.setText(name);
        holder.txtRestaurantType.setText(foodType);
        holder.txtVenuesStatus.setText(restaurantStatus);
        holder.txtVenuesAddress.setText(address);
//        holder.txtStatusHalal.setText("Status : " + statusHalalText);

//        ArrayList<AttachmentModel> attachmentModels = object.getAttachmentModels();
        String imageUrl = null;
        Integer no = 0;
        for (AttachmentModel attachmentModel : object.getAttachmentModels()) {
            if (no == 0) {
                imageUrl = attachmentModel.getUrl();
            }
            no++;
        }
        if (imageUrl != null) {
            Log.d("IMAGE URL",imageUrl);
            Picasso.get().load(imageUrl).into(holder.imgPoster);
        } else {
            Picasso.get().load("https://via.placeholder.com/468x250?text=No+Image").into(holder.imgPoster);
        }
        holder.venuesItemBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HalalVenuesDetailFragment halalItemsFragment = new HalalVenuesDetailFragment();
                halalItemsFragment.setVenuesModel(object);
                fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
                fragmentTransaction.commit();
            }

        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class VenuesViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView txtTitleRow;
        TextView txtRestaurantType;
        TextView txtVenuesStatus;
        TextView txtVenuesAddress;
        ImageView imgPoster;
        ConstraintLayout venuesItemBox;

        VenuesViewHolder(View itemView) {
            super(itemView);
            txtTitleRow = (TextView) itemView.findViewById(R.id.venuesTitle);
            txtRestaurantType = (TextView) itemView.findViewById(R.id.restaurantType);
            txtVenuesStatus = (TextView) itemView.findViewById(R.id.venuesStatus);
            txtVenuesAddress = (TextView) itemView.findViewById(R.id.venuesAddress);
            venuesItemBox = (ConstraintLayout) itemView.findViewById(R.id.listVenuesItemBox);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
