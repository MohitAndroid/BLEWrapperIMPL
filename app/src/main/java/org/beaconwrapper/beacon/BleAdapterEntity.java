package org.beaconwrapper.beacon;

import android.bluetooth.BluetoothAdapter;

public class BleAdapterEntity {

    private int listenerCode;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter.LeScanCallback leScanCallback;

    public BleAdapterEntity() {
    }

    public BleAdapterEntity(int listenerCode) {
        this.listenerCode = listenerCode;
    }

    public BleAdapterEntity(int listenerCode, BluetoothAdapter bluetoothAdapter, BluetoothAdapter.LeScanCallback leScanCallback) {
        this.listenerCode = listenerCode;
        this.bluetoothAdapter = bluetoothAdapter;
        this.leScanCallback = leScanCallback;
    }

    public int getListenerCode() {
        return listenerCode;
    }

    public void setListenerCode(int listenerCode) {
        this.listenerCode = listenerCode;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public BluetoothAdapter.LeScanCallback getLeScanCallback() {
        return leScanCallback;
    }

    public void setLeScanCallback(BluetoothAdapter.LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
    }
}
