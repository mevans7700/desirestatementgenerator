package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DSStep12Activity extends DSActivity {
	private TextView mAnswerText;
	private EditText mBox18;
	private EditText mBox19;
	private EditText mBox20;
	private EditText mBox22;
	private StatementDbAdpater mDbHelper;
	private Long mRowId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step12);
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mAnswerText = (TextView) findViewById(R.id.TextView_Box16_17);
        mBox18 = (EditText) findViewById(R.id.Box18);
        mBox19 = (EditText) findViewById(R.id.Box19);
        mBox20 = (EditText) findViewById(R.id.Box20);
        mBox22 = (EditText) findViewById(R.id.Box22);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(StatementDbAdpater.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(StatementDbAdpater.KEY_ROWID)
                                    : null;
        }
        
        Button finishButton = (Button) findViewById(R.id.Button_2);        
        finishButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	String s = mBox22.getText().toString();
            	if (s == null || s.trim().length() == 0) {
            		Toast.makeText(DSStep12Activity.this, "Please choose a name for your Desire Statment", Toast.LENGTH_LONG).show();
            	} else {
            		saveState(0);
            		Intent i = new Intent(DSStep12Activity.this, DSMenuActivity.class);
            		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            		startActivity(i);
            		startActivity(new Intent(DSStep12Activity.this, DSMyDSActivity.class));
            	}    	 
            }
          
        });
        Button adjustButton = (Button) findViewById(R.id.Button_3);        
        adjustButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	saveState(1);
            	Intent i = new Intent(DSStep12Activity.this, DSEditActivity.class);
                i.putExtra(StatementDbAdpater.KEY_ROWID, mRowId);
                startActivity(i);                 
            }
          
        });
    }
    
    private void populateFields() {
        if (mRowId != null) {
        	String[] columns = {StatementDbAdpater.KEY_BOX16,StatementDbAdpater.KEY_BOX17,StatementDbAdpater.KEY_BOX18,StatementDbAdpater.KEY_BOX19,StatementDbAdpater.KEY_BOX20,StatementDbAdpater.KEY_BOX21,StatementDbAdpater.KEY_BOX22};
            Cursor entry = mDbHelper.fetchEntry(mRowId, columns);
            startManagingCursor(entry);
            
            String s = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX17));
            String s1 = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21));
            if (s1 != null && s1.length() > 0) {
            	mAnswerText.setText(entry.getString(
                		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21)));
            } else if (s != null && s.length() > 0) {
            	mAnswerText.setText(entry.getString(
                		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX17)));
            } else {
            	mAnswerText.setText(entry.getString(
                		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX16)));
            }
            
            
            mBox18.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX18)));
            mBox19.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX19)));
            mBox20.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX20)));
            mBox22.setText(entry.getString(
            		entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX22)));
            
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
    
	private void saveState(int finish) {
        String box18 = mBox18.getText().toString();
        String box19 = mBox19.getText().toString();
        String box20 = mBox20.getText().toString();        
        String box21 = mAnswerText.getText().toString();        
        String box22 = mBox22.getText().toString();
        
        
        if (mRowId != null) {
        	ContentValues args = new ContentValues();
            args.put(StatementDbAdpater.KEY_BOX18, box18);
            args.put(StatementDbAdpater.KEY_BOX19, box19);
            args.put(StatementDbAdpater.KEY_BOX20, box20);
            args.put(StatementDbAdpater.KEY_BOX21, box21);
            args.put(StatementDbAdpater.KEY_BOX22, box22);
            args.put(StatementDbAdpater.KEY_BOX23, finish);
            mDbHelper.updateEntry(mRowId, args);            
        } 
    }
}