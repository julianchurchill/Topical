package com.ChewieLouie.Topical;

import com.google.api.client.util.DateTime;

public class PostComment {
	public String author = null;
	public DateTime updatedTime = null;
	public String content = null;

	public PostComment( String author, DateTime updatedTime, String content ) {
		this.author = author;
		this.updatedTime = updatedTime;
		this.content = content;
	}
}
