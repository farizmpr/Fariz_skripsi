package com.acomp.khobarapp.ui.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.adapter.HeadlineNewsBaseAdapter;
import com.acomp.khobarapp.ui.adapter.HistoryItemsBaseAdapter;
import com.acomp.khobarapp.ui.adapter.ListNewsBaseAdapter;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    public Integer page = 1;
    public Integer type = 0;
    public Integer total = 0;
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.banner_home_1};
    public SwipeRefreshLayout pullToRefresh;
    BottomNavigationView bottomNavigationView = null;

    ListNewsBaseAdapter listNewsBaseAdapter = null;
    ArrayList<NewsModel> listNewsModel = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_news, container, false);

        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //            int Refreshcounter = 1; //Counting how many times user have refreshed the layout
            @Override
            public void onRefresh() {
//                getListNews();
//                Refreshcounter = Refreshcounter + 1;
//                notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });
        NestedScrollView nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scrollViewListNews);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (diff == 0) {
                    Log.i("TAG 1", "BOTTOM SCROLL");

                    if(listNewsModel == null){
                        page = 1;
                        getListNews(page);
                    } else {
                        page = page + 1;
                        if(listNewsModel.size() >= total){

                        } else {
                            getListNews(page);
                        }
                    }
                }
            }
        });
        if(type == 0){
            bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.getMenu().findItem(R.id.nav_news).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.nav_news).setEnabled(false);
        }
        getListNews(this.page);
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        return rootView;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

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
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AccountFragment accountFragment = new AccountFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    public void getListNews(Integer page) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        if(listNewsModel == null){
            listNewsModel = new ArrayList<NewsModel>();
        }

        ArrayList<NewsModel> headNewsModel = new ArrayList<NewsModel>();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Call call = getDataService.getNews(page);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        NewsModel sr1 = null;
                        Integer totalData = jsonObject.getInt("total");
                        total = totalData;
                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                            JSONObject objects = jsonObject.getJSONArray("data").optJSONObject(i);
//                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                            String strDate = objects.getString("strDate");
                            String code = objects.getString("code");
                            String name = objects.getString("name");
                            String content = objects.getString("content");
                            JSONArray jsArrayAt = objects.getJSONArray("image");
                            ArrayList<AttachmentModel> attachmentModels = new ArrayList<AttachmentModel>();
                            if (jsArrayAt.length() > 0) {
                                AttachmentModel attach = null;
                                for (int i2 = 0; i2 < jsArrayAt.length(); i2++) {
                                    JSONObject objectImg = jsArrayAt.optJSONObject(i2);
                                    String path = objectImg.getString("path");
                                    String filename = objectImg.getString("filename");
                                    String type = objectImg.getString("type");
                                    String mime = objectImg.getString("mime");
                                    String url = objectImg.getString("url");
                                    attach = new AttachmentModel();
                                    attach.setPath(path);
                                    attach.setFilename(filename);
                                    attach.setType(type);
                                    attach.setMime(mime);
                                    attach.setUrl(url);
                                    attachmentModels.add(attach);
                                }
                            }
                            sr1 = new NewsModel();
                            sr1.setTitle(name);
                            sr1.setCode(code);
                            sr1.setContent(content);
                            sr1.setStrDate(strDate);
                            sr1.setAttachmentModels(attachmentModels);
//                            if(objects.getJSONArray("certificate").length() >= 1){
//                                String organization = objects.getJSONArray("certificate").optJSONObject(0).getString("organization_name");
////                                sr1.setOrganization(organization);
//                            }
                            if (i == 0) {
                                headNewsModel.add(sr1);
                            }
                            listNewsModel.add(sr1);
//                            itemList.add(new StringWithTag(objects.getString("name"), objects.getInt("id")));
                        }
//                        ListView lv1 = (ListView) getActivity().findViewById(R.id.list_rv_regular);
//                        lv1.setAdapter(new ListNewsBaseAdapter(getActivity(), listNewsModel));
                        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.list_rv_regular);
                        assert recyclerView != null;
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        listNewsBaseAdapter = new ListNewsBaseAdapter(getActivity(), listNewsModel);
                        recyclerView.setAdapter(listNewsBaseAdapter);


                        RecyclerView headRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_headline);
                        assert headRecyclerView != null;
                        headRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        HeadlineNewsBaseAdapter headAdapter = new HeadlineNewsBaseAdapter(getActivity(), headNewsModel);
                        headRecyclerView.setAdapter(headAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
//                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
                if(type == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.nav_news).setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressDoalog.dismiss();
                if(type == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.nav_news).setEnabled(true);
                }
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}