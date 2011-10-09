package com.ChewieLouie.Topical.test;

import android.test.AndroidTestCase;

import com.ChewieLouie.Topical.StringUtils;

public class StringUtilsTests extends AndroidTestCase {

	public void testSplitWithBlankSeperatorReturnsEmptyList() {
		assertEquals( 0, StringUtils.split( "text", "").size() );
	}

	public void testSplitEmptyStringReturnsEmptyList() {
		assertEquals( 0, StringUtils.split( "", "seperator").size() );
	}

	public void testSplitOneStringWithNoSeperatorReturnsOneToken() {
		assertEquals( 1, StringUtils.split( "sometext", "," ).size() );
	}

	public void testSplitOneStringWithNoSeperatorReturnsTheCorrectToken() {
		assertTrue( StringUtils.split( "sometext", "," ).get( 0 ).equals( "sometext" ) );
	}
	
	public void testSplitTwoStringsReturnsTwoTokens() {
		assertEquals( 2, StringUtils.split( "text1,text2", "," ).size() );
	}
	
	public void testSplitTwoStringsReturnsCorrectTwoTokens() {
		assertTrue( StringUtils.split( "text1,text2", "," ).get( 0 ).equals( "text1" ) );
		assertTrue( StringUtils.split( "text1,text2", "," ).get( 1 ).equals( "text2" ) );
	}
	
	public void testJoinWithNoSeperatorReturnsBlankString() {
		assertTrue( StringUtils.join( "", new String[] { "text1" } ).equals( "" ) );
	}

	public void testJoinWithNoStringsReturnsBlankString() {
		assertTrue( StringUtils.join( ",", new String[0] ).equals( "" ) );
	}
	
	public void testJoinTwoStringsReturnsExpectedResult() {
		assertTrue( StringUtils.join( ",", new String[] { "text1", "text2" } ).equals( "text1,text2" ) );
	}
}
