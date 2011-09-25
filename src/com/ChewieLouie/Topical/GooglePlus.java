package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<DataType, String> getPostInformationByPostID(String postID) throws IOException {
		Plus.Activities.Get request = plus.activities.get( postID ); 
		return extractDataFromActivity( request.execute() );
	}

	public Map<DataType, String> getPostInformation( String authorID, String url ) throws IOException {
		Activity foundActivity = null;
		Plus.Activities.List request = plus.activities.list( authorID, collectionPublic );
		ActivityFeed activityFeed = request.execute();
		List<Activity> activities = activityFeed.getItems();
		while( activities != null )
		{
			for( Activity activity : activities )
			{
				if( activity.getUrl().equals( url ) )
				{
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
		return extractDataFromActivity( foundActivity );
	}
	
	private Map<DataType, String> extractDataFromActivity( Activity activity ) {
		Map<DataType, String> values = new HashMap<DataType, String>();
		if( activity != null )
		{
			values.put( DataType.AUTHOR_NAME, activity.getActor().getDisplayName() );
			values.put( DataType.AUTHOR_IMAGE, activity.getActor().getImage().getUrl() );
			values.put( DataType.POST_CONTENT, activity.getPlusObject().getContent() );
			values.put( DataType.COMMENTS, activity.getPlusObject().getReplies().toString() );
			values.put( DataType.POST_ID, activity.getId() );
			values.put( DataType.MODIFICATION_TIME, activity.getUpdated().toStringRfc3339() );
		}
		return values;
	}
}
