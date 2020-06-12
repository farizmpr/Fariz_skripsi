package com.acomp.khobarapp.ui.items;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.model.NutritionDetailModel;
import com.acomp.khobarapp.model.NutritionModel;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.adapter.HeadlineNewsBaseAdapter;
import com.acomp.khobarapp.ui.adapter.ListFoodBaseAdapter;
import com.acomp.khobarapp.ui.adapter.ListNewsBaseAdapter;
import com.acomp.khobarapp.ui.home.HomeFragment;
import com.acomp.khobarapp.ui.home.TabSearchAllFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalalItemsFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    public String querySearch = "";
    public Integer page = 1;
    public Integer type = 0;
    public Integer total = 0;
    public Boolean isScrollChanged = true;
    public Boolean isSearchNotFound = true;
    public Integer limitItems = null;
    public Integer tabPageType = null;
    public TabLayout tabLayout = null;
    BottomNavigationView bottomNavigationView = null;
    RelativeLayout layItemsNotFound;
    public View viewSearchAll = null;
    ListFoodBaseAdapter listFoodBaseAdapter = null;
    ArrayList<ItemsModel> listFoodModel = null;
    public TabSearchAllFragment tabSearchAllFragment = null;
    public FragmentActivity fragmentActivity = null;
    public Boolean isCheckTab = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_list_food, container, false);
//        SearchManager searchManager =
//                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) rootView.findViewById(R.id.btnViewSearchAllItems);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setActivity(getActivity());
        homeFragment.setNoPaddingAutoComplete(true);
        homeFragment.getSuggetsSearchAutoComplete("suggestSearchAll",searchView);
        TextView titleMenuBar = (TextView) rootView.findViewById(R.id.titleMenuBar);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);
        LinearLayout.LayoutParams paramsBack = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout lnlFav = (RelativeLayout) rootView.findViewById(R.id.lnl_favorite);
        LinearLayout.LayoutParams paramsLnlFav = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout layoutAll = (LinearLayout) rootView.findViewById(R.id.layoutAll);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                closeBtn.setVisibility(View.GONE);
                //do what you want when search view expended
                paramsBack.weight = 1.0f;
                paramsLnlFav.weight = 8.0f;
                closeBtn.setLayoutParams(paramsBack);
                lnlFav.setLayoutParams(paramsLnlFav);
                titleMenuBar.setVisibility(View.GONE);
                layoutAll.setVisibility(View.GONE);
//                closeBtn.setGravity();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                closeBtn.setVisibility(View.VISIBLE);
                paramsBack.weight = 2.0f;
                paramsLnlFav.weight = 4.0f;
                closeBtn.setLayoutParams(paramsBack);
                lnlFav.setLayoutParams(paramsLnlFav);
                titleMenuBar.setVisibility(View.VISIBLE);
                layoutAll.setVisibility(View.VISIBLE);
                //do what you want  searchview is not expanded
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                layoutAll.setVisibility(View.VISIBLE);
                if(isScrollChanged == true) {
                    querySearch = s;
                    page = 1;
                    listFoodModel.clear();
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setActivity(getActivity());
                    homeFragment.saveSuggest("suggestSearchAll", s);
                    getListFood(page, querySearch);
                    Log.d("QUERY Submit", "QueryTextSubmit: " + s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("QUERY CHANGE", "QueryTextChange: " + newText);
                return false;
            }
        });
        NestedScrollView nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scrollViewHalalItems);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (diff == 0) {
                    Log.i("TAG 1", "BOTTOM SCROLL");

                    if (listFoodModel == null) {
                        page = 1;
                        getListFood(page, querySearch);
                    } else {
                        page = page + 1;
                        if (listFoodModel.size() >= total) {

                        } else {
                            getListFood(page, querySearch);
                        }
                    }
                }
            }
        });
        layItemsNotFound = (RelativeLayout) rootView.findViewById(R.id.layItemsNotFound);
        layItemsNotFound.setVisibility(View.GONE);
        getListFood(this.page, this.querySearch);

        LinearLayout headLineItems = (LinearLayout) rootView.findViewById(R.id.toolbar_items);
        if(this.type == 1){
            headLineItems.setVisibility(View.GONE);
        }


        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HomeFragment accountFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    public void getListFood(Integer page, String search) {
        Log.d("WriteSearch", search);
        if(fragmentActivity == null){
            fragmentActivity = getActivity();
        }
        progressDoalog = new ProgressDialog(fragmentActivity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        if (listFoodModel == null) {
            listFoodModel = new ArrayList<ItemsModel>();
        }
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call call = getDataService.getListFood(page, search, 1);
        if(isCheckTab == false) {
            layItemsNotFound.setVisibility(View.GONE);
        }
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        ItemsModel sr1 = null;
                        Integer totalData = jsonObject.getInt("total");
                        Log.d("TOTAL JSON", String.valueOf(totalData));
                        total = totalData;
                        if(isCheckTab == true){
                            if(totalData == 0){
                                tabLayout.removeTabAt(tabPageType);
                            }
                        } else {
                            for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                                JSONObject objects = jsonObject.getJSONArray("data").optJSONObject(i);
//                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                                String ingredient = objects.getString("ingredient");
                                String code = objects.getString("code");
                                String name = objects.getString("name");
                                String manufacture = objects.getString("manufacture");
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
                                        String url = null;
                                        if (objectImg.isNull("url")) {
                                            url = null;
                                        } else {
                                            url = objectImg.getString("url");
                                        }

                                        attach = new AttachmentModel();
                                        attach.setPath(path);
                                        attach.setFilename(filename);
                                        attach.setType(type);
                                        attach.setMime(mime);
                                        attach.setUrl(url);
                                        attachmentModels.add(attach);
                                    }
                                }
                                JSONArray jsArrayCert = objects.getJSONArray("certificate");
                                ArrayList<CertificateRowModel> certificateRowModels = new ArrayList<CertificateRowModel>();
                                if (jsArrayCert.length() > 0) {
                                    CertificateRowModel certMod = null;
                                    for (int i2 = 0; i2 < jsArrayCert.length(); i2++) {
                                        JSONObject objectCert = jsArrayCert.optJSONObject(i2);
                                        String codeCert = null;
                                        if (!objectCert.isNull("code")) {
                                            codeCert = objectCert.getString("code");
                                        }
                                        String nameCert = null;
                                        if (!objectCert.isNull("name")) {
                                            nameCert = objectCert.getString("name");
                                        }
                                        String organizationName = null;
                                        if (!objectCert.isNull("organization_name")) {
                                            organizationName = objectCert.getString("organization_name");
                                        }

                                        String expiredDate = null;
                                        if (!objectCert.isNull("expired_date")) {
                                            expiredDate = objectCert.getString("expired_date");
                                        }


                                        certMod = new CertificateRowModel();
                                        certMod.setCode(codeCert);
                                        certMod.setTitle(organizationName);
                                        certMod.setExpiredDate(expiredDate);
//                                    attach.setType(type);
//                                    attach.setMime(mime);
//                                    attach.setUrl(url);
                                        certificateRowModels.add(certMod);
                                    }
                                }

                                JSONObject jsObjNutri = objects.getJSONObject("nutrition");

                                //GET NUTRITION SERVING
                                JSONArray jsObjNutriServ = jsObjNutri.getJSONArray("serving");
                                NutritionModel nutritionModel = new NutritionModel();
                                ArrayList<NutritionDetailModel> nutritionDetailModelsServ = new ArrayList<NutritionDetailModel>();
                                if (jsObjNutriServ.length() > 0) {
                                    NutritionDetailModel nuDeModServ = null;
                                    for (int i2 = 0; i2 < jsObjNutriServ.length(); i2++) {
                                        JSONObject objectServ = jsObjNutriServ.optJSONObject(i2);
                                        String codeServ = null;
                                        if (!objectServ.isNull("code")) {
                                            codeServ = objectServ.getString("code");
                                        }
                                        String nameServ = null;
                                        if (!objectServ.isNull("name")) {
                                            nameServ = objectServ.getString("name");
                                        }
                                        Double valueServ = null;
                                        if (!objectServ.isNull("value")) {
                                            valueServ = objectServ.getDouble("value");
                                        }
                                        String unitCodeServ = null;
                                        if (!objectServ.isNull("unit_code")) {
                                            unitCodeServ = objectServ.getString("unit_code");
                                        }
                                        Double percentageServ = null;
                                        if (!objectServ.isNull("percentage")) {
                                            percentageServ = objectServ.getDouble("percentage");
                                        }

                                        nuDeModServ = new NutritionDetailModel();
                                        nuDeModServ.setCode(codeServ);
                                        nuDeModServ.setName(nameServ);
                                        nuDeModServ.setValue(valueServ);
                                        nuDeModServ.setUnitCode(unitCodeServ);
                                        nuDeModServ.setPercentage(percentageServ);

                                        JSONArray jsObjNutriChildServ = objectServ.getJSONArray("child");
                                        ArrayList<NutritionDetailModel> nutritionDetailModelsChildServ = new ArrayList<NutritionDetailModel>();
                                        if (jsObjNutriChildServ.length() > 0) {
                                            NutritionDetailModel nuDeModChildServ = null;
                                            for (int i22 = 0; i22 < jsObjNutriChildServ.length(); i22++) {
                                                JSONObject objectChildServ = jsObjNutriChildServ.optJSONObject(i22);
                                                String codeChildServ = null;
                                                if (!objectChildServ.isNull("code")) {
                                                    codeChildServ = objectChildServ.getString("code");
                                                }
                                                String nameChildServ = null;
                                                if (!objectChildServ.isNull("name")) {
                                                    nameChildServ = objectChildServ.getString("name");
                                                }
                                                Double valueChildServ = null;
                                                if (!objectChildServ.isNull("value")) {
                                                    valueChildServ = objectChildServ.getDouble("value");
                                                }
                                                String unitCodeChildServ = null;
                                                if (!objectChildServ.isNull("unit_code")) {
                                                    unitCodeChildServ = objectChildServ.getString("unit_code");
                                                }
                                                Double percentageChildServ = null;
                                                if (!objectChildServ.isNull("percentage")) {
                                                    percentageChildServ = objectChildServ.getDouble("percentage");
                                                }

                                                nuDeModChildServ = new NutritionDetailModel();
                                                nuDeModChildServ.setCode(codeChildServ);
                                                nuDeModChildServ.setName(nameChildServ);
                                                nuDeModChildServ.setValue(valueChildServ);
                                                nuDeModChildServ.setUnitCode(unitCodeChildServ);
                                                nuDeModChildServ.setPercentage(percentageChildServ);
                                                nutritionDetailModelsChildServ.add(nuDeModChildServ);
                                            }

                                        }
                                        nuDeModServ.setChild(nutritionDetailModelsChildServ);
                                        nutritionDetailModelsServ.add(nuDeModServ);
                                    }
                                }
                                nutritionModel.setServing(nutritionDetailModelsServ);

                                //GET NUTRITION DAILY VALUE
                                JSONArray jsObjNutriDai = jsObjNutri.getJSONArray("daily_value");
                                NutritionModel nutritionModelDai = new NutritionModel();
                                ArrayList<NutritionDetailModel> nutritionDetailModelsDai = new ArrayList<NutritionDetailModel>();
                                if (jsObjNutriDai.length() > 0) {
                                    NutritionDetailModel nuDeModDai = null;
                                    for (int i2 = 0; i2 < jsObjNutriDai.length(); i2++) {
                                        JSONObject objectDai = jsObjNutriDai.optJSONObject(i2);
                                        String codeDai = null;
                                        if (!objectDai.isNull("code")) {
                                            codeDai = objectDai.getString("code");
                                        }
                                        String nameDai = null;
                                        if (!objectDai.isNull("name")) {
                                            nameDai = objectDai.getString("name");
                                        }
                                        Double valueDai = null;
                                        if (!objectDai.isNull("value")) {
                                            valueDai = objectDai.getDouble("value");
                                        }
                                        String unitCodeDai = null;
                                        if (!objectDai.isNull("unit_code")) {
                                            unitCodeDai = objectDai.getString("unit_code");
                                        }
                                        Double percentageDai = null;
                                        if (!objectDai.isNull("percentage")) {
                                            percentageDai = objectDai.getDouble("percentage");
                                        }

                                        nuDeModDai = new NutritionDetailModel();
                                        nuDeModDai.setCode(codeDai);
                                        nuDeModDai.setName(nameDai);
                                        nuDeModDai.setValue(valueDai);
                                        nuDeModDai.setUnitCode(unitCodeDai);
                                        nuDeModDai.setPercentage(percentageDai);
                                        nutritionDetailModelsDai.add(nuDeModDai);
                                        JSONArray jsObjNutriChildDai = objectDai.getJSONArray("child");
                                        ArrayList<NutritionDetailModel> nutritionDetailModelsChildDai = new ArrayList<NutritionDetailModel>();
                                        if (jsObjNutriChildDai.length() > 0) {
                                            NutritionDetailModel nuDeModChildDai = null;
                                            for (int i22 = 0; i22 < jsObjNutriChildDai.length(); i22++) {
                                                JSONObject objectChildDai = jsObjNutriChildDai.optJSONObject(i22);
                                                String codeChildDai = null;
                                                if (!objectChildDai.isNull("code")) {
                                                    codeChildDai = objectChildDai.getString("code");
                                                }
                                                String nameChildDai = null;
                                                if (!objectChildDai.isNull("name")) {
                                                    nameChildDai = objectChildDai.getString("name");
                                                }
                                                Double valueChildDai = null;
                                                if (!objectChildDai.isNull("value")) {
                                                    valueChildDai = objectChildDai.getDouble("value");
                                                }
                                                String unitCodeChildDai = null;
                                                if (!objectChildDai.isNull("unit_code")) {
                                                    unitCodeChildDai = objectChildDai.getString("unit_code");
                                                }
                                                Double percentageChildDai = null;
                                                if (!objectChildDai.isNull("percentage")) {
                                                    percentageChildDai = objectChildDai.getDouble("percentage");
                                                }

                                                nuDeModChildDai = new NutritionDetailModel();
                                                nuDeModChildDai.setCode(codeChildDai);
                                                nuDeModChildDai.setName(nameChildDai);
                                                nuDeModChildDai.setValue(valueChildDai);
                                                nuDeModChildDai.setUnitCode(unitCodeChildDai);
                                                nuDeModChildDai.setUrutChild(2);
                                                nuDeModChildDai.setPercentage(percentageChildDai);
                                                nutritionDetailModelsChildDai.add(nuDeModChildDai);
                                                nutritionDetailModelsDai.add(nuDeModChildDai);
                                            }
//                                        nutritionDetailModelsDai.add(nuDeModChildDai);
                                        }
                                        nuDeModDai.setChild(nutritionDetailModelsChildDai);

                                    }
                                }
                                NutritionDetailModel nuDeModChildDaiMore = new NutritionDetailModel();
                                nuDeModChildDaiMore.setType("more");
                                nutritionDetailModelsDai.add(nuDeModChildDaiMore);
                                nutritionModel.setDailyValue(nutritionDetailModelsDai);


                                sr1 = new ItemsModel();
                                sr1.setName(name);
                                sr1.setCode(code);
                                sr1.setIngredient(ingredient);
                                sr1.setManufacture(manufacture);
                                sr1.setOrganization(manufacture);
                                sr1.setAttachmentModels(attachmentModels);
                                sr1.setCertificateRowModels(certificateRowModels);
                                sr1.setNutrition(nutritionModel);
//                            if(objects.getJSONArray("certificate").length() >= 1){
//                                String organization = objects.getJSONArray("certificate").optJSONObject(0).getString("organization_name");
////                                sr1.setOrganization(organization);
//                            }
                                listFoodModel.add(sr1);
//                            itemList.add(new StringWithTag(objects.getString("name"), objects.getInt("id")));
                            }
//                        ListView lv1 = (ListView) getActivity().findViewById(R.id.list_rv_regular);
//                        lv1.setAdapter(new ListNewsBaseAdapter(getActivity(), listNewsModel));
                            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_list_food);
                            assert recyclerView != null;
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            listFoodBaseAdapter = new ListFoodBaseAdapter(getActivity(), listFoodModel, limitItems);
                            listFoodBaseAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(listFoodBaseAdapter);

                            if (totalData == 0) {
                                if (viewSearchAll != null) {
                                    LinearLayout headSearchTabs = (LinearLayout) viewSearchAll.findViewById(R.id.head_items_search_all);
                                    headSearchTabs.setVisibility(View.GONE);
                                    FrameLayout frameSearchTabs = (FrameLayout) viewSearchAll.findViewById(R.id.fragment_search_all_items);
                                    frameSearchTabs.setVisibility(View.GONE);

                                    if (tabSearchAllFragment != null) {
//                                        tabLayout.removeTabAt(tabPageType);
                                        tabSearchAllFragment.isRemove = true;
                                    }
                                }
                                if (isSearchNotFound == true) {
                                    layItemsNotFound.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        if(isCheckTab == false) {
                            layItemsNotFound.setVisibility(View.VISIBLE);
                            Toast.makeText(fragmentActivity, "Data Not Found", Toast.LENGTH_SHORT).show();
                        }
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

                Toast.makeText(fragmentActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
