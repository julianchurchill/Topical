package com.ChewieLouie.Topical;



public interface GooglePlusIfc {
	public enum DataType { POST_ID, AUTHOR_NAME, AUTHOR_IMAGE, POST_CONTENT, COMMENTS, MODIFICATION_TIME, URL, TITLE, SUMMARY, AUTHOR_ID, RESHARE_AUTHOR_NAME };

	public abstract void getPostInformation( GooglePlusCallbackIfc callbackObj,
			GooglePlusQuery query, int requestID );
	public abstract void getComments( GooglePlusCallbackIfc callbackObj, String postID );
	public abstract void search( String searchText, GooglePlusSearchCallbackIfc callbackObj );

}
