package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.Map;

public interface GooglePlusIfc {
	public enum DataType { POST_ID, AUTHOR_NAME, AUTHOR_IMAGE, POST_CONTENT, COMMENTS, MODIFICATION_TIME };

	public abstract Map<DataType, String> getPostInformationByPostID( String postID ) throws IOException;
	public abstract Map<DataType, String> getPostInformation( String authorID, String url ) throws IOException;

}
