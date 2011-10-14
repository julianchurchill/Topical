package com.ChewieLouie.Topical.View;

public interface ViewWatchedTopicIfc {

	public abstract void setText( String text );
	public abstract void setTopicResultsHaveChanged();
	public abstract void setTopicResultsHaveNotChanged();
	public abstract void activityStarted();
	public abstract void activityStopped();
}
