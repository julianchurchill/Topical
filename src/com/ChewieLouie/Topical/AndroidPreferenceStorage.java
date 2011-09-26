package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;

public class AndroidPreferenceStorage implements PersistentStorageIfc {

	private static final String allFollowedPostsKey = "AllFollowedPostURLs";

	private SharedPreferences prefs = null;
	private Map<ValueType, String> typeToString = new HashMap<ValueType, String>();
	
	public AndroidPreferenceStorage( Activity activity ) {
		this.prefs = activity.getPreferences( Activity.MODE_PRIVATE );
		typeToString.put( ValueType.IS_FOLLOWED, "IsFollowed" );
		typeToString.put( ValueType.LAST_VIEWED_MODIFICATION_TIME, "LastViewedModificationTime" );
		typeToString.put( ValueType.POST_ID, "PostID" );
		typeToString.put( ValueType.TITLE, "Title" );
		typeToString.put( ValueType.SUMMARY, "Summary" );
	}
	
	@Override
	public void save( String url, ValueType type, String value ) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString( makeKey( url, type ), value );
		if( type == ValueType.IS_FOLLOWED )
			updateFollowedPostList( url, value, editor );
		editor.commit();
	}

	@Override
	public String load( String url, ValueType type ) {
		return prefs.getString( makeKey( url, type ), "" );
	}

	@Override
	public List<String> getAllPostURLsWhereFollowingIsTrue() {
		String postURLsString = prefs.getString( allFollowedPostsKey, "" );
		if( postURLsString.isEmpty() )
			return new ArrayList<String>();
		return new ArrayList<String>( Arrays.asList( postURLsString.split( "," ) ) );
	}

	private String makeKey( String url, ValueType type ) {
		return typeToString.get( type ) + url;
	}

	private void updateFollowedPostList( String url, String nowFollowing, SharedPreferences.Editor editor ) {
		Set<String> setOfPostURLs = new HashSet<String>( getAllPostURLsWhereFollowingIsTrue() );
		if( Boolean.parseBoolean( nowFollowing ) == true )
			setOfPostURLs.add( url );
		else
			setOfPostURLs.remove( url );
		if( setOfPostURLs.isEmpty() == false )
		{
			editor.putString( allFollowedPostsKey, join( ",", setOfPostURLs.toArray( new String[0] ) ) );
		}
		else
			editor.remove( allFollowedPostsKey );
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
