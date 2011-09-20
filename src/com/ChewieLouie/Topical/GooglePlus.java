package com.ChewieLouie.Topical;

import java.io.IOException;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Person;

public class GooglePlus implements GooglePlusIfc {

	private static final String googleAPIKey = "AIzaSyA1UkMy-J3PbX6ozp22P0KbV7XApguSb7s";

	private Plus plus = null;

	public GooglePlus() {
		plus = new Plus( new NetHttpTransport(), new GsonFactory() );
		plus.setKey( googleAPIKey );
	}
	
	@Override
	public String getPostContent( String postID ) {
		String content = "";
		Plus.Activities.Get request = plus.activities.get( postID );
		try {
			Activity activity = request.execute();
			if( activity != null )
			{
				content = activity.getPlusObject().getContent();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public String getAuthor( String authorID ) {
		String author = "";
		Plus.People.Get request = plus.people.get( authorID );
		try {
			Person person = request.execute();
			if( person != null )
			{
				author = person.getDisplayName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return author;
	}

	@Override
	public String getComments( String postID ) {
		String comments = "";
		Plus.Activities.Get request = plus.activities.get( postID );
		try {
			Activity activity = request.execute();
			if( activity != null )
			{
				comments = activity.getPlusObject().getReplies().toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return comments;
	}
}
