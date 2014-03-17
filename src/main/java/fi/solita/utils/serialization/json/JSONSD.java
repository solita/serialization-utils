package fi.solita.utils.serialization.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import fi.solita.utils.codegen.MetaNamedMember;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Tuple1;
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
import fi.solita.utils.functional.Tuple21;
import fi.solita.utils.functional.Tuple22;
import fi.solita.utils.functional.Tuple3;
import fi.solita.utils.functional.Tuple4;
import fi.solita.utils.functional.Tuple5;
import fi.solita.utils.functional.Tuple6;
import fi.solita.utils.functional.Tuple7;
import fi.solita.utils.functional.Tuple8;
import fi.solita.utils.functional.Tuple9;
import fi.solita.utils.serialization.Deserializer;
import fi.solita.utils.serialization.SD;
import fi.solita.utils.serialization.Serializer;
import fi.solita.utils.serialization.Util;

public final class JSONSD<TYPE> extends JSONDeserializer<TYPE> implements JSONSerializer<TYPE>, SD<JSON,TYPE,JSONStr> {
    public final Serializer<JSON,? super TYPE,JSONStr> serializer;
    public final Deserializer<JSON,? extends TYPE,JSONStr> deserializer;
    
    public JSONSD(Serializer<JSON,? super TYPE,JSONStr> serializer, Deserializer<JSON,? extends TYPE,JSONStr> deserializer) {
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

    public static final JSONSD<Byte> byt = new JSONSD<Byte>(JSONSerialization.byt, JSONDeserialization.byt);
    
    public static final JSONSD<BigInteger> bigint = new JSONSD<BigInteger>(JSONSerialization.bigint, JSONDeserialization.bigint);

    public static final JSONSD<BigDecimal> bigdecimal = new JSONSD<BigDecimal>(JSONSerialization.bigdecimal, JSONDeserialization.bigdecimal);
    
    static final String className(Class<?> clazz) {
        return clazz.getName();
    }
    
    static final Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
    
    static final String enumName(Enum<?> e) {
        return e.name();
    }
    
    static final <E extends Enum<E>> E newEnum(Class<E> enumClass, String enumName) {
        return Enum.valueOf(enumClass, enumName);
    }
    
    public static final <E extends Enum<E>> SD<JSON,E,JSONStr> Enum(Class<E> enumClass) {
        return Util.delegate(string, JSONSD_.enumName, JSONSD_.<E>newEnum().ap(enumClass));
    }
    
    public static final SD<JSON,Class<?>,JSONStr> clazz = Util.delegate(JSONSD.string, JSONSD_.className, JSONSD_.classForName);
    
    public static final <T> JSONSD<Option<T>> option(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<Option<T>>(JSONSerialization.option(sd), JSONDeserialization.option(sd));
    }
    
    public static final <L,R> JSONSD<Either<L,R>> either(SD<JSON,L,JSONStr> sdL, SD<JSON,R,JSONStr> sdR) {
        return new JSONSD<Either<L,R>>(JSONSerialization.either(sdL, sdR), JSONDeserialization.either(sdL, sdR));
    }
    
    public static final <L,R> JSONSD<Pair<L,R>> pair(SD<JSON,L,JSONStr> sdL, SD<JSON,R,JSONStr> sdR) {
        return new JSONSD<Pair<L,R>>(JSONSerialization.pair(sdL, sdR), JSONDeserialization.pair(sdL, sdR));
    }
    
    public static final <T> JSONSD<T[]> array(SD<JSON,T,JSONStr> sd, Class<T> clazz) {
        return new JSONSD<T[]>(JSONSerialization.array(sd), JSONDeserialization.array(sd, clazz));
    }
    
    public static final <T> JSONSD<Iterable<T>> iterable(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<Iterable<T>>(JSONSerialization.iterable(sd), JSONDeserialization.iterable(sd));
    }
    
    public static final <T> JSONSD<Collection<T>> collection(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<Collection<T>>(JSONSerialization.collection(sd), JSONDeserialization.collection(sd));
    }
    
    public static final <T> JSONSD<List<T>> list(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<List<T>>(JSONSerialization.list(sd), JSONDeserialization.list(sd));
    }
    
    public static final <T> JSONSD<Set<T>> set(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<Set<T>>(JSONSerialization.set(sd), JSONDeserialization.set(sd));
    }
    
    public static final <T extends Comparable<T>> JSONSD<SortedSet<T>> sortedSet(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<SortedSet<T>>(JSONSerialization.sortedSet(sd), JSONDeserialization.sortedSet(sd));
    }
    
    public static final <T> JSONSD<Map<String, T>> map(SD<JSON,T,JSONStr> sd) {
        return new JSONSD<Map<String,T>>(JSONSerialization.map(sd), JSONDeserialization.map(sd));
    }
    
    public static final <T1> JSONSD<Tuple1<T1>> tuple(
            SD<JSON,T1,JSONStr> sd1) {
        return new JSONSD<Tuple1<T1>>(
            JSONSerialization.tuple(sd1),
            JSONDeserialization.tuple(sd1));
    }
    
    public static final <T1,T2> JSONSD<Tuple2<T1,T2>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2) {
        return new JSONSD<Tuple2<T1,T2>>(
            JSONSerialization.tuple(sd1, sd2),
            JSONDeserialization.tuple(sd1, sd2));
    }
    
    public static final <T1,T2,T3> JSONSD<Tuple3<T1,T2,T3>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3) {
        return new JSONSD<Tuple3<T1,T2,T3>>(
            JSONSerialization.tuple(sd1, sd2, sd3),
            JSONDeserialization.tuple(sd1, sd2, sd3));
    }
    
    public static final <T1,T2,T3,T4> JSONSD<Tuple4<T1,T2,T3,T4>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4) {
        return new JSONSD<Tuple4<T1,T2,T3,T4>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4));
    }
    
    public static final <T1,T2,T3,T4,T5> JSONSD<Tuple5<T1,T2,T3,T4,T5>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5) {
        return new JSONSD<Tuple5<T1,T2,T3,T4,T5>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5));
    }
    
    public static final <T1,T2,T3,T4,T5,T6> JSONSD<Tuple6<T1,T2,T3,T4,T5,T6>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6) {
        return new JSONSD<Tuple6<T1,T2,T3,T4,T5,T6>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7> JSONSD<Tuple7<T1,T2,T3,T4,T5,T6,T7>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7) {
        return new JSONSD<Tuple7<T1,T2,T3,T4,T5,T6,T7>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8> JSONSD<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8) {
        return new JSONSD<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9> JSONSD<Tuple9<T1,T2,T3,T4,T5,T6,T7,T8,T9>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9) {
        return new JSONSD<Tuple9<T1,T2,T3,T4,T5,T6,T7,T8,T9>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> JSONSD<Tuple10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10) {
        return new JSONSD<Tuple10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> JSONSD<Tuple11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11) {
        return new JSONSD<Tuple11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> JSONSD<Tuple12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12) {
        return new JSONSD<Tuple12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> JSONSD<Tuple13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13) {
        return new JSONSD<Tuple13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> JSONSD<Tuple14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14) {
        return new JSONSD<Tuple14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> JSONSD<Tuple15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15) {
        return new JSONSD<Tuple15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> JSONSD<Tuple16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16) {
        return new JSONSD<Tuple16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17> JSONSD<Tuple17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16,
            SD<JSON,T17,JSONStr> sd17) {
        return new JSONSD<Tuple17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18> JSONSD<Tuple18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16,
            SD<JSON,T17,JSONStr> sd17,
            SD<JSON,T18,JSONStr> sd18) {
        return new JSONSD<Tuple18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19> JSONSD<Tuple19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16,
            SD<JSON,T17,JSONStr> sd17,
            SD<JSON,T18,JSONStr> sd18,
            SD<JSON,T19,JSONStr> sd19) {
        return new JSONSD<Tuple19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20> JSONSD<Tuple20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16,
            SD<JSON,T17,JSONStr> sd17,
            SD<JSON,T18,JSONStr> sd18,
            SD<JSON,T19,JSONStr> sd19,
            SD<JSON,T20,JSONStr> sd20) {
        return new JSONSD<Tuple20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21> JSONSD<Tuple21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16,
            SD<JSON,T17,JSONStr> sd17,
            SD<JSON,T18,JSONStr> sd18,
            SD<JSON,T19,JSONStr> sd19,
            SD<JSON,T20,JSONStr> sd20,
            SD<JSON,T21,JSONStr> sd21) {
        return new JSONSD<Tuple21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20, sd21),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20, sd21));
    }
    
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22> JSONSD<Tuple22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22>> tuple(
            SD<JSON,T1,JSONStr> sd1,
            SD<JSON,T2,JSONStr> sd2,
            SD<JSON,T3,JSONStr> sd3,
            SD<JSON,T4,JSONStr> sd4,
            SD<JSON,T5,JSONStr> sd5,
            SD<JSON,T6,JSONStr> sd6,
            SD<JSON,T7,JSONStr> sd7,
            SD<JSON,T8,JSONStr> sd8,
            SD<JSON,T9,JSONStr> sd9,
            SD<JSON,T10,JSONStr> sd10,
            SD<JSON,T11,JSONStr> sd11,
            SD<JSON,T12,JSONStr> sd12,
            SD<JSON,T13,JSONStr> sd13,
            SD<JSON,T14,JSONStr> sd14,
            SD<JSON,T15,JSONStr> sd15,
            SD<JSON,T16,JSONStr> sd16,
            SD<JSON,T17,JSONStr> sd17,
            SD<JSON,T18,JSONStr> sd18,
            SD<JSON,T19,JSONStr> sd19,
            SD<JSON,T20,JSONStr> sd20,
            SD<JSON,T21,JSONStr> sd21,
            SD<JSON,T22,JSONStr> sd22) {
        return new JSONSD<Tuple22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22>>(
            JSONSerialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20, sd21, sd22),
            JSONDeserialization.tuple(sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20, sd21, sd22));
    }
    
    public static final <T,F1> JSONSD<T> object(
            MetaNamedMember<? super T, F1> field,
            SD<JSON,F1,JSONStr> sd1,
            Apply<? super F1,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(field, sd1),
                             JSONDeserialization.object(field, sd1, constructor));
    }
    
    public static final <T,F1> JSONSD<T> object(
            Tuple1<? extends MetaNamedMember<? super T, F1>> fields,
            SD<JSON,F1,JSONStr> sd1,
            Apply<? super F1,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields._1, sd1),
                             JSONDeserialization.object(fields._1, sd1, constructor));
    }
    
    public static final <T,F1,F2> JSONSD<T> object(
            Tuple2<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            Apply<? extends Tuple2<? super F1, ? super F2>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2),
                             JSONDeserialization.object(fields, sd1, sd2, constructor));
    }
    
    public static final <T,F1,F2,F3> JSONSD<T> object(
            Tuple3<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            Apply<? extends Tuple3<? super F1,? super F2,? super F3>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, constructor));
    }
    
    public static final <T,F1,F2,F3,F4> JSONSD<T> object(
            Tuple4<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            Apply<? extends Tuple4<? super F1,? super F2,? super F3,? super F4>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5> JSONSD<T> object(
            Tuple5<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            Apply<? extends Tuple5<? super F1,? super F2,? super F3,? super F4,? super F5>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6> JSONSD<T> object(
            Tuple6<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            Apply<? extends Tuple6<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7> JSONSD<T> object(
            Tuple7<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            Apply<? extends Tuple7<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8> JSONSD<T> object(
            Tuple8<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            Apply<? extends Tuple8<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9> JSONSD<T> object(
            Tuple9<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            Apply<? extends Tuple9<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10> JSONSD<T> object(
            Tuple10<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            Apply<? extends Tuple10<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11> JSONSD<T> object(
            Tuple11<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            Apply<? extends Tuple11<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12> JSONSD<T> object(
            Tuple12<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            Apply<? extends Tuple12<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13> JSONSD<T> object(
            Tuple13<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            Apply<? extends Tuple13<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14> JSONSD<T> object(
            Tuple14<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            Apply<? extends Tuple14<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15> JSONSD<T> object(
            Tuple15<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            SD<JSON,F15,JSONStr> sd15,
            Apply<? extends Tuple15<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16> JSONSD<T> object(
            Tuple16<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            SD<JSON,F15,JSONStr> sd15,
            SD<JSON,F16,JSONStr> sd16,
            Apply<? extends Tuple16<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17> JSONSD<T> object(
            Tuple17<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            SD<JSON,F15,JSONStr> sd15,
            SD<JSON,F16,JSONStr> sd16,
            SD<JSON,F17,JSONStr> sd17,
            Apply<? extends Tuple17<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18> JSONSD<T> object(
            Tuple18<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            SD<JSON,F15,JSONStr> sd15,
            SD<JSON,F16,JSONStr> sd16,
            SD<JSON,F17,JSONStr> sd17,
            SD<JSON,F18,JSONStr> sd18,
            Apply<? extends Tuple18<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19> JSONSD<T> object(
            Tuple19<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            SD<JSON,F15,JSONStr> sd15,
            SD<JSON,F16,JSONStr> sd16,
            SD<JSON,F17,JSONStr> sd17,
            SD<JSON,F18,JSONStr> sd18,
            SD<JSON,F19,JSONStr> sd19,
            Apply<? extends Tuple19<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, constructor));
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20> JSONSD<T> object(
            Tuple20<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>, ? extends MetaNamedMember<? super T, F20>> fields,
            SD<JSON,F1,JSONStr> sd1,
            SD<JSON,F2,JSONStr> sd2,
            SD<JSON,F3,JSONStr> sd3,
            SD<JSON,F4,JSONStr> sd4,
            SD<JSON,F5,JSONStr> sd5,
            SD<JSON,F6,JSONStr> sd6,
            SD<JSON,F7,JSONStr> sd7,
            SD<JSON,F8,JSONStr> sd8,
            SD<JSON,F9,JSONStr> sd9,
            SD<JSON,F10,JSONStr> sd10,
            SD<JSON,F11,JSONStr> sd11,
            SD<JSON,F12,JSONStr> sd12,
            SD<JSON,F13,JSONStr> sd13,
            SD<JSON,F14,JSONStr> sd14,
            SD<JSON,F15,JSONStr> sd15,
            SD<JSON,F16,JSONStr> sd16,
            SD<JSON,F17,JSONStr> sd17,
            SD<JSON,F18,JSONStr> sd18,
            SD<JSON,F19,JSONStr> sd19,
            SD<JSON,F20,JSONStr> sd20,
            Apply<? extends Tuple20<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19,? super F20>,T> constructor) {
        return new JSONSD<T>(JSONSerialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20),
                             JSONDeserialization.object(fields, sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sd11, sd12, sd13, sd14, sd15, sd16, sd17, sd18, sd19, sd20, constructor));
    }
}