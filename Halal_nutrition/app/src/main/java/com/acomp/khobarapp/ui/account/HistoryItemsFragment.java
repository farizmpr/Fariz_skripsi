package com.acomp.khobarapp.ui.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.StringWithTag;
import com.acomp.khobarapp.ui.adapter.HistoryItemsBaseAdapter;
import com.acomp.khobarapp.ui.adapter.MyCustomBaseAdapter;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryItemsFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_history_items, container, false);

        ImageView closeBtn = (ImageView) rootView.findViewById(R.id.GoBackIcon);
        closeBtn.setOnClickListener(goBackListener);

//        Button btnUpdatePassword = (Button) rootView.findViewById(R.id.btnUpdatePassword);
//        btnUpdatePassword.setOnClickListener(updatePasswordListener);
        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            int Refreshcounter = 1; //Counting how many times user have refreshed the layout
            @Override
            public void onRefresh() {
                getHistoryItems();
//                Refreshcounter = Refreshcounter + 1;
//                notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

        getHistoryItems();
        return rootView;
    }

    public void getHistoryItems(){
        ArrayList<ItemsModel> listItemsModels =  new ArrayList<ItemsModel>();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Call call = getDataService.getItemsByUsers();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        ItemsModel sr1 =null;
                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                            JSONObject objects = jsonObject.getJSONArray("data").optJSONObject(i);
//                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                            String code = objects.getString("code");
                            String name = objects.getString("name");
                            String manufacture = objects.getString("manufacture");
                            String ingredient = objects.getString("ingredient");
                            String status = objects.getString("status");

                            sr1 = new ItemsModel();
                            sr1.setCode(code);
                            sr1.setName(name);
                            sr1.setManufacture(manufacture);
                            sr1.setIngredient(ingredient);
                            sr1.setStatus(status);
                            if(objects.getJSONArray("certificate").length() >= 1){
                                String organization = objects.getJSONArray("certificate").optJSONObject(0).getString("organization_name");
                                sr1.setOrganization(organization);
                            }

                            listItemsModels.add(sr1);
//                            itemList.add(new StringWithTag(objects.getString("name"), objects.getInt("id")));
                        }
                        ListView lv1 = (ListView) getActivity().findViewById(R.id.listHistoryItems);
                        lv1.setAdapter(new HistoryItemsBaseAdapter(getActivity(), listItemsModels));
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
//                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                }
//                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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


}
