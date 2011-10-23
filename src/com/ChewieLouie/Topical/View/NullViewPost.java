package com.ChewieLouie.Topical.View;

import java.util.List;

import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.PostComment;
import com.ChewieLouie.Topical.TopicListStatus;

public class NullViewPost implements ViewPostIfc {

	@Override
	public void setAuthor(String author) {
	}

	@Override
	public void setAuthorImage(String imageURL) {
	}

	@Override
	public void setHTMLContent(String content) {
	}

	@Override
	public void setComments(List<PostComment> comments) {
	}

	@Override
	public void setStatus(Status status) {
	}

	@Override
	public void setSummaryText(String summary) {
	}

	@Override
	public void showError(String errorText) {
	}

	@Override
	public void activityStarted() {
	}

	@Override
	public void activityStopped() {
	}

	@Override
	public void setReshareAuthorName( String author ) {
	}

	@Override
	public void setTopicListStatus( TopicListStatus status ) {
	}

	@Override
	public void setModificationTime( String modTime ) {
	}
}
