package com.ChewieLouie.Topical.test;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.Post;

public class PostTest extends AndroidTestCase {
	public PostTest() {
		super();
	}

	public void testPostCanBeCreatedWithURL() {
		new Post( "url", new MockPersistentStorage() );
		assertTrue( true );
	}
}
