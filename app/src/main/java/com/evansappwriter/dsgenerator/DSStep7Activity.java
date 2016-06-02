package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DSStep7Activity extends DSActivity {
	private TextView mAnswerText;
	private TextView mAnswerText2;
	private EditText mBox10;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step7);
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mAnswerText = (TextView) findViewById(R.id.TextView_Box8);
        mAnswerText2 = (TextView) findViewById(R.id.TextView_Box9);
        mBox10 = (EditText) findViewById(R.id.Box10);
        
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
            	Intent i = new Intent(DSStep7Activity.this, DSStep8Activity.class);
                i.putExtra(StatementDbAdpater.KEY_ROWID, mRowId);
                startActivity(i); 
            }
          
        }); 
    }
    
    private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX8,StatementDbAdpater.KEY_BOX9,StatementDbAdpater.KEY_BOX10};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            mAnswerText.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX8)));
            mAnswerText2.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX9)));
            mBox10.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX10)));
            
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
        String box10 = mBox10.getText().toString();
        
        if (mRowId != null) {
        	ContentValues args = new ContentValues();
            args.put(StatementDbAdpater.KEY_BOX10, box10);
            mDbHelper.updateEntry(mRowId, args);            
        } 
    }
}