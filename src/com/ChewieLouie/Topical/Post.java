package com.ChewieLouie.Topical;

import java.io.IOException;

import android.os.AsyncTask;

public class Post {
	public enum Status { NEW, FOLLOWING_AND_NOT_CHANGED, FOLLOWING_AND_HAS_CHANGED };

	public String title = "";
	public String text = "";
	public String url = "";
	public String author = null;
	public String content = null;
	public String comments = null;
	public String imageURL = null;

	private String authorID = null;
	private GooglePlusIfc googlePlus = GooglePlusFactory.create();
	private boolean isFollowed = false;
	
	public Post( String title, String text, String url )
	{
		this.title = title;
		this.text = text;
		setUrl( url );
	}

	public Status getStatus() {
		if( isFollowed )
			return Status.FOLLOWING_AND_NOT_CHANGED;
		return Status.NEW;
	}
	
	public void retrieveRemoteInformation() throws IOException {
		if( author == null )
			author = googlePlus.getAuthor( authorID );
		if( content == null )
			content = googlePlus.getPostContent( authorID, url );
		if( comments == null )
			comments = googlePlus.getComments( authorID, url );
		if( imageURL == null )
			imageURL = googlePlus.getImageURL( authorID );
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
		isFollowed = true;
	}

	public void unfollow() {
		isFollowed = false;
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
			viewPost.setAuthor( author );
			viewPost.setAuthorImage( imageURL );
			viewPost.setHTMLContent( content );
			viewPost.setComments( "Comments: " + comments );
			viewPost.activityStopped();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			viewPost.activityStarted();
		}
    }
}