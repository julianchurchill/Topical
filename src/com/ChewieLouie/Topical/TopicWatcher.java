package com.ChewieLouie.Topical;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.View.ViewTopicListIfc;

public class TopicWatcher {
	
	private final static String seperator = ",";
	
	private PersistentStorageIfc storage = null;
	private Set<Topic> watchedTopics = new HashSet<Topic>();

	public TopicWatcher( PersistentStorageIfc storage ) {
		this.storage = storage;
		populateWatchedTopicsSet();
	}

	private void populateWatchedTopicsSet() {
		List<String> topicStrings = StringUtils.split( 
				storage.loadValueByKeyAndType( "", ValueType.WATCHED_TOPICS ), seperator );
		for( String topic : topicStrings )
			watchedTopics.add( new Topic( topic ) );
	}

	public void watch( String topic ) {
		watchedTopics.add( new Topic( topic ) );
		storage.saveValueByKeyAndType( watchedTopicsAsString(), "", ValueType.WATCHED_TOPICS );
	}
	
	private String watchedTopicsAsString() {
		String[] topics = new String[ watchedTopics.size() ];
		int i = 0;
		for( Topic topic : watchedTopics ) {
			topics[i++] = topic.topicName();
		}
		return StringUtils.join( seperator, topics );
	}

	public void unwatch( String topic ) {
		if( watchedTopics.remove( topic ) )
			storage.saveValueByKeyAndType( watchedTopicsAsString(), "", ValueType.WATCHED_TOPICS );
	}
	
	public boolean isWatched( String topic ) {
		return watchedTopics.contains( topic );
	}

	public String topicAtPosition( int position ) {
		return watchedTopics.toArray( new Topic[0] )[position].topicName();
	}

	public void populateTopicList( ViewTopicListIfc view ) {
		view.populateTopicList( watchedTopics );
	}
	
	public void updateStatuses() {
		for( Topic topic : watchedTopics )
			topic.updateStatus();
	}
}
