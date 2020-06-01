package com.acomp.khobarapp.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.NutritionDetailModel;

import java.util.ArrayList;

public class ListServingNutritionAdapter extends RecyclerView.Adapter<ListServingNutritionAdapter.ServingNutritionViewHolder> {
    private static ArrayList<NutritionDetailModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;
    String url = null;

    public ListServingNutritionAdapter(FragmentActivity c, ArrayList<NutritionDetailModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
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
    public ServingNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_serving_nutrition, parent, false);
        return new ServingNutritionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServingNutritionViewHolder holder, int position) {
        NutritionDetailModel object = searchArrayList.get(position);

        String name = object.getName();
        Double percentage = object.getPercentage();
        Double value = object.getValue();
        String unitCode = object.getUnitCode();
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        if (name != null) {
            String txtServing = name;
            if (value != null) {
                String strValue = value.toString();
                txtServing += " "+strValue;
            }
            holder.txtServing.setText(txtServing);
        }
//
//        if(value != null ){
//            String txtSubCircle = value.toString();
//            if (unitCode != null) {
////                String strPercentage = unitCode;
//                txtSubCircle += " "+unitCode;
//            }
//            holder.txtSubCircleNutrition.setText(txtSubCircle);
//        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ServingNutritionViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView txtServing;
        LinearLayout layServeNutrition;

        ServingNutritionViewHolder(View itemView) {
            super(itemView);
            layServeNutrition = (LinearLayout) itemView.findViewById(R.id.layServeNutrition);
            txtServing = (TextView) itemView.findViewById(R.id.txtServing);
//            txtSubCircleNutrition = (TextView) itemView.findViewById(R.id.txtSubCircleNutrition);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
