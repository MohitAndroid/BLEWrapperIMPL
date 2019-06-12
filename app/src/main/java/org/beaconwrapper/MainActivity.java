package org.beaconwrapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.beaconwrapper.beacon.BeaconListener;
import org.beaconwrapper.beacon.BeaconResultEntity;
import org.beaconwrapper.beacon.IBeacon;
import org.beaconwrapper.beaconwrapper.BLEBeaconWrapper;
import org.beaconwrapper.beaconwrapper.BleBeaconListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BeaconListener beaconListener = new BeaconListener() {
        @Override
        public void onResult(List<IBeacon> beaconResultEntities) {
            for (int i = 0; i < beaconResultEntities.size(); i++) {
                Log.d("BLE-RESPONSE", "Beacon : " + beaconResultEntities.get(i).
                        getBluetoothAddress());
            }
            Log.d("BLE-RESPONSE", "***********************************");
        }

        @Override
        public void onError(String errorMsg) {

        }
    };
    BleBeaconListener bleCouponListener = new BleBeaconListener<CouponEntity>() {
        @Override
        public void onBeaconDataResult(List<BeaconResultEntity> list) {

            Log.d("BLE-RESPONSE", "[Coupon] Total : " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Log.d("BLE-RESPONSE", "[Coupon] Inside : " + list.get(i).getBeaconDetail().getBluetoothAddress()
                        + " | Accuracy : " + list.get(i).getBeaconDetail().getAccuracy());
//                Toast.makeText(MainActivity.this,""+list.get(i).getBeaconDetail().getAccuracy(),Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onError(String errorMsg) {
            Log.d("BLE-RESPONSE", "[Coupon] " + errorMsg);
        }

        @Override
        public void onShowProgress() {
            Log.d("BLE-RESPONSE", "[Coupon] onShowProgress");
        }

        @Override
        public void onParsableDataResult(List<CouponEntity> parsableData) {
            Log.d("BLE-RESPONSE", "[Coupon] onParsableDataResult");
        }
    };


    BleBeaconListener bleDnaListener = new BleBeaconListener<DNAEntity>() {
        @Override
        public void onBeaconDataResult(List<BeaconResultEntity> list) {

            Log.d("BLE-RESPONSE", "[DNA]Total : " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Log.d("BLE-RESPONSE", "[DNA]Inside : " + list.get(i).getBeaconDetail().getBluetoothAddress()
                        + " | Accuracy : " + list.get(i).getBeaconDetail().getAccuracy());
            }

        }

        @Override
        public void onError(String errorMsg) {
            Log.d("BLE-RESPONSE", "[DNA]" + errorMsg);
        }

        @Override
        public void onShowProgress() {
            Log.d("BLE-RESPONSE", "[DNA]onShowProgress");
        }

        @Override
        public void onParsableDataResult(List<DNAEntity> parsableData) {
            Log.d("BLE-RESPONSE", "[DNA]onParsableDataResult");
            //Your json parsable data list
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BLEBeaconWrapper bleBeaconWrapper = new BLEBeaconWrapper(this);
        String url = "https://www.kroger.com/cl/api/coupons?couponsCountPerLoad=100";

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");

        bleBeaconWrapper.getBeaconData(url,
                CouponEntity.class, headerMap, 3000, bleCouponListener);


        Map headerMap1 = new HashMap<>();
        headerMap.put("Accept", "*/*");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");

        bleBeaconWrapper.getBeaconData("http://api.plos.org/search?q=title:DNA",
                DNAEntity.class, headerMap1, 3000, bleDnaListener);


    }


}
