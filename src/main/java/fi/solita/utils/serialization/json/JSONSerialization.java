package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import fi.solita.utils.functional.Functional;
import fi.solita.utils.functional.FunctionalM;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Transformer;
import fi.solita.utils.serialization.Serializer;
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

    public static final <T> JSONSerializer<Iterable<T>> iter(final Serializer<JSON,? super T,JSONStr> serializer) {
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
}
