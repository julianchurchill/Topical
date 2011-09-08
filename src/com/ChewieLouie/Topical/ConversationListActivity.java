package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ConversationListActivity extends ListActivity {

	private List<String> searchResults = new ArrayList<String>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String[] conversations = getIntent().getStringArrayExtra( TopicalConstants.IntentExtraKey_Conversations );
		if( conversations != null )
			searchResults = Arrays.asList( conversations );
        setListAdapter( new ArrayAdapter<String>( this, R.layout.conversation_list_item, 
        		R.id.conversation_list_item_text, searchResults ) );
    }
}
