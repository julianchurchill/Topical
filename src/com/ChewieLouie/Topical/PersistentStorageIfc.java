package com.ChewieLouie.Topical;

public interface PersistentStorageIfc {
	public abstract void save( String key, String value );
	public abstract String load( String key );
}
