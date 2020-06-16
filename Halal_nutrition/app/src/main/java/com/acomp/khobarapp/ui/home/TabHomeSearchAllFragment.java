package com.acomp.khobarapp.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ui.adapter.TabSearchAdapter;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabHomeSearchAllFragment extends Fragment {
    private TabSearchAdapter adapter;
    private TabLayout tabLayout = null;
    private ViewPager viewPager;
    String defaultTextSearch = null;
    public Integer defaultTabsPage = 0;
    public String defaultTabsPageText = null;
    public Boolean isExpandSearch = false;
    private Handler _hRedraw;
    protected static final int REFRESH = 0;
    public List<Integer> listRemoveTab = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_search_all, container, false);
        RelativeLayout btnBack = (RelativeLayout) rootView.findViewById(R.id.back);
        LinearLayout layoutAll = (LinearLayout) rootView.findViewById(R.id.layoutAll);
        SearchView searchView =
                (SearchView) rootView.findViewById(R.id.btnViewSearchAllItems);


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                layoutAll.setVisibility(View.VISIBLE);
//                btnBack.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(), "CLOSE VIEW", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//        searchView.setLeft(0);
        if (defaultTextSearch != null) {
//        if (isExpandSearch) {

//            btnBack.setVisibility(View.GONE);
            String str = getActivity().getIntent().getStringExtra(defaultTextSearch);
//            searchView.onActionViewCollapsed();
//            searchView.setQuery(str, false);
//            searchView.onActionViewExpanded();
            searchView.setIconified(false);
//            searchView.setIconifiedByDefault(true);

            searchView.setQuery(defaultTextSearch, false);
            searchView.clearFocus();
            layoutAll.setVisibility(View.VISIBLE);
//            btnBack.setVisibility(View.VISIBLE);
//            layoutAll.setVisibility(View.GONE);
//

//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​;

//            View view = getActivity().getCurrentFocus();
//            if (view != null) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
//            hideKeyboard(getActivity());
        }

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setActivity(getActivity());
        homeFragment.setNoPaddingAutoComplete(true);
        homeFragment.getSuggetsSearchAutoComplete("suggestSearchAll", searchView);


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutAll.setVisibility(View.GONE);
//                    btnBack.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), "OPEN VIEW", Toast.LENGTH_SHORT).show();
                    // searchView expanded
                } else {
                    // searchView not expanded
                }
            }
        });
//searchView.setDrop
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                layoutAll.setVisibility(View.VISIBLE);
//                btnBack.setVisibility(View.VISIBLE);
//                if(isScrollChanged == true) {
//                    querySearch = s;
//                    page = 1;
//                    listFoodModel.clear();
//                    getListFood(page, querySearch);
//                    Log.d("QUERY Submit", "QueryTextSubmit: " + s);
//                }
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setActivity(getActivity());
                homeFragment.saveSuggest("suggestSearchAll", s);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
                tabHomeSearchAllFragment.setDefaultTextSearch(s);
                if (defaultTabsPage != 0) {
                    tabHomeSearchAllFragment.tabLayout = tabLayout;
                }
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

        btnBack.setOnClickListener(goBackListener);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        FragmentManager fragmentTransaction = getActivity().getSupportFragmentManager();
        adapter = new TabSearchAdapter(fragmentTransaction);
//        tabLayout.not
        TabSearchAllFragment tabSearchAllFragment = new TabSearchAllFragment();
        tabSearchAllFragment.tabLayout = tabLayout;
        tabSearchAllFragment.type = 0;
        if (defaultTabsPage == 0) {
            tabSearchAllFragment.isConnect = 1;
        } else {
            tabSearchAllFragment.isConnect = 0;
        }
        tabSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
        adapter.addFragment(tabSearchAllFragment, "All");

        TabSearchAllFragment tabSearchAllFragment2 = new TabSearchAllFragment();
        tabSearchAllFragment2.setDefaultTextSearch(defaultTextSearch);
        tabSearchAllFragment2.type = 1;
        tabSearchAllFragment2.tabLayout = tabLayout;
        if (defaultTabsPage == 1) {
            tabSearchAllFragment2.isConnect = 1;
        } else {
            tabSearchAllFragment2.isConnect = 0;
        }
        Boolean isRemove2 = tabSearchAllFragment2.isRemove();
        Log.d("REMOVE 2", "IS=" + isRemove2);
        adapter.addFragment(tabSearchAllFragment2, "Items");

        TabSearchAllFragment tabSearchAllFragment3 = new TabSearchAllFragment();
        tabSearchAllFragment3.setDefaultTextSearch(defaultTextSearch);
        tabSearchAllFragment3.tabLayout = tabLayout;
        tabSearchAllFragment3.type = 2;
        if (defaultTabsPage == 2) {
            tabSearchAllFragment3.isConnect = 1;
        } else {
            tabSearchAllFragment3.isConnect = 0;
        }
        Boolean isRemove3 = tabSearchAllFragment3.isRemove();
        Log.d("REMOVE 3", "IS=" + isRemove3);
        adapter.addFragment(tabSearchAllFragment3, "News");

        TabSearchAllFragment tabSearchAllFragment4 = new TabSearchAllFragment();
        tabSearchAllFragment4.setDefaultTextSearch(defaultTextSearch);
        tabSearchAllFragment4.tabLayout = tabLayout;
        tabSearchAllFragment4.type = 3;
        if (defaultTabsPage == 3) {
            tabSearchAllFragment4.isConnect = 1;
        } else {
            tabSearchAllFragment4.isConnect = 0;
        }
        Boolean isRemove4 = tabSearchAllFragment4.isRemove();
        Log.d("REMOVE 4", "IS=" + isRemove4);
        adapter.addFragment(tabSearchAllFragment4, "Venues");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setS
//        getTag
        Integer tabCount = tabLayout.getTabCount();
        if (defaultTabsPageText == null) {
            viewPager.setCurrentItem(defaultTabsPage);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Integer selectedTabPosition = 0;
            for (Integer no = 0; no < tabCount; no++) {
                String txt = tabLayout.getTabAt(no).getText().toString();
                if (txt == defaultTabsPageText) {
                    selectedTabPosition = no;

                }
            }
            tabLayout.getTabAt(selectedTabPosition).select();
            viewPager.setCurrentItem(selectedTabPosition);
            tabLayout.setupWithViewPager(viewPager);
        }

        Log.d("TAB COUNT", "C=" + tabCount);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                TabHomeSearchAllFragment tabHomeSearchAllFragment = new TabHomeSearchAllFragment();
                String text = tab.getText().toString();
//                    tabHomeSearchAllFragment.adapter = adapter;
//                    tabHomeSearchAllFragment.viewPager = viewPager;
                tabHomeSearchAllFragment.listRemoveTab = listRemoveTab;
                if (text == "All") {
                    tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);

                    tabHomeSearchAllFragment.defaultTabsPage = 0;
                    tabHomeSearchAllFragment.defaultTabsPageText = "All";
                    tabHomeSearchAllFragment.tabLayout = tabLayout;
                    fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                    fragmentTransaction.commit();
                } else if (text == "Items") {
                    tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                    tabHomeSearchAllFragment.defaultTabsPage = 1;
                    tabHomeSearchAllFragment.defaultTabsPageText = "Items";
                    tabHomeSearchAllFragment.tabLayout = tabLayout;
                    fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                    fragmentTransaction.commit();
                } else if (text == "News") {
                    tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                    tabHomeSearchAllFragment.defaultTabsPage = 2;
                    tabHomeSearchAllFragment.defaultTabsPageText = "News";
                    tabHomeSearchAllFragment.tabLayout = tabLayout;
                    fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                    fragmentTransaction.commit();
                } else if (text == "Venues") {
                    tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                    tabHomeSearchAllFragment.defaultTabsPage = 3;
                    tabHomeSearchAllFragment.defaultTabsPageText = "Venues";
                    tabHomeSearchAllFragment.tabLayout = tabLayout;
                    fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                    fragmentTransaction.commit();
                }
                /*
                switch (tab.getPosition()) {
                    case 0:

                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 0;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                    case 1:
                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 1;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                    case 2:
                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 2;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                    case 3:
                        tabHomeSearchAllFragment.setDefaultTextSearch(defaultTextSearch);
                        tabHomeSearchAllFragment.defaultTabsPage = 3;
                        fragmentTransaction.replace(R.id.fragment_content, tabHomeSearchAllFragment);
                        fragmentTransaction.commit();
                        // ...
                }

                 */
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
        MenuItem mSearchMenuItem;
        mSearchMenuItem = menu.findItem(R.id.action_search);
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
