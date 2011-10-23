package com.ChewieLouie.Topical.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ChewieLouie.Topical.AndroidPreferenceStorage;
import com.ChewieLouie.Topical.GooglePlus;
import com.ChewieLouie.Topical.GooglePlusIfc.DataType;
import com.ChewieLouie.Topical.PersistentStorageIfc;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicWatcher;
import com.ChewieLouie.Topical.TopicalConstants;

public class TopicalActivity extends Activity {

	public static List<Post> currentPosts = null;
	public static TopicWatcher topicWatcher = null;
	public static PersistentStorageIfc storage = null;
	
	private static final int ABOUT_MENU_ITEM = 0;
	private static final int EXIT_MENU_ITEM = 1;

	public TopicalActivity() {
		super();
	}

	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
		storage = new AndroidPreferenceStorage( this );
		topicWatcher = new TopicWatcher( storage, GooglePlus.Make(), new TopicFactory() );
        setContentView( R.layout.main );
	}

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
    	menu.clear();
		menu.add( Menu.NONE, ABOUT_MENU_ITEM, Menu.NONE, getText( R.string.about ) );
		menu.add( Menu.NONE, EXIT_MENU_ITEM, Menu.NONE, getText( R.string.exit ) );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
    	boolean retVal = true;
        switch( item.getItemId() )
        {
			case ABOUT_MENU_ITEM:
				showAboutDialog();
				break;
        	case EXIT_MENU_ITEM:
        		finish();
        		break;
	        default:
	            retVal = super.onOptionsItemSelected( item );
	            break;
        }
        return retVal;
    }
    
    private void showAboutDialog() {
    	Dialog dialog = new Dialog( this );
    	dialog.setContentView( R.layout.about_dialog );

        String versionName = "";
    	try {
            versionName = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
        } 
        catch ( PackageManager.NameNotFoundException e ) {
        }
        dialog.setTitle( getResources().getString( R.string.app_name ) + " " + versionName );

        dialog.show();
    }

    public void search( View view ) {
    	startActivity( new Intent().setClass( getApplicationContext(), SearchActivity.class ) );
    }

	public void showFollowedPosts( View view ) {
    	currentPosts = findFollowedPosts();
    	startTopicListActivityWithTitle( "Followed Posts" );
	}

	private List<Post> findFollowedPosts() {
    	List<Post> posts = new ArrayList<Post>();
    	for( String url : storage.getAllPostURLsWhereFollowingIsTrue() )
    		posts.add( createPostWithURL( url ) );
    	return posts;
    }

	private Post createPostWithURL( String url ) {
		Map<DataType,String> postInfo = new HashMap<DataType, String>();
		postInfo.put( DataType.URL, url );
		return new Post( postInfo, storage, GooglePlus.Make() );
	}

	private void startTopicListActivityWithTitle( String title ) {
		Intent intent = new Intent().setClass( getApplicationContext(), TopicListActivity.class );
		intent.putExtra( TopicalConstants.IntentExtraKey_TopicListTopic, title );
		startActivity( intent );
	}
	
	public void showWatchedTopics( View view ) {
    	startActivity( new Intent().setClass( getApplicationContext(), WatchedTopicsActivity.class ) );
	}
}
