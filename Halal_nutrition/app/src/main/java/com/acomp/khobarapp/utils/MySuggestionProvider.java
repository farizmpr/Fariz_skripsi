package com.acomp.khobarapp.utils;

import android.content.SearchRecentSuggestionsProvider;

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    //    public final static String AUTHORITY = "com.acomp.khobarapp.utils.MySuggestionProvider";
    public final static String AUTHORITY = MySuggestionProvider.class.getName();
    public final static int MODE = DATABASE_MODE_QUERIES;


    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
