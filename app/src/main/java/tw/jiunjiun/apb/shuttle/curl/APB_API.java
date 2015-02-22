package tw.jiunjiun.apb.shuttle.curl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jiun on 2015/2/23.
 */
public class APB_API {
    private static final String SERVER = "http://apb.jiunjiun.me/api/";

    private static final String TAG = "apb_api";

    public static int KIND = 0;
    public static final int KIND_NOW  = 2;
    public static final int KIND_NEXT = 1;
    public static final int KIND_ALL  = 3;
    public static final int KIND_INFO = 4;


    private Handler mHandler;
    private String API_URL = "";
    private String result = "";

    public APB_API() {

    }

    public APB_API(Handler mHandler, int kind) {
        this.mHandler = mHandler;
        getAPI(kind, new int[]{});
    }

    public APB_API(Handler mHandler, int kind, int[] params) {
        this.mHandler = mHandler;
        getAPI(kind, params);
    }

    public void getAPI(int kind, int[] params) {
        this.KIND = kind;
        switch(KIND) {
            case KIND_NOW:
                API_URL = SERVER + "now.json";
                break;
            case KIND_NEXT:
                API_URL = SERVER + "next/"+ params[0] +".json";
                break;
            case KIND_ALL:
                API_URL = SERVER + "all.json";
                break;
            case KIND_INFO:
                API_URL = SERVER + "info.json";
                break;
        }
        new Pull();
        sendMessage();
    }

    private void sendMessage() {
        Message message = mHandler.obtainMessage(KIND, result);
        mHandler.sendMessage(message);
    }

    class Pull implements Runnable {
        Pull() {
            Thread mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(API_URL);
            try {
                HttpResponse mHttpResponse = new DefaultHttpClient().execute(httpGet);
                if(mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(mHttpResponse.getEntity());
//                    Log.e(TAG, "result " + result);
                    sendMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
