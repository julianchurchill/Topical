package com.ChewieLouie.Topical;

import android.app.Activity;
import android.content.SharedPreferences;

public class AndroidPreferenceStorage implements PersistentStorageIfc {

	private SharedPreferences prefs = null;
	
	public AndroidPreferenceStorage( Activity activity ) {
		this.prefs = activity.getPreferences( Activity.MODE_PRIVATE );
	}
	
	@Override
	public void save( String key, String value ) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString( key, value );
		editor.commit();
	}

	@Override
	public String load( String key ) {
		return prefs.getString( key, "" );
	}
}
