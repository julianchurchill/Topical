package com.ChewieLouie.Topical;

import java.util.HashSet;
import java.util.Set;

import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;

public class TopicWatcher {

	private final static String seperator = ",";
	
	private PersistentStorageIfc storage = null;
	private Set<String> watchedTopics = null;

	public TopicWatcher( PersistentStorageIfc storage ) {
		this.storage = storage;
		watchedTopics = new HashSet<String>( StringUtils.split( 
				storage.loadValueByKeyAndType( "", ValueType.WATCHED_TOPICS ), seperator ) );
	}

	public void watch( String topic ) {
		watchedTopics.add( topic );
		storage.saveValueByKeyAndType( watchedTopicsAsString(), "", ValueType.WATCHED_TOPICS );
	}
	
	private String watchedTopicsAsString() {
		return StringUtils.join( seperator, watchedTopics.toArray( new String[0] ) );
	}

	public void unwatch( String topic ) {
		if( watchedTopics.remove( topic ) )
			storage.saveValueByKeyAndType( watchedTopicsAsString(), "", ValueType.WATCHED_TOPICS );
	}
	
	public boolean isWatched( String topic ) {
		return watchedTopics.contains( topic );
	}
	
	public Set<String> watchedTopics() {
		return watchedTopics;
	}
}
