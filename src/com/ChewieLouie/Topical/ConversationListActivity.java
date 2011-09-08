package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ConversationListActivity extends ListActivity {

	private List<String> searchResults = new ArrayList<String>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationList);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.conversationListItem, searchResults));
    }
}
