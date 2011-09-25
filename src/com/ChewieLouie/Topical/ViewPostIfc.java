package com.ChewieLouie.Topical;

public interface ViewPostIfc {

	public abstract void setAuthor( String author );
	public abstract void setAuthorImage( String imageURL );
	public abstract void setHTMLContent( String content );
	public abstract void setComments( String comments );
	public abstract void setStatus( Post.Status status );
	public abstract void setTitle( String title );
	public abstract void setSummaryText( String summary );
	public abstract void showError( String errorText );
	public abstract void activityStarted();
	public abstract void activityStopped();
}
