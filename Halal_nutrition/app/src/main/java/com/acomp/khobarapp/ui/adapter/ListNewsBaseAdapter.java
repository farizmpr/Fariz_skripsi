package com.acomp.khobarapp.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.ui.news.NewsDetailFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListNewsBaseAdapter extends RecyclerView.Adapter<ListNewsBaseAdapter.NewsViewHolder> {
    private static ArrayList<NewsModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;
    private Integer limit = 3;

    public ListNewsBaseAdapter(FragmentActivity c, ArrayList<NewsModel> arrayList,Integer limitData) {
        context = c;
        searchArrayList = arrayList;
        limit = limitData;
    }

    public void addListNews(FragmentActivity c, ArrayList<NewsModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount()
    {
//        return searchArrayList.size();
        if (limit == null) {
            return searchArrayList.size();
        } else if (searchArrayList.size() > limit) {
            return limit;
        } else {
            return searchArrayList.size();
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_regular, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsModel object = searchArrayList.get(position);

        // My example assumes CustomClass objects have getFirstText() and getSecondText() methods
        String firstText = object.getTitle();
        String secondText = object.getStrDate();

        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        holder.txtTitleRow.setText(firstText);
        holder.txtStrDate.setText(secondText);

//        ArrayList<AttachmentModel> attachmentModels = object.getAttachmentModels();
        String imageUrl = null;
        Integer no=0;
        for(AttachmentModel attachmentModel: object.getAttachmentModels()){
            if(no == 0){
                imageUrl = attachmentModel.getUrl();
            }
            no++;
        }
        if(imageUrl != null){
            Picasso.get().load(imageUrl).into(holder.imgPoster);
        }
        holder.newsRegularBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailFragment accountFragment1 = new NewsDetailFragment();
                accountFragment1.setNewsModel(object);
                fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                fragmentTransaction.commit();
            }

        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnScrollChangeListener {
        TextView txtTitleRow;
        TextView txtStrDate;
        ImageView imgPoster;
        ConstraintLayout newsRegularBox;
        NewsViewHolder(View itemView) {
            super(itemView);
            txtTitleRow = (TextView) itemView.findViewById(R.id.tv_title);
            txtStrDate = (TextView) itemView.findViewById(R.id.tv_source);
            newsRegularBox = (ConstraintLayout) itemView.findViewById(R.id.newsRegularBox);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.d("TES",scrollX+"-"+scrollY+"="+oldScrollX+"-"+oldScrollY);
//            NewsFragment newsFragment = new NewsFragment();
//            if (v.getVerticalScrollbarPosition() == 0) {
//                Log.d("MASUK","UP");
//                newsFragment.pullToRefresh.setRefreshing(true);
//            } else {
//                Log.d("MASUK","DOWN");
//                newsFragment.pullToRefresh.setRefreshing(false);
//            }
        }
//        ImageView btnRemoveCertificate;
    }

}
