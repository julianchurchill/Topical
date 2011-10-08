package com.ChewieLouie.Topical;

import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;

public interface GooglePlusPostFinderIfc {
	public abstract List< Map<DataType,String> > search( String searchText );
}