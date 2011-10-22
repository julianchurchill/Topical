package com.ChewieLouie.Topical.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.widget.TextView;

import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.TopicListStatus;

public class AndroidSummaryViewPost implements ViewPostIfc {

	// Key is post status, value is color int (see android.graphics.Color class)
	private static final Map<Post.Status, Integer> statusToColourMap = initializeStatusMap();
	private static Map<Post.Status, Integer> initializeStatusMap() {
		Map<Post.Status, Integer> map = new HashMap<Post.Status, Integer>();
		map.put( Post.Status.NEW, Color.GRAY );
		map.put( Post.Status.FOLLOWING_AND_NOT_CHANGED, Color.BLUE );
		map.put( Post.Status.FOLLOWING_AND_HAS_CHANGED, Color.CYAN );
		return Collections.unmodifiableMap( map );
	}

	private TextView titleTextView = null;
	private TextView summaryTextView = null;
	private TextView listStatusTextView = null;

	public AndroidSummaryViewPost( TextView titleTextView, TextView summaryTextView, TextView listStatusTextView ) {
		this.titleTextView = titleTextView;
		this.summaryTextView = summaryTextView;
		this.listStatusTextView = listStatusTextView;
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
	public void setComments(List<PostComment> comments) {
	}

	@Override
	public void setStatus( Status status ) {
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

	@Override
	public void setReshareAuthorName( String author ) {
	}

	@Override
	public void setTopicListStatus( TopicListStatus status ) {
		if( status == TopicListStatus.NEW ) {
			listStatusTextView.setText( "New!" );
			listStatusTextView.setTextColor( Color.MAGENTA );
		}
		else if( status == TopicListStatus.OLD )
			listStatusTextView.setText( "" );
	}
}
