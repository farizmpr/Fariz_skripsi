package com.acomp.khobarapp.ui.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.IngredientDetailModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NutritionDetailModel;
import com.acomp.khobarapp.ui.items.NutritionFragment;

import java.util.ArrayList;

public class ListIngredientDetailAdapter extends RecyclerView.Adapter<ListIngredientDetailAdapter.IngredientDetailViewHolder> {
    private static ArrayList<IngredientDetailModel> searchArrayList;
    private ItemsModel itemsModel;
    private LayoutInflater mInflater;
    FragmentActivity context;
    String url = null;

    public ListIngredientDetailAdapter(FragmentActivity c, ArrayList<IngredientDetailModel> arrayList, ItemsModel itemsModelList) {
        context = c;
        searchArrayList = arrayList;
        itemsModel = itemsModelList;
    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    @Override
    public IngredientDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_status_ingredients, parent, false);
        return new IngredientDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientDetailViewHolder holder, int position) {
        IngredientDetailModel object = searchArrayList.get(position);

        String type = object.getType();
        String title = object.getTitle();
        String description = object.getDescription();
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();

        holder.txtTitle.setText(title);
        holder.txtType.setText(type);
        holder.txtDescription.setText(description);
        holder.historyItemsArrowBtn.setOnClickListener(view -> {
            if (holder.layDescIngDetail.getVisibility() == View.GONE) {
                holder.historyItemsArrowBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_black_24dp));
//                holder.historyItemsArrowBtn.setVisibility(View.VISIBLE);
                holder.layDescIngDetail.setVisibility(View.VISIBLE);
            } else {
                holder.historyItemsArrowBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp));
//                holder.historyItemsArrowBtn.setVisibility(View.GONE);
                holder.layDescIngDetail.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class IngredientDetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtType;
        TextView txtDescription;
        RelativeLayout layIngredientDetail;
        ConstraintLayout layDescIngDetail;
        ImageView historyItemsArrowBtn;

        IngredientDetailViewHolder(View itemView) {
            super(itemView);
            historyItemsArrowBtn = (ImageView) itemView.findViewById(R.id.historyItemsArrowBtn);
            layIngredientDetail = (RelativeLayout) itemView.findViewById(R.id.layIngredientDetail);
            layDescIngDetail = (ConstraintLayout) itemView.findViewById(R.id.layDescIngDetail);
            txtTitle = (TextView) itemView.findViewById(R.id.historyItemsTitle);
            txtType = (TextView) itemView.findViewById(R.id.lblStatus);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
        }


//        ImageView btnRemoveCertificate;
    }

}
