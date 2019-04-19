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

/**
 * <p>This class defines wrapper of beacon operation to gives more user abstraction level.</p>
 * <p>This class is design for getting beacon data in many ways like</p>
 * <p>
 * <pre>
 *         1st : getBeaconData along with URL and provides final output as list of filter
 *         data in callbacks. i.e filtration is done via beacon payload
 *
 *         2nd : getBeaconData along with LIST passes through and provides final output as filter
 *         data in callbacks. i.e filtration is done via beacon payload
 *
 *         3rd : getBeaconData along with time interval and provides final output as beacon details
 *         in callbacks.
 *     </pre>
 * </p>
 *
 * @param <T> the type parameter is generic class provided by user to be filtered later on
 */
public class BLEBeaconWrapper<T> {

    private Activity context;
    private List<BleAdapterEntity> leScanCallbacks;
    private static final String LOG1 = "TTMMPP";
    private static final String LOG2 = "BLE-RESPONSE";

    /**
     * Instantiates a new Ble beacon wrapper.
     *
     * @param context the context
     */
    public BLEBeaconWrapper(Activity context) {
        this.context = context;
        leScanCallbacks = new ArrayList<>();

    }

    /**
     * Gets beacon data passed with params details and provide result via callbacks
     * {@link BleBeaconListener}.
     *
     * @param url                the url to be processed to get response
     * @param t                  the t is generic class which is to be filtered later on
     * @param headerData         the header data which helps to fire get/post request
     * @param timeInterval       the time interval represent in how much time beacon updates
     *                           will broadcast updates
     * @param tBleBeaconListener represent callback for beacon update at specified time if there
     *                           is any difference available otherwise no need to publish just
     *                           keep previous data remains as it is.
     */
    public void getBeaconData(String url, Class<T> t, Map<String, String> headerData,
                              long timeInterval, final BleBeaconListener<T> tBleBeaconListener) {
        new Thread(() -> networkOperation(ParserListClass.getParserListClass(),
                BeaconHelper.getBeaconHelper(context),
                url, t, headerData, timeInterval, tBleBeaconListener)).start();

    }

    /**
     * Gets beacon data passed with params details and provide result via callbacks
     *
     * @param list               the list which is to be filtered based on beacon payload as key
     * @param timeInterval       the time interval represent in how much time beacon updates
     *                           will broadcast updates
     * @param tBleBeaconListener represent callback for beacon update at specified time if there
     *                           is any difference available otherwise no need to publish just
     *                           keep previous data remains as it is.
     */
    public void getBeaconData(List<T> list, long timeInterval,
                              final BleBeaconListener<T> tBleBeaconListener) {
        new Thread(() -> beaconWrapperOperation(BeaconHelper.getBeaconHelper(this.context),
                list, timeInterval, tBleBeaconListener)).start();
    }

    /**
     * Gets beacon data passed with params details and provide result via callbacks
     *
     * @param timeInterval    the time interval represent in how much time beacon updates
     *                        will broadcast updates
     * @param tBeaconListener represent callback for beacon update at specified time if there
     *                        is any difference available otherwise no need to publish just
     *                        keep previous data remains as it is.
     */
    public void getBeaconData(long timeInterval,
                              final BeaconListener tBeaconListener) {
        new Thread(() -> beaconOnlyWrapper(BeaconHelper.getBeaconHelper(this.context),
                timeInterval, tBeaconListener)).start();
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
                context.runOnUiThread(() -> tBleBeaconListener.onParsableDataResult(filteredData));
                beaconWrapperOperation(beaconHelper, filteredData, timeInterval, tBleBeaconListener);
            }

            @Override
            public void onError(String errorMsg) {
                tBleBeaconListener.onError(errorMsg);
            }
        });
    }

    /**
     * Stop all beacon updates.
     */
    public void stopAllBeaconUpdates() {
        for (int i = 0; i < leScanCallbacks.size(); i++) {
            if (leScanCallbacks.get(i).getBluetoothAdapter() != null &&
                    leScanCallbacks.get(i).getLeScanCallback() != null) {
                leScanCallbacks.get(i).getBluetoothAdapter().
                        stopLeScan(leScanCallbacks.get(i).getLeScanCallback());
                Log.d(LOG2, "Deleted : " + leScanCallbacks.get(i).getListenerCode());
            }
        }

    }

    /**
     * Stop beacon updates.
     *
     * @param bleBeaconListener the ble beacon listener
     */
    public void stopBeaconUpdates(BleBeaconListener bleBeaconListener) {
        Log.d(LOG1, "*Code : " + bleBeaconListener.hashCode());
        for (int i = 0; i < leScanCallbacks.size(); i++) {
            Log.d(LOG1, "Code : " + leScanCallbacks.get(i).getListenerCode());
            if (leScanCallbacks.get(i).getBluetoothAdapter() != null &&
                    leScanCallbacks.get(i).getLeScanCallback() != null &&
                    bleBeaconListener.hashCode() == leScanCallbacks.get(i).getListenerCode()) {
                leScanCallbacks.get(i).getBluetoothAdapter().
                        stopLeScan(leScanCallbacks.get(i).getLeScanCallback());
                Log.d(LOG2, "Deleted : " + leScanCallbacks.get(i).getListenerCode());
                break;
            }
        }
    }

    /**
     * Stop beacon updates.
     *
     * @param beaconListener the beacon listener
     */
    public void stopBeaconUpdates(BeaconListener beaconListener) {
        Log.d(LOG1, "*Code : " + beaconListener.hashCode());
        for (int i = 0; i < leScanCallbacks.size(); i++) {
            Log.d(LOG1, "Code : " + leScanCallbacks.get(i).getListenerCode());
            if (leScanCallbacks.get(i).getBluetoothAdapter() != null &&
                    leScanCallbacks.get(i).getLeScanCallback() != null &&
                    beaconListener.hashCode() == leScanCallbacks.get(i).getListenerCode()) {
                leScanCallbacks.get(i).getBluetoothAdapter().
                        stopLeScan(leScanCallbacks.get(i).getLeScanCallback());
                Log.d(LOG2, "[Deleted] : " + leScanCallbacks.get(i).getListenerCode());
                break;
            }
        }
    }

    private void beaconWrapperOperation(BeaconHelper beaconHelper, List<T> filteredData,
                                        long timeInterval,
                                        final BleBeaconListener<T> tBleBeaconListener) {

        BleAdapterEntity bleAdapterEntity = new BleAdapterEntity(tBleBeaconListener.hashCode());
        leScanCallbacks.add(bleAdapterEntity);
        beaconHelper.startBeaconUpdates(filteredData, timeInterval, bleAdapterEntity,
                new BeaconResultListener() {
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
        beaconHelper.startBeaconUpdates(timeInterval, bleAdapterEntity, new BeaconListener() {
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
