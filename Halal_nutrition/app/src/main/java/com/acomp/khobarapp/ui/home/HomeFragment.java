package com.acomp.khobarapp.ui.home;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.model.SliderItem;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.adapter.ArrayAdapterSearchView;
import com.acomp.khobarapp.ui.adapter.SliderHomeAdapter;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.items.HalalVenuesFragment;
import com.acomp.khobarapp.ui.items.ScanItemsDetailFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
//    CarouselView carouselView;

    FragmentActivity fragmentActivity = null;
    ViewPager2 myViewPager2;
    SliderHomeAdapter sliderHomeAdapter;
    Handler sliderHandler = new Handler();

    public void setActivity(FragmentActivity fragmentActivitys) {
        fragmentActivity = fragmentActivitys;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        CardView btnSearchItems = (CardView) rootView.findViewById(R.id.btnSearchItems);
        btnSearchItems.setOnClickListener(btnSearchItemsListener);

        CardView btnScanItems = (CardView) rootView.findViewById(R.id.btnScanItems);
        btnScanItems.setOnClickListener(btnScanItemsListener);

        CardView btnHalalVenues = (CardView) rootView.findViewById(R.id.btnHalalVenues);
        btnHalalVenues.setOnClickListener(btnHalalVenuesListener);
//        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
//        btnUpdatePassword.setOnClickListener(updatePasswordListener);

//        carouselView = rootView.findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
//        carouselView.setImageListener(imageListener);
        myViewPager2 = rootView.findViewById(R.id.viewpager);
//        Integer[] sampleImages = {R.drawable.halal_info_news,R.drawable.nambah_produk_home_ps,R.drawable.slide_berita_ps};
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.halal_info_news));
        sliderItems.add(new SliderItem(R.drawable.nambah_produk_home_ps));
        sliderItems.add(new SliderItem(R.drawable.slide_berita_ps));
        sliderHomeAdapter = new SliderHomeAdapter(getActivity(),myViewPager2,sliderItems);
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(sliderHomeAdapter);
//        myViewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        myViewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        myViewPager2.setOffscreenPageLimit(3);
//        myViewPager2.setCurrentItem(myViewPager2.getChildCount() * SliderHomeAdapter.LOOPS_COUNT / 2, false); // set current item in the adapter to middle

        float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMarginSlider);
        float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offsetSlider);

        myViewPager2.setPageTransformer((page, position) -> {
            float myOffset = position * -(2 * pageOffset + pageMargin);
            if (position < -1) {
                page.setTranslationX(-myOffset);
            } else if (position <= 1) {
                float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                page.setTranslationX(myOffset);
                page.setScaleY(scaleFactor);
                page.setAlpha(scaleFactor);
            } else {
                page.setAlpha(0);
                page.setTranslationX(myOffset);
            }
        });
        myViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,6000);
            }
        });


        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.type = 1;
        fragmentTransaction.replace(R.id.fragment_content_news_home, newsFragment);
        fragmentTransaction.commit();

//        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SearchView searchView =
                (SearchView) rootView.findViewById(R.id.btnSearchAllHome);
        getSuggetsSearchAutoComplete("suggestSearchAll", searchView);


        ImageView logoImgHN = (ImageView) rootView.findViewById(R.id.logoImgHN);
        LinearLayout layoutAll = (LinearLayout) rootView.findViewById(R.id.layoutAll);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoImgHN.setVisibility(View.GONE);
                layoutAll.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "OPEN VIEW", Toast.LENGTH_SHORT).show();
                //do what you want when search view expended
//                paramsBack.weight = 1.0f;
//                paramsLnlFav.weight = 8.0f;
//                closeBtn.setLayoutParams(paramsBack);
//                lnlFav.setLayoutParams(paramsLnlFav);
//                titleMenuBar.setVisibility(View.GONE);
//                closeBtn.setGravity();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                logoImgHN.setVisibility(View.VISIBLE);
                layoutAll.setVisibility(View.VISIBLE);
//                paramsBack.weight = 2.0f;
//                paramsLnlFav.weight = 4.0f;
//                closeBtn.setLayoutParams(paramsBack);
//                lnlFav.setLayoutParams(paramsLnlFav);
//                titleMenuBar.setVisibility(View.VISIBLE);
                //do what you want  searchview is not expanded
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                layoutAll.setVisibility(View.VISIBLE);
                saveSuggest("suggestSearchAll", s);
                searchHomeAll(s);
                Log.d("QUERY Submit", "QueryTextSubmit: " + s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("QUERY CHANGE", "QueryTextChange: " + newText);
                return false;
            }
        });

//        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
//        navBar.setVisibility(View.VISIBLE);
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        return rootView;
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            myViewPager2.setCurrentItem(myViewPager2.getCurrentItem()+1);
        }
    };

    public static String[] convertSetToArrayString(Set<String> setOfString) {

        // Create String[] of size of setOfString
        String[] arrayOfString = new String[setOfString.size()];

        // Copy elements from set to string array
        // using advanced for loop
        int index = 0;
        for (String str : setOfString)
            arrayOfString[index++] = str;

        // return the formed String[]
        return arrayOfString;
    }

    public void getSuggetsSearchAutoComplete(String key, SearchView searchView) {
        if (fragmentActivity == null) {
            fragmentActivity = getActivity();
        }

        SharedPreferences preferences = fragmentActivity.getPreferences(Context.MODE_PRIVATE);
        Set<String> listSuggestSearchAll = preferences.getStringSet(key, null);
        String[] dataListSuggestSearchAll = {};
        if (listSuggestSearchAll != null) {
            if (listSuggestSearchAll.size() > 0) {
                dataListSuggestSearchAll = convertSetToArrayString(listSuggestSearchAll);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragmentActivity, R.layout.autocomplete_search, R.id.textAutoComplete, dataListSuggestSearchAll);
        ArrayAdapterSearchView dataAdapter = new ArrayAdapterSearchView(fragmentActivity, searchView);
        dataAdapter.setAdapter(adapter);
        List<String> stringList = new ArrayList<String>(Arrays.asList(dataListSuggestSearchAll)); //new ArrayList is only needed if you absolutely need an ArrayList
        dataAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter selectedView = (ArrayAdapter) parent.getAdapter();
                String txtVal = (String) selectedView.getItem(position);
                searchView.setQuery(txtVal, false);
            }
        });
    }

    public void saveSuggest(String key, String text) {
        if (fragmentActivity == null) {
            fragmentActivity = getActivity();
        }
        SharedPreferences preferences = fragmentActivity.getPreferences(Context.MODE_PRIVATE);
        Set<String> set = null;
        Set<String> listSuggestSearchAll = preferences.getStringSet(key, null);
        if (listSuggestSearchAll != null) {
            if (listSuggestSearchAll.size() >= 11) {
//            listSuggestSearchAll.remove(1);
                Iterator<String> iterator = listSuggestSearchAll.iterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    if (s.length() >= 11) {
                        iterator.remove();
                    }

                }
                set = new HashSet<String>((Collection<? extends String>) iterator);
            } else {
                set = new HashSet<String>(listSuggestSearchAll);
                if (!set.contains(text)) {
                    set.add(text);
                }
            }
        } else {
            if (listSuggestSearchAll != null) {
                set = new HashSet<String>(listSuggestSearchAll);
            } else {
                set = new HashSet<String>();
            }


        }


        SharedPreferences.Editor prefsEditr = preferences.edit();
        prefsEditr.putStringSet(key, set);
        prefsEditr.commit();
//                        preferences.edit().putString("token", token).commit();
//                        SharedPreferences.Editor editor = preferences.edit();
        prefsEditr.apply();
    }

    public void searchHomeAll(String text) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        TabHomeSearchAllFragment halalItemsFragment = new TabHomeSearchAllFragment();
        halalItemsFragment.setDefaultTextSearch(text);
        halalItemsFragment.isExpandSearch = true;
        fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
        fragmentTransaction.commit();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
        }
    };

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

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    private View.OnClickListener btnSearchItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HalalItemsFragment halalItemsFragment = new HalalItemsFragment();
            fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnScanItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            ScanItemsDetailFragment halalItemsFragment = new ScanItemsDetailFragment();
            fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnHalalVenuesListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HalalVenuesFragment halalVenuesFragment = new HalalVenuesFragment();
            fragmentTransaction.replace(R.id.fragment_content, halalVenuesFragment);
            fragmentTransaction.commit();
        }
    };

}
