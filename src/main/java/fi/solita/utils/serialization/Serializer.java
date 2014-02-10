package fi.solita.utils.serialization;

public interface Serializer<FORMAT, SOURCE_TYPE, SERIAL_REPRESENTATION> {
    SERIAL_REPRESENTATION serialize(FORMAT format, SOURCE_TYPE object);
}
