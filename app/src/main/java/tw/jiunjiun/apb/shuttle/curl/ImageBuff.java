package tw.jiunjiun.apb.shuttle.curl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jiun on 2015/2/25.
 */
public class ImageBuff {
    private static final String TAG = "ImageBuff";
    private static String PULL_URL = "";

    public static final int KIND_FINISH  = 5;

    public static Bitmap mBitmap;
    private Handler mHandler;

    public ImageBuff(Handler mHandler, String url) {
        this.PULL_URL = url;
        this.mHandler = mHandler;

        new Pull();
    }

    private void sendMessage() {
        Message message = mHandler.obtainMessage(KIND_FINISH);
        mHandler.sendMessage(message);
    }

    class Pull implements Runnable {
        public Pull() {
            Thread mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL(PULL_URL);
                URLConnection conn = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection)conn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConn.getInputStream();

                    mBitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                    sendMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
