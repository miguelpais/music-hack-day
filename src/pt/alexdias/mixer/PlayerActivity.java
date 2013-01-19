package pt.alexdias.mixer;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PlayerActivity extends Activity {

	private SoundPool pool;
	private Integer s1;
	private Integer s2;
	
	private Integer play1;
	private Integer play2;
	
	private boolean loaded = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        Intent i = getIntent();
        String firstToPlayPath = i.getStringExtra("firsttoplay");
        String nextToPlayPath = i.getStringExtra("secondtoplay");
        
        setContentView(R.layout.activity_player);
        
        pool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        
        s1 = pool.load(firstToPlayPath, 1);
        s2 = pool.load(nextToPlayPath, 1);
        
        pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
				loaded = true;
			}
        	
        	
        });
        
        Button b1 = (Button) findViewById(R.id.ImageButton01);
        b1.setOnClickListener(
        		new View.OnClickListener() {
        		    public void onClick(View v) {
        		    	if(loaded) {
	        		    	AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	        		    	float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	        		    	float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        		    	float volume = streamVolumeCurrent / streamVolumeMax;
	        		    	play1 = pool.play(s1, volume, volume, 1, 0, 1.0f);
	        		    	play2 = pool.play(s2, volume, volume, 1, 0, 1.0f);
        		    	}
        		    	else {
        		    		Log.v("INFO","Sound not yet loaded, please wait.");
        		    	}
        		    }
        		  }
        		
        );
        
        
        Button b2 = (Button) findViewById(R.id.PauseButton);
        b2.setOnClickListener(
        		new View.OnClickListener() {
					@Override
					public void onClick(View v) {
							pool.pause(play1);
							pool.pause(play2);
					}
				}
        );
        
        
    }
	
	
}
