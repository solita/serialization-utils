package fi.solita.utils.serialization;


/**
 * A combined serializer/deserializer.
 */
public interface SD<FORMAT,TYPE,SERIAL> extends Serializer<FORMAT,TYPE,SERIAL>, Deserializer<FORMAT,TYPE,SERIAL> {
}