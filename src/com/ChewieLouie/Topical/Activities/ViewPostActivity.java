package com.ChewieLouie.Topical.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ChewieLouie.Topical.AndroidViewPost;
import com.ChewieLouie.Topical.Post;
import com.ChewieLouie.Topical.R;
import com.ChewieLouie.Topical.TopicalConstants;

public class ViewPostActivity extends Activity {

	private static final int FOLLOW_MENU_ITEM = 0;
	private static final int UNFOLLOW_MENU_ITEM = 1;
	private TextView authorTextView = null;
	private ImageView authorImageView = null;
	private TextView textTextView = null;
	private Post post = null;
	private AndroidViewPost androidViewPost = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView( R.layout.view_post );
        findViews();
		androidViewPost = new AndroidViewPost( this, authorTextView, authorImageView, textTextView );
    }

	private void findViews() {
		authorImageView = (ImageView)findViewById( R.id.authorImage );
		authorTextView = (TextView)findViewById( R.id.author );
		textTextView = (TextView)findViewById( R.id.text );
	}

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
    	menu.clear();
    	if( post.isFollowed() )
    		menu.add( Menu.NONE, UNFOLLOW_MENU_ITEM, Menu.NONE, getText( R.string.unfollow ) );
    	else
    		menu.add( Menu.NONE, FOLLOW_MENU_ITEM, Menu.NONE, getText( R.string.follow ) );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
    	boolean retVal = true;
        switch( item.getItemId() )
        {
        	case FOLLOW_MENU_ITEM:
        		post.follow();
        		break;
	        case UNFOLLOW_MENU_ITEM:
        		post.unfollow();
	            break;
	        default:
	            retVal = super.onOptionsItemSelected( item );
	            break;
        }
        return retVal;
    }

    @Override
	protected void onResume() {
		super.onResume();
		final int index = getIntent().getIntExtra( TopicalConstants.IntentExtraKey_ViewTopicIndex, -1 );
		post = TopicalActivity.currentPosts.get( index );
		post.markAsViewedBeforeShowing();
		post.forceGooglePlusRefresh();
		post.show( androidViewPost );
		post.showComments( androidViewPost );
	}
}
