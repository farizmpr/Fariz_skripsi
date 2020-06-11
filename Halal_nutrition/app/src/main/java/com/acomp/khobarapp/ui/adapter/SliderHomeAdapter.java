package com.acomp.khobarapp.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.NewsModel;
import com.acomp.khobarapp.model.SliderItem;
import com.acomp.khobarapp.model.UserModel;
import com.acomp.khobarapp.ui.account.AccountFragment;
import com.acomp.khobarapp.ui.account.AccountFragment1;
import com.acomp.khobarapp.ui.account.AddItemsFragment;
import com.acomp.khobarapp.ui.home.HomeActivity;
import com.acomp.khobarapp.ui.items.HalalVenuesDetailFragment;
import com.acomp.khobarapp.ui.news.NewsDetailFragment;
import com.acomp.khobarapp.ui.news.NewsFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SliderHomeAdapter extends RecyclerView.Adapter<SliderHomeAdapter.MyViewHolder> {

    public static int LOOPS_COUNT = 1000;
    private FragmentActivity context;
    private List<SliderItem> listSlider;
    private ViewPager2 viewPager2;

    public SliderHomeAdapter(FragmentActivity context, ViewPager2 viewPager2_1, List<SliderItem> sliderImage) {
        this.context = context;
        this.listSlider = sliderImage;
        this.viewPager2 = viewPager2_1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_slider_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.tvName.setText(String.format("Row number%d", position));

        holder.tvName.setText("");
//        holder.imgBanner.setImageResource(listSlider.get(position));
        holder.setImage(listSlider.get(position));
        if (position == listSlider.size() - 2) {
            viewPager2.post(runnable);
        }
//        holder.imgBanner.setImageResource(listSlider.get(position));
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        holder.imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    NewsFragment newsFragment = new NewsFragment();
                    fragmentTransaction.replace(R.id.fragment_content, newsFragment);
                    fragmentTransaction.commit();
                } else if (position == 1) {
                    ProgressDialog progressDoalog = new ProgressDialog(context);
                    progressDoalog.setMessage("Loading....");
                    progressDoalog.show();
                    SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
                    String token = preferences.getString("token", "");
//                        Log.d("TOKEN", token);
                    if (token == null) {
                        AccountFragment1 accountFragment1 = new AccountFragment1();
                        fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                        fragmentTransaction.commit();
                        progressDoalog.dismiss();
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
                                        AddItemsFragment accountFragment = new AddItemsFragment();
                                        fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                                        fragmentTransaction.commit();
                                        progressDoalog.dismiss();
                                    } else {
//                                            Log.d("TOKEN NULL", token);
                                        AccountFragment1 accountFragment1 = new AccountFragment1();
                                        fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                                        fragmentTransaction.commit();
                                        progressDoalog.dismiss();
                                    }
                                } else {
//                                        Log.d("TOKEN LOGIN NOT SUCCESS", token);
                                    AccountFragment1 accountFragment1 = new AccountFragment1();
                                    fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                                    fragmentTransaction.commit();
                                    progressDoalog.dismiss();
                                }
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
                } else if (position == 2) {
//                    ArrayList<NewsModel> listNewsModel = new ArrayList<NewsModel>();
                    ProgressDialog progressDoalog = new ProgressDialog(context);
                    progressDoalog.setMessage("Loading....");
                    progressDoalog.show();
                    ArrayList<NewsModel> arrayNewsModel = new ArrayList<NewsModel>();
                    SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
                    String token = preferences.getString("token", "");
                    GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
                    Call call = getDataService.getNewsByKeyValue(1, "code", "1900234512346786");
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.body() != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                    NewsModel sr1 = null;
                                    Integer totalData = jsonObject.getInt("total");
//                                    total = totalData;
                                    for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                                        JSONObject objects = jsonObject.getJSONArray("data").optJSONObject(i);
//                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                                        String strDate = objects.getString("strDate");
                                        String code = objects.getString("code");
                                        String name = objects.getString("name");
                                        String content = objects.getString("content");
                                        JSONArray jsArrayAt = objects.getJSONArray("image");
                                        ArrayList<AttachmentModel> attachmentModels = new ArrayList<AttachmentModel>();
                                        if (jsArrayAt.length() > 0) {
                                            AttachmentModel attach = null;
                                            for (int i2 = 0; i2 < jsArrayAt.length(); i2++) {
                                                JSONObject objectImg = jsArrayAt.optJSONObject(i2);
                                                String path = objectImg.getString("path");
                                                String filename = objectImg.getString("filename");
                                                String type = objectImg.getString("type");
                                                String mime = objectImg.getString("mime");
                                                String url = objectImg.getString("url");
                                                attach = new AttachmentModel();
                                                attach.setPath(path);
                                                attach.setFilename(filename);
                                                attach.setType(type);
                                                attach.setMime(mime);
                                                attach.setUrl(url);
                                                attachmentModels.add(attach);
                                            }
                                        }
                                        sr1 = new NewsModel();
                                        sr1.setTitle(name);
                                        sr1.setCode(code);
                                        sr1.setContent(content);
                                        sr1.setStrDate(strDate);
                                        sr1.setAttachmentModels(attachmentModels);

                                        if (i == 0) {
                                            arrayNewsModel.add(sr1);
                                        }

                                    }
                                    if (arrayNewsModel.size() > 0) {
                                        NewsDetailFragment accountFragment1 = new NewsDetailFragment();
                                        accountFragment1.setNewsModel(arrayNewsModel.get(0));
                                        fragmentTransaction.replace(R.id.fragment_content, accountFragment1);
                                        fragmentTransaction.commit();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            } else {
//                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                            }
                            progressDoalog.dismiss();

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            progressDoalog.dismiss();
                            Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            t.getStackTrace();
                        }
                    });
                }

            }

        });
    }

    @Override
    public int getItemCount() {

        return this.listSlider.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgBanner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imgBanner = itemView.findViewById(R.id.imgBanner);
        }

        void setImage(SliderItem sliderItem) {
            imgBanner.setImageResource(sliderItem.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            listSlider.addAll(listSlider);
            notifyDataSetChanged();
        }
    };
}
