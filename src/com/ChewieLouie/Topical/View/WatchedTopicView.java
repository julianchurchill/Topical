package com.ChewieLouie.Topical.View;

import android.graphics.Color;
import android.widget.TextView;

public class WatchedTopicView implements ViewWatchedTopicIfc {

	private TextView textView = null;

	public WatchedTopicView(TextView textView) {
		this.textView = textView;
	}

	@Override
	public void setText( String text ) {
		textView.setText( text );
	}

	@Override
	public void setTopicResultsHaveChanged() {
		textView.setBackgroundColor( Color.MAGENTA );
	}

	@Override
	public void setTopicResultsHaveNotChanged() {
		textView.setBackgroundColor( Color.GREEN );
	}

	@Override
	public void activityStarted() {
		textView.setBackgroundColor( Color.BLACK );
	}

	@Override
	public void activityStopped() {
	}
}
