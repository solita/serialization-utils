package fi.solita.utils.serialization;

import java.io.Serializable;

public interface Serializer<FORMAT, SOURCE_TYPE, SERIAL_REPRESENTATION> extends Serializable {
    SERIAL_REPRESENTATION serialize(FORMAT format, SOURCE_TYPE object);
}
