package com.otomogroove.OGReactNativeWaveform;

/**
 * Created by spellew on 10/31/18.
 */

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

public class OGWaveModule extends ReactContextBaseJavaModule {

    static int mRecordingDuration;

    static class WaveformMeta extends ReactContext {
        public WaveformMeta(ReactContext reactContext) {
            super(reactContext);
        }

        public int getmRecordingDuration() {
            return mRecordingDuration;
        }
    }

    @Override
    public String getName() {
        return "OGWaveModule";
    }

    public OGWaveModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void setmRecordingDuration(int duration) {
        mRecordingDuration = duration;
    }

}
