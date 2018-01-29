package com.lipu.findnearbyplacesapp.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by mdmunirhossain on 5/19/17.
 */

public class RequestUserPermission {
    private Activity activity;
    // Storage Permissions
    public static final int REQUEST_PERMISSION = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    public RequestUserPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean verifyStoragePermissions() {
        // Check if we have write permission
        int permission_fine_location = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_coarse_location = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission_fine_location != PackageManager.PERMISSION_GRANTED || permission_coarse_location != PackageManager.PERMISSION_GRANTED) {
            // no  permission,so ask permission
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }
}
