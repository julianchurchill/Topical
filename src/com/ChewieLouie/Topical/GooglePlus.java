package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class GooglePlus implements GooglePlusIfc {

	private static GooglePlus myPlus = null;
	
	public static GooglePlus Make() {
		if( myPlus == null )
			myPlus = new GooglePlus();
		return myPlus;
	}

	private static final String googleAPIKey = "AIzaSyA1UkMy-J3PbX6ozp22P0KbV7XApguSb7s";
	private static final String appName = "Topical";
	private static final String collectionPublic = "public";

	private Plus plus = null;

	private GooglePlus() {
		plus = new Plus( appName, new NetHttpTransport(), new GsonFactory() );
		plus.setKey( googleAPIKey );
	}

	@Override
	public void getPostInformationByPostID( GooglePlusCallbackIfc callbackObj, String postID ) {
		GetPostInformationTask task = new GetPostInformationTask( callbackObj );
		task.setPostID( postID );
		task.execute();
	}

	public void getPostInformation( GooglePlusCallbackIfc callbackObj, String authorID, String url ) {
		GetPostInformationTask task = new GetPostInformationTask( callbackObj );
		task.setAuthorID( authorID );
		task.setURL( url );
		task.execute();
	}

	private class GetPostInformationTask extends AsyncTask<Void, Void, Boolean> {
    	private String errorText = null;
    	private String postID = null;
    	private String authorID = null;
    	private String url = null;
    	private Map<DataType, String> postInfo = new HashMap<DataType, String>();
    	private GooglePlusCallbackIfc callbackObj = null;
    	
    	public GetPostInformationTask( GooglePlusCallbackIfc callbackObj ) {
    		this.callbackObj = callbackObj;
    	}

    	public void setPostID( String postID ) {
    		this.postID = postID;
    	}
    	
    	public void setAuthorID( String authorID ) {
    		this.authorID = authorID;
    	}

    	public void setURL( String url ) {
    		this.url = url;
    	}

    	@Override
		protected Boolean doInBackground( Void... params ) {
    		try {
    			Activity activity = null;
    			if( postID != null )
    				activity = plus.activities.get( postID ).execute();
    			else if( authorID != null && url != null )
    				activity = findActivityByAuthorAndURL();
    			if( activity != null )
    				postInfo = extractDataFromActivity( activity );
    			else
    				errorText = "No Google Plus post found";
			} catch (IOException e) {
				e.printStackTrace();
				errorText = e.getMessage();
				return false;
			}
    		return true;
		}
   
    	private Activity findActivityByAuthorAndURL() throws IOException {
			Activity foundActivity = null;
			Plus.Activities.List request = plus.activities.list( authorID, collectionPublic );
			ActivityFeed activityFeed = request.execute();
			List<Activity> activities = activityFeed.getItems();
			while( activities != null ) {
				for( Activity activity : activities ) {
					if( activity.getUrl().equals( url ) ) {
						foundActivity = activity;
						break;
					}
				}
				if( foundActivity != null || activityFeed.getNextPageToken() == null )
					break;
				request.setPageToken( activityFeed.getNextPageToken() );
				activityFeed = request.execute();
				activities = activityFeed.getItems();
			}
			return foundActivity;
    	}

		@Override
		protected void onPostExecute( Boolean postPopulatedOk ) {
			super.onPostExecute( postPopulatedOk );
			if( errorText != null )
				callbackObj.postInformationError( errorText );
			else
				callbackObj.postInformationResults( postInfo );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
    }

	private Map<DataType, String> extractDataFromActivity( Activity activity ) {
		Map<DataType, String> values = new HashMap<DataType, String>();
		if( activity != null ) {
			putStringButDefaultToBlankIfNull( values, DataType.AUTHOR_NAME, activity.getActor().getDisplayName() );
			putStringButDefaultToBlankIfNull( values, DataType.AUTHOR_IMAGE, activity.getActor().getImage().getUrl() );
			putStringButDefaultToBlankIfNull( values, DataType.POST_CONTENT, activity.getPlusObject().getContent() );
			putStringButDefaultToBlankIfNull( values, DataType.COMMENTS, activity.getPlusObject().getReplies().toString() );
			putStringButDefaultToBlankIfNull( values, DataType.POST_ID, activity.getId() );
			if( activity.getUpdated() != null )
				putStringButDefaultToBlankIfNull( values, DataType.MODIFICATION_TIME,
					activity.getUpdated().toStringRfc3339() );
			else
				putStringButDefaultToBlankIfNull( values, DataType.MODIFICATION_TIME,
					activity.getPublished().toStringRfc3339() );
		}
		return values;
	}

	private void putStringButDefaultToBlankIfNull( Map<DataType, String> values, DataType type, String value ) {
		if( value == null )
			values.put( type, "" );
		else
			values.put( type, value );
	}
}
