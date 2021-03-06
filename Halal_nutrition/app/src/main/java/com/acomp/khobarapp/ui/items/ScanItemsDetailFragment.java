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
import com.acomp.khobarapp.model.IngredientDetailModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NutritionDetailModel;
import com.acomp.khobarapp.model.NutritionModel;
import com.acomp.khobarapp.ui.adapter.ListFoodBaseAdapter;
import com.acomp.khobarapp.ui.home.HomeFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
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
    RelativeLayout layScanNotFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_scan, container, false);
        ImageView closeBtn = (ImageView) rootView.findViewById(R.id.GoBackIcon);
        closeBtn.setOnClickListener(goBackListener);
        if (!checkPermission()) {
            requestPermission();
        }
        mScannerView = new ZXingScannerView(getActivity());
        RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.scannerLayout);
        rl.addView(mScannerView);

        MaterialButton tabsOCRBtn = (MaterialButton) rootView.findViewById(R.id.tabsOCR);
        tabsOCRBtn.setOnClickListener(tabsOCRBtnListener);
//        mScannerView.startCamera();
        ImageView layFlashCamera = (ImageView) rootView.findViewById(R.id.btnFlashCamera);
        layFlashCamera.setOnClickListener(layFlashCameraListener);

        layScanNotFound  = (RelativeLayout) rootView.findViewById(R.id.layScanNotFound);
        layScanNotFound.setVisibility(View.GONE);

        Button btnScanAgain = (Button) rootView.findViewById(R.id.btnScanAgain);
        btnScanAgain.setOnClickListener(btnScanAgainListener);

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
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
    private View.OnClickListener tabsOCRBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            ScanOCRItemsDetailFragment accountFragment = new ScanOCRItemsDetailFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnScanAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            ScanItemsDetailFragment accountFragment = new ScanItemsDetailFragment();
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

            if (mScannerView.getFlash() == true) {
                mScannerView.setFlash(false);
                btnFlashCamera.setImageResource(R.drawable.ic_flash_off_black_24dp);
//                btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));
            } else {
                mScannerView.setFlash(true);
                btnFlashCamera.setImageResource(R.drawable.ic_flash_on_black_24dp);
//                btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on_black_24dp));
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
                            Integer isIngredient = 0;
                            if(!objects.isNull("is_ingredient")){
                                isIngredient = objects.getInt("is_ingredient");
                            }
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
                                    String expiredDate = null;
                                    if (!objectCert.isNull("expired_date")) {
                                        expiredDate = objectCert.getString("expired_date");
                                    }

                                    Integer certificateStatusId = null;
                                    if (!objectCert.isNull("certificate_status_id")) {
                                        certificateStatusId = objectCert.getInt("certificate_status_id");
                                    }

                                    String certificateStatus = "";
                                    if (!objectCert.isNull("certificate_status")) {
                                        JSONObject jsonObjectCertificateStatus = objectCert.getJSONObject("certificate_status");
                                        if (!jsonObjectCertificateStatus.isNull("name")) {
                                            certificateStatus = jsonObjectCertificateStatus.getString("name");
                                        }
                                    }

                                    Integer certificateTypeId = null;
                                    if (!objectCert.isNull("certificate_type_id")) {
                                        certificateTypeId = objectCert.getInt("certificate_type_id");
                                    }

                                    String certificateType = "";
                                    if (!objectCert.isNull("certificate_type")) {
                                        JSONObject jsonObjectCertificateType = objectCert.getJSONObject("certificate_type");
                                        if (!jsonObjectCertificateType.isNull("name")) {
                                            certificateType = jsonObjectCertificateType.getString("name");
                                        }
                                    }

                                    certMod = new CertificateRowModel();
                                    certMod.setCode(codeCert);
                                    certMod.setTitle(organizationName);
                                    certMod.setExpiredDate(expiredDate);
                                    certMod.setTypeId(certificateTypeId);
                                    certMod.setHalalStatus(certificateStatus);
                                    certMod.setStatusId(certificateStatusId);
                                    certMod.setType(certificateType);
                                    certificateRowModels.add(certMod);
                                }
                            }

                            JSONObject jsObjNutri = objects.getJSONObject("nutrition");

                            //GET NUTRITION SERVING
                            JSONArray jsObjNutriServ = jsObjNutri.getJSONArray("serving");
                            NutritionModel nutritionModel = new NutritionModel();
                            ArrayList<NutritionDetailModel> nutritionDetailModelsServ = new ArrayList<NutritionDetailModel>();
                            if (jsObjNutriServ.length() > 0) {
                                NutritionDetailModel nuDeModServ = null;
                                for (int i2 = 0; i2 < jsObjNutriServ.length(); i2++) {
                                    JSONObject objectServ = jsObjNutriServ.optJSONObject(i2);
                                    String codeServ = null;
                                    if (!objectServ.isNull("code")) {
                                        codeServ = objectServ.getString("code");
                                    }
                                    String nameServ = null;
                                    if (!objectServ.isNull("name")) {
                                        nameServ = objectServ.getString("name");
                                    }
                                    Double valueServ = null;
                                    if (!objectServ.isNull("value")) {
                                        valueServ = objectServ.getDouble("value");
                                    }
                                    String unitCodeServ = null;
                                    if (!objectServ.isNull("unit_code")) {
                                        unitCodeServ = objectServ.getString("unit_code");
                                    }
                                    Double percentageServ = null;
                                    if (!objectServ.isNull("percentage")) {
                                        percentageServ = objectServ.getDouble("percentage");
                                    }

                                    nuDeModServ = new NutritionDetailModel();
                                    nuDeModServ.setCode(codeServ);
                                    nuDeModServ.setName(nameServ);
                                    nuDeModServ.setValue(valueServ);
                                    nuDeModServ.setUnitCode(unitCodeServ);
                                    nuDeModServ.setPercentage(percentageServ);

                                    JSONArray jsObjNutriChildServ = objectServ.getJSONArray("child");
                                    ArrayList<NutritionDetailModel> nutritionDetailModelsChildServ = new ArrayList<NutritionDetailModel>();
                                    if (jsObjNutriChildServ.length() > 0) {
                                        NutritionDetailModel nuDeModChildServ = null;
                                        for (int i22 = 0; i22 < jsObjNutriChildServ.length(); i22++) {
                                            JSONObject objectChildServ = jsObjNutriChildServ.optJSONObject(i22);
                                            String codeChildServ = null;
                                            if (!objectChildServ.isNull("code")) {
                                                codeChildServ = objectChildServ.getString("code");
                                            }
                                            String nameChildServ = null;
                                            if (!objectChildServ.isNull("name")) {
                                                nameChildServ = objectChildServ.getString("name");
                                            }
                                            Double valueChildServ = null;
                                            if (!objectChildServ.isNull("value")) {
                                                valueChildServ = objectChildServ.getDouble("value");
                                            }
                                            String unitCodeChildServ = null;
                                            if (!objectChildServ.isNull("unit_code")) {
                                                unitCodeChildServ = objectChildServ.getString("unit_code");
                                            }
                                            Double percentageChildServ = null;
                                            if (!objectChildServ.isNull("percentage")) {
                                                percentageChildServ = objectChildServ.getDouble("percentage");
                                            }

                                            nuDeModChildServ = new NutritionDetailModel();
                                            nuDeModChildServ.setCode(codeChildServ);
                                            nuDeModChildServ.setName(nameChildServ);
                                            nuDeModChildServ.setValue(valueChildServ);
                                            nuDeModChildServ.setUnitCode(unitCodeChildServ);
                                            nuDeModChildServ.setPercentage(percentageChildServ);
                                            nutritionDetailModelsChildServ.add(nuDeModChildServ);
                                        }

                                    }
                                    nuDeModServ.setChild(nutritionDetailModelsChildServ);
                                    nutritionDetailModelsServ.add(nuDeModServ);
                                }
                            }
                            nutritionModel.setServing(nutritionDetailModelsServ);

                            //GET NUTRITION DAILY VALUE
                            JSONArray jsObjNutriDai = jsObjNutri.getJSONArray("daily_value");
                            NutritionModel nutritionModelDai = new NutritionModel();
                            ArrayList<NutritionDetailModel> nutritionDetailModelsDai = new ArrayList<NutritionDetailModel>();
                            if (jsObjNutriDai.length() > 0) {
                                NutritionDetailModel nuDeModDai = null;
                                for (int i2 = 0; i2 < jsObjNutriDai.length(); i2++) {
                                    JSONObject objectDai = jsObjNutriDai.optJSONObject(i2);
                                    String codeDai = null;
                                    if (!objectDai.isNull("code")) {
                                        codeDai = objectDai.getString("code");
                                    }
                                    String nameDai = null;
                                    if (!objectDai.isNull("name")) {
                                        nameDai = objectDai.getString("name");
                                    }
                                    Double valueDai = null;
                                    if (!objectDai.isNull("value")) {
                                        valueDai = objectDai.getDouble("value");
                                    }
                                    String unitCodeDai = null;
                                    if (!objectDai.isNull("unit_code")) {
                                        unitCodeDai = objectDai.getString("unit_code");
                                    }
                                    Double percentageDai = null;
                                    if (!objectDai.isNull("percentage")) {
                                        percentageDai = objectDai.getDouble("percentage");
                                    }

                                    nuDeModDai = new NutritionDetailModel();
                                    nuDeModDai.setCode(codeDai);
                                    nuDeModDai.setName(nameDai);
                                    nuDeModDai.setValue(valueDai);
                                    nuDeModDai.setUnitCode(unitCodeDai);
                                    nuDeModDai.setPercentage(percentageDai);
                                    nutritionDetailModelsDai.add(nuDeModDai);
                                    JSONArray jsObjNutriChildDai = objectDai.getJSONArray("child");
                                    ArrayList<NutritionDetailModel> nutritionDetailModelsChildDai = new ArrayList<NutritionDetailModel>();
                                    if (jsObjNutriChildDai.length() > 0) {
                                        NutritionDetailModel nuDeModChildDai = null;
                                        for (int i22 = 0; i22 < jsObjNutriChildDai.length(); i22++) {
                                            JSONObject objectChildDai = jsObjNutriChildDai.optJSONObject(i22);
                                            String codeChildDai = null;
                                            if (!objectChildDai.isNull("code")) {
                                                codeChildDai = objectChildDai.getString("code");
                                            }
                                            String nameChildDai = null;
                                            if (!objectChildDai.isNull("name")) {
                                                nameChildDai = objectChildDai.getString("name");
                                            }
                                            Double valueChildDai = null;
                                            if (!objectChildDai.isNull("value")) {
                                                valueChildDai = objectChildDai.getDouble("value");
                                            }
                                            String unitCodeChildDai = null;
                                            if (!objectChildDai.isNull("unit_code")) {
                                                unitCodeChildDai = objectChildDai.getString("unit_code");
                                            }
                                            Double percentageChildDai = null;
                                            if (!objectChildDai.isNull("percentage")) {
                                                percentageChildDai = objectChildDai.getDouble("percentage");
                                            }

                                            nuDeModChildDai = new NutritionDetailModel();
                                            nuDeModChildDai.setCode(codeChildDai);
                                            nuDeModChildDai.setName(nameChildDai);
                                            nuDeModChildDai.setValue(valueChildDai);
                                            nuDeModChildDai.setUnitCode(unitCodeChildDai);
                                            nuDeModChildDai.setUrutChild(2);
                                            nuDeModChildDai.setPercentage(percentageChildDai);
                                            nutritionDetailModelsChildDai.add(nuDeModChildDai);
                                            nutritionDetailModelsDai.add(nuDeModChildDai);
                                        }
//                                        nutritionDetailModelsDai.add(nuDeModChildDai);
                                    }
                                    nuDeModDai.setChild(nutritionDetailModelsChildDai);

                                }
                            }

                            JSONArray jsArrayIngDetail = objects.getJSONArray("ingredient_detail");
                            ArrayList<IngredientDetailModel> ingredientDetailModels = new ArrayList<IngredientDetailModel>();
                            if (jsArrayIngDetail.length() > 0) {
                                IngredientDetailModel ingredientDetailModel = null;
                                for (int i5 = 0; i5 < jsArrayIngDetail.length(); i5++) {
                                    JSONObject objectIngDetail = jsArrayIngDetail.optJSONObject(i5);
                                    String ingTitle = "";
                                    if (!objectIngDetail.isNull("title")) {
                                        ingTitle = objectIngDetail.getString("title");
                                    }
                                    String ingType = "";
                                    if (!objectIngDetail.isNull("type")) {
                                        ingType = objectIngDetail.getString("type");
                                    }
                                    String ingDesc = "";
                                    if (!objectIngDetail.isNull("description")) {
                                        ingDesc = objectIngDetail.getString("description");
                                    }
                                    ingredientDetailModel = new IngredientDetailModel();
                                    ingredientDetailModel.setTitle(ingTitle);
                                    ingredientDetailModel.setType(ingType);
                                    ingredientDetailModel.setDescription(ingDesc);
                                    ingredientDetailModels.add(ingredientDetailModel);
                                }
                            }

                            NutritionDetailModel nuDeModChildDaiMore = new NutritionDetailModel();
                            nuDeModChildDaiMore.setType("more");
                            nutritionDetailModelsDai.add(nuDeModChildDaiMore);
                            nutritionModel.setDailyValue(nutritionDetailModelsDai);

                            sr1 = new ItemsModel();
                            sr1.setName(name);
                            sr1.setCode(code);
                            sr1.setIsIngredient(isIngredient);
                            sr1.setIngredientDetailModels(ingredientDetailModels);
                            sr1.setIngredient(ingredient);
                            sr1.setManufacture(manufacture);
                            sr1.setOrganization(manufacture);
                            sr1.setAttachmentModels(attachmentModels);
                            sr1.setCertificateRowModels(certificateRowModels);
                            sr1.setNutrition(nutritionModel);


                            HalalItemsDetailFragment halalItemsFragment = new HalalItemsDetailFragment();
                            halalItemsFragment.setBeforeFragment("scanBarcode");
                            halalItemsFragment.setItemsModel(sr1);
                            halalItemsFragment.setBeforeFragment("scanBarcode");
                            fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
                            fragmentTransaction.commit();
                        } else {
                            restartCamera();
//                            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                            layScanNotFound.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        restartCamera();
//                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        layScanNotFound.setVisibility(View.VISIBLE);
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
