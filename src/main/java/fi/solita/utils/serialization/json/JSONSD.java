package fi.solita.utils.serialization.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.solita.utils.codegen.MetaField;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Tuple10;
import fi.solita.utils.functional.Tuple11;
import fi.solita.utils.functional.Tuple12;
import fi.solita.utils.functional.Tuple13;
import fi.solita.utils.functional.Tuple14;
import fi.solita.utils.functional.Tuple15;
import fi.solita.utils.functional.Tuple16;
import fi.solita.utils.functional.Tuple17;
import fi.solita.utils.functional.Tuple18;
import fi.solita.utils.functional.Tuple19;
import fi.solita.utils.functional.Tuple2;
import fi.solita.utils.functional.Tuple20;
import fi.solita.utils.functional.Tuple3;
import fi.solita.utils.functional.Tuple4;
import fi.solita.utils.functional.Tuple5;
import fi.solita.utils.functional.Tuple6;
import fi.solita.utils.functional.Tuple7;
import fi.solita.utils.functional.Tuple8;
import fi.solita.utils.functional.Tuple9;
import fi.solita.utils.serialization.Util;

/**
 * A combined serializer/deserializer.
 */
public final class JSONSD<TYPE> extends JSONDeserializer<TYPE> implements JSONSerializer<TYPE> {
    public final JSONSerializer<? super TYPE> serializer;
    public final JSONDeserializer<? extends TYPE> deserializer;
    
    public JSONSD(JSONSerializer<? super TYPE> serializer, JSONDeserializer<? extends TYPE> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }
    
    @Override
    public JSONStr serialize(JSON format, TYPE object) {
        return serializer.serialize(format, object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Either<Failure<TYPE>,TYPE> deserialize(JSON format, JSONStr object) {
        return ((JSONDeserializer<TYPE>)deserializer).deserialize(format, object);
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
    
    public static final <T> JSONSD<Option<T>> option(JSONSD<T> sd) {
        return new JSONSD<Option<T>>(JSONSerialization.option(sd.serializer), JSONDeserialization.option(sd.deserializer));
    }
    
    public static final <T> JSONSD<T[]> array(final JSONSD<T> sd, Class<T> clazz) {
        return new JSONSD<T[]>(JSONSerialization.array(sd.serializer), JSONDeserialization.array(sd.deserializer, clazz));
    }
    
    public static final <T> JSONSD<List<T>> list(JSONSD<T> sd) {
        return new JSONSD<List<T>>(JSONSerialization.iter(sd.serializer), JSONDeserialization.list(sd.deserializer));
    }
    
    public static final <T> JSONSD<Set<T>> set(JSONSD<T> sd) {
        return new JSONSD<Set<T>>(JSONSerialization.iter(sd.serializer), JSONDeserialization.set(sd.deserializer));
    }
    
    public static final <T> JSONSD<Map<String, T>> map(JSONSD<T> sd) {
        return new JSONSD<Map<String,T>>(JSONSerialization.map(sd.serializer), JSONDeserialization.map(sd.deserializer));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1> JSONSD<T> object(
            MetaField<? super T, F1> field,
            JSONSD<F1> sd1, Apply<? super F1,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(field, sd1.serializer)),
                             JSONDeserialization.object(
                               Util.field(field, sd1.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2> JSONSD<T> object(
            Tuple2<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            Apply<? extends Tuple2<? super F1, ? super F2>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3> JSONSD<T> object(
            Tuple3<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            Apply<? extends Tuple3<? super F1,? super F2,? super F3>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4> JSONSD<T> object(
            Tuple4<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            Apply<? extends Tuple4<? super F1,? super F2,? super F3,? super F4>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5> JSONSD<T> object(
            Tuple5<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            Apply<? extends Tuple5<? super F1,? super F2,? super F3,? super F4,? super F5>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6> JSONSD<T> object(
            Tuple6<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            Apply<? extends Tuple6<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7> JSONSD<T> object(
            Tuple7<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            Apply<? extends Tuple7<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8> JSONSD<T> object(
            Tuple8<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            Apply<? extends Tuple8<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9> JSONSD<T> object(
            Tuple9<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            Apply<? extends Tuple9<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10> JSONSD<T> object(
            Tuple10<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            Apply<? extends Tuple10<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11> JSONSD<T> object(
            Tuple11<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            Apply<? extends Tuple11<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12> JSONSD<T> object(
            Tuple12<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            Apply<? extends Tuple12<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13> JSONSD<T> object(
            Tuple13<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            Apply<? extends Tuple13<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14> JSONSD<T> object(
            Tuple14<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            Apply<? extends Tuple14<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15> JSONSD<T> object(
            Tuple15<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>, ? extends MetaField<? super T, F15>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            JSONSD<F15> sd15,
            Apply<? extends Tuple15<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer),
                               Util.field(fields._15, sd15.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               Util.field(fields._15, sd15.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16> JSONSD<T> object(
            Tuple16<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>, ? extends MetaField<? super T, F15>, ? extends MetaField<? super T, F16>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            JSONSD<F15> sd15,
            JSONSD<F16> sd16,
            Apply<? extends Tuple16<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer),
                               Util.field(fields._15, sd15.serializer),
                               Util.field(fields._16, sd16.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               Util.field(fields._15, sd15.deserializer),
                               Util.field(fields._16, sd16.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17> JSONSD<T> object(
            Tuple17<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>, ? extends MetaField<? super T, F15>, ? extends MetaField<? super T, F16>, ? extends MetaField<? super T, F17>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            JSONSD<F15> sd15,
            JSONSD<F16> sd16,
            JSONSD<F17> sd17,
            Apply<? extends Tuple17<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer),
                               Util.field(fields._15, sd15.serializer),
                               Util.field(fields._16, sd16.serializer),
                               Util.field(fields._17, sd17.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               Util.field(fields._15, sd15.deserializer),
                               Util.field(fields._16, sd16.deserializer),
                               Util.field(fields._17, sd17.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18> JSONSD<T> object(
            Tuple18<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>, ? extends MetaField<? super T, F15>, ? extends MetaField<? super T, F16>, ? extends MetaField<? super T, F17>, ? extends MetaField<? super T, F18>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            JSONSD<F15> sd15,
            JSONSD<F16> sd16,
            JSONSD<F17> sd17,
            JSONSD<F18> sd18,
            Apply<? extends Tuple18<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer),
                               Util.field(fields._15, sd15.serializer),
                               Util.field(fields._16, sd16.serializer),
                               Util.field(fields._17, sd17.serializer),
                               Util.field(fields._18, sd18.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               Util.field(fields._15, sd15.deserializer),
                               Util.field(fields._16, sd16.deserializer),
                               Util.field(fields._17, sd17.deserializer),
                               Util.field(fields._18, sd18.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19> JSONSD<T> object(
            Tuple19<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>, ? extends MetaField<? super T, F15>, ? extends MetaField<? super T, F16>, ? extends MetaField<? super T, F17>, ? extends MetaField<? super T, F18>, ? extends MetaField<? super T, F19>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            JSONSD<F15> sd15,
            JSONSD<F16> sd16,
            JSONSD<F17> sd17,
            JSONSD<F18> sd18,
            JSONSD<F19> sd19,
            Apply<? extends Tuple19<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer),
                               Util.field(fields._15, sd15.serializer),
                               Util.field(fields._16, sd16.serializer),
                               Util.field(fields._17, sd17.serializer),
                               Util.field(fields._18, sd18.serializer),
                               Util.field(fields._19, sd19.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               Util.field(fields._15, sd15.deserializer),
                               Util.field(fields._16, sd16.deserializer),
                               Util.field(fields._17, sd17.deserializer),
                               Util.field(fields._18, sd18.deserializer),
                               Util.field(fields._19, sd19.deserializer),
                               constructor));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20> JSONSD<T> object(
            Tuple20<? extends MetaField<? super T, F1>, ? extends MetaField<? super T, F2>, ? extends MetaField<? super T, F3>, ? extends MetaField<? super T, F4>, ? extends MetaField<? super T, F5>, ? extends MetaField<? super T, F6>, ? extends MetaField<? super T, F7>, ? extends MetaField<? super T, F8>, ? extends MetaField<? super T, F9>, ? extends MetaField<? super T, F10>, ? extends MetaField<? super T, F11>, ? extends MetaField<? super T, F12>, ? extends MetaField<? super T, F13>, ? extends MetaField<? super T, F14>, ? extends MetaField<? super T, F15>, ? extends MetaField<? super T, F16>, ? extends MetaField<? super T, F17>, ? extends MetaField<? super T, F18>, ? extends MetaField<? super T, F19>, ? extends MetaField<? super T, F20>> fields,
            JSONSD<F1> sd1,
            JSONSD<F2> sd2,
            JSONSD<F3> sd3,
            JSONSD<F4> sd4,
            JSONSD<F5> sd5,
            JSONSD<F6> sd6,
            JSONSD<F7> sd7,
            JSONSD<F8> sd8,
            JSONSD<F9> sd9,
            JSONSD<F10> sd10,
            JSONSD<F11> sd11,
            JSONSD<F12> sd12,
            JSONSD<F13> sd13,
            JSONSD<F14> sd14,
            JSONSD<F15> sd15,
            JSONSD<F16> sd16,
            JSONSD<F17> sd17,
            JSONSD<F18> sd18,
            JSONSD<F19> sd19,
            JSONSD<F20> sd20,
            Apply<? extends Tuple20<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19,? super F20>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(
                               Util.field(fields._1, sd1.serializer),
                               Util.field(fields._2, sd2.serializer),
                               Util.field(fields._3, sd3.serializer),
                               Util.field(fields._4, sd4.serializer),
                               Util.field(fields._5, sd5.serializer),
                               Util.field(fields._6, sd6.serializer),
                               Util.field(fields._7, sd7.serializer),
                               Util.field(fields._8, sd8.serializer),
                               Util.field(fields._9, sd9.serializer),
                               Util.field(fields._10, sd10.serializer),
                               Util.field(fields._11, sd11.serializer),
                               Util.field(fields._12, sd12.serializer),
                               Util.field(fields._13, sd13.serializer),
                               Util.field(fields._14, sd14.serializer),
                               Util.field(fields._15, sd15.serializer),
                               Util.field(fields._16, sd16.serializer),
                               Util.field(fields._17, sd17.serializer),
                               Util.field(fields._18, sd18.serializer),
                               Util.field(fields._19, sd19.serializer),
                               Util.field(fields._20, sd20.serializer)),
                             JSONDeserialization.object(
                               Util.field(fields._1, sd1.deserializer),
                               Util.field(fields._2, sd2.deserializer),
                               Util.field(fields._3, sd3.deserializer),
                               Util.field(fields._4, sd4.deserializer),
                               Util.field(fields._5, sd5.deserializer),
                               Util.field(fields._6, sd6.deserializer),
                               Util.field(fields._7, sd7.deserializer),
                               Util.field(fields._8, sd8.deserializer),
                               Util.field(fields._9, sd9.deserializer),
                               Util.field(fields._10, sd10.deserializer),
                               Util.field(fields._11, sd11.deserializer),
                               Util.field(fields._12, sd12.deserializer),
                               Util.field(fields._13, sd13.deserializer),
                               Util.field(fields._14, sd14.deserializer),
                               Util.field(fields._15, sd15.deserializer),
                               Util.field(fields._16, sd16.deserializer),
                               Util.field(fields._17, sd17.deserializer),
                               Util.field(fields._18, sd18.deserializer),
                               Util.field(fields._19, sd19.deserializer),
                               Util.field(fields._20, sd20.deserializer),
                               constructor));
    }
}