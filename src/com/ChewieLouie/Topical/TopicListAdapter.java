package com.ChewieLouie.Topical;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TopicListAdapter extends ArrayAdapter<Post> {

    private List<Post> items = null;
    private Context myContext = null;

    // Key is post status, value is color int (see android.graphics.Color class)
	private Map<Post.Status, Integer> statusToColourMap = new HashMap<Post.Status, Integer>();
	
    public TopicListAdapter(Context context, int textViewResourceId, List<Post> items) {
    	super(context, textViewResourceId, items);
    	this.items = items;
    	this.myContext = context;
		statusToColourMap.put( Post.Status.NEW, Color.GRAY );
		statusToColourMap.put( Post.Status.FOLLOWING_AND_NOT_CHANGED, Color.BLUE );
		statusToColourMap.put( Post.Status.FOLLOWING_AND_HAS_CHANGED, Color.CYAN );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
    	if (v == null)
    	{
    		LayoutInflater vi = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		v = vi.inflate(R.layout.topic_list_item, null);
    	}
    	Post p = items.get(position);
    	if (p != null)
    	{
    		TextView titleTextView = setListItemContent( v, R.id.topic_list_item_title, p.title );
    		setItemStatus( titleTextView, p.getStatus() );
    		setListItemContent( v, R.id.topic_list_item_text, p.text );
        }
        return v;
    }
    
    private TextView setListItemContent( View v, int listItemResource, String text ) {
		TextView view = (TextView) v.findViewById( listItemResource );
    	view.setText( text );
        return view;
    }
    
    private void setItemStatus( TextView titleTextView, Post.Status status ) {
    	titleTextView.setBackgroundColor( statusToColourMap.get( status ) );
    }
}