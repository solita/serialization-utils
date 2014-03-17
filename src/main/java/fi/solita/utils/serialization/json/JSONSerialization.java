package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newMap;
import static fi.solita.utils.functional.FunctionalA.zip;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import fi.solita.utils.codegen.MetaNamedMember;
import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Functional;
import fi.solita.utils.functional.FunctionalM;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Transformer;
import fi.solita.utils.functional.Tuple;
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
import fi.solita.utils.serialization.Serializer;
import fi.solita.utils.serialization.Util;
import fi.solita.utils.serialization.Util.FieldSerializer;

public abstract class JSONSerialization {

    public static final JSONSerializer<String> string = new JSONSerializer<String>() {
        @Override
        public JSONStr serialize(JSON format, String object) {
            return format.toJSON(object);
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.string";
        }
    };

    public static final JSONSerializer<Boolean> bool = new JSONSerializer<Boolean>() {
        @Override
        public JSONStr serialize(JSON format, Boolean object) {
            return format.toJSON(object);
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.boolean";
        }
    };

    public static final JSONSerializer<Integer> integer = new JSONSerializer<Integer>() {
        @Override
        public JSONStr serialize(JSON format, Integer object) {
            return format.toJSON(BigInteger.valueOf(object));
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.integer";
        }
    };

    public static final JSONSerializer<Long> lng = new JSONSerializer<Long>() {
        @Override
        public JSONStr serialize(JSON format, Long object) {
            return format.toJSON(BigInteger.valueOf(object));
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.long";
        }
    };

    public static final JSONSerializer<Short> shrt = new JSONSerializer<Short>() {
        @Override
        public JSONStr serialize(JSON format, Short object) {
            return format.toJSON(BigInteger.valueOf(object));
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.short";
        }
    };

    public static final JSONSerializer<Float> flt = new JSONSerializer<Float>() {
        @Override
        public JSONStr serialize(JSON format, Float object) {
            return format.toJSON(object.doubleValue());
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.float";
        }
    };

    public static final JSONSerializer<Double> dbl = new JSONSerializer<Double>() {
        @Override
        public JSONStr serialize(JSON format, Double object) {
            return format.toJSON(object);
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.double";
        }
    };
    
    public static final JSONSerializer<Byte> byt = new JSONSerializer<Byte>() {
        @Override
        public JSONStr serialize(JSON format, Byte object) {
            return format.toJSON(new String(new byte[]{object}, Charset.forName("ISO-8859-1")));
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.byte";
        }
    };

    public static final JSONSerializer<BigInteger> bigint = new JSONSerializer<BigInteger>() {
        @Override
        public JSONStr serialize(JSON format, BigInteger object) {
            return format.toJSON(object);
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.biginteger";
        }
    };

    public static final JSONSerializer<BigDecimal> bigdecimal = new JSONSerializer<BigDecimal>() {
        @Override
        public JSONStr serialize(JSON format, BigDecimal object) {
            return format.toJSON(object);
        }
        
        @Override
        public String toString() {
            return "JSONSerialization.bigdecimal";
        }
    };

    public static final <T> JSONSerializer<Option<T>> option(final Serializer<JSON,? super T,JSONStr> serializer) {
        return new JSONSerializer<Option<T>>() {
            @Override
            public JSONStr serialize(JSON format, Option<T> object) {
                return object.isDefined() ? serializer.serialize(format, object.get()) : new JSONStr("null");
            }
            
            @Override
            public String toString() {
                return "JSONSerialization.option";
            }
        };
    }
    
    public static final <L,R> JSONSerializer<Either<L,R>> either(final Serializer<JSON,? super L,JSONStr> serializerL, final Serializer<JSON,? super R,JSONStr> serializerR) {
        return new JSONSerializer<Either<L,R>>() {
            @Override
            public JSONStr serialize(JSON format, Either<L,R> object) {
                return object.isLeft() ? serializerL.serialize(format, object.left.get()) : serializerR.serialize(format, object.right.get());
            }
            
            @Override
            public String toString() {
                return "JSONSerialization.either";
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public static final <L,R> JSONSerializer<Pair<L,R>> pair(Serializer<JSON,? super L,JSONStr> serializerL, Serializer<JSON,? super R,JSONStr> serializerR) {
        return (JSONSerializer<Pair<L,R>>)(Object)tuple(serializerL, serializerR);
    }

    public static final <T> JSONSerializer<T[]> array(final Serializer<JSON,? super T,JSONStr> serializer) {
        return new JSONSerializer<T[]>() {
            @Override
            public JSONStr serialize(final JSON format, T[] object) {
                return format.toJSON(Functional.map(object, new Transformer<T,JSONStr>() {
                    @Override
                    public JSONStr transform(T object) {
                        return serializer.serialize(format, object);
                    }
                }));
            }
            
            @Override
            public String toString() {
                return "JSONSerialization.array";
            }
        };
    }

    public static final <T> JSONSerializer<Iterable<T>> iterable(final Serializer<JSON,? super T,JSONStr> serializer) {
        return new JSONSerializer<Iterable<T>>() {
            @Override
            public JSONStr serialize(final JSON format, Iterable<T> objects) {
                return format.toJSON(Functional.map(objects, new Transformer<T,JSONStr>() {
                    @Override
                    public JSONStr transform(T object) {
                        return serializer.serialize(format, object);
                    }
                }));
            }
            
            @Override
            public String toString() {
                return "JSONSerialization.iter";
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> JSONSerializer<Collection<T>> collection(Serializer<JSON,? super T,JSONStr> serializer) {
        return (JSONSerializer<Collection<T>>)(Object)iterable(serializer);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> JSONSerializer<List<T>> list(Serializer<JSON,? super T,JSONStr> serializer) {
        return (JSONSerializer<List<T>>)(Object)iterable(serializer);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> JSONSerializer<Set<T>> set(Serializer<JSON,? super T,JSONStr> serializer) {
        return (JSONSerializer<Set<T>>)(Object)iterable(serializer);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T extends Comparable<T>> JSONSerializer<SortedSet<T>> sortedSet(Serializer<JSON,T,JSONStr> serializer) {
        return (JSONSerializer<SortedSet<T>>)(Object)JSONSerialization.set(serializer);
    }

    public static final <T> JSONSerializer<Map<String, T>> map(final Serializer<JSON,? super T,JSONStr> serializer) {
        return new JSONSerializer<Map<String, T>>() {
            @Override
            public JSONStr serialize(final JSON format, Map<String, T> objects) {
                return format.toJSON(FunctionalM.map(objects, new Transformer<Map.Entry<String,T>,Map.Entry<String,JSONStr>>() {
                    @Override
                    public Entry<String, JSONStr> transform(Entry<String, T> entry) {
                        return Pair.of(entry.getKey(), serializer.serialize(format, entry.getValue()));
                    }
                }));
            }
            
            @Override
            public String toString() {
                return "JSONSerialization.map";
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    static final JSONStr ser(JSON format, Tuple2<Object, ? extends Serializer<JSON,? extends Object,JSONStr>> t) {
        return ((Serializer<JSON,Object,JSONStr>)t._2).serialize(format, t._1);
    }
    
    private static final <T extends Tuple> JSONSerializer<T> tupleUnsafe(final Serializer<JSON,? extends Object,JSONStr>... serializers) {
        return new JSONSerializer<T>() {
            @Override
            public JSONStr serialize(JSON format, T object) {
                return JSONSerialization.iterable(Util.<JSON,JSONStr>id()).serialize(format, Functional.map(JSONSerialization_.ser.ap(format), zip(object.toArray(), serializers)));
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1> JSONSerializer<Tuple1<T1>> tuple(Serializer<JSON,? extends T1,JSONStr> serializer1) {
        return tupleUnsafe(serializer1);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2> JSONSerializer<Tuple2<T1,T2>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2) {
        return tupleUnsafe(serializer1, serializer2);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3> JSONSerializer<Tuple3<T1,T2,T3>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2, 
            Serializer<JSON,? extends T3,JSONStr> serializer3) {
        return tupleUnsafe(serializer1, serializer2, serializer3);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4> JSONSerializer<Tuple4<T1,T2,T3,T4>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5> JSONSerializer<Tuple5<T1,T2,T3,T4,T5>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6> JSONSerializer<Tuple6<T1,T2,T3,T4,T5,T6>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7> JSONSerializer<Tuple7<T1,T2,T3,T4,T5,T6,T7>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8> JSONSerializer<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9> JSONSerializer<Tuple9<T1,T2,T3,T4,T5,T6,T7,T8,T9>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> JSONSerializer<Tuple10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> JSONSerializer<Tuple11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> JSONSerializer<Tuple12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> JSONSerializer<Tuple13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> JSONSerializer<Tuple14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> JSONSerializer<Tuple15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> JSONSerializer<Tuple16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17> JSONSerializer<Tuple17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16,
            Serializer<JSON,? extends T17,JSONStr> serializer17) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16, serializer17);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18> JSONSerializer<Tuple18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16,
            Serializer<JSON,? extends T17,JSONStr> serializer17,
            Serializer<JSON,? extends T18,JSONStr> serializer18) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16, serializer17, serializer18);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19> JSONSerializer<Tuple19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16,
            Serializer<JSON,? extends T17,JSONStr> serializer17,
            Serializer<JSON,? extends T18,JSONStr> serializer18,
            Serializer<JSON,? extends T19,JSONStr> serializer19) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16, serializer17, serializer18, serializer19);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20> JSONSerializer<Tuple20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16,
            Serializer<JSON,? extends T17,JSONStr> serializer17,
            Serializer<JSON,? extends T18,JSONStr> serializer18,
            Serializer<JSON,? extends T19,JSONStr> serializer19,
            Serializer<JSON,? extends T20,JSONStr> serializer20) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16, serializer17, serializer18, serializer19, serializer20);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21> JSONSerializer<Tuple21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16,
            Serializer<JSON,? extends T17,JSONStr> serializer17,
            Serializer<JSON,? extends T18,JSONStr> serializer18,
            Serializer<JSON,? extends T19,JSONStr> serializer19,
            Serializer<JSON,? extends T20,JSONStr> serializer20,
            Serializer<JSON,? extends T21,JSONStr> serializer21) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16, serializer17, serializer18, serializer19, serializer20, serializer21);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22> JSONSerializer<Tuple22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22>> tuple(
            Serializer<JSON,? extends T1,JSONStr> serializer1,
            Serializer<JSON,? extends T2,JSONStr> serializer2,
            Serializer<JSON,? extends T3,JSONStr> serializer3,
            Serializer<JSON,? extends T4,JSONStr> serializer4,
            Serializer<JSON,? extends T5,JSONStr> serializer5,
            Serializer<JSON,? extends T6,JSONStr> serializer6,
            Serializer<JSON,? extends T7,JSONStr> serializer7,
            Serializer<JSON,? extends T8,JSONStr> serializer8,
            Serializer<JSON,? extends T9,JSONStr> serializer9,
            Serializer<JSON,? extends T10,JSONStr> serializer10,
            Serializer<JSON,? extends T11,JSONStr> serializer11,
            Serializer<JSON,? extends T12,JSONStr> serializer12,
            Serializer<JSON,? extends T13,JSONStr> serializer13,
            Serializer<JSON,? extends T14,JSONStr> serializer14,
            Serializer<JSON,? extends T15,JSONStr> serializer15,
            Serializer<JSON,? extends T16,JSONStr> serializer16,
            Serializer<JSON,? extends T17,JSONStr> serializer17,
            Serializer<JSON,? extends T18,JSONStr> serializer18,
            Serializer<JSON,? extends T19,JSONStr> serializer19,
            Serializer<JSON,? extends T20,JSONStr> serializer20,
            Serializer<JSON,? extends T21,JSONStr> serializer21,
            Serializer<JSON,? extends T22,JSONStr> serializer22) {
        return tupleUnsafe(serializer1, serializer2, serializer3, serializer4, serializer5, serializer6, serializer7, serializer8, serializer9, serializer10, serializer11, serializer12, serializer13, serializer14, serializer15, serializer16, serializer17, serializer18, serializer19, serializer20, serializer21, serializer22);
    }
    
    public static final <T> JSONSerializer<T> object(final FieldSerializer<JSON,? super T,?,JSONStr>... fields) {
        return new JSONSerializer<T>() {
            @Override
            public JSONStr serialize(final JSON format, final T object) {
                @SuppressWarnings("unchecked")
                Map<String, JSONStr> serializedFields = newMap(Functional.map((FieldSerializer<JSON,T,?,JSONStr>[])fields, new Transformer<FieldSerializer<JSON,T,?,JSONStr>, Map.Entry<String, JSONStr>>() {
                    @Override
                    public Map.Entry<String, JSONStr> transform(final FieldSerializer<JSON,T,?,JSONStr> source) {
                        JSONStr json = source.apply(format, object);
                        return Pair.of(source.getName(), json);
                    }
                }));
                return format.toJSON(serializedFields);
            }
            
            @Override
            public String toString() {
                return "JSONSerialization.object";
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1> JSONSerializer<T> object(
            MetaNamedMember<? super T, F1> field,
            Serializer<JSON,F1,JSONStr> serializer1) {
        return JSONSerialization.object(
                               Util.field(field, serializer1));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2> JSONSerializer<T> object(
            Tuple2<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3> JSONSerializer<T> object(
            Tuple3<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4> JSONSerializer<T> object(
            Tuple4<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5> JSONSerializer<T> object(
            Tuple5<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6> JSONSerializer<T> object(
            Tuple6<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7> JSONSerializer<T> object(
            Tuple7<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8> JSONSerializer<T> object(
            Tuple8<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9> JSONSerializer<T> object(
            Tuple9<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10> JSONSerializer<T> object(
            Tuple10<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11> JSONSerializer<T> object(
            Tuple11<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12> JSONSerializer<T> object(
            Tuple12<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13> JSONSerializer<T> object(
            Tuple13<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14> JSONSerializer<T> object(
            Tuple14<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15> JSONSerializer<T> object(
            Tuple15<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16> JSONSerializer<T> object(
            Tuple16<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15,
            Serializer<JSON,F16,JSONStr> serializer16) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15),
                               Util.field(fields._16, serializer16));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17> JSONSerializer<T> object(
            Tuple17<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15,
            Serializer<JSON,F16,JSONStr> serializer16,
            Serializer<JSON,F17,JSONStr> serializer17) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15),
                               Util.field(fields._16, serializer16),
                               Util.field(fields._17, serializer17));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18> JSONSerializer<T> object(
            Tuple18<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15,
            Serializer<JSON,F16,JSONStr> serializer16,
            Serializer<JSON,F17,JSONStr> serializer17,
            Serializer<JSON,F18,JSONStr> serializer18) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15),
                               Util.field(fields._16, serializer16),
                               Util.field(fields._17, serializer17),
                               Util.field(fields._18, serializer18));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19> JSONSerializer<T> object(
            Tuple19<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15,
            Serializer<JSON,F16,JSONStr> serializer16,
            Serializer<JSON,F17,JSONStr> serializer17,
            Serializer<JSON,F18,JSONStr> serializer18,
            Serializer<JSON,F19,JSONStr> serializer19) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15),
                               Util.field(fields._16, serializer16),
                               Util.field(fields._17, serializer17),
                               Util.field(fields._18, serializer18),
                               Util.field(fields._19, serializer19));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20> JSONSerializer<T> object(
            Tuple20<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>, ? extends MetaNamedMember<? super T, F20>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15,
            Serializer<JSON,F16,JSONStr> serializer16,
            Serializer<JSON,F17,JSONStr> serializer17,
            Serializer<JSON,F18,JSONStr> serializer18,
            Serializer<JSON,F19,JSONStr> serializer19,
            Serializer<JSON,F20,JSONStr> serializer20) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15),
                               Util.field(fields._16, serializer16),
                               Util.field(fields._17, serializer17),
                               Util.field(fields._18, serializer18),
                               Util.field(fields._19, serializer19),
                               Util.field(fields._20, serializer20));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,F21> JSONSerializer<T> object(
            Tuple21<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>, ? extends MetaNamedMember<? super T, F20>, ? extends MetaNamedMember<? super T, F21>> fields,
            Serializer<JSON,F1,JSONStr> serializer1,
            Serializer<JSON,F2,JSONStr> serializer2,
            Serializer<JSON,F3,JSONStr> serializer3,
            Serializer<JSON,F4,JSONStr> serializer4,
            Serializer<JSON,F5,JSONStr> serializer5,
            Serializer<JSON,F6,JSONStr> serializer6,
            Serializer<JSON,F7,JSONStr> serializer7,
            Serializer<JSON,F8,JSONStr> serializer8,
            Serializer<JSON,F9,JSONStr> serializer9,
            Serializer<JSON,F10,JSONStr> serializer10,
            Serializer<JSON,F11,JSONStr> serializer11,
            Serializer<JSON,F12,JSONStr> serializer12,
            Serializer<JSON,F13,JSONStr> serializer13,
            Serializer<JSON,F14,JSONStr> serializer14,
            Serializer<JSON,F15,JSONStr> serializer15,
            Serializer<JSON,F16,JSONStr> serializer16,
            Serializer<JSON,F17,JSONStr> serializer17,
            Serializer<JSON,F18,JSONStr> serializer18,
            Serializer<JSON,F19,JSONStr> serializer19,
            Serializer<JSON,F20,JSONStr> serializer20,
            Serializer<JSON,F21,JSONStr> serializer21) {
        return JSONSerialization.object(
                               Util.field(fields._1, serializer1),
                               Util.field(fields._2, serializer2),
                               Util.field(fields._3, serializer3),
                               Util.field(fields._4, serializer4),
                               Util.field(fields._5, serializer5),
                               Util.field(fields._6, serializer6),
                               Util.field(fields._7, serializer7),
                               Util.field(fields._8, serializer8),
                               Util.field(fields._9, serializer9),
                               Util.field(fields._10, serializer10),
                               Util.field(fields._11, serializer11),
                               Util.field(fields._12, serializer12),
                               Util.field(fields._13, serializer13),
                               Util.field(fields._14, serializer14),
                               Util.field(fields._15, serializer15),
                               Util.field(fields._16, serializer16),
                               Util.field(fields._17, serializer17),
                               Util.field(fields._18, serializer18),
                               Util.field(fields._19, serializer19),
                               Util.field(fields._20, serializer20),
                               Util.field(fields._21, serializer21));
    }
}
