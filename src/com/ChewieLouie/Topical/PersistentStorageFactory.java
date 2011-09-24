package com.ChewieLouie.Topical;

import android.app.Activity;

public class PersistentStorageFactory {
	private static Activity activity = null;
	private static PersistentStorageIfc storage = null;

	public static void setActivity( Activity act ) {
		activity = act;
	}

	public static PersistentStorageIfc create() {
		if( storage == null )
			storage = new AndroidPreferenceStorage( activity );
		return storage;
	}
}
