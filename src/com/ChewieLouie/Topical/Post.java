package com.ChewieLouie.Topical;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	public String postID = "";
	public String authorID = "";
	public Status status = Status.NEW;
	public String title = "";
	public String text = "";
	public String url = "";

	private static final String gPlusURLPostsSeperator = "/posts/";
	private String content = null;
	
	public Post( String title, String text, String url )
	{
		this.title = title;
		this.text = text;
		setUrl( url );
	}

	public Post( String title, String text, Status status )
	{
		this.title = title;
		this.text = text;
		this.status = status;
	}
	
	public String getContent() {
		if( content == null )
		{
			content = GooglePlusFactory.create().getPostContent( postID );
		}
		return content;
	}

	private void setUrl( String url ) {
		this.url = url;
		extractPostIDFromURL( url );
		extractAuthorIDFromURL( url );
	}
	
	private void extractPostIDFromURL( String url ) {
		int postIDStartIndex = url.indexOf( gPlusURLPostsSeperator ) + gPlusURLPostsSeperator.length();
		if( postIDStartIndex != -1 )
		{
			postID = url.substring( postIDStartIndex );
		}
	}

	private void extractAuthorIDFromURL( String url ) {
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
