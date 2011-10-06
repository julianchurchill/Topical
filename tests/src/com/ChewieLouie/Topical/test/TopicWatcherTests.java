package com.ChewieLouie.Topical.test;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.TopicWatcher;


public class TopicWatcherTests extends AndroidTestCase {

	private TopicWatcher topicWatcher = null;
	private final String topic = "test topic";
	private MockPersistentStorage mockStorage = new MockPersistentStorage();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockStorage = new MockPersistentStorage();
		topicWatcher = new TopicWatcher( topic, mockStorage );
	}

	public void testTopicWatcherLoadsFromStorageOnCreation() {
		assertTrue( mockStorage.loadCalled );
	}

	public void testTopicWatcherLoadsUsingTopicAsKeyOnCreation() {
		assertTrue( mockStorage.loadArgsKey.contains( topic ) );
	}

	public void testTopicWatcherLoadsWatchedStateTypeOnCreation() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.WATCHED_TOPIC ) );
	}

	public void testTopicWatcherLoadsWatchedStateValueOnCreationForTrue() {
		mockStorage.loadReturns.put( ValueType.WATCHED_TOPIC, Boolean.toString( true ) );

		TopicWatcher watchedTopicWatcher = new TopicWatcher( topic, mockStorage );

		assertTrue( watchedTopicWatcher.isWatched() );
	}

	public void testTopicWatcherLoadsWatchedStateValueOnCreationForFalseState() {
		mockStorage.loadReturns.put( ValueType.WATCHED_TOPIC, Boolean.toString( false ) );

		TopicWatcher notWatchedTopicWatcher = new TopicWatcher( topic, mockStorage );

		assertFalse( notWatchedTopicWatcher.isWatched() );
	}

	public void testCallingWatchedOnTopicWatcherCausesSaveToStorage() {
		topicWatcher.watched();
	
		assertTrue( mockStorage.saveCalled );
	}
}
