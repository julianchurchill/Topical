package com.ChewieLouie.Topical.View;

import java.util.List;

import android.graphics.Color;
import android.widget.Button;

import com.ChewieLouie.Topical.Post.Status;
import com.ChewieLouie.Topical.PostComment;


public class FollowedPostsStatusView implements ViewPostIfc {

	private Button button = null;

	public FollowedPostsStatusView( Button button ) {
		this.button = button;
	}
	
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
		if( status == Status.FOLLOWING_AND_HAS_CHANGED )
			button.setBackgroundColor( Color.YELLOW );
	}

	@Override
	public void setTitle(String title) {
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
}
