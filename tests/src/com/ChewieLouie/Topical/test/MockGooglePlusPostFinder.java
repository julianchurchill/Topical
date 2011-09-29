package com.ChewieLouie.Topical.test;

import java.util.List;

import com.ChewieLouie.Topical.GooglePlusPostFinderIfc;
import com.google.api.services.customsearch.model.Result;

public class MockGooglePlusPostFinder implements GooglePlusPostFinderIfc {

	// A random URL - while valid in syntax doesn't actually point to a valid post/user
//	private static final String testURL = "https://plus.google.com/u/0/11361562123432432532128/posts/cFJH2197HuihTuU";

	public MockGooglePlusPostFinder()
	{
	}
	
	@Override
	public List<Result> search( String searchText ) {
		return null;
	}
}
