package com.acomp.khobarapp.ui.items;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.adapter.DetailFoodPagerAdapter;
import com.acomp.khobarapp.ui.adapter.FoodVenuesGalleryAdapter;
import com.acomp.khobarapp.ui.adapter.ListFoodVenuesBaseAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VenuesFoodGalleryDetailFragment extends Fragment implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    Gallery simpleGallery;
    FoodVenuesGalleryAdapter foodVenuesGalleryAdapter;
    ImageView selectedImageView;
    VenuesModel venuesModel = new VenuesModel();
    private static ArrayList<AttachmentModel> searchArrayList;
    public String urlImage = null;
    ListFoodVenuesBaseAdapter listFoodVenuesBaseAdapter = null;
    public ViewPager viewPager;
    public Gallery gallery;
    private Handler handler;
    public Integer positionIndex = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_venues_food_gallery_detail, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);
        searchArrayList = venuesModel.getAttachmentModels();
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new DetailFoodPagerAdapter(getActivity(),venuesModel.getAttachmentModels()));
        viewPager.setOnPageChangeListener(this);


        gallery = (Gallery) rootView.findViewById(R.id.gallery);
        gallery.setAdapter(new FoodVenuesGalleryAdapter(getActivity(),venuesModel.getAttachmentModels(),2));
        gallery.setOnItemSelectedListener(this);
        handler = new Handler();

        if(positionIndex != null){
            gallery.setSelection(positionIndex);
            viewPager.setCurrentItem(positionIndex);
        }
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
//        navBar.setVisibility(View.VISIBLE);
        return rootView;
    }


    public VenuesModel setVenuesModel(VenuesModel itemsModel) {
        this.venuesModel = itemsModel;
        return this.venuesModel;
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
            VenuesFoodGalleryFragment halalVenuesDetailFragment = new VenuesFoodGalleryFragment();
            halalVenuesDetailFragment.setVenuesModel(venuesModel);
            fragmentTransaction.replace(R.id.fragment_content, halalVenuesDetailFragment);
            fragmentTransaction.commit();
        }
    };
    public void updateUI(final int i) {
        handler.post(new Runnable() {
            public void run() {
                gallery.setSelection(i);
                viewPager.setCurrentItem(i);
//                textView.setText("Photo #" + i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.d("DEBUG SELECT","POSITION="+position);
        updateUI(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateUI(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
