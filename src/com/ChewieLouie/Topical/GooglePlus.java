package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

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
	private Map<String, Map<DataType, String>> postInfoCache = new HashMap<String, Map<DataType, String>>();
	private Map<String, Semaphore> queriesInProgress = new HashMap<String, Semaphore>();
	private Semaphore queriesInProgressSema4 = new Semaphore( 1 );

	private GooglePlus() {
		plus = new Plus( appName, new NetHttpTransport(), new GsonFactory() );
		plus.setKey( googleAPIKey );
	}

	public void getPostInformation( GooglePlusCallbackIfc callbackObj, GooglePlusQuery query, int requestID ) {
		if( postInfoCache.containsKey( query.makeKeyFromAuthorAndURL() ) )
			callbackObj.postInformationResults( postInfoCache.get( query.makeKeyFromAuthorAndURL() ), requestID );
		else
			new GetPostTask( callbackObj, requestID, query ).execute();
	}

	private class GetPostTask extends AsyncTask<Void, Void, Void> {
    	private String errorText = null;
    	private GooglePlusQuery query = null;
    	private GooglePlusCallbackIfc callbackObj = null;
    	private int requestID;
    	private String queryKey = "";
    	
    	public GetPostTask( GooglePlusCallbackIfc callbackObj, int requestID, GooglePlusQuery query ) {
    		this.callbackObj = callbackObj;
    		this.requestID = requestID;
    		this.query = query;
    		queryKey = query.makeKeyFromAuthorAndURL();
    	}

    	@Override
		protected Void doInBackground( Void... params ) {
			queriesInProgressSema4.acquireUninterruptibly();
			if( isQueryInProgress() ) {
    			queriesInProgressSema4.release();
				waitForQueryToComplete();
        		if( postInfoCache.containsKey( queryKey ) == false )
    				errorText = "No Google Plus post found";
			}
    		else {
    			queriesInProgress.put( queryKey, new Semaphore( 0 ) );
    			queriesInProgressSema4.release();
    			Activity activity = findActivity( query );
    			if( activity != null )
    				postInfoCache.put( queryKey, extractDataFromActivity( activity ) );
    			else if( errorText == null )
    				errorText = "No Google Plus post found";
				queriesInProgress.get( queryKey ).release();
    		}
    		return null;
		}

    	private boolean isQueryInProgress() {
    		return queriesInProgress.containsKey( queryKey );
    	}
    	
    	private void waitForQueryToComplete() {
			queriesInProgress.get( queryKey ).acquireUninterruptibly();
    	}

    	private Activity findActivity( GooglePlusQuery query ) {
    		try {
				if( query.postID != null )
					return plus.activities.get( query.postID ).execute();
				else if( query.authorID != null && query.url != null )
					return findActivityByAuthorAndURL();
			} catch (IOException e) {
				e.printStackTrace();
				errorText = e.getMessage();
			}
			return null;
    	}
   
    	private Activity findActivityByAuthorAndURL() throws IOException {
			Plus.Activities.List request = plus.activities.list( query.authorID, collectionPublic );
			ActivityFeed activityFeed = request.execute();
			List<Activity> activitiesByAuthor = activityFeed.getItems();
			while( activitiesByAuthor != null ) {
				Activity foundActivity = findActivityByURL( query.url, activitiesByAuthor );
				if( foundActivity != null )
					return foundActivity;
				if( moreActivitiesAvailable( activityFeed ) )
					activitiesByAuthor = getMoreActivities( request, activityFeed );
				else
					break;
			}
			return null;
    	}
    	
    	private Activity findActivityByURL( String url, List<Activity> activities ) {
			for( Activity activity : activities )
				if( activity.getUrl().equals( url ) )
					return activity;
			return null;
    	}

		private List<Activity> getMoreActivities( Plus.Activities.List request,ActivityFeed feed )
				throws IOException {
			request.setPageToken( feed.getNextPageToken() );
			return request.execute().getItems();
		}
		
		private boolean moreActivitiesAvailable( ActivityFeed feed ) {
			return feed.getNextPageToken() != null;
		}

		@Override
		protected void onPostExecute( Void voidArg ) {
			super.onPostExecute( voidArg );
			if( errorText != null )
				callbackObj.postInformationError( errorText, requestID );
			else
				callbackObj.postInformationResults( postInfoCache.get( queryKey ), requestID );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
    }

	private Map<DataType, String> extractDataFromActivity( Activity activity ) {
		Map<DataType, String> values = new HashMap<DataType, String>();
		if( activity != null ) {
			putString( values, DataType.AUTHOR_NAME, activity.getActor().getDisplayName() );
			putString( values, DataType.AUTHOR_IMAGE, activity.getActor().getImage().getUrl() );
			putString( values, DataType.POST_CONTENT, activity.getPlusObject().getContent() );
			putString( values, DataType.COMMENTS, activity.getPlusObject().getReplies().toString() );
			putString( values, DataType.POST_ID, activity.getId() );
			if( activity.getUpdated() != null )
				putString( values, DataType.MODIFICATION_TIME, activity.getUpdated().toStringRfc3339() );
			else
				putString( values, DataType.MODIFICATION_TIME, activity.getPublished().toStringRfc3339() );
		}
		return values;
	}

	private void putString( Map<DataType, String> values, DataType type, String value ) {
		if( value == null )
			values.put( type, "" );
		else
			values.put( type, value );
	}
}
