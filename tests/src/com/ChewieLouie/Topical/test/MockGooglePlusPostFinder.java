package com.ChewieLouie.Topical.test;


import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.GooglePlusPostFinderIfc;

public class MockGooglePlusPostFinder implements GooglePlusPostFinderIfc {

	// A random URL - while valid in syntax doesn't actually point to a valid post/user
//	private static final String testURL = "https://plus.google.com/u/0/11361562123432432532128/posts/cFJH2197HuihTuU";

	public MockGooglePlusPostFinder()
	{
	}
	
	@Override
	public List< Map<DataType,String> > search( String searchText ) {
		return null;
	}
}
