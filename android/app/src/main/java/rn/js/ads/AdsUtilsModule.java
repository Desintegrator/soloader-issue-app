package rn.js.ads;

import rn.js.templates.RnModule;
import rn.js.RnMap;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.app.Application.ActivityLifecycleCallbacks;
import android.util.Log;

import java.util.Map;
import java.util.HashMap; 
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

import androidx.annotation.NonNull;
import java.io.IOException;

import com.facebook.ads.*;

public class AdsUtilsModule extends RnModule {

    private static final String TAG = "AdsUtilsModule";
    private final Context mContext;
    private InterstitialAd interstitialAd;

    public AdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mInstance = this;
        mContext = reactContext;
        initAds();
    }
    protected static AdsUtilsModule mInstance;
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }
    @Override
    protected List<String> supportedEvents() {
        return Arrays.asList( "jslog", "phoneScreenState" );
    }
    @Override
    public String getName() {
        return "AdsUtilsModule";
    }

    public void initAds() {
        AudienceNetworkAds.initialize(mContext);
    }
    
    @ReactMethod
    public void loadFullscreenAds(String adId, final Promise p) {
        interstitialAd = new InterstitialAd(mContext, adId);

        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.d(TAG, "Interstitial ad displayed.");
                WritableMap params = Arguments.createMap();
                params.putString("name", "AdDisplayed");
                getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("AdEvent", params);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.d(TAG, "Interstitial ad dismissed.");
                WritableMap params = Arguments.createMap();
                params.putString("name", "AdDismissed");
                getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("AdEvent", params);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.d(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                if (p != null) {
                    p.reject("error load: " + adError.getErrorMessage());
                }
                WritableMap params = Arguments.createMap();
                params.putString("name", "AdError");
                getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("AdEvent", params);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                if (p != null) {
                    p.resolve("loaded");
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
                WritableMap params = Arguments.createMap();
                params.putString("name", "AdClicked");
                getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("AdEvent", params);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
                WritableMap params = Arguments.createMap();
                params.putString("name", "ImpressionLogged");
                getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("AdEvent", params);
            }
        };

        interstitialAd.loadAd(
            interstitialAd.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                    .build()
        );
    }

    @ReactMethod
    public void showFullscreenAds() {
        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            // p.reject("not loaded");
        } else if (interstitialAd.isAdInvalidated()) {
            // p.reject("adInvalidated");
        } else {
            interstitialAd.show();
        }
    }
}
