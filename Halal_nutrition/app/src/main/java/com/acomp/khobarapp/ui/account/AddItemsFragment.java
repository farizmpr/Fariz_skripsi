package com.acomp.khobarapp.ui.account;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.ui.adapter.MyCustomBaseAdapter;
import com.acomp.khobarapp.ui.home.HomeActivity;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemsFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String email = null;
    public String address = null;
    ArrayList<CertificateRowModel> listCertificateRowModel = new ArrayList<CertificateRowModel>();
    ItemsModel itemsModel = new ItemsModel();
    AlertDialog dialog;
    LayoutInflater inflater;
    View dialogView;
    private Bitmap bitmap;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Integer activityRequestCode = 0;
    private String foodCode;
    private String foodName;
    private Intent activityData;
    private String foodManufacture;
    private String foodIngredient;
    private Integer foodId = 0;
    private NachoTextView mChipsView;
    private RelativeLayout layAddSuccessItems;
    private RelativeLayout btnBackAddSuccessItems;
    private LinearLayout layAddItems;
    public FragmentActivity fragmentActivity = null;

    public Boolean isShowHideBtnAddItem = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_additem, container, false);

        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        layAddSuccessItems = (RelativeLayout) rootView.findViewById(R.id.layAddSuccess);
        layAddItems = (LinearLayout) rootView.findViewById(R.id.layAddItems);
//        layAddSuccessItems.setOnClickListener(layAddSuccessItemsListener);

        btnBackAddSuccessItems = (RelativeLayout) rootView.findViewById(R.id.backAddSuccessItems);
        btnBackAddSuccessItems.setOnClickListener(btnBackAddSuccessItemsListener);


        RelativeLayout btnCertificate = (RelativeLayout) rootView.findViewById(R.id.certificate);
        btnCertificate.setOnClickListener(btnCertificateListener);


        ListView listItemCertificate = (ListView) rootView.findViewById(R.id.listItemCertificate);
        Button btnAddCertificate = (Button) rootView.findViewById(R.id.btnAddCertificate);
        RelativeLayout layAddCert = (RelativeLayout) rootView.findViewById(R.id.certificate);
        LinearLayout btnBottomAddCertificate = (LinearLayout) rootView.findViewById(R.id.btnBottomAddCertificate);
        TextView titleCertificate = (TextView) rootView.findViewById(R.id.titleCertificate);
        RelativeLayout itemCertificate = (RelativeLayout) rootView.findViewById(R.id.itemCertificate);
        RelativeLayout layTitleCertificate = (RelativeLayout) rootView.findViewById(R.id.layTitleCertificate);

        if (isShowHideBtnAddItem == true) {
            layAddCert.setVisibility(View.VISIBLE);
            btnBottomAddCertificate.setVisibility(View.GONE);
            listItemCertificate.setVisibility(View.GONE);
            titleCertificate.setVisibility(View.GONE);
            itemCertificate.setVisibility(View.GONE);
            layTitleCertificate.setVisibility(View.GONE);
        } else {
            listItemCertificate.setVisibility(View.VISIBLE);
            layAddCert.setVisibility(View.GONE);
            titleCertificate.setVisibility(View.VISIBLE);
            btnBottomAddCertificate.setVisibility(View.VISIBLE);
            itemCertificate.setVisibility(View.VISIBLE);
            layTitleCertificate.setVisibility(View.VISIBLE);
        }
        btnAddCertificate.setOnClickListener(btnCertificateListener);
        btnBottomAddCertificate.setOnClickListener(btnCertificateListener);


        ImageView btnSendItems = (ImageView) rootView.findViewById(R.id.btnSendItems);
        btnSendItems.setOnClickListener(sendItemsListener);

        EditText foodCodeText = (EditText) rootView.findViewById(R.id.foodCodeText);
        String foodCode = foodCodeText.getText().toString();
        if (itemsModel.getCode() != "") {
            foodCodeText.setText(itemsModel.getCode());
        }
        EditText foodNameText = (EditText) rootView.findViewById(R.id.foodNameText);
        String foodName = foodNameText.getText().toString();
        if (itemsModel.getName() != "") {
            foodNameText.setText(itemsModel.getName());
        }
        EditText foodManufactureText = (EditText) rootView.findViewById(R.id.foodManufactureText);
        String foodManufacture = foodManufactureText.getText().toString();
        if (itemsModel.getManufacture() != "") {
            foodManufactureText.setText(itemsModel.getManufacture());
        }

        mChipsView = (NachoTextView) rootView.findViewById(R.id.foodIngredientText);
//        mChipsView.setText();
        mChipsView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        mChipsView.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        mChipsView.enableEditChipOnTouch(true, true);


//        String foodIngredient = foodIngredientText.getText().toString();
        if (itemsModel.getIngredient() != "") {
            String[] splitIngredient = itemsModel.getIngredient().split(";");
            mChipsView.setText(Arrays.asList(splitIngredient));
//            ingredientTxt += text.toString() + ";";
//            foodIngredientText.setText(itemsModel.getIngredient());
        }

        String ingredientTxt = "";
        for (Chip chip : mChipsView.getAllChips()) {
            CharSequence text = chip.getText();
            ingredientTxt += text.toString() + ";";
            Object data = chip.getData();
        }
        itemsModel.setCode(foodCode);
        itemsModel.setName(foodName);
        itemsModel.setManufacture(foodManufacture);
        itemsModel.setIngredient(ingredientTxt);
        ListView lv1 = (ListView) rootView.findViewById(R.id.listItemCertificate);
        lv1.setAdapter(new MyCustomBaseAdapter(getActivity(), listCertificateRowModel, getActivity(), itemsModel));
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private View.OnClickListener sendItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(formValidationAddItems()){
                DialogForm(foodCode, foodName, foodManufacture, foodIngredient);
            }
        }
    };

    public Boolean formValidationAddItems() {
        EditText foodCodeText = (EditText) getActivity().findViewById(R.id.foodCodeText);
        TextView msgProductCode = (TextView) getActivity().findViewById(R.id.msgProductCode);
        String foodCode = foodCodeText.getText().toString();
        if (foodCode.length() >= 8 && foodCode.length() <= 13) {

        } else {
            foodCodeText.requestFocus();
            msgProductCode.setVisibility(View.VISIBLE);
            msgProductCode.setText("Product Code must be more than 8 characters and less than 13 characters");
            return false;
        }
        if (foodCode.matches("")) {
            foodCodeText.requestFocus();
            msgProductCode.setVisibility(View.VISIBLE);
            msgProductCode.setText("Product Code is Required");
            return false;
        } else {
            msgProductCode.setVisibility(View.GONE);
            msgProductCode.setText("");
        }

        EditText foodNameText = (EditText) getActivity().findViewById(R.id.foodNameText);
        TextView msgProductName = (TextView) getActivity().findViewById(R.id.msgProductName);
        String foodName = foodNameText.getText().toString();
        if (foodName.matches("")) {
            foodNameText.requestFocus();
            msgProductName.setVisibility(View.VISIBLE);
            msgProductName.setText("Product Name is Required");
            return false;
        } else {
            msgProductName.setVisibility(View.GONE);
            msgProductName.setText("");
        }
        EditText foodManufactureText = (EditText) getActivity().findViewById(R.id.foodManufactureText);
        TextView msgProductManufacture = (TextView) getActivity().findViewById(R.id.msgProductManufacture);
        String foodManufacture = foodManufactureText.getText().toString();
        if (foodManufacture.matches("")) {
            foodManufactureText.requestFocus();
            msgProductManufacture.setVisibility(View.VISIBLE);
            msgProductManufacture.setText("Product Manufacture is Required");
            return false;
        } else {
            msgProductManufacture.setVisibility(View.GONE);
            msgProductManufacture.setText("");
        }
        TextView msgProductIngredient = (TextView) getActivity().findViewById(R.id.msgProductIngredient);
        String ingredientTxt = "";
        Integer totalProductIngredient = 0;
        for (Chip chip : mChipsView.getAllChips()) {
            totalProductIngredient += 1;
            CharSequence text = chip.getText();
            ingredientTxt += text.toString() + ";";
            Object data = chip.getData();
        }
//        Enter At Least Two Ingredients
        if (totalProductIngredient >= 2) {
            msgProductIngredient.setVisibility(View.GONE);
            msgProductIngredient.setText("");
        } else {
            mChipsView.requestFocus();
            msgProductIngredient.setVisibility(View.VISIBLE);
            msgProductIngredient.setText("Please Enter At Least Two Ingredients");
            return false;
        }
        if (ingredientTxt == "") {
            mChipsView.requestFocus();
            msgProductIngredient.setVisibility(View.VISIBLE);
            msgProductIngredient.setText("Product Ingredient is Required");
            return false;
        } else {
            msgProductIngredient.setVisibility(View.GONE);
            msgProductIngredient.setText("");
        }
        foodIngredient = method(ingredientTxt);
        if (listCertificateRowModel.size() == 0) {
            Toast.makeText(getActivity(), "Please Add Certificate", Toast.LENGTH_SHORT).show();
            return false;
        }
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.foodIngredient = foodIngredient;
        this.foodManufacture = foodManufacture;
        return true;
    }

    private Integer sendInsertItems(String foodCode, String foodName, String foodManufacture, String foodIngredient) {
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("code", foodCode);
        jsonParams.put("name", foodName);
        jsonParams.put("manufacture", foodManufacture);
        jsonParams.put("ingredient", foodIngredient);
        ArrayList<Object> arrayListCertificate = null;
//            ArrayList<String, Object> jsonListParams = new ArrayList<>();
        for (CertificateRowModel lst : listCertificateRowModel) {
            arrayListCertificate = new ArrayList<Object>();
//                arrayListCertificate.
            Map<String, Object> jsonParamsCertificate = new ArrayMap<>();
            jsonParamsCertificate.put("code", lst.getCode());
            jsonParamsCertificate.put("expiredDate", lst.getExpiredDate());
            jsonParamsCertificate.put("certificateStatusId", lst.getStatusId());
            jsonParamsCertificate.put("certificateTypeId", lst.getTypeId());
            jsonParamsCertificate.put("organization", lst.getTitle());
            Log.d("SEND DATA", lst.getCode() + "-" + lst.getExpiredDate() + "-" + lst.getTitle() + "-" + lst.getType());
            arrayListCertificate.add(jsonParamsCertificate);
        }
        jsonParams.put("certificate", arrayListCertificate);
//            String resultJson = new Gson().toJson(arrayListCertificate);
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
        Call call = getDataService.insertItems(jsonParams);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                progressDoalog.dismiss();
                Log.d("TOKEN STRING", response.toString());
                if (response.body() != null) {
                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.getBoolean("result")) {
                            if (jsonObject.getJSONObject("data") != null) {
                                Integer id = jsonObject.getJSONObject("data").getInt("id");
                                if (activityRequestCode == PICK_IMAGE_CAMERA) {
                                    foodId = id;
                                    sendPicturePhotos();
//                                    return id;
                                } else {
                                    layAddSuccessItems.setVisibility(View.VISIBLE);
                                    layAddItems.setVisibility(View.GONE);
//                                    HistoryItemsFragment accountFragment = new HistoryItemsFragment();
//                                    fragmentTransaction.replace(R.id.fragment_content, accountFragment);
//                                    fragmentTransaction.commit();
                                    Toast.makeText(getActivity(), "Insert Items Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Insert Items Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
//                        progressDoalog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Insert Items Error", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        return 0;
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AccountFragment accountFragment = new AccountFragment();
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnClickListener btnBackAddSuccessItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
//            HistoryItemsFragment accountFragment = new HistoryItemsFragment();
//            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
//            fragmentTransaction.commit();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HistoryItemsFragment historyItemsFragment = new HistoryItemsFragment();
            fragmentTransaction.replace(R.id.fragment_content, historyItemsFragment);
            fragmentTransaction.commit();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == 100) {
            if (!verifyAllPermissions(grantResults)) {
                Toast.makeText(getActivity().getApplicationContext(), "No sufficient permissions", Toast.LENGTH_LONG).show();
            } else {
                verifyStoragePermissions(getActivity());
                Log.d("CHOOSE CAMERA", "OK");
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_IMAGE_CAMERA);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean verifyAllPermissions(int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private final String[] listPermission = new String[]{
            Manifest.permission.CAMERA
    };

    private void DialogForm(String foodCode, String foodName, String foodManufacture, String foodIngredient) {
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.foodManufacture = foodManufacture;
        this.foodIngredient = foodIngredient;
        dialog = new AlertDialog.Builder(getActivity()).create();
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.notif_photos, null);
        Button btnAddPhoto = (Button) dialogView.findViewById(R.id.btnAddPhoto);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getActivity().checkSelfPermission(listPermission[0]) != PackageManager.PERMISSION_DENIED) {
                    verifyStoragePermissions(getActivity());
                    Log.d("CHOOSE CAMERA", "OK");
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, PICK_IMAGE_CAMERA);
                } else {
                    requestPermissions(listPermission, 100);
                }

            }
        });
        Button btnWithoutPhoto = (Button) dialogView.findViewById(R.id.btnWithoutPhoto);
        btnWithoutPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);
                sendInsertItems(foodCode, foodName, foodManufacture, foodIngredient);
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        inputStreamImg = null;
        Log.d("Activity Image", "Request Code = " + requestCode);
        activityRequestCode = PICK_IMAGE_CAMERA;
        activityData = data;
        sendInsertItems(foodCode, foodName, foodManufacture, foodIngredient);

    }

    public void sendPicturePhotos() {

        if (activityRequestCode == PICK_IMAGE_CAMERA) {
            try {
                if (foodId != 0) {
                    Uri selectedImage = activityData.getData();
                    bitmap = (Bitmap) activityData.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    File file = savebitmap(bitmap);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                    SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    String token = preferences.getString("token", "");
                    GetDataService getDataService = RetrofitClientInstance.getRetrofitAuthInstance(token).create(GetDataService.class);
                    String txtRefId = foodId.toString();
                    String txtRefTable = "mst_item";
                    RequestBody referenceId = RequestBody.create(MediaType.parse("text/plain"), txtRefId);
                    RequestBody referenceTable = RequestBody.create(MediaType.parse("text/plain"), txtRefTable);
                    progressDoalog = new ProgressDialog(getActivity());
                    progressDoalog.setMessage("Loading....");
                    progressDoalog.show();
//                String referenceId = "1";
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Call call = getDataService.uploadAttachment(body, referenceId, referenceTable);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
//                        Log.d("TOKEN STRING", response.toString());
                            if (response.body() != null) {
//                            Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                                try {
                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                    if (jsonObject.getBoolean("result")) {
                                        layAddSuccessItems.setVisibility(View.VISIBLE);
                                        layAddItems.setVisibility(View.GONE);

                                        Toast.makeText(getActivity(), "Upload File Berhasil", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                    progressDoalog.dismiss();
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), "Upload File Failed", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(getActivity(), "Upload File Failed", Toast.LENGTH_SHORT).show();
                            }
                            progressDoalog.dismiss();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            t.printStackTrace();
                            progressDoalog.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (activityRequestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = activityData.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

//                destination = new File(imgPath.toString());
//                imageview.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        String currentTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss", Locale.getDefault()).format(new Date());

        // String temp = null;
        File file = new File(extStorageDirectory, currentTime + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, currentTime + ".png");

        }
        try {

            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private View.OnClickListener btnCertificateListener = new View.OnClickListener() {
        public void onClick(View v) {
            clickBtnAddListener();
        }
    };

    public void clickBtnAddListener() {
        if (fragmentActivity == null) {
            fragmentActivity = getActivity();
        }
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();

        AddItemsCertificateFragment accountFragment = new AddItemsCertificateFragment();
        accountFragment.listCertificateRowModel = listCertificateRowModel;
        EditText foodCodeText = (EditText) getActivity().findViewById(R.id.foodCodeText);
        String foodCode = foodCodeText.getText().toString();

        EditText foodNameText = (EditText) getActivity().findViewById(R.id.foodNameText);
        String foodName = foodNameText.getText().toString();

        EditText foodManufactureText = (EditText) getActivity().findViewById(R.id.foodManufactureText);
        String foodManufacture = foodManufactureText.getText().toString();
        String ingredientTxt = "";
        for (Chip chip : mChipsView.getAllChips()) {
            CharSequence text = chip.getText();
            ingredientTxt += text.toString() + ";";
            Object data = chip.getData();
        }
        String ingredientTxt2 = method(ingredientTxt);
        foodIngredient = ingredientTxt2;
        accountFragment.itemsModel.setCode(foodCode);
        accountFragment.itemsModel.setName(foodName);
        accountFragment.itemsModel.setIngredient(foodIngredient);
        accountFragment.itemsModel.setManufacture(foodManufacture);
        fragmentTransaction.replace(R.id.fragment_content, accountFragment);
        fragmentTransaction.commit();
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ';') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private ArrayList<CertificateRowModel> GetSearchResults() {
        ArrayList<CertificateRowModel> results = new ArrayList<CertificateRowModel>();

        CertificateRowModel sr1 = new CertificateRowModel();
        sr1.setTitle("John Smith");
        sr1.setType("Dallas, TX");
        results.add(sr1);

        sr1 = new CertificateRowModel();
        sr1.setTitle("Jane Doe");
        sr1.setType("Atlanta, GA");
        results.add(sr1);

        sr1 = new CertificateRowModel();
        sr1.setTitle("Steve Young");
        sr1.setType("Miami, FL");
        results.add(sr1);

        sr1 = new CertificateRowModel();
        sr1.setTitle("Fred Jones");
        sr1.setType("Las Vegas, NV");
        results.add(sr1);

        return results;
    }


}
