package rn.js;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;

import java.util.List;
import java.util.Map;

public class RnMap {

    public static RnMap map() {
        return new RnMap(RnMapType.MAP);
    }
    public static RnMap array() {
        return new RnMap(RnMapType.ARRAY);
    }
    public static RnMap make() {
        return new RnMap(RnMapType.MAP);
    }
    
    public RnMap put(String key, String value) {
        assertMap();
        mMap.putString(key, value);
        return this;
    }
    public RnMap put(String key, int value) {
        assertMap();
        mMap.putInt(key, value);
        return this;
    }
    public RnMap put(String key, double value) {
        assertMap();
        mMap.putDouble(key, value);
        return this;
    }
    public RnMap put(String key, boolean value) {
        assertMap();
        mMap.putBoolean(key, value);
        return this;
    }

    public RnMap putNull(String key) {
        assertMap();
        mMap.putNull(key);
        return this;
    }

    public RnMap put( String key, List list ) {
        assertMap();

        RnMap result = RnMap.array();
        for( Object value : list ) {
            if( value == null ) {
                result.pushNull();
            } else if( value instanceof Integer ) {
                result.push( ( Integer ) value );
            } else if( value instanceof Boolean ) {
                result.push( ( Boolean ) value );
            } else if( value instanceof Number ) {
                result.push( ( ( Number ) value ).doubleValue() );
            } else if( value instanceof List ) {
                result.push( ( List ) value );
            } else if( value instanceof Map ) {
                result.push( ( Map<String, Object> ) value );
            } else if( value instanceof RnMap ) {
                result.push( ( RnMap ) value );
            } else {
                result.push( value.toString() );
            }
        }

        mMap.putArray(key, result.getWritableArray());
        return this;
    }

    public RnMap put( String key, Map<String, Object> map ) {
        assertMap();

        RnMap result = RnMap.map();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if( entryValue == null ) {
                result.putNull( entryKey );
            } else if( entryValue instanceof Integer ) {
                result.put( entryKey, ( Integer ) entryValue );
            } else if( entryValue instanceof Boolean ) {
                result.put( entryKey, ( Boolean ) entryValue );
            } else if( entryValue instanceof Number ) {
                result.put( entryKey, ( ( Number ) entryValue ).doubleValue() );
            } else if( entryValue instanceof List ) {
                result.put( entryKey, ( List ) entryValue );
            } else if( entryValue instanceof Map ) {
                result.put( entryKey, ( Map<String, Object> ) entryValue );
            } else if( entryValue instanceof RnMap ) {
                result.put( entryKey, ( RnMap ) entryValue );
            } else {
                result.put( entryKey, entryValue.toString() );
            }
        }

        mMap.putMap(key, result.getWritableMap());
        return this;
    }

    public RnMap put(String key, RnMap value) {
        assertMap();
        if (value.isMap())
            mMap.putMap(key, value.getWritableMap());
        else if (value.isArray())
            mMap.putArray(key, value.getWritableArray());
        else
            throw new RuntimeException("Cannot put typeless RnMap, use .array() or .map() instead of .make()");
        return this;
    }
    
    public RnMap push(String value) {
        assertArray();
        mArray.pushString(value);
        return this;
    }
    public RnMap push(int value) {
        assertArray();
        mArray.pushInt(value);
        return this;
    }
    public RnMap push(double value) {
        assertArray();
        mArray.pushDouble(value);
        return this;
    }
    public RnMap push(boolean value) {
        assertArray();
        mArray.pushBoolean(value);
        return this;
    }
    public RnMap pushNull() {
        assertArray();
        mArray.pushNull();
        return this;
    }

    public RnMap push( List list ) {
        assertMap();

        RnMap result = RnMap.array();
        for( Object value : list ) {
            if( value == null ) {
                result.pushNull();
            } else if( value instanceof Integer ) {
                result.push( ( Integer ) value );
            } else if( value instanceof Boolean ) {
                result.push( ( Boolean ) value );
            } else if( value instanceof Number ) {
                result.push( ( ( Number ) value ).doubleValue() );
            } else if( value instanceof List ) {
                result.push( ( List ) value );
            } else if( value instanceof Map ) {
                result.push( ( Map<String, Object> ) value );
            } else if( value instanceof RnMap ) {
                result.push( ( RnMap ) value );
            } else {
                result.push( value.toString() );
            }
        }

        mArray.pushArray( result.getWritableArray() );
        return this;
    }

    public RnMap push( Map<String, Object> map ) {
        assertMap();

        RnMap result = RnMap.map();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if( entryValue == null ) {
                result.putNull( entryKey );
            } else if( entryValue instanceof Integer ) {
                result.put( entryKey, ( Integer ) entryValue );
            } else if( entryValue instanceof Boolean ) {
                result.put( entryKey, ( Boolean ) entryValue );
            } else if( entryValue instanceof Number ) {
                result.put( entryKey, ( ( Number ) entryValue ).doubleValue() );
            } else if( entryValue instanceof List ) {
                result.put( entryKey, ( List ) entryValue );
            } else if( entryValue instanceof Map ) {
                result.put( entryKey, ( Map<String, Object> ) entryValue );
            } else if( entryValue instanceof RnMap ) {
                result.put( entryKey, ( RnMap ) entryValue );
            } else {
                result.put( entryKey, entryValue.toString() );
            }
        }

        mArray.pushMap( result.getWritableMap() );
        return this;
    }

    public RnMap push(RnMap value) {
        assertArray();
        if (value.isMap())
            mArray.pushMap(value.getWritableMap());
        else if (value.isArray())
            mArray.pushArray(value.getWritableArray());
        else
            throw new RuntimeException("Cannot push typeless RnMap, use .array() or .map() instead of .make()");
        return this;
    }
    
    public WritableMap getWritableMap() {
        assertMap();
        return mMap;
    }
    public WritableArray getWritableArray() {
        assertArray();
        return mArray;
    }
    public boolean isArray() {
        return mType == RnMapType.ARRAY;
    }
    public boolean isMap() {
        return mType == RnMapType.MAP;
    }

    protected WritableMap mMap;
    protected WritableArray mArray;
    protected RnMapType mType;
    
    protected RnMap(RnMapType type) {
        mType = type;
        if (type == RnMapType.ARRAY)
            mArray = Arguments.createArray();
        else
            mMap = Arguments.createMap();
    }
    protected void assertArray() {
        if (mType == RnMapType.MAP)
            throw new RuntimeException("RnMap is map, cannot use as array");
    }
    protected void assertMap() {
        if (mType == RnMapType.ARRAY)
            throw new RuntimeException("RnMap is array, cannot use as map");
    }
    
    protected enum RnMapType {
        MAP,
        ARRAY;
    }
  
}
