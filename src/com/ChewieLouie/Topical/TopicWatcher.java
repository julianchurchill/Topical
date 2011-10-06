package com.ChewieLouie.Topical;

import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;


public class TopicWatcher {
	
//	private String topic = "";
	private PersistentStorageIfc storage = null;
	private boolean isWatched = false;

	public TopicWatcher( String topic, PersistentStorageIfc storage ) {
//		this.topic = topic;
		this.storage = storage;
		isWatched = Boolean.parseBoolean( storage.loadValueByKeyAndType( topic, ValueType.WATCHED_TOPIC ) );
	}

	public boolean isWatched() {
		return isWatched;
	}

	public void watched() {
		storage.saveValueByKeyAndType( "", "", ValueType.WATCHED_TOPIC );
	}
}
