package com.ChewieLouie.Topical.Test;

import java.util.ArrayList;
import java.util.List;

import com.ChewieLouie.Topical.GooglePlusPostFinderIfc;
import com.ChewieLouie.Topical.Post;

public class TestGooglePlusPostFinder implements GooglePlusPostFinderIfc {

	// A random URL - while valid in syntax doesn't actually point to a valid post/user
	private static final String testURL = "https://plus.google.com/u/0/11361562123432432532128/posts/cFJH2197HuihTuU";
	private static List<Post> testSearchResults = new ArrayList<Post>();
	
	public TestGooglePlusPostFinder()
	{
		testSearchResults.add( new Post( "Some text", "test snippet 1", testURL ) );
		testSearchResults.add( new Post( "Another result", "test snippet 2", testURL ) );
		testSearchResults.add( new Post( "And another one", "test snippet 3", testURL ) );
	}
	
	@Override
	public List<Post> search( String searchText ) {
		return testSearchResults;
	}
}
