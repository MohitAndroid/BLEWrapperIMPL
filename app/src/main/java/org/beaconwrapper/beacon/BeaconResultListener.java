package org.beaconwrapper.beacon;

import java.util.List;

/**
 * The interface Beacon result listener is a callback for beacon operations.
 */
public interface BeaconResultListener {

    /**
     * On result which provides updated beacon filter data.
     *
     * @param beaconResultEntities the beacon result entities
     */
    void onResult(List<BeaconResultEntity> beaconResultEntities);

    /**
     * On error.
     *
     * @param errorMsg the error msg
     */
    void onError(String errorMsg);
}
