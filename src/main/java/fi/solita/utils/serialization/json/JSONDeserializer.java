package fi.solita.utils.serialization.json;

import fi.solita.utils.serialization.Deserializer;

public interface JSONDeserializer<TYPE> extends Deserializer<JSON, JSONStr, TYPE> {
}