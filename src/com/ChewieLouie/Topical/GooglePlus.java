package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.ArrayList;
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
			callCallbackObj( query.makeKeyFromAuthorAndURL(), callbackObj, null, requestID );
		else
			new GetPostTask( callbackObj, requestID, query ).execute();
	}

	@Override
	public List< Map<DataType,String> > search( String searchText ) {
		List< Map<DataType,String> > results = new ArrayList< Map<DataType,String> >();
		Plus.Activities.Search request = plus.activities.search();
		request.setQuery( searchText );
		final int maxResults = 20;
		int totalResultsParsed = 0;
		ActivityFeed feed = null;
		try {
			do {
				feed = request.execute();
				request.setPageToken( feed.getNextPageToken() );
				for( Activity activity : feed.getItems() )
					results.add( GooglePlus.extractDataFromActivity( activity ) );
				totalResultsParsed += feed.getItems().size();
			} while( totalResultsParsed < maxResults && feed.getNextPageToken() != null );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
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
			}
    		else {
    			queriesInProgress.put( queryKey, new Semaphore( 0 ) );
    			queriesInProgressSema4.release();
				postInfoCache.put( queryKey, extractDataFromActivity( findActivity( query ) ) );
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
				if( query.postIDIsValid() )
					return plus.activities.get( query.postID ).execute();
				else if( query.authorIDIsValid() && query.urlIsValid() )
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
			callCallbackObj( queryKey, callbackObj, errorText, requestID );
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
    }
	
	private void callCallbackObj( String queryKey, GooglePlusCallbackIfc callbackObj, String errorText, int requestID ) {
		Map<DataType, String> postInfo = postInfoCache.get( queryKey );
		if( postInfo == null && errorText == null )
			errorText = "No Google Plus post found";
		if( errorText != null )
			callbackObj.postInformationError( errorText, requestID );
		else
			callbackObj.postInformationResults( postInfo, requestID );
	}

	public static Map<DataType, String> extractDataFromActivity( Activity activity ) {
		Map<DataType, String> values = null;
		if( activity != null ) {
			values = new HashMap<DataType, String>();
			putString( values, DataType.AUTHOR_ID, activity.getActor().getId() );
			putString( values, DataType.AUTHOR_IMAGE, activity.getActor().getImage().getUrl() );
			final String authorName = activity.getActor().getDisplayName();
			putString( values, DataType.AUTHOR_NAME, authorName );
			putString( values, DataType.COMMENTS, activity.getPlusObject().getReplies().toString() );
			if( activity.getUpdated() != null )
				putString( values, DataType.MODIFICATION_TIME, activity.getUpdated().toStringRfc3339() );
			else
				putString( values, DataType.MODIFICATION_TIME, activity.getPublished().toStringRfc3339() );
			putString( values, DataType.POST_CONTENT, activity.getPlusObject().getContent() );
			putString( values, DataType.POST_ID, activity.getId() );
			putString( values, DataType.SUMMARY, activity.getTitle() );
			putString( values, DataType.TITLE, authorName );
			putString( values, DataType.URL, activity.getUrl() );
		}
		return values;
	}

	private static void putString( Map<DataType, String> values, DataType type, String value ) {
		if( value == null )
			values.put( type, "" );
		else
			values.put( type, value );
	}
}
