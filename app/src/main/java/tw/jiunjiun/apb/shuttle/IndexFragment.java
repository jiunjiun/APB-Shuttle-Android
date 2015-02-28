package tw.jiunjiun.apb.shuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import tw.jiunjiun.apb.shuttle.DetectTouchGesture.DTGLinearLayout;
import tw.jiunjiun.apb.shuttle.curl.APB_API;
import tw.jiunjiun.apb.shuttle.parser.ParserBus;

import static tw.jiunjiun.apb.shuttle.curl.APB_API.KIND_NEXT;
import static tw.jiunjiun.apb.shuttle.curl.APB_API.KIND_NOW;

/**
 * Created by jiun on 2015/2/22.
 *
 * http://antonioleiva.com/swiperefreshlayout/
 *
 */
public class IndexFragment extends Fragment {
    private static final String TAG = "IndexFragment";
    private ParserBus mParserBus;

    private int NextNum = 0;

    private TextView depart, details;
    private ImageButton logo;
    private Button paginate_previous, paginate_next;
    private DTGLinearLayout mDTGLinearLayout;

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
        paginate_reset();
    }

    private View initView(View mView) {
        depart  = (TextView) mView.findViewById(R.id.depart);
        details = (TextView) mView.findViewById(R.id.details);

        logo   = (ImageButton) mView.findViewById(R.id.logo);
        paginate_previous = (Button) mView.findViewById(R.id.paginate_previous);
        paginate_next     = (Button) mView.findViewById(R.id.paginate_next);

        mDTGLinearLayout = (DTGLinearLayout) mView.findViewById(R.id.frag_index);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GAnalyticsOnClick("Reset");
                paginate_reset();
            }
        });

        paginate_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GAnalyticsOnClick("Previous, " + NextNum);
                paginate_previous();
            }
        });

        paginate_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GAnalyticsOnClick("Next, " + NextNum);
                paginate_next();
            }
        });


        mDTGLinearLayout.setSwipeGestureListener(new DTGLinearLayout.onSwipeGestureListener() {
            @Override
            public void onRightSwipe() {
                paginate_previous();
            }

            @Override
            public void onLeftSwipe() {
                paginate_next();
            }
        });

        return mView;
    }

    private void setupView() {
        ProgressBarIndeterminateVisibility(false);

        depart.setText(mParserBus.depart);
        details.setText(mParserBus.full_note);

        if(NextNum == 0) paginate_previous.setVisibility(View.GONE);
        if(NextNum == 1) paginate_previous.setVisibility(View.VISIBLE);
    }

    private void paginate_reset() {
        NextNum = 0;
        new APB_API(mHandler, KIND_NOW);
        ProgressBarIndeterminateVisibility(true);
    }

    private void paginate_previous() {
        NextNum--;
        new APB_API(mHandler, KIND_NEXT, new int[]{NextNum});
        ProgressBarIndeterminateVisibility(true);
    }

    private void paginate_next() {
        NextNum++;
        new APB_API(mHandler, KIND_NEXT, new int[]{NextNum});
        ProgressBarIndeterminateVisibility(true);
    }

    private void ProgressBarIndeterminateVisibility(boolean visible) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.ReceiverProgress);
        intent.putExtra("visible", visible);
        getActivity().sendBroadcast(intent);
    }

    private void GAnalyticsOnClick(String Label) {
        // Get tracker.
        Tracker t = ((GAnalytics) getActivity().getApplication()).getTracker(
                GAnalytics.TrackerName.APP_TRACKER);
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("IndexFragmentClick")
                .setAction("click")
                .setLabel(Label)
                .build());
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case KIND_NOW:
            case KIND_NEXT:
                mParserBus = new ParserBus(msg.obj.toString());
                setupView();
                break;
            }
            super.handleMessage(msg);
        }
    };
}
