package fi.solita.utils.serialization;

import static fi.solita.utils.functional.Collections.newList;
import fi.solita.utils.functional.Either;

public interface Deserializer<FORMAT, SERIAL_REPRESENTATION, TARGET_TYPE> {
    public static final class Failure<T> {
        /**
         * An <i>invalid</i> result object, if one could be constructed from partial data.
         * This little beast may even be <i>null</i>, which in this case represents
         * uninitialized data (that is, data that cannot be initialized).
         * 
         * Use at your own risk. 
         */
        public final T partialResult;
        
        /**
         * All errors that occurred during deserialization.
         */
        public final Iterable<Either<Exception,Object>> errors;
        
        public static final <T> Failure<T> of(T partialResult, Iterable<Either<Exception,Object>> errors) {
            return new Failure<T>(partialResult, errors);
        }
        
        public static final <T> Failure<T> of(T partialResult, Exception error) {
            return new Failure<T>(partialResult, newList(Either.left(error)));
        }
        
        public static final <T> Failure<T> of(T partialResult, String error) {
            return new Failure<T>(partialResult, newList(Either.<Exception,Object>right(error)));
        }
        
        private Failure(T partialResult, Iterable<Either<Exception,Object>> errors) {
            this.partialResult = partialResult;
            this.errors = errors;
        }
    }
    
    public Either<Failure<TARGET_TYPE>,TARGET_TYPE> deserialize(FORMAT format, SERIAL_REPRESENTATION serial);
}