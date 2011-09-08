package com.ChewieLouie.Topical;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TopicalActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void search( View view ) {
    	EditText editText = (EditText)findViewById( R.id.SearchText );
    	GooglePlusIfc.search( editText.getText().toString() );
    }
}