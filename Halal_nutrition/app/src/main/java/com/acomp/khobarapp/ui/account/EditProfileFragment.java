package com.acomp.khobarapp.ui.account;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    ProgressDialog progressDoalog;
    public Integer userId = null;
    public String fullname = null;
    public String email = null;
    public String address = null;
    Integer attachmentId = 0;
    String attachmentUrl = "";
    Bitmap bitmap = null;
    CircularImageView circularImageView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_edit_profile, container, false);
        if (this.fullname != null) {
            TextView fieldNameText = (TextView) rootView.findViewById(R.id.fieldFullname);
            fieldNameText.setText(this.fullname);
        }
        if (this.email != null) {
            TextView fieldEmailText = (TextView) rootView.findViewById(R.id.fieldEmail);
            fieldEmailText.setText(this.email);
        }
        if (this.address != null) {
            TextView fieldAddressText = (TextView) rootView.findViewById(R.id.fieldAddress);
            fieldAddressText.setText(this.address);
        }
        circularImageView = (CircularImageView) rootView.findViewById(R.id.profile_image);
        circularImageView.setImageDrawable(getResources().getDrawable(R.drawable.sample_avatar));
        circularImageView.setImageResource(R.drawable.sample_avatar);
        if (attachmentId != 0) {
            Log.d("Image Profile",attachmentUrl);
            Picasso.get().load(attachmentUrl).fit().centerCrop().into(circularImageView);
        }

        RelativeLayout layUpdatePhotoProfile = (RelativeLayout) rootView.findViewById(R.id.layUpdatePhotoProfile);
        layUpdatePhotoProfile.setOnClickListener(layUpdatePhotoProfileListener);
//        circularImageView.
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        Button btnUpdateProfile = (Button) rootView.findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(updateProfileListener);

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private final int SELECT_IMAGE = 200;
    private final String[] listPermission = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View.OnClickListener layUpdatePhotoProfileListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (getActivity().checkSelfPermission(listPermission[0]) != PackageManager.PERMISSION_DENIED &&
                    getActivity().checkSelfPermission(listPermission[1]) != PackageManager.PERMISSION_DENIED ) {
                selectImage();
            } else {
                requestPermissions(listPermission, 100);
            }


        }
    };

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == 100) {
            if (!verifyAllPermissions(grantResults)) {
                Toast.makeText(getActivity().getApplicationContext(), "No sufficient permissions", Toast.LENGTH_LONG).show();
            } else {
                selectImage();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                        circularImageView.setImageDrawable(d);
                        circularImageView.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
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
                Log.d("RESULT TOKEN", response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (!jsonObject.has("error")) {
                            Boolean result = jsonObject.getBoolean("result");
                            if (result == true) {
                                if (bitmap == null) {
                                    Toast.makeText(getActivity(), "Update Profile Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDoalog.hide();
                                    sendPhotoProfile();
                                }

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

    public void sendPhotoProfile() {
//        AddItemsFragment addItemsFragment = new AddItemsFragment();
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
        String txtRefId = userId.toString();
        String txtRefTable = "users";
        RequestBody referenceId = RequestBody.create(MediaType.parse("text/plain"), txtRefId);
        RequestBody referenceTable = RequestBody.create(MediaType.parse("text/plain"), txtRefTable);
        RequestBody rAttachmentId = RequestBody.create(MediaType.parse("text/plain"), attachmentId.toString());
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        Call call = getDataService.uploadAttachmentById(body, rAttachmentId, referenceId, referenceTable);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
//                        Log.d("TOKEN STRING", response.toString());
                if (response.body() != null) {
//                            Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.getBoolean("result")) {
//                            layAddSuccessItems.setVisibility(View.VISIBLE);
//                            layAddItems.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), "Update Profile Success", Toast.LENGTH_SHORT).show();
                            progressDoalog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        progressDoalog.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Upload File Failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        progressDoalog.dismiss();
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

    public File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        String currentTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss", Locale.getDefault()).format(new Date());

        // String temp = null;
        char randChar = randomSeriesForThreeCharacter();
        File file = new File(extStorageDirectory, email + ".png?r=" + randChar);
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, email + ".png?r=" + randChar);

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

    public static char randomSeriesForThreeCharacter() {
        Random r = new Random();
        char random_3_Char = (char) (97 + r.nextInt(10));
        return random_3_Char;
    }
}
