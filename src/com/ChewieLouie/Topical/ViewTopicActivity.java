package com.ChewieLouie.Topical;

import android.app.ListActivity;
import android.os.Bundle;

public class ViewTopicActivity extends ListActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);
    }
}
