package com.ChewieLouie.Topical;

import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.View.NullViewWatchedTopic;
import com.ChewieLouie.Topical.View.ViewWatchedTopicIfc;

public class Topic implements GooglePlusSearchCallbackIfc {

	public boolean hasChanged = false;
	
	private ViewWatchedTopicIfc view = new NullViewWatchedTopic();
	private String topicName = "";
	private GooglePlusIfc googlePlus = null;

	public Topic( String topicName, GooglePlusIfc googlePlus ) {
		this.topicName = topicName;
		this.googlePlus = googlePlus;
	}

	public void viewIsNoLongerUsable() {
		view = new NullViewWatchedTopic();
	}

	public void show( ViewWatchedTopicIfc view ) {
		this.view = view;
		view.setText( topicName );
		updateViewWithStatus();
	}

	private void updateViewWithStatus() {
		if( hasChanged )
			view.setTopicResultsHaveChanged();
		else
			view.setTopicResultsHaveNotChanged();
	}

	public String topicName() {
		return this.topicName;
	}

	public void updateStatus() {
		view.activityStarted();
   		googlePlus.search( topicName, this );
	}

	@Override
	public void searchResults( List<Map<DataType, String>> results ) {
		hasChanged = topicResultsHaveChanged( results );
		updateViewWithStatus();
		view.activityStopped();
	}

	private boolean topicResultsHaveChanged( List<Map<DataType, String>> results ) {
		return true;
	}
}
