package com.evansappwriter.dsgenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class DSOnlineContentActivity extends DSActivity {
	ImageView mBanner;
	int mProgressCounter = 0;
	WebcontentDownLoader videoLoader;
	WebcontentDownLoader audioLoader;
	TabHost host;
	ListAdapter myVideoListAdapter;	
	ListAdapter myAudioListAdapter;
	ListView videolv;
	ListView audiolv;
	ArrayList<DisplayItem> mVideoList;
	ArrayList<DisplayItem> mAudioList;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.online_content);
        
        // Based on orientation set the banner to appropriate one
        mBanner = (ImageView) findViewById(R.id.bannerImage);
        setBanner();
        
        // Set up the tabs
        host = (TabHost) findViewById(R.id.TabHost01);
        host.setup();
        
        // Videos tab
        TabSpec videoTab = host.newTabSpec("videoTab");
        videoTab.setIndicator(getResources().getString(R.string.online_content_videos),
        		getResources().getDrawable(R.drawable.videos));
        videoTab.setContent(R.id.videoList);
        host.addTab(videoTab);
        
        // Audio tab
        TabSpec audioTab = host.newTabSpec("audioTab");
        audioTab.setIndicator(getResources().getString(R.string.online_content_audio),
        		getResources().getDrawable(R.drawable.audio));
        audioTab.setContent(R.id.audioList);
        host.addTab(audioTab);
        
        for(int i=0;i<host.getTabWidget().getChildCount();i++) {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.BLUE);
        }
        
        // Set the default tab
        host.setCurrentTabByTag("videoTab");
        
        // ********************************** //
        // set up the video list
        mVideoList = new ArrayList<DisplayItem>();        
        videolv = (ListView) findViewById(R.id.videoList);
        
        // set up the video adapter
        myVideoListAdapter = new ListAdapter(this, R.id.videoList, mVideoList);
        videolv.setAdapter(myVideoListAdapter);        
                       
        // Get Video Data
        videoLoader = new WebcontentDownLoader();
        videoLoader.execute(DSActivity.DS_SERVER_CONTENT + "&type=1", mVideoList, myVideoListAdapter);
        
        // ********************************** //
        // set up the audio list
        mAudioList = new ArrayList<DisplayItem>();        
        audiolv = (ListView) findViewById(R.id.audioList);
        
        // set up the audio adapter
        myAudioListAdapter = new ListAdapter(this, R.id.audioList, mAudioList);
        audiolv.setAdapter(myAudioListAdapter);   
        
        // Get Audio Data
        audioLoader = new WebcontentDownLoader();
        audioLoader.execute(DSActivity.DS_SERVER_CONTENT + "&type=3", mAudioList, myAudioListAdapter);          
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setBanner();	        
	}
    
    @Override
	protected void onPause() {
		if (videoLoader != null && videoLoader.getStatus() != AsyncTask.Status.FINISHED) {
			videoLoader.cancel(true);			
		}
		if (audioLoader != null && audioLoader.getStatus() != AsyncTask.Status.FINISHED) {
			audioLoader.cancel(true);			
		}
		super.onPause();
	}
    
    public void setBanner() {
    	Display getOrient = getWindowManager().getDefaultDisplay();
		if (getOrient.getOrientation() == Configuration.ORIENTATION_UNDEFINED) {
			mBanner.setImageResource(R.drawable.online_content_portrait);
		} else {
			mBanner.setImageResource(R.drawable.online_content);
		}
    }
    
    public void myClickHandler(View v) {
    	String link = "";
    	switch (host.getCurrentTab()) {
    	case 0:
    		link = mVideoList.get(v.getId()).getLink();
    		break;
    	case 1:
    		link = mAudioList.get(v.getId()).getLink();
    		break;
    	}
    	
    	Uri uri = Uri.parse(link);
    	Intent i = new Intent(Intent.ACTION_VIEW, uri);
    	startActivity(i);       	
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
        	ViewWrapper wrapper = null;

        	// if we got no view, inflate one of our rows
        	if (convertView == null) {
        		LayoutInflater vi = (LayoutInflater) getContext()
                	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		convertView = vi.inflate(R.layout.online_content_row, null);
        		wrapper = new ViewWrapper(convertView);
        		        		
        		convertView.setTag(wrapper);
        	} else {
        		wrapper = (ViewWrapper)convertView.getTag();
        	}       	
        	
        	// get the current object from the list
        	DisplayItem row = items.get(position);
            
        	// get the views for setting data after and set the values...         	
        	wrapper.getItem().setText("");
        	wrapper.getItem2().setText(row.getTitle());
        	wrapper.getItem3().setId(position);
        	
        	// return the view
        	return convertView;			 
        }   				
    }
    
    private class DisplayItem {
		private String title;
		private String link;
		
		public DisplayItem(String title, String link) {
			super();
			this.title = title;
			this.link = link;
		}

		public String getTitle() {
			return title;
		}		

		public String getLink() {
			return link;
		}	
	}     
    
    private class WebcontentDownLoader extends AsyncTask<Object, String, Boolean> {
    	@SuppressWarnings("unused")
		private static final String DEBUG_TAG = "WebcontentDownLoader";
    	ArrayList<DisplayItem> items;
    	ListAdapter adapter;
    	
    	@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			String pathToContent = (String) params[0];
			items = (ArrayList<DisplayItem>) params[1];	
			adapter = (ListAdapter) params[2];
			
			XmlPullParser content = null;
			try {
				URL xmlUrl = new URL(pathToContent);
				content = XmlPullParserFactory.newInstance().newPullParser();
				content.setInput(xmlUrl.openStream(), null);								
			} catch (XmlPullParserException e) {
				content = null;
            } catch (IOException e) {
            	content = null;
            }
            
            if (content != null) {
                try {
                    processContent(content);
                    result = true;
                } catch (XmlPullParserException e) {
                    //Log.e(DEBUG_TAG, "Pull Parser failure", e);
                } catch (IOException e) {
                    //Log.e(DEBUG_TAG, "IO Exception parsing XML", e);
                }
            }
			
			return result;			
		}
		
		/**
	     * Churn through an XML online content information and populate a the list
	     * 
	     * @param contents
	     *            A standard {@code XmlPullParser} containing the scores
	     * @throws XmlPullParserException
	     *             Thrown on XML errors
	     * @throws IOException
	     *             Thrown on IO errors reading the XML
	     */
	    private void processContent(XmlPullParser contents) throws XmlPullParserException, IOException {
	        int eventType = -1;
	        boolean bContent = false;
	        
	        // Find Content records from XML
	        while (eventType != XmlResourceParser.END_DOCUMENT) {
	            if (eventType == XmlResourceParser.START_TAG) {
	                // Get the name of the tag (eg webcontent)
	                String strName = contents.getName();
	                if (strName.equals(DS_XML_TAG_WEBCONTENT)) {
	                	bContent = true;
	                    String contentTitle = contents.getAttributeValue(null, DS_XML_TAG_TITLE);
	                    String contentSite = contents.getAttributeValue(null, DS_XML_TAG_SITE);
	                    items.add(new DisplayItem(contentTitle,contentSite));
	                }
	            }
	            eventType = contents.next();
	        }
	        
	        // Refresh the list
	        if (bContent == true) {
	        	publishProgress();
	        }
	    }
	    
	    @Override
		protected void onPreExecute() {
			mProgressCounter++;
			DSOnlineContentActivity.this.setProgressBarIndeterminateVisibility(true);
		} 
	    
	    @Override
		protected void onPostExecute(Boolean result) {
			mProgressCounter--;
			if (mProgressCounter <=0) {
				mProgressCounter = 0;
				DSOnlineContentActivity.this.setProgressBarIndeterminateVisibility(false);
			}
		}
	    
	    @Override
		protected void onCancelled() {
			mProgressCounter--;
			if (mProgressCounter <=0) {
				mProgressCounter = 0;
				DSOnlineContentActivity.this.setProgressBarIndeterminateVisibility(false);
			}
		}
	    
	    @Override
		protected void onProgressUpdate(String... values) {
	    	adapter.notifyDataSetChanged();
		}		   	
    }
}