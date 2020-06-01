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

public class ListDailyValueNutritionAdapter extends RecyclerView.Adapter<ListDailyValueNutritionAdapter.DailyValueNutritionViewHolder> {
    private static ArrayList<NutritionDetailModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;
    Integer urut = null;

    public ListDailyValueNutritionAdapter(FragmentActivity c, ArrayList<NutritionDetailModel> arrayList, Integer urutChild) {
        context = c;
        searchArrayList = arrayList;
        urut = urutChild;
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
    public DailyValueNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_daily_value_nutrition, parent, false);
        return new DailyValueNutritionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyValueNutritionViewHolder holder, int position) {
        NutritionDetailModel object = searchArrayList.get(position);

        String name = object.getName();
        Double percentage = object.getPercentage();
        Double value = object.getValue();
        String unitCode = object.getUnitCode();
        Integer urutC = object.getUrutChild();
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        if (name != null) {
            String txtServing = name;
            if (value != null) {
                String strValue = value.toString();
                txtServing += " " + strValue;
            }
            if (unitCode != null) {
                txtServing += " " + unitCode;
            }
            holder.txtDailyValue.setText(txtServing);
            if (urutC == 2) {
                holder.txtDailyValue.setPadding(30, 0, 0, 0);
            }
        }

        if (percentage != null) {
            String strPercentage = percentage.toString();
            holder.txtPercentageDailyValue.setText(strPercentage + " % ");
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class DailyValueNutritionViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView txtDailyValue;
        TextView txtPercentageDailyValue;
        LinearLayout layDailyValueNutrition;

        DailyValueNutritionViewHolder(View itemView) {
            super(itemView);
            layDailyValueNutrition = (LinearLayout) itemView.findViewById(R.id.layDailyValueNutrition);
            txtDailyValue = (TextView) itemView.findViewById(R.id.txtDailyValue);
            txtPercentageDailyValue = (TextView) itemView.findViewById(R.id.txtPercentageDailyValue);
//            txtSubCircleNutrition = (TextView) itemView.findViewById(R.id.txtSubCircleNutrition);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
