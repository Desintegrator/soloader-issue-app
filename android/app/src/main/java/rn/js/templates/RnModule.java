package rn.js.templates;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.List;
import java.util.Collections;

import rn.js.RnMap;

public abstract class RnModule extends ReactContextBaseJavaModule {    
    protected ReactApplicationContext mReactContext = null;
    protected List<String> mSupportedEvents = null;
    
    public RnModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        mSupportedEvents = supportedEvents();
    }
    
    protected List<String> supportedEvents() {
        return Collections.emptyList();
    }
    
    public void dispatchEvent(String eventName, RnMap params) {
        if (!mSupportedEvents.contains(eventName))
            throw new RuntimeException("Cannot dispath event with name " + eventName);
        if (params.isMap())
            mReactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params.getWritableMap());
        else if (params.isArray())
            mReactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params.getWritableArray());
        else
            throw new RuntimeException("Cannot dispath typeless RnMap");
    }  
}
