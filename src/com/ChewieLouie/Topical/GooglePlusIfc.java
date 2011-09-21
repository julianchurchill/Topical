package com.ChewieLouie.Topical;

import java.io.IOException;

public interface GooglePlusIfc {
	public abstract String getAuthor( String postID ) throws IOException;
	public abstract String getPostContent( String authorID, String url ) throws IOException;
	public abstract String getComments( String authorID, String url ) throws IOException;
}
