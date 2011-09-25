package com.ChewieLouie.Topical;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TopicListAdapter extends ArrayAdapter<Post> {

    private List<Post> items = null;
    private Context myContext = null;

    public TopicListAdapter(Context context, int textViewResourceId, List<Post> items) {
    	super(context, textViewResourceId, items);
    	this.items = items;
    	this.myContext = context;
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
    		TextView titleTextView = (TextView) v.findViewById( R.id.topic_list_item_title );
    		TextView textView = (TextView) v.findViewById( R.id.topic_list_item_text );
    		p.show( new AndroidSummaryViewPost( titleTextView, textView ) );
        }
        return v;
    }
}