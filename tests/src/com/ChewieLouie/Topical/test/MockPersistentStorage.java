package com.ChewieLouie.Topical.test;

import java.util.ArrayList;
import java.util.List;

import com.ChewieLouie.Topical.PersistentStorageIfc;

public class MockPersistentStorage implements PersistentStorageIfc {

	@Override
	public void save(String postURL, ValueType type, String value) {
	}

	@Override
	public String load(String postURL, ValueType type) {
		return "";
	}

	@Override
	public List<String> getAllPostURLsWhereFollowingIsTrue() {
		return new ArrayList<String>();
	}
}
