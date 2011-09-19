package com.ChewieLouie.Topical;

import com.ChewieLouie.Topical.Test.TestGooglePlus;

public class GooglePlusFactory {

	static GooglePlusIfc create() {
//		return new GooglePlus();
		return new TestGooglePlus();
	}
}
