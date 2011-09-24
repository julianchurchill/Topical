package com.ChewieLouie.Topical;

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

public class TopicalActivity extends Activity {

	public static List<Post> currentTopic = null;

	private EditText searchEditText = null;	
	private final String[] testTopics = { "Topic 1", "Topic 2", "Topic 3" };
	private List<Post> testTopicListContents = new ArrayList<Post>();

	public TopicalActivity() {
		super();
		createTestPosts();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        populateTopicList();
        addTopicItemClickNotifier();
    	searchEditText = (EditText)findViewById( R.id.SearchText );
    }

    public void search( View view ) {
    	new SearchGoogleTask().execute( searchEditText.getText().toString() );
    }
    
    private class SearchGoogleTask extends AsyncTask<String, Void, List<Post> > {
    	private String topic = "";
    	
    	@Override
		protected List<Post> doInBackground( String... searchTerm ) {
    		topic = searchTerm[0];
	    	return GooglePlusPostFinderFactory.create().search( topic );
		}

		@Override
		protected void onPostExecute(List<Post> result) {
			super.onPostExecute(result);
	    	setProgressBarIndeterminateVisibility( false );
	    	showTopicList( topic, result );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	    	setProgressBarIndeterminateVisibility( true );
		}
    }

    private void showTopicList( String topic, List<Post> posts )
    {
    	currentTopic = posts;
    	Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, topic );
    	startActivity( intent );
    }
    
    private void populateTopicList()
    {
    	ListView conversationList = (ListView)findViewById( R.id.topicList );
    	conversationList.setAdapter( new ArrayAdapter<String>( this, R.layout.topic_list_item, 
        		R.id.topic_list_item_text, testTopics ) );
    }

    private void addTopicItemClickNotifier()
	{
	    ListView lv = (ListView)findViewById( R.id.topicList );
	    lv.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				onTopicListClicked( view, position );
			}
		});
	}
    
    protected void onTopicListClicked( View view, int position )
    {
    	showTopicList( testTopics[position], testTopicListContents );
    }
    
    private void createTestPosts() {
    	testTopicListContents.add( new Post( "new post 1",
    			"snippet 1",
    			"http://somedomain/anauthorID/posts/aPostID" ) );
		testTopicListContents.add( new Post( "following and not changed post",
				"snippet 2",
    			"http://somedomain/anauthorID/posts/aPostID" ) );
		testTopicListContents.add( new Post( "following and has changed post",
				"snippet 3 the final snippet, returns.",
    			"http://somedomain/anauthorID/posts/aPostID" ) );
    }
}