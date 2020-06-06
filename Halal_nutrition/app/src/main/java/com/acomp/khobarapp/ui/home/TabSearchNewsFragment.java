package com.acomp.khobarapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;

public class TabSearchNewsFragment extends Fragment {

    String defaultTextSearch = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_search_news, container, false);
//        SearchView searchView =
//                (SearchView) getActivity().findViewById(R.id.btnViewSearchAllItems);
//        if (defaultTextSearch != null) {
//            String str = getActivity().getIntent().getStringExtra(defaultTextSearch);
//            searchView.setQuery(str, false);
//        }
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.type = 1;
        newsFragment.isScrollChanged = false;
        newsFragment.limitNews = 3;
        fragmentTransaction.replace(R.id.fragment_search_news, newsFragment);
        fragmentTransaction.commit();

//        FragmentTransaction fragmentTransactionItems = getActivity().getSupportFragmentManager().beginTransaction();
//        HalalItemsFragment halalItemsFragment = new HalalItemsFragment();
//        halalItemsFragment.type = 1;
//        halalItemsFragment.isScrollChanged = false;
//        halalItemsFragment.limitItems = 3;
//        halalItemsFragment.querySearch = defaultTextSearch;
//        fragmentTransactionItems.replace(R.id.fragment_search_all_items, halalItemsFragment);
//        fragmentTransactionItems.commit();

        return rootView;
    }

    public void setDefaultTextSearch(String text){
        defaultTextSearch = text;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
