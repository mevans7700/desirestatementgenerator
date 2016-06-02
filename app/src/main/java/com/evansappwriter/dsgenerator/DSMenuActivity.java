package com.evansappwriter.dsgenerator;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DSMenuActivity extends ListActivity {
	SharedPreferences mPrefSettings;
	ArrayList<DisplayItem> mMenuList;
	ListAdapter mAdapter;
	String dsNameText;
	String dsStatementText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);            
        
        // set up the menu list
		mMenuList = new ArrayList<DisplayItem>();
		
		// set up the adapter
        mAdapter = new ListAdapter(this, R.layout.menu, mMenuList);

        // and set the adapter of the list view to the adapter
        setListAdapter(mAdapter);
        
        // populate the menu list
		initList(); 
        
        // inform the adapter that the data set has changed, so the list will be
        // redrawn.
        mAdapter.notifyDataSetChanged();                       
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		
		populateFields();
		
		mAdapter.notifyDataSetChanged();
		
	}
    
    private void populateFields() {
		// Retrieve the shared preferences
        mPrefSettings = getSharedPreferences(DSActivity.DS_PREFERENCES, Context.MODE_PRIVATE);
        dsNameText = (mPrefSettings.getString(DSActivity.DS_PREFERENCES_STATEMENTNAME, ""));
        dsStatementText = (mPrefSettings.getString(DSActivity.DS_PREFERENCES_STATEMENT, ""));
	}


	void initList() {
		mMenuList.add(new DisplayItem(null,""));
		mMenuList.add(new DisplayItem(getResources().getDrawable(R.drawable.overviewicon),
				getResources().getString(R.string.menu_item_overview)));
		mMenuList.add(new DisplayItem(getResources().getDrawable(R.drawable.mydsicon),
				getResources().getString(R.string.menu_item_myds)));
		mMenuList.add(new DisplayItem(getResources().getDrawable(R.drawable.writedsicon),
				getResources().getString(R.string.menu_item_write_new)));
		mMenuList.add(new DisplayItem(getResources().getDrawable(R.drawable.onlineicon),
				getResources().getString(R.string.menu_item_online_content)));		
		mMenuList.add(new DisplayItem(getResources().getDrawable(R.drawable.abouticon),
				getResources().getString(R.string.menu_item_about)));
	}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
        case 0:
        	break;
        case 1:
        	startActivity(new Intent(this,DSOverviewActivity.class));
        	break;
        case 2:
        	startActivity(new Intent(this,DSMyDSActivity.class));
        	break;
        case 3:
        	Intent i = new Intent(this,DSStep1Activity.class);
        	i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
        	startActivity(i);
        	break;
        case 4:
        	startActivity(new Intent(this,DSOnlineContentActivity.class));
        	break; 	
        case 5:
        	startActivity(new Intent(this,DSAboutActivity.class));
        	break;
        }
    }
    
    private class DisplayItem {
		private Drawable image;
		private String text;
		
		public DisplayItem(Drawable image, String text) {
			super();
			this.image = image;
			this.text = text;
		}

		public Drawable getImage() {
			return image;
		}		

		public String getText() {
			return text;
		}			
	}
    
    private class ListAdapter extends ArrayAdapter<DisplayItem> {
		ArrayList<DisplayItem> items;

	    public ListAdapter(Context context, int resource,
	                    ArrayList<DisplayItem> items) {
	            super(context, resource, items);
	            this.items = items;
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

	        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        if (position == 0) {
	        	v = vi.inflate(R.layout.menu_item_ds, null);
	        } else {
	        	v = vi.inflate(R.layout.menu_item, null);
	        }	        

	        // get the current object from the list
	        DisplayItem current = items.get(position);

	        if (position == 0) {
	        	TextView dsitem1 = (TextView) v.findViewById(R.id.Selected_DSName);
	        	TextView dsitem2 = (TextView) v.findViewById(R.id.Selected_DS);
	        	        	 
	        	dsitem1.setText(dsNameText);
	        	dsitem2.setText(dsStatementText);
	        } else {
	        	// get the views for setting data after
		        ImageView image = (ImageView) v.findViewById(R.id.menu_icon);
		        TextView item = (TextView) v.findViewById(R.id.menu_text);

		        // and set the values... 
		        image.setImageDrawable(current.getImage());
		        item.setText(current.getText());
	        }

	        // return the view
	        return v;			
		}   		
	}   
}