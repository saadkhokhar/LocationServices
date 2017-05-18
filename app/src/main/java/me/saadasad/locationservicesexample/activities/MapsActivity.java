package me.saadasad.locationservicesexample.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import me.saadasad.locationservicesexample.LocationCallback;
import me.saadasad.locationservicesexample.LocationProvider;
import me.saadasad.locationservicesexample.R;
import me.saadasad.locationservicesexample.utils.AlertDialog;

/**
 * Created by Saad on 5/16/2017.
 */

public class MapsActivity extends FragmentActivity implements LocationCallback, OnMapReadyCallback {
    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

    private LocationProvider mLocationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /**
         * Check if app has required permissions, if not create a dialog to ask user for permission grant otherwise setupMap*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.CreateAlertDialog(this, "", getResources().getString(R.string.permission_error_message), getString(R.string.exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MapsActivity.this.finish();
                }
            }, getString(R.string.request_permission), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestLocationPermission();
                }
            });
        } else {
            setupMap();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationProvider != null)
            mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationProvider != null)
            mLocationProvider.disconnect();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    private void setupMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
            mLocationProvider = new LocationProvider(this, this);
        }
    }

    /**
     * onMapReady will be called when Map is ready to be used, here we will setup map parameters
     */
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions().position(latLng).title(currentLatitude + " : " + currentLongitude);
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                /**
                 * If request is cancelled, the result arrays are empty.
                 * */
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * permission was granted, setupMap
                     * */
                    setupMap();
                } else {
                    /**
                     * Permission was Denied, notify user and request again or Exit app.
                     * */
                    AlertDialog.CreateAlertDialog(this, "", getResources().getString(R.string.permission_error_message), getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MapsActivity.this.finish();
                        }
                    }, getString(R.string.request_permission), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestLocationPermission();
                        }
                    });
                }
                return;
            }
        }
    }
}
