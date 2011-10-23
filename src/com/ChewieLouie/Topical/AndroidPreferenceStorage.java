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
	private static final List<ValueType> saveableTypes = initialiseSaveableTypesList();

	private SharedPreferences prefs = null;
	
	private static Map<ValueType, String> initializeTypeToStringMap() {
		Map<ValueType, String> map = new HashMap<ValueType, String>();
		map.put( ValueType.IS_FOLLOWED, "IsFollowed" );
		map.put( ValueType.LAST_VIEWED_MODIFICATION_TIME, "LastViewedModificationTime" );
		map.put( ValueType.MODIFICATION_TIME, "ModificationTime" );
		map.put( ValueType.POST_ID, "PostID" );
		map.put( ValueType.SUMMARY, "Summary" );
		map.put( ValueType.AUTHOR_NAME, "AuthorName" );
		map.put( ValueType.WATCHED_TOPICS, "WatchedTopics" );
		return Collections.unmodifiableMap( map );
	}
	
	private static List<ValueType> initialiseSaveableTypesList() {
		List<ValueType> types = new ArrayList<ValueType>();
		types.add( ValueType.IS_FOLLOWED );
		types.add( ValueType.LAST_VIEWED_MODIFICATION_TIME );
		types.add( ValueType.MODIFICATION_TIME );
		types.add( ValueType.POST_ID );
		types.add( ValueType.SUMMARY );
		types.add( ValueType.AUTHOR_NAME );
		return Collections.unmodifiableList( types );
	}
	
	public AndroidPreferenceStorage( Activity activity ) {
		this.prefs = activity.getPreferences( Activity.MODE_PRIVATE );
	}
	
	@Override
	public void saveValueByKeyAndType( String value, String key, ValueType type ) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString( makeKey( key, type ), value );
		if( type == ValueType.IS_FOLLOWED )
			updateFollowedPostList( key, value, editor );
		editor.commit();
	}

	private String makeKey( String uniqueString, ValueType type ) {
		return typeToString.get( type ) + uniqueString;
	}

	@Override
	public String loadValueByKeyAndType( String key, ValueType type ) {
		return prefs.getString( makeKey( key, type ), "" );
	}

	@Override
	public List<String> getAllPostURLsWhereFollowingIsTrue() {
		String postURLsString = prefs.getString( allFollowedPostsKey, "" );
		if( postURLsString.isEmpty() )
			return new ArrayList<String>();
		return new ArrayList<String>( Arrays.asList( postURLsString.split( "," ) ) );
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
			editor.putString( allFollowedPostsKey, StringUtils.join( ",", setOfPostURLs.toArray( new String[0] ) ) );
	}

	@Override
	public void remove( String url ) {
		SharedPreferences.Editor editor = prefs.edit();
		for( ValueType type : saveableTypes )
			editor.remove( makeKey( url, type ) );
		updateFollowedPostList( url, Boolean.toString( false ), editor );
		editor.commit();
	}
}
