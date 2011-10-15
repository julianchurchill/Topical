package com.ChewieLouie.Topical.View;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.ChewieLouie.Topical.Topic;
import com.ChewieLouie.Topical.WatchedTopicListAdapter;

public class WatchedTopicsListView implements ViewTopicListIfc {

	private Activity activity = null;
	private ListView topicList = null;

	public WatchedTopicsListView( Activity activity, ListView topicList ) {
		this.activity = activity;
		this.topicList = topicList;
	}

	@Override
	public void populateTopicList( Collection<Topic> topics ) {
    	topicList.setAdapter( new WatchedTopicListAdapter( (Context)activity, new ArrayList<Topic>( topics ) ) );
	}
}
