package com.ChewieLouie.Topical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicalActivity extends Activity {
	
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
    
    private Intent createTopicListIntent( String topic, String[] listContents )
    {
    	Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, topic );
    	intent.putExtra( TopicalConstants.IntentExtraKey_TopicListContents, listContents );
    	return intent;
    }
    
    private void populateTopicList()
    {
    	final String[] topics = { "Topic 1", "Topic 2", "Topic 3" };
    	ListView conversationList = (ListView)findViewById( R.id.topicList );
    	conversationList.setAdapter( new ArrayAdapter<String>( this, R.layout.topic_list_item, 
        		R.id.topic_list_item_text, topics ) );
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
    	final String topic = "test topic";
    	final String[] listContents = { "test contents1", "test contents2", "test contents3" };
    	startActivity( createTopicListIntent( topic, listContents ) );
    }
}