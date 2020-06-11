package com.acomp.khobarapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ui.items.HalalItemsFragment;
import com.acomp.khobarapp.ui.items.HalalVenuesFragment;

public class TabSearchVenuesFragment extends Fragment {

    String defaultTextSearch = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_search_items, container, false);

        FragmentTransaction fragmentTransactionItems = getActivity().getSupportFragmentManager().beginTransaction();
        HalalVenuesFragment halalVenuesFragment = new HalalVenuesFragment();
        halalVenuesFragment.type = 1;
        halalVenuesFragment.isScrollChanged = false;
        halalVenuesFragment.limitItems = 3;
        halalVenuesFragment.querySearch = defaultTextSearch;
        fragmentTransactionItems.replace(R.id.fragment_search_items, halalVenuesFragment);
        fragmentTransactionItems.commit();

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
