package fi.solita.utils.serialization.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Base class for different low-level JSON implementations
 */
public abstract class JSON {
    
    public abstract JSONStr toJSON(String v);
    public abstract JSONStr toJSON(boolean v);
    public abstract JSONStr toJSON(double v);
    public abstract JSONStr toJSON(BigInteger v);
    public abstract JSONStr toJSON(BigDecimal v);
    public abstract JSONStr toJSON(Iterable<JSONStr> v);
    public abstract JSONStr toJSON(Map<String,JSONStr> v);
    
    public abstract String toString(JSONStr v);
    public abstract boolean toBoolean(JSONStr v);
    public abstract double toDouble(JSONStr v);
    public abstract BigInteger toBigInteger(JSONStr v);
    public abstract BigDecimal toBigDecimal(JSONStr v);
    public abstract JSONStr[] toArray(JSONStr v);
    public abstract Map<String,JSONStr> toMap(JSONStr v);
    
    /**
     * Serialize <i>object</i> using <i>serializer</i>
     */
    public final <T> JSONStr serialize(JSONSerializer<T> serializer, T object) {
        return serializer.serialize(this, object);
    }
    
    /**
     * Deserialize <i>json</i> using <i>deserializer</i>
     */
    public final <T> T deserialize(JSONDeserializer<T> deserializer, JSONStr json) {
        return deserializer.deserialize(this, json);
    }
}
