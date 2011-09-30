package com.ChewieLouie.Topical;

import android.os.AsyncTask;

public interface PostThreadExecuterIfc {

	public abstract void execute( AsyncTask<Void, Void, Boolean> task );
}