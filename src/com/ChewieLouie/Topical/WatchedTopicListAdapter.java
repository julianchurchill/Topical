package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ChewieLouie.Topical.View.WatchedTopicView;

public class WatchedTopicListAdapter extends ArrayAdapter<TopicIfc> {

    private static final int layoutResource = R.layout.topic_list_item;

    private List<TopicIfc> items = null;
    private Context myContext = null;
    private Map<Integer, View> viewCache = new HashMap<Integer, View>();

    public WatchedTopicListAdapter(Context context, ArrayList<TopicIfc> arrayList) {
    	super(context, layoutResource, arrayList);
    	this.items = arrayList;
    	this.myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = viewCache.get( position );
    	if( v == null ) {
    		LayoutInflater vi = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		v = vi.inflate(layoutResource, null);
    	  	viewCache.put( position, v );
    	}
    	TopicIfc topic = items.get(position);
    	if( topic != null ) {
    		TextView textView = (TextView) v.findViewById( R.id.topic_list_item_text );
    		topic.showStatus( new WatchedTopicView( textView ) );
        }
        return v;
    }
}