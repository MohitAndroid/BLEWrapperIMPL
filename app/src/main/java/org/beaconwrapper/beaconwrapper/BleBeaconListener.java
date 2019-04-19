package org.beaconwrapper.beaconwrapper;

import org.beaconwrapper.beacon.BeaconResultEntity;

import java.util.List;

/**
 * The interface Ble beacon listener is a wrapper callback.
 *
 * @param <T> the type parameter is generic class provided by user to be filtered later on
 */
public interface BleBeaconListener<T> {

    /**
     * Provides list of type T data as a result.
     *
     * @param beaconResultEntities the beacon result entities
     */
    void onBeaconDataResult(List<BeaconResultEntity> beaconResultEntities);

    /**
     * On error.
     *
     * @param errorMsg the error msg
     */
    void onError(String errorMsg);

    /**
     * Gives callback to manage UI operation before network call
     */
    void onShowProgress();

    /**
     * Gives parsable data list.
     *
     * @param parsableData the parsable data list
     */
    void onParsableDataResult(List<T> parsableData);


}
