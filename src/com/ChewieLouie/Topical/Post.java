package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.google.api.client.util.DateTime;

import android.os.AsyncTask;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	private String authorID = null;
	private GooglePlusIfc googlePlus = GooglePlusFactory.create();
	private boolean isFollowed = false;
	private Map<DataType, String> postInfo = null;
	private PersistentStorageIfc storage = null;
	private String postID = "";
	private String url = "";
	private String title = "";
	private String summaryText = "";
	private DateTime lastViewedModificationTime = null;
	private DateTime currentModificationTime = null;

	public Post( String url ) {
		loadData( url );
		this.title = storage.load( url, ValueType.TITLE );
		this.summaryText = storage.load( url, ValueType.SUMMARY );
	}

	public Post( String title, String summaryText, String url )	{
		loadData( url );
		this.title = title;
		this.summaryText = summaryText;
		storage.save( url, ValueType.TITLE, title );
		storage.save( url, ValueType.SUMMARY, summaryText );
	}
	
	private void loadData( String url ) {
		this.storage = PersistentStorageFactory.create();
		setUrl( url );
		this.isFollowed = Boolean.parseBoolean( storage.load( url, ValueType.IS_FOLLOWED ) );
		this.postID = storage.load( url, ValueType.POST_ID );
		String modTime = storage.load( url, ValueType.LAST_VIEWED_MODIFICATION_TIME );
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
		storage.save( url, ValueType.IS_FOLLOWED, String.valueOf( isFollowed ) );
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void show( ViewPostIfc viewPost ) {
		new GetPostInformationTask( viewPost ).execute();
	}

	public void viewed() {
		lastViewedModificationTime = currentModificationTime;
		storage.save( url, ValueType.LAST_VIEWED_MODIFICATION_TIME, lastViewedModificationTime.toStringRfc3339() );
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
			viewPost.setSummaryText( summaryText );
			viewPost.activityStopped();
			String lastViewedModificationTime = postInfo.get( DataType.MODIFICATION_TIME );
			if( lastViewedModificationTime != null )
				currentModificationTime = DateTime.parseRfc3339( lastViewedModificationTime );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			viewPost.activityStarted();
			viewPost.setTitle( title );
		}

		private void retrieveRemoteInformation() throws IOException {
			if( postInfo == null )
			{
				if( postID.equals( "" ) )
				{
					postInfo = googlePlus.getPostInformation( authorID, url );
					postID = postInfo.get( DataType.POST_ID );
					storage.save( url, ValueType.POST_ID, postID );
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
			if( lastViewedModificationTime != null && currentModificationTime != null ) {
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