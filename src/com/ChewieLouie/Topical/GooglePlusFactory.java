package com.ChewieLouie.Topical;


public class GooglePlusFactory {

	static GooglePlusIfc create() {
		return new GooglePlus();
//		return new TestGooglePlus();
	}
}
