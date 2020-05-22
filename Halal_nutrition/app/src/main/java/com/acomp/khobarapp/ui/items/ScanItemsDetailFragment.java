package com.acomp.khobarapp.ui.items;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.media.MediaBrowserServiceCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.ui.adapter.ListFoodBaseAdapter;
import com.acomp.khobarapp.ui.home.HomeFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import com.acomp.khobarapp.ZXingScannerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanItemsDetailFragment extends Fragment implements ZXingScannerView.ResultHandler {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    ItemsModel itemsModel = new ItemsModel();
    CarouselView carouselView;
    String[] sampleImages;
    List<String> mImages = new ArrayList<String>();
    ArrayList<ItemsModel> listFoodModel = null;
    private ZXingScannerView mScannerView;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_scan, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);
        if (!checkPermission()) {
            requestPermission();
        }
        mScannerView = new ZXingScannerView(getActivity());
        RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.scannerLayout);
        rl.addView(mScannerView);

//        mScannerView.startCamera();
        RelativeLayout layFlashCamera = (RelativeLayout) rootView.findViewById(R.id.layFlashCamera);
layFlashCamera.setOnClickListener(layFlashCameraListener);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
//        navBar.setVisibility(View.GONE);
        return rootView;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        onResume();
//                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        ScanItemsDetailFragment halalItemsFragment = new ScanItemsDetailFragment();
//                        fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
//                        fragmentTransaction.commit();
                    }
                }
        }
    }

    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
            Picasso.get().load(mImages.get(position)).into(imageView);
        }
    };

    public ItemsModel setItemsModel(ItemsModel itemsModel) {
        this.itemsModel = itemsModel;
        return this.itemsModel;
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
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        mScannerView.setSoundEffectsEnabled(true);
        mScannerView.setAutoFocus(true);
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HomeFragment accountFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener layFlashCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView btnFlashCamera = (ImageView) getActivity().findViewById(R.id.btnFlashCamera);
//            Drawable getBackG = btnFlashCamera.getBackground();
//            Log.d("DEBUG DRAW","D="+getBackG.toString());

            if(mScannerView.getFlash() == true){
                mScannerView.setFlash(false);
                btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));
            } else {
                mScannerView.setFlash(true);
                btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on_black_24dp));
            }

        }
    };


    @Override
    public void handleResult(Result rawResult) {
        Log.v("TEXT_1 CAMERA", rawResult.getText()); // Prints scan results
        Log.v("TEXT_2 CAMERA", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
//        Toast.makeText(getActivity(), "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_LONG).show();
//        mScannerView.resumeCameraPreview(this);
        getListVenues(rawResult.getText());

    }

    public void restartCamera() {
        mScannerView.stopCamera();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        mScannerView.setSoundEffectsEnabled(true);
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        mScannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void handleResult(MediaBrowserServiceCompat.Result rawResult) {

        // If you would like to resume scanning, call this method below:

    }

    public void getListVenues(String code) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
//        if (listFoodModel == null) {
//            listFoodModel = new ArrayList<ItemsModel>();
//        }
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call call = getDataService.getDetailFood("code", code);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        ItemsModel sr1 = null;
                        Integer totalData = jsonObject.getInt("total");
//                        Log.d("TOTAL JSON",String.valueOf(totalData));
//                        total = totalData;
                        JSONArray jsArrayData = jsonObject.getJSONArray("data");
                        if (jsArrayData.length() > 0) {
                            JSONObject objects = jsonObject.getJSONArray("data").optJSONObject(0);
//                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                            String ingredient = objects.getString("ingredient");
                            String code = objects.getString("code");
                            String name = objects.getString("name");
                            String manufacture = objects.getString("manufacture");
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
                                    String url = null;
                                    if (objectImg.isNull("url")) {
                                        url = null;
                                    } else {
                                        url = objectImg.getString("url");
                                    }

                                    attach = new AttachmentModel();
                                    attach.setPath(path);
                                    attach.setFilename(filename);
                                    attach.setType(type);
                                    attach.setMime(mime);
                                    attach.setUrl(url);
                                    attachmentModels.add(attach);
                                }
                            }
                            JSONArray jsArrayCert = objects.getJSONArray("certificate");
                            ArrayList<CertificateRowModel> certificateRowModels = new ArrayList<CertificateRowModel>();
                            if (jsArrayCert.length() > 0) {
                                CertificateRowModel certMod = null;
                                for (int i2 = 0; i2 < jsArrayCert.length(); i2++) {
                                    JSONObject objectCert = jsArrayCert.optJSONObject(i2);
                                    String codeCert = null;
                                    if (!objectCert.isNull("code")) {
                                        codeCert = objectCert.getString("code");
                                    }
                                    String nameCert = null;
                                    if (!objectCert.isNull("name")) {
                                        nameCert = objectCert.getString("name");
                                    }
                                    String organizationName = null;
                                    if (!objectCert.isNull("organization_name")) {
                                        organizationName = objectCert.getString("organization_name");
                                    }


                                    certMod = new CertificateRowModel();
                                    certMod.setCode(codeCert);
                                    certMod.setTitle(organizationName);
//                                    attach.setType(type);
//                                    attach.setMime(mime);
//                                    attach.setUrl(url);
                                    certificateRowModels.add(certMod);
                                }
                            }
                            sr1 = new ItemsModel();
                            sr1.setName(name);
                            sr1.setCode(code);
                            sr1.setIngredient(ingredient);
                            sr1.setManufacture(manufacture);
                            sr1.setOrganization(manufacture);
                            sr1.setAttachmentModels(attachmentModels);
                            sr1.setCertificateRowModels(certificateRowModels);
                            HalalItemsDetailFragment halalItemsFragment = new HalalItemsDetailFragment();
                            halalItemsFragment.setItemsModel(sr1);
                            fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
                            fragmentTransaction.commit();
                        } else {
                            restartCamera();
                            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        restartCamera();
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    restartCamera();
//                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressDoalog.dismiss();
                restartCamera();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
