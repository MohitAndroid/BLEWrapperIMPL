package org.beaconwrapper.beacon;

import java.util.List;

public interface BeaconListener {
    void onResult(List<IBeacon> beaconResultEntities);

    void onError(String errorMsg);
}
