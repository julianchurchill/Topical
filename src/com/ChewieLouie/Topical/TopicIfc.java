package com.ChewieLouie.Topical;

import java.util.List;

import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public interface TopicIfc {

	public abstract void showStatus(ViewWatchedTopicIfc view);
	public abstract String topicName();
	public abstract void updateStatus();
	public abstract void viewed();
	public abstract void updatePostsForTopicListStatus( List<Post> posts );
	public abstract List<Post> orderPostsByTopicListStatus( List<Post> posts );
}