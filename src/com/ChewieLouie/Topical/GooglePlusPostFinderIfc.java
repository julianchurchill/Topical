package com.ChewieLouie.Topical;

import java.util.List;

import com.google.api.services.customsearch.model.Result;

public interface GooglePlusPostFinderIfc {
	public abstract List<Result> search( String searchText );
}