package com.acomp.khobarapp.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
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

        CardView closeBtn = (CardView) rootView.findViewById(R.id.btnSearchItems);
        closeBtn.setOnClickListener(btnSearchItemsListener);

//        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
//        btnUpdatePassword.setOnClickListener(updatePasswordListener);

        carouselView = rootView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);


        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.type = 1;
        fragmentTransaction.replace(R.id.fragment_content_news_home, newsFragment);
        fragmentTransaction.commit();

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



}
