package com.peyo.searchmedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceView;

public class SearchableActivity extends Activity implements SurfaceHolder.Callback {
	private static final String TAG = "SearchMedia.Activity";

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private MediaPlayer mMediaPlayer;

	private static final String MEDIA1 = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
	private static final String MEDIA2 = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search.mp4";
	private String mUri = MEDIA1;

	private static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Log.d(TAG, "onCreate() " + intent);

		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			Uri uri = intent.getData();
			if (uri != null) {
				int id = Integer.valueOf(uri.getLastPathSegment());
				Log.d(TAG, "Video id: " + id);
				if (id == 2) {
					mUri = MEDIA2;
				}
			}
			boolean startPlayback = getIntent().getBooleanExtra(EXTRA_START_PLAYBACK, false);
			Log.d(TAG, "EXTRA_START_PLAYBACK? " + (startPlayback ? "yes" : "no"));

			setSurfaceHolder();
		} else {
			Log.d(TAG, "onCreate() calling finish()");
			finish();
		}
	}

	private void setSurfaceHolder() {
		setContentView(R.layout.main);
		mSurfaceView = findViewById(R.id.surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
	}

	protected void playVideo() {
        mMediaPlayer = new MediaPlayer();
        try {
	        mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.setDataSource(mUri);
	        mMediaPlayer.prepare();
	        mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		playVideo();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {}

    @Override
    protected void onPause() {
        releaseMediaPlayer();
		super.onPause();
    }

    @Override
    protected void onDestroy() {
        releaseMediaPlayer();
		super.onDestroy();
    }

    protected void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
