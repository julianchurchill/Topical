package com.ChewieLouie.Topical.test;

import java.util.HashMap;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusCallbackIfc;
import com.ChewieLouie.Topical.GooglePlusIfc;

public class MockGooglePlus implements GooglePlusIfc {

	Map<DataType, String> postInformation = new HashMap<DataType, String>();
	public boolean getPostInformationCalled = false;
	public GooglePlusCallbackIfc getPostInformationArgsCallbackObj = null;
	public String getPostInformationArgsPostID = null;
	public String getPostInformationArgsAuthorID = null;
	public String getPostInformationArgsURL = null;

	@Override
	public void getPostInformation(GooglePlusCallbackIfc callbackObj, String postID, String authorID, String url) {
		getPostInformationCalled = true;
		getPostInformationArgsCallbackObj = callbackObj;
		getPostInformationArgsPostID = postID;
		getPostInformationArgsAuthorID = authorID;
		getPostInformationArgsURL = url;
		callbackObj.postInformationResults( postInformation );
	}
}
