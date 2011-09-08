package com.ChewieLouie.Topical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TopicalActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void search( View view ) {
    	EditText editText = (EditText)findViewById( R.id.SearchText );
    	Intent intent = new Intent().setClass( getApplicationContext(), ConversationListActivity.class );
    	intent.putExtra( TopicalConstants.IntentExtraKey_Conversations,
    			GooglePlusIfc.search( editText.getText().toString() ) );
    	startActivity( intent );
    }
}