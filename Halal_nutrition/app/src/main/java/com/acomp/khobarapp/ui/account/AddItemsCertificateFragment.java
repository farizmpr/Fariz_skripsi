package com.acomp.khobarapp.ui.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.api.GetDataService;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.model.StringWithTag;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemsCertificateFragment extends Fragment {
    ProgressDialog progressDoalog;
    public String fullname = null;
    public String dateED = null;
    public String organization = null;
    public Integer halalStatusId = null;
    public Integer halalTypeId = null;
    public Integer halalCertificateIndex = null;
    Map<String, Integer> mapStatusCert = new HashMap<>();
    Map<String, Integer> mapTypeCert = new HashMap<>();
    public ArrayList<CertificateRowModel> listCertificateRowModel =  new ArrayList<CertificateRowModel>();
    public ItemsModel itemsModel = new ItemsModel();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_certificate, container, false);

        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        TextView titleHeadCertificate = (TextView) rootView.findViewById(R.id.titleHeadCertificate);
//

        EditText txtFullname = (EditText) rootView.findViewById(R.id.name_edit_text);
        if(fullname != null){
            txtFullname.setText(fullname);
            titleHeadCertificate.setText("Edit Certificate");
        }


        EditText date = (EditText) rootView.findViewById(R.id.Date);
        if(dateED != null){
            date.setText(dateED);
        }

        EditText txtOrganization = (EditText) rootView.findViewById(R.id.halal_org);
        if(organization != null){
            txtOrganization.setText(organization);
        }
        date.setOnClickListener(dateListenerClick);
        date.setOnFocusChangeListener(dateListener);
        Spinner mySpinner = (Spinner) rootView.findViewById(R.id.spinner1);
        Spinner mySpinner2 = (Spinner) rootView.findViewById(R.id.spinner_type);

        TextView btnSendCertificate = (TextView) rootView.findViewById(R.id.btnSendCertificate);
        btnSendCertificate.setOnClickListener(sendCertificateListener);
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call call = getDataService.getCertificateStatus();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
//                Log.d("TOKEN STRING", response.toString());
                if (response.body() != null) {
//                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    try {
                        List<StringWithTag> itemList = new ArrayList<StringWithTag>();
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        Integer selIndex = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objects = jsonArray.optJSONObject(i);
                            mapStatusCert.put(objects.getString("name"), objects.getInt("id"));
                            itemList.add(new StringWithTag(objects.getString("name"), objects.getInt("id")));
                            if(halalStatusId != null){
                                if(halalStatusId == objects.getInt("id")){
                                    selIndex = i;
                                }

                            }
                        }
                        ArrayAdapter<StringWithTag> spinnerAdapter = new ArrayAdapter<StringWithTag>(getActivity(), android.R.layout.simple_spinner_item, itemList);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mySpinner.setAdapter(spinnerAdapter);
//                        mySpinner.setPadding(0, mySpinner.getPaddingTop(), mySpinner.getPaddingRight(), mySpinner.getPaddingBottom());

                        if(halalStatusId != null) {
                            mySpinner.setSelection(selIndex);
                        }
                    } catch (JSONException e) {
//                        Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
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

        Call callType = getDataService.getCertificateType();
        callType.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
//                Log.d("TOKEN STRING", response.toString());
                if (response.body() != null) {
//                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    try {
                        List<StringWithTag> itemList = new ArrayList<StringWithTag>();
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        Integer selIndex = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objects = jsonArray.optJSONObject(i);
                            mapTypeCert.put(objects.getString("name"), objects.getInt("id"));
                            itemList.add(new StringWithTag(objects.getString("name"), objects.getInt("id")));
                            if(halalTypeId != null){
                                if(halalTypeId == objects.getInt("id")){
                                    selIndex = i;
                                }

                            }
                        }
                        ArrayAdapter<StringWithTag> spinnerAdapter = new ArrayAdapter<StringWithTag>(getActivity(), android.R.layout.simple_spinner_item, itemList);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mySpinner2.setAdapter(spinnerAdapter);
//                        mySpinner2.setPadding(0, mySpinner2.getPaddingTop(), mySpinner2.getPaddingRight(), mySpinner2.getPaddingBottom());
                        if(halalTypeId != null) {
                            mySpinner2.setSelection(selIndex);
                        }
                    } catch (JSONException e) {
//                        Toast.makeText(getActivity(), "Email Has been Registered , Please Write Other Email", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
        navBar.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private View.OnClickListener dateListenerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDatePicker();
        }
    };
    private View.OnClickListener sendCertificateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText codeCertificate = (EditText) getActivity().findViewById(R.id.name_edit_text);
            String code = codeCertificate.getText().toString();
            TextView msgCertificateNumber = (TextView) getActivity().findViewById(R.id.msgCertificateNumber);
            if (code.matches("")) {
                codeCertificate.requestFocus();
                msgCertificateNumber.setVisibility(View.VISIBLE);
                msgCertificateNumber.setText("You did not enter a Certificate Number");
                return;
            } else {
                msgCertificateNumber.setVisibility(View.GONE);
                msgCertificateNumber.setText("");
            }
            EditText dateCertificate = (EditText) getActivity().findViewById(R.id.Date);
            String date = dateCertificate.getText().toString();
            TextView msgDateExpired = (TextView) getActivity().findViewById(R.id.msgDateExpired);
            if (date.matches("")) {
                dateCertificate.requestFocus();
                msgDateExpired.setVisibility(View.VISIBLE);
                msgDateExpired.setText("You did not enter a Expiry Date");
                return;
            } else {
                msgDateExpired.setVisibility(View.GONE);
                msgDateExpired.setText("");
            }
            Spinner mySpinner = (Spinner) getActivity().findViewById(R.id.spinner1);
            String selectedStatus = mySpinner.getSelectedItem().toString();
//            mySpinner.getSelectedItemId();
            Integer selectStatus = mapStatusCert.get(selectedStatus);
            Spinner mySpinner2 = (Spinner) getActivity().findViewById(R.id.spinner_type);
            String selectedType = mySpinner2.getSelectedItem().toString();
            Integer selectType = mapTypeCert.get(selectedType);
            EditText halalCertificate = (EditText) getActivity().findViewById(R.id.halal_org);
            String halal = halalCertificate.getText().toString();
            TextView msgHalalOrganization = (TextView) getActivity().findViewById(R.id.msgHalalOrganization);
            if (halal.matches("")) {
                halalCertificate.requestFocus();
                msgHalalOrganization.setVisibility(View.VISIBLE);
                msgHalalOrganization.setText("You did not enter a Halal Organization");
                return;
            } else {
                msgHalalOrganization.setVisibility(View.GONE);
                msgHalalOrganization.setText("");
            }
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AddItemsFragment accountFragment = new AddItemsFragment();
            CertificateRowModel sr1 = new CertificateRowModel();
            sr1.setTitle(halal);
            String type = selectedType;
            sr1.setType(type);
            sr1.setTypeId(selectType);
            sr1.setHalalStatus(selectedStatus);
            sr1.setStatusId(selectStatus);
            sr1.setExpiredDate(date);
            sr1.setCode(code);
            if(fullname != null){
                listCertificateRowModel.set(halalCertificateIndex,sr1);
            } else {
                listCertificateRowModel.add(sr1);
            }
            accountFragment.listCertificateRowModel = listCertificateRowModel;
            accountFragment.itemsModel = itemsModel;
            accountFragment.isShowHideBtnAddItem = false;
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };

    private View.OnFocusChangeListener dateListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                showDatePicker();
            }

        }
    };


    public void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Choose Date");
        MaterialDatePicker<Long> picker = builder.build();
        picker.show(getActivity().getSupportFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                EditText date = (EditText) getActivity().findViewById(R.id.Date);

                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;

                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date dates = new Date(selectedDate + offsetFromUTC);
                date.setText(simpleFormat.format(dates));
//                dataEntry.setText(simpleFormat.format(lDate));
            }
        });
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            AddItemsFragment accountFragment = new AddItemsFragment();
            accountFragment.listCertificateRowModel = listCertificateRowModel;
            accountFragment.itemsModel = itemsModel;
            if(listCertificateRowModel.size() > 0 ){
                accountFragment.isShowHideBtnAddItem = false;
            } else {
                accountFragment.isShowHideBtnAddItem = true;
            }
            fragmentTransaction.replace(R.id.fragment_content, accountFragment);
            fragmentTransaction.commit();
        }
    };



}
