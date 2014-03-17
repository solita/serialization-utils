package fi.solita.utils.serialization.json;

import static fi.solita.utils.functional.Collections.newArray;
import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newSet;
import static fi.solita.utils.functional.Functional.concat;
import static fi.solita.utils.functional.Functional.forall;
import static fi.solita.utils.functional.FunctionalA.zip;
import static fi.solita.utils.functional.FunctionalImpl.cons;
import static fi.solita.utils.functional.FunctionalImpl.flatMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import fi.solita.utils.codegen.MetaNamedMember;
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
import fi.solita.utils.serialization.Util;
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
    
    public static final JSONDeserializer<Byte> byt = new JSONDeserializer<Byte>() {
        @Override
        public Byte deserializeOptimistic(JSON format, JSONStr json) {
            return format.toString(json).getBytes(Charset.forName("ISO-8859-1"))[0];
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

    public static final <T> JSONDeserializer<Option<T>> option(final Deserializer<JSON, ? extends T, JSONStr> deserializer) {
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
                    Either<Failure<T>, T> res = ((Deserializer<JSON, T, JSONStr>)deserializer).deserialize(format, json);
                    return res.bimap(failureMapper, Option_.<T>Some());
                }
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.option";
            }
        };
    }
    
    public static final <L,R> JSONDeserializer<Either<L,R>> either(final Deserializer<JSON, ? extends L, JSONStr> deserializerL, final Deserializer<JSON, ? extends R, JSONStr> deserializerR) {
        return new JSONDeserializer<Either<L,R>>() {
            @Override
            public Either<Failure<Either<L,R>>,Either<L,R>> deserialize(JSON format, JSONStr json) {
                Either<? extends Failure<? extends L>, ? extends L> resL = deserializerL.deserialize(format, json);
                if (resL.isRight()) {
                    return Either.right(Either.<L,R>left(resL.right.get()));
                }
                Either<? extends Failure<? extends R>, ? extends R> resR = deserializerR.deserialize(format, json);
                if (resR.isRight()) {
                    return Either.right(Either.<L,R>right(resR.right.get()));
                }
                
                L partialL = resL.left.get().partialResult;
                R partialR = resR.left.get().partialResult;
                Either<L,R> partial = partialL != null ? Either.<L,R>left(partialL) :
                                      partialR != null ? Either.<L,R>right(partialR) :
                                      null;
                return Either.left(Failure.of(partial, concat(resL.left.get().errors, resR.left.get().errors)));
            }
            
            @Override
            public String toString() {
                return "JSONDeserialization.either";
            }
        };
    }
    
    public static final <L,R> JSONDeserializer<Pair<L,R>> pair(final Deserializer<JSON, ? extends L, JSONStr> deserializerL, final Deserializer<JSON, ? extends R, JSONStr> deserializerR) {
        return new JSONDeserializer<Pair<L,R>>() {
            @Override
            public Either<fi.solita.utils.serialization.Deserializer.Failure<Pair<L, R>>, Pair<L, R>> deserialize(JSON format, JSONStr serial) {
                Either<Deserializer.Failure<Tuple2<L, R>>, Tuple2<L, R>> res = tuple(deserializerL, deserializerR).deserialize(format, serial);
                if (res.isLeft()) {
                    Deserializer.Failure<Tuple2<L, R>> f = res.left.get();
                    return Either.left(Deserializer.Failure.<Pair<L,R>>of(f.partialResult != null ? Pair.of(f.partialResult._1, f.partialResult._2) : null, f.errors));
                }
                return Either.right(Pair.of(res.right.get()._1, res.right.get()._2));
            }
        };
    }
    
    public static final <T> JSONDeserializer<T[]> array(final Deserializer<JSON, ? extends T, JSONStr> deserializer, final Class<T> clazz) {
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
    
    public static final <T> JSONDeserializer<List<T>> list(final Deserializer<JSON, ? extends T, JSONStr> deserializer) {
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
                        return ((Deserializer<JSON, T, JSONStr>)deserializer).deserialize(format, source);
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
    
    @SuppressWarnings("unchecked")
    public static final <T> JSONDeserializer<Collection<T>> collection(final Deserializer<JSON, ? extends T, JSONStr> deserializer) {
        return (JSONDeserializer<Collection<T>>)(Object)list(deserializer);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> JSONDeserializer<Iterable<T>> iterable(final Deserializer<JSON, ? extends T, JSONStr> deserializer) {
        return (JSONDeserializer<Iterable<T>>)(Object)list(deserializer);
    }
    
    public static final <T> JSONDeserializer<Set<T>> set(final Deserializer<JSON, ? extends T, JSONStr> deserializer) {
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
    
    public static final <T extends Comparable<T>> JSONDeserializer<SortedSet<T>> sortedSet(final Deserializer<JSON,T,JSONStr> deserializer) {
        final JSONDeserializer<Set<T>> des = JSONDeserialization.set(deserializer);
        return new JSONDeserializer<SortedSet<T>>() {
            @Override
            public Either<fi.solita.utils.serialization.Deserializer.Failure<SortedSet<T>>, SortedSet<T>> deserialize(JSON format, JSONStr serial) {
                Either<Deserializer.Failure<Set<T>>, Set<T>> res = des.deserialize(format, serial);
                if (res.isLeft()) {
                    Deserializer.Failure<Set<T>> f = res.left.get();
                    return Either.left(Deserializer.Failure.<SortedSet<T>>of(f.partialResult != null ? new TreeSet<T>(f.partialResult) : null, f.errors));
                }
                return Either.<Deserializer.Failure<SortedSet<T>>,SortedSet<T>>right(new TreeSet<T>(res.right.get()));
            }
        };
    }
    
    public static final <T> JSONDeserializer<Map<String, T>> map(final Deserializer<JSON, ? extends T, JSONStr> deserializer) {
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
                        return Pair.of(source.getKey(), ((Deserializer<JSON, T, JSONStr>)deserializer).deserialize(format, source.getValue()));
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
    
    static final Object deser(JSON format, Tuple2<JSONStr, ? extends Deserializer<JSON,? extends Object, JSONStr>> t) {
        return t._2.deserialize(format, t._1);
    }
    
    private static final <T extends Tuple> JSONDeserializer<T> tupleUnsafe(final Deserializer<JSON, ? extends Object, JSONStr>... deserializers) {
        return new JSONDeserializer<T>() {
            @SuppressWarnings({ "unchecked" })
            @Override
            protected T deserializeOptimistic(JSON format, JSONStr serial) {
                return (T)Tuple.of(newArray(Object.class, Functional.map(JSONDeserialization_.deser.ap(format), zip(JSONDeserialization.list(Util.<JSON,JSONStr>id()).deserialize(format, serial).right.get(), deserializers))));
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1> JSONDeserializer<Tuple1<T1>> tuple(Deserializer<JSON,? extends T1,JSONStr> deserializer1) {
        return tupleUnsafe(deserializer1);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2> JSONDeserializer<Tuple2<T1,T2>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2) {
        return tupleUnsafe(deserializer1, deserializer2);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3> JSONDeserializer<Tuple3<T1,T2,T3>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4> JSONDeserializer<Tuple4<T1,T2,T3,T4>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5> JSONDeserializer<Tuple5<T1,T2,T3,T4,T5>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6> JSONDeserializer<Tuple6<T1,T2,T3,T4,T5,T6>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7> JSONDeserializer<Tuple7<T1,T2,T3,T4,T5,T6,T7>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8> JSONDeserializer<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9> JSONDeserializer<Tuple9<T1,T2,T3,T4,T5,T6,T7,T8,T9>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> JSONDeserializer<Tuple10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> JSONDeserializer<Tuple11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> JSONDeserializer<Tuple12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> JSONDeserializer<Tuple13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> JSONDeserializer<Tuple14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> JSONDeserializer<Tuple15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> JSONDeserializer<Tuple16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17> JSONDeserializer<Tuple17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16,
            Deserializer<JSON,? extends T17,JSONStr> deserializer17) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16, deserializer17);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18> JSONDeserializer<Tuple18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16,
            Deserializer<JSON,? extends T17,JSONStr> deserializer17,
            Deserializer<JSON,? extends T18,JSONStr> deserializer18) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16, deserializer17, deserializer18);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19> JSONDeserializer<Tuple19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16,
            Deserializer<JSON,? extends T17,JSONStr> deserializer17,
            Deserializer<JSON,? extends T18,JSONStr> deserializer18,
            Deserializer<JSON,? extends T19,JSONStr> deserializer19) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16, deserializer17, deserializer18, deserializer19);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20> JSONDeserializer<Tuple20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16,
            Deserializer<JSON,? extends T17,JSONStr> deserializer17,
            Deserializer<JSON,? extends T18,JSONStr> deserializer18,
            Deserializer<JSON,? extends T19,JSONStr> deserializer19,
            Deserializer<JSON,? extends T20,JSONStr> deserializer20) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16, deserializer17, deserializer18, deserializer19, deserializer20);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21> JSONDeserializer<Tuple21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16,
            Deserializer<JSON,? extends T17,JSONStr> deserializer17,
            Deserializer<JSON,? extends T18,JSONStr> deserializer18,
            Deserializer<JSON,? extends T19,JSONStr> deserializer19,
            Deserializer<JSON,? extends T20,JSONStr> deserializer20,
            Deserializer<JSON,? extends T21,JSONStr> deserializer21) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16, deserializer17, deserializer18, deserializer19, deserializer20, deserializer21);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22> JSONDeserializer<Tuple22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22>> tuple(
            Deserializer<JSON,? extends T1,JSONStr> deserializer1,
            Deserializer<JSON,? extends T2,JSONStr> deserializer2,
            Deserializer<JSON,? extends T3,JSONStr> deserializer3,
            Deserializer<JSON,? extends T4,JSONStr> deserializer4,
            Deserializer<JSON,? extends T5,JSONStr> deserializer5,
            Deserializer<JSON,? extends T6,JSONStr> deserializer6,
            Deserializer<JSON,? extends T7,JSONStr> deserializer7,
            Deserializer<JSON,? extends T8,JSONStr> deserializer8,
            Deserializer<JSON,? extends T9,JSONStr> deserializer9,
            Deserializer<JSON,? extends T10,JSONStr> deserializer10,
            Deserializer<JSON,? extends T11,JSONStr> deserializer11,
            Deserializer<JSON,? extends T12,JSONStr> deserializer12,
            Deserializer<JSON,? extends T13,JSONStr> deserializer13,
            Deserializer<JSON,? extends T14,JSONStr> deserializer14,
            Deserializer<JSON,? extends T15,JSONStr> deserializer15,
            Deserializer<JSON,? extends T16,JSONStr> deserializer16,
            Deserializer<JSON,? extends T17,JSONStr> deserializer17,
            Deserializer<JSON,? extends T18,JSONStr> deserializer18,
            Deserializer<JSON,? extends T19,JSONStr> deserializer19,
            Deserializer<JSON,? extends T20,JSONStr> deserializer20,
            Deserializer<JSON,? extends T21,JSONStr> deserializer21,
            Deserializer<JSON,? extends T22,JSONStr> deserializer22) {
        return tupleUnsafe(deserializer1, deserializer2, deserializer3, deserializer4, deserializer5, deserializer6, deserializer7, deserializer8, deserializer9, deserializer10, deserializer11, deserializer12, deserializer13, deserializer14, deserializer15, deserializer16, deserializer17, deserializer18, deserializer19, deserializer20, deserializer21, deserializer22);
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
    
    public static final <T,F1> JSONDeserializer<T> object(
            MetaNamedMember<? super T, F1> field,
            Deserializer<JSON,F1,JSONStr> sd1,
            Apply<? super F1,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(field, sd1),
                               constructor);
    }
    
    public static final <T,F1> JSONDeserializer<T> object(
            Tuple1<? extends MetaNamedMember<? super T, F1>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Apply<? super F1,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               constructor);
    }
    
    public static final <T,F1,F2> JSONDeserializer<T> object(
            Tuple2<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Apply<? extends Tuple2<? super F1, ? super F2>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               constructor);
    }
    
    public static final <T,F1,F2,F3> JSONDeserializer<T> object(
            Tuple3<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Apply<? extends Tuple3<? super F1,? super F2,? super F3>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4> JSONDeserializer<T> object(
            Tuple4<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Apply<? extends Tuple4<? super F1,? super F2,? super F3,? super F4>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5> JSONDeserializer<T> object(
            Tuple5<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Apply<? extends Tuple5<? super F1,? super F2,? super F3,? super F4,? super F5>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6> JSONDeserializer<T> object(
            Tuple6<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Apply<? extends Tuple6<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7> JSONDeserializer<T> object(
            Tuple7<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Apply<? extends Tuple7<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8> JSONDeserializer<T> object(
            Tuple8<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Apply<? extends Tuple8<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9> JSONDeserializer<T> object(
            Tuple9<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Apply<? extends Tuple9<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10> JSONDeserializer<T> object(
            Tuple10<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Apply<? extends Tuple10<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11> JSONDeserializer<T> object(
            Tuple11<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Apply<? extends Tuple11<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12> JSONDeserializer<T> object(
            Tuple12<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Apply<? extends Tuple12<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13> JSONDeserializer<T> object(
            Tuple13<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Apply<? extends Tuple13<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14> JSONDeserializer<T> object(
            Tuple14<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Apply<? extends Tuple14<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15> JSONDeserializer<T> object(
            Tuple15<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Deserializer<JSON,F15,JSONStr> sd15,
            Apply<? extends Tuple15<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               Util.field(fields._15, sd15),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16> JSONDeserializer<T> object(
            Tuple16<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Deserializer<JSON,F15,JSONStr> sd15,
            Deserializer<JSON,F16,JSONStr> sd16,
            Apply<? extends Tuple16<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               Util.field(fields._15, sd15),
                               Util.field(fields._16, sd16),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17> JSONDeserializer<T> object(
            Tuple17<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Deserializer<JSON,F15,JSONStr> sd15,
            Deserializer<JSON,F16,JSONStr> sd16,
            Deserializer<JSON,F17,JSONStr> sd17,
            Apply<? extends Tuple17<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               Util.field(fields._15, sd15),
                               Util.field(fields._16, sd16),
                               Util.field(fields._17, sd17),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18> JSONDeserializer<T> object(
            Tuple18<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Deserializer<JSON,F15,JSONStr> sd15,
            Deserializer<JSON,F16,JSONStr> sd16,
            Deserializer<JSON,F17,JSONStr> sd17,
            Deserializer<JSON,F18,JSONStr> sd18,
            Apply<? extends Tuple18<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               Util.field(fields._15, sd15),
                               Util.field(fields._16, sd16),
                               Util.field(fields._17, sd17),
                               Util.field(fields._18, sd18),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19> JSONDeserializer<T> object(
            Tuple19<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Deserializer<JSON,F15,JSONStr> sd15,
            Deserializer<JSON,F16,JSONStr> sd16,
            Deserializer<JSON,F17,JSONStr> sd17,
            Deserializer<JSON,F18,JSONStr> sd18,
            Deserializer<JSON,F19,JSONStr> sd19,
            Apply<? extends Tuple19<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               Util.field(fields._15, sd15),
                               Util.field(fields._16, sd16),
                               Util.field(fields._17, sd17),
                               Util.field(fields._18, sd18),
                               Util.field(fields._19, sd19),
                               constructor);
    }
    
    public static final <T,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20> JSONDeserializer<T> object(
            Tuple20<? extends MetaNamedMember<? super T, F1>, ? extends MetaNamedMember<? super T, F2>, ? extends MetaNamedMember<? super T, F3>, ? extends MetaNamedMember<? super T, F4>, ? extends MetaNamedMember<? super T, F5>, ? extends MetaNamedMember<? super T, F6>, ? extends MetaNamedMember<? super T, F7>, ? extends MetaNamedMember<? super T, F8>, ? extends MetaNamedMember<? super T, F9>, ? extends MetaNamedMember<? super T, F10>, ? extends MetaNamedMember<? super T, F11>, ? extends MetaNamedMember<? super T, F12>, ? extends MetaNamedMember<? super T, F13>, ? extends MetaNamedMember<? super T, F14>, ? extends MetaNamedMember<? super T, F15>, ? extends MetaNamedMember<? super T, F16>, ? extends MetaNamedMember<? super T, F17>, ? extends MetaNamedMember<? super T, F18>, ? extends MetaNamedMember<? super T, F19>, ? extends MetaNamedMember<? super T, F20>> fields,
            Deserializer<JSON,F1,JSONStr> sd1,
            Deserializer<JSON,F2,JSONStr> sd2,
            Deserializer<JSON,F3,JSONStr> sd3,
            Deserializer<JSON,F4,JSONStr> sd4,
            Deserializer<JSON,F5,JSONStr> sd5,
            Deserializer<JSON,F6,JSONStr> sd6,
            Deserializer<JSON,F7,JSONStr> sd7,
            Deserializer<JSON,F8,JSONStr> sd8,
            Deserializer<JSON,F9,JSONStr> sd9,
            Deserializer<JSON,F10,JSONStr> sd10,
            Deserializer<JSON,F11,JSONStr> sd11,
            Deserializer<JSON,F12,JSONStr> sd12,
            Deserializer<JSON,F13,JSONStr> sd13,
            Deserializer<JSON,F14,JSONStr> sd14,
            Deserializer<JSON,F15,JSONStr> sd15,
            Deserializer<JSON,F16,JSONStr> sd16,
            Deserializer<JSON,F17,JSONStr> sd17,
            Deserializer<JSON,F18,JSONStr> sd18,
            Deserializer<JSON,F19,JSONStr> sd19,
            Deserializer<JSON,F20,JSONStr> sd20,
            Apply<? extends Tuple20<? super F1,? super F2,? super F3,? super F4,? super F5,? super F6,? super F7,? super F8,? super F9,? super F10,? super F11,? super F12,? super F13,? super F14,? super F15,? super F16,? super F17,? super F18,? super F19,? super F20>,T> constructor) {
        return JSONDeserialization.object(
                               Util.field(fields._1, sd1),
                               Util.field(fields._2, sd2),
                               Util.field(fields._3, sd3),
                               Util.field(fields._4, sd4),
                               Util.field(fields._5, sd5),
                               Util.field(fields._6, sd6),
                               Util.field(fields._7, sd7),
                               Util.field(fields._8, sd8),
                               Util.field(fields._9, sd9),
                               Util.field(fields._10, sd10),
                               Util.field(fields._11, sd11),
                               Util.field(fields._12, sd12),
                               Util.field(fields._13, sd13),
                               Util.field(fields._14, sd14),
                               Util.field(fields._15, sd15),
                               Util.field(fields._16, sd16),
                               Util.field(fields._17, sd17),
                               Util.field(fields._18, sd18),
                               Util.field(fields._19, sd19),
                               Util.field(fields._20, sd20),
                               constructor);
    }
}
