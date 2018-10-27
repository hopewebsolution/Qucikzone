package com.quikzon.add.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.quikzon.add.utility.Utility;

public class MyLocationService extends Service {
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 500;
    private static final float LOCATION_DISTANCE = 10f;
    public  Location mLastLocation;
    public double lat=0.0,lng=0.0;

    private class LocationListener implements android.location.LocationListener {

        LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            lat=mLastLocation.getLatitude();
            lng=mLastLocation.getLongitude();
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            Log.d("my_lat",mLastLocation.getLatitude()+"/"+mLastLocation.getLongitude());
            lat=location.getLatitude();
            lng=location.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            Utility.displayLocationSettingsRequest(getBaseContext());

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }*/
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
       stopSelf();
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
