package com.acomp.khobarapp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;

import com.acomp.khobarapp.R;

public class ArrayAdapterSearchView extends SearchView {
    private String[] arraySearch;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private OnItemClickListener listener;

    public ArrayAdapterSearchView(Context context,SearchView searchView) {
        super(context);
//        arraySearch = data;
        mSearchView = searchView;
        initialize();
    }

    public ArrayAdapterSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @SuppressLint("RestrictedApi")
    public void initialize() {
        mSearchAutoComplete = (SearchAutoComplete) mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        mSearchAutoComplete.setThreshold(0);
        mSearchAutoComplete.setElevation(0);
        mSearchAutoComplete.setDropDownBackgroundDrawable(
                getContext().getResources().getDrawable(R.drawable.remove_shadow));
        mSearchAutoComplete.setShadowLayer(0,0,0, Color.BLACK);
        this.setAdapter(null);
        this.setOnItemClickListener(null);
//        new SuggestionAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
//        this.setSuggestionsAdapter();
    }

//    @Override
//    public void setSuggestionsAdapter(CursorAdapter adapter) {
//        // don't let anyone touch this
//        Log.d("SUGGEST","TEST");
//    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mSearchAutoComplete.setOnItemClickListener(listener);
    }

    public interface OnItemClickListener{
        public void onItemClick(String item);
    }

//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.listener = listener;
//    }
//
//    public interface OnItemClickListener{
//        public void onItemClick(String item);
//    }

    public void setAdapter(ArrayAdapter<?> adapter) {
        mSearchAutoComplete.setAdapter(adapter);
    }

    public void setText(String text) {
        mSearchAutoComplete.setText(text);
    }

}
