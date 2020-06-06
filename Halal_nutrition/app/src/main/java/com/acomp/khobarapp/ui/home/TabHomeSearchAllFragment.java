package com.acomp.khobarapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ui.adapter.TabSearchAdapter;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.google.android.material.tabs.TabLayout;

public class TabHomeSearchAllFragment extends Fragment {
    private TabSearchAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String defaultTextSearch = null;
    public Integer defaultTabsPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_search_all, container, false);

        SearchView searchView =
                (SearchView) rootView.findViewById(R.id.btnViewSearchAllItems);
        if (defaultTextSearch != null) {
            String str = getActivity().getIntent().getStringExtra(defaultTextSearch);
//            searchView.setQuery(str, false);
            searchView.onActionViewExpanded();
            searchView.setQuery(defaultTextSearch, false);
            searchView.clearFocus();

//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​;

//            View view = getActivity().getCurrentFocus();
//            if (view != null) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
//            hideKeyboard(getActivity());
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
//                if(isScrollChanged == true) {
//                    querySearch = s;
//                    page = 1;
//                    listFoodModel.clear();
//                    getListFood(page, querySearch);
//                    Log.d("QUERY Submit", "QueryTextSubmit: " + s);
//                }
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
                tabHomeSearchAllFragment.setDefaultTextSearch(s);
                tabHomeSearchAllFragment.defaultTabsPage = defaultTabsPage;
                fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                fragmentTransaction.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d("QUERY CHANGE", "QueryTextChange: " + newText);
                return false;
            }
        });

        RelativeLayout btnBack = (RelativeLayout) rootView.findViewById(R.id.back);
        rootView.setOnClickListener(goBackListener);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        FragmentManager fragmentTransaction = getActivity().getSupportFragmentManager();
        adapter = new TabSearchAdapter(fragmentTransaction);
        TabSearchAllFragment tabSearchAllFragment = new TabSearchAllFragment();
        tabSearchAllFragment.type = 0;
        if(defaultTabsPage == 0){
            tabSearchAllFragment.isConnect = 1;
        } else {
            tabSearchAllFragment.isConnect = 0;
        }
        tabSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
        adapter.addFragment(tabSearchAllFragment, "Search All");

//        TabSearchItemsFragment tabSearchItemsFragment = new TabSearchItemsFragment();
//        tabSearchItemsFragment.setDefaultTextSearch(defaultTextSearch);
        TabSearchAllFragment tabSearchAllFragment2 = new TabSearchAllFragment();
        tabSearchAllFragment2.setDefaultTextSearch(defaultTextSearch);
        tabSearchAllFragment2.type = 1;
//        tabSearchAllFragment2.isConnect = 0;
        if(defaultTabsPage == 1){
            tabSearchAllFragment2.isConnect = 1;
        } else {
            tabSearchAllFragment2.isConnect = 0;
        }
        adapter.addFragment(tabSearchAllFragment2, "Items");

//        TabSearchNewsFragment tabSearchNewsFragment = new TabSearchNewsFragment();
//        tabSearchNewsFragment.setDefaultTextSearch(defaultTextSearch);
//        tabSearchItemsFragment
        TabSearchAllFragment tabSearchAllFragment3 = new TabSearchAllFragment();
        tabSearchAllFragment3.setDefaultTextSearch(defaultTextSearch);
        tabSearchAllFragment3.type = 2;
//        tabSearchAllFragment3.isConnect = 0;
        if(defaultTabsPage == 2){
            tabSearchAllFragment3.isConnect = 1;
        } else {
            tabSearchAllFragment3.isConnect = 0;
        }
        adapter.addFragment(tabSearchAllFragment3, "News");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(defaultTabsPage);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addOnTabSelectedListener();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
                switch(tab.getPosition()) {
                    case 0:
                        Log.d("TAB","TAB1");
                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 0;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                        break;
                    case 1:
//                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 1;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                        Log.d("TAB","TAB2");
                        break;
                    case 2:
                        Log.d("TAB","TAB3");
                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 2;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                        break;
                        // ...
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }

    public void setDefaultTextSearch(String text) {
        defaultTextSearch = text;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_fetch);
        item.setVisible(false);
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HomeFragment accountFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
