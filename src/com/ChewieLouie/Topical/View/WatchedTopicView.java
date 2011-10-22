package com.ChewieLouie.Topical.View;

import android.graphics.Color;
import android.widget.TextView;

public class WatchedTopicView implements ViewWatchedTopicIfc {

	private static final int pendingColour = Color.WHITE;
	private TextView topicTextView = null;
	private TextView statusTextView = null;
	private int resultColour = pendingColour;
	private boolean activityStarted = false;
	private String statusText = "---";

	public WatchedTopicView( TextView topicTextView, TextView statusTextView ) {
		this.topicTextView = topicTextView;
		this.statusTextView = statusTextView;
	}

	@Override
	public void setText( String text ) {
		topicTextView.setText( text );
	}

	@Override
	public void setTopicResultsHaveChanged() {
		resultColour = Color.MAGENTA;
		statusText = "New!";
		if( activityStarted == false )
			updateStatusTextView( resultColour );
	}

	@Override
	public void setTopicResultsHaveNotChanged() {
		statusText = "No new posts";
		if( activityStarted == false )
			updateStatusTextView( resultColour );
	}

	@Override
	public void activityStarted() {
		activityStarted = true;
		statusText = "---";
		updateStatusTextView( pendingColour );
	}

	@Override
	public void activityStopped() {
		updateStatusTextView( resultColour );
		activityStarted = false;
	}
	
	private void updateStatusTextView( int colour ) {
		statusTextView.setTextColor( colour );
		statusTextView.setText( statusText );
	}
}
