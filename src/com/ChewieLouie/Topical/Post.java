package com.ChewieLouie.Topical;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	public String ID = "";
	public Status status = Status.NEW;
	public String title = "";
	public String text = "";
	
	public Post( String title, String text )
	{
		this.title = title;
		this.text = text;
	}

	public Post( String title, String text, Status status )
	{
		this.title = title;
		this.text = text;
		this.status = status;
	}
}
