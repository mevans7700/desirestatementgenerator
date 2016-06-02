package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DSStep5Activity extends DSActivity {
	private TextView mAnswerText;
	private TextView mAnswerText2;
	private TextView mAnswerText3;
	private EditText mBox8;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step5);
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mAnswerText = (TextView) findViewById(R.id.TextView_Box3);
        mAnswerText2 = (TextView) findViewById(R.id.TextView_Box6);
        mAnswerText3 = (TextView) findViewById(R.id.TextView_Box7);
        mBox8 = (EditText) findViewById(R.id.Box8);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(StatementDbAdpater.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(StatementDbAdpater.KEY_ROWID)
                                    : null;
        }
        
        Button continueButton = (Button) findViewById(R.id.Button_1);        
        continueButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	saveState();
            	Intent i = new Intent(DSStep5Activity.this, DSStep6Activity.class);
                i.putExtra(StatementDbAdpater.KEY_ROWID, mRowId);
                startActivity(i); 
            }
          
        }); 
    }
    
    private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX3,StatementDbAdpater.KEY_BOX6,StatementDbAdpater.KEY_BOX7,StatementDbAdpater.KEY_BOX8};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            mAnswerText.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX3)));
            mAnswerText2.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX6)));
            mAnswerText3.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX7)));
            mBox8.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX8)));
            
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
        String box8 = mBox8.getText().toString();
        
        if (mRowId != null) {
        	ContentValues args = new ContentValues();
            args.put(StatementDbAdpater.KEY_BOX8, box8);
            mDbHelper.updateEntry(mRowId, args);            
        } 
    }
}