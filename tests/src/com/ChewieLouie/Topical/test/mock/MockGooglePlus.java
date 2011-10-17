package com.ChewieLouie.Topical.test.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusSearchCallbackIfc;
import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.GooglePlusCallbackIfc;
import com.ChewieLouie.Topical.GooglePlusIfc;
import com.ChewieLouie.Topical.GooglePlusQuery;

public class MockGooglePlus implements GooglePlusIfc {

	public Map<DataType, String> postInformation = new HashMap<DataType, String>();
	public boolean getPostInformationCalled = false;
	public GooglePlusCallbackIfc getPostInformationArgsCallbackObj = null;
	public GooglePlusQuery getPostInformationArgsQuery = null;
	public List<PostComment> comments = new ArrayList<PostComment>();
	public boolean getCommentsCalled = false;
	public GooglePlusCallbackIfc getCommentsArgsCallbackObj = null;
	public String getCommentsArgsPostID = null;
	public boolean searchCalled = false;
	public String searchArgSearchText = "";
	public GooglePlusSearchCallbackIfc searchArgCallbackObj = null;

	@Override
	public void getPostInformation( GooglePlusCallbackIfc callbackObj, GooglePlusQuery query, int requestID ) {
		getPostInformationCalled = true;
		getPostInformationArgsCallbackObj = callbackObj;
		getPostInformationArgsQuery = query;
		callbackObj.postInformationResults( postInformation, requestID );
	}

	@Override
	public void getComments( GooglePlusCallbackIfc callbackObj, String postID ) {
		getCommentsCalled = true;
		getCommentsArgsCallbackObj = callbackObj;
		getCommentsArgsPostID = postID;
		callbackObj.commentResults( comments );
	}

	@Override
	public void search( String searchText, GooglePlusSearchCallbackIfc callbackObj ) {
		searchCalled = true;
		searchArgSearchText = searchText;
		searchArgCallbackObj = callbackObj;
	}
}
