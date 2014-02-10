package fi.solita.utils.serialization;

import fi.solita.utils.functional.Either;
import fi.solita.utils.serialization.Deserializer;

public abstract class AbstractDeserializer<FORMAT, SERIAL_REPRESENTATION, TARGET_TYPE> implements Deserializer<FORMAT, SERIAL_REPRESENTATION, TARGET_TYPE> {
    /**
     * if you override this, be sure to wrap all exceptions to a {@link Failure}.
     */
    @Override
    public Either<Failure<TARGET_TYPE>, TARGET_TYPE> deserialize(FORMAT format, SERIAL_REPRESENTATION serial) {
        try {
            return Either.right(deserializeOptimistic(format, serial));
        } catch (RuntimeException e) {
            return Either.left(Failure.of((TARGET_TYPE)null, e));
        }
    }
    
    /**
     * Override this to assume the deserialization succeeds, and possible errors are
     * indicated by throwing exceptions. {@link #deserialize} will wrap the exception
     * to a {@link Failure}.
     */
    protected TARGET_TYPE deserializeOptimistic(FORMAT format, SERIAL_REPRESENTATION serial) {
        throw new UnsupportedOperationException("implement this or override deserialize!");
    }
}