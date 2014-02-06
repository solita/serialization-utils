package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newArray;
import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.BiFunctor_;
import fi.solita.utils.functional.BiFunctors;
import fi.solita.utils.functional.Function;
import fi.solita.utils.functional.Functional;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Transformer;
import fi.solita.utils.functional.Tuple;
import fi.solita.utils.functional.Tuple2;
import fi.solita.utils.functional.Tuple3;
import fi.solita.utils.functional.Tuple4;
import fi.solita.utils.functional.Tuple5;
import fi.solita.utils.functional.Tuple6;
import fi.solita.utils.functional.Tuple7;
import fi.solita.utils.serialization.Util.Des;

public abstract class JSONDeserialization {

    public static final JSONDeserializer<String> string = new JSONDeserializer<String>() {
        @Override
        public String deserialize(JSON f, JSONStr v) {
            return f.toString(v);
        }
        @Override
        public String toString() {
            return "JSONDeserialization.string";
        };
    };

    public static final JSONDeserializer<Boolean> bool = new JSONDeserializer<Boolean>() {
        @Override
        public Boolean deserialize(JSON f, JSONStr v) {
            return f.toBoolean(v);
        }
        @Override
        public String toString() {
            return "JSONDeserialization.boolean";
        }
    };

    public static final JSONDeserializer<Integer> integer = new JSONDeserializer<Integer>() {
        @Override
        public Integer deserialize(JSON f, JSONStr v) {
            return f.toBigInteger(v).intValue();
        }
        @Override
        public String toString() {
            return "JSONDeserialization.integer";
        }
    };

    public static final JSONDeserializer<Long> lng = new JSONDeserializer<Long>() {
        @Override
        public Long deserialize(JSON f, JSONStr v) {
            return f.toBigInteger(v).longValue();
        }
        @Override
        public String toString() {
            return "JSONDeserialization.long";
        }
    };

    public static final JSONDeserializer<Short> shrt = new JSONDeserializer<Short>() {
        @Override
        public Short deserialize(JSON f, JSONStr v) {
            return f.toBigInteger(v).shortValue();
        }
        @Override
        public String toString() {
            return "JSONDeserialization.short";
        }
    };

    public static final JSONDeserializer<Float> flt = new JSONDeserializer<Float>() {
        @Override
        public Float deserialize(JSON f, JSONStr v) {
            return (float)f.toDouble(v);
        }
        @Override
        public String toString() {
            return "JSONDeserialization.float";
        }
    };

    public static final JSONDeserializer<Double> dbl = new JSONDeserializer<Double>() {
        @Override
        public Double deserialize(JSON f, JSONStr v) {
            return f.toDouble(v);
        }
        @Override
        public String toString() {
            return "JSONDeserialization.double";
        }
    };

    public static final JSONDeserializer<BigInteger> bigint = new JSONDeserializer<BigInteger>() {
        @Override
        public BigInteger deserialize(JSON f, JSONStr v) {
            return f.toBigInteger(v);
        }
        @Override
        public String toString() {
            return "JSONDeserialization.biginteger";
        }
    };

    public static final JSONDeserializer<BigDecimal> bigdecimal = new JSONDeserializer<BigDecimal>() {
        @Override
        public BigDecimal deserialize(JSON f, JSONStr v) {
            return f.toBigDecimal(v);
        }
        @Override
        public String toString() {
            return "JSONDeserialization.bigdecimal";
        }
    };

    public static final <T> JSONDeserializer<Option<T>> option(final JSONDeserializer<? extends T> s) {
        return new JSONDeserializer<Option<T>>() {
            @Override
            public Option<T> deserialize(JSON f, JSONStr v) {
                return Option.<T>of(v.toString().equals("null") ? null : s.deserialize(f, v));
            }
            @Override
            public String toString() {
                return "JSONDeserialization.option";
            }
        };
    }
    
    public static final <T> JSONDeserializer<T[]> array(final JSONDeserializer<? extends T> s, final Class<T> clazz) {
        return new JSONDeserializer<T[]>() {
            @Override
            public T[] deserialize(final JSON f, JSONStr v) {
                return newArray(clazz, Functional.map(f.toArray(v), new Transformer<JSONStr,T>() {
                    @Override
                    public T transform(JSONStr source) {
                        return s.deserialize(f, source);
                    }}));
            }
            @Override
            public String toString() {
                return "JSONDeserialization.array";
            }
        };
    }
    
    public static final <T> JSONDeserializer<List<T>> list(final JSONDeserializer<? extends T> s) {
        return new JSONDeserializer<List<T>>() {
            @Override
            public List<T> deserialize(final JSON f, JSONStr v) {
                return newList(Functional.map(f.toArray(v), new Transformer<JSONStr,T>() {
                    @Override
                    public T transform(JSONStr source) {
                        return s.deserialize(f, source);
                    }}));
            }
            @Override
            public String toString() {
                return "JSONDeserialization.list";
            }
        };
    }
    
    public static final <T> JSONDeserializer<Set<T>> set(final JSONDeserializer<? extends T> s) {
        return new JSONDeserializer<Set<T>>() {
            @Override
            public Set<T> deserialize(final JSON f, JSONStr v) {
                return newSet(Functional.map(f.toArray(v), new Transformer<JSONStr,T>() {
                    @Override
                    public T transform(JSONStr source) {
                        return s.deserialize(f, source);
                    }}));
            }
            @Override
            public String toString() {
                return "JSONDeserialization.set";
            }
        };
    }
    
    public static final <T> JSONDeserializer<Map<String, T>> map(final JSONDeserializer<? extends T> s) {
        return new JSONDeserializer<Map<String, T>>() {
            @Override
            public Map<String, T> deserialize(JSON f, JSONStr v) {
                Apply<Map.Entry<String, JSONStr>, Map.Entry<? extends String, ? extends T>> ff =
                        BiFunctor_.<Map.Entry<?, ?>, JSONStr, T, Map.Entry<String, JSONStr>, Map.Entry<? extends String, ? extends T>>
                        second().ap(BiFunctors.<String, String, JSONStr, T>entry(), JSONDeserialization_.<T>deserializeT().ap(f, s));
                return Functional.map(ff, json2map(f, v)); 
            }
            @Override
            public String toString() {
                return "JSONDeserialization.map";
            }
        };
    }
    
    static final <T> T deserializeT(JSON f, JSONDeserializer<? extends T> s, JSONStr t) {
        return s.deserialize(f, t);
    }
    
    public static final Map<String,JSONStr> json2map(JSON f, JSONStr json) {
        return f.toMap(json);
    }
    
    @SuppressWarnings("unchecked")
    public static final <O,T1> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Apply<? super T1,O> f) {
        return objectUnsafe(newList(p1), (Apply<Tuple,O>)(Object)Function.of(f).tuppled());
    }

    @SuppressWarnings("unchecked")
    public static final <O,T1,T2> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Des<? super O,T2,JSONStr> p2, Apply<? extends Tuple2<? super T1,? super T2>,O> f) {
        return objectUnsafe((Iterable<? extends Des<? super O,?,JSONStr>>)(Object)newList(p1, p2), (Apply<Tuple,O>)(Object)f);
    }

    @SuppressWarnings("unchecked")
    public static final <O,T1,T2,T3> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Des<? super O,T2,JSONStr> p2, Des<? super O,T3,JSONStr> p3, Apply<? extends Tuple3<? super T1,? super T2,? super T3>,O> f) {
        return objectUnsafe((Iterable<? extends Des<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3), (Apply<Tuple,O>)(Object)f);
    }

    @SuppressWarnings("unchecked")
    public static final <O,T1,T2,T3,T4> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Des<? super O,T2,JSONStr> p2, Des<? super O,T3,JSONStr> p3, Des<? super O,T4,JSONStr> p4, Apply<? extends Tuple4<? super T1,? super T2,? super T3,? super T4>,O> f) {
        return objectUnsafe((Iterable<? extends Des<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4), (Apply<Tuple,O>)(Object)f);
    }

    @SuppressWarnings("unchecked")
    public static final <O,T1,T2,T3,T4,T5> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Des<? super O,T2,JSONStr> p2, Des<? super O,T3,JSONStr> p3, Des<? super O,T4,JSONStr> p4, Des<? super O,T5,JSONStr> p5, Apply<? extends Tuple5<? super T1,? super T2,? super T3,? super T4,? super T5>,O> f) {
        return objectUnsafe((Iterable<? extends Des<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4, p5), (Apply<Tuple,O>)(Object)f);
    }

    @SuppressWarnings("unchecked")
    public static final <O,T1,T2,T3,T4,T5,T6> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Des<? super O,T2,JSONStr> p2, Des<? super O,T3,JSONStr> p3, Des<? super O,T4,JSONStr> p4, Des<? super O,T5,JSONStr> p5, Des<? super O,T6,JSONStr> p6, Apply<? extends Tuple6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6>,O> f) {
        return objectUnsafe((Iterable<? extends Des<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4, p5, p6), (Apply<Tuple,O>)(Object)f);
    }

    @SuppressWarnings("unchecked")
    public static final <O,T1,T2,T3,T4,T5,T6,T7> JSONDeserializer<O> object(Des<? super O,T1,JSONStr> p1, Des<? super O,T2,JSONStr> p2, Des<? super O,T3,JSONStr> p3, Des<? super O,T4,JSONStr> p4, Des<? super O,T5,JSONStr> p5, Des<? super O,T6,JSONStr> p6, Des<? super O,T7,JSONStr> p7, Apply<? extends Tuple7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7>,O> f) {
        return objectUnsafe((Iterable<? extends Des<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4, p5, p6, p7), (Apply<Tuple,O>)(Object)f);
    }
    
    public static final <O> JSONDeserializer<O> objectUnsafe(final Iterable<? extends Des<? super O,?,JSONStr>> entries, final Apply<Tuple,O> c) {
        return new JSONDeserializer<O>() {
            @Override
            public O deserialize(final JSON f, JSONStr v) {
                final Map<String,JSONStr> jsonObject = f.toMap(v);
                Tuple constructorArgs = Tuple.of(newArray(Object.class, Functional.map(entries, new Transformer<Des<? super O,?,JSONStr>, Object>() {
                    @Override
                    public Object transform(Des<? super O,?,JSONStr> source) {
                        return source.apply(f, jsonObject.get(source.getName()));
                    }
                })));
                return c.apply(constructorArgs);
            }
            @Override
            public String toString() {
                return "JSONDeserialization.object";
            }
        };
    }
}
