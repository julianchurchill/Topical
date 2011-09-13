package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TopicListActivity extends ListActivity {

	private List<String> searchResults = new ArrayList<String>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_list);
        addTopicListContents();
        addTopicToTitle();
    }

	private void addTopicListContents()
	{
		String[] topicListContents = getIntent().getStringArrayExtra( TopicalConstants.IntentExtraKey_TopicListContents );
		if( topicListContents != null )
			searchResults = Arrays.asList( topicListContents );
        setListAdapter( new ArrayAdapter<String>( this, R.layout.topic_list_item, 
        		R.id.topic_list_item_text, searchResults ) );
	}

	private void addTopicToTitle()
	{
		String searchQuery = getIntent().getStringExtra(
			TopicalConstants.IntentExtraKey_TopicListTopic );
		TextView title = (TextView)findViewById( R.id.topicListTitle );
		title.setText( title.getText() + ": \"" + searchQuery + "\"" );
	}
}
