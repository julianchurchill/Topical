package com.ChewieLouie.Topical.test;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

import com.ChewieLouie.Topical.PostThreadExecuterIfc;

public class BlockingPostThreadExecuter implements PostThreadExecuterIfc {
	@Override
	public void execute( AsyncTask<Void, Void, Boolean> task ) {
		try {
			task.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
