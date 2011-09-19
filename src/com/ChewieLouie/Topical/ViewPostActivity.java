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
		final String postID = getIntent().getStringExtra( TopicalConstants.IntentExtraKey_ViewTopicID );
		textView.setText( GooglePlusPostFinderFactory.create().getPostContent( postID ) );
	}
}
