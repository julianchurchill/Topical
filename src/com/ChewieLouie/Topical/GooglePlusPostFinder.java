package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class GooglePlusPostFinder implements GooglePlusPostFinderIfc {
	
	private static final String googleAPIKey = "AIzaSyA1UkMy-J3PbX6ozp22P0KbV7XApguSb7s";
	// This is the value of the 'cx' parameter for a search query - see my custom search engines
	private static final String customSearchEngineID = "007835778103156738196:6m3_5zp5pye";
	      	
	private Customsearch customSearch = null;
	
	public GooglePlusPostFinder()	{
		customSearch = new Customsearch( new NetHttpTransport(), new GsonFactory() );
		customSearch.setKey( googleAPIKey );
	}

	@Override
	public List<Post> search( String searchText ) {
		List<Post> posts = new ArrayList<Post>();
		String query = searchText;
		Customsearch.Cse.List request = customSearch.cse.list( query );
		request.setCx( customSearchEngineID );
		List<Result> results = null;
		try {
			Search searchResult = request.execute();
			if( searchResult != null )
			{
				results = searchResult.getItems();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if( results != null )
		{
			for( Result result : results )
			{
				posts.add( new Post( result.getTitle() ) );
			}
		}
		return posts;
	}

	@Override
	public String getPostContent( String postID ) {
		return null;
	}
}
