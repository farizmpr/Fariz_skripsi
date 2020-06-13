package com.acomp.khobarapp.ui.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
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
import com.acomp.khobarapp.model.BearerTokenModel;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragmentSignUp extends Fragment {

    ProgressDialog progressDoalog;
    private BottomNavigationView mBtmView;
    public String strFromFragmentCode = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_reg, container, false);
        RelativeLayout b = (RelativeLayout) rootView.findViewById(R.id.back);
        b.setOnClickListener(mButtonClickListener);

        Button btnSaveSignUp = (Button) rootView.findViewById(R.id.btnSaveSignUp);
        btnSaveSignUp.setOnClickListener(btnSaveSignUpListener);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;


    }

    private View.OnClickListener btnSaveSignUpListener = new View.OnClickListener() {
        public void onClick(View v) {
            signUpProcess(v);
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if(strFromFragmentCode == null){
                AccountFragment1 accountFragment = new AccountFragment1();
                fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                fragmentTransaction.commit();
            } else if(strFromFragmentCode == "homeLogin"){
                HomeAccountFragment accountFragment = new HomeAccountFragment();
                fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                fragmentTransaction.commit();
            } else {
                AccountFragment1 accountFragment = new AccountFragment1();
                fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                fragmentTransaction.commit();
            }

        }
    };

    public void signUpProcess(View view) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        EditText fullnameText = (EditText) getActivity().findViewById(R.id.registerFieldFullname);
        TextView msgFullname = (TextView) getActivity().findViewById(R.id.msgFullname);
        String fullname = fullnameText.getText().toString();
        if (fullname.matches("")) {
            fullnameText.requestFocus();
            msgFullname.setVisibility(View.VISIBLE);
            msgFullname.setText("You did not enter a Full Name");
            return;
        } else {
            msgFullname.setVisibility(View.GONE);
            msgFullname.setText("");
        }
//        Log.d("DEBUG", "MASUK");
        EditText emailText = (EditText) getActivity().findViewById(R.id.registerFieldEmail);
        TextView msgEmail = (TextView) getActivity().findViewById(R.id.msgEmail);
        String email = emailText.getText().toString();
        if(!validateEmail(email)){
            emailText.requestFocus();
            msgEmail.setVisibility(View.VISIBLE);
            msgEmail.setText("Email is not Valid");
            return;
        }
        if (email.matches("")) {
            emailText.requestFocus();
            msgEmail.setVisibility(View.VISIBLE);
            msgEmail.setText("You did not enter a Email");
            return;
        } else {
            msgEmail.setVisibility(View.GONE);
            msgEmail.setText("");
        }
        EditText addressText = (EditText) getActivity().findViewById(R.id.registerFieldAddress);
        TextView msgAddress = (TextView) getActivity().findViewById(R.id.msgAddress);
        String address = addressText.getText().toString();
        if (address.matches("")) {
            addressText.requestFocus();
            msgAddress.setVisibility(View.VISIBLE);
            msgAddress.setText("You did not enter a Address");
            return;
        } else {
            msgAddress.setVisibility(View.GONE);
            msgAddress.setText("");
        }
        EditText passwordText = (EditText) getActivity().findViewById(R.id.registerFieldPassword);
        TextView msgPassword = (TextView) getActivity().findViewById(R.id.msgPassword);
        String password = passwordText.getText().toString();
        if (password.length() >= 8) {

        } else {
            passwordText.requestFocus();
            msgPassword.setVisibility(View.VISIBLE);
            msgPassword.setText("Your Password must be more than 8 characters");
            return;
        }
        if (password.matches("")) {
            passwordText.requestFocus();
            msgPassword.setVisibility(View.VISIBLE);
            msgPassword.setText("You did not enter a Password");
            return;
        } else {
            msgPassword.setVisibility(View.GONE);
            msgPassword.setText("");
        }
        progressDoalog.show();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("name", fullname);
        jsonParams.put("email", email);
        jsonParams.put("address", address);
        jsonParams.put("password", password);
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        Call call = getDataService.registerUserMobile(jsonParams);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("TOKEN STRING", response.toString());
                if (response.body() != null) {
                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (!jsonObject.has("error")) {
                            String token = jsonObject.getString("token");
                            SharedPreferences.Editor prefsEditr = preferences.edit();
                            prefsEditr.putString("token", token);
                            prefsEditr.commit();
                            prefsEditr.apply();
                            AccountFragment accountFragment = new AccountFragment();
                            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                            fragmentTransaction.commit();
                        } else {
                            JSONObject jsonObjectErr = jsonObject.getJSONObject("error");
                            if (jsonObjectErr.has("email")) {
                                String ErrorMessageMail = jsonObject.getJSONObject("error").getJSONArray("email").get(0).toString();
                                Toast.makeText(getActivity(), ErrorMessageMail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
