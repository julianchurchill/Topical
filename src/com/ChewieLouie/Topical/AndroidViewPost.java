package com.ChewieLouie.Topical;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidViewPost implements ViewPostIfc {

	private Activity activity = null;
	private TextView authorTextView = null;
	private ImageView authorImageView = null;
	private TextView textTextView = null;
	private TextView commentTextView = null;

	public AndroidViewPost( Activity activity, TextView authorTextView, ImageView authorImageView,
			TextView textTextView, TextView commentTextView ) {
		this.activity = activity;
		this.authorTextView = authorTextView;
		this.authorImageView = authorImageView;
		this.textTextView = textTextView;
		this.commentTextView = commentTextView;
	}
	
	@Override
	public void setAuthor(String author) {
		authorTextView.setText( author );
	}

	@Override
	public void setAuthorImage( String imageURL ) {
		authorImageView.setImageDrawable( ImageOperations( imageURL ) );
	}

	@Override
	public void setHTMLContent( String content ) {
		textTextView.setText( Html.fromHtml( content ) );
	}

	@Override
	public void setComments( String comments ) {
		commentTextView.setText( "Comments: " + comments );
	}

	@Override
	public void showError(String errorText) {
		Toast.makeText( activity, errorText, Toast.LENGTH_LONG ).show();
	}


	@Override
	public void activityStarted() {
    	activity.setProgressBarIndeterminateVisibility( true );
	}

	@Override
	public void activityStopped() {
    	activity.setProgressBarIndeterminateVisibility( false );
	}

	private Object fetch(String address) throws MalformedURLException, IOException {
        return new URL(address).getContent();
    }  

    private Drawable ImageOperations( String url ) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            return Drawable.createFromStream(is, "src");
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
