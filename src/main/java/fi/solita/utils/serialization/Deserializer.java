package fi.solita.utils.serialization;

public interface Deserializer<FORMAT_TAG, SERIAL_REPRESENTATION, TYPE> {
    TYPE deserialize(FORMAT_TAG format, SERIAL_REPRESENTATION serial);
}