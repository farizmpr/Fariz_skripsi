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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragmentSignUp extends Fragment {

    ProgressDialog progressDoalog;
    private BottomNavigationView mBtmView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_reg, container, false);
        ImageView b = (ImageView) rootView.findViewById(R.id.GoBackIcon);
        b.setOnClickListener(mButtonClickListener);

        Button btnSaveSignUp = (Button) rootView.findViewById(R.id.btnSaveSignUp);
        btnSaveSignUp.setOnClickListener(btnSaveSignUpListener);
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
            AccountFragment1 accountFragment = new AccountFragment1();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    public void signUpProcess(View view) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        EditText fullnameText = (EditText) getActivity().findViewById(R.id.registerFieldFullname);
        String fullname = fullnameText.getText().toString();
        if (fullname.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Full Name", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("DEBUG", "MASUK");
        EditText emailText = (EditText) getActivity().findViewById(R.id.registerFieldEmail);
        String email = emailText.getText().toString();
        if (email.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Email", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText addressText = (EditText) getActivity().findViewById(R.id.registerFieldAddress);
        String address = addressText.getText().toString();
        if (address.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Address", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText passwordText = (EditText) getActivity().findViewById(R.id.registerFieldPassword);
        String password = passwordText.getText().toString();
        if (password.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Password", Toast.LENGTH_SHORT).show();
            return;
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

}
