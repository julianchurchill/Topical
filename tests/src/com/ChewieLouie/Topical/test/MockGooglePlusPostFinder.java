package com.ChewieLouie.Topical.test;

import java.util.ArrayList;
import java.util.List;

import com.ChewieLouie.Topical.GooglePlusPostFinderIfc;
import com.ChewieLouie.Topical.Post;

public class MockGooglePlusPostFinder implements GooglePlusPostFinderIfc {

	// A random URL - while valid in syntax doesn't actually point to a valid post/user
	private static final String testURL = "https://plus.google.com/u/0/11361562123432432532128/posts/cFJH2197HuihTuU";
	private static List<Post> testSearchResults = null;
	
	public MockGooglePlusPostFinder()
	{
	}
	
	@Override
	public List<Post> search( String searchText ) {
		if( testSearchResults == null )
		{
			testSearchResults = new ArrayList<Post>();
			testSearchResults.add( createPost( testURL, "Some title", "test snippet 1" ) );
			testSearchResults.add( createPost( testURL, "Another result", "test snippet 2" ) );
			testSearchResults.add( createPost( testURL, "And another one", "test snippet 3" ) );
		}
		return testSearchResults;
	}
	
	private Post createPost( String url, String title, String summary ) {
		Post post = new Post( url );
		post.setTitle( title );
		post.setSummary( summary );
		return post;
	}
}
