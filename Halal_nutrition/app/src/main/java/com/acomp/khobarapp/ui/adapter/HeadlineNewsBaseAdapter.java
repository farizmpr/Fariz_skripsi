package com.acomp.khobarapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.ui.news.NewsDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HeadlineNewsBaseAdapter extends RecyclerView.Adapter<HeadlineNewsBaseAdapter.NewsViewHolder> {
    private static ArrayList<NewsModel> searchArrayList;

    private LayoutInflater mInflater;
    FragmentActivity context;
    public HeadlineNewsBaseAdapter(FragmentActivity c,ArrayList<NewsModel> arrayList) {
        context = c;
        searchArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_headline, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsModel object = searchArrayList.get(position);

        // My example assumes CustomClass objects have getFirstText() and getSecondText() methods
        String firstText = object.getTitle();
        String secondText = object.getStrDate();

        holder.txtTitleRow.setText(firstText);
        holder.txtStrDate.setText(secondText);
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

        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        holder.newsHeadlineBox.setOnClickListener(new View.OnClickListener() {
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


    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleRow;
        TextView txtStrDate;
        CardView newsHeadlineBox;
        ImageView imgPoster;
        NewsViewHolder(View itemView) {
            super(itemView);
            txtTitleRow = (TextView) itemView.findViewById(R.id.tv_title);
            txtStrDate = (TextView) itemView.findViewById(R.id.tv_source);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
            newsHeadlineBox = (CardView) itemView.findViewById(R.id.newsHeadlineBox);
        }
//        ImageView btnRemoveCertificate;
    }
}
