package com.ChewieLouie.Topical;

public class GooglePlusPostFinderFactory {

	public static GooglePlusPostFinderIfc create() {
		return new GooglePlusPostFinder();
//		return new TestGooglePlusPostFinder();
	}
}
