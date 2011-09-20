package com.ChewieLouie.Topical;

public interface GooglePlusIfc {
	public abstract String getAuthor( String postID );
	public abstract String getPostContent( String postID );
	public abstract String getComments( String postID );
}
