package com.ChewieLouie.Topical.test;

import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.ViewPostIfc;

public class MockViewPost implements ViewPostIfc {

	public boolean setAuthorCalled = false;
	public boolean activityStartedCalled = false;
	public boolean setTitleCalled = false;
	public String setTitleArg = null;
	
	@Override
	public void setAuthor( String author ) {
		setAuthorCalled = true;
	}

	@Override
	public void setAuthorImage(String imageURL) {
	}

	@Override
	public void setHTMLContent(String content) {
	}

	@Override
	public void setComments(String comments) {
	}

	@Override
	public void setStatus(Status status) {
	}

	@Override
	public void setTitle(String title) {
		setTitleCalled = true;
		setTitleArg = title;
	}

	@Override
	public void setSummaryText(String summary) {
	}

	@Override
	public void showError(String errorText) {
	}

	@Override
	public void activityStarted() {
		activityStartedCalled = true;
	}

	@Override
	public void activityStopped() {
	}
}
