package com.acomp.khobarapp.ui.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.BearerTokenModel;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAccountFragment extends Fragment {
    ProgressDialog progressDoalog;
    private BottomNavigationView mBtmView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_account, container, false);
        Button b = (Button) rootView.findViewById(R.id.btnSignIn);
        b.setOnClickListener(btnLoginListener);

        Button btnSignUp = (Button) rootView.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(mClickSignUp);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        return rootView;
    }

    private View.OnClickListener mClickSignUp = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AccountFragmentSignUp accountFragment = new AccountFragmentSignUp();
            accountFragment.strFromFragmentCode = "homeLogin";
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnLoginListener = new View.OnClickListener() {
        public void onClick(View v) {
            loginPage(v);
        }
    };

    public void loginPage(View v){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        AccountFragment1 accountFragment = new AccountFragment1();
        fragmentTransaction.replace(R.id.fragment_content, accountFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

}
