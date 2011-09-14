package com.ChewieLouie.Topical;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TopicListActivity extends ListActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_list);
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		addTopicListContents();
        addTopicToTitle();
    }

	private void addTopicListContents()
	{
        setListAdapter( new TopicListAdapter( this, R.layout.topic_list_item, TopicalActivity.currentTopic ) );
	}

	private void addTopicToTitle()
	{
		String searchQuery = getIntent().getStringExtra(
			TopicalConstants.IntentExtraKey_TopicListTopic );
		TextView title = (TextView)findViewById( R.id.topicListTitle );
		title.setText( title.getText() + ": \"" + searchQuery + "\"" );
	}
}
