package com.ChewieLouie.Topical.test.mock;

import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public class MockViewWatchedTopic implements ViewWatchedTopicIfc {

	public boolean setTextCalled = false;
	public String setTextArg = "";
	public boolean setTopicResultsHaveChangedCalled = false;
	public boolean setTopicResultsHaveNotChangedCalled = false;
	public boolean activityStartedCalled = false;
	public boolean activityStoppedCalled = false;

	@Override
	public void setText( String text ) {
		setTextCalled = true;
		setTextArg = text;
	}

	@Override
	public void setTopicResultsHaveChanged() {
		setTopicResultsHaveChangedCalled = true;
	}

	@Override
	public void setTopicResultsHaveNotChanged() {
		setTopicResultsHaveNotChangedCalled = true;
	}

	@Override
	public void activityStarted() {
		activityStartedCalled = true;
	}

	@Override
	public void activityStopped() {
		activityStoppedCalled = true;
	}
}
