package com.example.hackcmu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.example.hackcmu.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Waiting extends Activity {
	public static String mParentPath;
	public static String peopleMetFile = "people_met";

	// A File object containing the path to the transferred files
    // Incoming Intent
    private Intent mIntent;
	NfcAdapter mNfcAdapter;
	private Uri[] mFileUris = new Uri[1];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_waiting);
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		if (!mNfcAdapter.isEnabled())
		{
			Toast.makeText(this, "NFC not enabled.", Toast.LENGTH_LONG).show();
		}

        /*
         * Instantiate a new FileUriCallback to handle requests for
         * URIs
         */
        mFileUriCallback = new FileUriCallback();
        // Set the dynamic callback for URI requests.
        mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback,this); 
		
        /*
         * Create a list of URIs, get a File,
         * and set its permissions
         */
        
        Uri[] mFileUris = new Uri[1];
        String transferFile = "PLACEHOLDER.txt";
        File extDir = getExternalFilesDir(null);
        File requestFile = new File(extDir, transferFile);
        // Get a URI for the File and add it to the list of URIs
        Uri external = Uri.fromFile(requestFile);
        
        if (external != null) {
            mFileUris[0] = external;
        } else {
            Log.e("My Activity", "No File URI available for file.");
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
		public FileUriCallback() {
		}
		//create content URIs as needed.
		Uri[] mFileUris;
		
		@Override
		public Uri[] createBeamUris(NfcEvent event) {
			return mFileUris;
		}
	}

	private FileUriCallback mFileUriCallback;
	
	protected void onNewIntent(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setIntent(getIntent());
		
		setContentView(R.layout.activity_carly_rae);
		
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		handleViewIntent();
	
		final Waiting THIS = this;
		
		Thread thread = new Thread()
		{
			public void run()
			{
				try {
					MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.callme);
					mediaPlayer.start();
					sleep(7000);
					mediaPlayer.stop();
					Intent intent = new Intent(THIS, MainActivity.class);
					startActivity(intent);
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		};
		thread.start();
	}

		private void handleViewIntent() throws FileNotFoundException {
			// Get the Intent action
	        mIntent = getIntent();
	        String action = mIntent.getAction();
	        /*
	         * For ACTION_VIEW, the Activity is being asked to display data.
	         * Get the URI.
	         */
	        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
	            // Get the URI from the Intent
	            Uri beamUri = mIntent.getData();
	            /*
	             * Test for the type of URI, by getting its scheme value
	             */
	            if (TextUtils.equals(beamUri.getScheme(), "file")) {
	                mParentPath = handleFileUri(beamUri);
	                URI uri = null;
	        		try {
	        			uri = new URI(mParentPath);
	        		} catch (URISyntaxException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	                File file = new File(uri);
	                BufferedReader in = null;
	        		try {
	        			in = new BufferedReader(new FileReader(file));
	        		} catch (FileNotFoundException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		FileOutputStream fos = openFileOutput(peopleMetFile, Context.MODE_PRIVATE);
	        		String string = "";
	                try {
	        			for(int i = 0; i < 10; i++) {
	        				String line = in.readLine();
	        				string += line + "\n";
	        			}
        				Toast.makeText(this, string, Toast.LENGTH_LONG).show();
        				fos.write(string.getBytes());
        				fos.close();
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	                
	            }
	            
	        }
		}
		
		 public String handleFileUri(Uri beamUri) {
		        // Get the path part of the URI
		        String fileName = beamUri.getPath();
		        // Create a File object for this filename
		        File copiedFile = new File(fileName);
		        // Get a string containing the file's parent directory
		        return copiedFile.getParent();
		    }

	}
