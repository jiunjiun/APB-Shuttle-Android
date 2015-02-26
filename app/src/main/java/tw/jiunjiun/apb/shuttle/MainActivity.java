package tw.jiunjiun.apb.shuttle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


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

//        registerReceiver(mBroadcast, new IntentFilter(ReceiverProgress));
    }

    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        getSupportActionBar().getCustomView().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

//        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
//        this.unregisterReceiver(mBroadcast);
        super.onStop();
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
            fragment = new IndexFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();
            break;
        case 1:
//            fragment = new ApbFragment();
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apb.jiunjiun.me/apb"));
            startActivity(intent);
            break;
        case 2:
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apb.jiunjiun.me/orange"));
            startActivity(intent);
//            fragment = new OrangeFragment();
            break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
//                mTitle = getString(R.string.title_index);
                mTitle = getString(R.string.app_name);
                break;
            case 1:
                mTitle = getString(R.string.title_apb);
                break;
            case 2:
                mTitle = getString(R.string.title_orange);
                break;
        }
        restoreActionBar();
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

    // Should be called manually when an async task has started
//    public static void showProgressBar() {
//        setSupportProgressBarIndeterminateVisibility(true);
//    }
//
//    // Should be called when an async task has finished
//    public static void hideProgressBar() {
//        setSupportProgressBarIndeterminateVisibility(true);
//    }


//    private BroadcastReceiver mBroadcast =  new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context mContext, Intent mIntent) {
//            // TODO Auto-generated method stub
//            if(ReceiverProgress.equals(mIntent.getAction())){
//                boolean visible = mIntent.getBooleanExtra("visible", false);
//                setSupportProgressBarIndeterminateVisibility(visible);
//            }
//        }
//    };
}
