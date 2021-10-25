package test.app.fabricutils;

import java.util.Currency;
import java.math.BigDecimal;
import java.util.HashMap;

import android.os.Bundle;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.yandex.metrica.Revenue;
import com.yandex.metrica.YandexMetrica;
import rn.js.templates.RnModule;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

public class FabricUtilsModule extends RnModule {
    private FirebaseAnalytics mFirebaseAnalytics;

    private AppEventsLogger mFacebookLogger;

    public FabricUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mFacebookLogger = AppEventsLogger.newLogger( reactContext );
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(reactContext);
    }
    @Override
    public String getName() {
        return "FabricUtils";
    }
    @ReactMethod
    public void setUserIdentifier(String identifier, Promise p) {
        try {
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.setUserId(identifier);
            AppEventsLogger.setUserID( identifier );
            YandexMetrica.setUserProfileID( identifier );
            p.resolve(null);
        } catch (Exception e) {
            p.reject(e);
        }
    }
    @ReactMethod
    public void logEvent(String name, ReadableMap stringParams, Promise p) {
        try {
            ReadableMapKeySetIterator iterator = stringParams.keySetIterator();
            HashMap<String, Object> eventParams = new HashMap<>();
            Bundle facebookParams = new Bundle();
            while (iterator.hasNextKey()) {
                try {
                    String key = iterator.nextKey();
                    String param = stringParams.getString(key);
                    facebookParams.putString( key, param );
                    eventParams.put( key, param );
                } catch( Throwable e ) {
                    e.printStackTrace();
                }
            }
            mFirebaseAnalytics.logEvent(name, facebookParams);
            YandexMetrica.reportEvent( name, eventParams );
            mFacebookLogger.logEvent( name, facebookParams );
            p.resolve(null);
        } catch (Throwable e) {
            p.reject(e);
        }
    }
    @ReactMethod
    public void logAddToCart(
        String price, String currency,
        String itemName, String itemType, String itemId,
        ReadableMap stringParams,
        Promise p
    ) {
        try {
            ReadableMapKeySetIterator iterator = stringParams.keySetIterator();
            Bundle params = new Bundle();
            Bundle firBundle = new Bundle();
            HashMap<String, Object> eventParams = new HashMap<>();
            while (iterator.hasNextKey()) {
                try {
                    String key = iterator.nextKey();
                    String param = stringParams.getString( key );
                    params.putString( key, param );
                    firBundle.putString( key, param );
                    eventParams.put( key, param );
                } catch( Throwable e ) {
                    e.printStackTrace();
                }
            }
            firBundle.putString( FirebaseAnalytics.Param.CURRENCY, currency );
            firBundle.putString( FirebaseAnalytics.Param.ITEMS, itemName );
            firBundle.putString( FirebaseAnalytics.Param.VALUE, price );
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, firBundle);

            eventParams.put( "item_name", itemName );
            eventParams.put( "price", price );
            eventParams.put( "currency", currency );
            eventParams.put( "item_id", itemId );
            eventParams.put( "item_type", itemType );
            YandexMetrica.reportEvent( "add_to_cart", eventParams );

            params.putString( "item_name", itemName );
            params.putString( AppEventsConstants.EVENT_PARAM_CONTENT_ID, itemId);
            params.putString( AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, itemType);
            params.putString( AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
            mFacebookLogger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, Double.parseDouble( price ), params);

            p.resolve(null);
        } catch (Throwable e) {
            p.reject(e);
        }
    }
    @ReactMethod
    public void logPurchase(
        String price, String currencyString, boolean success,
        String itemName, String itemType, String itemId,
        ReadableMap stringParams,
        Promise p
    ) {
        try {
            Currency currency = Currency.getInstance( currencyString );

            // PurchaseEvent event = new PurchaseEvent();
            // event
            //     .putItemPrice(new BigDecimal(price))
            //     .putCurrency(currency)
            //     .putItemName(itemName)
            //     .putItemType(itemType)
            //     .putItemId(itemId)
            //     .putSuccess(success);
            ReadableMapKeySetIterator iterator = stringParams.keySetIterator();
            Bundle params = new Bundle();
            Bundle firBundle = new Bundle();
            while (iterator.hasNextKey()) {
                try {
                    String key = iterator.nextKey();
                    String param = stringParams.getString( key );
                    firBundle.putString( key, param );
                    params.putString( key, param );
                } catch( Throwable e ) {
                    e.printStackTrace();
                }
            }
            firBundle.putString( FirebaseAnalytics.Param.CURRENCY, currencyString );
            firBundle.putString( FirebaseAnalytics.Param.ITEMS, itemName );
            firBundle.putString( FirebaseAnalytics.Param.VALUE, price );
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, firBundle);

            params.putString( AppEventsConstants.EVENT_PARAM_CURRENCY, currencyString );
            mFacebookLogger.logEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, Double.parseDouble( price ), params );

            YandexMetrica.reportRevenue( Revenue.newBuilder( Double.parseDouble( price ), currency ).withProductID( itemId ).build() );
            p.resolve(null);
        } catch (Throwable e) {
            p.reject(e);
        }
    }
}
