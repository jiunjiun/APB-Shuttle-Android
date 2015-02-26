package tw.jiunjiun.apb.shuttle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by jiun on 2015/2/25.
 *
 * http://v4all123.blogspot.tw/2014/05/simple-navigation-drawer-example-in.html
 */
public class NavDrawerAdapter extends BaseAdapter {
    private Context context;
    private String[] titles;
    private LayoutInflater inflater;

    public NavDrawerAdapter(Context context, String[] titles) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.titles = titles;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list, null);
            mViewHolder = new ViewHolder();
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
        mViewHolder.tvTitle.setText(titles[position]);

        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
    }
}
