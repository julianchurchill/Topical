package com.ChewieLouie.Topical;


public interface GooglePlusIfc {
	public enum DataType { POST_ID, AUTHOR_NAME, AUTHOR_IMAGE, POST_CONTENT, COMMENTS, MODIFICATION_TIME };

	public abstract void getPostInformationByPostID( GooglePlusCallbackIfc callbackObj, 
			String postID );
	public abstract void getPostInformation( GooglePlusCallbackIfc callbackObj,
			String authorID, String url );

}
