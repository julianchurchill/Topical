package com.ChewieLouie.Topical;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.widget.TextView;

import com.ChewieLouie.Topical.Post.Status;

public class AndroidSummaryViewPost implements ViewPostIfc {

	private TextView titleTextView = null;
	private TextView summaryTextView = null;
    // Key is post status, value is color int (see android.graphics.Color class)
	private Map<Post.Status, Integer> statusToColourMap = new HashMap<Post.Status, Integer>();
	
	public AndroidSummaryViewPost( TextView titleTextView, TextView summaryTextView ) {
		this.titleTextView = titleTextView;
		this.summaryTextView = summaryTextView;
		statusToColourMap.put( Post.Status.NEW, Color.GRAY );
		statusToColourMap.put( Post.Status.FOLLOWING_AND_NOT_CHANGED, Color.BLUE );
		statusToColourMap.put( Post.Status.FOLLOWING_AND_HAS_CHANGED, Color.CYAN );
	}

	@Override
	public void setAuthor(String author) {
	}

	@Override
	public void setAuthorImage(String imageURL) {
	}

	@Override
	public void setHTMLContent(String content) {
	}

	@Override
	public void setComments(String comments) {
	}

	@Override
	public void setStatus(  Status status ) {
    	titleTextView.setBackgroundColor( statusToColourMap.get( status ) );
	}

	@Override
	public void setTitle( String title ) {
		titleTextView.setText( title );
	}

	@Override
	public void setSummaryText( String summary ) {
		summaryTextView.setText( summary );
	}

	@Override
	public void showError( String errorText ) {
		summaryTextView.setText( errorText );
	}

	@Override
	public void activityStarted() {
		titleTextView.setText( "Loading..." );
		summaryTextView.setText( "Loading..." );
	}

	@Override
	public void activityStopped() {
	}
}
