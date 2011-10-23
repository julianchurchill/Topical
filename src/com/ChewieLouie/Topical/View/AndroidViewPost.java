package com.ChewieLouie.Topical.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicListStatus;

public class AndroidViewPost implements ViewPostIfc {

	private Activity activity = null;
	private TextView authorTextView = null;
	private ImageView authorImageView = null;
	private TextView textTextView = null;
	private LayoutInflater layoutInflater = null;
	private String reshareAuthorName = "";

	public AndroidViewPost( Activity activity, TextView authorTextView, ImageView authorImageView,
			TextView textTextView ) {
		this.activity = activity;
		this.authorTextView = authorTextView;
		this.authorImageView = authorImageView;
		this.textTextView = textTextView;
		layoutInflater = (LayoutInflater)activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
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
		textTextView.setText( Html.fromHtml( makeReshareString() + content ) );
	}
	
	private String makeReshareString() {
		if( reshareAuthorName.isEmpty() )
			return "";
		return "Reshared post from " + reshareAuthorName + "<br/><br/>";
	}

	@Override
	public void setComments( List<PostComment> comments ) {
		LinearLayout commentsLayout = (LinearLayout)activity.findViewById( R.id.commentsLayout );
		for( PostComment comment : comments )
			commentsLayout.addView( createCommentView( createCommentText( comment ) ) );
		if( comments.isEmpty() )
			commentsLayout.addView( createCommentView( "No comments" ) );
	}
	
	private String createCommentText( PostComment comment ) {
		return ">> " + comment.author + " " + comment.updatedTime + " " + comment.content + "\n";
	}
	
	private View createCommentView( String content ) {
		View v = layoutInflater.inflate( R.layout.comment_item, null );
		TextView textView = (TextView)v.findViewById( R.id.comments_text );
		textView.setText( Html.fromHtml( content ) );
		return v;
	}

	@Override
	public void setStatus( Status status ) {
	}

	@Override
	public void setSummaryText( String summary ) {
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

	@Override
	public void setReshareAuthorName( String author ) {
		reshareAuthorName = author;
	}

	@Override
	public void setTopicListStatus( TopicListStatus status ) {
	}

	@Override
	public void setModificationTimeRfc3339( String modTime ) {
	}
}
