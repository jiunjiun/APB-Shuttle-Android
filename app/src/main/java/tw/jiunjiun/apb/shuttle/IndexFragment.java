package tw.jiunjiun.apb.shuttle;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tw.jiunjiun.apb.shuttle.curl.APB_API;
import tw.jiunjiun.apb.shuttle.parser.ParserBus;

import static tw.jiunjiun.apb.shuttle.curl.APB_API.KIND_NOW;

/**
 * Created by jiun on 2015/2/22.
 */
public class IndexFragment extends Fragment {
    private static final String TAG = "IndexFragment";
    private ParserBus mParserBus;

    private TextView depart, details;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }

    public IndexFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_index, container, false);
        mView = initView(mView);
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        new APB_API(mHandler, KIND_NOW);
    }

    private View initView(View mView) {
        depart  = (TextView) mView.findViewById(R.id.depart);
        details = (TextView) mView.findViewById(R.id.details);
        return mView;
    }

    private void setupView() {
        depart.setText(mParserBus.depart);
        details.setText(mParserBus.full_note);
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KIND_NOW:
                    mParserBus = new ParserBus(msg.obj.toString());
                    setupView();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
