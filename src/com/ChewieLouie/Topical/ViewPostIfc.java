package com.ChewieLouie.Topical;

import java.util.List;

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
