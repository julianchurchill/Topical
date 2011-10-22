package com.ChewieLouie.Topical.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ChewieLouie.Topical.AndroidPreferenceStorage;
import com.ChewieLouie.Topical.GooglePlus;
import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.GooglePlusSearchCallbackIfc;
import com.ChewieLouie.Topical.PersistentStorageIfc;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicWatcher;
import com.ChewieLouie.Topical.TopicalConstants;
import com.ChewieLouie.Topical.View.FollowedPostsStatusView;
import com.ChewieLouie.Topical.View.ViewPostIfc;
import com.ChewieLouie.Topical.View.ViewTopicListIfc;
import com.ChewieLouie.Topical.View.WatchedTopicsListView;

public class TopicalActivity extends Activity implements GooglePlusSearchCallbackIfc {

	public static List<Post> currentPosts = null;
	public static TopicWatcher topicWatcher = null;
	public static PersistentStorageIfc storage = null;
	
	private static final int EXIT_MENU_ITEM = 0;

	private String topic = null;
	private List<Post> followedPosts = new ArrayList<Post>();
	private ViewPostIfc followedPostsView = null;
	private ViewTopicListIfc watchedTopicsStatusView = null;

	public TopicalActivity() {
		super();
	}

	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
		storage = new AndroidPreferenceStorage( this );
		topicWatcher = new TopicWatcher( storage, GooglePlus.Make(), new TopicFactory() );
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS );
        setContentView( R.layout.main );
        addTopicItemClickNotifier();
    	followedPostsView = new FollowedPostsStatusView( (Button)findViewById( R.id.ShowFollowedPostsButton ) );
    	followedPosts = findFollowedPosts();
    	updateFollowedPostsStatus();
    	watchedTopicsStatusView = new WatchedTopicsListView( this, (ListView)findViewById( R.id.topicList ) );
	}

    private void addTopicItemClickNotifier() {
	    ListView lv = (ListView)findViewById( R.id.topicList );
	    lv.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				searchForTopic( topicWatcher.topicAtPosition( position ) );
			}
		});
	}
    
    private void searchForTopic( String topic ) {
    	this.topic = topic;
    	setProgressBarIndeterminateVisibility( true );
    	GooglePlus.Make().search( topic, this );
    }

	private List<Post> findFollowedPosts() {
    	List<Post> posts = new ArrayList<Post>();
    	for( String url : storage.getAllPostURLsWhereFollowingIsTrue() )
    		posts.add( createPostWithURL( url ) );
    	return posts;
    }

	private void updateFollowedPostsStatus() {
    	for( Post post : followedPosts )
    		post.show( followedPostsView  );
    }

    @Override
	protected void onResume() {
		super.onResume();
    	topicWatcher.populateTopicList( watchedTopicsStatusView  );
    	topicWatcher.updateStatuses();
    }

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
    	menu.clear();
		menu.add( Menu.NONE, EXIT_MENU_ITEM, Menu.NONE, getText( R.string.exit ) );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
    	boolean retVal = true;
        switch( item.getItemId() )
        {
        	case EXIT_MENU_ITEM:
        		finish();
        		break;
	        default:
	            retVal = super.onOptionsItemSelected( item );
	            break;
        }
        return retVal;
    }

	public void showFollowedPosts( View view ) {
    	currentPosts = findFollowedPosts();
    	startTopicListActivityWithTitle( "Followed Posts" );
	}

	private void startTopicListActivityWithTitle( String title ) {
		cancelFollowedPostsViewUpdate();
		Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
		intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, title );
		startActivity( intent );
	}
	
	private void cancelFollowedPostsViewUpdate() {
    	for( Post post : followedPosts )
    		post.viewIsNoLongerUsable();
    }

	private Post createPostWithURL( String url ) {
		Map<DataType,String> postInfo = new HashMap<DataType, String>();
		postInfo.put( DataType.URL, url );
		return new Post( postInfo, storage, GooglePlus.Make() );
	}

    public void search( View view ) {
    	startActivity( new Intent().setClass( getApplicationContext(), SearchActivity.class ) );
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

    private void showTopicList( String topic, List<Post> posts ) {
    	currentPosts = posts;
		topicWatcher.updatePostsForTopicListStatus( topic, posts );
		topicWatcher.orderPostsByTopicListStatus( topic, posts );
		topicWatcher.viewed( topic );
    	startTopicListActivityWithTitle( topic );
    }
}
