package com.ChewieLouie.Topical.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ChewieLouie.Topical.AndroidPreferenceStorage;
import com.ChewieLouie.Topical.GooglePlus;
import com.ChewieLouie.Topical.GooglePlusCallbackIfc;
import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicWatcher;
import com.ChewieLouie.Topical.TopicalConstants;

public class TopicalActivity extends Activity implements GooglePlusCallbackIfc {

	public static List<Post> currentPosts = null;
	public static TopicWatcher topicWatcher = null;

	private EditText searchEditText = null;	
	private PersistentStorageIfc storage = null;
	private String topic = null;

	public TopicalActivity() {
		super();
	}

	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
		storage = new AndroidPreferenceStorage( this );
		topicWatcher = new TopicWatcher( storage );
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS );
        setContentView( R.layout.main );
        addTopicItemClickNotifier();
    	searchEditText = (EditText)findViewById( R.id.SearchText );
    }

    @Override
	protected void onResume() {
		super.onResume();
        populateTopicList();
    }

	public void showFollowedPosts( View view ) {
    	currentPosts = new ArrayList<Post>();
    	for( String url : storage.getAllPostURLsWhereFollowingIsTrue() )
    		currentPosts.add( createPostWithURL( url ) );
    	Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, "Followed Posts" );
    	startActivity( intent );
	}

	private Post createPostWithURL( String url ) {
		Map<DataType,String> postInfo = new HashMap<DataType, String>();
		postInfo.put( DataType.URL, url );
		return new Post( postInfo, storage, GooglePlus.Make() );
	}

    public void search( View view ) {
    	searchForTopic( searchEditText.getText().toString().trim() );
    }
    
    private void searchForTopic( String topic ) {
    	this.topic = topic;
    	setProgressBarIndeterminateVisibility( true );
    	GooglePlus.Make().search( topic, this );
    }

    private void showTopicList( String topic, List<Post> posts ) {
    	currentPosts = posts;
    	Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, topic );
    	startActivity( intent );
    }
    
    private void populateTopicList() {
    	ListView conversationList = (ListView)findViewById( R.id.topicList );
    	conversationList.setAdapter( new ArrayAdapter<String>( this, R.layout.topic_list_item, 
        		R.id.topic_list_item_text, watchedTopics() ) );
    }
    
    private String[] watchedTopics() {
    	return topicWatcher.watchedTopics().toArray( new String[0] );
    }

    private void addTopicItemClickNotifier() {
	    ListView lv = (ListView)findViewById( R.id.topicList );
	    lv.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				searchForTopic( watchedTopics()[position] );
			}
		});
	}

	@Override
	public void postInformationResults(Map<DataType, String> postInfo, int requestID) {
	}

	@Override
	public void postInformationError(String errorText, int requestID) {
	}

	@Override
	public void searchResults(List<Map<DataType, String>> results) {
    	List<Post> posts = new ArrayList<Post>();
		for( Map<DataType,String> result : results )
			posts.add( new Post( result, storage, GooglePlus.Make() ) );
    	setProgressBarIndeterminateVisibility( false );
    	if( posts.size() > 0 )
    		showTopicList( topic, posts );
    	else
    		Toast.makeText( TopicalActivity.this, "No results found", Toast.LENGTH_LONG ).show();
	}
}
