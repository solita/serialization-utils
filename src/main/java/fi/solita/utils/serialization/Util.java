package fi.solita.utils.serialization;

import fi.solita.utils.codegen.MetaNamedMember;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Function;
import fi.solita.utils.functional.Function0;
import fi.solita.utils.serialization.Deserializer.Failure;

public abstract class Util {
    private Util() {
    }
    
    /**
     * Serializer for a named field of an object
     */
    public static final class FieldSerializer<FORMAT,OWNER,T,SERIAL> {
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
            return serializer.serialize(format, getter.apply(owner));
        }
    }
    
    /**
     * Deserializer for a named value
     */
    public static final class FieldDeserializer<FORMAT,OWNER,T,SERIAL> {
        private Function0<String> name;
        private Deserializer<FORMAT,SERIAL, ? extends T> deserializer;

        FieldDeserializer(Function0<String> name, Deserializer<FORMAT, SERIAL, ? extends T> deserializer) {
            this.name = name;
            this.deserializer = deserializer;
        }
        
        public String getName() {
            return name.apply();
        }

        @SuppressWarnings("unchecked")
        public Either<Failure<T>,T> apply(FORMAT format, SERIAL serial) {
            return ((Deserializer<FORMAT, SERIAL, T>)deserializer).deserialize(format, serial);
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
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldDeserializer<FORMAT,OWNER,T,SERIAL> field(Function0<String> name, Deserializer<FORMAT, SERIAL, ? extends T> deserializer) {
        return new FieldDeserializer<FORMAT,OWNER,T,SERIAL>(name, deserializer);
    }

    public static final <FORMAT,OWNER,T,SERIAL> FieldDeserializer<FORMAT,OWNER,T,SERIAL> field(String name, Deserializer<FORMAT, SERIAL, ? extends T> deserializer) {
        return field(Function.of(name), deserializer);
    }
    
    public static final <FORMAT,OWNER,T,SERIAL> FieldDeserializer<FORMAT,OWNER,T,SERIAL> field(MetaNamedMember<OWNER, T> m, Deserializer<FORMAT, SERIAL, ? extends T> deserializer) {
        return field(m.getName(), deserializer);
    }
}
