package com.acomp.khobarapp.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.NutritionDetailModel;

import java.util.ArrayList;

public class ListDetailIngredientAdapter extends RecyclerView.Adapter<ListDetailIngredientAdapter.ListDetailIngredientViewHolder> {
    private static String[] searchArrayList;

    private Integer limit = 3;

    private LayoutInflater mInflater;
    FragmentActivity context;
    String url = null;

    public ListDetailIngredientAdapter(FragmentActivity c, String[] arrayList,Integer limitData) {
        context = c;
        searchArrayList = arrayList;
        limit = limitData;
    }

    public void addListNews(FragmentActivity c, String[] arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount() {

        if (limit == null) {
            return searchArrayList.length;
        } else if (searchArrayList.length > limit) {
            return limit;
        } else {
            return searchArrayList.length;
        }

    }

    @Override
    public ListDetailIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_ingredient, parent, false);
        return new ListDetailIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListDetailIngredientViewHolder holder, int position) {
//        NutritionDetailModel object = searchArrayList.get(position);
        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < searchArrayList.length; i++) {
            holder.txtListIngredient.setText(searchArrayList[position]);
//        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ListDetailIngredientViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView txtListIngredient;
        LinearLayout layDetailIngredient;

        ListDetailIngredientViewHolder(View itemView) {
            super(itemView);
            layDetailIngredient = (LinearLayout) itemView.findViewById(R.id.layDetailIngredient);
            txtListIngredient = (TextView) itemView.findViewById(R.id.txtListIngredient);
//            txtSubCircleNutrition = (TextView) itemView.findViewById(R.id.txtSubCircleNutrition);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
