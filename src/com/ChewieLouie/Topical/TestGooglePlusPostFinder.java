package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.List;

public class TestGooglePlusPostFinder implements GooglePlusPostFinderIfc {

	private static List<Post> testSearchResults = new ArrayList<Post>();
	
	public TestGooglePlusPostFinder()
	{
		testSearchResults.add( new Post( "Some text" ) );
		testSearchResults.add( new Post( "Another result" ) );
		testSearchResults.add( new Post( "And another one" ) );
	}
	
	@Override
	public List<Post> search( String searchText ) {
		return testSearchResults;
	}

	@Override
	public String getPostContent( String postID ) {
		return "Some test post content";
	}
}
