package com.ChewieLouie.Topical;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.google.api.client.util.DateTime;

public class Post implements GooglePlusCallbackIfc {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	private String url = "";
	private String authorID = null;
	private String postID = null;
	private String title = "";
	private String summaryText = "";
	private DateTime lastViewedModificationTime = null;
	private DateTime currentModificationTime = null;
	private String authorName = "";
	private String authorImage = "";
	private String content = "";
	private String comments = "";
	private boolean isFollowed = false;
	private PersistentStorageIfc storage = null;
	private GooglePlusIfc googlePlus = null;
	private int requestID = 0;
	private Map<Integer, ViewPostIfc> views = new HashMap<Integer, ViewPostIfc>();

	public Post( Map<DataType, String> postInfo, PersistentStorageIfc storage, GooglePlusIfc googlePlus ) {
		this.storage = storage;
		this.googlePlus = googlePlus;
		setURL( postInfo.get( DataType.URL ) );
		loadDataFromStorage();
		parsePostInfo( postInfo );
	}

	private void setURL( String url ) {
		this.url = url;
		final String gPlusURLPostsSeperator = "/posts/";
		int authorIDEndIndex = url.indexOf( gPlusURLPostsSeperator );
		if( authorIDEndIndex != -1 ) {
			int authorIDStartIndex = url.lastIndexOf( "/", authorIDEndIndex-1 ) + 1;
			if( authorIDStartIndex != -1 )
				authorID = url.substring( authorIDStartIndex, authorIDEndIndex );
		}
	}

	private void parsePostInfo( Map<DataType, String> postInfo ) {
		setPostID( postInfo.get( DataType.POST_ID ) );
		if( postInfo.get( DataType.MODIFICATION_TIME ) != null )
			currentModificationTime = DateTime.parseRfc3339( postInfo.get( DataType.MODIFICATION_TIME ) );
		setTitle( postInfo.get( DataType.TITLE ) );
		setSummary( postInfo.get( DataType.SUMMARY ) );
		if( postInfo.get( DataType.AUTHOR_ID ) != null )
			authorID = postInfo.get( DataType.AUTHOR_ID );
		authorName = postInfo.get( DataType.AUTHOR_NAME );
		authorImage = postInfo.get( DataType.AUTHOR_IMAGE );
		content = postInfo.get( DataType.POST_CONTENT );
		comments = postInfo.get( DataType.COMMENTS );
	}

	private void loadDataFromStorage() {
		this.title = storage.load( url, ValueType.TITLE );
		this.summaryText = storage.load( url, ValueType.SUMMARY );
		this.isFollowed = Boolean.parseBoolean( storage.load( url, ValueType.IS_FOLLOWED ) );
		this.postID = storage.load( url, ValueType.POST_ID );
		String modTime = storage.load( url, ValueType.LAST_VIEWED_MODIFICATION_TIME );
		if( modTime.equals( "" ) == false )
			this.lastViewedModificationTime = DateTime.parseRfc3339( modTime );
	}

	private void setPostID( String postID ) {
		if( postID != null ) {
			this.postID = postID;
			storage.save( url, ValueType.POST_ID, postID );
		}
	}

	private void setTitle( String title ) {
		if( title != null ) {
			this.title = title;
			storage.save( url, ValueType.TITLE, title );
		}
	}

	private void setSummary( String summaryText ) {
		if( summaryText != null ) {
			this.summaryText = summaryText;
			storage.save( url, ValueType.SUMMARY, summaryText );
		}
	}
	
	public void follow() {
		isFollowed = true;
		storage.save( url, ValueType.IS_FOLLOWED, String.valueOf( isFollowed ) );
	}

	public void unfollow() {
		isFollowed = false;
		storage.save( url, ValueType.IS_FOLLOWED, String.valueOf( isFollowed ) );
	}
	
	public boolean isFollowed() {
		return isFollowed;
	}

	public void show( ViewPostIfc view ) {
		view.activityStarted();
		view.setTitle( title );
		if( postInfoIncomplete() ) {
			views.put( requestID, view );
			googlePlus.getPostInformation( this, new GooglePlusQuery( postID, authorID, url ), requestID++ );
		}
		else
			updateView( view );
	}
	
	private boolean postInfoIncomplete() {
		return  postID == null || postID.isEmpty() || 
				authorID == null || authorID.isEmpty() || 
				content == null || content.isEmpty() ||
				authorImage == null || authorImage.isEmpty();
	}

	private void updateView( ViewPostIfc view ) {
		view.setAuthor( authorName );
		view.setAuthorImage( authorImage );
		view.setHTMLContent( content );
		view.setComments( comments );
		view.setStatus( status() );
		view.setSummaryText( summaryText );
		view.activityStopped();
	}

	@Override
	public void postInformationResults( Map<DataType, String> postInfo, int requestID ) {
		parsePostInfo( postInfo );
		updateView( views.get( requestID ) );
		views.remove( requestID );
	}

	@Override
	public void postInformationError( String errorText, int requestID ) {
		ViewPostIfc view = views.get( requestID );
		view.showError( errorText );
		view.activityStopped();
		views.remove( requestID );
	}

	public void viewed() {
		lastViewedModificationTime = currentModificationTime;
		if( lastViewedModificationTime != null )
			storage.save( url, ValueType.LAST_VIEWED_MODIFICATION_TIME, lastViewedModificationTime.toStringRfc3339() );
	}

	private boolean isPostModifiedSinceLastView() {
		if( lastViewedModificationTime != null && currentModificationTime != null ) {
			Calendar currentModificationCalendar = Calendar.getInstance();
			currentModificationCalendar.setTimeInMillis( currentModificationTime.getValue() );
			if( currentModificationCalendar.after( lastViewedModificationTime ) )
				return true;
		}
		return false;
	}

	private Post.Status status() {
		if( isFollowed ) {
			if( isPostModifiedSinceLastView() )
				return Post.Status.FOLLOWING_AND_HAS_CHANGED;
			return Post.Status.FOLLOWING_AND_NOT_CHANGED;
		}
		return Post.Status.NEW;
	}
}