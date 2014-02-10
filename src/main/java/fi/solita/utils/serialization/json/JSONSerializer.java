package fi.solita.utils.serialization.json;

import fi.solita.utils.serialization.Serializer;

public interface JSONSerializer<SOURCE_TYPE> extends Serializer<JSON, SOURCE_TYPE, JSONStr> {
}