package com.evansappwriter.dsgenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TabHost.TabSpec;

public class DSMyDSActivity extends DSActivity {
	SharedPreferences mPrefSettings;
	private static final int FINISHTAB = 0;
	private static final int SELECT_ID = Menu.FIRST;
	private static final int VIEW_ID = Menu.FIRST + 1;
	private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int DELETE_ID = Menu.FIRST + 3;
    private ListView mStatementList;
    private ListView mUnfinishList;
    private TabHost mHost;
	private StatementDbAdpater mDbHelper;
	private Cursor dsCursor;
	private boolean mEditDS = false;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myds);
                
        // Set up the tabs
        mHost = (TabHost) findViewById(R.id.TabHost01);
        mHost.setup();
               
        // Finish tab
        TabSpec finishTab = mHost.newTabSpec("finishTab");
        finishTab.setIndicator(getResources().getString(R.string.my_ds_tab_finish),
        		getResources().getDrawable(android.R.drawable.star_on));
        finishTab.setContent(R.id.finish_DS);
        mHost.addTab(finishTab);
        
        // unfinish tab
        TabSpec unfinishTab = mHost.newTabSpec("unfinishTab");
        unfinishTab.setIndicator(getResources().getString(R.string.my_ds_tab_unfinish),
        		getResources().getDrawable(android.R.drawable.star_on));
        unfinishTab.setContent(R.id.unfinish_DS);
        mHost.addTab(unfinishTab);
        
        for(int i=0;i<mHost.getTabWidget().getChildCount();i++) {
            TextView tv = (TextView) mHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.BLUE);
        }
        
        // Set the default tab
        mHost.setCurrentTabByTag("finishTab");
        
        mDbHelper = new StatementDbAdpater(this);
        mDbHelper.open();
        
        mStatementList = (ListView) findViewById(R.id.finish_DS);  
        fillData(mStatementList, StatementDbAdpater.KEY_BOX22, StatementDbAdpater.KEY_BOX21, 0);
        registerForContextMenu(mStatementList); 
                
        mUnfinishList = (ListView) findViewById(R.id.unfinish_DS); 
        fillData(mUnfinishList, StatementDbAdpater.KEY_BOX22, StatementDbAdpater.KEY_BOX1, 1);
        registerForContextMenu(mUnfinishList);    
        
        /*mStatementList.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		String[] columns = {StatementDbAdpater.KEY_BOX21,StatementDbAdpater.KEY_BOX22};        		
        		Cursor entry = mDbHelper.fetchEntry(id, columns);
        		startManagingCursor(entry);
                String dsName = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX22));
                String dsStatement = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21));
                
        		// Retrieve the shared preferences
                mPrefSettings = getSharedPreferences(DS_PREFERENCES, Context.MODE_PRIVATE);
                // Set the Selected DS Statement
                Editor editor = mPrefSettings.edit();
                editor.putString(DS_PREFERENCES_STATEMENTNAME, dsName);
                editor.putString(DS_PREFERENCES_STATEMENT, dsStatement);
                editor.commit();
                
                finish();
        	}        	
        }); */														
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}
    
    private void fillData(ListView list, String item1, String item2, int type) {
    	String[] columns = {StatementDbAdpater.KEY_ROWID,item1,item2};
    	dsCursor = mDbHelper.fetchAllEntries(columns,type);
        startManagingCursor(dsCursor);    
        
        
        // Create an array to specify the fields we want to display in the list 
        String[] from = new String[]{item1,item2};
        
        // and an array of the fields we want to bind those fields to 
        int[] to = new int[]{R.id.item1, R.id.item2};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter adapt = 
        	    new SimpleCursorAdapter(this, R.layout.my_ds_row, dsCursor, from, to);
        list.setAdapter(adapt);    	
        adapt.notifyDataSetChanged();
    }
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (mHost.getCurrentTab() == FINISHTAB) {
			menu.add(0, SELECT_ID, 0, R.string.context_menu_select);
			menu.add(0, VIEW_ID, 1, R.string.context_menu_view);
			menu.add(0, EDIT_ID, 2, R.string.context_menu_edit);
	        menu.add(0, DELETE_ID, 3, R.string.context_menu_delete);
		} else {
			menu.add(0, EDIT_ID, 1, R.string.context_menu_edit);
	        menu.add(0, DELETE_ID, 2, R.string.context_menu_delete);
		}             
	}
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	
		switch(item.getItemId()) {
		case SELECT_ID:   
	    	  
    		String[] columns = {StatementDbAdpater.KEY_BOX21,StatementDbAdpater.KEY_BOX22};
    		Cursor entry = mDbHelper.fetchEntry(info.id, columns);
            startManagingCursor(entry);
            String dsName = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX22));
            String dsStatement = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21));
            
            // Retrieve the shared preferences
            mPrefSettings = getSharedPreferences(DS_PREFERENCES, Context.MODE_PRIVATE);
            // Set the Selected DS Statement
            Editor editor = mPrefSettings.edit();
            editor.putLong(DS_PREFERENCES_STATEMENTID, info.id);
            editor.putString(DS_PREFERENCES_STATEMENTNAME, dsName);
            editor.putString(DS_PREFERENCES_STATEMENT, dsStatement);
            editor.commit();
            
            finish();
	        return true;
		case VIEW_ID:
			Intent intent = new Intent(this, DSViewActivity.class);
			intent.putExtra(StatementDbAdpater.KEY_ROWID, info.id);
			startActivity(intent);
			return true;
		case EDIT_ID:	
			if (mHost.getCurrentTab() == FINISHTAB) {
				// Retrieve the shared preferences
	            mPrefSettings = getSharedPreferences(DS_PREFERENCES, Context.MODE_PRIVATE);
				Long dsid = mPrefSettings.getLong(DSActivity.DS_PREFERENCES_STATEMENTID, 0);
				if (dsid == info.id) {
					mEditDS = true;
				}
				Intent i = new Intent(this, DSEditActivity.class);
				i.putExtra(StatementDbAdpater.KEY_ROWID, info.id);
				startActivityForResult(i, 0);
			} else {				
				Intent i2 = new Intent(this, DSStep2Activity.class);
				i2.putExtra(StatementDbAdpater.KEY_ROWID, info.id);
				startActivity(i2);
			}								
			return true;
    	case DELETE_ID:    		
	        mDbHelper.deleteEntry(info.id);
	        if (mHost.getCurrentTab() == FINISHTAB) {
	        	fillData(mStatementList, StatementDbAdpater.KEY_BOX22, StatementDbAdpater.KEY_BOX21, mHost.getCurrentTab());
	        	// Retrieve the shared preferences
	            mPrefSettings = getSharedPreferences(DS_PREFERENCES, Context.MODE_PRIVATE);
	            Long dsid = mPrefSettings.getLong(DSActivity.DS_PREFERENCES_STATEMENTID, 0);
				if (dsid == info.id) {
					// Set the Selected DS Statement
		            Editor ed = mPrefSettings.edit();
		            ed.putLong(DS_PREFERENCES_STATEMENTID, 0);
		            ed.putString(DS_PREFERENCES_STATEMENTNAME, "");
		            ed.putString(DS_PREFERENCES_STATEMENT, "");
		            ed.commit();
				}	            
	        } else {
	        	fillData(mUnfinishList, StatementDbAdpater.KEY_BOX22, StatementDbAdpater.KEY_BOX1, mHost.getCurrentTab());
	        } 
	        return true;    	 
		}
		
		return super.onContextItemSelected(item);
	}  
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (mEditDS) {
        	long dsID = resultCode;
        	String[] columns = {StatementDbAdpater.KEY_BOX21,StatementDbAdpater.KEY_BOX22};
    		Cursor entry = mDbHelper.fetchEntry(dsID, columns);
            startManagingCursor(entry);
            String dsName = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX22));
            String dsStatement = entry.getString(entry.getColumnIndexOrThrow(StatementDbAdpater.KEY_BOX21));
            
            // Retrieve the shared preferences
            mPrefSettings = getSharedPreferences(DS_PREFERENCES, Context.MODE_PRIVATE);
            // Set the Selected DS Statement
            Editor editor = mPrefSettings.edit();
            editor.putLong(DS_PREFERENCES_STATEMENTID, dsID);
            editor.putString(DS_PREFERENCES_STATEMENTNAME, dsName);
            editor.putString(DS_PREFERENCES_STATEMENT, dsStatement);
            editor.commit();
            mEditDS = false;
        }
                
    }
}