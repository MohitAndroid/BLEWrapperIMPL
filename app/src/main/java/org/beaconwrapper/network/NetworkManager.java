package org.beaconwrapper.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * This class is responsible for fire request to server and providing proper response for same.
 */
public class NetworkManager {

    /**
     * Handle get request.
     *
     * @param baseUrl                 the base url
     * @param headerData              the header data
     * @param requestCallBackListener the request call back listener
     */
    public static void getRequest(String baseUrl, Map<String, String> headerData,
                                  RequestCallBackListener requestCallBackListener) {
        try {
            OkHttpClient client = new OkHttpClient();
            OkHttpClient eagerClient = client.newBuilder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request.Builder builder = new Request.Builder();
            builder.url(baseUrl);
            if (headerData != null && !headerData.isEmpty()) {
                for (Map.Entry<String, String> entry : headerData.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            Request request = builder.build();
            requestCallBackListener.beforeCallBack();
            eagerClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    requestCallBackListener.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    requestCallBackListener.onResponse(response.body().string());
                }
            });
        } catch (Exception e) {
            requestCallBackListener.onError(e.getMessage());
        }

    }

    /**
     * Handle Post request.
     *
     * @param baseUrl                 the base url
     * @param headerData              the header data
     * @param bodyData                the body data
     * @param requestCallBackListener the request call back listener
     */
    public void postRequest(String baseUrl, Map<String, String> headerData,
                            Map<String, String> bodyData,
                            RequestCallBackListener requestCallBackListener) {

        try {
            MediaType mediaType = MediaType.parse("application/json");
            OkHttpClient client = new OkHttpClient();
            OkHttpClient eagerClient = client.newBuilder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Request.Builder builder = new Request.Builder();
            builder.url(baseUrl);

            RequestBody body = RequestBody.create(mediaType, getPostData(bodyData).toString());
            builder.post(body);


            if (headerData != null && !headerData.isEmpty()) {
                for (Map.Entry<String, String> entry : headerData.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            Request request = builder.build();
            requestCallBackListener.beforeCallBack();
            eagerClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    requestCallBackListener.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    requestCallBackListener.onResponse(response.body().string());
                }
            });
        } catch (Exception e) {
            requestCallBackListener.onError(e.getMessage());
        }

    }

    private JSONObject getPostData(Map<String, String> bodyData) {
        JSONObject postData = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : bodyData.entrySet()) {
                postData.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            Log.e(NetworkManager.class.getName(), e.getMessage(), e);
        }
        return postData;
    }
}
