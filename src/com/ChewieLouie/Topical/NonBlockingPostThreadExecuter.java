package com.ChewieLouie.Topical;

import android.os.AsyncTask;

public class NonBlockingPostThreadExecuter implements PostThreadExecuterIfc {
	@Override
	public void execute( AsyncTask<Void, Void, Boolean> task ) {
		task.execute();
	}
}
