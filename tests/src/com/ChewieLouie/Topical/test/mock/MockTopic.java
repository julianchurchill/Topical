package com.ChewieLouie.Topical.test.mock;

import java.util.List;

import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.TopicIfc;
import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public class MockTopic implements TopicIfc {

	public boolean viewedCalled = false;
	
	public MockTopic() {
	}

	@Override
	public void showStatus(ViewWatchedTopicIfc view) {
	}

	@Override
	public String topicName() {
		return null;
	}

	@Override
	public void updateStatus() {
	}

	@Override
	public void viewed() {
		viewedCalled = true;
	}

	@Override
	public void updatePostsForTopicListStatus( List<Post> posts ) {
	}

	@Override
	public List<Post> orderPostsByTopicListStatus( List<Post> posts ) {
		return null;
	}
}
