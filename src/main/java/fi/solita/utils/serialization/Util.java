package fi.solita.utils.serialization;

import fi.solita.utils.codegen.MetaNamedMember;
import fi.solita.utils.codegen.MetaNamedMember_;
import fi.solita.utils.functional.Apply;
import fi.solita.utils.functional.Function;
import fi.solita.utils.functional.Function0;

public abstract class Util {
    private Util() {
    }
    
    public static final class Ser<O,T,S> {
        private Apply<O, T> f;
        private Function0<String> name;
        private Serializer<Object, ? super T, ? extends S> t;

        @SuppressWarnings("unchecked")
        Ser(MetaNamedMember<O, T> m, Serializer<?, ? super T, ? extends S> t) {
            this.f = m;
            this.name = MetaNamedMember_.getName.ap(m);
            this.t = (Serializer<Object, ? super T, ? extends S>) t;
        }

        @SuppressWarnings("unchecked")
        Ser(Function0<String> name, Apply<O, T> m, Serializer<?, ? super T, ? extends S> t) {
            this.f = m;
            this.name = name;
            this.t = (Serializer<Object, ? super T, ? extends S>) t;
        }
        
        public String getName() {
            return name.apply();
        }
        
        public S apply(Object ff, O v) {
            return t.serialize(ff, f.apply(v));
        }
    }
    
    public static final class Des<O,T,S> {
        private Function0<String> name;
        private Deserializer<Object,S, ? extends T> t;

        @SuppressWarnings("unchecked")
        Des(MetaNamedMember<O, T> m, Deserializer<?,S, ? extends T> t) {
            this.name = MetaNamedMember_.getName.ap(m);
            this.t = (Deserializer<Object, S, ? extends T>) t;
        }

        @SuppressWarnings("unchecked")
        Des(Function0<String> name, Deserializer<?, S, ? extends T> t) {
            this.name = name;
            this.t = (Deserializer<Object, S, ? extends T>) t;
        }
        
        public String getName() {
            return name.apply();
        }

        public T apply(Object f, S o) {
            return t.deserialize(f, o);
        }
    }
    
    public static final <O,T,S> Ser<O,T,S> pair(MetaNamedMember<O, T> m, Serializer<?, ? super T, ? extends S> s) {
        return new Ser<O,T,S>(m, s);
    }
    
    public static final <O,T,S> Ser<O,T,S> pair(Function0<String> name, Apply<O, T> m, Serializer<?, ? super T, ? extends S> s) {
        return new Ser<O,T,S>(name, m, s);
    }

    public static final <O,T,S> Ser<O,T,S> pair(String name, Apply<O, T> m, Serializer<?, ? super T, ? extends S> s) {
        return new Ser<O,T,S>(Function.of(name), m, s);
    }
    
    public static final <O,T,S> Des<? super O,T,S> pair(MetaNamedMember<O, T> m, Deserializer<?, S, ? extends T> s) {
        return new Des<O,T,S>(m, s);
    }
    
    public static final <O,T,S> Des<? super O,T,S> pair(Function0<String> name, Apply<O, T> m, Deserializer<?, S, ? extends T> s) {
        return new Des<O,T,S>(name, s);
    }

    public static final <O,T,S> Des<? super O,T,S> pair(String name, Deserializer<?, S, ? extends T> s) {
        return new Des<O,T,S>(Function.of(name), s);
    }
}
