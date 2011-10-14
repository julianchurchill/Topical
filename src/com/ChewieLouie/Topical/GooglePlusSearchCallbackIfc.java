package com.ChewieLouie.Topical;

import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;

public interface GooglePlusSearchCallbackIfc {

	public abstract void searchResults(List<Map<DataType, String>> results);
}
