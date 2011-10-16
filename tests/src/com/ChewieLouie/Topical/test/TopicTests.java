package com.ChewieLouie.Topical.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.StringUtils;
import com.ChewieLouie.Topical.Topic;

public class TopicTests extends AndroidTestCase {

	private MockViewWatchedTopic mockView = null;
	private Topic topic = null;
	private String topicName = "topic1";
	private MockGooglePlus mockGooglePlus = new MockGooglePlus();
	private MockPersistentStorage mockStorage = new MockPersistentStorage();
	private List<Map<DataType, String>> resultsList = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockView = new MockViewWatchedTopic();

		resultsList = new ArrayList<Map<DataType, String>>();
		Map<DataType, String> result = new HashMap<DataType, String>();
		result.put( DataType.POST_ID, "postID1" );
		resultsList.add( result );
		result.put( DataType.POST_ID, "postID2" );
		resultsList.add( result );
		result.put( DataType.POST_ID, "postID3" );
		resultsList.add( result );

		Map<ValueType, String> loadValues = new HashMap<ValueType, String>();
		loadValues.put( ValueType.POST_ID_LIST, createPostIDsList( resultsList ) );
		mockStorage.loadReturns = loadValues;

		topic = new Topic( topicName, mockGooglePlus, mockStorage );
	}

	private String createPostIDsList( List<Map<DataType, String>> results ) {
		List<String> postIDs = new ArrayList<String>();
		for( Map<DataType, String> result : results )
			postIDs.add( result.get( DataType.POST_ID ) );
		return StringUtils.join( ",", postIDs.toArray( new String[0] ) );
	}

	public void testShowCallsSetTextOnView() {
		topic.show( mockView );

		assertTrue( mockView.setTextCalled );
	}

	public void testShowCallsSetTextOnViewWithTopicName() {
		topic.show( mockView );

		assertEquals( topicName, mockView.setTextArg );
	}

	public void testTopicNameReturnsTopicPassedInConstructor() {
		assertEquals( topicName, topic.topicName() );
	}

	public void testShowThenUpdateStatusCallsActivityStartedOnView() {
		topic.show( mockView );
		topic.updateStatus();

		assertTrue( mockView.activityStartedCalled );
	}

	public void testUpdateStatusCallsGooglePlusSearch() {
		topic.updateStatus();

		assertTrue( mockGooglePlus.searchCalled );
	}

	public void testUpdateStatusCallsGooglePlusSearchWithTopicAsSearchText() {
		topic.updateStatus();

		assertEquals( topicName, mockGooglePlus.searchArgSearchText );
	}

	public void testUpdateStatusCallsGooglePlusSearchWithTopicAsCallbackObject() {
		topic.updateStatus();

		assertEquals( topic, mockGooglePlus.searchArgCallbackObj );
	}

	public void testShowThenSearchResultsCallsSetTopicResultsHaveNotChangedOnView() {
		topic.show( mockView );
		topic.searchResults( null );

		assertTrue( mockView.setTopicResultsHaveNotChangedCalled );
	}

	public void testShowThenSearchResultsCallsActivityStoppedOnView() {
		topic.show( mockView );
		topic.searchResults( null );

		assertTrue( mockView.activityStoppedCalled );
	}
	
	public void testOnConstructionTopicLoadsDataFromStorage() {
		assertTrue( mockStorage.loadCalled );
	}
	
	public void testOnConstructionTopicLoadsPostIDListTypeFromStorage() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.POST_ID_LIST ) );
	}
	
	public void testOnConstructionTopicLoadsPostIDValuesUsingTopicNameAsKeyFromStorage() {
		assertTrue( mockStorage.loadArgsKey.contains( topicName ) );
	}

	public void testSearchResultsChangedCallsSetTopicResultsHaveChangedOnView() {
		topic.show( mockView );
		topic.searchResults( createNewResults() );

		assertTrue( mockView.setTopicResultsHaveChangedCalled );
	}
	
	private List<Map<DataType, String>> createNewResults() {
		List<Map<DataType, String>> newResultsList = new ArrayList<Map<DataType, String>>();
		Map<DataType, String> newResult = new HashMap<DataType, String>();
		newResult.put( DataType.POST_ID, "newPostID1" );
		newResultsList.add( newResult );
		return newResultsList;
	}

	public void testSearchResultsUnchangedDoesNotSaveToStorage() {
		topic.show( mockView );
		topic.searchResults( resultsList );

		assertFalse( mockStorage.saveCalled );
	}

	public void testSearchResultsChangedSavesToStorage() {
		topic.show( mockView );
		topic.searchResults( createNewResults() );

		assertTrue( mockStorage.saveCalled );
	}

	public void testSearchResultsChangedSavesNewPostIDListTypeToStorage() {
		topic.show( mockView );
		topic.searchResults( createNewResults() );

		assertTrue( mockStorage.saveArgsType.contains( ValueType.POST_ID_LIST ) );
	}

	public void testSearchResultsChangedSavesUsingTopicNameAsKeyToStorage() {
		topic.show( mockView );
		topic.searchResults( createNewResults() );

		assertTrue( mockStorage.saveArgsKey.contains( topicName ) );
	}

	public void testSearchResultsChangedSavesPostIDsToStorage() {
		final List<Map<DataType, String>> newResults = createNewResults();
		final String postIDsList = createPostIDsList( newResults );
		topic.show( mockView );
		topic.searchResults( newResults );

		assertTrue( mockStorage.saveArgsValue.contains( postIDsList ) );
	}
}