package com.ChewieLouie.Topical;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPostActivity extends Activity {

	private TextView authorTextView = null;
	private TextView textTextView = null;
	private TextView commentTextView = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
		new GetPostInformationTask().execute( post );
	}

    private class GetPostInformationTask extends AsyncTask<Post, Void, Boolean> {
    	private String errorText = null;
    	private Post post = null;
    	@Override
		protected Boolean doInBackground( Post... posts ) {
    		post = posts[0];
    		try {
    			post.retrieveRemoteInformation();
			} catch (IOException e) {
				e.printStackTrace();
				errorText = e.getMessage();
				return false;
			}
    		return true;
		}

		@Override
		protected void onPostExecute( Boolean postPopulatedOk ) {
			super.onPostExecute( postPopulatedOk );
			if( postPopulatedOk == false )
			{
				Toast.makeText( getApplicationContext(), errorText, Toast.LENGTH_LONG ).show();
			}
			authorTextView.setText( post.author );
			textTextView.setText( post.content );
			commentTextView.setText( post.comments );
	    	setProgressBarIndeterminateVisibility( false );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	    	setProgressBarIndeterminateVisibility( true );
		}
    }
}
