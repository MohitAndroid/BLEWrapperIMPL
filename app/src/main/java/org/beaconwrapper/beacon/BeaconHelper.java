package org.beaconwrapper.beacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>This class is responsible for start beacon updates along with overloaded parameters.</p>
 * <p>Two ways for start beacon updates.</p>
 * <p>
 * <pre>
 *     1st way :
 *              Pass List of model classes in which you want to filter data according to beacon
 *              along with time interval and callbacks
 *     2nd way :
 *              Get only beacon list along with time interval and callbacks
 *     </pre>
 * </p>
 *
 * @param <T> the type parameter represent Generic Model class which is actually pass in
 *            startBeaconUpdate method of this class.
 */
public class BeaconHelper<T> {
    private BluetoothAdapter mBluetoothAdapter;
    private Activity context;
    private BluetoothManager bluetoothManager;
    private List<BeaconResultEntity> beaconResultEntities;
    private BeaconKeySerializer beaconKeySerializer;
    private List<T> data;
    private List<T> filteredData;
    private Map<String, List<T>> dataMap = new HashMap<>();
    private List<DifferentBeaconEntity> oldKeys;
    private List<DifferentBeaconEntity> newKeys;
    private BeaconResultListener beaconResultListener;
    private BeaconListener beaconListener;
    private Timer timer;
    private boolean isOnlyBeaconStuff;
    private List<IBeacon> iBeacons;

    /**
     * Provide BeaconHelper instance.
     *
     * @param activityContext defines context
     * @return the BeaconHelper instance
     */
    public static BeaconHelper getBeaconHelper(Activity activityContext) {
        return new BeaconHelper(activityContext);
    }

    private BeaconHelper(Activity context) {
        this.context = context;
        beaconKeySerializer = new BeaconKeySerializer();
        beaconResultEntities = new ArrayList<>();
        bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        filteredData = new ArrayList<>();
        oldKeys = new ArrayList<>();
        newKeys = new ArrayList<>();
        iBeacons = new ArrayList<>();
        timer = new Timer();
        isOnlyBeaconStuff = false;
    }

    /**
     * Start beacon updates and provide filter list of data which is passed as parameter in
     * this method via callbacks along with specified time frequency.
     *
     * @param data                 the list of data along with generic model class which is
     *                             to be filtered based on beacon frequency
     * @param timeInterval         the time interval represent in how much time beacon updates
     *                             will broadcast updates
     * @param bleAdapterEntity     keep tracking records for stop beacon updates later on time
     * @param beaconResultListener represent callback for beacon update at specified time if there
     *                             is any difference available otherwise no need to publish just
     *                             keep previous data remains as it is.
     */
    public void startBeaconUpdates(List<T> data, long timeInterval,
                                   BleAdapterEntity bleAdapterEntity,
                                   BeaconResultListener beaconResultListener) {
        this.isOnlyBeaconStuff = false;
        this.beaconResultListener = beaconResultListener;
        this.data = data;
        try {
            String validationErrorMsg = checkValidation();
            if (!TextUtils.isEmpty(validationErrorMsg)) {
                displayError(validationErrorMsg);
                return;
            }
            if (!isStringTypeData()) {
                setMapFromList();
            }
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            bleAdapterEntity.setBluetoothAdapter(mBluetoothAdapter);
            bleAdapterEntity.setLeScanCallback(mLeScanCallback);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    context.runOnUiThread(() -> {
                        if (beaconResultEntities != null
                                && !beaconResultEntities.isEmpty() &&
                                isDifferent(oldKeys, newKeys)) {
                            setOldKeys();
                            BeaconHelper.this.beaconResultListener.onResult(beaconResultEntities);
                        }
                    });
                }
            }, 0, timeInterval);
        } catch (BeaconKeySerializeException e) {
            displayError(e.getMessage());
        }
    }

    /**
     * Start beacon updates and provides only beacons with specified time frequency.
     *
     * @param timeInterval     the time interval represent in how much time beacon updates
     *                         will broadcast updates
     * @param bleAdapterEntity keep tracking records for stop beacon updates later on time
     * @param beaconListener   represent callback for beacon update
     */
    public void startBeaconUpdates(long timeInterval, BleAdapterEntity bleAdapterEntity,
                                   BeaconListener beaconListener) {
        this.isOnlyBeaconStuff = true;
        this.beaconListener = beaconListener;
        try {
            String validationErrorMsg = checkValidation();
            if (!TextUtils.isEmpty(validationErrorMsg)) {
                displayBeaconError(validationErrorMsg);
                return;
            }
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            bleAdapterEntity.setBluetoothAdapter(mBluetoothAdapter);
            bleAdapterEntity.setLeScanCallback(mLeScanCallback);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (iBeacons != null
                            && !iBeacons.isEmpty()) {
                        BeaconHelper.this.beaconListener.onResult(iBeacons);
                    }
                }
            }, 0, timeInterval);
        } catch (Exception e) {
            displayBeaconError(e.getMessage());
        }
    }

    private String checkValidation() {
        String errorMsg = "";
        if (mBluetoothAdapter == null) {
            errorMsg = "Bluetooth not supported.";
        } else if (!mBluetoothAdapter.isEnabled()) {
            errorMsg = "Bluetooth not enabled.";
        }
        return errorMsg;
    }

    private void setMapFromList() throws BeaconKeySerializeException {
        try {
            for (int i = 0; i < data.size(); i++) {
                FieldTypeEntity beaconAnnotationDetails = beaconKeySerializer.
                        getBeaconFieldTypeEntity(data.get(i));
                for (int j = 0; j < beaconAnnotationDetails.getFieldValue().size(); j++) {
                    if (dataMap.containsKey(beaconAnnotationDetails.getFieldValue().get(j))) {
                        dataMap.get(beaconAnnotationDetails.getFieldValue().get(j)).
                                add(data.get(i));
                    } else {
                        List<T> entities = new ArrayList<>();
                        entities.add(data.get(i));
                        dataMap.put(beaconAnnotationDetails.getFieldValue().get(j), entities);
                    }
                }
            }
        } catch (Exception e) {
            throw new BeaconKeySerializeException(e.getMessage());
        }

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    IBeacon iBeacon = IBeacon.fromScanData(scanRecord, rssi, device);
                    if (iBeacon != null) {
                        if (isOnlyBeaconStuff) {
                            getOnlyBeaconData(iBeacon);
                        } else {
                            getBeaconFilteredData(iBeacon);
                        }
                    }
                }

                private void getOnlyBeaconData(IBeacon iBeacon) {
                    try {
                        boolean isAdd = true;
                        for (int i = 0; i < iBeacons.size(); i++) {
                            if (iBeacons.get(i).getBluetoothAddress().equals(iBeacon.
                                    getBluetoothAddress())) {
                                iBeacons.get(i).setAccuracy(iBeacon.getAccuracy());
                                iBeacons.get(i).setBleDataPayload(iBeacon.getBleDataPayload());
                                iBeacons.get(i).setProximityUuid(iBeacon.getProximityUuid());
                                iBeacons.get(i).setRssi(iBeacon.getRssi());
                                iBeacons.get(i).setTimezoneString(iBeacon.getTimezoneString());
                                isAdd = false;
                                break;
                            }
                        }
                        if (isAdd) {
                            iBeacons.add(iBeacon);
                        }
                    } catch (Exception e) {
                        displayBeaconError(e.getMessage());
                    }
                }

                private void getBeaconFilteredData(IBeacon iBeacon) {
                    try {
                        if (data == null) {
                            throw new BeaconKeySerializeException("List can not be null");
                        }
                        if (!dataMap.containsKey(iBeacon.getBleDataPayload())) {
                            throw new BeaconKeySerializeException("Key(Payload) not found in data");
                        }
                        BeaconResultEntity entity = isPresentInBeaconPagerEntities(iBeacon.
                                getBluetoothAddress());
                        if (entity == null) {
                            BeaconResultEntity beaconResultEntity = new BeaconResultEntity();
                            beaconResultEntity.setBeaconDetail(iBeacon);
                            beaconResultEntity.setKey(iBeacon.getBleDataPayload());
                            filterBeaconData(iBeacon);
                            beaconResultEntity.setResult(filteredData);
                            beaconResultEntities.add(beaconResultEntity);
                        } else {
                            entity.setBeaconDetail(iBeacon);
                            entity.setKey(iBeacon.getBleDataPayload());
                            filterBeaconData(iBeacon);
                            entity.setResult(filteredData);
                        }
                        sortBeaconData();
                        setNewKeys();
                    } catch (Exception e) {
                        displayError(e.getMessage());
                    }
                }

                private void setNewKeys() {
                    newKeys.clear();
                    for (int i = 0; i < beaconResultEntities.size(); i++) {
                        String bluetoothAddress = beaconResultEntities.get(i).getBeaconDetail().
                                getBluetoothAddress();
                        String name = beaconResultEntities.get(i).getBeaconDetail().
                                getBleDataPayload();
                        newKeys.add(new DifferentBeaconEntity(bluetoothAddress, name));
                    }
                }

                private BeaconResultEntity isPresentInBeaconPagerEntities(String macAddress)
                        throws BeaconKeySerializeException {
                    try {
                        for (int i = 0; i < beaconResultEntities.size(); i++) {
                            if (beaconResultEntities.get(i).getBeaconDetail().
                                    getBluetoothAddress().equals(macAddress)) {
                                return beaconResultEntities.get(i);
                            }
                        }
                        return null;
                    } catch (Exception e) {
                        throw new BeaconKeySerializeException(e.getMessage());
                    }
                }

                private void sortBeaconData() {
                    Collections.sort(beaconResultEntities,
                            (o1, o2) -> Double.compare(o1.getBeaconDetail().getAccuracy(),
                                    o2.getBeaconDetail().getAccuracy()));
                }

                private void filterBeaconData(IBeacon iBeacon) {
                    if (isStringTypeData()) {
                        filterBeaconListData(iBeacon);
                    } else {
                        filterBeaconMapData(iBeacon);
                    }
                }

                private void filterBeaconListData(IBeacon iBeacon) {
                    filteredData = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).equals(iBeacon.getBleDataPayload())) {
                            filteredData.add(data.get(i));
                        }
                    }
                }

                private void filterBeaconMapData(IBeacon iBeacon) {
                    filteredData = new ArrayList<>();
                    filteredData.addAll(dataMap.get(iBeacon.getBleDataPayload()));
                }
            };

    private boolean isStringTypeData() {
        boolean isStringType = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof String) {
                isStringType = true;
                break;
            }
        }
        return isStringType;
    }

    private void displayError(String msg) {
        this.beaconResultListener.onError(msg);
    }

    private void displayBeaconError(String msg) {
        this.beaconListener.onError(msg);
    }

    private void setOldKeys() {
        oldKeys.clear();
        for (int i = 0; i < newKeys.size(); i++) {
            oldKeys.add(newKeys.get(i));
        }
    }

    private boolean isDifferent(List<DifferentBeaconEntity> oldKeys,
                                List<DifferentBeaconEntity> newKeys) {
        boolean isDifferent = false;
        if (oldKeys.size() == newKeys.size()) {
            for (int i = 0; i < oldKeys.size(); i++) {
                if (!(oldKeys.get(i).getBeaconKey().equals(newKeys.get(i).getBeaconKey())) ||
                        !(oldKeys.get(i).getBeaconName().equals(newKeys.get(i).getBeaconName()))) {
                    isDifferent = true;
                    break;
                }
            }
        } else {
            isDifferent = true;
        }
        return isDifferent;
    }
}
