package com.example.hackcmu;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		File file = new File(getFilesDir(), "info.txt");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String[] information = new String[10];
		String interest1 = preferences.getString("pref_interest_one", "Unnamed");
		String interest2 = preferences.getString("pref_interest_two", "Unnamed");
		String interest3 = preferences.getString("pref_interest_three", "Unnamed");
		String interest4 = preferences.getString("pref_interest_four", "Unnamed");
		String interest5 = preferences.getString("pref_interest_five", "Unnamed");
		String name = preferences.getString("pref_name", "Unnamed");
		String phone = preferences.getString("pref_phone_number", "No Phone");
		String facebook = preferences.getString("pref_facebook_link", "No Facebook");
		String twitter = preferences.getString("pref_twitter_link", "No Twitter");
		String email = preferences.getString("pref_email", "No Email");
		
		information[0] = interest1;
		information[1] = interest2;
		information[2] = interest3;
		information[3] = interest4;
		information[4] = interest5;
		information[5] = name;
		information[6] = phone;
		information[7] = facebook;
		information[8] = twitter;
		information[9] = email;
		
		String result = "";
		for (int i = 0; i < information.length; i++) {
			result += "information[i]\n";
		}
		writeToFile(result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
	        case R.id.action_settings:
	    		Intent intent = new Intent(this, SettingsActivity.class);
	    		startActivity(intent);
	            return true;
	        case R.id.action_people:
	        	Intent intent1 = new Intent(this, People.class);
	        	startActivity(intent1);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	private Uri[] mFilesUris = new Uri[10];
	
	//Callback that Android Beam file transfer calls to get files to share
	
	private class FileUriCallback implements
		NfcAdapter.CreateBeamUrisCallback {
			public FileUriCallback() {
			}
			
			Uri[] mFileUris;
			
			@Override
			public Uri[] createBeamUris(NfcEvent event) {
				return mFileUris;
			}
		}

	public void callMe(View view) {
		Intent intent = new Intent(this, Waiting.class);
		startActivity(intent);
	}
	
	private void handleIntent(Intent intent) {
		// WILL HANDLE INTENT
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	private void writeToFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("information.txt", MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
}
