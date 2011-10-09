package com.ChewieLouie.Topical.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.PersistentStorageIfc;

public class MockPersistentStorage implements PersistentStorageIfc {

	public boolean loadCalled = false;
	public List<String> loadArgsKey = new ArrayList<String>();
	public List<ValueType> loadArgsType = new ArrayList<ValueType>();
	public Map<ValueType, String> loadReturns = new HashMap<ValueType, String>();
	public boolean saveCalled = false;
	public List<ValueType> saveArgsType = new ArrayList<ValueType>();
	public List<String> saveArgsValue = new ArrayList<String>();
	public boolean removeCalled = false;
	public String removeArg = "";

	@Override
	public void save(String postURL, ValueType type, String value) {
		saveCalled = true;
		saveArgsType.add( type );
		saveArgsValue.add( value );		
	}

	@Override
	public String load(String postURL, ValueType type) {
		loadCalled = true;
		loadArgsKey.add( postURL );
		loadArgsType.add( type );
		return loadReturns.get( type );
	}

	@Override
	public List<String> getAllPostURLsWhereFollowingIsTrue() {
		return new ArrayList<String>();
	}

	@Override
	public void remove( String postURL ) {
		removeCalled = true;
		removeArg = postURL;
	}
}
