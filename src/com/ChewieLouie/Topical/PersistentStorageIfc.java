package com.ChewieLouie.Topical;

import java.util.List;

public interface PersistentStorageIfc {
	public enum ValueType { POST_ID, IS_FOLLOWED, LAST_VIEWED_MODIFICATION_TIME, TITLE, SUMMARY, WATCHED_TOPICS, POST_ID_LIST, RESHARE_AUTHOR_NAME };
	public abstract void saveValueByKeyAndType( String value, String key, ValueType type );
	public abstract String loadValueByKeyAndType( String key, ValueType type );
	public abstract void remove( String key );
	public abstract List<String> getAllPostURLsWhereFollowingIsTrue();
}
