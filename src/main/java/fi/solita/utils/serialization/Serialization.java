package fi.solita.utils.serialization;

import fi.solita.utils.functional.Either;
import fi.solita.utils.serialization.Deserializer.Failure;

public interface Serialization<FORMAT,SERIAL_REPRESENTATION> {
    public <SOURCE_TYPE> SERIAL_REPRESENTATION serialize(Serializer<FORMAT,SOURCE_TYPE,SERIAL_REPRESENTATION> serializer, SOURCE_TYPE object);
    
    public <TARGET_TYPE> Either<Failure<TARGET_TYPE>,TARGET_TYPE> deserialize(Deserializer<FORMAT,TARGET_TYPE,SERIAL_REPRESENTATION> deserializer, SERIAL_REPRESENTATION json);
}
