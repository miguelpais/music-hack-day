package pt.alexdias.mixer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import android.os.AsyncTask;

public class NetworkAccessTask extends AsyncTask<URL, Void, String> {

	@Override
	protected String doInBackground(URL... arg0) {
		
		String jsonString = "";
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(  
			        arg0[0].openStream()));
			
			String inputLine;
			while ((inputLine = in.readLine()) != null) {  
	           jsonString = inputLine;  
	          } 
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonString;
		
		
	}

}
