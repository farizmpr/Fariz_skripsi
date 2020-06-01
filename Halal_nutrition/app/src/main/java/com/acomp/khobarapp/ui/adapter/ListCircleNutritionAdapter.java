package com.acomp.khobarapp.ui.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NutritionDetailModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.detail.DetailedActivity;
import com.acomp.khobarapp.ui.items.NutritionFragment;
import com.acomp.khobarapp.ui.items.VenuesFoodGalleryDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListCircleNutritionAdapter extends RecyclerView.Adapter<ListCircleNutritionAdapter.CircleNutritionViewHolder> {
    private static ArrayList<NutritionDetailModel> searchArrayList;
    private ItemsModel itemsModel;
    private LayoutInflater mInflater;
    FragmentActivity context;
    String url = null;

    public ListCircleNutritionAdapter(FragmentActivity c, ArrayList<NutritionDetailModel> arrayList, ItemsModel itemsModelList) {
        context = c;
        searchArrayList = arrayList;
        itemsModel = itemsModelList;
    }

    public void addListNews(FragmentActivity c, ArrayList<NutritionDetailModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    @Override
    public CircleNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_circle_nutrition, parent, false);
        return new CircleNutritionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CircleNutritionViewHolder holder, int position) {
        NutritionDetailModel object = searchArrayList.get(position);

        String type = object.getType();
        String name = object.getName();
        Double percentage = object.getPercentage();
        Double value = object.getValue();
        String unitCode = object.getUnitCode();
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        Integer sizing = (getItemCount() - 1);
        Log.d("SIZING ", "SIZE=" + sizing.toString());
        Log.d("POSITION ", "POSITION=" + position);
        Log.d("DATA ", "DATA=" + name + "-" + type);
        if (position == (getItemCount() - 1)) {
//            Integer sizing = searchArrayList.size() - 1;
            if (type == "more") {
                if (name == null) {
                    holder.txtCircleNutrition.setText("View More");
                    holder.txtCircleNutrition.setTextSize(16);
                    holder.txtCircleNutrition.setTextColor(Color.WHITE);
                    holder.txtCircleNutrition.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_textview_black));
                    holder.txtSubCircleNutrition.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(view -> {
                        NutritionFragment nutritionFragment = new NutritionFragment();
                        nutritionFragment.setItemsModel(itemsModel);
                        fragmentTransaction.replace(R.id.fragment_content, nutritionFragment);
                        fragmentTransaction.commit();
                    });
                }
            }

        } else {
            holder.txtCircleNutrition.setTextSize(14);
            holder.txtCircleNutrition.setTextColor(Color.BLACK);
            holder.txtSubCircleNutrition.setVisibility(View.VISIBLE);
            holder.txtCircleNutrition.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_textview_gray));
            holder.itemView.setOnClickListener(view -> {
            });
            if (name != null) {
                String txtCircle = name;
                if (percentage != null) {
                    String strPercentage = percentage.toString();
                    txtCircle += "\n" + strPercentage;
                }
                holder.txtCircleNutrition.setText(txtCircle);
            }

            if (value != null) {
                String txtSubCircle = value.toString();
                if (unitCode != null) {
//                String strPercentage = unitCode;
                    txtSubCircle += " " + unitCode;
                }
                holder.txtSubCircleNutrition.setText(txtSubCircle);
            }
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class CircleNutritionViewHolder extends RecyclerView.ViewHolder {
        TextView txtCircleNutrition;
        TextView txtSubCircleNutrition;
        RelativeLayout layCircleNutrition;

        CircleNutritionViewHolder(View itemView) {
            super(itemView);
            layCircleNutrition = (RelativeLayout) itemView.findViewById(R.id.layCircleNutrition);
            txtCircleNutrition = (TextView) itemView.findViewById(R.id.txtCircleNutrition);
            txtSubCircleNutrition = (TextView) itemView.findViewById(R.id.txtSubCircleNutrition);
        }


//        ImageView btnRemoveCertificate;
    }

}
