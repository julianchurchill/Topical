package com.ChewieLouie.Topical.Activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.ChewieLouie.Topical.GooglePlus;
import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.GooglePlusSearchCallbackIfc;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicalConstants;

public class SearchActivity extends Activity implements GooglePlusSearchCallbackIfc {

	private EditText searchEditText = null;
	private String topic = "";

	public SearchActivity() {
		super();
	}

	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS );
        setContentView( R.layout.search );
    	searchEditText = (EditText)findViewById( R.id.SearchText );
	}

    public void search( View view ) {
    	searchForTopic( searchEditText.getText().toString().trim() );
    }

    private void searchForTopic( String topic ) {
    	this.topic  = topic;
    	setProgressBarIndeterminateVisibility( true );
    	GooglePlus.Make().search( topic, this );
    }

	@Override
	public void searchResults(List<Map<DataType, String>> results) {
    	List<Post> posts = new ArrayList<Post>();
		for( Map<DataType,String> result : results )
			posts.add( new Post( result, TopicalActivity.storage, GooglePlus.Make() ) );
    	setProgressBarIndeterminateVisibility( false );
    	if( posts.size() > 0 )
    		showTopicList( posts );
    	else
    		Toast.makeText( this, "No results found", Toast.LENGTH_LONG ).show();
	}

    private void showTopicList( List<Post> posts ) {
    	TopicalActivity.currentPosts = posts;
    	startTopicListActivityWithTitle( topic );
    }

	private void startTopicListActivityWithTitle( String title ) {
		Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
		intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, title );
		startActivity( intent );
	}	
}