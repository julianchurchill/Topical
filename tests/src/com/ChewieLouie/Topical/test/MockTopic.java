package com.ChewieLouie.Topical.test;

import com.ChewieLouie.Topical.TopicIfc;
import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public class MockTopic implements TopicIfc {

	public boolean viewedCalled = false;
	
	public MockTopic() {
	}

	@Override
	public void viewIsNoLongerUsable() {
	}

	@Override
	public void show(ViewWatchedTopicIfc view) {
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
}
