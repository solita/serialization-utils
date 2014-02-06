package fi.solita.utils.serialization;

public interface Serializer<FORMAT_TAG, TYPE, SERIAL_REPRESENTATION> {
    SERIAL_REPRESENTATION serialize(FORMAT_TAG f, TYPE object);
}
