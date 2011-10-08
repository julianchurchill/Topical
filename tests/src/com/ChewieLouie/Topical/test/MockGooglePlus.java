package com.ChewieLouie.Topical.test;

import java.util.HashMap;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusCallbackIfc;
import com.ChewieLouie.Topical.GooglePlusIfc;
import com.ChewieLouie.Topical.GooglePlusQuery;

public class MockGooglePlus implements GooglePlusIfc {

	Map<DataType, String> postInformation = new HashMap<DataType, String>();
	public boolean getPostInformationCalled = false;
	public GooglePlusCallbackIfc getPostInformationArgsCallbackObj = null;
	public GooglePlusQuery getPostInformationArgsQuery = null;

	@Override
	public void getPostInformation(GooglePlusCallbackIfc callbackObj, GooglePlusQuery query, int requestID) {
		getPostInformationCalled = true;
		getPostInformationArgsCallbackObj = callbackObj;
		getPostInformationArgsQuery = query;
		callbackObj.postInformationResults( postInformation, requestID );
	}
}
