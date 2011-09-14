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
	private Map<Post.PostStatus, Integer> statusToColourMap = new HashMap<Post.PostStatus, Integer>();
	
    public TopicListAdapter(Context context, int textViewResourceId, List<Post> items) {
    	super(context, textViewResourceId, items);
    	this.items = items;
    	this.myContext = context;
		statusToColourMap.put( Post.PostStatus.New, Color.GRAY );
		statusToColourMap.put( Post.PostStatus.FollowingNoChanges, Color.GREEN );
		statusToColourMap.put( Post.PostStatus.FollowingHasChanges, Color.BLUE );
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
    		TextView tt = (TextView) v.findViewById(R.id.topic_list_item_text);
            if (tt != null)
            {
        		tt.setText( p.text );
        		tt.setBackgroundColor( statusToColourMap.get( TopicalActivity.currentTopic.get( position ).status ) );
            }
        }
        return v;
    }
}