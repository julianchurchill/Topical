package com.ChewieLouie.Topical;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPostActivity extends Activity {

	private TextView textView = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_post );
		textView = (TextView)findViewById( R.id.text );
    }

	@Override
	protected void onResume() {
		super.onResume();
		final int index = getIntent().getIntExtra( TopicalConstants.IntentExtraKey_ViewTopicIndex, -1 );
		Post post = TopicalActivity.currentTopic.get( index );
		textView.setText( post.getContent() );
	}
}
