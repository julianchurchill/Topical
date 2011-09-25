package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.google.api.client.util.DateTime;

import android.os.AsyncTask;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	private String authorID = null;
	private GooglePlusIfc googlePlus = GooglePlusFactory.create();
	private boolean isFollowed = false;
	private Map<DataType, String> postInfo = null;
	private PersistentStorageIfc storage = null;
	private static final String StorageKey_IsFollowed = "IsFollowed";
	private String isFollowedStorageKey = null;
	private static final String StorageKey_PostID = "PostID";
	private String postIDStorageKey = null;
	private String postID = "";
	private String title = "";
	private String url = "";
	private String summaryText = "";
	private static final String StorageKey_LastViewedModificationTime = "LastViewedModificationTime";
	private String lastViewedModificationTimeStorageKey = null;
	private DateTime lastViewedModificationTime = null;
	private DateTime currentModificationTime = null;
	
	public Post( String title, String summaryText, String url )
	{
		this.storage = PersistentStorageFactory.create();
		this.title = title;
		this.summaryText = summaryText;
		setUrl( url );
		isFollowedStorageKey = StorageKey_IsFollowed + url;
		this.isFollowed = Boolean.parseBoolean( storage.load( isFollowedStorageKey ) );
		postIDStorageKey = StorageKey_PostID + url;
		this.postID = storage.load( postIDStorageKey );
		lastViewedModificationTimeStorageKey = StorageKey_LastViewedModificationTime + url;
		String modTime = storage.load( lastViewedModificationTimeStorageKey );
		if( modTime.equals( "" ) == false )
			this.lastViewedModificationTime = DateTime.parseRfc3339( modTime );
	}

	private void setUrl( String url ) {
		this.url = url;
		extractAuthorIDFromURL( url );
	}
	
	private void extractAuthorIDFromURL( String url ) {
		final String gPlusURLPostsSeperator = "/posts/";
		int authorIDEndIndex = url.indexOf( gPlusURLPostsSeperator );
		if( authorIDEndIndex != -1 )
		{
			int authorIDStartIndex = url.lastIndexOf( "/", authorIDEndIndex-1 ) + 1;
			if( authorIDStartIndex != -1 )
				authorID = url.substring( authorIDStartIndex, authorIDEndIndex );
		}
	}

	public void follow() {
		setFollow( true );
	}

	public void unfollow() {
		setFollow( false );
	}
	
	private void setFollow( boolean follow ) {
		isFollowed = follow;
		storage.save( isFollowedStorageKey, String.valueOf( isFollowed ) );
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void show( ViewPostIfc viewPost ) {
		new GetPostInformationTask( viewPost ).execute();
	}

	public void viewed() {
		lastViewedModificationTime = currentModificationTime;
		storage.save( StorageKey_LastViewedModificationTime, lastViewedModificationTime.toStringRfc3339() );
	}

	private class GetPostInformationTask extends AsyncTask<Void, Void, Boolean> {
    	private String errorText = null;
    	private ViewPostIfc viewPost = null;
    	public GetPostInformationTask( ViewPostIfc viewPost ) {
    		this.viewPost = viewPost;
    	}

    	@Override
		protected Boolean doInBackground( Void... params ) {
    		try {
    			retrieveRemoteInformation();
			} catch (IOException e) {
				e.printStackTrace();
				errorText = e.getMessage();
				return false;
			}
    		return true;
		}

		@Override
		protected void onPostExecute( Boolean postPopulatedOk ) {
			super.onPostExecute( postPopulatedOk );
			if( postPopulatedOk == false )
				viewPost.showError( errorText );
			viewPost.setAuthor( author() );
			viewPost.setAuthorImage( authorImageURL() );
			viewPost.setHTMLContent( content() );
			viewPost.setComments( comments() );
			viewPost.setStatus( status() );
			viewPost.setTitle( title );
			viewPost.setSummaryText( summaryText );
			viewPost.activityStopped();
			currentModificationTime = DateTime.parseRfc3339( postInfo.get( DataType.MODIFICATION_TIME ) );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			viewPost.activityStarted();
		}

		private void retrieveRemoteInformation() throws IOException {
			if( postInfo == null )
			{
				if( postID.equals( "" ) )
				{
					postInfo = googlePlus.getPostInformation( authorID, url );
					postID = postInfo.get( DataType.POST_ID );
					storage.save( postIDStorageKey, postID );
				}
				else
				{
					postInfo = googlePlus.getPostInformationByPostID( postID );
				}
			}
		}

		private String author() {
			return postInfo.get( DataType.AUTHOR_NAME );
		}

		private String authorImageURL() {
			return postInfo.get( DataType.AUTHOR_IMAGE );
		}

		private String content() {
			return postInfo.get( DataType.POST_CONTENT );
		}

		private String comments() {
			return postInfo.get( DataType.COMMENTS );
		}
		
		private boolean isPostModifiedSinceLastView() {
			if( lastViewedModificationTime != null ) {
				Calendar currentModificationCalendar = Calendar.getInstance();
				currentModificationCalendar.setTimeInMillis( currentModificationTime.getValue() );
				if( currentModificationCalendar.after( lastViewedModificationTime ) )
					return true;
			}
			return false;
		}

		private Post.Status status() {
			if( isFollowed )
			{
				if( isPostModifiedSinceLastView() )
					return Post.Status.FOLLOWING_AND_HAS_CHANGED;
				return Post.Status.FOLLOWING_AND_NOT_CHANGED;
			}
			return Post.Status.NEW;
		}
    }
}