package com.acomp.khobarapp.ui.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.adapter.HeadlineNewsBaseAdapter;
import com.acomp.khobarapp.ui.adapter.ListNewsBaseAdapter;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailFragment extends Fragment {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    NewsModel newsModel = new NewsModel();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detailed, container, false);
        ImageView closeBtn = (ImageView) rootView.findViewById(R.id.GoBackIcon);
        closeBtn.setOnClickListener(goBackListener);

        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(newsModel.getTitle());
        TextView tvTimeStamp = (TextView) rootView.findViewById(R.id.tv_time_stamp);
        tvTimeStamp.setText(newsModel.getStrDate());
        TextView tvDescription = (TextView) rootView.findViewById(R.id.tv_description);
        String content = newsModel.getContent();
        content = content.replace("\\\n", System.getProperty("line.separator"));
        tvDescription.setText(content);
        ImageView imgPoster = (ImageView) rootView.findViewById(R.id.img_poster);
        String imageUrl = null;
        Integer no=0;
        for(AttachmentModel attachmentModel: newsModel.getAttachmentModels()){
            if(no == 0){
                imageUrl = attachmentModel.getUrl();
            }
            no++;
        }
        if(imageUrl != null){
            Picasso.get().load(imageUrl).fit()
                    .centerCrop().into(imgPoster);
        }


        return rootView;
    }

    public NewsModel setNewsModel(NewsModel newsModel){
        this.newsModel = newsModel;
        return this.newsModel;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_fetch);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            NewsFragment accountFragment = new NewsFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };



}
