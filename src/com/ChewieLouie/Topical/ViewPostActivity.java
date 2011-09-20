package com.ChewieLouie.Topical;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPostActivity extends Activity {

	private TextView authorTextView = null;
	private TextView textTextView = null;
	private TextView commentTextView = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_post );
		authorTextView = (TextView)findViewById( R.id.author );
		textTextView = (TextView)findViewById( R.id.text );
		commentTextView = (TextView)findViewById( R.id.comments );
    }

	@Override
	protected void onResume() {
		super.onResume();
		final int index = getIntent().getIntExtra( TopicalConstants.IntentExtraKey_ViewTopicIndex, -1 );
		Post post = TopicalActivity.currentTopic.get( index );
		authorTextView.setText( post.getAuthor() );
		textTextView.setText( post.getContent() );
		commentTextView.setText( post.getComments() );
	}
}
