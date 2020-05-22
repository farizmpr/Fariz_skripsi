package com.acomp.khobarapp.ui.items;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acomp.khobarapp.model.VenuesModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.acomp.khobarapp.R;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    VenuesModel venuesModel = new VenuesModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        TextView addressOnMaps = (TextView) rootView.findViewById(R.id.addressOnMaps);
        addressOnMaps.setText(venuesModel.getAddress());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    private View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            HalalVenuesDetailFragment halalVenuesDetailFragment = new HalalVenuesDetailFragment();
            halalVenuesDetailFragment.setVenuesModel(venuesModel);
            fragmentTransaction.replace(R.id.fragment_content, halalVenuesDetailFragment);
            fragmentTransaction.commit();
        }
    };

    public VenuesModel setVenuesModel(VenuesModel itemsModel) {
        this.venuesModel = itemsModel;
        return this.venuesModel;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Double latitude = 0.0;
        if(venuesModel.getLatitude() != null){
            latitude = Double.parseDouble(venuesModel.getLatitude());
        }
        Double longitude = 0.0;
        if(venuesModel.getLongitude() != null){
            longitude = Double.parseDouble(venuesModel.getLongitude());
        }
        Log.d("LATLONG","LAT="+latitude+",LONG="+longitude);
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Jakarta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
