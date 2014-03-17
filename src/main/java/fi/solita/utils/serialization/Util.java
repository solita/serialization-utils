package fi.solita.utils.serialization;

import java.io.Serializable;

import fi.solita.utils.codegen.MetaNamedMember;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Function;
import fi.solita.utils.functional.Function0;
import fi.solita.utils.serialization.Deserializer.Failure;

public abstract class Util {
    private Util() {
    }
    
    public static final <FORMAT,T,D,SERIAL> SD<FORMAT,T,SERIAL> delegate(final SD<FORMAT,D,SERIAL> sd, final Apply<? super T, ? extends D> toDelegate, final Apply<? super D, ? extends T> fromDelegate) {
        return new SD<FORMAT,T,SERIAL>() {
            @Override
            public SERIAL serialize(FORMAT format, T object) {
                return delegate(sd, toDelegate).serialize(format, object);
            }

            @Override
            public Either<Failure<T>, T> deserialize(FORMAT format, SERIAL serial) {
                return delegate(sd, fromDelegate).deserialize(format, serial);
            }
        };
    }
    
    public static final <FORMAT,SERIAL,T, D> Serializer<FORMAT,T,SERIAL> delegate(final Serializer<FORMAT,D,SERIAL> sd, final Apply<? super T, ? extends D> toDelegate) {
        return new Serializer<FORMAT,T,SERIAL>() {
            @Override
            public SERIAL serialize(FORMAT format, T object) {
                return sd.serialize(format, toDelegate.apply(object));
            }
        };
    }

    public static final <FORMAT,SERIAL,T, D> Deserializer<FORMAT,T,SERIAL> delegate(final Deserializer<FORMAT,D,SERIAL> sd, final Apply<? super D, ? extends T> fromDelegate) {
        return new Deserializer<FORMAT,T,SERIAL>() {
            @Override
            public Either<Failure<T>, T> deserialize(FORMAT format, SERIAL serial) {
                Either<Failure<D>, D> d = sd.deserialize(format, serial);
                if (d.isRight()) {
                    try {
                        return Either.right((T)fromDelegate.apply(d.right.get()));
                    } catch (RuntimeException e) {
                        return Either.left(Failure.<T>of(null, e));
                    }
                } else {
                    Failure<D> failure = d.left.get();
                    T partial;
                    try {
                        partial = fromDelegate.apply(failure.partialResult);
                    } catch (Exception e) {
                        partial = null;
                    }
                    return Either.left(Failure.<T>of(partial, failure.errors));
                }
            }
        };
    }
    
    public static final <FORMAT,SERIAL> SD<FORMAT,SERIAL,SERIAL> id() {
        return new SD<FORMAT,SERIAL,SERIAL>() {
            public SERIAL serialize(FORMAT format, SERIAL object) {
                return object;
            };
            public Either<Failure<SERIAL>,SERIAL> deserialize(FORMAT format, SERIAL serial) {
                return Either.right(serial);
            };
        };
    }
    
    /**
     * Serializer for a named field of an object
     */
    public static final class FieldSerializer<FORMAT,OWNER,T,SERIAL> implements Serializable {
        private Apply<OWNER, T> getter;
        private Function0<String> name;
        private Serializer<FORMAT, ? super T, SERIAL> serializer;

        public FieldSerializer(Function0<String> name, Apply<OWNER, T> getter, Serializer<FORMAT, ? super T, SERIAL> serializer) {
            this.getter = getter;
            this.name = name;
            this.serializer = serializer;
        }
        
        public String getName() {
            return name.apply();
        }
        
        public SERIAL apply(FORMAT format, OWNER owner) {
            if (owner == null) {
                throw new NullPointerException("owner was null");
            }
            T target = getter.apply(owner);
            if (target == null) {
                throw new NullPointerException("target: " + getter + " for owner: " + owner + " was null");
            }
            return serializer.serialize(format, target);
        }
    }
    
    /**
     * Deserializer for a named value
     */
    public static final class FieldDeserializer<FORMAT,OWNER,T,SERIAL> implements Serializable {
        private Function0<String> name;
        private Deserializer<FORMAT, ? extends T,SERIAL> deserializer;

        FieldDeserializer(Function0<String> name, Deserializer<FORMAT, ? extends T, SERIAL> deserializer) {
            this.name = name;
            this.deserializer = deserializer;
        }
        
        public String getName() {
            return name.apply();
        }

        @SuppressWarnings("unchecked")
        public Either<Failure<T>,T> apply(FORMAT format, SERIAL serial) {
            return ((Deserializer<FORMAT, T, SERIAL>)deserializer).deserialize(format, serial);
        }
    }
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldSerializer<FORMAT,OWNER,T,SERIAL> field(Function0<String> name, Apply<OWNER, T> getter, Serializer<FORMAT, ? super T, SERIAL> serializer) {
        return new FieldSerializer<FORMAT,OWNER,T,SERIAL>(name, getter, serializer);
    }
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldSerializer<FORMAT,OWNER,T,SERIAL> field(String name, Apply<OWNER, T> getter, Serializer<FORMAT, ? super T, SERIAL> serializer) {
        return field(Function.of(name), getter, serializer);
    }
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldSerializer<FORMAT,OWNER,T,SERIAL> field(MetaNamedMember<OWNER, T> getter, Serializer<FORMAT, ? super T, SERIAL> serializer) {
        return field(getter.getName(), getter, serializer);
    }
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldDeserializer<FORMAT,OWNER,T,SERIAL> field(Function0<String> name, Deserializer<FORMAT, ? extends T, SERIAL> deserializer) {
        return new FieldDeserializer<FORMAT,OWNER,T,SERIAL>(name, deserializer);
    }

    public static final <FORMAT,OWNER,T,SERIAL> FieldDeserializer<FORMAT,OWNER,T,SERIAL> field(String name, Deserializer<FORMAT, ? extends T, SERIAL> deserializer) {
        return field(Function.of(name), deserializer);
    }
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldDeserializer<FORMAT,OWNER,T,SERIAL> field(MetaNamedMember<OWNER, T> m, Deserializer<FORMAT, ? extends T, SERIAL> deserializer) {
        return field(m.getName(), deserializer);
    }
}
