package com.ChewieLouie.Topical;

import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public interface TopicIfc {

	public abstract void viewIsNoLongerUsable();

	public abstract void showStatus(ViewWatchedTopicIfc view);

	public abstract String topicName();

	public abstract void updateStatus();

	public abstract void viewed();

}