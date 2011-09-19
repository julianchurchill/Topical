package com.ChewieLouie.Topical;

import java.util.List;

public interface GooglePlusPostFinderIfc {

	public abstract List<Post> search(String searchText);

	public abstract String getPostContent(String postID);

}