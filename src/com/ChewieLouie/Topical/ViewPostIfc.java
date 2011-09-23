package com.ChewieLouie.Topical;

public interface ViewPostIfc {

	public abstract void setAuthor( String author );
	public abstract void setAuthorImage( String imageURL );
	public abstract void setHTMLContent( String content );
	public abstract void setComments( String comments );
	public abstract void showError( String errorText );
	public abstract void activityStarted();
	public abstract void activityStopped();
}
