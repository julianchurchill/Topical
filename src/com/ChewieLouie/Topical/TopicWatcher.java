package com.ChewieLouie.Topical;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.View.ViewTopicListIfc;

public class TopicWatcher {
	
	private final static String seperator = ",";
	
	private PersistentStorageIfc storage = null;
	private Map<String, TopicIfc> watchedTopics = new HashMap<String, TopicIfc>();
	private GooglePlusIfc googlePlus = null;
	private TopicFactoryIfc topicFactory = null;

	public TopicWatcher( PersistentStorageIfc storage, GooglePlusIfc googlePlus, TopicFactoryIfc topicFactory ) {
		this.storage = storage;
		this.googlePlus = googlePlus;
		this.topicFactory = topicFactory;
		populateWatchedTopicsSet();
	}

	private void populateWatchedTopicsSet() {
		List<String> topicStrings = StringUtils.split( 
				storage.loadValueByKeyAndType( "", ValueType.WATCHED_TOPICS ), seperator );
		for( String topic : topicStrings )
			watchedTopics.put( topic, topicFactory.create( topic, googlePlus, storage ) );
	}

	public void watch( String topic ) {
		watchedTopics.put( topic, topicFactory.create( topic, googlePlus, storage ) );
		storage.saveValueByKeyAndType( watchedTopicsAsString(), "", ValueType.WATCHED_TOPICS );
	}

	private String watchedTopicsAsString() {
		return StringUtils.join( seperator, watchedTopics.keySet().toArray( new String[0] ) );
	}

	public void unwatch( String topic ) {
		if( watchedTopics.remove( topic ) != null )
			storage.saveValueByKeyAndType( watchedTopicsAsString(), "", ValueType.WATCHED_TOPICS );
	}
	
	public boolean isWatched( String topic ) {
		return watchedTopics.containsKey( topic );
	}

	public String topicAtPosition( int position ) {
		return watchedTopics.keySet().toArray( new String[0] )[position];
	}

	public void populateTopicList( ViewTopicListIfc view ) {
		view.populateTopicList( watchedTopics.values() );
	}
	
	public void updateStatuses() {
		for( TopicIfc topic : watchedTopics.values() )
			topic.updateStatus();
	}

	public void viewed( String topic ) {
		watchedTopics.get( topic ).viewed();
	}
}
