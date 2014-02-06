package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMapOfSize;
import static fi.solita.utils.functional.Functional.map;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

import fi.solita.utils.functional.Pair;

/**
 * JSON serialization/deserialization low-level implementation
 * using the library from json.org. Which sucks, so you should probably make a new one...
 */
public class JSONorgImplementation extends JSON {
    
    @Override
    public JSONStr toJSON(String v) {
        return new JSONStr(assertNotNull(JSONObject.quote(v)));
    }

    @Override
    public JSONStr toJSON(boolean v) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(v)));
    }

    @Override
    public JSONStr toJSON(double v) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(v)));
    }

    @Override
    public JSONStr toJSON(BigInteger v) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(v)));
    }

    @Override
    public JSONStr toJSON(BigDecimal v) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(v)));
    }

    @Override
    public JSONStr toJSON(Iterable<JSONStr> v) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(newList(map(JSONorgImplementation_.toJSONString, v)))));
    }

    @Override
    public JSONStr toJSON(Map<String, JSONStr> v) {
        return new JSONStr(assertNotNull(JSONObject.valueToString(map(JSONorgImplementation_.mapToJSONString, v))));
    }

    @Override
    public String toString(JSONStr v) {
        return (String) new JSONTokener(v.toString()).nextValue();
    }

    @Override
    public boolean toBoolean(JSONStr v) {
        return (Boolean) new JSONTokener(v.toString()).nextValue();
    }

    @Override
    public double toDouble(JSONStr v) {
        return (Double) new JSONTokener(v.toString()).nextValue();
    }

    @Override
    public BigInteger toBigInteger(JSONStr v) {
        return BigInteger.valueOf(((Number)new JSONTokener(v.toString()).nextValue()).longValue());
    }

    @Override
    public BigDecimal toBigDecimal(JSONStr v) {
        return BigDecimal.valueOf(((Number)new JSONTokener(v.toString()).nextValue()).doubleValue());
    }

    @Override
    public JSONStr[] toArray(JSONStr v) {
        JSONArray arr = new JSONArray(v.toString());
        JSONStr[] ret = new JSONStr[arr.length()];
        for (int i = 0; i < arr.length(); ++i) {
            ret[i] = new JSONStr(assertNotNull(JSONObject.valueToString(arr.get(i))));
        }
        return ret;
    }

    @Override
    public Map<String, JSONStr> toMap(JSONStr v) {
        JSONObject m = new JSONObject(v.toString());
        Map<String,JSONStr> ret = newMapOfSize(m.keySet().size());
        for (Object key: m.keySet()) {
            ret.put(key.toString(), new JSONStr(assertNotNull(JSONObject.valueToString(m.get(key.toString())))));
        }
        return ret;
    }
    
    static <T> T assertNotNull(T o) {
        if (o == null) {
            throw new RuntimeException("failed converting to JSON");
        }
        return o;
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