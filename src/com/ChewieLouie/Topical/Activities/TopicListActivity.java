package com.ChewieLouie.Topical.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicListAdapter;
import com.ChewieLouie.Topical.TopicalConstants;

public class TopicListActivity extends ListActivity {

	private static final int WATCH_MENU_ITEM = 0;
	private static final int UNWATCH_MENU_ITEM = 1;

	private TextView title = null;
	private String topic = "";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_list);
		title = (TextView)findViewById( R.id.topicListTitle );
	}	

    @Override
	protected void onResume() {
		super.onResume();
        addTopicToTitle();
		addTopicListContents();
    }

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
    	menu.clear();
    	if( TopicalActivity.topicWatcher.isWatched( topic ) )
    		menu.add( Menu.NONE, UNWATCH_MENU_ITEM, Menu.NONE, getText( R.string.unwatch ) );
    	else
    		menu.add( Menu.NONE, WATCH_MENU_ITEM, Menu.NONE, getText( R.string.watch ) );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
    	boolean retVal = true;
        switch( item.getItemId() )
        {
        	case WATCH_MENU_ITEM:
            	TopicalActivity.topicWatcher.watch( topic );
        		break;
	        case UNWATCH_MENU_ITEM:
            	TopicalActivity.topicWatcher.unwatch( topic );
	            break;
	        default:
	            retVal = super.onOptionsItemSelected( item );
	            break;
        }
        return retVal;
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
    	Intent intent = new Intent().setClass( getApplicationContext(), ViewPostActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_ViewTopicIndex, position );
    	startActivity( intent );
	}

	private void addTopicToTitle()
	{
		topic = getIntent().getStringExtra( TopicalConstants.IntentExtraKey_TopicListTopic );
		CharSequence titlePrefix = getApplicationContext().getText( R.string.topicListTitle );
		title.setText( titlePrefix + ": \"" + topic + "\"" );
	}

	private void addTopicListContents()
	{
        setListAdapter( new TopicListAdapter( this, R.layout.topic_list_item, TopicalActivity.currentPosts ) );
	}
}
