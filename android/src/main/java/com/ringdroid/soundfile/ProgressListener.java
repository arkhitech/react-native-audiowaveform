package com.ringdroid.soundfile;

import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by spellew on 10/30/18.
 */

public class ProgressListener implements SoundFile.ProgressListener {
    private int limitCount = 0;
    private ReactContext mContext;

    private void sendEvent(ReactContext context, String eventName, WritableMap params) {
        context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public ProgressListener(ReactContext context) {
        mContext = context;
    }

    @Override
    public boolean reportProgress(double fractionComplete) {
//        Log.i("XSXGOT", "fractionComplete sound file: " + fractionComplete);

        if (limitCount < 500000) {

            WritableMap map = new WritableNativeMap();
            map.putDouble("progress", fractionComplete);

            sendEvent(mContext, "onPlaybackInitialization", map);

        } else {
            limitCount = 0;
        }
        return true;
    }
}
