package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class CustomSearchGooglePlusPostFinder implements GooglePlusPostFinderIfc {
	
	private static final String googleAPIKey = "AIzaSyA1UkMy-J3PbX6ozp22P0KbV7XApguSb7s";
	// This is the value of the 'cx' parameter for a search query - see my custom search engines
	private static final String customSearchEngineID = "007835778103156738196:6m3_5zp5pye";

	private Customsearch customSearch = null;
	
	public CustomSearchGooglePlusPostFinder() {
		customSearch = new Customsearch( new NetHttpTransport(), new GsonFactory() );
		customSearch.setKey( googleAPIKey );
	}

	@Override
	public List< Map<DataType,String> > search( String searchText ) {
		if( searchText.isEmpty() == false ) {
			String query = searchText;
			Customsearch.Cse.List request = customSearch.cse.list( query );
			request.setCx( customSearchEngineID );
			try {
				Search searchResult = request.execute();
				if( searchResult != null )
					return convertResultsToPostInfo( searchResult.getItems() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private List< Map<DataType, String> > convertResultsToPostInfo( List<Result> results ) {
		List< Map<DataType, String> > postInfos = new ArrayList< Map<DataType, String> >();
		for( Result result : results ) {
			Map<DataType, String> postInfo = new HashMap<DataType, String>();
			postInfo.put( DataType.URL, result.getLink() );
			postInfo.put( DataType.TITLE, result.getTitle() );
			postInfo.put( DataType.SUMMARY, result.getSnippet() );
			postInfos.add( postInfo );
		}
		return postInfos;
	}
}
