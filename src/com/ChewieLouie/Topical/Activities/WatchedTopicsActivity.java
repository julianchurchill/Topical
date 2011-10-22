package com.ChewieLouie.Topical.Activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ChewieLouie.Topical.GooglePlus;
import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.GooglePlusSearchCallbackIfc;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicalConstants;
import com.ChewieLouie.Topical.View.ViewTopicListIfc;
import com.ChewieLouie.Topical.View.WatchedTopicsListView;

public class WatchedTopicsActivity extends Activity implements GooglePlusSearchCallbackIfc {
	
	private String topic = null;
	private ViewTopicListIfc watchedTopicsStatusView = null;

	public WatchedTopicsActivity() {
		super();
	}

	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS );
        setContentView( R.layout.watched_topic_list );
        addTopicItemClickNotifier();
    	watchedTopicsStatusView = new WatchedTopicsListView( this, (ListView)findViewById( R.id.topicList ) );
	}

	private void addTopicItemClickNotifier() {
	    ListView lv = (ListView)findViewById( R.id.topicList );
	    lv.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				searchForTopic( TopicalActivity.topicWatcher.topicAtPosition( position ) );
			}
		});
	}

    private void searchForTopic( String topic ) {
    	this.topic = topic;
    	setProgressBarIndeterminateVisibility( true );
    	GooglePlus.Make().search( topic, this );
    }

	@Override
	public void searchResults(List<Map<DataType, String>> results) {
    	List<Post> posts = new ArrayList<Post>();
		for( Map<DataType,String> result : results )
			posts.add( new Post( result, TopicalActivity.storage, GooglePlus.Make() ) );
    	setProgressBarIndeterminateVisibility( false );
    	if( posts.size() > 0 )
    		showTopicList( topic, posts );
    	else
    		Toast.makeText( this, "No results found", Toast.LENGTH_LONG ).show();
	}

    private void showTopicList( String topic, List<Post> posts ) {
    	TopicalActivity.currentPosts = posts;
    	TopicalActivity.topicWatcher.updatePostsForTopicListStatus( topic, posts );
    	TopicalActivity.topicWatcher.orderPostsByTopicListStatus( topic, posts );
    	TopicalActivity.topicWatcher.viewed( topic );
    	startTopicListActivityWithTitle( topic );
    }

	private void startTopicListActivityWithTitle( String title ) {
		Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
		intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, title );
		startActivity( intent );
	}

    @Override
	protected void onResume() {
		super.onResume();
		TopicalActivity.topicWatcher.populateTopicList( watchedTopicsStatusView  );
		TopicalActivity.topicWatcher.updateStatuses();
    }
}