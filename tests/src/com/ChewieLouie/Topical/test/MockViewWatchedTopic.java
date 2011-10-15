package com.ChewieLouie.Topical.test;

import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public class MockViewWatchedTopic implements ViewWatchedTopicIfc {

	public boolean setTextCalled = false;
	public String setTextArg = "";
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
