package com.ChewieLouie.Topical;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import android.os.AsyncTask;

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
		viewPost.activityStarted();
		viewPost.setTitle( title );

		new ShowTask( viewPost ).execute();
//		this.viewPost = viewPost;
//		googlePlus.getPostInformation( Post.this, postID, authorID, url );
	}

	class ShowTask extends AsyncTask<Void, Void, Void> {
		private ViewPostIfc view = null;
		private boolean updateView = false;

		public ShowTask( ViewPostIfc view ) {
			this.view = view;
		}

		@Override
		protected Void doInBackground( Void... params ) {
			protectView.acquireUninterruptibly();
			viewPost = view;
			if( needsLoadingFromGooglePlus ) {
				googlePlus.getPostInformation( Post.this, postID, authorID, url );
				needsLoadingFromGooglePlus = false;
			}
			else
				updateView = true;
			return null;
		}

		@Override
		protected void onPostExecute( Void noArg ) {
			super.onPostExecute( noArg );
			if( updateView )
				updateViewFromPostInfo();
		}
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
			storage.save( url, ValueType.POST_ID, postID );
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