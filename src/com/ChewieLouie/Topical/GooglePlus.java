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

	public Map<DataType, String> getPostInformation( String authorID, String url ) throws IOException {
		Map<DataType, String> values = new HashMap<DataType, String>();
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
		if( foundActivity != null )
		{
			values.put( DataType.AUTHOR_NAME, foundActivity.getActor().getDisplayName() );
			values.put( DataType.AUTHOR_IMAGE, foundActivity.getActor().getImage().getUrl() );
			values.put( DataType.POST_CONTENT, foundActivity.getPlusObject().getContent() );
			values.put( DataType.COMMENTS, foundActivity.getPlusObject().getReplies().toString() );
		}
		return values;
	}
}
