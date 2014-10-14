package com.ocean9technologies.onlineradio;

import java.io.IOException;

import android.app.ActionBar;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * entry class for media player
 * 
 * @author Ankur.Goel
 * 
 */
public class MediaActivity extends ActionBarActivity implements OnClickListener {

	/**
	 * stop media player button
	 */
	private Button stopButton;
	/**
	 * media player object
	 */
	private MediaPlayer mediaPlayer;
	/**
	 * start media player button
	 */
	private Button startButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Radio Halbmesser");

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-6629498227929727/6887591897");

		AdRequest adRequest = new AdRequest.Builder()
				.tagForChildDirectedTreatment(true).build();

		adView.loadAd(adRequest);

		stopButton = (Button) findViewById(R.id.stop_button);
		stopButton.setOnClickListener(this);

		startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(this);

	}

	private void playMedia() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://203.150.224.217:8600"; // your URL here
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				try {
					mediaPlayer.setDataSource(url);
					mediaPlayer.prepare(); // might take long! (for buffering,
											// etc)
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mediaPlayer.start();
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stop_button:
			stopMediaPlayer();
			startButton.setEnabled(true);
			startButton.setClickable(true);

			stopButton.setEnabled(false);
			stopButton.setClickable(false);
			break;

		case R.id.start_button:
			if (isConnected()) {
				playMedia();
			} else {
				Toast.makeText(
						MediaActivity.this,
						"Please enjoy advertisement until your network connection comes back.",
						Toast.LENGTH_SHORT).show();
			}

			startButton.setEnabled(false);
			startButton.setClickable(false);

			stopButton.setEnabled(true);
			stopButton.setClickable(true);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 */
	private synchronized void stopMediaPlayer() {
		if (mediaPlayer != null) {
			System.out.println("Playing :" + mediaPlayer.isPlaying()
					+ " looping :" + mediaPlayer.isLooping());
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.release();

				mediaPlayer = null;
			}
		}
	}

	/**
	 * checking for connectivity whether the system has network connectivity or not 
	 * @return
	 */
	public boolean isConnected() {
		final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobile = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean online = false;
		if (wifi.isAvailable() || mobile.isAvailable()) {
			if (connectivityManager.getActiveNetworkInfo() != null) {
				online = connectivityManager.getActiveNetworkInfo()
						.isConnectedOrConnecting();
			}
		}
		return online;
	}

}
