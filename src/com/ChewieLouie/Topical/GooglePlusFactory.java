package com.ChewieLouie.Topical;


public class GooglePlusFactory {

	static GooglePlusIfc create() {
		return GooglePlus.Make();
//		return new TestGooglePlus();
	}
}
