package com.ChewieLouie.Topical.test;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.Topic;

public class TopicTests extends AndroidTestCase {

	private MockViewWatchedTopic mockView = null;
	private Topic topic = null;
	private String topicName = "topic1";
	private MockGooglePlus mockGooglePlus = new MockGooglePlus();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockView = new MockViewWatchedTopic();
		topic = new Topic( topicName, mockGooglePlus );
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
}