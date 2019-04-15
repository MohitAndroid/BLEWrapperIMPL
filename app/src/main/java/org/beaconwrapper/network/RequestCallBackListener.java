package org.beaconwrapper.network;

public interface RequestCallBackListener {

    void beforeCallBack();

    void onResponse(String responseString);

    void onError(String errorMsg);
}
