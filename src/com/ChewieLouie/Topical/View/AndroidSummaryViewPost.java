package com.ChewieLouie.Topical.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import android.graphics.Color;
import android.widget.TextView;

import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.TopicListStatus;

public class AndroidSummaryViewPost implements ViewPostIfc {

	// Key is post status, value is color int (see android.graphics.Color class)
	private static final Map<Post.Status, Integer> statusToBackgroundColourMap = initializeStatusMap();
	private static Map<Post.Status, Integer> initializeStatusMap() {
		Map<Post.Status, Integer> map = new HashMap<Post.Status, Integer>();
		map.put( Post.Status.NEW, Color.BLACK );
		map.put( Post.Status.FOLLOWING_AND_NOT_CHANGED, Color.BLUE );
		map.put( Post.Status.FOLLOWING_AND_HAS_CHANGED, Color.CYAN );
		return Collections.unmodifiableMap( map );
	}

	private TextView titleTextView = null;
	private TextView summaryTextView = null;
	private TextView listStatusTextView = null;
	private String author = "";
	private DateTime modificationDateTime = null;

	public AndroidSummaryViewPost( TextView titleTextView, TextView summaryTextView, TextView listStatusTextView ) {
		this.titleTextView = titleTextView;
		this.summaryTextView = summaryTextView;
		this.listStatusTextView = listStatusTextView;
	}

	@Override
	public void setAuthor(String author) {
		this.author = author;
		updateTitle();
	}
	
	private void updateTitle() {
		String dateTimeString = "";
		if( modificationDateTime != null ) {
			Duration difference = new Duration( modificationDateTime.getMillis(), DateTime.now().getMillis() );
			if( difference.isLongerThan( Duration.standardDays( 2 ) ) )
				dateTimeString = modificationDateTime.dayOfMonth().getAsString() + " " +
								 modificationDateTime.monthOfYear().getAsShortText();
			else if( difference.isLongerThan( Duration.standardDays( 1 ) ) )
				dateTimeString = "yesterday";
			else if( difference.isLongerThan( Duration.standardHours( 1 ) ) ) {
				int hours = difference.toStandardHours().getHours();
				if( hours == 1 )
					dateTimeString = Integer.toString( hours ) + " hour ago";
				else
					dateTimeString = Integer.toString( hours ) + " hours ago";
			}
			else if( difference.isLongerThan( Duration.standardMinutes( 1 ) ) ) {
				int minutes = difference.toStandardMinutes().getMinutes();
				if( minutes == 1 )
					dateTimeString = Integer.toString( minutes ) + " minute ago";
				else
					dateTimeString = Integer.toString( minutes ) + " minutes ago";
			}
			else
				dateTimeString = modificationDateTime.dayOfMonth().getAsString() + " " +
								 modificationDateTime.monthOfYear().getAsShortText() + " " +
								 modificationDateTime.year().getAsString();
		}
		titleTextView.setText( author + " " + dateTimeString );
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
    	titleTextView.setBackgroundColor( statusToBackgroundColourMap.get( status ) );
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

	@Override
	public void setModificationTimeRfc3339( String modificationTime ) {
		this.modificationDateTime = new DateTime( modificationTime );
	}
}
