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
	private Map<Integer, ViewPostIfc> viewPosts = new HashMap<Integer, ViewPostIfc>();

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
		if( postInfo.get( DataType.POST_ID ) != null )
			setPostID( postInfo.get( DataType.POST_ID ) );
		if( postInfo.get( DataType.MODIFICATION_TIME ) != null )
			currentModificationTime = DateTime.parseRfc3339( postInfo.get( DataType.MODIFICATION_TIME ) );
		if( postInfo.get( DataType.TITLE ) != null )
			setTitle( postInfo.get( DataType.TITLE ) );
		if( postInfo.get( DataType.SUMMARY ) != null )
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

	private void setTitle( String title ) {
		this.title = title;
		storage.save( url, ValueType.TITLE, title );
	}

	private void setSummary( String summaryText ) {
		this.summaryText = summaryText;
		storage.save( url, ValueType.SUMMARY, summaryText );
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

	public void show( ViewPostIfc viewPost ) {
		viewPost.activityStarted();
		viewPost.setTitle( title );
		viewPosts.put( requestID, viewPost );
		googlePlus.getPostInformation( this, new GooglePlusQuery( postID, authorID, url ), requestID++ );
	}

	private void updateViewFromPostInfo( ViewPostIfc viewPost ) {
		viewPost.setAuthor( authorName );
		viewPost.setAuthorImage( authorImage );
		viewPost.setHTMLContent( content );
		viewPost.setComments( comments );
		viewPost.setStatus( status() );
		viewPost.setSummaryText( summaryText );
		viewPost.activityStopped();
	}

	private void setPostID( String newPostID ) {
		postID = newPostID;
		storage.save( url, ValueType.POST_ID, postID );
	}

	@Override
	public void postInformationResults( Map<DataType, String> postInfo, int requestID ) {
		parsePostInfo( postInfo );
		updateViewFromPostInfo( viewPosts.get( requestID ) );
		viewPosts.remove( requestID );
	}

	@Override
	public void postInformationError( String errorText, int requestID ) {
		ViewPostIfc viewPost = viewPosts.get( requestID );
		viewPost.showError( errorText );
		viewPost.activityStopped();
		viewPosts.remove( requestID );
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