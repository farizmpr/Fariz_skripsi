package com.acomp.khobarapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;

public class TabSearchAllFragment extends Fragment {

    String defaultTextSearch = "";
    public Integer type = 0;
    public Integer isConnect = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        NewsFragment newsFragment = new NewsFragment();
        HalalItemsFragment halalItemsFragment = new HalalItemsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransactionItems = getActivity().getSupportFragmentManager().beginTransaction();
        if (type == 0) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_all, container, false);
            if (isConnect == 1) {
                newsFragment.type = 1;
                newsFragment.isScrollChanged = false;
                newsFragment.limitNews = 3;
                fragmentTransaction.replace(R.id.fragment_search_all_news, newsFragment);
                fragmentTransaction.commit();

                halalItemsFragment.type = 1;
                halalItemsFragment.isScrollChanged = false;
                halalItemsFragment.limitItems = 3;
                halalItemsFragment.querySearch = defaultTextSearch;
                fragmentTransactionItems.replace(R.id.fragment_search_all_items, halalItemsFragment);
                fragmentTransactionItems.commit();
            }
            RelativeLayout btnShowAllItems = (RelativeLayout) rootView.findViewById(R.id.btnShowAllItems);
            btnShowAllItems.setOnClickListener(btnShowAllItemsListener);

            RelativeLayout btnShowAllNews = (RelativeLayout) rootView.findViewById(R.id.btnShowAllNews);
            btnShowAllNews.setOnClickListener(btnShowAllNewsListener);
        } else if (type == 1) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_items, container, false);
            if (isConnect == 1) {
                halalItemsFragment.type = 1;
                halalItemsFragment.isScrollChanged = true;
//            halalItemsFragment.limitItems = 3;
                halalItemsFragment.querySearch = defaultTextSearch;
                fragmentTransactionItems.replace(R.id.fragment_search_items, halalItemsFragment);
                fragmentTransactionItems.commit();
            }
        } else if (type == 2) {
            rootView = inflater.inflate(R.layout.tab_fragment_search_news, container, false);
            if (isConnect == 1) {
                newsFragment.type = 1;
                newsFragment.isScrollChanged = true;
//            halalItemsFragment.limitItems = 3;
                newsFragment.searchValue = defaultTextSearch;
                fragmentTransactionItems.replace(R.id.fragment_search_news, newsFragment);
                fragmentTransactionItems.commit();
            }
        }


        return rootView;
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
            fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
            fragmentTransaction.commit();
        }
    };
}
