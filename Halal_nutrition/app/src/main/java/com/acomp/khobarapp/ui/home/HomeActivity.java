package com.acomp.khobarapp.ui.home;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acomp.khobarapp.LoginActivity;
import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.BearerTokenModel;
import com.acomp.khobarapp.model.UserModel;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.account.AccountFragment1;
import com.acomp.khobarapp.ui.adapter.HeadlineNewsAdapter;
import com.acomp.khobarapp.ui.adapter.RegularNewsAdapter;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.acomp.khobarapp.utils.SpacesItemDecoration;
import com.acomp.khobarapp.viewmodel.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private HeadlineNewsAdapter headlineNewsAdapter;
    private RegularNewsAdapter regularNewsAdapter;
    private HomeViewModel homeViewModel;
    ProgressDialog progressDoalog;
    private ProgressBar progressBar;
    private int recyclerViewsLoadedCount = 0;
    private BottomNavigationView mBtmView;
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().hide();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");
//        progressBar = findViewById(R.id.toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Context context = getApplicationContext();
        FrameLayout fragmentContent = (FrameLayout) findViewById(R.id.fragment_content);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("ITEM", "TEST=" + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.nav_account:
                        progressDoalog = new ProgressDialog(HomeActivity.this);
                        progressDoalog.setMessage("Loading....");
                        progressDoalog.show();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                        String token = preferences.getString("token", "");
//                        Log.d("TOKEN", token);
                        if (token == null) {
//                            Log.d("TOKEN LOGIN GAGAL", token);
                            AccountFragment1 accountFragment1 = new AccountFragment1();
                            fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                            fragmentTransaction.commit();
                        } else {
//                            Log.d("TOKEN LOGIN DULU", token);
                            GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
                            Call call = getDataService.getUser();
                            call.enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            String name = response.body().getName();
                                            AccountFragment accountFragment = new AccountFragment();
                                            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                                            fragmentTransaction.commit();
                                        } else {
//                                            Log.d("TOKEN NULL", token);
                                            AccountFragment1 accountFragment1 = new AccountFragment1();
                                            fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                                            fragmentTransaction.commit();
                                        }
                                    } else {
//                                        Log.d("TOKEN LOGIN NOT SUCCESS", token);
                                        AccountFragment1 accountFragment1 = new AccountFragment1();
                                        fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                                        fragmentTransaction.commit();
                                    }
                                    progressDoalog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<UserModel> call, Throwable t) {
                                    Log.d("TOKEN LOGIN FAILURE", token);
                                    AccountFragment1 accountFragment1 = new AccountFragment1();
                                    fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                                    fragmentTransaction.commit();
                                    progressDoalog.dismiss();
                                }
                            });

                        }

                        break;
                    case R.id.nav_news:
//                        String token2 = preferences.getString("token",null);
//                        Log.d("TOKEN NEWS",token2);
                        RecyclerView rvHeadlineNews = findViewById(R.id.rv_headline);
                        RecyclerView rvRegularNews = findViewById(R.id.rv_regular);

                        progressBar = findViewById(R.id.progress_bar);
                        progressBar.setVisibility(View.VISIBLE);

                        headlineNewsAdapter = new HeadlineNewsAdapter(HomeActivity.this);
                        regularNewsAdapter = new RegularNewsAdapter(HomeActivity.this);
                        homeViewModel = obtainViewModel(HomeActivity.this);

                        homeViewModel.fetch(false);

                        homeViewModel.headlineNewsList.observe(HomeActivity.this, listResource -> {
                            if (listResource != null) {
                                switch (listResource.status) {
                                    case LOADING:
                                        break;
                                    case SUCCESS:
                                        countLoadedOrFailNews();
                                        headlineNewsAdapter.setHeadlineNewsList(listResource.data);
                                        break;
                                    case ERROR:
                                        countLoadedOrFailNews();
                                        Toast.makeText(HomeActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });

                        homeViewModel.regularNewsList.observe(HomeActivity.this, listResource -> {
                            if (listResource != null) {
                                switch (listResource.status) {
                                    case LOADING:
                                        break;
                                    case SUCCESS:
                                        countLoadedOrFailNews();
                                        regularNewsAdapter.setRegularNewsList(listResource.data);
                                        break;
                                    case ERROR:
                                        countLoadedOrFailNews();
                                        Toast.makeText(HomeActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });

                        rvHeadlineNews.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
                        rvHeadlineNews.setHasFixedSize(true);

//                        rvHeadlineNews.setAdapter(headlineNewsAdapter);

                        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.rv_item_margin);
                        rvRegularNews.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
                        rvRegularNews.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        rvRegularNews.setHasFixedSize(true);
//                        rvRegularNews.setAdapter(regularNewsAdapter);
                        break;
                }
                return false;
            }
        });
    }

    private HomeViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(activity, factory).get(HomeViewModel.class);
    }

    private void countLoadedOrFailNews() {
        // Progress bar akan dihilangkan dari layar apabila kedua recyclerview telah diload
        if (++recyclerViewsLoadedCount == 2)
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackPressed) {
            super.onBackPressed();
        } else {
            doubleBackPressed = true;
            final ConstraintLayout constraintLayout = findViewById(R.id.acivity_home);
            Snackbar.make(constraintLayout, getString(R.string.tekan_lagi), Snackbar.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_fetch) {
            recyclerViewsLoadedCount -= 2;
            progressBar.setVisibility(View.VISIBLE);
            headlineNewsAdapter.clear();
            regularNewsAdapter.clear();
            homeViewModel.fetch(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
