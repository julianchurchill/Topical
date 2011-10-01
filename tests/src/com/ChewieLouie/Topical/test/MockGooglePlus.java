package com.ChewieLouie.Topical.test;

import java.util.HashMap;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusCallbackIfc;
import com.ChewieLouie.Topical.GooglePlusIfc;

public class MockGooglePlus implements GooglePlusIfc {

	Map<DataType, String> postInformation = new HashMap<DataType, String>();

	@Override
	public void getPostInformation(GooglePlusCallbackIfc callbackObj, String postID, String authorID, String url) {
		callbackObj.postInformationResults( postInformation );
	}
}
