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
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.adapter.HistoryItemsBaseAdapter;
import com.acomp.khobarapp.ui.adapter.ListNewsBaseAdapter;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.banner_home_1};
    SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_news, container, false);


//        CardView closeBtn = (CardView) rootView.findViewById(R.id.btnSearchItems);
//        closeBtn.setOnClickListener(goBackListener);

//        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
//        btnUpdatePassword.setOnClickListener(updatePasswordListener);

//        carouselView = rootView.findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
//        carouselView.setImageListener(imageListener);
        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //            int Refreshcounter = 1; //Counting how many times user have refreshed the layout
            @Override
            public void onRefresh() {
                getListNews();
//                Refreshcounter = Refreshcounter + 1;
//                notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

        getListNews();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_news).setChecked(true);
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AccountFragment accountFragment = new AccountFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    public void getListNews(){
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        ArrayList<NewsModel> listNewsModel =  new ArrayList<NewsModel>();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Call call = getDataService.getNews();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        NewsModel sr1 =null;
                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                            JSONObject objects = jsonObject.getJSONArray("data").optJSONObject(i);
//                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                            String strDate = objects.getString("strDate");
                            String code = objects.getString("code");
                            String name = objects.getString("name");
                            String content = objects.getString("content");

                            sr1 = new NewsModel();
                            sr1.setTitle(name);
                            sr1.setCode(code);
                            sr1.setContent(content);
                            sr1.setStrDate(strDate);
//                            if(objects.getJSONArray("certificate").length() >= 1){
//                                String organization = objects.getJSONArray("certificate").optJSONObject(0).getString("organization_name");
////                                sr1.setOrganization(organization);
//                            }

                            listNewsModel.add(sr1);
//                            itemList.add(new StringWithTag(objects.getString("name"), objects.getInt("id")));
                        }
                        ListView lv1 = (ListView) getActivity().findViewById(R.id.list_rv_regular);
                        lv1.setAdapter(new ListNewsBaseAdapter(getActivity(), listNewsModel));
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
//                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
