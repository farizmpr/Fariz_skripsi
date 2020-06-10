package com.acomp.khobarapp.ui.items;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.media.MediaBrowserServiceCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.ZXingScannerView;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.AttachmentModel;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.NutritionDetailModel;
import com.acomp.khobarapp.model.NutritionModel;
import com.acomp.khobarapp.ui.home.HomeFragment;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanOCRItemsDetailFragment extends Fragment {
    ProgressDialog progressDoalog;
    SwipeRefreshLayout pullToRefresh;
    ItemsModel itemsModel = new ItemsModel();
    CarouselView carouselView;
    String[] sampleImages;
    List<String> mImages = new ArrayList<String>();
    ArrayList<ItemsModel> listFoodModel = null;
    CameraSource mCameraSource;
    SurfaceView mCameraView;
    TextView mTextView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Camera camera = null;
    boolean flashmode=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_scan_ocr, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);
        if (!checkPermission()) {
            requestPermission();
        }

        mCameraView = (SurfaceView) rootView.findViewById(R.id.surfaceView);
        mTextView =  (TextView) rootView.findViewById(R.id.text_view);
//        mScannerView = new ZXingScannerView(getActivity());
//        RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.scannerLayout);
//        rl.addView(mScannerView);
        MaterialButton tabsBarcodeBtn = (MaterialButton) rootView.findViewById(R.id.tabsBarcode);
        tabsBarcodeBtn.setOnClickListener(tabsBarcodeBtnListener);
//        mScannerView.startCamera();
        startCameraSource();
        RelativeLayout layFlashCamera = (RelativeLayout) rootView.findViewById(R.id.layFlashCamera);
        layFlashCamera.setOnClickListener(layFlashCameraListener);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;
    }

    private View.OnClickListener tabsBarcodeBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            ScanItemsDetailFragment accountFragment = new ScanItemsDetailFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("DETECTOR", "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                String textOCR= stringBuilder.toString();
                                mTextView.setText(textOCR);
                                getListVenues(textOCR);
                            }
                        });
                    }
                }
            });
        }
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
//        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        mScannerView.startCamera();
//        mScannerView.setSoundEffectsEnabled(true);
//        mScannerView.setAutoFocus(true);
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
            camera=getCamera(mCameraSource);
            if (camera != null) {
                try {
                    Camera.Parameters param = camera.getParameters();
                    param.setFlashMode(!flashmode?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(param);
                    flashmode = !flashmode;
                    if(flashmode){
//                        showToast("Flash Switched ON");
                        btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on_black_24dp));
                    }
                    else {
//                        showToast("Flash Switched Off");
                        btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
//            if (mCameraSource.get() == true) {
//                mScannerView.setFlash(false);
//                btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));
//            } else {
//                mScannerView.setFlash(true);
//                btnFlashCamera.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flash_on_black_24dp));
//            }

        }
    };
    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    public void getListVenues(String code) {
        if(getActivity() != null) {
            progressDoalog = new ProgressDialog(getActivity());
            progressDoalog.setMessage("Loading....");
            progressDoalog.show();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call call = getDataService.getDetailFood("name", code);
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
                                        String expiredDate = null;
                                        if (!objectCert.isNull("expired_date")) {
                                            expiredDate = objectCert.getString("expired_date");
                                        }

                                        certMod = new CertificateRowModel();
                                        certMod.setCode(codeCert);
                                        certMod.setTitle(organizationName);
                                        certMod.setExpiredDate(expiredDate);
//                                    attach.setType(type);
//                                    attach.setMime(mime);
//                                    attach.setUrl(url);
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
                                NutritionDetailModel nuDeModChildDaiMore = new NutritionDetailModel();
                                nuDeModChildDaiMore.setType("more");
                                nutritionDetailModelsDai.add(nuDeModChildDaiMore);
                                nutritionModel.setDailyValue(nutritionDetailModelsDai);


                                sr1 = new ItemsModel();
                                sr1.setName(name);
                                sr1.setCode(code);
                                sr1.setIngredient(ingredient);
                                sr1.setManufacture(manufacture);
                                sr1.setOrganization(manufacture);
                                sr1.setAttachmentModels(attachmentModels);
                                sr1.setCertificateRowModels(certificateRowModels);
                                sr1.setNutrition(nutritionModel);
                                HalalItemsDetailFragment halalItemsFragment = new HalalItemsDetailFragment();
                                halalItemsFragment.setItemsModel(sr1);
                                fragmentTransaction.replace(R.id.fragment_content, halalItemsFragment);
                                fragmentTransaction.commit();
                            } else {
//                            restartCamera();
//                                Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
//                        restartCamera();
//                            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    } else {
//                    restartCamera();
//                    Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
                    }
                    progressDoalog.dismiss();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progressDoalog.dismiss();
//                restartCamera();
                    Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
