package com.acomp.khobarapp.ui.items;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.adapter.ListFoodVenuesBaseAdapter;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalalVenuesDetailFragment extends Fragment {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    VenuesModel venuesModel = new VenuesModel();
    CarouselView carouselView;
    String[] sampleImages;
    List<String> mImages = new ArrayList<String>();
    private static final int PERMISSION_REQUEST_CODE = 200;
    ListFoodVenuesBaseAdapter listFoodVenuesBaseAdapter = null;
    ArrayList<AttachmentModel> listVenuesFoodModel = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detil_venue, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        LinearLayout venuesCallPhone = (LinearLayout) rootView.findViewById(R.id.venuesCallPhone);
        venuesCallPhone.setOnClickListener(venuesCallPhoneListener);

        LinearLayout venuesMaps = (LinearLayout) rootView.findViewById(R.id.venuesMaps);
        venuesMaps.setOnClickListener(venuesMapsListener);

        TextView tvTitleHead = (TextView) rootView.findViewById(R.id.venuesTitleHead);
        tvTitleHead.setText(venuesModel.getName());
        TextView tvTitle = (TextView) rootView.findViewById(R.id.venuesTitle);
        tvTitle.setText(venuesModel.getName());
        TextView venuesAddress = (TextView) rootView.findViewById(R.id.venuesAddress);
        venuesAddress.setText(venuesModel.getAddress());

        TextView strTotalImage = (TextView) rootView.findViewById(R.id.strTotalImage);
        Integer totalImage = venuesModel.getAttachmentModels().size();
        strTotalImage.setText("See All (" + totalImage + ")");
        if(venuesModel.getAttachmentModels().size() > 0){
            strTotalImage.setOnClickListener(totalImageListener);
        }


        AppCompatRatingBar appCompatRatingBar = (AppCompatRatingBar) rootView.findViewById(R.id.place_rating);
        Float votes = Float.valueOf(String.valueOf(venuesModel.getVotes()));
        appCompatRatingBar.setRating(votes);

        TextView txtRatingStar = (TextView) rootView.findViewById(R.id.txtRatingStar);
        txtRatingStar.setText(venuesModel.getVotes().toString());
//        TextView tvManufacture = (TextView) rootView.findViewById(R.id.manufacture);
//        tvManufacture.setText("\u25CF " + itemsModel.getManufacture());
//        TextView tvIngredient = (TextView) rootView.findViewById(R.id.list_ingredient_food);
//        String ingredient = itemsModel.getIngredient();
//        ingredient = ingredient.replace("\\\n", System.getProperty("line.separator"));
//        tvIngredient.setText("\u25CF " + ingredient);
        ImageView venuesImageHead = (ImageView) rootView.findViewById(R.id.venuesImageHead);

//        carouselView = rootView.findViewById(R.id.carouselViewDetailFood);
        String imageUrl = "https://via.placeholder.com/468x250?text=No+Image";
        Integer no = 0;
        if (venuesModel.getAttachmentModels().size() > 0) {
            for (AttachmentModel attachmentModel : venuesModel.getAttachmentModels()) {
                if (no == 0) {
                    imageUrl = attachmentModel.getUrl();
                    if (imageUrl == null) {
                        mImages.add("https://via.placeholder.com/468x250?text=No+Image");
                    } else {
                        mImages.add(imageUrl);
                    }
                }
//            Picasso.get().load(imageUrl).fit().centerCrop().into(imgPoster);
                no++;
            }
        } else {
            mImages.add("https://via.placeholder.com/468x250?text=No+Image");
        }
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).fit()
                    .centerCrop().into(venuesImageHead);
        }
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//
//        RecyclerView myList = (RecyclerView) rootView.findViewById(R.id.listFoodVenues);
//        myList.setLayoutManager(layoutManager);
        getListFoodVenues(rootView);
        getVenuesScheduleNow(rootView);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
//        navBar.setVisibility(View.VISIBLE);
        return rootView;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
            Picasso.get().load(mImages.get(position)).into(imageView);
        }
    };

    public VenuesModel setVenuesModel(VenuesModel itemsModel) {
        this.venuesModel = itemsModel;
        return this.venuesModel;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_fetch);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    public void getListFoodVenues(View view) {
        if (listVenuesFoodModel == null) {
            listVenuesFoodModel = new ArrayList<AttachmentModel>();
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listFoodVenues);
        assert recyclerView != null;
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        listFoodVenuesBaseAdapter = new ListFoodVenuesBaseAdapter(getActivity(), venuesModel.getAttachmentModels(),venuesModel);

        recyclerView.setAdapter(listFoodVenuesBaseAdapter);
    }

    public void getVenuesScheduleNow(View view) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call call = getDataService.getVenuesScheduleNow(venuesModel.getId());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String strScheduleStatus = jsonObject.getString("str_status_schedule");
                        Integer scheduleStatus = jsonObject.getInt("status_schedule");
                        String strOpenSchedule = null;
                        if(!jsonObject.isNull("str_open_schedule")) {
                            strOpenSchedule = jsonObject.getString("str_open_schedule");
                        }
                        TextView scheduleStatusHead = (TextView) view.findViewById(R.id.strScheduleStatusHead);
                        String strScheduleStatus_1 = "CLOSE";
                        if (scheduleStatus == 1) {
                            strScheduleStatus_1 = "OPEN";
                        }
                        scheduleStatusHead.setText(strScheduleStatus_1);
                        TextView descScheduleStatus = (TextView) view.findViewById(R.id.descStatusSchedule);
                        descScheduleStatus.setText(strScheduleStatus + " daily time " + strOpenSchedule);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HalalVenuesFragment accountFragment = new HalalVenuesFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener totalImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            VenuesFoodGalleryFragment venuesFoodGalleryFragment = new VenuesFoodGalleryFragment();
            venuesFoodGalleryFragment.setVenuesModel(venuesModel);
            fragmentTransaction.replace(R.id.fragment_content, venuesFoodGalleryFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener venuesCallPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callPhoneNumber();
        }
    };

    public void callPhoneNumber() {
        if (!checkPermission()) {
            requestPermission();
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + venuesModel.getPhoneNumber()));
            startActivity(intent);
        }

    }

    private View.OnClickListener venuesMapsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            } else {
                venuesGoMaps();
            }

        }
    };
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } else {
                            venuesGoMaps();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            }
        });
        alert.show();
    }

    public void venuesGoMaps(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        MapsActivity mapsActivity = new MapsActivity();
        mapsActivity.setVenuesModel(venuesModel);
        fragmentTransaction.replace(R.id.fragment_content, mapsActivity);
        fragmentTransaction.commit();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean callAccepter = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (callAccepter) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + venuesModel.getPhoneNumber()));
                        startActivity(intent);
                    }
                }
        }
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }
}
