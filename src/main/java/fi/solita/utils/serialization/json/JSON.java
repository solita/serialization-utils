package fi.solita.utils.serialization.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import fi.solita.utils.functional.Either;
import fi.solita.utils.serialization.Deserializer.Failure;

/**
 * Base class for different low-level JSON implementations
 */
public abstract class JSON {
    
    public abstract JSONStr toJSON(String object);
    public abstract JSONStr toJSON(boolean object);
    public abstract JSONStr toJSON(double object);
    public abstract JSONStr toJSON(BigInteger object);
    public abstract JSONStr toJSON(BigDecimal object);
    public abstract JSONStr toJSON(Iterable<JSONStr> object);
    public abstract JSONStr toJSON(Map<String,JSONStr> object);
    
    public abstract String              toString(JSONStr json);
    public abstract boolean             toBoolean(JSONStr json);
    public abstract double              toDouble(JSONStr json);
    public abstract BigInteger          toBigInteger(JSONStr json);
    public abstract BigDecimal          toBigDecimal(JSONStr json);
    public abstract JSONStr[]           toArray(JSONStr json);
    public abstract Map<String,JSONStr> toMap(JSONStr json);
    
    public final <T> JSONStr serialize(JSONSerializer<T> serializer, T object) {
        return serializer.serialize(this, object);
    }
    
    public final <T> Either<Failure<T>,T> deserialize(JSONDeserializer<T> deserializer, JSONStr json) {
        return deserializer.deserialize(this, json);
    }
}
