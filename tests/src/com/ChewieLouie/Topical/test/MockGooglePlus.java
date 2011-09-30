package com.ChewieLouie.Topical.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc;

public class MockGooglePlus implements GooglePlusIfc {

	Map<DataType, String> postInformation = new HashMap<DataType, String>();

	@Override
	public Map<DataType, String> getPostInformationByPostID( String postID ) throws IOException {
		return postInformation;
	}

	@Override
	public Map<DataType, String> getPostInformation( String authorID, String url ) throws IOException {
		return postInformation;
	}
}
