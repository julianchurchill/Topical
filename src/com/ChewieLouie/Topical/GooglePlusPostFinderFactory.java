package com.ChewieLouie.Topical;

public class GooglePlusPostFinderFactory {

	static GooglePlusPostFinderIfc create() {
		return new GooglePlusPostFinder();
//		return new TestGooglePlusPostFinder();
	}
}
