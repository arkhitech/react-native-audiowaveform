package com.otomogroove.OGReactNativeWaveform;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewManager;
import com.ringdroid.WaveformView;


import java.util.HashMap;
import java.util.Map;

import static com.facebook.react.common.ReactConstants.TAG;

/**
 * Created by juanjimenez on 13/01/2017.
 */

public class OGWaveManager extends SimpleViewManager<OGWaveView> implements LifecycleEventListener,WaveformView.WaveformListener {

    private Callback onPressCallback;
    private ReactContext mcontext;
    private OGWaveView _mWaveView;
    private Activity mCurrentActivity;
    public static final int COMMAND_SEEK_TO_TIME= 1;

    private static final String REACT_CLASS = "OGWaveManager";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public OGWaveView createViewInstance(ThemedReactContext context) {
        context.addLifecycleEventListener(this);
        mcontext = context;

        // deleteFiles(Environment.getExternalStorageDirectory().toString(),"mp3");
        OGWaveView mWaveView = new OGWaveView(context);

        mWaveView.setWaveformListener(this);
        _mWaveView = mWaveView;
        return mWaveView;
    }

    @Override
    public void setTestId(OGWaveView view, String testId) {
        super.setTestId(view, testId);
        Log.e("XSXGOT","TTTTTTTTTT::: "+ testId);
    }

    @ReactMethod
    public void testCallback(Callback cb) {

        Log.e("XSXGOT","TESXT CA:LBACK CALLED!!! ");
        String sampleText = "Java is fun";
        int textLength = sampleText.length();
        try{
            cb.invoke(textLength);
        }catch (Exception e){
            cb.invoke("err");
        }
    }

    @javax.annotation.Nullable
    @Override
    public Map<String, Object> getExportedViewConstants() {
        final Map<String, Object> constants =  new HashMap<>();
        constants.put("WaveConstant" , 16000);
        return constants;
    }

    @ReactMethod
    public void seekToTime(long position)
    {
        Toast.makeText(mcontext, "SAVE US!", Toast.LENGTH_LONG);
    }

//    @ReactMethod
////    public void setPlaybackRate(float position)
////    {
////        Toast.makeText(mcontext, "HELP US GOD!", Toast.LENGTH_LONG);
////    }

    @ReactProp(name = "playbackRate")
    public void setPlaybackRate(OGWaveView view, double src) {
        view.setPlaybackRate((float)src);
    }

    @ReactProp(name = "playbackTime")
    public void setPlaybackTime(OGWaveView view, @Nullable ReadableMap src) {
        view.seekToTime((long)src.getDouble("playbackTime"));
    }

//    @ReactProp(name = "offsetStart")
//    public void setOffsetStart(OGWaveView view, final float seek) {
//        view.setOffsetStart((long)seek);
//    }
//
//    @ReactProp(name = "offsetEnd")
//    public void setOffsetEnd(OGWaveView view, final float seek) {
//        view.setOffsetEnd((long)seek);
//    }

    @ReactProp(name = "waveFormStyle")
    public void setWaveFormStyle(OGWaveView view, @Nullable ReadableMap waveFormStyle) {
        view.setmWaveColor(waveFormStyle.getInt("ogWaveColor"));
        view.setScrubColor(waveFormStyle.getInt("ogScrubColor"));
        view.setOffsetStart((long)waveFormStyle.getDouble("ogTimeOffsetStart"));
        view.setOffsetEnd((long)waveFormStyle.getDouble("ogTimeOffsetEnd"));
    }

    private static final String PROP_SEEK = "seek";
    @ReactProp(name = PROP_SEEK)
    public void setSeek(OGWaveView view, final float seek) {
        view.seekToTime((long)seek);
    }
    
    @ReactProp(name = "src")
    public void setSrc(OGWaveView view, @Nullable ReadableMap src) {
        view.setURI(src.getString("uri"));
    }

    @ReactProp(name="componentID")
    public void setComponentID(OGWaveView view, String componentID) {
        Log.e("XSXGOT","componentID SETTED:::!!!!" +componentID);
        view.setComponentID(componentID);

    }

    @ReactProp(name = "autoPlay", defaultBoolean = false)
    public void setAutoPlay(OGWaveView view, boolean autoPlay) {
        Log.e("XSXSXS","setAutoPlay:::: "+autoPlay);
        view.setAutoPlay(autoPlay);
    }




    @ReactProp(name = "play")
    public void setPlay(OGWaveView view, @Nullable boolean play)
    {
        view.onPlay(play);
    }



    /** @ReactProp(name = "pause")
    public void setPause(OGWaveView view, @Nullable Callback pause){
    // view.onPlay();

    }

     @ReactProp(name = "stop")
     public void setStop(OGWaveView view, @Nullable Callback stop){
     //view.onPlay();

     }**/


    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Override
    public void waveformTouchStart(ReactContext context, String componentID) {

        WritableMap map = Arguments.createMap();
        map.putString("componentID",componentID);
        sendEvent(context, "OGOnPress",map);
        Log.e("OGTAGDEBUG::", "waveformTouchStart: " );

    }

    @Override
    public void waveformFinishPlay(ReactContext context, String componentID) {

        WritableMap map = Arguments.createMap();
        map.putString("componentID",componentID);
        sendEvent(context, "OGFinishPlay",map);
        Log.e("OGTAGDEBUG::", "waveformFinishPlay: " );

    }

    @Override
    public void waveformTouchStart(float x) {

    }

    @Override
    public void waveformTouchMove(float x) {

    }

    @Override
    public void waveformTouchEnd() {

    }

    @Override
    public void waveformFling(float x) {

    }

    @Override
    public void waveformDraw() {

    }

    @Override
    public void waveformZoomIn() {

    }

    @Override
    public void waveformZoomOut() {

    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        Log.d("React"," View manager getCommandsMap:");
        return MapBuilder.of(
                "CoolMethod",
                COMMAND_SEEK_TO_TIME);
    }

    @Override
    public void receiveCommand(OGWaveView view, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(view, commandId, args);
        Log.i("XSXGOT", args.toString());

        if (commandId == 0) {
            view.seekPosition(args.getInt(0));
        } else if (commandId == 1) {
            view.zoomIn();
        } else if (commandId == 2) {
            view.zoomOut();
        } else {
          throw new IllegalArgumentException(String.format(
            "Unsupported command %d received by %s.",
            commandId,
            getClass().getSimpleName()));
        }
    }
}

