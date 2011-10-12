package com.ChewieLouie.Topical.test;

import java.util.List;

import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.ViewPostIfc;

public class MockViewPost implements ViewPostIfc {

	public boolean setAuthorCalled = false;
	public boolean activityStartedCalled = false;
	public boolean setTitleCalled = false;
	public String setTitleArg = null;
	public boolean setCommentsCalled = false;
	public List<PostComment> setCommentsArg = null;
	public String setAuthorArg = null;
	public boolean setAuthorImageCalled = false;
	public String setAuthorImageArg = null;
	public boolean setHTMLContentCalled = false;
	public String setHTMLContentArg = null;
	public boolean setStatusCalled = false;
	public Status setStatusArg = null;
	public boolean setSummaryTextCalled = false;
	public String setSummaryTextArg = null;
	
	@Override
	public void setAuthor( String author ) {
		setAuthorCalled = true;
		setAuthorArg = author;
	}

	@Override
	public void setAuthorImage(String imageURL) {
		setAuthorImageCalled = true;
		setAuthorImageArg = imageURL;
	}

	@Override
	public void setHTMLContent(String content) {
		setHTMLContentCalled = true;
		setHTMLContentArg = content;
	}

	@Override
	public void setComments(List<PostComment> comments) {
		setCommentsCalled = true;
		setCommentsArg = comments;
	}

	@Override
	public void setStatus(Status status) {
		setStatusCalled = true;
		setStatusArg = status;
	}

	@Override
	public void setTitle(String title) {
		setTitleCalled = true;
		setTitleArg = title;
	}

	@Override
	public void setSummaryText(String summary) {
		setSummaryTextCalled = true;
		setSummaryTextArg = summary;
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
