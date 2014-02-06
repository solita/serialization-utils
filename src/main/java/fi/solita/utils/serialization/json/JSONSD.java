package fi.solita.utils.serialization.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.solita.utils.codegen.MetaField;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Tuple2;
import fi.solita.utils.functional.Tuple3;
import fi.solita.utils.functional.Tuple4;
import fi.solita.utils.functional.Tuple5;
import fi.solita.utils.functional.Tuple6;
import fi.solita.utils.functional.Tuple7;
import fi.solita.utils.serialization.Util;

/**
 * A combined serializer/deserializer.
 */
public final class JSONSD<T> implements JSONSerializer<T>, JSONDeserializer<T> {
    public final JSONSerializer<? super T> serializer;
    public final JSONDeserializer<? extends T> deserializer;
    
    public JSONSD(JSONSerializer<? super T> serializer, JSONDeserializer<? extends T> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }
    
    @Override
    public JSONStr serialize(JSON f, T v) {
        return serializer.serialize(f, v);
    }

    @Override
    public T deserialize(JSON f, JSONStr v) {
        return deserializer.deserialize(f, v);
    }
    
    public static final JSONSD<String> string = new JSONSD<String>(JSONSerialization.string, JSONDeserialization.string);
    
    public static final JSONSD<Boolean> bool = new JSONSD<Boolean>(JSONSerialization.bool, JSONDeserialization.bool);
    
    public static final JSONSD<Integer> integer = new JSONSD<Integer>(JSONSerialization.integer, JSONDeserialization.integer);

    public static final JSONSD<Long> lng = new JSONSD<Long>(JSONSerialization.lng, JSONDeserialization.lng);

    public static final JSONSD<Short> shrt = new JSONSD<Short>(JSONSerialization.shrt, JSONDeserialization.shrt);

    public static final JSONSD<Float> flt = new JSONSD<Float>(JSONSerialization.flt, JSONDeserialization.flt);

    public static final JSONSD<Double> dbl = new JSONSD<Double>(JSONSerialization.dbl, JSONDeserialization.dbl);

    public static final JSONSD<BigInteger> bigint = new JSONSD<BigInteger>(JSONSerialization.bigint, JSONDeserialization.bigint);

    public static final JSONSD<BigDecimal> bigdecimal = new JSONSD<BigDecimal>(JSONSerialization.bigdecimal, JSONDeserialization.bigdecimal);
    
    public static final <T> JSONSD<Option<T>> option(JSONSD<T> s) {
        return new JSONSD<Option<T>>(JSONSerialization.option(s.serializer), JSONDeserialization.option(s.deserializer));
    }
    
    public static final <T> JSONSD<T[]> array(final JSONSD<T> s, Class<T> clazz) {
        return new JSONSD<T[]>(JSONSerialization.array(s.serializer), JSONDeserialization.array(s.deserializer, clazz));
    }
    
    public static final <T> JSONSD<List<T>> list(JSONSD<T> sd) {
        return new JSONSD<List<T>>(JSONSerialization.iter(sd.serializer), JSONDeserialization.list(sd.deserializer));
    }
    
    public static final <T> JSONSD<Set<T>> set(JSONSD<T> sd) {
        return new JSONSD<Set<T>>(JSONSerialization.iter(sd.serializer), JSONDeserialization.set(sd.deserializer));
    }
    
    public static final <T> JSONSD<Map<String, T>> map(JSONSD<T> s) {
        return new JSONSD<Map<String,T>>(JSONSerialization.map(s.serializer), JSONDeserialization.map(s.deserializer));
    }
    
    public static final <T,T1,T2> JSONSD<T> object(MetaField<? super T, T1> f, JSONSD<T1> sd1, Apply<? super T1,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f, sd1.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f, sd1.deserializer),
                               c));
    }
    
    public static final <T,T1,T2> JSONSD<T> object(Tuple2<? extends MetaField<? super T, T1>, ? extends MetaField<? super T, T2>> f, JSONSD<T1> sd1, JSONSD<T2> sd2, Apply<? extends Tuple2<? super T1, ? super T2>,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f._1, sd1.serializer),
                               Util.pair(f._2, sd2.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f._1, sd1.deserializer),
                               Util.pair(f._2, sd2.deserializer),
                               c));
    }
    
    public static final <T,T1,T2,T3> JSONSD<T> object(Tuple3<? extends MetaField<? super T, T1>, ? extends MetaField<? super T, T2>, ? extends MetaField<? super T, T3>> f, JSONSD<T1> sd1, JSONSD<T2> sd2, JSONSD<T3> sd3, Apply<? extends Tuple3<? super T1,? super T2,? super T3>,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f._1, sd1.serializer),
                               Util.pair(f._2, sd2.serializer),
                               Util.pair(f._3, sd3.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f._1, sd1.deserializer),
                               Util.pair(f._2, sd2.deserializer),
                               Util.pair(f._3, sd3.deserializer),
                               c));
    }
    
    public static final <T,T1,T2,T3,T4> JSONSD<T> object(Tuple4<? extends MetaField<? super T, T1>, ? extends MetaField<? super T, T2>, ? extends MetaField<? super T, T3>, ? extends MetaField<? super T, T4>> f, JSONSD<T1> sd1, JSONSD<T2> sd2, JSONSD<T3> sd3, JSONSD<T4> sd4, Apply<? extends Tuple4<? super T1,? super T2,? super T3,? super T4>,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f._1, sd1.serializer),
                               Util.pair(f._2, sd2.serializer),
                               Util.pair(f._3, sd3.serializer),
                               Util.pair(f._4, sd4.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f._1, sd1.deserializer),
                               Util.pair(f._2, sd2.deserializer),
                               Util.pair(f._3, sd3.deserializer),
                               Util.pair(f._4, sd4.deserializer),
                               c));
    }
    
    public static final <T,T1,T2,T3,T4,T5> JSONSD<T> object(Tuple5<? extends MetaField<? super T, T1>, ? extends MetaField<? super T, T2>, ? extends MetaField<? super T, T3>, ? extends MetaField<? super T, T4>, ? extends MetaField<? super T, T5>> f, JSONSD<T1> sd1, JSONSD<T2> sd2, JSONSD<T3> sd3, JSONSD<T4> sd4, JSONSD<T5> sd5, Apply<? extends Tuple5<? super T1,? super T2,? super T3,? super T4,? super T5>,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f._1, sd1.serializer),
                               Util.pair(f._2, sd2.serializer),
                               Util.pair(f._3, sd3.serializer),
                               Util.pair(f._4, sd4.serializer),
                               Util.pair(f._5, sd5.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f._1, sd1.deserializer),
                               Util.pair(f._2, sd2.deserializer),
                               Util.pair(f._3, sd3.deserializer),
                               Util.pair(f._4, sd4.deserializer),
                               Util.pair(f._5, sd5.deserializer),
                               c));
    }
    
    public static final <T,T1,T2,T3,T4,T5,T6> JSONSD<T> object(Tuple6<? extends MetaField<? super T, T1>, ? extends MetaField<? super T, T2>, ? extends MetaField<? super T, T3>, ? extends MetaField<? super T, T4>, ? extends MetaField<? super T, T5>, ? extends MetaField<? super T, T6>> f, JSONSD<T1> sd1, JSONSD<T2> sd2, JSONSD<T3> sd3, JSONSD<T4> sd4, JSONSD<T5> sd5, JSONSD<T6> sd6, Apply<? extends Tuple6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6>,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f._1, sd1.serializer),
                               Util.pair(f._2, sd2.serializer),
                               Util.pair(f._3, sd3.serializer),
                               Util.pair(f._4, sd4.serializer),
                               Util.pair(f._5, sd5.serializer),
                               Util.pair(f._6, sd6.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f._1, sd1.deserializer),
                               Util.pair(f._2, sd2.deserializer),
                               Util.pair(f._3, sd3.deserializer),
                               Util.pair(f._4, sd4.deserializer),
                               Util.pair(f._5, sd5.deserializer),
                               Util.pair(f._6, sd6.deserializer),
                               c));
    }
    
    public static final <T,T1,T2,T3,T4,T5,T6,T7> JSONSD<T> object(Tuple7<? extends MetaField<? super T, T1>, ? extends MetaField<? super T, T2>, ? extends MetaField<? super T, T3>, ? extends MetaField<? super T, T4>, ? extends MetaField<? super T, T5>, ? extends MetaField<? super T, T6>, ? extends MetaField<? super T, T7>> f, JSONSD<T1> sd1, JSONSD<T2> sd2, JSONSD<T3> sd3, JSONSD<T4> sd4, JSONSD<T5> sd5, JSONSD<T6> sd6, JSONSD<T7> sd7, Apply<? extends Tuple7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7>,T> c) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.pair(f._1, sd1.serializer),
                               Util.pair(f._2, sd2.serializer),
                               Util.pair(f._3, sd3.serializer),
                               Util.pair(f._4, sd4.serializer),
                               Util.pair(f._5, sd5.serializer),
                               Util.pair(f._6, sd6.serializer),
                               Util.pair(f._7, sd7.serializer)),
                             JSONDeserialization.object(
                               Util.pair(f._1, sd1.deserializer),
                               Util.pair(f._2, sd2.deserializer),
                               Util.pair(f._3, sd3.deserializer),
                               Util.pair(f._4, sd4.deserializer),
                               Util.pair(f._5, sd5.deserializer),
                               Util.pair(f._6, sd6.deserializer),
                               Util.pair(f._7, sd7.deserializer),
                               c));
    }
}