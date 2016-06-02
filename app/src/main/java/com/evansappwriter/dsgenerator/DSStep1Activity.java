package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DSStep1Activity extends DSActivity {
	private EditText mBox1;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step1);
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mBox1 = (EditText) findViewById(R.id.Box1);

              
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(StatementDbAdpater.KEY_ROWID);
        
        Button continueButton = (Button) findViewById(R.id.Button_1);        
        continueButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	String s = mBox1.getText().toString();
            	if (s == null || s.trim().length() == 0) {
            		Toast.makeText(DSStep1Activity.this, "Please choose a Desire before continuing...", Toast.LENGTH_LONG).show();
            	} else {
            		saveState();
                	Intent i = new Intent(DSStep1Activity.this, DSStep2Activity.class);
                    i.putExtra(StatementDbAdpater.KEY_ROWID, mRowId);
                    startActivity(i);                 
                    finish();
            	}
            	
                
            }
          
        });        
    }
    
    private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX1};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            mBox1.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX1)));
            
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
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}
    
	private void saveState() {
        String box1 = mBox1.getText().toString();
        
        if (mRowId == null) {
            long id = mDbHelper.createEntry(box1);
            if (id > 0) {
                mRowId = id;
            }
        } else {
        	ContentValues args = new ContentValues();
            args.put(StatementDbAdpater.KEY_BOX1, box1);
            mDbHelper.updateEntry(mRowId, args);
        }
    }
}