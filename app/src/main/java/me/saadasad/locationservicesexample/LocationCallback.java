package me.saadasad.locationservicesexample;

import android.location.Location;

/**
 * Created by Saad on 5/16/2017.
 */

public interface LocationCallback {
    public void handleNewLocation(Location location);
}
