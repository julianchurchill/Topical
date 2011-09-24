package com.ChewieLouie.Topical;

import java.io.IOException;
import java.util.Map;

public interface GooglePlusIfc {
	public enum DataType { AUTHOR_NAME, AUTHOR_IMAGE, POST_CONTENT, COMMENTS };

	public abstract Map<DataType, String> getPostInformation( String authorID, String url ) throws IOException;

}
