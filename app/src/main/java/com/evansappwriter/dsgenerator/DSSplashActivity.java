package com.evansappwriter.dsgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class DSSplashActivity extends DSActivity {
	ImageView splashCover;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        splashCover = (ImageView)  findViewById(R.id.ImageViewCover);
    }
	
	private void startAnimating() {
	
		Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		splashCover.startAnimation(fade);
		
		fade.setAnimationListener(new AnimationListener() {
        	public void onAnimationEnd(Animation animation) {
        		
                startActivity(new Intent(DSSplashActivity.this, DSMenuActivity.class));            		
                DSSplashActivity.this.finish();
        	}

			public void onAnimationRepeat(Animation animation) {
				
			}

			public void onAnimationStart(Animation animation) {
							
			}
        }); 
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		splashCover.clearAnimation();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		startAnimating();
	}
}