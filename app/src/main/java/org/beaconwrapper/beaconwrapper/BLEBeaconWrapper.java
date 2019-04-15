package org.beaconwrapper.beaconwrapper;

import android.app.Activity;
import android.util.Log;


import org.beaconwrapper.beacon.BeaconHelper;
import org.beaconwrapper.beacon.BeaconListener;
import org.beaconwrapper.beacon.BeaconResultEntity;
import org.beaconwrapper.beacon.BeaconResultListener;
import org.beaconwrapper.beacon.BleAdapterEntity;
import org.beaconwrapper.beacon.IBeacon;
import org.beaconwrapper.network.NetworkManager;
import org.beaconwrapper.network.RequestCallBackListener;
import org.beaconwrapper.parse.FilterListener;
import org.beaconwrapper.parse.ParserListClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BLEBeaconWrapper<T> {

    private Activity context;
    private List<BleAdapterEntity> leScanCallbacks;

    public BLEBeaconWrapper(Activity context) {
        this.context = context;
        leScanCallbacks = new ArrayList<>();

    }

    public void getBeaconData(String url, Class<T> t, Map<String, String> headerData,
                              long timeInterval, final BleBeaconListener<T> tBleBeaconListener) {
        networkOperation(ParserListClass.getParserListClass(),
                BeaconHelper.getBeaconHelper(this.context),
                url, t, headerData, timeInterval, tBleBeaconListener);
    }

    public void getBeaconData(List<T> list, long timeInterval,
                              final BleBeaconListener<T> tBleBeaconListener) {
        beaconWrapperOperation(BeaconHelper.getBeaconHelper(this.context),
                list, timeInterval, tBleBeaconListener);
    }

    public void getBeaconData(long timeInterval,
                              final BeaconListener tBeaconListener) {
        beaconOnlyWrapper(BeaconHelper.getBeaconHelper(this.context),
                timeInterval, tBeaconListener);
    }

    private void networkOperation(ParserListClass parserListClass, BeaconHelper beaconHelper,
                                  String url, Class<T> t, Map<String, String> headerData,
                                  long timeInterval, final BleBeaconListener<T> tBleBeaconListener) {
        NetworkManager.getRequest(url,
                headerData, new RequestCallBackListener() {
                    @Override
                    public void beforeCallBack() {
                        tBleBeaconListener.onShowProgress();
                    }

                    @Override
                    public void onResponse(String responseString) {
                        parseClassFields(parserListClass, beaconHelper, responseString,
                                t, timeInterval, tBleBeaconListener);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        tBleBeaconListener.onError(errorMsg);
                    }
                });
    }

    private void parseClassFields(ParserListClass parserListClass, BeaconHelper beaconHelper,
                                  String responseString, Class<T> t,
                                  long timeInterval, final BleBeaconListener<T> tBleBeaconListener) {

        parserListClass.parseData(t, responseString, new FilterListener<T>() {
            @Override
            public void onResponse(List<T> filteredData) {
                tBleBeaconListener.onParsableDataResult(filteredData);
                beaconWrapperOperation(beaconHelper, filteredData, timeInterval, tBleBeaconListener);
            }

            @Override
            public void onError(String errorMsg) {
                tBleBeaconListener.onError(errorMsg);
            }
        });
    }

    public void stopAllBeaconUpdates() {
        for (int i = 0; i < leScanCallbacks.size(); i++) {
            if (leScanCallbacks.get(i).getBluetoothAdapter() != null &&
                    leScanCallbacks.get(i).getLeScanCallback() != null) {
                leScanCallbacks.get(i).getBluetoothAdapter().
                        stopLeScan(leScanCallbacks.get(i).getLeScanCallback());
                Log.d("BLE-RESPONSE", "Deleted : " + leScanCallbacks.get(i).getListenerCode());
            }
        }

    }

    public void stopBeaconUpdates(BleBeaconListener bleBeaconListener) {
        Log.d("TTMMPP", "*Code : " + bleBeaconListener.hashCode());
        for (int i = 0; i < leScanCallbacks.size(); i++) {
            Log.d("TTMMPP", "Code : " + leScanCallbacks.get(i).getListenerCode());
            if (leScanCallbacks.get(i).getBluetoothAdapter() != null &&
                    leScanCallbacks.get(i).getLeScanCallback() != null &&
                    bleBeaconListener.hashCode() == leScanCallbacks.get(i).getListenerCode()) {
                leScanCallbacks.get(i).getBluetoothAdapter().
                        stopLeScan(leScanCallbacks.get(i).getLeScanCallback());
                Log.d("BLE-RESPONSE", "Deleted : " + leScanCallbacks.get(i).getListenerCode());
                break;
            }
        }
    }

    public void stopBeaconUpdates(BeaconListener beaconListener) {
        Log.d("TTMMPP", "*Code : " + beaconListener.hashCode());
        for (int i = 0; i < leScanCallbacks.size(); i++) {
            Log.d("TTMMPP", "Code : " + leScanCallbacks.get(i).getListenerCode());
            if (leScanCallbacks.get(i).getBluetoothAdapter() != null &&
                    leScanCallbacks.get(i).getLeScanCallback() != null &&
                    beaconListener.hashCode() == leScanCallbacks.get(i).getListenerCode()) {
                leScanCallbacks.get(i).getBluetoothAdapter().
                        stopLeScan(leScanCallbacks.get(i).getLeScanCallback());
                Log.d("BLE-RESPONSE", "[Deleted] : " + leScanCallbacks.get(i).getListenerCode());
                break;
            }
        }
    }

    private void beaconWrapperOperation(BeaconHelper beaconHelper, List<T> filteredData,
                                        long timeInterval,
                                        final BleBeaconListener<T> tBleBeaconListener) {

        BleAdapterEntity bleAdapterEntity = new BleAdapterEntity(tBleBeaconListener.hashCode());
        leScanCallbacks.add(bleAdapterEntity);
        beaconHelper.startBeaconUpdates(filteredData, timeInterval, bleAdapterEntity, new BeaconResultListener() {
            @Override
            public void onResult(List<BeaconResultEntity> beaconResultEntities) {
                tBleBeaconListener.onBeaconDataResult(beaconResultEntities);
            }

            @Override
            public void onError(String errorMsg) {
                tBleBeaconListener.onError(errorMsg);
            }
        });
    }

    private void beaconOnlyWrapper(BeaconHelper beaconHelper, long timeInterval,
                                   final BeaconListener tBeaconListener) {
        BleAdapterEntity bleAdapterEntity = new BleAdapterEntity(tBeaconListener.hashCode());
        leScanCallbacks.add(bleAdapterEntity);
        beaconHelper.startBeaconUpdates(timeInterval, bleAdapterEntity,new BeaconListener() {
            @Override
            public void onResult(List<IBeacon> beaconResultEntities) {
                tBeaconListener.onResult(beaconResultEntities);
            }

            @Override
            public void onError(String errorMsg) {
                tBeaconListener.onError(errorMsg);
            }
        });
    }
}
