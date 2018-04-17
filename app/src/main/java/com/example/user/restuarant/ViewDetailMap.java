package com.example.user.restuarant;import android.content.Intent;import android.os.Bundle;import android.os.StrictMode;import android.support.v7.app.AppCompatActivity;import android.widget.ImageView;import android.widget.TextView;import com.google.android.gms.common.ConnectionResult;import com.google.android.gms.common.GooglePlayServicesUtil;import com.google.android.gms.maps.CameraUpdateFactory;import com.google.android.gms.maps.GoogleMap;import com.google.android.gms.maps.MapView;import com.google.android.gms.maps.model.BitmapDescriptorFactory;import com.google.android.gms.maps.model.LatLng;import com.google.android.gms.maps.model.Marker;import com.google.android.gms.maps.model.MarkerOptions;import com.squareup.picasso.Picasso;public class ViewDetailMap extends AppCompatActivity{    String Name,Address,ImageURL,Latitude,Longitude,TellNumber,path_imgae;    TextView detail_textView,name_textView;    ImageView pic_imageView;    private Marker mMarker;    MapView mMapView;    private GoogleMap mMap;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_view_detail_map);        if (android.os.Build.VERSION.SDK_INT > 9) {            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()                    .permitAll().build();            StrictMode.setThreadPolicy(policy);        }        path_imgae = getResources().getString(R.string.path_imgae);        detail_textView=(TextView)findViewById(R.id.detail_textView);        name_textView=(TextView)findViewById(R.id.name_textView);        pic_imageView=(ImageView)findViewById(R.id.pic_imageView);        mMapView = (MapView)findViewById(R.id.mapView);        mMapView.onCreate(savedInstanceState);        mMapView.onResume();// needed to get the map to display immediately        // Do a null check to confirm that we have not already instantiated the map.        if (mMap == null) {            // Try to obtain the map from the SupportMapFragment.            //mMap = ((SupportMapFragment) myContext.getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap();            mMap = mMapView.getMap();            // Check if we were successful in obtaining the map.        }        Intent intent = getIntent();        Name = intent.getStringExtra("Name");//รับค่าจากหน้าที่ส่งมาให้        Address = intent.getStringExtra("Address");        ImageURL = intent.getStringExtra("ImageURL");        Latitude = intent.getStringExtra("Latitude");        Longitude = intent.getStringExtra("Longitude");        TellNumber = intent.getStringExtra("TellNumber");        setTitle(Name);        String link = path_imgae+ImageURL;        name_textView.setText(Name);        detail_textView.setText(Address);        Picasso.with(getApplication())                .load(link)                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น                .error(R.drawable.logo_bunchooo)//                            .resize(imgWidth, imgHeight)//ปรับขนาด//                            .centerCrop()//ตัดภาพ                .into(pic_imageView);//เอารูปตัวไหน        setmap();    }    private void setmap(){        mMap.clear();        double Lat = Double.parseDouble(Latitude);        double Long = Double.parseDouble(Longitude);        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Long), 18));// zoom map        mMarker = mMap.addMarker(new MarkerOptions()                .position(new LatLng(Lat, Long))                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))                .title(Name)                .snippet(TellNumber));    }    @Override    public void onResume() {        super.onResume();// เชคว่า มี googleplay service อยู่หรือไม่        final int RQS_GooglePlayServices = 1;        int resultCode =                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication().getApplicationContext());        if (resultCode == ConnectionResult.SUCCESS) {        /*Toast.makeText(getApplicationContext(),                "GooglePlayServices is Available",                Toast.LENGTH_LONG).show();*/        } else {            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);        }    } // end onResume    @Override    public void onPause() {        super.onPause();        mMapView.onPause();    }    @Override    public void onDestroy() {        super.onDestroy();        mMapView.onDestroy();    }    @Override    public void onLowMemory() {        super.onLowMemory();        mMapView.onLowMemory();    }}