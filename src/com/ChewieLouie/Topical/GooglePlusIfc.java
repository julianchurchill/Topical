package com.ChewieLouie.Topical;


public interface GooglePlusIfc {
	public enum DataType { POST_ID, AUTHOR_NAME, AUTHOR_IMAGE, POST_CONTENT, COMMENTS, MODIFICATION_TIME, URL, TITLE, SUMMARY, AUTHOR_ID };

	public abstract void getPostInformation( GooglePlusCallbackIfc callbackObj,
			GooglePlusQuery query, int requestID );

}
