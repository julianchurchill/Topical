package com.ChewieLouie.Topical;

import java.util.Calendar;
import java.util.concurrent.Semaphore;
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
	private boolean isFollowed = false;
	private Map<DataType, String> postInfo = new HashMap<DataType, String>();
	private boolean needsLoadingFromGooglePlus = true;
	private PersistentStorageIfc storage = null;
	private GooglePlusIfc googlePlus = null;
	private ViewPostIfc viewPost = null;
	private Semaphore protectView = new Semaphore( 1 );

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
		this.title = storage.loadValueByKeyAndType( url, ValueType.TITLE );
		this.summaryText = storage.loadValueByKeyAndType( url, ValueType.SUMMARY );
		this.isFollowed = Boolean.parseBoolean( storage.loadValueByKeyAndType( url, ValueType.IS_FOLLOWED ) );
		this.postID = storage.loadValueByKeyAndType( url, ValueType.POST_ID );
		String modTime = storage.loadValueByKeyAndType( url, ValueType.LAST_VIEWED_MODIFICATION_TIME );
		if( modTime.equals( "" ) == false )
			this.lastViewedModificationTime = DateTime.parseRfc3339( modTime );
	}

	public void setTitle( String title ) {
		this.title = title;
		storage.saveValueByKeyAndType( title, url, ValueType.TITLE );
	}

	public void setSummary( String summaryText ) {
		this.summaryText = summaryText;
		storage.saveValueByKeyAndType( summaryText, url, ValueType.SUMMARY );
	}
	
	public void follow() {
		isFollowed = true;
		storage.saveValueByKeyAndType( String.valueOf( isFollowed ), url, ValueType.IS_FOLLOWED );
	}

	public void unfollow() {
		isFollowed = false;
		storage.saveValueByKeyAndType( String.valueOf( isFollowed ), url, ValueType.IS_FOLLOWED );
	}
	
	public boolean isFollowed() {
		return isFollowed;
	}

	public void show( ViewPostIfc viewPost ) {
		viewPost.activityStarted();
		viewPost.setTitle( title );

		protectView.acquireUninterruptibly();
		this.viewPost = viewPost;
		if( needsLoadingFromGooglePlus ) {
			googlePlus.getPostInformation( this, postID, authorID, url );
			needsLoadingFromGooglePlus = false;
		}
		else
			updateViewFromPostInfo();
	}

	private void updateViewFromPostInfo() {
		viewPost.setAuthor( postInfo.get( DataType.AUTHOR_NAME ) );
		viewPost.setAuthorImage( postInfo.get( DataType.AUTHOR_IMAGE ) );
		viewPost.setHTMLContent( postInfo.get( DataType.POST_CONTENT ) );
		viewPost.setComments( postInfo.get( DataType.COMMENTS ) );
		viewPost.setStatus( status() );
		viewPost.setSummaryText( summaryText );
		viewPost.activityStopped();
		protectView.release();
	}

	private void setPostID( String newPostID ) {
		if( newPostID.equals( postID ) == false ) {
			postID = newPostID;
			storage.saveValueByKeyAndType( postID, url, ValueType.POST_ID );
		}
	}

	@Override
	public void postInformationResults( Map<DataType, String> postInfo ) {
		this.postInfo = postInfo;
		setPostID( postInfo.get( DataType.POST_ID ) );
		currentModificationTime = DateTime.parseRfc3339( postInfo.get( DataType.MODIFICATION_TIME ) );
		updateViewFromPostInfo();
	}

	@Override
	public void postInformationError( String errorText ) {
		viewPost.showError( errorText );
		viewPost.activityStopped();
		protectView.release();
	}

	public void viewed() {
		lastViewedModificationTime = currentModificationTime;
		if( lastViewedModificationTime != null )
			storage.saveValueByKeyAndType( lastViewedModificationTime.toStringRfc3339(), url, ValueType.LAST_VIEWED_MODIFICATION_TIME );
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