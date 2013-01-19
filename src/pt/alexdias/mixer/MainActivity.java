package pt.alexdias.mixer;

import java.io.File;
import pt.alexdias.mixer.domain.Track;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // iterate through files in the folder and set the MediaMetadataRetriever source to each of them
        
        File[] trackFiles = new File("/sdcard/mixer").listFiles();
        
        for(File f : trackFiles) {
        	Track track = new Track(f.getAbsolutePath());
        	Log.v("TEST", track.getBPM().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
