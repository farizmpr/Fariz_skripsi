package com.acomp.khobarapp.ui.items;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalalItemsFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.banner_home_1};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

//        ImageView closeBtn = (ImageView) rootView.findViewById(R.id.GoBackIcon);
//        closeBtn.setOnClickListener(goBackListener );

//        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
//        btnUpdatePassword.setOnClickListener(updatePasswordListener);

        carouselView = rootView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        return rootView;
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        Menu menu = (Menu) getActivity().findViewById(R.id.toolbar);
        MenuItem item = menu.findItem(R.id.action_fetch);
        item.setVisible(true);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

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
        String oldpassword = oldpasswordText.getText().toString();
        if (oldpassword.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a Old Password", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText newpasswordText = (EditText) getActivity().findViewById(R.id.fieldNewPassword);
        String newpassword = newpasswordText.getText().toString();
        if (newpassword.matches("")) {
            Toast.makeText(getActivity(), "You did not enter a New Password", Toast.LENGTH_SHORT).show();
            return;
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
