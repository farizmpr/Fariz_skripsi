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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.NutritionFact;
import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.model.NutritionDetailModel;
import com.acomp.khobarapp.model.NutritionModel;
import com.acomp.khobarapp.ui.adapter.ListCircleNutritionAdapter;
import com.acomp.khobarapp.ui.adapter.ListDetailIngredientAdapter;
import com.acomp.khobarapp.ui.adapter.ListFoodVenuesBaseAdapter;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class HalalItemsDetailFragment extends Fragment {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    ItemsModel itemsModel = new ItemsModel();
    CarouselView carouselView;
    String[] sampleImages;
    List<String> mImages = new ArrayList<String>();
    ListCircleNutritionAdapter listCircleNutritionAdapter = null;
    ArrayList<NutritionDetailModel> nutritionDetailModels = null;
    ListDetailIngredientAdapter listDetailIngredientAdapter = null;
    TextView btnShowMoreIngredient, btnHideMoreIngredient;
    RecyclerView recyclerViewIngredient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detil_food, container, false);
        ImageView closeBtn = (ImageView) rootView.findViewById(R.id.GoBackIcon);
        closeBtn.setOnClickListener(goBackListener);

        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(itemsModel.getName());
        TextView tvManufacture = (TextView) rootView.findViewById(R.id.manufacture);
        tvManufacture.setText("\u25CF " + itemsModel.getManufacture());
//        TextView tvIngredient = (TextView) rootView.findViewById(R.id.ingredient_list);
//        String ingredient = itemsModel.getIngredient();
//        ingredient = ingredient.replace("\\\n", System.getProperty("line.separator"));
//        tvIngredient.setText("\u25CF " + ingredient);
//        ImageView imgPoster = (ImageView) rootView.findViewById(R.id.img_poster);
        carouselView = rootView.findViewById(R.id.carouselViewDetailFood);

        String imageUrl = null;
        Integer no = 0;
        if (itemsModel.getAttachmentModels().size() > 0) {
            for (AttachmentModel attachmentModel : itemsModel.getAttachmentModels()) {
                imageUrl = attachmentModel.getUrl();
                if (imageUrl == null) {
                    mImages.add("https://via.placeholder.com/468x250?text=No+Image");
                } else {
                    mImages.add(imageUrl);
                }
//            Picasso.get().load(imageUrl).fit().centerCrop().into(imgPoster);
                no++;
            }
        } else {
            mImages.add("https://via.placeholder.com/468x250?text=No+Image");
        }
        if (imageUrl != null) {
//            Picasso.get().load(imageUrl).fit()
//                    .centerCrop().into(imgPoster);
        }


        Log.d("TOTAL IMAGES", "SIZE=" + mImages.size());
        carouselView.setPageCount(mImages.size());
        carouselView.setImageListener(imageListener);
        TextView tvCertificateName = (TextView) rootView.findViewById(R.id.item_description);
        String txtOrganizationName = "";
        for (CertificateRowModel certificateRowModel : itemsModel.getCertificateRowModels()) {
            String organiName = certificateRowModel.getTitle();
            txtOrganizationName = "\u25CF " + organiName + "\n";
        }
        txtOrganizationName = txtOrganizationName.replace("\\\n", System.getProperty("line.separator"));
        tvCertificateName.setText(txtOrganizationName);

//        NutritionDetailModel nuDeModChildDaiMore = new NutritionDetailModel();
////        ArrayList<NutritionDetailModel> nutritionDetailModelsDai = new ArrayList<NutritionDetailModel>();
//        nuDeModChildDaiMore.setType("more");
//        ArrayList<NutritionDetailModel> nutritionDetailModelsDai = itemsModel.getNutrition().getDailyValue();
//        nutritionDetailModelsDai.add(nuDeModChildDaiMore);

//        ArrayList<NutritionDetailModel> nutritionDetailModelsServ = itemsModel.getNutrition().getServing();
//        NutritionModel nuModel = new NutritionModel();
//        nuModel.setServing(nutritionDetailModelsServ);
//        nuModel.setDailyValue(nutritionDetailModelsDai);
//        itemsModel.setNutrition(nuModel);
//        itemsModel.getNutrition().setDailyValue(nutritionDetailModelsDai);

        getListCircleNutrition(rootView);
        getListDetailIngredient(rootView);

//        TextView btnViewMore = (TextView) rootView.findViewById(R.id.btnViewMore);
//        btnViewMore.setOnClickListener(btnViewMoreListener);

        btnShowMoreIngredient = (TextView) rootView.findViewById(R.id.Show_More_Ingredients);
        btnShowMoreIngredient.setOnClickListener(btnShowMoreIngredientListener);

        btnHideMoreIngredient = (TextView) rootView.findViewById(R.id.Hide_More_Ingredients);
        btnHideMoreIngredient.setVisibility(View.GONE);
        btnHideMoreIngredient.setOnClickListener(btnHideMoreIngredientListener);


//        nutritionDetailModelsDai.add(nuDeModChildDaiMore);


        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
            Picasso.get().load(mImages.get(position)).into(imageView);
        }
    };

    public ItemsModel setItemsModel(ItemsModel itemsModel) {
        this.itemsModel = itemsModel;
        return this.itemsModel;
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
            HalalItemsFragment accountFragment = new HalalItemsFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnViewMoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            NutritionFragment nutritionFragment = new NutritionFragment();
            nutritionFragment.setItemsModel(itemsModel);
            fragmentTransaction.replace(R.id.fragment_content, nutritionFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnShowMoreIngredientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String ingredient = itemsModel.getIngredient();
            String[] splitIngredient = ingredient.split(";");
            listDetailIngredientAdapter = new ListDetailIngredientAdapter(getActivity(), splitIngredient, null);
            listDetailIngredientAdapter.notifyDataSetChanged();
            recyclerViewIngredient.setAdapter(listDetailIngredientAdapter);
            btnHideMoreIngredient.setVisibility(View.VISIBLE);
            btnShowMoreIngredient.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener btnHideMoreIngredientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String ingredient = itemsModel.getIngredient();
            String[] splitIngredient = ingredient.split(";");
            listDetailIngredientAdapter = new ListDetailIngredientAdapter(getActivity(), splitIngredient, 3);
            listDetailIngredientAdapter.notifyDataSetChanged();
            recyclerViewIngredient.setAdapter(listDetailIngredientAdapter);
            btnHideMoreIngredient.setVisibility(View.GONE);
            btnShowMoreIngredient.setVisibility(View.VISIBLE);
        }
    };

    public void getListCircleNutrition(View view) {
//        if (listVenuesFoodModel == null) {
//            listVenuesFoodModel = new ArrayList<AttachmentModel>();
//        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listCircleNutrition);
        assert recyclerView != null;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        listCircleNutritionAdapter = new ListCircleNutritionAdapter(getActivity(), itemsModel.getNutrition().getDailyValue(),itemsModel);

        recyclerView.setAdapter(listCircleNutritionAdapter);
    }

    public void getListDetailIngredient(View view) {

        String ingredient = itemsModel.getIngredient();
        String[] splitIngredient = ingredient.split(";");
        recyclerViewIngredient = (RecyclerView) view.findViewById(R.id.listDetailIngredient);
        assert recyclerViewIngredient != null;
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewIngredient.setLayoutManager(mLayoutManager);
        listDetailIngredientAdapter = new ListDetailIngredientAdapter(getActivity(), splitIngredient, 3);

        recyclerViewIngredient.setAdapter(listDetailIngredientAdapter);
    }


}
