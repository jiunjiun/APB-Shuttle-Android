package tw.jiunjiun.apb.shuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import tw.jiunjiun.apb.shuttle.ImageTouch.ImageViewHelper;
import tw.jiunjiun.apb.shuttle.curl.APB_API;
import tw.jiunjiun.apb.shuttle.curl.ImageBuff;
import tw.jiunjiun.apb.shuttle.parser.ParseInfo;

import static tw.jiunjiun.apb.shuttle.curl.APB_API.KIND_INFO;
import static tw.jiunjiun.apb.shuttle.curl.ImageBuff.KIND_FINISH;

/**
 * Created by jiun on 2015/2/22.
 */
public class ApbFragment extends Fragment {
    private static final String TAG = "ApbFragment";

//    private ImageView apb_table;
    private ParseInfo mParseInfo;
    private ImageBuff mImageBuff;
    private WebView apb_table;

    public ApbFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_apb, container, false);
        mView = initView(mView);
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onStart() {
        super.onStart();
        init_start();
    }

    private View initView(View mView) {
//        apb_table = (ImageView) mView.findViewById(R.id.apb_table);
        apb_table = (WebView) mView.findViewById(R.id.apb_table);
        return mView;
    }

    private void init_start() {
//        ProgressBarIndeterminateVisibility(true);
//        new APB_API(mHandler, KIND_INFO);

        apb_table.getSettings().setJavaScriptEnabled(true);
        apb_table.requestFocus();
        apb_table.setWebViewClient(new MyWebViewClient());
        apb_table.loadUrl("http://apb.jiunjiun.me/apb");
    }

//    private void setupView() {
//        ProgressBarIndeterminateVisibility(false);
//
//
//        DisplayMetrics dm = new DisplayMetrics();
//        apb_table.setImageBitmap(mImageBuff.mBitmap);
//        new ImageViewHelper(dm, apb_table, mImageBuff.mBitmap);
//    }
//
//    private void ProgressBarIndeterminateVisibility(boolean visible) {
//        Intent intent = new Intent();
//        intent.setAction(MainActivity.ReceiverProgress);
//        intent.putExtra("visible", visible);
//        getActivity().sendBroadcast(intent);
//    }
//
//    Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case KIND_INFO:
//                    mParseInfo = new ParseInfo(msg.obj.toString());
//                    mImageBuff = new ImageBuff(mHandler, mParseInfo.APBImgsUrl("desktop"));
//                    break;
//                case KIND_FINISH:
//                    setupView();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

}
