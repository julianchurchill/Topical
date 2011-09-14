package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.List;

public class GooglePlusIfc {

	private static List<Post> testSearchResults = new ArrayList<Post>();
	
	public GooglePlusIfc()
	{
		testSearchResults.add( new Post( "Some text" ) );
		testSearchResults.add( new Post( "Another result" ) );
		testSearchResults.add( new Post( "And another one" ) );
	}
	
	public List<Post> search( String searchText ) {
		return testSearchResults;
	}
}
