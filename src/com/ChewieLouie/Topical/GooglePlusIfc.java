package com.ChewieLouie.Topical;

import java.io.IOException;

public interface GooglePlusIfc {
	public abstract String getAuthor( String authorID ) throws IOException;
	public abstract String getPostContent( String authorID, String url ) throws IOException;
	public abstract String getComments( String authorID, String url ) throws IOException;
	public abstract String getImageURL( String authorID ) throws IOException;
}
