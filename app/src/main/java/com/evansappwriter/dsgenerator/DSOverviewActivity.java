package com.evansappwriter.dsgenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class DSOverviewActivity extends DSActivity implements OnClickListener {
	View mVideoButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);
        
        // Get Handle of ImageButton
        mVideoButton = findViewById(R.id.overview_video);
        mVideoButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
		Uri uri = Uri.parse(DS_OVERVIEW_VIDEO);
    	Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(webIntent);		
	}
}