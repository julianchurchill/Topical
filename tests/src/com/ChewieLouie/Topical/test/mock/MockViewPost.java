package com.ChewieLouie.Topical.test.mock;

import java.util.List;

import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.TopicListStatus;
import com.ChewieLouie.Topical.View.ViewPostIfc;

public class MockViewPost implements ViewPostIfc {

	public boolean setAuthorCalled = false;
	public boolean activityStartedCalled = false;
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
	public boolean setReshareAuthorNameCalled = false;
	public String setReshareAuthorNameArg = null;
	public boolean setTopicListStatusCalled = false;
	public TopicListStatus setTopicListStatusArg = TopicListStatus.OLD;
	public boolean setModificationTimeCalled = false;
	public String setModificationTimeArg = null;
	
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

	@Override
	public void setReshareAuthorName(String author) {
		setReshareAuthorNameCalled = true;
		setReshareAuthorNameArg = author;
	}

	@Override
	public void setTopicListStatus( TopicListStatus status ) {
		setTopicListStatusCalled = true;
		setTopicListStatusArg = status;
	}

	@Override
	public void setModificationTime( String modTime ) {
		setModificationTimeCalled = true;
		setModificationTimeArg = modTime;
	}
}
