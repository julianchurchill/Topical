package com.ChewieLouie.Topical.Test;

import java.io.IOException;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc;

public class TestGooglePlus implements GooglePlusIfc {

	@Override
	public Map<DataType, String> getPostInformationByPostID( String postID ) throws IOException {
		return null;
	}

	@Override
	public Map<DataType, String> getPostInformation( String authorID, String url ) throws IOException {
		return null;
	}
}
