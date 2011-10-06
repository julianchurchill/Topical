package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;

public class AndroidPreferenceStorage implements PersistentStorageIfc {

	private static final String allFollowedPostsKey = "AllFollowedPostURLs";
	private static final Map<ValueType, String> typeToString = initializeTypeToStringMap();

	private SharedPreferences prefs = null;
	
	private static Map<ValueType, String> initializeTypeToStringMap() {
		Map<ValueType, String> map = new HashMap<ValueType, String>();
		map.put( ValueType.IS_FOLLOWED, "IsFollowed" );
		map.put( ValueType.LAST_VIEWED_MODIFICATION_TIME, "LastViewedModificationTime" );
		map.put( ValueType.POST_ID, "PostID" );
		map.put( ValueType.TITLE, "Title" );
		map.put( ValueType.SUMMARY, "Summary" );
		map.put( ValueType.WATCHED_TOPIC, "WatchedTopic" );
		return Collections.unmodifiableMap( map );
	}
	
	public AndroidPreferenceStorage( Activity activity ) {
		this.prefs = activity.getPreferences( Activity.MODE_PRIVATE );
	}
	
	@Override
	public void save( String key, ValueType type, String value ) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString( makeKey( key, type ), value );
		if( type == ValueType.IS_FOLLOWED )
			updateFollowedPostList( key, value, editor );
		editor.commit();
	}

	@Override
	public String load( String key, ValueType type ) {
		return prefs.getString( makeKey( key, type ), "" );
	}

	@Override
	public List<String> getAllPostURLsWhereFollowingIsTrue() {
		String postURLsString = prefs.getString( allFollowedPostsKey, "" );
		if( postURLsString.isEmpty() )
			return new ArrayList<String>();
		return new ArrayList<String>( Arrays.asList( postURLsString.split( "," ) ) );
	}

	private String makeKey( String uniqueString, ValueType type ) {
		return typeToString.get( type ) + uniqueString;
	}

	private void updateFollowedPostList( String url, String nowFollowing, SharedPreferences.Editor editor ) {
		Set<String> setOfPostURLs = new HashSet<String>( getAllPostURLsWhereFollowingIsTrue() );
		if( Boolean.parseBoolean( nowFollowing ) )
			setOfPostURLs.add( url );
		else
			setOfPostURLs.remove( url );
		if( setOfPostURLs.isEmpty() )
			editor.remove( allFollowedPostsKey );
		else
			editor.putString( allFollowedPostsKey, join( ",", setOfPostURLs.toArray( new String[0] ) ) );
	}
	
	private String join( String separator, String[] strings ) {
		if( strings.length == 0 )
			return null;
		StringBuilder out = new StringBuilder();
		out.append( strings[0] );
		for( int x = 1; x < strings.length; ++x )
			out.append( separator ).append( strings[x] );
		return out.toString();
	}
}
