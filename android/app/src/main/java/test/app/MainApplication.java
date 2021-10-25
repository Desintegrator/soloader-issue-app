package test.app;

import android.content.Context;
import android.app.Application;

import com.facebook.react.ReactApplication;
import com.reactnativecommunity.clipboard.ClipboardPackage;
import com.swmansion.reanimated.ReanimatedPackage;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
import com.dooboolab.RNIap.RNIapPackage;
import com.th3rdwave.safeareacontext.SafeAreaContextPackage;
import com.facebook.react.ReactInstanceManager;
import fr.snapp.imagebase64.RNImgToBase64Package;
import com.reactnativecommunity.netinfo.NetInfoPackage;
import com.BV.LinearGradient.LinearGradientPackage;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import com.reactcommunity.rnlocalize.RNLocalizePackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.airbnb.android.react.lottie.LottiePackage;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import rn.js.JsRnPackage;
import test.app.fabricutils.FabricUtilsPackage;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import com.google.firebase.FirebaseApp;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      @SuppressWarnings("UnnecessaryLocalVariable")
      List<ReactPackage> packages = new PackageList(this).getPackages();
      // Packages that cannot be autolinked yet can be added manually here, for example:
      // packages.add(new MyReactNativePackage());
      packages.add(new FabricUtilsPackage());
      packages.add(new JsRnPackage());
      packages.add(new LottiePackage());
      return packages;
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    FirebaseApp.initializeApp(this);

    YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder( getString( R.string.appmetrica_api_key ));
    YandexMetrica.activate(getApplicationContext(), configBuilder.build());
    // Отслеживание активности пользователей
    YandexMetrica.enableActivityAutoTracking(this);

    SoLoader.init(this, /* native exopackage */ false);
    mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);

    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
        We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("test.app.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
  
  private static Thread.UncaughtExceptionHandler mDefaultUEH;
  private static Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
      @Override 
      public void uncaughtException(Thread thread, Throwable ex) {
          if (
              !(ex instanceof IllegalStateException) ||
              !"closed".equals(ex.getMessage()) ||
              !"OkHttp Dispatcher".equals(thread.getName())
          ) {
              mDefaultUEH.uncaughtException(thread, ex);
          } else {}
      }
  };
  
}
