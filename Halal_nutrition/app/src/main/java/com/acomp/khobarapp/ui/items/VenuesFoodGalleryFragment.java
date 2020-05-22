package com.acomp.khobarapp.ui.items;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.adapter.FoodVenuesGalleryAdapter;
import com.acomp.khobarapp.ui.adapter.ListFoodVenuesBaseAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class VenuesFoodGalleryFragment extends Fragment {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    Gallery simpleGallery;
    FoodVenuesGalleryAdapter foodVenuesGalleryAdapter;
    ImageView selectedImageView;
    VenuesModel venuesModel = new VenuesModel();
    private static ArrayList<AttachmentModel> searchArrayList;
    public String urlImage = null;
    ListFoodVenuesBaseAdapter listFoodVenuesBaseAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_venues_food_gallery, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);
        searchArrayList = venuesModel.getAttachmentModels();
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listFoodVenues);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setHasFixedSize(true); // Helps improve performance
        listFoodVenuesBaseAdapter = new ListFoodVenuesBaseAdapter(getActivity(), venuesModel.getAttachmentModels(),venuesModel);

        mRecyclerView.setAdapter(listFoodVenuesBaseAdapter);


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
            HalalVenuesDetailFragment halalVenuesDetailFragment = new HalalVenuesDetailFragment();
            halalVenuesDetailFragment.setVenuesModel(venuesModel);
            halalVenuesDetailFragment.setVenuesModel(venuesModel);
            fragmentTransaction.replace(R.id.fragment_content, halalVenuesDetailFragment);
            fragmentTransaction.commit();
        }
    };


}
