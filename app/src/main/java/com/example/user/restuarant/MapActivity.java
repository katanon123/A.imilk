package com.example.user.restuarant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.restuarant.commonclass.GPSTracker;
import com.example.user.restuarant.commonclass.ReadJson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {
    MapView mMapView;
    private GoogleMap mMap;
    private Marker mMarker;
    GPSTracker gps;
    double lat, lng, Mylat, Mylng, lati1, longti1;
    ReadJson readJson;
    ProgressDialog pDialog;
    private Button tabMapfilterbutton;
    private String gropuMember = "all";
    String locationId, locationName, locationTitle, lati, longti;
    private int IsYEC;
    private int IsVIP;
    String IP,Name;
    private int Id;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        IP = getResources().getString(R.string.path_webservice);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately


        SharedPreferences cookie1 = getSharedPreferences("ComplaintCategory", 0);//เรียกใช้ คุ้กกี้
        Id = cookie1.getInt("sComplaintCategory", 0);



        Intent intent = getIntent();
//        lati = intent.getStringExtra("Latitude");
//        longti = intent.getStringExtra("Longitude");
        lati ="17.866119";
        longti = "102.7209975";
        lati1 = Double.parseDouble(lati); //แปลงString เป็น Double
        longti1 = Double.parseDouble(longti);
        Name = intent.getStringExtra("Name");

        mMap = mMapView.getMap();

        mMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lati1, longti1))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(Name)
                .snippet(lati1+","+longti1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati1, longti1), 12));// zoom map


        //setUpMapIfNeeded();
        setUpMap2();
    }
    private void setUpMap2() {
        mMap.clear();
        gps = new GPSTracker(MapActivity.this);

        if (gps.canGetLocation()) {
            Mylat = gps.getLatitude();
            Mylng = gps.getLongitude();
//            mMarker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(Mylat, Mylng))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                    .title("My Location")
//                    .snippet("0")
//            );
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lati1, longti1))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(Name)
                    .snippet(lati1+","+longti1));

//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Mylat, Mylng), 12));// zoom map
//            if (gropuMember.equals("all")) {
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Mylat, Mylng), 12));// zoom map
//            }
        } else {
            Toast.makeText(getApplication(), "กรุณาเปิด GPS", Toast.LENGTH_LONG).show();

        }
//        String rqsurl, resultJson;
//
//
//        rqsurl = IP + "GetTicketOrderByCompliantCate/" + Id;
//        resultJson = "GetTicketOrderByCompliantCateResult";
//
//
//        readJson = new ReadJson();
//        String jsonResult = readJson.getHttpGet(rqsurl);
//        try {
//            JSONObject jsonObjMain = new JSONObject(jsonResult);
//            JSONArray data = jsonObjMain.getJSONArray(resultJson);
//
//            for (int i1 = 0; i1 < data.length(); i1++) {
//                JSONObject c = data.getJSONObject(i1);
//                try {
//                    lat = c.getDouble("Lastitude");
//                    lng = c.getDouble("Longitude");
//                    locationId = c.getString("Id");
//                    locationName = c.getString("OwnerName");
//                    locationTitle = c.getString("OwnerName");
//
//                    mMarker = mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(lat, lng))
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                            .title(locationName)
//                            .snippet(locationId));
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//            mMarker.showInfoWindow();
//
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }
    @Override
    public void onResume() {
        super.onResume();

// เชคว่า มี googleplay service อยู่หรือไม่
        final int RQS_GooglePlayServices = 1;
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS) {
        /*Toast.makeText(getApplicationContext(),
                "GooglePlayServices is Available",
                Toast.LENGTH_LONG).show();*/
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }

    } // end onResume


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
