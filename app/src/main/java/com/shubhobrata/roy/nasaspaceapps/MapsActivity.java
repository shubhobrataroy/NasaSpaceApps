package com.shubhobrata.roy.nasaspaceapps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

//import com.google.android.gms.maps.MapView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    DBHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database=new DBHelper(MapsActivity.this,"placeCordinate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng latLng = new LatLng(lat, lon);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    //mMap.addMarker(new MarkerOptions().position(latLng).title());
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    try {
                        List<Address>addressList=geocoder.getFromLocation(lat,lon,1);
                        String str=addressList.get(0).getSubLocality()+",";
                        str+=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Posssible location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng latLng = new LatLng(lat, lon);

                   // mMap.addMarker(new MarkerOptions().position(latLng).title("my location"));
                   // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address>addressList=geocoder.getFromLocation(lat,lon,1);
                        String str=addressList.get(0).getLocality()+",";
                        str+=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,30.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

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
        //overLayMap m=new overLayMap();
       // m.onTouchEvent()
       // database.onCreate();

        Cursor data= database.getAllData();
        data.moveToFirst();
        double la;
        double lo;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.person_drawable);
        while(data.isAfterLast()==false){
            la=Double.parseDouble(data.getString(data.getColumnIndex("lat")));
            lo=Double.parseDouble(data.getString(data.getColumnIndex("long")));
            mMap.addMarker(new MarkerOptions().position(new LatLng(la,lo))
                    .title("Submitted by user")
                    .icon(icon)

            );
            data.moveToNext();
        }
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                double lat=latLng.latitude;
                double lon=latLng.longitude;
                Toast.makeText(MapsActivity.this,lat+","+lon,Toast.LENGTH_SHORT).show();
                database.Insert(lat,lon);
               Cursor data= database.getAllData();
                data.moveToFirst();
                double la;
                double lo;
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.person_drawable);
                while(data.isAfterLast()==false){
                     la=Double.parseDouble(data.getString(data.getColumnIndex("lat")));
                     lo=Double.parseDouble(data.getString(data.getColumnIndex("long")));
                     mMap.addMarker(new MarkerOptions().position(new LatLng(la,lo))
                                                        .title("Submitted by user")
                                                        .icon(icon)
                     );
                    data.moveToNext();
                }

            }
        });
    }
}
