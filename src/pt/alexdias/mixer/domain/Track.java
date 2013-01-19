package pt.alexdias.mixer.domain;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import pt.alexdias.mixer.NetworkAccessTask;
import android.media.MediaMetadataRetriever;
import android.util.Log;

public class Track {
	String title;
	String artist;
	String path;
	String songId = null;
	Float bpm = null;
	final String searchQuery = "http://developer.echonest.com/api/v4/song/search?api_key=M3ATLVAJVEGQGRJWN&format=json&results=1";
	
	public Track(String absolutePath) {
		this.path = absolutePath;
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(this.path);

		this.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		this.title =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		
		Log.v("TEST", this.artist);
		Log.v("TEST", this.title);
	}
	
	public String getEscapedTitle() {
		return this.title.replace(" ", "%20");
	}
	
	public String getEscapedArtist() {
		return this.artist.replace(" ", "%20");
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getArtist() {
		return this.artist;
	}

	public Float getBPM() {
		if (this.bpm == null) {
			try {
				URL queryString;
				queryString = new URL(searchQuery + "&artist=" + getEscapedArtist() + "&title=" + getEscapedTitle() + "&bucket=audio_summary");
			
				String jsonString = new NetworkAccessTask().execute(queryString).get();
				this.bpm = parseBPM(jsonString);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return this.bpm;
	}
	
	private Float parseBPM(String json) {
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		JsonObject responseObject = jsonObject.getAsJsonObject("response");
		JsonArray songsArray = responseObject.getAsJsonArray("songs");
		JsonObject songObject = songsArray.get(0).getAsJsonObject();
		JsonObject summaryObject = songObject.getAsJsonObject("audio_summary");
		Float bpm = summaryObject.get("tempo").getAsFloat();
		return bpm;
	}
}
