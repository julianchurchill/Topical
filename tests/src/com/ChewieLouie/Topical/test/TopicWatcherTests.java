package com.ChewieLouie.Topical.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.StringUtils;
import com.ChewieLouie.Topical.TopicWatcher;


public class TopicWatcherTests extends AndroidTestCase {

	private TopicWatcher topicWatcher = null;
	private MockPersistentStorage mockStorage = new MockPersistentStorage();
	private final String topicsList = "topic1,topic2,topic3";
	private final List<String> expectedTopics = StringUtils.split( topicsList, "," );

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockStorage = new MockPersistentStorage();
		mockStorage.loadReturns.put( ValueType.WATCHED_TOPICS, topicsList );
		topicWatcher = new TopicWatcher( mockStorage );
	}

	public void testTopicWatcherLoadsFromStorageOnCreation() {
		assertTrue( mockStorage.loadCalled );
	}

	public void testTopicWatcherLoadsUsingNothingAsKeyOnCreation() {
		assertTrue( mockStorage.loadArgsKey.contains( "" ) );
	}

	public void testTopicWatcherLoadsWatchedTopicListOnCreation() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.WATCHED_TOPICS ) );
	}

	public void testTopicWatchedTopicsReturnsListOfTopicsAsLoadedFromStorage() {
		for( String topic : expectedTopics )
			assertTrue( topicWatcher.isWatched( topic ) );
	}
	
	public void testCallingWatchCausesTopicToBeAddedToWatchedTopicsList() {
		topicWatcher.watch( "newTopic" );

		assertTrue( topicWatcher.isWatched( "newTopic" ) );
	}

	public void testCallingWatchCausesSaveToStorage() {
		topicWatcher.watch( "newTopic" );

		assertTrue( mockStorage.saveCalled );
	}
	
	public void testCallingWatchCausesTopicListToBeSavedAsWatchedTopicsType() {
		topicWatcher.watch( "newTopic" );

		assertTrue( mockStorage.saveArgsType.contains( ValueType.WATCHED_TOPICS ) );
	}

	public void testCallingWatchCausesTopicListToBeSavedToStorageIncludingWatchedTopic() {
		topicWatcher.watch( "newTopic" );

		boolean topicSavedToStorage = false;
		for( String saveValue : mockStorage.saveArgsValue )
			topicSavedToStorage = saveValue.contains( "newTopic" );
		assertTrue( topicSavedToStorage );
	}

	public void testCallingUnwatchOnWatchedTopicCausesRemovalOfTopicFromWatchedTopicsList() {
		topicWatcher.unwatch( "topic1" );

		assertFalse( topicWatcher.isWatched( "topic1" ) );
	}

	public void testCallingUnwatchOnWatchedTopicCausesSaveToStorage() {
		topicWatcher.unwatch( "topic1" );

		assertTrue( mockStorage.saveCalled );
	}

	public void testCallingUnwatchOnWatchedTopicCausesTopicListToBeSavedAsWatchedTopicsType() {
		topicWatcher.unwatch( "topic1" );

		assertTrue( mockStorage.saveArgsType.contains( ValueType.WATCHED_TOPICS ) );
	}

	public void testCallingUnwatchOnWatchedTopicCausesTopicListToBeSavedToStorageWithoutUnwatchedTopic() {
		topicWatcher.unwatch( "topic1" );

		assertFalse( mockStorage.saveArgsValue.contains( "topic1" ) );
	}

	public void testCallingUnwatchForNotWatchedTopicDoesNotCauseSaveToStorage() {
		topicWatcher.unwatch( "newTopic" );

		assertFalse( mockStorage.saveCalled );
	}
	
	public void testIsWatchedReturnsTrueForWatchedTopics() {
		for( String topic : expectedTopics )
			assertTrue( topicWatcher.isWatched( topic ) );
	}

	public void testIsWatchedReturnsFalseForNotWatchedTopics() {
		assertFalse( topicWatcher.isWatched( "notwatchedtopic" ) );
	}
}
