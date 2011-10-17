package com.ChewieLouie.Topical.test.mock;

import java.util.HashMap;
import java.util.Map;

import com.ChewieLouie.Topical.GooglePlusIfc;
import com.ChewieLouie.Topical.PersistentStorageIfc;
import com.ChewieLouie.Topical.TopicFactoryIfc;
import com.ChewieLouie.Topical.TopicIfc;

public class MockTopicFactory implements TopicFactoryIfc {

	public static Map<String, MockTopic> mockTopics = new HashMap<String, MockTopic>();

	@Override
	public TopicIfc create( String topicName, GooglePlusIfc googlePlus, PersistentStorageIfc storage ) {
		mockTopics.put( topicName, new MockTopic() );
		return mockTopics.get( topicName );
	}
}
