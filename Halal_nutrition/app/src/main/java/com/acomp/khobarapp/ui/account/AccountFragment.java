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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.BearerTokenModel;
import com.acomp.khobarapp.model.UserModel;
import com.acomp.khobarapp.ui.home.HomeActivity;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    ProgressDialog progressDoalog;
    Integer userId = null;
    String fullname = null;
    String email = null;
    String address = null;
    Integer attachmentId = 0;
    String attachmentUrl = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        View rootView = inflater.inflate(R.layout.activity_dashboard, container, false);
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Call call = getDataService.getUser();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        CircularImageView circularImageView = (CircularImageView) rootView.findViewById(R.id.profile_image);
                        circularImageView.setImageDrawable(getResources().getDrawable(R.drawable.sample_avatar));
                        circularImageView.setImageResource(R.drawable.sample_avatar);
                        Integer id = response.body().getId();
                        String name = response.body().getName();
                        String resEmail = response.body().getEmail();
                        String resAddress = response.body().getAddress();
                        Integer resAttachmentId = response.body().getAttachmentId();
                        String resAttachmentUrl = response.body().getAttachmentUrl();
                        TextView fieldNameText = (TextView) rootView.findViewById(R.id.account_text);
                        fieldNameText.setText(name);
                        userId = id;
                        fullname = name;
                        email = resEmail;
                        address = resAddress;
                        attachmentId = resAttachmentId;
                        attachmentUrl = resAttachmentUrl;
                        if(attachmentId != 0){
//                            Picasso.get().load(mImages.get(position)).into(circularImageView);
                            Log.d("Image Profile",resAttachmentUrl);
                            Picasso.get().load(resAttachmentUrl).fit().centerCrop().into(circularImageView);
                        }

                        TextView fieldEmailText = (TextView) rootView.findViewById(R.id.account_email_text);
                        fieldEmailText.setText(email);
//                        AccountFragment accountFragment = new AccountFragment();
//                        accountFragment.name = name;
//                        fragmentTransaction.replace(R.id.fragment_content, accountFragment);
//                        fragmentTransaction.commit();
                    } else {
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

        Button b = (Button) rootView.findViewById(R.id.logoutBtnLinear);
        b.setOnClickListener(mButtonClickListener);
        LinearLayout btnEditProfile = (LinearLayout) rootView.findViewById(R.id.linear2);
        btnEditProfile.setOnClickListener(btnEditProfileListener);

        LinearLayout btnChangePassMenu = (LinearLayout) rootView.findViewById(R.id.change_password_menu);
        btnChangePassMenu.setOnClickListener(btnChangePassMenuListener);

        LinearLayout btnAddItems = (LinearLayout) rootView.findViewById(R.id.linear1);
        btnAddItems.setOnClickListener(btnAddItemsListener);

        LinearLayout btnHistoryItems = (LinearLayout) rootView.findViewById(R.id.historyItems);
        btnHistoryItems.setOnClickListener(btnHistoryItemsListener);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
//        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private View.OnClickListener btnAddItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AddItemsFragment accountFragment1 = new AddItemsFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnHistoryItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HistoryItemsFragment accountFragment1 = new HistoryItemsFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnEditProfileListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            EditProfileFragment accountFragment1 = new EditProfileFragment();
            accountFragment1.userId = userId;
            accountFragment1.fullname = fullname;
            accountFragment1.email = email;
            accountFragment1.address = address;
            accountFragment1.attachmentId = attachmentId;
            accountFragment1.attachmentUrl = attachmentUrl;
            fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnChangePassMenuListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            ChangePasswordFragment accountFragment1 = new ChangePasswordFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            logoutProcess(v);
        }
    };

    public void logoutProcess(View view) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        SharedPreferences.Editor prefsEditr = preferences.edit();
        prefsEditr.putString("token", null);
        prefsEditr.commit();
        prefsEditr.clear();
        prefsEditr.apply();
        AccountFragment1 accountFragment1 = new AccountFragment1();
        fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
        fragmentTransaction.commit();
        progressDoalog.dismiss();
    }

}
