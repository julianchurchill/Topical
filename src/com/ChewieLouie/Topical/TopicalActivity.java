package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.ChewieLouie.Topical.Post.PostStatus;

public class TopicalActivity extends Activity {

	public static List<Post> currentTopic = new ArrayList<Post>();

	private final String[] testTopics = { "Topic 1", "Topic 2", "Topic 3" };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        populateTopicList();
        addTopicItemClickNotifier();
    }

    public void search( View view ) {
    	EditText editText = (EditText)findViewById( R.id.SearchText );
    	String topic = editText.getText().toString();
    	startActivity( createTopicListIntent( topic, GooglePlusIfc.search( topic ) ) );
    }
    
    private Intent createTopicListIntent( String topic, String[] postsContent )
    {
    	Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, topic );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListContents, postsContent );
    	currentTopic.clear();
    	for( String post : postsContent )
    	{
    		currentTopic.add( new Post( post, PostStatus.New ) );
    	}
    	return intent;
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
    	final String topic = testTopics[ position ];
    	final String[] listContents = { "new post 1", "following no changes post", "following has changes" };
    	Intent intent = createTopicListIntent( topic, listContents );
    	currentTopic.get( 0 ).status = PostStatus.New;
    	currentTopic.get( 1 ).status = PostStatus.FollowingNoChanges;
    	currentTopic.get( 2 ).status = PostStatus.FollowingHasChanges;
    	startActivity( intent );
    }
}