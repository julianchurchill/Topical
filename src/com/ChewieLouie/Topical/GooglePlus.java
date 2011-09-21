package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.List;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;

public class GooglePlus implements GooglePlusIfc {

	private static GooglePlus myPlus = null;
	
	public static GooglePlus Make() {
		if( myPlus == null )
		{
			myPlus = new GooglePlus();
		}
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
	public String getAuthor( String authorID ) throws IOException {
		String author = "";
		Person person = plus.people.get( authorID ).execute();
		if( person != null )
		{
			author = person.getDisplayName();
		}
		return author;
	}

	@Override
	public String getPostContent( String authorID, String url ) throws IOException {
		return getActivityInformation( authorID, url, 
			new ActivityInformationRetrieverIfc() {
				@Override
				public String retrieve(Activity activity) {
					return activity.getPlusObject().getContent();
				}
			});
	}

	@Override
	public String getComments( String authorID, String url ) throws IOException {
		return getActivityInformation( authorID, url, 
			new ActivityInformationRetrieverIfc() {
				@Override
				public String retrieve(Activity activity) {
					return activity.getPlusObject().getReplies().toString();
				}
			});
	}
	
	private interface ActivityInformationRetrieverIfc {
		public String retrieve( Activity activity );
	}
	
	private String getActivityInformation( String authorID, String url, ActivityInformationRetrieverIfc command ) throws IOException {
		String information = "";
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
			information = command.retrieve( foundActivity );
		}
		return information;
	}
}
