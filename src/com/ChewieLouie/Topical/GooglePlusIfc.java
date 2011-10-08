package com.ChewieLouie.Topical;

import java.util.List;
import java.util.Map;


public interface GooglePlusIfc {
	public enum DataType { POST_ID, AUTHOR_NAME, AUTHOR_IMAGE, POST_CONTENT, COMMENTS, MODIFICATION_TIME, URL, TITLE, SUMMARY, AUTHOR_ID };

	public abstract void getPostInformation( GooglePlusCallbackIfc callbackObj,
			GooglePlusQuery query, int requestID );
	public abstract List< Map<DataType,String> > search( String searchText );

}
