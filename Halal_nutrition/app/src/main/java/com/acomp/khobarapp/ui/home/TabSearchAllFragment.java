package com.acomp.khobarapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.items.HalalVenuesFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.google.android.material.tabs.TabLayout;

public class TabSearchAllFragment extends Fragment {

    String defaultTextSearch = "";
    public Integer type = 0;
    public Integer isConnect = 1;
    public TabLayout tabLayout = null;
    public Boolean isRemove = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        NewsFragment newsFragment = new NewsFragment();
        HalalItemsFragment halalItemsFragment = new HalalItemsFragment();
        HalalVenuesFragment halalVenuesFragment = new HalalVenuesFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransactionItems = getActivity().getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransactionVenues = getActivity().getSupportFragmentManager().beginTransaction();
        if (type == 0) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_all, container, false);
            LinearLayout headSearchTabs = (LinearLayout) rootView.findViewById(R.id.head_news_search_all);
            headSearchTabs.setVisibility(View.VISIBLE);
            FrameLayout frameSearchTabs = (FrameLayout) rootView.findViewById(R.id.fragment_search_all_news);
            frameSearchTabs.setVisibility(View.VISIBLE);

            LinearLayout headSearchTabs2 = (LinearLayout) rootView.findViewById(R.id.head_items_search_all);
            headSearchTabs2.setVisibility(View.VISIBLE);
            FrameLayout frameSearchTabs2 = (FrameLayout) rootView.findViewById(R.id.fragment_search_all_items);
            frameSearchTabs2.setVisibility(View.VISIBLE);

            LinearLayout headSearchTabs3 = (LinearLayout) rootView.findViewById(R.id.head_items_search_all);
            headSearchTabs3.setVisibility(View.VISIBLE);
            FrameLayout frameSearchTabs3 = (FrameLayout) rootView.findViewById(R.id.fragment_search_all_items);
            frameSearchTabs3.setVisibility(View.VISIBLE);

            if (isConnect == 1) {
                newsFragment.tabPageType = 2;
                newsFragment.tabLayout = tabLayout;
                newsFragment.type = 1;
                newsFragment.isScrollChanged = false;
                newsFragment.limitNews = 3;
                newsFragment.tabSearchAllFragment = this;
                newsFragment.isSearchNotFound = false;
                newsFragment.viewSearchAll = rootView;
                newsFragment.searchValue = defaultTextSearch;
                fragmentTransaction.replace(R.id.fragment_search_all_news, newsFragment);
                fragmentTransaction.commit();

                Log.d("TOTAL DATA NEWS",newsFragment.total.toString());
                halalItemsFragment.tabLayout = tabLayout;
                halalItemsFragment.tabPageType = 1;
                halalItemsFragment.type = 1;
                halalItemsFragment.isScrollChanged = false;
                halalItemsFragment.isSearchNotFound = false;
                halalItemsFragment.limitItems = 3;
                halalItemsFragment.tabSearchAllFragment = this;
                halalItemsFragment.viewSearchAll = rootView;
                halalItemsFragment.querySearch = defaultTextSearch;

                fragmentTransactionItems.replace(R.id.fragment_search_all_items, halalItemsFragment);
                fragmentTransactionItems.commit();

//                Log.d("TOTAL DATA NEWS",halalVenuesFragment.total.toString());
                halalVenuesFragment.tabLayout = tabLayout;
                halalVenuesFragment.tabPageType = 3;
                halalVenuesFragment.type = 1;
                halalVenuesFragment.isScrollChanged = false;
                halalVenuesFragment.isSearchNotFound = false;
                halalVenuesFragment.limitItems = 3;
                halalVenuesFragment.tabSearchAllFragment = this;
                halalVenuesFragment.viewSearchAll = rootView;
                halalVenuesFragment.querySearch = defaultTextSearch;

                fragmentTransactionVenues.replace(R.id.fragment_search_all_venues, halalVenuesFragment);
                fragmentTransactionVenues.commit();
//                Log.d("TOTAL DATA ITEMS",halalItemsFragment.total.toString());
            }
            RelativeLayout btnShowAllItems = (RelativeLayout) rootView.findViewById(R.id.btnShowAllItems);
            btnShowAllItems.setOnClickListener(btnShowAllItemsListener);

            RelativeLayout btnShowAllNews = (RelativeLayout) rootView.findViewById(R.id.btnShowAllNews);
            btnShowAllNews.setOnClickListener(btnShowAllNewsListener);

            RelativeLayout btnShowAllVenues = (RelativeLayout) rootView.findViewById(R.id.btnShowAllVenues);
            btnShowAllVenues.setOnClickListener(btnShowAllVenuesListener);
        } else if (type == 1) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_items, container, false);
            if (isConnect == 1) {
                halalItemsFragment.type = 1;
                halalItemsFragment.isScrollChanged = true;
                halalItemsFragment.isSearchNotFound = false;
                halalItemsFragment.querySearch = defaultTextSearch;
                fragmentTransactionItems.replace(R.id.fragment_search_items, halalItemsFragment);
                fragmentTransactionItems.commit();


//                newsFragment.tabLayout = tabLayout;
//                newsFragment.fragmentActivity = getActivity();
//                newsFragment.tabPageType = 2;
//                newsFragment.isCheckTab = true;
//                newsFragment.type = 1;
//                newsFragment.searchValue = defaultTextSearch;
//                newsFragment.getListNews(1);
//
//                halalVenuesFragment.tabLayout = tabLayout;
//                halalVenuesFragment.fragmentActivity = getActivity();
//                halalVenuesFragment.tabPageType = 3;
//                halalVenuesFragment.type = 1;
//                halalVenuesFragment.isCheckTab = true;
//                halalVenuesFragment.getListVenues(1,defaultTextSearch);

            }
        } else if (type == 2) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_news, container, false);
            if (isConnect == 1) {
                newsFragment.type = 1;
                newsFragment.isScrollChanged = true;
//            halalItemsFragment.limitItems = 3;
                newsFragment.searchValue = defaultTextSearch;
                newsFragment.isSearchNotFound = false;
                fragmentTransactionItems.replace(R.id.fragment_search_news, newsFragment);
                fragmentTransactionItems.commit();

//                halalItemsFragment.tabLayout = tabLayout;
//                halalItemsFragment.fragmentActivity = getActivity();
//                halalItemsFragment.tabPageType = 1;
//                halalItemsFragment.isCheckTab = true;
//                halalItemsFragment.type = 1;
//                halalItemsFragment.getListFood(1,defaultTextSearch);
//
//                halalVenuesFragment.tabLayout = tabLayout;
//                halalVenuesFragment.fragmentActivity = getActivity();
//                halalVenuesFragment.tabPageType = 3;
//                halalVenuesFragment.type = 1;
//                halalVenuesFragment.isCheckTab = true;
//                halalVenuesFragment.getListVenues(1,defaultTextSearch);
            }
        } else if (type == 3) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_venues, container, false);
            if (isConnect == 1) {
                halalVenuesFragment.type = 1;
                halalVenuesFragment.isScrollChanged = true;
//            halalItemsFragment.limitItems = 3;
                halalVenuesFragment.querySearch = defaultTextSearch;
                halalVenuesFragment.isSearchNotFound = false;
                fragmentTransactionItems.replace(R.id.fragment_search_venues, halalVenuesFragment);
                fragmentTransactionItems.commit();

//                halalItemsFragment.tabLayout = tabLayout;
//                halalItemsFragment.fragmentActivity = getActivity();
//                halalItemsFragment.tabPageType = 1;
//                halalItemsFragment.isCheckTab = true;
//                halalItemsFragment.type = 1;
//                halalItemsFragment.getListFood(1,defaultTextSearch);
//
//                newsFragment.tabLayout = tabLayout;
//                newsFragment.fragmentActivity = getActivity();
//                newsFragment.tabPageType = 2;
//                newsFragment.isCheckTab = true;
//                newsFragment.type = 1;
//                newsFragment.searchValue = defaultTextSearch;
//                newsFragment.getListNews(1);
            }
        }
//        RelativeLayout btnBack = (RelativeLayout) getActivity().findViewById(R.id.back);
//        LinearLayout layoutAll = (LinearLayout) getActivity().findViewById(R.id.layoutAll);
//        SearchView searchView =
//                (SearchView) getActivity().findViewById(R.id.btnViewSearchAllItems);
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                layoutAll.setVisibility(View.GONE);
//                btnBack.setVisibility(View.GONE);
//
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                layoutAll.setVisibility(View.VISIBLE);
//                btnBack.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });

        return rootView;
    }

    public Boolean isRemove(){
        return this.isRemove;
    }

    public void setDefaultTextSearch(String text) {
        defaultTextSearch = text;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener btnShowAllItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
            tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
            tabHomeSearchAllFragment.defaultTabsPage = 1;
            tabHomeSearchAllFragment.defaultTabsPageText = "Items";
            fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnShowAllNewsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
            tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
            tabHomeSearchAllFragment.defaultTabsPage = 2;
            tabHomeSearchAllFragment.defaultTabsPageText = "News";
//            tabLayout.setab();
            fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnShowAllVenuesListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
            tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
            tabHomeSearchAllFragment.defaultTabsPageText = "Venues";
            tabHomeSearchAllFragment.defaultTabsPage = 3;
            fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
            fragmentTransaction.commit();
        }
    };
}
