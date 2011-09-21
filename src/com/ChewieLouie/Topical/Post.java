package com.ChewieLouie.Topical;

import java.io.IOException;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	public Status status = Status.NEW;
	public String title = "";
	public String text = "";
	public String url = "";
	public String author = null;
	public String content = null;
	public String comments = null;

	private String authorID = null;
	
	public Post( String title, String text, String url )
	{
		this.title = title;
		this.text = text;
		setUrl( url );
	}

	public Post( String title, String text, String url, Status status )
	{
		this.title = title;
		this.text = text;
		setUrl( url );
		this.status = status;
	}
	
	public void retrieveRemoteInformation() throws IOException {
		if( author == null )
		{
			author = GooglePlusFactory.create().getAuthor( authorID );
		}
		if( content == null )
		{
			content = GooglePlusFactory.create().getPostContent( authorID, url );
		}
		if( comments == null )
		{
			comments = GooglePlusFactory.create().getComments( authorID, url );
		}
	}

	private void setUrl( String url ) {
		this.url = url;
		extractAuthorIDFromURL( url );
	}
	
	private void extractAuthorIDFromURL( String url ) {
		final String gPlusURLPostsSeperator = "/posts/";
		int authorIDEndIndex = url.indexOf( gPlusURLPostsSeperator );
		if( authorIDEndIndex != -1 )
		{
			int authorIDStartIndex = url.lastIndexOf( "/", authorIDEndIndex-1 ) + 1;
			if( authorIDStartIndex != -1 )
			{
				authorID = url.substring( authorIDStartIndex, authorIDEndIndex );
			}
		}
	}
}
