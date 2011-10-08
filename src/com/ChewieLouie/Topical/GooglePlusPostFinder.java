package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class GooglePlusPostFinder implements GooglePlusPostFinderIfc {
	private static final String googleAPIKey = "AIzaSyA1UkMy-J3PbX6ozp22P0KbV7XApguSb7s";
	private static final String appName = "Topical";

	private Plus plus = null;

	public GooglePlusPostFinder() {
		plus = new Plus( appName, new NetHttpTransport(), new GsonFactory() );
		plus.setKey( googleAPIKey );
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
}
