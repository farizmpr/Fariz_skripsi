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
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.ui.items.HalalItemsDetailFragment;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.news.NewsDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListFoodBaseAdapter extends RecyclerView.Adapter<ListFoodBaseAdapter.ItemsViewHolder> {
    private static ArrayList<ItemsModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;
    private Integer limit = 3;

    public ListFoodBaseAdapter(FragmentActivity c, ArrayList<ItemsModel> arrayList,Integer limitData) {
        context = c;
        searchArrayList = arrayList;
        limit = limitData;
    }

    public void addListNews(FragmentActivity c, ArrayList<ItemsModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        if (limit == null) {
            return searchArrayList.size();
        } else if (searchArrayList.size() > limit) {
            return limit;
        } else {
            return searchArrayList.size();
        }
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_regular, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        ItemsModel object = searchArrayList.get(position);

        // My example assumes CustomClass objects have getFirstText() and getSecondText() methods
        String firstText = object.getName();
        String ingredientText = object.getIngredient();
        String statusHalalText = "Halal";
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        holder.txtTitleRow.setText(firstText);
        holder.txtIngredient.setText("Tipe : " + ingredientText);
        holder.txtStatusHalal.setText("Status : " + statusHalalText);

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
            Picasso.get().load(imageUrl).into(holder.imgPoster);
        }
        holder.foodRegularBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HalalItemsDetailFragment halalItemsFragment = new HalalItemsDetailFragment();
                halalItemsFragment.setItemsModel(object);
                fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
                fragmentTransaction.commit();
            }

        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView txtTitleRow;
        TextView txtIngredient;
        TextView txtStatusHalal;
        ImageView imgPoster;
        ConstraintLayout foodRegularBox;

        ItemsViewHolder(View itemView) {
            super(itemView);
            txtTitleRow = (TextView) itemView.findViewById(R.id.tv_title);
            txtIngredient = (TextView) itemView.findViewById(R.id.tv_source);
            txtStatusHalal = (TextView) itemView.findViewById(R.id.status_halal);
            foodRegularBox = (ConstraintLayout) itemView.findViewById(R.id.listFoodRegularBox);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
