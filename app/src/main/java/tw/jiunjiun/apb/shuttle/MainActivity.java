package tw.jiunjiun.apb.shuttle;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     *
     * https://zybuluo.com/Faith/note/55693
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static final String ReceiverProgress = "tw.jiunjiun.apb.Progress";
    private static final String TAG = "MainActivity";
    private static final String WebUrl = "http://apb-shuttle.info/";

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    public static  CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        registerReceiver(mBroadcast, new IntentFilter(ReceiverProgress));
    }

    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        getSupportActionBar().getCustomView().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isNetworkConnected()) showAlertDialog();
        init();
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mBroadcast);
        super.onDestroy();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        Intent intent;
        switch(position) {
        default:
        case 0:
            GAnalytics("IndexFragment");
            fragment = new IndexFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();

            break;
        case 1:
            GAnalytics("APBFragment");
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebUrl + "apb"));
            startActivity(intent);
            break;
        case 2:
            GAnalytics("OrangeFragment");
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebUrl + "orange"));
            startActivity(intent);
            break;
        case 3:
            GAnalytics("AboutFragment");
            fragment = new AboutFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();
            break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.app_name);
                break;
            case 1:
                mTitle = getString(R.string.title_apb);
                break;
            case 2:
                mTitle = getString(R.string.title_orange);
                break;
            case 3:
                mTitle = getString(R.string.title_about);
                break;
        }
//        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) return true;

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(progressBar, new ActionBar.LayoutParams(GravityCompat.END));
    }

    private void GAnalytics(String path) {
        Tracker t = ((GAnalytics) getApplication()).getTracker(GAnalytics.TrackerName.APP_TRACKER);
        t.setScreenName(path);
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private boolean isNetworkConnected() {
        /*
         * http://blog.kenyang.net/2010/06/android.html
         */
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service
        NetworkInfo networInfo = conManager.getActiveNetworkInfo();
        return networInfo == null || !networInfo.isAvailable();
    }

    private void showAlertDialog() {
        /*
         * http://www.cnblogs.com/LeeYZ/archive/2012/08/20/2648308.html
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
               .setMessage(R.string.alert_message)
               .setPositiveButton(R.string.alert_setting,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               // TODO Auto-generated method stub
                               Intent intent = null;
                               //判断手机系统的版本  即API大于10 就是3.0或以上版本
                               if (android.os.Build.VERSION.SDK_INT > 10) {
                                   intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                               } else {
                                   intent = new Intent();
                                   ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                                   intent.setComponent(component);
                                   intent.setAction("android.intent.action.VIEW");
                               }
                               startActivity(intent);
                           }
                       }).setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                dialog.dismiss();
                finish();
            }
        }).show();
    }

    private BroadcastReceiver mBroadcast =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, Intent mIntent) {
            // TODO Auto-generated method stub
            if(ReceiverProgress.equals(mIntent.getAction())){
                boolean visible = mIntent.getBooleanExtra("visible", false);
                setSupportProgressBarIndeterminateVisibility(visible);
            }
        }
    };
}
