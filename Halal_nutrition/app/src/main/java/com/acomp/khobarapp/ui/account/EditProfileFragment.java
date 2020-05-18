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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_edit_profile, container, false);
        if(this.fullname != null){
            TextView fieldNameText = (TextView) rootView.findViewById(R.id.fieldFullname);
            fieldNameText.setText(this.fullname);
        }
        if(this.email != null){
            TextView fieldEmailText = (TextView) rootView.findViewById(R.id.fieldEmail);
            fieldEmailText.setText(this.email);
        }
        if(this.address != null){
            TextView fieldAddressText = (TextView) rootView.findViewById(R.id.fieldAddress);
            fieldAddressText.setText(this.address);
        }
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener );

        Button btnUpdateProfile = (Button) rootView.findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(updateProfileListener);
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

    private View.OnClickListener updateProfileListener = new View.OnClickListener() {
        public void onClick(View v) {
            updateProfile(v);
        }
    };

    public void updateProfile(View view) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        EditText fullnameText = (EditText) getActivity().findViewById(R.id.fieldFullname);
        String fullname = fullnameText.getText().toString();
        if (fullname.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Full Name", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText addressText = (EditText) getActivity().findViewById(R.id.fieldAddress);
        String address = addressText.getText().toString();
        if (address.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Address", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDoalog.show();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("name", fullname);
        jsonParams.put("address", address);
        Call call = getDataService.updateUser(jsonParams);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("RESULT TOKEN",response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (!jsonObject.has("error")) {
                            Boolean result = jsonObject.getBoolean("result");
                            if(result == true){
                                Toast.makeText(getActivity(), "Update Profile Success", Toast.LENGTH_SHORT).show();
                            } else {
//                                String message =  jsonObject.getString("message");
                                Toast.makeText(getActivity(), "Update Profile Success", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Update Profile Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Update Profile Failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "Update Profile Failed", Toast.LENGTH_SHORT).show();
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
