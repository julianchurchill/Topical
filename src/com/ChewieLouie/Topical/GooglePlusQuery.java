package com.ChewieLouie.Topical;

public class GooglePlusQuery {
	public String postID = null;
	public String authorID = null;
	public String url = null;

	public GooglePlusQuery( String postID, String authorID, String url ) {
		this.postID = postID;
		this.authorID = authorID;
		this.url = url;
	}
	
	public String makeKeyFromAuthorAndURL() {
		String key = "";
		if( authorID != null )
			key += authorID;
		if( url != null )
			key += url;
		return key;
	}
}
