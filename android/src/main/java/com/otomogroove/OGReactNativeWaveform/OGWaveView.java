package com.otomogroove.OGReactNativeWaveform;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.ringdroid.WaveformView;

import java.util.logging.Logger;
import java.io.IOException;

import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by juanjimenez on 13/01/2017.
 */

public class OGWaveView extends FrameLayout {

    private Logger logger = Logger.getLogger("OGWaveView");

    private final OGUIWaveView mUIWave;
    private MediaPlayer mMediaPlayer;
    private WaveformView mWaveView;

    private String componentID;

    private ReactContext mContext;
    private int mWaveColor;
    private int mScrubColor;

    private boolean mAutoplay = false;
    private boolean mHasBeenPlayed = false;
    private boolean mHasEnded = true;



    private void sendEvent(ReactContext context, String eventName, WritableMap params) {
        context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
    public OGWaveView(ReactContext context) {
        super(context);
        mContext = context;

        mWaveView = new WaveformView(mContext);
        mUIWave = new OGUIWaveView(mContext);


        mUIWave.setBackgroundColor(Color.TRANSPARENT);


    }
    public void setmWaveColor(int mWaveColor) {

        this.mWaveView.setmWaveColor(mWaveColor);


    }
    public void setScrubColor(int scrubcolor){
        mScrubColor = scrubcolor;
        mUIWave.scrubColor=this.mScrubColor;
        mUIWave.invalidate();
    }

    public void onPlay(boolean play){
        if(play){
            this.mMediaPlayer.start();
            if (!mHasBeenPlayed) {
                mHasBeenPlayed = true;
            }

        }else{
            if(mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();

            }

        }

        progressReportinghandler.postDelayed(progressRunnable, 500);

    }
    public void onPause(){
        this.mMediaPlayer.pause();
    }
    public void onStop(){
        this.mMediaPlayer.stop();
    }

    public void setAutoPlay(boolean autoplay){
        Log.e(TAG, "setAutoPlay: " + autoplay );
        this.mAutoplay = autoplay;
        if(mAutoplay) {
            mMediaPlayer.start();
            Log.e(TAG, "setURI:starting ");
        }

        progressReportinghandler.postDelayed(progressRunnable, 500);

    }

    public void setURI(String uri){
        // Create the MediaPlayer

        this.mWaveView.setmURI(uri);

        mMediaPlayer= new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepare();



        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("duration: " + String.valueOf(mMediaPlayer.getDuration()));
        Log.e(TAG, "setURI: mMediaPlayer is"+mMediaPlayer.getDuration());

        addView(this.mWaveView);
        addView(this.mUIWave);

        WritableMap map = new WritableNativeMap();
        map.putDouble("duration", mMediaPlayer.getDuration());
        sendEvent(mContext, "onPlaybackInitialize", map);

        this.mWaveView.setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.e("OGWaveView","LCICK");
                    waveformListener.waveformTouchStart(mContext, componentID);
                }

                return true;
            }

        });


    }

    private WaveformView.WaveformListener waveformListener;



    public WaveformView.WaveformListener getWaveformListener() {
        return waveformListener;
    }

    public void setWaveformListener(WaveformView.WaveformListener waveformListener) {
        this.waveformListener = waveformListener;
    }

    private Handler progressReportinghandler = new Handler();
    private Runnable progressRunnable = new Runnable() {

        public void run() {
            try {
                logger.info("current position: " + String.valueOf(mMediaPlayer.getCurrentPosition()));
                if (mMediaPlayer.isPlaying()) {
                    logger.info("isPlaying");
                    if (mHasEnded) {
                        logger.info("trackStart");
                        mHasEnded = false;
                        sendEvent(mContext, "onPlaybackStart", null);
                    }
                    new UpdateProgressRequest().execute();

                    // seconds
                    progressReportinghandler.postDelayed(progressRunnable, 50);
                } else if (mHasBeenPlayed && (mMediaPlayer.getCurrentPosition() <= mMediaPlayer.getDuration() + 2000)) {
                    if (mMediaPlayer.getCurrentPosition() >= mMediaPlayer.getDuration()) {
                        logger.info("trackEnd");
                        mHasEnded = true;
                        sendEvent(mContext, "onPlaybackEnd", null);
                    } else {
                        logger.info("trackPaused");
                        sendEvent(mContext, "onPlaybackPause", null);
                    }
                }
            } catch (IllegalStateException ex) {
                ex.getStackTrace();
            }
        };
    };

    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }


    protected class UpdateProgressRequest extends AsyncTask<Void, Void, Float> {

        @Override
        protected Float doInBackground(Void... params) {

            if (mMediaPlayer.isPlaying()) {

                Float currrentPos = (float) mMediaPlayer.getCurrentPosition()/mMediaPlayer.getDuration();

                WritableMap map = new WritableNativeMap();
                map.putDouble("currentTime", mMediaPlayer.getCurrentPosition());

                sendEvent(mContext, "onPlaybackProgress", map);
                return currrentPos;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);


            mUIWave.updatePlayHead(aFloat);
        }
    }



}
