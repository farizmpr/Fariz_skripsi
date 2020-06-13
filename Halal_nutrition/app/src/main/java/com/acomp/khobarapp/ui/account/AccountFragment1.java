package com.acomp.khobarapp.ui.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.LoginActivity;
import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.BearerTokenModel;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountFragment1 extends Fragment {
    ProgressDialog progressDoalog;
    private BottomNavigationView mBtmView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_signin, container, false);
        RelativeLayout btnBack = (RelativeLayout) rootView.findViewById(R.id.back);
        btnBack.setOnClickListener(btnBackListener);

        Button b = (Button) rootView.findViewById(R.id.LoginbtnProses);
        b.setOnClickListener(mButtonClickListener);

        TextView btnSignUp = (TextView) rootView.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(mClickSignUp);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
        bottomNavigationView.setVisibility(View.GONE);
        return rootView;
    }

    private View.OnClickListener mClickSignUp = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AccountFragmentSignUp accountFragment = new AccountFragmentSignUp();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HomeAccountFragment accountFragment = new HomeAccountFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            loginProcess(v);
        }
    };

    public void loginProcess(View view) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        EditText usernameText = (EditText) getActivity().findViewById(R.id.username_login);
        String username = usernameText.getText().toString();
        if (username.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a email", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText passwordText = (EditText) getActivity().findViewById(R.id.password_login);
        String password = passwordText.getText().toString();
        if (password.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a username", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDoalog.show();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, Object> jsonParams = new ArrayMap<>();
        Log.d("DATA LOGIN", username + "|" + password);
        jsonParams.put("email", username);
        jsonParams.put("password", password);
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        Call call = getDataService.getBearerToken(jsonParams);
        call.enqueue(new Callback<BearerTokenModel>() {
            @Override
            public void onResponse(Call<BearerTokenModel> call, Response<BearerTokenModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String token = response.body().getToken();
                        SharedPreferences.Editor prefsEditr = preferences.edit();
                        prefsEditr.putString("token", token);
                        prefsEditr.commit();
//                        preferences.edit().putString("token", token).commit();
//                        SharedPreferences.Editor editor = preferences.edit();
                        prefsEditr.apply();
//                        getActivity().finish();
                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
//                        mBtmView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
//                        mBtmView.setOnNavigationItemSelectedListener(getActivity());
                        AccountFragment accountFragment = new AccountFragment();
                        fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Login Failed, Please Check your email & Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Login Failed, Please Check your email & Password", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<BearerTokenModel> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d("DEBUG ERROR","MESSAGE="+t.getMessage());
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

}
