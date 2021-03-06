package com.acomp.khobarapp.ui.items;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
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
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.adapter.ListFoodBaseAdapter;
import com.acomp.khobarapp.ui.adapter.ListVenuesBaseAdapter;
import com.acomp.khobarapp.ui.home.HomeFragment;
import com.acomp.khobarapp.ui.home.TabSearchAllFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalalVenuesFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    public String querySearch = "";
    public Integer page = 1;
    public Integer type = 0;
    public Integer total = 0;
    BottomNavigationView bottomNavigationView = null;
    RelativeLayout layItemsNotFound;
    public Boolean isScrollChanged = true;
    public Boolean isSearchNotFound = true;
    public Boolean isCheckTab = false;
    public Integer limitItems = null;
    public TabLayout tabLayout = null;
    public View viewSearchAll = null;
    public Integer tabPageType = null;
    public TabSearchAllFragment tabSearchAllFragment = null;
    public FragmentActivity fragmentActivity = null;

    ListVenuesBaseAdapter listVenuesBaseAdapter = null;
    ArrayList<VenuesModel> listVenuesModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_list_venues, container, false);
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
        NestedScrollView scrollViewHalalItems = (NestedScrollView) rootView.findViewById(R.id.scrollViewHalalItems);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want when search view expended
//                closeBtn.setVisibility(View.GONE);
                paramsBack.weight = 1.0f;
                paramsLnlFav.weight = 8.0f;
                closeBtn.setLayoutParams(paramsBack);
                lnlFav.setLayoutParams(paramsLnlFav);
                titleMenuBar.setVisibility(View.GONE);
                layoutAll.setVisibility(View.GONE);
                scrollViewHalalItems.setVisibility(View.GONE);
//                closeBtn.setGravity();
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutAll.setVisibility(View.GONE);
                    scrollViewHalalItems.setVisibility(View.GONE);
                } else {
                    // searchView not expanded
                }
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
                scrollViewHalalItems.setVisibility(View.VISIBLE);
                //do what you want  searchview is not expanded
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                layoutAll.setVisibility(View.VISIBLE);
                scrollViewHalalItems.setVisibility(View.VISIBLE);
                if(isScrollChanged == true) {

                    querySearch = s;
                    page = 1;
                    listVenuesModel.clear();

                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setActivity(getActivity());
                    homeFragment.saveSuggest("suggestSearchAll", s);
                    getListVenues(page, querySearch);
                    Log.d("QUERY Submit", "QueryTextSubmit: " + s);
//                                        searchView.setQuery(querySearch, false);
                    searchView.clearFocus();
                }
                return true;
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

                    if (listVenuesModel == null) {
                        page = 1;
                        getListVenues(page, querySearch);
                    } else {
                        page = page + 1;
                        if (listVenuesModel.size() >= total) {

                        } else {
                            getListVenues(page, querySearch);
                        }
                    }
                }
            }
        });
        layItemsNotFound = (RelativeLayout) rootView.findViewById(R.id.layItemsNotFound);
        layItemsNotFound.setVisibility(View.GONE);

        getListVenues(this.page, this.querySearch);

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

    public void getListVenues(Integer page, String search) {
        Log.d("WriteSearch", search);
        if(fragmentActivity == null){
            fragmentActivity = getActivity();
        }
        progressDoalog = new ProgressDialog(fragmentActivity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        if (listVenuesModel == null) {
            listVenuesModel = new ArrayList<VenuesModel>();
        }
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call call = getDataService.getListVenues(page, search, 1);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    if(isCheckTab == false) {
                        layItemsNotFound.setVisibility(View.GONE);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        VenuesModel sr1 = null;
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
                                BigInteger id = BigInteger.valueOf(objects.getLong("id"));
                                String code = objects.getString("code");
                                String name = objects.getString("name");
                                String foodType = objects.getString("food_type");
                                String restaurantStatusId = objects.getString("restaurant_status_id");
                                String address = objects.getString("address");
                                String longitude = "";
                                if (!objects.isNull("longitude")) {
                                    longitude = objects.getString("longitude");
                                }
                                String latitude = "";
                                if (!objects.isNull("latitude")) {
                                    latitude = objects.getString("latitude");
                                }
                                String phoneNumber = objects.getString("phone_number");
                                String restaurantStatus = objects.getString("restaurant_status");
                                Double votes = objects.getDouble("votes");
                                JSONArray jsArrayAt = objects.getJSONArray("image");
                                ArrayList<AttachmentModel> attachmentModels = new ArrayList<AttachmentModel>();
                                if (jsArrayAt.length() > 0) {
                                    AttachmentModel attach = null;
                                    for (int i2 = 0; i2 < jsArrayAt.length(); i2++) {
                                        JSONObject objectImg = jsArrayAt.optJSONObject(i2);
                                        String path = null;
                                        if (objectImg.isNull("path")) {
                                            path = null;
                                        } else {
                                            path = objectImg.getString("path");
                                        }
                                        String filename = null;
                                        if (objectImg.isNull("filename")) {
                                            filename = null;
                                        } else {
                                            filename = objectImg.getString("filename");
                                        }
                                        String type = null;
                                        if (objectImg.isNull("type")) {
                                            type = null;
                                        } else {
                                            type = objectImg.getString("type");
                                        }
                                        String mime = null;
                                        if (objectImg.isNull("mime")) {
                                            mime = null;
                                        } else {
                                            mime = objectImg.getString("mime");
                                        }
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

                                sr1 = new VenuesModel();
                                sr1.setId(id);
                                sr1.setName(name);
                                sr1.setCode(code);
                                sr1.setFoodType(foodType);
                                sr1.setRestaurantStatusId(restaurantStatusId);
                                sr1.setAddress(address);
                                sr1.setRestaurantStatus(restaurantStatus);
                                sr1.setVotes(votes);
                                sr1.setLongitude(longitude);
                                sr1.setLatitude(latitude);
                                sr1.setPhoneNumber(phoneNumber);
                                sr1.setAttachmentModels(attachmentModels);
                                listVenuesModel.add(sr1);
                            }
                            if (totalData == 0) {
                                if (viewSearchAll != null) {
                                    LinearLayout headSearchTabs = (LinearLayout) viewSearchAll.findViewById(R.id.head_venues_search_all);
                                    headSearchTabs.setVisibility(View.GONE);
                                    FrameLayout frameSearchTabs = (FrameLayout) viewSearchAll.findViewById(R.id.fragment_search_all_venues);
                                    frameSearchTabs.setVisibility(View.GONE);
//                                tabLayout.getChildAt(tabPageType).setVisibility(View.GONE);
                                    if (tabSearchAllFragment != null) {
//                                        tabLayout.removeTabAt(tabPageType);
                                        tabSearchAllFragment.isRemove = true;
                                    }
                                }
                                if (isSearchNotFound == true) {
                                    layItemsNotFound.setVisibility(View.VISIBLE);
                                }
                            }
                            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_list_venues);
                            assert recyclerView != null;
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            listVenuesBaseAdapter = new ListVenuesBaseAdapter(getActivity(), listVenuesModel, limitItems);
                            listVenuesBaseAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(listVenuesBaseAdapter);
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
