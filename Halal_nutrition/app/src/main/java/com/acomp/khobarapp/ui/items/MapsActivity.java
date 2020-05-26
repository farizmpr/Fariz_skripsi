package com.acomp.khobarapp.ui.items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acomp.khobarapp.model.VenuesModel;
import com.acomp.khobarapp.ui.adapter.DirectionsJSONParser;
import com.acomp.khobarapp.utils.GPSTracker;
import com.acomp.khobarapp.utils.RetrofitClientInstance;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.acomp.khobarapp.R;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleMap map;
    VenuesModel venuesModel = new VenuesModel();
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private MarkerOptions mMarkerOptions;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    ArrayList<LatLng> markerPoints;
    private Criteria criteria;
    private String provider;
    private View rootViews = null;
    private String subTime = "";
    private String subDistance = "";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        RelativeLayout closeBtn = (RelativeLayout) rootView.findViewById(R.id.back);
        closeBtn.setOnClickListener(goBackListener);

        TextView addressOnMaps = (TextView) rootView.findViewById(R.id.addressOnMaps);
        addressOnMaps.setText(venuesModel.getAddress());

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



//        map = mapFragment.getMap();
        this.rootViews = rootView;
//        if (rootViews != null) {

//        }
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

    private View.OnClickListener goToMapsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str_origin = mOrigin.latitude + "," + mOrigin.longitude;

            // Destination of route
            String str_dest = mDestination.latitude + "," + mDestination.longitude;
            String url = "https://www.google.com/maps/dir/" + str_origin + "/" + str_dest;
            openWebURL(url);
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
        Double latitude = 0.0;
        if (venuesModel.getLatitude() != null) {
            latitude = Double.parseDouble(venuesModel.getLatitude());
        }
        Double longitude = 0.0;
        if (venuesModel.getLongitude() != null) {
            longitude = Double.parseDouble(venuesModel.getLongitude());
        }
        mDestination = new LatLng(latitude, longitude);

        getMyLocation();
        // Add a marker in Sydney and move the camera


//        Log.d("LATLONG","LAT="+latitude+",LONG="+longitude);
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(venuesModel.getName()));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));


        MaterialButton goToMapsBTN = (MaterialButton) getActivity().findViewById(R.id.goToMaps);
        goToMapsBTN.setOnClickListener(goToMapsListener);


    }

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

        startActivity(browse);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == 100) {
            if (!verifyAllPermissions(grantResults)) {
                Toast.makeText(getActivity().getApplicationContext(), "No sufficient permissions", Toast.LENGTH_LONG).show();
            } else {
                getMyLocation();
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

    private void getMyLocation() {
        Log.d("MORIGIN START", "FIRST");
        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

        // user defines the criteria
        mMap.setMyLocationEnabled(true);
        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = mLocationManager.getBestProvider(criteria, false);

/*
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("MORIGIN START", "OK");
                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("MORIGIN START", "OK 2");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 10));


                if (mOrigin != null && mDestination != null) {
//                    Log.d("MORIGIN START", "OK NEXT");
//                    drawRoute();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getActivity(), provider + "'s status changed to "+status +"!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getActivity(), "Provider " + provider + " enabled!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getActivity(), "Provider " + provider + " disabled!",
                        Toast.LENGTH_SHORT).show();
            }
        };
*/
        int currentApiVersion = Build.VERSION.SDK_INT;
//        if (currentApiVersion >= Build.VERSION_CODES.M) {

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
//                mMap.setMyLocationEnabled(true);
            Log.d("MORIGIN START", "SECOND");
//            mLocationManager.requestLocationUpdates(provider, 2000, 0, mLocationListener);
/*
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mDestination = latLng;
                    mMap.clear();
                    mMarkerOptions = new MarkerOptions().position(mDestination).title("Destination");
                    mMap.addMarker(mMarkerOptions);
                    if (mOrigin != null && mDestination != null)
                        drawRoute();
                }
            });

 */

        } else {
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
//        }
        GPSTracker gpsTracker = new GPSTracker(getActivity());

        if (gpsTracker.getIsGPSTrackingEnabled())   {

        }
//        Location location = mLocationManager.getLastKnownLocation(provider);
//        if(location != null){
//
//        }

//        String a=""+location.getLatitude();
//        Toast.makeText(getActivity().getApplicationContext(), a,  Toast.LENGTH_SHORT).show();

    }


    private void drawRoute() {

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);
        Log.d("MAP URL", url);
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
//        String key = "key=" + getString(R.string.google_maps_key);
        String key = "key=" + getString(R.string.google_maps_key_2);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key + "&sensor=true";

        // Output format
        String output = "json";

        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        String url = RetrofitClientInstance.BASE_URL + "venues/directions?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception on download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LONGLAT2", "LAT="+location.getLatitude()+"&LONG="+location.getLongitude());
        Toast.makeText(getActivity(), location.getLatitude() + " WORKS " + location.getLongitude() + "", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) getActivity());

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            mOrigin = new LatLng(currentLatitude, currentLongitude);
//            Log.d("LONGLAT", "LAT="+gpsTracker.getLatitude()+"&LONG="+gpsTracker.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 9));
            if (mOrigin != null && mDestination != null) {
                drawRoute();
            }
//            Toast.makeText(getActivity(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            JSONArray jsonArray;
            JSONArray jsonArray2;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                String distanceText = "";
                Integer distanceTimeVal = 0;
                for (int i = 0; i < jObject.getJSONArray("routes").length(); i++) {
//                    jsonArray = jObject.getJSONArray("routes");
                    JSONObject objects = jObject.getJSONArray("routes").optJSONObject(i);
                    for (int j = 0; j < objects.getJSONArray("legs").length(); j++) {
                        JSONObject objects2 = objects.getJSONArray("legs").optJSONObject(i);
                        distanceText = objects2.getJSONObject("distance").getString("text");
                        distanceTimeVal = objects2.getJSONObject("duration").getInt("value");
                        Log.d("distanceText", distanceText + "=" + distanceTimeVal);
                    }
                }
//                long minutes = TimeUnit.MILLISECONDS
//                        .toMinutes(distanceTimeVal);
                Integer numberOfMinutes = 0;
                if (distanceTimeVal > 0) {
                    numberOfMinutes = ((distanceTimeVal % 86400) % 3600) / 60;
                }
                subDistance = distanceText;
                subTime = numberOfMinutes.toString();
                // Starts parsing data


                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
//                    double lng = point.get("").j;
                    points.add(position);

                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);
                TextView txtSubTime = (TextView) getActivity().findViewById(R.id.txtSubTime);
                txtSubTime.setText(subTime + " min");

                TextView txtSubDistance = (TextView) getActivity().findViewById(R.id.txtSubDistance);
                txtSubDistance.setText(subDistance);
            } else
                Toast.makeText(getActivity().getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
        }
    }

}
