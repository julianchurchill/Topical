package com.ChewieLouie.Topical.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.ChewieLouie.Topical.GooglePlusPostFinderFactory;
import com.ChewieLouie.Topical.PersistentStorageIfc;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicalConstants;
import com.google.api.services.customsearch.model.Result;

public class TopicalActivity extends Activity {

	public static List<Post> currentPosts = null;

	private EditText searchEditText = null;	
	private final String[] watchedTopics = { "Topic 1", "Topic 2", "Topic 3" };
	private PersistentStorageIfc storage = null;

	public TopicalActivity() {
		super();
	}

	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
		storage = new AndroidPreferenceStorage( this );
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS );
        setContentView( R.layout.main );
        populateTopicList();
        addTopicItemClickNotifier();
    	searchEditText = (EditText)findViewById( R.id.SearchText );
    }

	public void showFollowedPosts( View view ) {
    	currentPosts = new ArrayList<Post>();
    	for( String url : storage.getAllPostURLsWhereFollowingIsTrue() )
    		currentPosts.add( new Post( url, storage ) );
    	Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, "Followed Posts" );
    	startActivity( intent );
	}

    public void search( View view ) {
    	new SearchGoogleTask().execute( searchEditText.getText().toString() );
    }
    
    private class SearchGoogleTask extends AsyncTask<String, Void, List<Post> > {
    	private String topic = "";
    	
    	@Override
		protected List<Post> doInBackground( String... searchTerm ) {
    		topic = searchTerm[0];
	    	List<Result> results = GooglePlusPostFinderFactory.create().search( topic );
	    	List<Post> posts = new ArrayList<Post>();
			if( results != null )
				for( Result result : results )
					posts.add( createPost( result ) );
			return posts;
		}
    	
    	private Post createPost( Result result ) {
			Post post = new Post( result.getLink(), storage );
			post.setTitle( result.getTitle() );
			post.setSummary( result.getSnippet() );
			return post;
    	}

		@Override
		protected void onPostExecute(List<Post> result) {
			super.onPostExecute(result);
	    	setProgressBarIndeterminateVisibility( false );
	    	if( result.size() > 0 )
	    		showTopicList( topic, result );
	    	else
	    		Toast.makeText( TopicalActivity.this, "No results found", Toast.LENGTH_LONG ).show();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	    	setProgressBarIndeterminateVisibility( true );
		}
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
        		R.id.topic_list_item_text, watchedTopics ) );
    }

    private void addTopicItemClickNotifier() {
	    ListView lv = (ListView)findViewById( R.id.topicList );
	    lv.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onTopicListClicked( view, position );
			}
		});
	}
    
    protected void onTopicListClicked( View view, int position ) {
    	showTopicList( watchedTopics[position], new ArrayList<Post>() );
    }
}