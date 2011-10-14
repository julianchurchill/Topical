package com.ChewieLouie.Topical.View;

import java.util.List;

import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.PostComment;

public interface ViewPostIfc {

	public abstract void setAuthor( String author );
	public abstract void setAuthorImage( String imageURL );
	public abstract void setHTMLContent( String content );
	public abstract void setComments( List<PostComment> comments );
	public abstract void setStatus( Post.Status status );
	public abstract void setTitle( String title );
	public abstract void setSummaryText( String summary );
	public abstract void showError( String errorText );
	public abstract void activityStarted();
	public abstract void activityStopped();
}
