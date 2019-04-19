package org.beaconwrapper.beacon;

import android.bluetooth.BluetoothAdapter;

/**
 * This class helps to store {@link android.bluetooth.BluetoothAdapter.LeScanCallback} callback
 * for stop beacon updates later on time.
 */
public class BleAdapterEntity {

    private int listenerCode;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter.LeScanCallback leScanCallback;

    /**
     * Instantiates a new Ble adapter entity.
     */
    public BleAdapterEntity() {}

    /**
     * Instantiates a new Ble adapter entity.
     *
     * @param listenerCode the listener code
     */
    public BleAdapterEntity(int listenerCode) {
        this.listenerCode = listenerCode;
    }

    /**
     * Instantiates a new Ble adapter entity.
     *
     * @param listenerCode     the listener code represent unique code for identify callback
     *                         regarding startBeaconUpdates in {@link BeaconHelper}
     * @param bluetoothAdapter the bluetooth adapter
     * @param leScanCallback   the callback associated with listenerCode
     */
    public BleAdapterEntity(int listenerCode, BluetoothAdapter bluetoothAdapter, BluetoothAdapter.LeScanCallback leScanCallback) {
        this.listenerCode = listenerCode;
        this.bluetoothAdapter = bluetoothAdapter;
        this.leScanCallback = leScanCallback;
    }

    /**
     * Gets listener code.
     *
     * @return the listener code
     */
    public int getListenerCode() {
        return listenerCode;
    }

    /**
     * Sets listener code.
     *
     * @param listenerCode the listener code
     */
    public void setListenerCode(int listenerCode) {
        this.listenerCode = listenerCode;
    }

    /**
     * Gets bluetooth adapter.
     *
     * @return the bluetooth adapter
     */
    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    /**
     * Sets bluetooth adapter.
     *
     * @param bluetoothAdapter the bluetooth adapter
     */
    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    /**
     * Gets le scan callback.
     *
     * @return the le scan callback
     */
    public BluetoothAdapter.LeScanCallback getLeScanCallback() {
        return leScanCallback;
    }

    /**
     * Sets le scan callback.
     *
     * @param leScanCallback the le scan callback
     */
    public void setLeScanCallback(BluetoothAdapter.LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
    }
}
