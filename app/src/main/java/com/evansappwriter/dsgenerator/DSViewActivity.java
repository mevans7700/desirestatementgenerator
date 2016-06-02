package com.evansappwriter.dsgenerator;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DSViewActivity extends DSActivity {
	private TextView mAnswerText;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_desire);
		
		mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
                                                   
        mAnswerText = (TextView) findViewById(R.id.TextView_Box21);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(StatementDbAdpater.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(StatementDbAdpater.KEY_ROWID)
                                    : null;
        }
        
        populateFields();
        
        Button updateButton = (Button) findViewById(R.id.Button_5);        
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	finish();
            }
          
        });
	}
	
	private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX21};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            mAnswerText.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21)));            
            
        }
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}
}
