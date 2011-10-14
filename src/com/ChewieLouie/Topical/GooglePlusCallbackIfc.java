package com.ChewieLouie.Topical;

import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;

public interface GooglePlusCallbackIfc {
	public abstract void postInformationResults( Map<DataType, String> postInfo, int requestID );
	public abstract void postInformationError( String errorText, int requestID );
	public abstract void commentResults(List<PostComment> comments);
	public abstract void commentsError(String errorText);
}
