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
    private Map<View, TopicIfc> viewsBeingUpdated = new HashMap<View, TopicIfc>();

    public WatchedTopicListAdapter(Context context, ArrayList<TopicIfc> arrayList) {
    	super(context, layoutResource, arrayList);
    	this.items = arrayList;
    	this.myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
    	if( v == null ) {
    		LayoutInflater vi = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		v = vi.inflate(layoutResource, null);
    	}
    	TopicIfc topic = items.get(position);
    	if( topic != null ) {
    		if( viewsBeingUpdated.containsKey( v ) )
    			viewsBeingUpdated.get( v ).viewIsNoLongerUsable();
    		viewsBeingUpdated.put( v, topic );
    		TextView textView = (TextView) v.findViewById( R.id.topic_list_item_text );
    		topic.show( new WatchedTopicView( textView ) );
        }
        return v;
    }
}