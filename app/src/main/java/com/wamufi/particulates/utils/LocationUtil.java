package com.wamufi.particulates.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationUtil implements LocationListener {
    private Context mContext;

    private String mBestProvider;
    private LocationManager mLocationManager;
    private Location mLocation;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 100 meters

    public LocationUtil(Context context) {
        mContext = context;
        getLocation();
    }

    public Location getLocation() throws SecurityException {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        mBestProvider = mLocationManager.getBestProvider(getCriteria(), true);
        if (mBestProvider == null) {
            requestLocationPermission();
        }
        Log.v("LocationUtil", "mBestProvider: "+mBestProvider);
        mLocationManager.requestLocationUpdates(mBestProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        mLocation = mLocationManager.getLastKnownLocation(mBestProvider);

//        Log.v("LocationUtil", "mBestProvider: "+mBestProvider + " mLocation: "+mLocation);

//        Log.v("geoLocation", "isGpsEnabled: "+isGpsEnabled+" isNetworkEnabled: "+isNetworkEnabled);

//        if (isNetworkEnabled && !isGpsEnabled) {
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Log.v("geoLocation", "isNetworkEnabled");
//        } else if (isGpsEnabled) {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Log.v("geoLocation", "isGpsEnabled");
//        }
//
//        if (mLocation != null) {
//            double latitude = mLocation.getLatitude();
//            double longitude = mLocation.getLongitude();
//        }

        return mLocation;
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        return criteria;
    }

    private void requestLocationPermission() {
        Log.i("LocationUtil", "LOCATION permission has NOT granted. Requesting permission.");

//        if (ActivityCompat.shouldShowRequestPermissionRationale(, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("LocationUtil", "onLocationChanged: "+location);
//        if (location != null) {
//            mLocationManager.removeUpdates(this);
//        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.v("LocationUtil", "onStatusChanged: "+s+" "+i+" "+bundle);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.v("LocationUtil", "onProviderEnabled: "+s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.v("LocationUtil", "onProviderDisabled: "+s);
    }

    public void resumeLocationUtil() throws SecurityException {
        if (mLocationManager != null)
            mLocationManager.requestLocationUpdates(mBestProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    public void stopLocationUtil() throws SecurityException {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(LocationUtil.this);
        }
    }
}
