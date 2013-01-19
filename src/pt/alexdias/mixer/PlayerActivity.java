package pt.alexdias.mixer;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerActivity extends Activity {

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        Intent i = getIntent();
        String toPlayPath = i.getStringExtra("toplay");
        
        //TextView t = new TextView(this);
        //t.setText(toPlayPath);
        
        //setContentView(t);
        
        setContentView(R.layout.activity_player);
        
        final MediaPlayer mp = new MediaPlayer();
        
        try {
        	
			mp.setDataSource(toPlayPath);
			mp.setOnPreparedListener(new OnPreparedListener() {
	            public void onPrepared(MediaPlayer mp) {
	            }
	        });

			mp.prepare();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        Button b1 = (Button) findViewById(R.id.ImageButton01);
        b1.setOnClickListener(
        		new View.OnClickListener() {
        		    public void onClick(View v) {
        		     
        		    	
        		    	try {
        	                mp.start();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
        		    }
        		  }
        		
        );
        
        
        
        Button b2 = (Button) findViewById(R.id.PauseButton);
        b2.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mp.pause();
					}
				}
        );
        
        
    }
	
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}
	
}
