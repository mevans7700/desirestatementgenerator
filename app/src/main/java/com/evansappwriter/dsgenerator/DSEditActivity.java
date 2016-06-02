package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DSEditActivity extends DSActivity {
	private EditText mBox21;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editds);
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mBox21 = (EditText) findViewById(R.id.Box21);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(StatementDbAdpater.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(StatementDbAdpater.KEY_ROWID)
                                    : null;
        }
        
        setResult(mRowId.intValue());
                
        Button updateButton = (Button) findViewById(R.id.Button_4);        
        updateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	saveState();
            	finish();
            }
          
        });
    }
    
    private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX21};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            mBox21.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21)));            
            
        }
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(StatementDbAdpater.KEY_ROWID, mRowId);
	}
        

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
        String box21 = mBox21.getText().toString();
        
        if (mRowId != null) {
        	ContentValues args = new ContentValues();
            args.put(StatementDbAdpater.KEY_BOX21, box21);
            mDbHelper.updateEntry(mRowId, args);              
        } 
    }
}