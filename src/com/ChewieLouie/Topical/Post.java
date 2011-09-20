package com.ChewieLouie.Topical;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	public Status status = Status.NEW;
	public String title = "";
	public String text = "";
	public String url = "";

	private static final String gPlusURLPostsSeperator = "/posts/";
	private String postID = null;
	private String authorID = null;
	private String author = null;
	private String content = null;
	private String comments = null;
	
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

	public String getAuthor() {
		if( author == null )
		{
			author = GooglePlusFactory.create().getAuthor( authorID );
		}
		return author;
	}
	
	public String getContent() {
		if( content == null )
		{
			content = GooglePlusFactory.create().getPostContent( postID );
		}
		return content;
	}
	
	public String getComments() {
		if( comments == null )
		{
			comments = GooglePlusFactory.create().getComments( postID );
		}
		return comments;
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
