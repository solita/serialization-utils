package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.BiFunctor_;
import fi.solita.utils.functional.BiFunctors;
import fi.solita.utils.functional.Functional;
import fi.solita.utils.functional.FunctionalM;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Transformer;
import static fi.solita.utils.serialization.Util.*;

public abstract class JSONSerialization {

    public static final JSONSerializer<String> string = new JSONSerializer<String>() {
        @Override
        public JSONStr serialize(JSON f, String v) {
            return f.toJSON(v);
        }
        @Override
        public String toString() {
            return "JSONSerialization.string";
        }
    };

    public static final JSONSerializer<Boolean> bool = new JSONSerializer<Boolean>() {
        @Override
        public JSONStr serialize(JSON f, Boolean v) {
            return f.toJSON(v);
        }
        @Override
        public String toString() {
            return "JSONSerialization.boolean";
        }
    };

    public static final JSONSerializer<Integer> integer = new JSONSerializer<Integer>() {
        @Override
        public JSONStr serialize(JSON f, Integer v) {
            return f.toJSON(BigInteger.valueOf(v));
        }
        @Override
        public String toString() {
            return "JSONSerialization.integer";
        }
    };

    public static final JSONSerializer<Long> lng = new JSONSerializer<Long>() {
        @Override
        public JSONStr serialize(JSON f, Long v) {
            return f.toJSON(BigInteger.valueOf(v));
        }
        @Override
        public String toString() {
            return "JSONSerialization.long";
        }
    };

    public static final JSONSerializer<Short> shrt = new JSONSerializer<Short>() {
        @Override
        public JSONStr serialize(JSON f, Short v) {
            return f.toJSON(BigInteger.valueOf(v));
        }
        @Override
        public String toString() {
            return "JSONSerialization.short";
        }
    };

    public static final JSONSerializer<Float> flt = new JSONSerializer<Float>() {
        @Override
        public JSONStr serialize(JSON f, Float v) {
            return f.toJSON(v.doubleValue());
        }
        @Override
        public String toString() {
            return "JSONSerialization.float";
        }
    };

    public static final JSONSerializer<Double> dbl = new JSONSerializer<Double>() {
        @Override
        public JSONStr serialize(JSON f, Double v) {
            return f.toJSON(v);
        }
        @Override
        public String toString() {
            return "JSONSerialization.double";
        }
    };

    public static final JSONSerializer<BigInteger> bigint = new JSONSerializer<BigInteger>() {
        @Override
        public JSONStr serialize(JSON f, BigInteger v) {
            return f.toJSON(v);
        }
        @Override
        public String toString() {
            return "JSONSerialization.biginteger";
        }
    };

    public static final JSONSerializer<BigDecimal> bigdecimal = new JSONSerializer<BigDecimal>() {
        @Override
        public JSONStr serialize(JSON f, BigDecimal v) {
            return f.toJSON(v);
        }
        @Override
        public String toString() {
            return "JSONSerialization.bigdecimal";
        }
    };

    public static final <T> JSONSerializer<Option<T>> option(final JSONSerializer<? super T> s) {
        return new JSONSerializer<Option<T>>() {
            @Override
            public JSONStr serialize(JSON f, Option<T> v) {
                return v.isDefined() ? s.serialize(f, v.get()) : new JSONStr("null");
            }
            @Override
            public String toString() {
                return "JSONSerialization.option";
            }
        };
    }

    public static final <T> JSONSerializer<T[]> array(final JSONSerializer<? super T> s) {
        return new JSONSerializer<T[]>() {
            @Override
            public JSONStr serialize(JSON f, T[] v) {
                return f.toJSON(newList(Functional.map(v, JSONSerialization_.<T> serializeT().ap(f, s))));
            }
            @Override
            public String toString() {
                return "JSONSerialization.array";
            }
        };
    }

    public static final <T> JSONSerializer<Iterable<T>> iter(final JSONSerializer<? super T> s) {
        return new JSONSerializer<Iterable<T>>() {
            @Override
            public JSONStr serialize(JSON f, Iterable<T> v) {
                List<JSONStr> strs = newList(Functional.map(v, JSONSerialization_.<T> serializeT().ap(f, s)));
                return f.toJSON(strs);
            }
            @Override
            public String toString() {
                return "JSONSerialization.iter";
            }
        };
    }

    public static final <T> JSONSerializer<Map<String, T>> map(final JSONSerializer<? super T> s) {
        return new JSONSerializer<Map<String, T>>() {
            @Override
            public JSONStr serialize(JSON f, Map<String, T> v) {
                Apply<Map.Entry<String, T>, Map.Entry<? extends String, ? extends JSONStr>> ff =
                        BiFunctor_.<Map.Entry<?, ?>, T, JSONStr, Map.Entry<String, T>, Map.Entry<? extends String, ? extends JSONStr>>
                        second().ap(BiFunctors.<String, String, T, JSONStr>entry(),
                                    JSONSerialization_.<T>serializeT().ap(f, s));
                return f.toJSON(FunctionalM.map(ff, v));
            }
            @Override
            public String toString() {
                return "JSONSerialization.map";
            }
        };
    }

    static final <T> JSONStr serializeT(JSON f, final JSONSerializer<? super T> s, final T t) {
        return s.serialize(f, t);
    }

    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1) {
        return object(newList(p1));
    }

    @SuppressWarnings("unchecked")
    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1, Ser<? super O,?,JSONStr> p2) {
        return object((Iterable<? extends Ser<? super O,?,JSONStr>>)(Object)newList(p1, p2));
    }

    @SuppressWarnings("unchecked")
    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1, Ser<? super O,?,JSONStr> p2, Ser<? super O,?,JSONStr> p3) {
        return object((Iterable<? extends Ser<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3));
    }

    @SuppressWarnings("unchecked")
    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1, Ser<? super O,?,JSONStr> p2, Ser<? super O,?,JSONStr> p3, Ser<? super O,?,JSONStr> p4) {
        return object((Iterable<? extends Ser<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4));
    }

    @SuppressWarnings("unchecked")
    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1, Ser<? super O,?,JSONStr> p2, Ser<? super O,?,JSONStr> p3, Ser<? super O,?,JSONStr> p4, Ser<? super O,?,JSONStr> p5) {
        return object((Iterable<? extends Ser<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4, p5));
    }

    @SuppressWarnings("unchecked")
    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1, Ser<? super O,?,JSONStr> p2, Ser<? super O,?,JSONStr> p3, Ser<? super O,?,JSONStr> p4, Ser<? super O,?,JSONStr> p5, Ser<? super O,?,JSONStr> p6) {
        return object((Iterable<? extends Ser<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4, p5, p6));
    }

    @SuppressWarnings("unchecked")
    public static final <O> JSONSerializer<O> object(Ser<? super O,?,JSONStr> p1, Ser<? super O,?,JSONStr> p2, Ser<? super O,?,JSONStr> p3, Ser<? super O,?,JSONStr> p4, Ser<? super O,?,JSONStr> p5, Ser<? super O,?,JSONStr> p6, Ser<? super O,?,JSONStr> p7) {
        return object((Iterable<? extends Ser<? super O,?,JSONStr>>)(Object)newList(p1, p2, p3, p4, p5, p6, p7));
    }

    public static final <O> JSONSerializer<O> object(final Iterable<? extends Ser<? super O,?,JSONStr>> entries) {
        return new JSONSerializer<O>() {
            @Override
            public JSONStr serialize(final JSON f, final O v) {
                Map<String, JSONStr> m = newMap(Functional.map(entries, new Transformer<Ser<? super O,?,JSONStr>, Map.Entry<String, JSONStr>>() {
                    @Override
                    public Map.Entry<String, JSONStr> transform(final Ser<? super O,?,JSONStr> source) {
                        JSONStr str = source.apply(f, v);
                        return Pair.of(source.getName(), str);
                    }
                }));
                return f.toJSON(m);
            }
            @Override
            public String toString() {
                return "JSONSerialization.object";
            }
        };
    }
}
