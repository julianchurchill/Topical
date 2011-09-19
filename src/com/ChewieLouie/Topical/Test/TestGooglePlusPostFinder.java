package com.ChewieLouie.Topical.Test;

import java.util.ArrayList;
import java.util.List;

import com.ChewieLouie.Topical.GooglePlusPostFinderIfc;
import com.ChewieLouie.Topical.Post;

public class TestGooglePlusPostFinder implements GooglePlusPostFinderIfc {

	private static List<Post> testSearchResults = new ArrayList<Post>();
	
	public TestGooglePlusPostFinder()
	{
		testSearchResults.add( new Post( "Some text", "test snippet 1" ) );
		testSearchResults.add( new Post( "Another result", "test snippet 2" ) );
		testSearchResults.add( new Post( "And another one", "test snippet 3" ) );
	}
	
	@Override
	public List<Post> search( String searchText ) {
		return testSearchResults;
	}
}
