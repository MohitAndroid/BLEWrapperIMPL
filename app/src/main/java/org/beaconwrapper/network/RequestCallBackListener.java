package org.beaconwrapper.network;

/**
 * The interface Request call back listener is a callback.
 */
public interface RequestCallBackListener {

    /**
     * Gives callback to manage UI operation before network call
     */
    void beforeCallBack();

    /**
     * Contains server response.
     *
     * @param responseString the response string
     */
    void onResponse(String responseString);

    /**
     * On error.
     *
     * @param errorMsg the error msg
     */
    void onError(String errorMsg);
}
