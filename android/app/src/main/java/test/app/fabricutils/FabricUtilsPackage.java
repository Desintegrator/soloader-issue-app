package test.app.fabricutils;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class FabricUtilsPackage implements ReactPackage {
  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }
  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    List<ViewManager> managers = new ArrayList<>();
    return managers;
  }
  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();
    modules.add(new FabricUtilsModule(reactContext));
    return modules;
  }
}
