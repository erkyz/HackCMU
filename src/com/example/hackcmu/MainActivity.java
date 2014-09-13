package com.example.hackcmu;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	
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
}
