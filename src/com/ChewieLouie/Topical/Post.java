package com.ChewieLouie.Topical;

public class Post {
	public enum PostStatus { New, FollowingNoChanges, FollowingHasChanges };
	public String text = "";
	public PostStatus status = PostStatus.New;
	Post( String t, PostStatus s )
	{
		text = t;
		status = s;
	}
}
