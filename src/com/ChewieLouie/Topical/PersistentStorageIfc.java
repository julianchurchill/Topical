package com.ChewieLouie.Topical;

import java.util.List;

public interface PersistentStorageIfc {
	public enum ValueType { POST_ID, IS_FOLLOWED, LAST_VIEWED_MODIFICATION_TIME, TITLE, SUMMARY, WATCHED_TOPIC };
	public abstract void save( String key, ValueType type, String value );
	public abstract String load( String key, ValueType type );
	public abstract List<String> getAllPostURLsWhereFollowingIsTrue();
}
