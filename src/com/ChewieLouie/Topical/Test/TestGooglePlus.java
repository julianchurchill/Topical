package com.ChewieLouie.Topical.Test;

import com.ChewieLouie.Topical.GooglePlusIfc;

public class TestGooglePlus implements GooglePlusIfc {

	@Override
	public String getPostContent( String postID ) {
		return "Some random content - this needs to be generated from a lookup with the Google Pluc Ifc";
	}

}
