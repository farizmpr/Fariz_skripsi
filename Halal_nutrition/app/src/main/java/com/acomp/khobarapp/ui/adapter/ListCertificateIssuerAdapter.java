package com.acomp.khobarapp.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.CertificateRowModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListCertificateIssuerAdapter extends RecyclerView.Adapter<ListCertificateIssuerAdapter.ListCertificateIssuerViewHolder> {
    private static ArrayList<CertificateRowModel> searchArrayList;

    private Integer limit = 3;

    private LayoutInflater mInflater;
    FragmentActivity context;
    String url = null;

    public ListCertificateIssuerAdapter(FragmentActivity c, ArrayList<CertificateRowModel> arrayList, Integer limitData) {
        context = c;
        searchArrayList = arrayList;
        limit = limitData;
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
    public ListCertificateIssuerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_certificate_issuer, parent, false);
        return new ListCertificateIssuerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListCertificateIssuerViewHolder holder, int position) {
        CertificateRowModel object = searchArrayList.get(position);

        holder.certificateIssuerName.setText(object.getTitle());
        SimpleDateFormat month_date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String actualDate = "2016-03-20";
        if(object.getExpiredDate() != null){
            actualDate = object.getExpiredDate();
        }

        Date fixDateParse = null;
        try {
            fixDateParse = sdf.parse(actualDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String expDate = month_date.format(fixDateParse);
        holder.certificateIssuerED.setText("Due "+expDate);
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < searchArrayList.length; i++) {
//            holder.txtListIngredient.setText(searchArrayList[position]);
//        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ListCertificateIssuerViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView certificateIssuerName,certificateIssuerED;
        LinearLayout layCertificateIssuer;

        ListCertificateIssuerViewHolder(View itemView) {
            super(itemView);
            layCertificateIssuer = (LinearLayout) itemView.findViewById(R.id.layCertificateIssuer);
            certificateIssuerName = (TextView) itemView.findViewById(R.id.certificateIssuerName);
            certificateIssuerED = (TextView) itemView.findViewById(R.id.certificateIssuerED);
//            txtSubCircleNutrition = (TextView) itemView.findViewById(R.id.txtSubCircleNutrition);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES", scrollX + "-" + scrollY + "=" + oldScrollX + "-" + oldScrollY);
        }
//        ImageView btnRemoveCertificate;
    }

}
