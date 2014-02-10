package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newArray;
import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newSet;
import static fi.solita.utils.functional.Functional.forall;
import static fi.solita.utils.functional.FunctionalImpl.cons;
import static fi.solita.utils.functional.FunctionalImpl.flatMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Collections;
import fi.solita.utils.functional.Collections_;
import fi.solita.utils.functional.Either;
import fi.solita.utils.functional.Either_;
import fi.solita.utils.functional.Function;
import fi.solita.utils.functional.Functional;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Option_;
import fi.solita.utils.functional.Pair;
import fi.solita.utils.functional.Transformer;
import fi.solita.utils.functional.Tuple;
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
import fi.solita.utils.serialization.Util.FieldDeserializer;

public abstract class JSONDeserialization {

    public static final JSONDeserializer<String> string = new JSONDeserializer<String>() {
        @Override
        public String deserializeOptimistic(JSON format, JSONStr json) {
            return format.toString(json);
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.string";
        };
    };

    public static final JSONDeserializer<Boolean> bool = new JSONDeserializer<Boolean>() {
        @Override
        public Boolean deserializeOptimistic(JSON format, JSONStr json) {
            return format.toBoolean(json);
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.boolean";
        }
    };

    public static final JSONDeserializer<Integer> integer = new JSONDeserializer<Integer>() {
        @Override
        public Integer deserializeOptimistic(JSON format, JSONStr json) {
            return format.toBigInteger(json).intValue();
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.integer";
        }
    };

    public static final JSONDeserializer<Long> lng = new JSONDeserializer<Long>() {
        @Override
        public Long deserializeOptimistic(JSON format, JSONStr json) {
            return format.toBigInteger(json).longValue();
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.long";
        }
    };

    public static final JSONDeserializer<Short> shrt = new JSONDeserializer<Short>() {
        @Override
        public Short deserializeOptimistic(JSON format, JSONStr json) {
            return format.toBigInteger(json).shortValue();
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.short";
        }
    };

    public static final JSONDeserializer<Float> flt = new JSONDeserializer<Float>() {
        @Override
        public Float deserializeOptimistic(JSON format, JSONStr json) {
            return (float)format.toDouble(json);
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.float";
        }
    };

    public static final JSONDeserializer<Double> dbl = new JSONDeserializer<Double>() {
        @Override
        public Double deserializeOptimistic(JSON format, JSONStr json) {
            return format.toDouble(json);
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.double";
        }
    };

    public static final JSONDeserializer<BigInteger> bigint = new JSONDeserializer<BigInteger>() {
        @Override
        public BigInteger deserializeOptimistic(JSON format, JSONStr json) {
            return format.toBigInteger(json);
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.biginteger";
        }
    };

    public static final JSONDeserializer<BigDecimal> bigdecimal = new JSONDeserializer<BigDecimal>() {
        @Override
        public BigDecimal deserializeOptimistic(JSON format, JSONStr json) {
            return format.toBigDecimal(json);
        }
        
        @Override
        public String toString() {
            return "JSONDeserialization.bigdecimal";
        }
    };

    public static final <T> JSONDeserializer<Option<T>> option(final Deserializer<JSON, JSONStr, ? extends T> deserializer) {
        return new JSONDeserializer<Option<T>>() {
            private final Transformer<Failure<T>,Failure<Option<T>>> failureMapper = new Transformer<Failure<T>,Failure<Option<T>>>() {
                @Override
                public Failure<Option<T>> transform(Failure<T> source) {
                    return Failure.of(Option.of(source.partialResult), source.errors);
                }
            };
            
            @Override
            public Either<Failure<Option<T>>,Option<T>> deserialize(JSON format, JSONStr json) {
                if (json.toString().equals("null")) {
                    return Either.right(Option.<T>None());
                } else {
                    @SuppressWarnings("unchecked")
                    Either<Failure<T>, T> res = ((Deserializer<JSON, JSONStr, T>)deserializer).deserialize(format, json);
                    return res.bimap(failureMapper, Option_.<T>Some());
                }
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.option";
            }
        };
    }
    
    public static final <T> JSONDeserializer<T[]> array(final Deserializer<JSON, JSONStr, ? extends T> deserializer, final Class<T> clazz) {
        return new JSONDeserializer<T[]>() {
            private final Transformer<Failure<List<T>>,Failure<T[]>> failureMapper = new Transformer<Failure<List<T>>,Failure<T[]>>() {
                @Override
                public Failure<T[]> transform(Failure<List<T>> source) {
                    return Failure.of(newArray(clazz, source.partialResult), source.errors);
                }
            };
            
            @Override
            public Either<Failure<T[]>,T[]> deserialize(JSON format, JSONStr json) {
                return list(deserializer).deserialize(format, json).bimap(
                    failureMapper,
                    Collections_.<T>newArray16().ap(clazz));
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.array";
            }
        };
    }
    
    public static final <T> JSONDeserializer<List<T>> list(final Deserializer<JSON, JSONStr, ? extends T> deserializer) {
        return new JSONDeserializer<List<T>>() {
            private final Transformer<Either<Failure<T>,T>, T> getPartials = new Transformer<Either<Failure<T>,T>, T>() {
                @Override
                public T transform(Either<Failure<T>, T> source) {
                    return source.isRight() ? source.right.get() : source.left.get().partialResult;
                }
            };
            
            private final Transformer<Either<Failure<T>,T>, Iterable<Either<Exception,Object>>> getErrors = new Transformer<Either<Failure<T>,T>, Iterable<Either<Exception,Object>>>() {
                @Override
                public Iterable<Either<Exception,Object>> transform(Either<Failure<T>, T> source) {
                    return source.isRight() ? Collections.<Either<Exception,Object>>emptyList() : source.left.get().errors;
                }
            };
            
            @Override
            public Either<Failure<List<T>>,List<T>> deserialize(final JSON format, JSONStr json) {
                JSONStr[] jsonArray;
                try {
                    jsonArray = format.toArray(json);
                } catch (Exception e) {
                    return Either.left(Failure.<List<T>>of(null, "Error reading Array from: " + json));
                }
                
                List<Either<Failure<T>, T>> deserializedArray = newList(Functional.map(jsonArray, new Transformer<JSONStr,Either<Failure<T>,T>>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Either<Failure<T>,T> transform(JSONStr source) {
                        return ((Deserializer<JSON, JSONStr, T>)deserializer).deserialize(format, source);
                    }
                }));
                
                if (forall(Either_.isRight, deserializedArray)) {
                    return Either.right(newList(Functional.map(Either_.<T>right().andThen(Option_.<T>get()), deserializedArray)));
                } else {
                    Iterable<T> partials = Functional.map(deserializedArray, getPartials);
                    Iterable<Either<Exception,Object>> errors = flatMap(deserializedArray, getErrors);
                    return Either.left(Failure.of(newList(partials), errors));
                }
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.list";
            }
        };
    }
    
    public static final <T> JSONDeserializer<Set<T>> set(final Deserializer<JSON, JSONStr, ? extends T> deserializer) {
        return new JSONDeserializer<Set<T>>() {
            Transformer<Failure<List<T>>,Failure<Set<T>>> failureMapper = new Transformer<Failure<List<T>>,Failure<Set<T>>>() {
                @Override
                public Failure<Set<T>> transform(Failure<List<T>> source) {
                    return Failure.of(newSet(source.partialResult), source.errors);
                }
            };
            
            @Override
            public Either<Failure<Set<T>>,Set<T>> deserialize(final JSON format, JSONStr json) {
                return list(deserializer).deserialize(format, json).bimap(
                        failureMapper,
                        Collections_.<T>newSet10());
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.set";
            }
        };
    }
    
    public static final <T> JSONDeserializer<Map<String, T>> map(final Deserializer<JSON, JSONStr, ? extends T> deserializer) {
        return new JSONDeserializer<Map<String, T>>() {
            private final Transformer<Map.Entry<String, Either<Failure<T>,T>>, Map.Entry<String, T>> getPartials = new Transformer<Map.Entry<String, Either<Failure<T>,T>>, Map.Entry<String, T>>() {
                @Override
                public Map.Entry<String, T> transform(Entry<String, Either<Failure<T>, T>> source) {
                    Either<Failure<T>,T> val = source.getValue();
                    return Pair.of(source.getKey(), val.isRight() ? val.right.get() : val.left.get().partialResult);
                }
            };
            
            private final Transformer<Either<Failure<T>,T>, Iterable<Either<Exception,Object>>> getErrors = new Transformer<Either<Failure<T>,T>, Iterable<Either<Exception,Object>>>() {
                @Override
                public Iterable<Either<Exception,Object>> transform(Either<Failure<T>, T> source) {
                    return source.isRight() ? Collections.<Either<Exception,Object>>emptyList() : source.left.get().errors;
                }
            };
            
            private final Transformer<Map.Entry<String, Either<Failure<T>,T>>, Map.Entry<String, T>> successMapper = new Transformer<Map.Entry<String, Either<Failure<T>,T>>, Map.Entry<String, T>>() {
                @Override
                public Map.Entry<String, T> transform(Entry<String, Either<Failure<T>, T>> source) {
                    return Pair.of(source.getKey(), source.getValue().right.get());
                }
            };
            
            @Override
            public Either<Failure<Map<String,T>>,Map<String,T>> deserialize(final JSON format, JSONStr json) {
                final Map<String,JSONStr> jsonObject;
                try {
                    jsonObject = format.toMap(json);
                } catch (Exception e) {
                    return Either.left(Failure.<Map<String,T>>of(null, "Error reading Map from: " + json));
                }
                
                Map<String, Either<Failure<T>, T>> deserializedMap = Functional.map(jsonObject, new Transformer<Map.Entry<String,JSONStr>, Map.Entry<String, Either<Failure<T>,T>>>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Map.Entry<String, Either<Failure<T>,T>> transform(Map.Entry<String,JSONStr> source) {
                        return Pair.of(source.getKey(), ((Deserializer<JSON, JSONStr, T>)deserializer).deserialize(format, source.getValue()));
                    }
                });
                
                if (forall(Either_.isRight, deserializedMap.values())) {
                    return Either.right(Functional.map(deserializedMap, successMapper));
                } else {
                    Map<String,T> partials = Functional.map(deserializedMap, getPartials);
                    Iterable<Either<Exception,Object>> errors = flatMap(deserializedMap.values(), getErrors);
                    return Either.left(Failure.of(partials, errors));
                }
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.map";
            }
        };
    }
    
    public static final <T,F1> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            Apply<? super F1,T> constructor) {
        return objectUnsafe(newList(field1), Function.of(constructor).tuppled());
    }

    public static final <T,F1,F2> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            Apply<? extends Tuple2<? super F1,? super F2>,T> constructor) {
        return objectUnsafe(newList(field1, field2), constructor);
    }

    public static final <T,F1,F2,F3> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            Apply<? extends Tuple3<? super F1,? super F2,? super F3>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3), constructor);
    }

    public static final <T,F1,F2,F3,F4> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            Apply<? extends Tuple4<? super F1,? super F2,? super F3,? super F4>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4), constructor);
    }

    public static final <T,F1,F2,F3,F4,F5> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            Apply<? extends Tuple5<? super F1,? super F2,? super F3,? super F4,? super F5>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5), constructor);
    }

    public static final <T,F1,F2,F3,F4,F5,F6> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            Apply<? extends Tuple6<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6), constructor);
    }

    public static final <T,F1,F2,F3,F4,F5,F6,F7> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            Apply<? extends Tuple7<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            Apply<? extends Tuple8<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            Apply<? extends Tuple9<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            Apply<? extends Tuple10<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            Apply<? extends Tuple11<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            Apply<? extends Tuple12<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            Apply<? extends Tuple13<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            Apply<? extends Tuple14<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            Apply<? extends Tuple15<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            Apply<? extends Tuple16<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            FieldDeserializer<JSON, ? super T,F17,JSONStr> field17,
            Apply<? extends Tuple17<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            FieldDeserializer<JSON, ? super T,F17,JSONStr> field17,
            FieldDeserializer<JSON, ? super T,F18,JSONStr> field18,
            Apply<? extends Tuple18<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            FieldDeserializer<JSON, ? super T,F17,JSONStr> field17,
            FieldDeserializer<JSON, ? super T,F18,JSONStr> field18,
            FieldDeserializer<JSON, ? super T,F19,JSONStr> field19,
            Apply<? extends Tuple19<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19), constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            FieldDeserializer<JSON, ? super T,F17,JSONStr> field17,
            FieldDeserializer<JSON, ? super T,F18,JSONStr> field18,
            FieldDeserializer<JSON, ? super T,F19,JSONStr> field19,
            FieldDeserializer<JSON, ? super T,F20,JSONStr> field20,
            Apply<? extends Tuple20<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19,? super F20>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20), constructor);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,F21> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            FieldDeserializer<JSON, ? super T,F17,JSONStr> field17,
            FieldDeserializer<JSON, ? super T,F18,JSONStr> field18,
            FieldDeserializer<JSON, ? super T,F19,JSONStr> field19,
            FieldDeserializer<JSON, ? super T,F20,JSONStr> field20,
            FieldDeserializer<JSON, ? super T,F21,JSONStr> field21,
            Apply<? extends Tuple21<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19,? super F20,? super F21>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21), constructor);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,F21,F22> JSONDeserializer<T> object(
            FieldDeserializer<JSON, ? super T,F1,JSONStr> field1,
            FieldDeserializer<JSON, ? super T,F2,JSONStr> field2,
            FieldDeserializer<JSON, ? super T,F3,JSONStr> field3,
            FieldDeserializer<JSON, ? super T,F4,JSONStr> field4,
            FieldDeserializer<JSON, ? super T,F5,JSONStr> field5,
            FieldDeserializer<JSON, ? super T,F6,JSONStr> field6,
            FieldDeserializer<JSON, ? super T,F7,JSONStr> field7,
            FieldDeserializer<JSON, ? super T,F8,JSONStr> field8,
            FieldDeserializer<JSON, ? super T,F9,JSONStr> field9,
            FieldDeserializer<JSON, ? super T,F10,JSONStr> field10,
            FieldDeserializer<JSON, ? super T,F11,JSONStr> field11,
            FieldDeserializer<JSON, ? super T,F12,JSONStr> field12,
            FieldDeserializer<JSON, ? super T,F13,JSONStr> field13,
            FieldDeserializer<JSON, ? super T,F14,JSONStr> field14,
            FieldDeserializer<JSON, ? super T,F15,JSONStr> field15,
            FieldDeserializer<JSON, ? super T,F16,JSONStr> field16,
            FieldDeserializer<JSON, ? super T,F17,JSONStr> field17,
            FieldDeserializer<JSON, ? super T,F18,JSONStr> field18,
            FieldDeserializer<JSON, ? super T,F19,JSONStr> field19,
            FieldDeserializer<JSON, ? super T,F20,JSONStr> field20,
            FieldDeserializer<JSON, ? super T,F21,JSONStr> field21,
            FieldDeserializer<JSON, ? super T,F22,JSONStr> field22,
            Apply<? extends Tuple22<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19,? super F20,? super F21,? super F22>,T> constructor) {
        return objectUnsafe(newList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22), constructor);
    }
    
    public static final <T> JSONDeserializer<T> objectUnsafe(final Iterable<? extends FieldDeserializer<JSON,? extends Object,?,JSONStr>> fields, final Apply<? extends Tuple,T> constructor) {
        return new JSONDeserializer<T>() {
            private final Transformer<Either<? extends Failure<?>,Object>, Object> getPartials = new Transformer<Either<? extends Failure<?>,Object>, Object>() {
                @Override
                public Object transform(Either<? extends Failure<?>, Object> source) {
                    return source.isRight() ? source.right.get() : source.left.get().partialResult;
                }
            };
            
            private final Transformer<Either<? extends Failure<?>,Object>, Iterable<Either<Exception,Object>>> getErrors = new Transformer<Either<? extends Failure<?>,Object>, Iterable<Either<Exception,Object>>>() {
                @Override
                public Iterable<Either<Exception,Object>> transform(Either<? extends Failure<?>, Object> source) {
                    return source.isRight() ? Collections.<Either<Exception,Object>>emptyList() : source.left.get().errors;
                }
            };
            
            @Override
            public Either<Failure<T>,T> deserialize(final JSON format, JSONStr json) {
                @SuppressWarnings("unchecked")
                Apply<Tuple,T> constr = (Apply<Tuple,T>)constructor;
                
                final Map<String,JSONStr> jsonObject;
                try {
                    jsonObject = format.toMap(json);
                } catch (Exception e) {
                    return Either.left(Failure.<T>of(null, "Error reading Map from: " + json));
                }
                
                @SuppressWarnings("unchecked")
                List<Either<? extends Failure<?>, Object>> deserializedFields = newList(Functional.map((Iterable<? extends FieldDeserializer<JSON, ? super T,Object,JSONStr>>)fields, new Transformer<FieldDeserializer<JSON, ? super T,Object,JSONStr>, Either<? extends Failure<?>,Object>>() {
                    @Override
                    public Either<? extends Failure<?>, Object> transform(FieldDeserializer<JSON, ? super T,Object,JSONStr> source) {
                        JSONStr value = jsonObject.get(source.getName());
                        return value == null ? Either.left(Failure.of(null, "Key '" + source.getName() + "' not found from object: " + jsonObject))
                                             : source.apply(format, value);
                    }
                }));
                
                if (forall(Either_.isRight, deserializedFields)) {
                    Tuple constructorArgs = Tuple.of(newArray(Object.class, Functional.flatMap(Either_.right(), deserializedFields)));
                    try {
                        return Either.right(constr.apply(constructorArgs));
                    } catch (Exception e) {
                        return Either.left(Failure.<T>of(null, e));
                    }
                } else {
                    Iterable<Object> partials = Functional.map(deserializedFields, getPartials);
                    Iterable<Either<Exception,Object>> errors = flatMap(deserializedFields, getErrors);
                    Tuple constructorArgs = Tuple.of(newArray(Object.class, partials));
                    
                    T partialResult = null;
                    try {
                        partialResult = constr.apply(constructorArgs);
                    } catch (Exception e) {
                        errors = cons(Either.left(e), errors);
                    }
                    return Either.left(Failure.of(partialResult, errors));
                }
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.object";
            }
        };
    }
}
