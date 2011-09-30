package com.ChewieLouie.Topical.test;

import android.test.AndroidTestCase;
import android.test.UiThreadTest;

import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc.ValueType;
import com.ChewieLouie.Topical.Post;

public class PostTest extends AndroidTestCase {
	private Post post = null;
	private MockPersistentStorage mockStorage = null;
	private BlockingPostThreadExecuter mockPostThreadExecuter = new BlockingPostThreadExecuter();
	private MockGooglePlus mockGooglePlus = null;
	private String postID = "0123456789";
	private String url = "startOfURL/authorID/posts/" + postID;
	private String lastViewedModificationTime = "1985-04-12T23:20:50.523Z";

	public PostTest() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockStorage = new MockPersistentStorage();
		mockStorage.loadReturns.put( ValueType.POST_ID, postID );
		mockStorage.loadReturns.put( ValueType.LAST_VIEWED_MODIFICATION_TIME, lastViewedModificationTime );
		mockGooglePlus = new MockGooglePlus();
		mockGooglePlus.postInformation.put( DataType.POST_ID, postID );
		post = new Post( url, mockStorage, mockPostThreadExecuter, mockGooglePlus );
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

	public void testSetTitleSavesToStorage() {
		post.setTitle( "testTitle" );

		assertEquals( "testTitle", mockStorage.saveArgsValue );
	}

	public void testSetTitleSavesTitleTypeToStorage() {
		post.setTitle( "testTitle" );

		assertEquals( ValueType.TITLE, mockStorage.saveArgsType );
	}

	public void testSetTitleSavesTitleValueToStorage() {
		post.setTitle( "testTitle" );

		assertEquals( "testTitle", mockStorage.saveArgsValue );
	}

	public void testSetSummarySavesToStorage() {
		post.setSummary( "testSummary" );

		assertTrue( mockStorage.saveCalled );
	}

	public void testSetSummarySavesSummaryTypeToStorage() {
		post.setSummary( "testSummary" );

		assertEquals( ValueType.SUMMARY, mockStorage.saveArgsType );
	}

	public void testSetSummarySavesSummaryValueToStorage() {
		post.setSummary( "testSummary" );

		assertEquals( "testSummary", mockStorage.saveArgsValue );
	}

	public void testFollowSavesToStorage() {
		post.follow();

		assertTrue( mockStorage.saveCalled );
	}

	public void testFollowSavesIsFollowedTypeToStorage() {
		post.follow();

		assertEquals( ValueType.IS_FOLLOWED, mockStorage.saveArgsType );
	}

	public void testFollowSavesTrueToStorage() {
		post.follow();

		assertEquals( String.valueOf( true ), mockStorage.saveArgsValue );
	}

	public void testUnfollowSavesToStorage() {
		post.unfollow();

		assertTrue( mockStorage.saveCalled );
	}

	public void testUnfollowSavesIsFollowedTypeToStorage() {
		post.unfollow();

		assertEquals( ValueType.IS_FOLLOWED, mockStorage.saveArgsType );
	}

	public void testUnfollowSavesFalseToStorage() {
		post.unfollow();

		assertEquals( String.valueOf( false ), mockStorage.saveArgsValue );
	}

	public void testFollowCausesIsFollowedToReturnTrue() {
		post.follow();
		
		assertTrue( post.isFollowed() );
	}

	public void testUnfollowCausesIsFollowedToReturnFalse() {
		post.unfollow();
		
		assertFalse( post.isFollowed() );
	}

//#error how do we test the show()/viewed() methods? They depend on an Async task to retrieve
//#error the GooglePlus results. How do we get rid of this Async task?
//	@UiThreadTest
//	public void testShowCallsSetAuthorOnView() {
//		MockViewPost mockViewPost = new MockViewPost();
//		post.show( mockViewPost );
//		
//		assertTrue( mockViewPost.setAuthorCalled );
//	}
//
//	public void testViewedCausesSaveToStorage() {
//		post.viewed();
//
//		assertTrue( mockStorage.saveCalled );
//	}
//
//	public void testViewedCausesLastModificationDateTypeToBeSaved() {
//		post.viewed();
//		
//		assertEquals( ValueType.LAST_VIEWED_MODIFICATION_TIME, mockStorage.saveArgsType );
//	}
//
//	public void testViewedCausesLastModificationDateValueToBeSaved() {
//		post.viewed();
//		
//		assertEquals( lastViewedModificationTime, mockStorage.saveArgsValue );
//	}
}
