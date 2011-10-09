package com.ChewieLouie.Topical;

import java.util.Calendar;
import java.util.List;
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
	private ViewPostIfc view = null;

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

	private void loadDataFromStorage() {
		this.title = storage.loadValueByKeyAndType( url, ValueType.TITLE );
		this.summaryText = storage.loadValueByKeyAndType( url, ValueType.SUMMARY );
		this.isFollowed = Boolean.parseBoolean( storage.loadValueByKeyAndType( url, ValueType.IS_FOLLOWED ) );
		this.postID = storage.loadValueByKeyAndType( url, ValueType.POST_ID );
		String modTime = storage.loadValueByKeyAndType( url, ValueType.LAST_VIEWED_MODIFICATION_TIME );
		if( modTime.equals( "" ) == false )
			this.lastViewedModificationTime = DateTime.parseRfc3339( modTime );
	}

	private void parsePostInfo( Map<DataType, String> postInfo ) {
		if( postInfo.get( DataType.POST_ID ) != null )
			setPostID( postInfo.get( DataType.POST_ID ) );
		if( postInfo.get( DataType.TITLE ) != null )
			setTitle( postInfo.get( DataType.TITLE ) );
		if( postInfo.get( DataType.SUMMARY ) != null )
			setSummary( postInfo.get( DataType.SUMMARY ) );
		if( postInfo.get( DataType.MODIFICATION_TIME ) != null )
			currentModificationTime = DateTime.parseRfc3339( postInfo.get( DataType.MODIFICATION_TIME ) );
		if( postInfo.get( DataType.AUTHOR_ID ) != null )
			authorID = postInfo.get( DataType.AUTHOR_ID );
		authorName = postInfo.get( DataType.AUTHOR_NAME );
		authorImage = postInfo.get( DataType.AUTHOR_IMAGE );
		content = postInfo.get( DataType.POST_CONTENT );
		comments = postInfo.get( DataType.COMMENTS );
	}

	private void setPostID( String postID ) {
		this.postID = postID;
		saveToStorage();
	}

	private void saveToStorage() {
		if( isFollowed() ) {
			saveValue( ValueType.POST_ID, postID );
			saveValue( ValueType.TITLE, title );		
			saveValue( ValueType.SUMMARY, summaryText );
			saveValue( ValueType.IS_FOLLOWED, String.valueOf( isFollowed ) );
			if( lastViewedModificationTime != null )
				saveValue( ValueType.LAST_VIEWED_MODIFICATION_TIME, lastViewedModificationTime.toStringRfc3339() );
		}
	}
	
	private void saveValue( ValueType type, String value ) {
		if( value != null )
			storage.saveValueByKeyAndType( value, url, type );
	}

	private void setTitle( String title ) {
		this.title = title;
		saveToStorage();
	}

	private void setSummary( String summaryText ) {
		this.summaryText = summaryText;
		saveToStorage();
	}
	
	public void follow() {
		isFollowed = true;
		saveToStorage();
	}

	public void unfollow() {
		isFollowed = false;
		storage.remove( url );
	}
	
	public boolean isFollowed() {
		return isFollowed;
	}

	public void viewed() {
		lastViewedModificationTime = currentModificationTime;
		saveToStorage();
	}

	public void show( ViewPostIfc view ) {
		this.view = view;
		view.activityStarted();
		view.setTitle( title );
		googlePlus.getPostInformation( this, new GooglePlusQuery( postID, authorID, url ), requestID++ );
	}

	@Override
	public void postInformationResults( Map<DataType, String> postInfo, int requestID ) {
		parsePostInfo( postInfo );
		updateView( view );
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
	public void postInformationError( String errorText, int requestID ) {
		view.showError( errorText );
		view.activityStopped();
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

	@Override
	public void searchResults( List<Map<DataType, String>> results ) {
	}
}
