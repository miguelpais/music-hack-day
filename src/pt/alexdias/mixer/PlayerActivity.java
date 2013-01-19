package pt.alexdias.mixer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
	
	AudioTrack mAudioTrack = null;
	AudioTrack mAudioTrack2 = null;

	byte[] buffer;
	byte[] buffer2;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        Intent i = getIntent();
        String firstToPlayPath = i.getStringExtra("firsttoplay");
        String nextToPlayPath = i.getStringExtra("secondtoplay");
        
        setContentView(R.layout.activity_player);

		
        try {
			buffer = decode(firstToPlayPath, 0, 10000);
			buffer2 = decode(nextToPlayPath, 0, 10000);
			
			int min = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
			int min2 = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
			
			mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, min, AudioTrack.MODE_STREAM);
			mAudioTrack2 = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, min2, AudioTrack.MODE_STREAM);
			
			
			
			
			//Log.v("INFO","Current volume is: "+currentVolume);
			
			new Thread(new Runnable() {
			    public void run() {
			    	Looper.prepare();
						    
			    	float currentVolume = 1.0f;
			    	
			    	mAudioTrack.play();
					
			    	// Next instruction has to be in a loop
			    	//mAudioTrack.write(buffer, 0, buffer.length);
			    	
			    	Log.v("TEST","Buffer len: "+buffer.length);
			    	Log.v("INFO","Max vol: "+mAudioTrack.getMaxVolume());
			    	Log.v("INFO","Min vol: "+mAudioTrack.getMinVolume());
			    	
			    	for(int i = 0; i < buffer.length; i=i+50) {
			    		mAudioTrack.write(buffer, i, 50);
			    		if(i > 50 && currentVolume > 0) { // Length of this particular song divided in half
			    			currentVolume -= 0.00005f;
			    			mAudioTrack.setStereoVolume(currentVolume, currentVolume);
			    			Log.v("INFO","Set vol to: "+currentVolume);
			    		}
			    		//Log.v("INFO","Playing!");
			    		
			    	}
			    	
					Log.v("TEST","This is a test thread 1!");

			    }
			  }).start();
			
			new Thread(new Runnable() {
			    public void run() {
					mAudioTrack2.play();
					
					for(int i = 0; i < buffer2.length; i=i+50) {
			    		mAudioTrack2.write(buffer2, i, 50);
			    	}
					
					Log.v("TEST","This is a test thread 2!");
					
			    }
			  }).start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        Button b1 = (Button) findViewById(R.id.ImageButton01);
        b1.setOnClickListener(
        		new View.OnClickListener() {
        		    public void onClick(View v) {	 
        		    	
        		    }
        		  }
        		
        );
        
        Button b2 = (Button) findViewById(R.id.PauseButton);
        b2.setOnClickListener(
        		new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}
        );
           
    }
	
	public static byte[] decode(String path, int startMs, int maxMs) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);
		
		float totalMs = 0;
		boolean seeking = true;
		   
		File file = new File(path);
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file), 8 * 1024);
		
		try {
		    Bitstream bitstream = new Bitstream(inputStream);
		    Decoder decoder = new Decoder();
		     
		    boolean done = false;
		    while (! done) {
		      Header frameHeader = bitstream.readFrame();
		      if (frameHeader == null) {
		        done = true;
		      } else {
		        totalMs += frameHeader.ms_per_frame();
		 
		        if (totalMs >= startMs) {
		          seeking = false;
		        }
		         
		        if (! seeking) {
		          SampleBuffer output = (SampleBuffer) decoder.decodeFrame(frameHeader, bitstream);
		           
		          if (output.getSampleFrequency() != 44100
		              || output.getChannelCount() != 2) {
		            throw new Exception("mono or non-44100 MP3 not supported");
		          }
		           
		          short[] pcm = output.getBuffer();
		          for (short s : pcm) {
		            outStream.write(s & 0xff);
		            outStream.write((s >> 8 ) & 0xff);
		          }
		        }
		         
		        if (totalMs >= (startMs + maxMs)) {
		          done = true;
		        }
		      }
		      bitstream.closeFrame();
		    }
		     
		    return outStream.toByteArray();
		  }
			catch (BitstreamException e) {
		    throw new IOException("Bitstream error: " + e);
		  } catch (DecoderException e) {
		    Log.w("ERROR", "Decoder error", e);
		    throw new Exception(e);
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}

class UpdateVolumeTask extends TimerTask {
	
	AudioTrack track;
	
	public void setTrack(AudioTrack tr) {
		this.track = tr;
	}
	
	public void run() {
		
		track.setStereoVolume(0, 0);
		
	}
	
}
