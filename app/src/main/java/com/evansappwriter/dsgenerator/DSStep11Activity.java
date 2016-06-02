package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DSStep11Activity extends DSActivity {
	private TextView mAnswerText;
	private TextView mAnswerText2;
	private TextView mAnswerText3;
	private EditText mBox16;
	private EditText mBox17;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step11);
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mAnswerText = (TextView) findViewById(R.id.TextView_Box8);
        mAnswerText2 = (TextView) findViewById(R.id.TextView_Box11);
        mAnswerText3 = (TextView) findViewById(R.id.TextView_Box15);
        mBox16 = (EditText) findViewById(R.id.Box16);
        mBox17 = (EditText) findViewById(R.id.Box17);
        
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
            	Intent i = new Intent(DSStep11Activity.this, DSStep12Activity.class);
                i.putExtra(StatementDbAdpater.KEY_ROWID, mRowId);
                startActivity(i); 
            }
          
        }); 
    }
    
    private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX8,StatementDbAdpater.KEY_BOX11,StatementDbAdpater.KEY_BOX15,StatementDbAdpater.KEY_BOX16,StatementDbAdpater.KEY_BOX17};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            mAnswerText.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX8)));
            mAnswerText2.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX11)));
            mAnswerText3.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX15)));
            
            String s = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX16));
            if (s != null && s.length() > 0) {
            	mBox16.setText(entry.getString(
                		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX16)));
            } else {
            	String s1 = mAnswerText.getText().toString() + " " + mAnswerText2.getText().toString() 
            	            + " " + mAnswerText3.getText().toString();
            	mBox16.setText(s1);
            }
            
            mBox17.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX17)));
            
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
        String box16 = mBox16.getText().toString();
        String box17 = mBox17.getText().toString();
        
        if (mRowId != null) {
        	ContentValues args = new ContentValues();
            args.put(StatementDbAdpater.KEY_BOX16, box16);
            args.put(StatementDbAdpater.KEY_BOX17, box17);
            mDbHelper.updateEntry(mRowId, args);            
        } 
    }
}