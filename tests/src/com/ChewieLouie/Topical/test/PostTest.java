package com.ChewieLouie.Topical.test;

import java.util.HashMap;
import java.util.Map;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.Post;

public class PostTest extends AndroidTestCase {
	private Post post = null;
	private MockPersistentStorage mockStorage = null;
	private MockGooglePlus mockGooglePlus = null;
	private final String postID = "0123456789";
	private final String authorID = "9876543210";
	private final String url = "startOfURL/" + authorID + "/posts/" + postID;
	private final String lastViewedModificationTime = "1985-04-12T23:20:50.523Z";
	private final String title = "TestTitle";
	Map<DataType,String> postInfo = new HashMap<DataType, String>();

	public PostTest() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockStorage = new MockPersistentStorage();
		mockStorage.loadReturns.put( ValueType.POST_ID, postID );
		mockStorage.loadReturns.put( ValueType.LAST_VIEWED_MODIFICATION_TIME, lastViewedModificationTime );
		mockStorage.loadReturns.put( ValueType.TITLE, title );
		mockGooglePlus = new MockGooglePlus();
		mockGooglePlus.postInformation.put( DataType.POST_ID, postID );
		mockGooglePlus.postInformation.put( DataType.MODIFICATION_TIME, lastViewedModificationTime );
		postInfo.put( DataType.URL, url );
		post = new Post( postInfo, mockStorage, mockGooglePlus );
	}
	
	private Post createFollowedPost() {
		mockStorage.loadReturns.put( ValueType.IS_FOLLOWED, Boolean.toString( true ) );
		return new Post( postInfo, mockStorage, mockGooglePlus );
	}

	public void testPostLoadsSomeDataFromStorageOnConstruction() {
		assertTrue( mockStorage.loadCalled );
	}

	public void testPostAlwaysUsesURLAsKeyToLoadDataFromStorageOnConstruction() {
		for( String key : mockStorage.loadArgsKey )
			assertEquals( url, key );
	}

	public void testPostLoadsTitleFromStorageOnConstruction() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.TITLE ) );
	}

	public void testPostLoadsSummaryTextFromStorageOnConstruction() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.SUMMARY ) );
	}

	public void testPostLoadsIsFollowedFromStorageOnConstruction() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.IS_FOLLOWED ) );
	}

	public void testPostLoadsPostIDFromStorageOnConstruction() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.POST_ID ) );
	}

	public void testPostLoadsLastViewedModificationTimeFromStorageOnConstruction() {
		assertTrue( mockStorage.loadArgsType.contains( ValueType.LAST_VIEWED_MODIFICATION_TIME ) );
	}

	public void testFollowSavesToStorage() {
		post.follow();

		assertTrue( mockStorage.saveCalled );
	}

	public void testFollowSavesIsFollowedTypeToStorage() {
		post.follow();

		assertTrue( mockStorage.saveArgsType.contains( ValueType.IS_FOLLOWED ) );
	}

	public void testFollowSavesTrueToStorage() {
		post.follow();

		assertTrue( mockStorage.saveArgsValue.contains( String.valueOf( true ) ) );
	}

	public void testUnfollowRemovesFromStorage() {
		post.unfollow();

		assertTrue( mockStorage.removeCalled );
	}

	public void testUnfollowRemovesFromStorageWithURL() {
		post.unfollow();

		assertEquals( url, mockStorage.removeArg );
	}

	public void testFollowCausesIsFollowedToReturnTrue() {
		post.follow();
		
		assertTrue( post.isFollowed() );
	}

	public void testUnfollowCausesIsFollowedToReturnFalse() {
		post.unfollow();
		
		assertFalse( post.isFollowed() );
	}

	public void testShowCallsActivityStartedOnView() {
		MockViewPost mockViewPost = new MockViewPost();
		post.show( mockViewPost );
		
		assertTrue( mockViewPost.activityStartedCalled );
	}

	public void testShowCallsSetTitleOnView() {
		MockViewPost mockViewPost = new MockViewPost();
		post.show( mockViewPost );

		assertTrue( mockViewPost.setTitleCalled );
	}

	public void testShowCallsSetTitleOnViewWithSavedTitle() {
		MockViewPost mockViewPost = new MockViewPost();
		post.show( mockViewPost );

		assertEquals( title, mockViewPost.setTitleArg );
	}

	public void testShowCallsGooglePlusGetPostInformation() {
		post.show( new MockViewPost() );

		assertTrue( mockGooglePlus.getPostInformationCalled );
	}

	public void testShowCallsGooglePlusGetPostInformationWithPostAsCallbackObj() {
		post.show( new MockViewPost() );

		assertEquals( post, mockGooglePlus.getPostInformationArgsCallbackObj );
	}

	public void testShowCallsGooglePlusGetPostInformationWithPostID() {
		post.show( new MockViewPost() );

		assertEquals( postID, mockGooglePlus.getPostInformationArgsQuery.postID );
	}

	public void testShowCallsGooglePlusGetPostInformationWithAuthorID() {
		post.show( new MockViewPost() );

		assertEquals( authorID, mockGooglePlus.getPostInformationArgsQuery.authorID );
	}

	public void testShowCallsGooglePlusGetPostInformationWithURL() {
		post.show( new MockViewPost() );

		assertEquals( url, mockGooglePlus.getPostInformationArgsQuery.url );
	}

	public void testShowCallsSetAuthorOnView() {
		MockViewPost mockViewPost = new MockViewPost();
		post.show( mockViewPost );
		
		assertTrue( mockViewPost.setAuthorCalled );
	}

	public void testViewedCausesSaveToStorageIfFollowed() {
		Post followedPost = createFollowedPost();
		followedPost.show( new MockViewPost() );

		followedPost.viewed();

		assertTrue( mockStorage.saveCalled );
	}

	public void testViewedCausesLastModificationDateTypeToBeSaved() {
		Post followedPost = createFollowedPost();
		followedPost.show( new MockViewPost() );

		followedPost.viewed();
		
		assertTrue( mockStorage.saveArgsType.contains( ValueType.LAST_VIEWED_MODIFICATION_TIME ) );
	}

	public void testViewedCausesLastModificationDateValueToBeSaved() {
		Post followedPost = createFollowedPost();
		followedPost.show( new MockViewPost() );

		followedPost.viewed();

		assertTrue( mockStorage.saveArgsValue.contains( lastViewedModificationTime ) );
	}
}
