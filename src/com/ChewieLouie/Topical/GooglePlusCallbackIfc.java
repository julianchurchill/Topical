package com.ChewieLouie.Topical;

import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;

public interface GooglePlusCallbackIfc {
	public abstract void postInformationResults( Map<DataType, String> postInfo );
	public abstract void postInformationError( String errorText );
}
