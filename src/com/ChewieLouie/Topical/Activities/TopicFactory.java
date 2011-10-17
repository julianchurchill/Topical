package com.ChewieLouie.Topical.Activities;

import com.ChewieLouie.Topical.GooglePlusIfc;
import com.ChewieLouie.Topical.PersistentStorageIfc;
import com.ChewieLouie.Topical.Topic;
import com.ChewieLouie.Topical.TopicFactoryIfc;
import com.ChewieLouie.Topical.TopicIfc;

public class TopicFactory implements TopicFactoryIfc {

	@Override
	public TopicIfc create(String topicName, GooglePlusIfc googlePlus, PersistentStorageIfc storage) {
		return new Topic( topicName, googlePlus, storage );
	}
}
