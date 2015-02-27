package tw.jiunjiun.apb.shuttle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by jiun on 2015/2/28.
 */
public class AboutFragment extends Fragment {

    private static final String TAG = "ApbFragment";

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_about, container, false);
        mView = initView(mView);
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private View initView(View mView) {
        WebView webView= (WebView) mView.findViewById(R.id.about_centent);
        webView.loadUrl("file:///android_asset/about.html");
        return mView;
    }
}
