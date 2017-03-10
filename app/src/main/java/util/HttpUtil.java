package util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用okhttp来发送请求
 */
public class HttpUtil {
    /**
     * 联网请求的方法
     */
    private static void sendRequest(final String address){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url(address)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseDate=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
