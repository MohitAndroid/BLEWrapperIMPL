package org.beaconwrapper.beacon;

import java.util.List;

/**
 * The interface Beacon result listener is a callback for beacon operations.
 */
public interface BeaconListener {
    /**
     * On result provides beacon details as a list.
     *
     * @param beaconResultEntities the beacon result entities
     */
    void onResult(List<IBeacon> beaconResultEntities);

    /**
     * On error.
     *
     * @param errorMsg the error msg
     */
    void onError(String errorMsg);
}
