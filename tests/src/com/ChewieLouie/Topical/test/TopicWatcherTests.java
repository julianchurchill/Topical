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
		assertEquals( expectedTopics.size(), topicWatcher.watchedTopics().size() );
		for( String topic : topicWatcher.watchedTopics() )
			assertTrue( expectedTopics.contains( topic ) );
	}
	
	public void testCallingWatchCausesTopicToBeAddedToWatchedTopicsList() {
		topicWatcher.watch( "newTopic" );

		assertTrue( topicWatcher.watchedTopics().contains( "newTopic" ) );
	}

	public void testCallingWatchCausesSaveToStorage() {
		topicWatcher.watch( "newTopic" );

		assertTrue( mockStorage.saveCalled );
	}
	
	public void testCallingWatchCausesTopicListToBeSavedAsWatchedTopicsType() {
		topicWatcher.watch( "newTopic" );

		assertTrue( mockStorage.saveArgsType.contains( ValueType.WATCHED_TOPICS ) );
	}

	public void testCallingWatchCausesTopicListToBeSavedToStorage() {
		topicWatcher.watch( "newTopic" );

		String expectedString = StringUtils.join( ",", topicWatcher.watchedTopics().toArray( new String[0] ) );
		assertTrue( mockStorage.saveArgsValue.contains( expectedString ) );
	}

	public void testCallingUnwatchCausesRemovalFoTopicFromWatchedTopicsList() {
		topicWatcher.unwatch( "topic1" );

		assertFalse( topicWatcher.watchedTopics().contains( "topic1" ) );
	}

	public void testCallingUnwatchCausesSaveToStorage() {
		topicWatcher.unwatch( "topic1" );

		assertTrue( mockStorage.saveCalled );
	}

	public void testCallingUnwatchCausesTopicListToBeSavedAsWatchedTopicsType() {
		topicWatcher.unwatch( "topic1" );

		assertTrue( mockStorage.saveArgsType.contains( ValueType.WATCHED_TOPICS ) );
	}

	public void testCallingUnwatchCausesTopicListToBeSavedToStorage() {
		topicWatcher.unwatch( "topic1" );

		String expectedString = StringUtils.join( ",", topicWatcher.watchedTopics().toArray( new String[0] ) );
		assertTrue( mockStorage.saveArgsValue.contains( expectedString ) );
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
