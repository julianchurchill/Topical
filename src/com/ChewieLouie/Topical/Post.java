package com.ChewieLouie.Topical;

import java.util.Calendar;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.google.api.client.util.DateTime;

public class Post implements GooglePlusCallbackIfc {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	private String authorID = null;
	private GooglePlusIfc googlePlus = null;
	private boolean isFollowed = false;
	private Map<DataType, String> postInfo = null;
	private PersistentStorageIfc storage = null;
	private String postID = "";
	private String url = "";
	private String title = "";
	private String summaryText = "";
	private DateTime lastViewedModificationTime = null;
	private DateTime currentModificationTime = null;
	private ViewPostIfc viewPost = null;
	private boolean needsLoadingFromGooglePlus = true;

	public Post( String url, PersistentStorageIfc storage, GooglePlusIfc googlePlus ) {
		this.storage = storage;
		this.url = url;
		this.googlePlus = googlePlus;
		extractAuthorIDFromURL( url );
		loadData();
	}

	private void extractAuthorIDFromURL( String url ) {
		final String gPlusURLPostsSeperator = "/posts/";
		int authorIDEndIndex = url.indexOf( gPlusURLPostsSeperator );
		if( authorIDEndIndex != -1 ) {
			int authorIDStartIndex = url.lastIndexOf( "/", authorIDEndIndex-1 ) + 1;
			if( authorIDStartIndex != -1 )
				authorID = url.substring( authorIDStartIndex, authorIDEndIndex );
		}
	}

	private void loadData() {
		this.title = storage.load( url, ValueType.TITLE );
		this.summaryText = storage.load( url, ValueType.SUMMARY );
		this.isFollowed = Boolean.parseBoolean( storage.load( url, ValueType.IS_FOLLOWED ) );
		this.postID = storage.load( url, ValueType.POST_ID );
		String modTime = storage.load( url, ValueType.LAST_VIEWED_MODIFICATION_TIME );
		if( modTime.equals( "" ) == false )
			this.lastViewedModificationTime = DateTime.parseRfc3339( modTime );
	}

	public void setTitle( String title ) {
		this.title = title;
		storage.save( url, ValueType.TITLE, title );
	}

	public void setSummary( String summaryText ) {
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
		this.viewPost = viewPost;
		viewPost.activityStarted();
		viewPost.setTitle( title );

		if( needsLoadingFromGooglePlus ) {
			if( postID.equals( "" ) )
				googlePlus.getPostInformation( this, authorID, url );
			else
				googlePlus.getPostInformationByPostID( this, postID );
		}
		else
			updateView();
	}

	@Override
	public void postInformationResults( Map<DataType, String> postInfo ) {
		this.postInfo = postInfo;
		this.needsLoadingFromGooglePlus = false;
		updateView();
	}
	
	private void updateView() {
		String newPostID = postInfo.get( DataType.POST_ID );
		if( newPostID != postID ) {
			postID = newPostID;
			storage.save( url, ValueType.POST_ID, postID );
		}
		viewPost.setAuthor( postInfo.get( DataType.AUTHOR_NAME ) );
		viewPost.setAuthorImage( postInfo.get( DataType.AUTHOR_IMAGE ) );
		viewPost.setHTMLContent( postInfo.get( DataType.POST_CONTENT ) );
		viewPost.setComments( postInfo.get( DataType.COMMENTS ) );
		viewPost.setStatus( status() );
		viewPost.setSummaryText( summaryText );
		currentModificationTime = DateTime.parseRfc3339( postInfo.get( DataType.MODIFICATION_TIME ) );
		viewPost.activityStopped();
	}

	@Override
	public void postInformationError( String errorText ) {
		this.needsLoadingFromGooglePlus = false;
		viewPost.showError( errorText );
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