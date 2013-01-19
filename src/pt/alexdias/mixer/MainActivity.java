package pt.alexdias.mixer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        
        // iterate through files in the folder and set the MediaMetadataRetriever source to each of them
        
        File[] trackFiles = new File("/sdcard/mixer").listFiles();
        
    	String toPlay = "";
        
        for(File f : trackFiles) {
        	
        	Log.v("TEST", "Track name: "+f.getName());
        
        	// get their metadata (artist and title) here
        	
        	mmr.setDataSource(f.getAbsolutePath());
        	
        	Log.v("TEST", "Track artist: "+ mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        	Log.v("TEST", "Track name: "+ mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        	
        	URL echonestSearch;
        	String jsonString = "";
        	
        	toPlay = f.getAbsolutePath();
        	
        	try {
				echonestSearch = new URL("http://developer.echonest.com/api/v4/song/search?api_key=" +
						"M3ATLVAJVEGQGRJWN&format=json&artist=" +
						mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).replace(" " , "%20") +
						"&title=" +
						mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).replace(" " , "%20")
						);
			
				jsonString = new NetworkAccessTask().execute(echonestSearch).get();
				

	        Log.v("TEST", "Response: "+ jsonString);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

        	
        }
        
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("toplay", toPlay);
        startActivity(intent);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
