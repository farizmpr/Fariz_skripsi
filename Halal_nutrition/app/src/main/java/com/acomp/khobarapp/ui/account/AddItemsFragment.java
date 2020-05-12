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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_additem, container, false);

        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        LinearLayout btnCertificate = (LinearLayout) rootView.findViewById(R.id.certificate);
        btnCertificate.setOnClickListener(btnCertificateListener);

        ListView lv1 = (ListView) rootView.findViewById(R.id.listItemCertificate);
        lv1.setAdapter(new MyCustomBaseAdapter(getActivity(), listCertificateRowModel));
        ImageView btnSendItems = (ImageView) rootView.findViewById(R.id.btnSendItems);
        btnSendItems.setOnClickListener(sendItemsListener);

        TextInputEditText foodCodeText = (TextInputEditText) rootView.findViewById(R.id.foodCodeText);
        String foodCode = foodCodeText.getText().toString();
        if (itemsModel.getCode() != "") {
            foodCodeText.setText(itemsModel.getCode());
        }
        TextInputEditText foodNameText = (TextInputEditText) rootView.findViewById(R.id.foodNameText);
        String foodName = foodNameText.getText().toString();
        if (itemsModel.getName() != "") {
            foodNameText.setText(itemsModel.getName());
        }
        TextInputEditText foodManufactureText = (TextInputEditText) rootView.findViewById(R.id.foodManufactureText);
        String foodManufacture = foodManufactureText.getText().toString();
        if (itemsModel.getManufacture() != "") {
            foodManufactureText.setText(itemsModel.getManufacture());
        }
        TextInputEditText foodIngredientText = (TextInputEditText) rootView.findViewById(R.id.foodIngredientText);
        String foodIngredient = foodIngredientText.getText().toString();
        if (itemsModel.getIngredient() != "") {
            foodIngredientText.setText(itemsModel.getIngredient());
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private View.OnClickListener sendItemsListener = new View.OnClickListener() {
        public void onClick(View v) {
            TextInputEditText foodCodeText = (TextInputEditText) getActivity().findViewById(R.id.foodCodeText);
            String foodCode = foodCodeText.getText().toString();
            if (foodCode.matches("")) {
                foodCodeText.requestFocus();
                return;
            }
            TextInputEditText foodNameText = (TextInputEditText) getActivity().findViewById(R.id.foodNameText);
            String foodName = foodNameText.getText().toString();
            if (foodName.matches("")) {
                foodNameText.requestFocus();
                return;
            }
            TextInputEditText foodManufactureText = (TextInputEditText) getActivity().findViewById(R.id.foodManufactureText);
            String foodManufacture = foodManufactureText.getText().toString();
            if (foodManufacture.matches("")) {
                foodManufactureText.requestFocus();
                return;
            }
            TextInputEditText foodIngredientText = (TextInputEditText) getActivity().findViewById(R.id.foodIngredientText);
            String foodIngredient = foodIngredientText.getText().toString();
            if (foodIngredient.matches("")) {
                foodIngredientText.requestFocus();
                return;
            }
            if (listCertificateRowModel.size() == 0) {
                Toast.makeText(getActivity(), "Please Add Certificate", Toast.LENGTH_SHORT).show();
                return;
            }
            DialogForm(foodCode, foodName, foodManufacture, foodIngredient);
        }
    };

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
                                    HistoryItemsFragment accountFragment = new HistoryItemsFragment();
                                    fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                                    fragmentTransaction.commit();
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

                verifyStoragePermissions(getActivity());
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_IMAGE_CAMERA);
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
//        Log.d("Activity Image", "Request Code = " + requestCode);
        activityRequestCode = PICK_IMAGE_CAMERA;
        activityData = data;
        sendInsertItems(foodCode, foodName, foodManufacture, foodIngredient);

    }

    public void sendPicturePhotos(){

        if (activityRequestCode == PICK_IMAGE_CAMERA) {
            try {
                if(foodId != 0) {
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
                    RequestBody referenceTable = RequestBody.create(MediaType.parse("text/plain"), txtRefId);
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

                                        HistoryItemsFragment accountFragment = new HistoryItemsFragment();
                                        fragmentTransaction.replace(R.id.fragment_content, accountFragment);
                                        fragmentTransaction.commit();
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

    private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

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
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AddItemsCertificateFragment accountFragment = new AddItemsCertificateFragment();
            accountFragment.listCertificateRowModel = listCertificateRowModel;
            TextInputEditText foodCodeText = (TextInputEditText) getActivity().findViewById(R.id.foodCodeText);
            String foodCode = foodCodeText.getText().toString();

            TextInputEditText foodNameText = (TextInputEditText) getActivity().findViewById(R.id.foodNameText);
            String foodName = foodNameText.getText().toString();

            TextInputEditText foodManufactureText = (TextInputEditText) getActivity().findViewById(R.id.foodManufactureText);
            String foodManufacture = foodManufactureText.getText().toString();

            TextInputEditText foodIngredientText = (TextInputEditText) getActivity().findViewById(R.id.foodIngredientText);
            String foodIngredient = foodIngredientText.getText().toString();
            accountFragment.itemsModel.setCode(foodCode);
            accountFragment.itemsModel.setName(foodName);
            accountFragment.itemsModel.setIngredient(foodIngredient);
            accountFragment.itemsModel.setManufacture(foodManufacture);
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

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
