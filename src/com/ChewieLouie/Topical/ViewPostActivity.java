package com.ChewieLouie.Topical;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

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

    private class GetPostInformationTask extends AsyncTask<Post, Void, Post> {
    	@Override
		protected Post doInBackground( Post... post ) {
    		post[0].getAuthor();
    		post[0].getContent();
    		post[0].getComments();
    		return post[0];
		}

		@Override
		protected void onPostExecute( Post post ) {
			super.onPostExecute( post );
			authorTextView.setText( post.getAuthor() );
			textTextView.setText( post.getContent() );
			commentTextView.setText( post.getComments() );
	    	setProgressBarIndeterminateVisibility( false );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	    	setProgressBarIndeterminateVisibility( true );
		}
    }
}
