package fi.solita.utils.serialization.impl;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMapOfSize;
import static fi.solita.utils.functional.Functional.map;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

import fi.solita.utils.functional.Pair;
import fi.solita.utils.serialization.json.JSON;
import fi.solita.utils.serialization.json.JSONStr;

/**
 * JSON serialization/deserialization low-level implementation
 * using the library from json.org. Which sucks, so you should probably make a new one...
 */
public class JSONorgImplementation extends JSON implements Serializable {
    
    @Override
    public JSONStr toJSON(String object) {
        return new JSONStr(assertNotNull(JSONObject.quote(object)));
    }

    @Override
    public JSONStr toJSON(boolean object) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(object)));
    }

    @Override
    public JSONStr toJSON(double object) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(object)));
    }

    @Override
    public JSONStr toJSON(BigInteger object) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(object)));
    }

    @Override
    public JSONStr toJSON(BigDecimal object) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(object)));
    }

    @Override
    public JSONStr toJSON(Iterable<JSONStr> object) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(newList(map(JSONorgImplementation_.toJSONString, object)))));
    }

    @Override
    public JSONStr toJSON(Map<String, JSONStr> object) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(map(JSONorgImplementation_.mapToJSONString, object))));
    }

    @Override
    public String toString(JSONStr json) {
        return (String) new JSONTokener(json.toString()).nextValue();
    }

    @Override
    public boolean toBoolean(JSONStr json) {
        return (Boolean) new JSONTokener(json.toString()).nextValue();
    }

    @Override
    public double toDouble(JSONStr json) {
        return (Double) new JSONTokener(json.toString()).nextValue();
    }

    @Override
    public BigInteger toBigInteger(JSONStr json) {
        return BigInteger.valueOf(((Number)new JSONTokener(json.toString()).nextValue()).longValue());
    }

    @Override
    public BigDecimal toBigDecimal(JSONStr json) {
        return BigDecimal.valueOf(((Number)new JSONTokener(json.toString()).nextValue()).doubleValue());
    }

    @Override
    public JSONStr[] toArray(JSONStr json) {
        JSONArray array = new JSONArray(json.toString());
        JSONStr[] ret = new JSONStr[array.length()];
        for (int i = 0; i < array.length(); ++i) {
            ret[i] = new JSONStr(assertNotNull(JSONObject.valueToString(array.get(i))));
        }
        return ret;
    }

    @Override
    public Map<String, JSONStr> toMap(JSONStr json) {
        JSONObject map = new JSONObject(json.toString());
        Map<String,JSONStr> ret = newMapOfSize(map.keySet().size());
        for (Object key: map.keySet()) {
            ret.put(key.toString(), new JSONStr(assertNotNull(JSONObject.valueToString(map.get(key.toString())))));
        }
        return ret;
    }
    
    static <T> T assertNotNull(T value) {
        if (value == null) {
            throw new RuntimeException("failed converting to JSON");
        }
        return value;
    }
    
    static JSONString toJSONString(final JSONStr json) {
        return new JSONString() {
            @Override
            public String toJSONString() {
                return json.toString();
            }
        };
    }
    
    static Map.Entry<String,? extends JSONString> mapToJSONString(final Map.Entry<String,? extends JSONStr> json) {
        return Pair.of(json.getKey(), new JSONString() {
            @Override
            public String toJSONString() {
                return json.getValue().toString();
            }
        });
    }
}