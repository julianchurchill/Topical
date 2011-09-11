package com.ChewieLouie.Topical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TopicalActivity extends Activity {
	String[] topics = { "Topic 1", "Topic 2", "Topic 3" };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        populateTopicList();
    }

    public void search( View view ) {
    	EditText editText = (EditText)findViewById( R.id.SearchText );
    	Intent intent = new Intent().setClass( getApplicationContext(), ConversationListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_ConversationsSearchString,
    			editText.getText().toString() );
    	intent.putExtra( TopicalConstants.IntentExtraKey_Conversations,
    			GooglePlusIfc.search( editText.getText().toString() ) );
    	startActivity( intent );
    }
    
    private void populateTopicList()
    {
    	ListView conversationList = (ListView)findViewById( R.id.topicList );
    	conversationList.setAdapter( new ArrayAdapter<String>( this, R.layout.conversation_list_item, 
        		R.id.conversation_list_item_text, topics ) );
    }
}