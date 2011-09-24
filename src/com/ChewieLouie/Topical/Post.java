package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;

import android.os.AsyncTask;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	public String title = "";
	public String text = "";
	public String url = "";

	private String authorID = null;
	private GooglePlusIfc googlePlus = GooglePlusFactory.create();
	private boolean isFollowed = false;
	private Map<DataType, String> postInfo = null;
	private PersistentStorageIfc storage = null;
	private static final String StorageKey_IsFollowed = "IsFollowed";
	private String isFollowedStorageKey = null;
	
	public Post( String title, String text, String url )
	{
		this.storage = PersistentStorageFactory.create();
		this.title = title;
		this.text = text;
		setUrl( url );
		isFollowedStorageKey = StorageKey_IsFollowed + url;
		this.isFollowed = Boolean.parseBoolean( storage.load( isFollowedStorageKey ) );
	}

	public Status getStatus() {
		if( isFollowed )
			return Status.FOLLOWING_AND_NOT_CHANGED;
		return Status.NEW;
	}
	
	public void retrieveRemoteInformation() throws IOException {
		if( postInfo == null )
		{
			postInfo = googlePlus.getPostInformation( authorID, url );
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
			viewPost.setComments( "Comments: " + comments() );
			viewPost.activityStopped();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			viewPost.activityStarted();
		}
    }
}