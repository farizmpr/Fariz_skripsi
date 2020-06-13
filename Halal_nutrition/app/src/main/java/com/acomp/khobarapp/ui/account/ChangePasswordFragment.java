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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_change_password, container, false);

        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener );

        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
        btnUpdatePassword.setOnClickListener(updatePasswordListener);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AccountFragment accountFragment = new AccountFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener updatePasswordListener = new View.OnClickListener() {
        public void onClick(View v) {
            updatePassword(v);
        }
    };

    public void updatePassword(View view) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        EditText oldpasswordText = (EditText) getActivity().findViewById(R.id.fieldOldPassword);
        TextView msgCurrentPassword = (TextView) getActivity().findViewById(R.id.msgCurrentPassword);
        String oldpassword = oldpasswordText.getText().toString();
        if (oldpassword.length() >= 8) {

        } else {
            oldpasswordText.requestFocus();
            msgCurrentPassword.setVisibility(View.VISIBLE);
            msgCurrentPassword.setText("Current Password must be more than 8 characters");
            return;
        }
        if (oldpassword.matches("")) {
            oldpasswordText.requestFocus();
            msgCurrentPassword.setVisibility(View.VISIBLE);
            msgCurrentPassword.setText("Current Password is Required");
            return;
        } else {
            msgCurrentPassword.setVisibility(View.GONE);
            msgCurrentPassword.setText("");
        }
        EditText newpasswordText = (EditText) getActivity().findViewById(R.id.fieldNewPassword);
        TextView msgNewPassword = (TextView) getActivity().findViewById(R.id.msgNewPassword);
        String newpassword = newpasswordText.getText().toString();
        if (newpassword.length() >= 8) {

        } else {
            newpasswordText.requestFocus();
            msgNewPassword.setVisibility(View.VISIBLE);
            msgNewPassword.setText("New Password must be more than 8 characters");
            return;
        }
        if (newpassword.matches("")) {
            newpasswordText.requestFocus();
            msgNewPassword.setVisibility(View.VISIBLE);
            msgNewPassword.setText("New Password is Required");
            return;
        } else {
            msgNewPassword.setVisibility(View.GONE);
            msgNewPassword.setText("");
        }
        progressDoalog.show();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("oldpassword", oldpassword);
        jsonParams.put("newpassword", newpassword);
//        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        Call call = getDataService.updatePassword(jsonParams);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (!jsonObject.has("error")) {
                            Boolean result = jsonObject.getBoolean("result");
                            if(result == true){
                                Toast.makeText(getActivity(), "Change Password Success", Toast.LENGTH_SHORT).show();
                            } else {
                                String message =  jsonObject.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Change Password Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Change Password Failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Change Password Failed", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
