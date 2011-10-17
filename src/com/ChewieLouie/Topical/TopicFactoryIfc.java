package com.ChewieLouie.Topical;

public interface TopicFactoryIfc {
	public abstract TopicIfc create( String topicName, GooglePlusIfc googlePlus, PersistentStorageIfc storage );
}
