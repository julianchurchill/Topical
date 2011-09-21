package com.ChewieLouie.Topical.Test;

import com.ChewieLouie.Topical.GooglePlusIfc;

public class TestGooglePlus implements GooglePlusIfc {

	@Override
	public String getAuthor( String postID ) {
		return "An popular authors name";
	}

	@Override
	public String getPostContent( String authorID, String url ) {
		return "Some random content - this needs to be generated from a lookup with the Google Pluc Ifc";
	}

	@Override
	public String getComments( String authorID, String url ) {
		return "Many comments, they just go on and on";
	}

}
