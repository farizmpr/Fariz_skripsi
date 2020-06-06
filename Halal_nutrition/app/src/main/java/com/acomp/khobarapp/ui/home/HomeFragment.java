package com.acomp.khobarapp.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.items.HalalVenuesFragment;
import com.acomp.khobarapp.ui.items.ScanItemsDetailFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.banner_home_1};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        CardView btnSearchItems = (CardView) rootView.findViewById(R.id.btnSearchItems);
        btnSearchItems.setOnClickListener(btnSearchItemsListener);

        CardView btnScanItems = (CardView) rootView.findViewById(R.id.btnScanItems);
        btnScanItems.setOnClickListener(btnScanItemsListener);

        CardView btnHalalVenues = (CardView) rootView.findViewById(R.id.btnHalalVenues);
        btnHalalVenues.setOnClickListener(btnHalalVenuesListener);
//        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
//        btnUpdatePassword.setOnClickListener(updatePasswordListener);

        carouselView = rootView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.type = 1;
        fragmentTransaction.replace(R.id.fragment_content_news_home, newsFragment);
        fragmentTransaction.commit();

        SearchView searchView =
                (SearchView) rootView.findViewById(R.id.btnSearchAllHome);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want when search view expended
//                paramsBack.weight = 1.0f;
//                paramsLnlFav.weight = 8.0f;
//                closeBtn.setLayoutParams(paramsBack);
//                lnlFav.setLayoutParams(paramsLnlFav);
//                titleMenuBar.setVisibility(View.GONE);
//                closeBtn.setGravity();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                paramsBack.weight = 2.0f;
//                paramsLnlFav.weight = 4.0f;
//                closeBtn.setLayoutParams(paramsBack);
//                lnlFav.setLayoutParams(paramsLnlFav);
//                titleMenuBar.setVisibility(View.VISIBLE);
                //do what you want  searchview is not expanded
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
//                querySearch = s;
//                page = 1;
//                listVenuesModel.clear();
//                getListVenues(page, querySearch);
                searchHomeAll(s);
                Log.d("QUERY Submit", "QueryTextSubmit: " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("QUERY CHANGE", "QueryTextChange: " + newText);
                return false;
            }
        });

//        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
//        navBar.setVisibility(View.VISIBLE);
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        return rootView;
    }

    public void searchHomeAll(String text){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        TabHomeSearchAllFragment halalItemsFragment = new TabHomeSearchAllFragment();
        halalItemsFragment.setDefaultTextSearch(text);
        fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
        fragmentTransaction.commit();
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

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    private View.OnClickListener btnSearchItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HalalItemsFragment halalItemsFragment = new HalalItemsFragment();
            fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnScanItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            ScanItemsDetailFragment halalItemsFragment = new ScanItemsDetailFragment();
            fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnHalalVenuesListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HalalVenuesFragment halalVenuesFragment = new HalalVenuesFragment();
            fragmentTransaction.replace(R.id.fragment_content, halalVenuesFragment);
            fragmentTransaction.commit();
        }
    };

}
