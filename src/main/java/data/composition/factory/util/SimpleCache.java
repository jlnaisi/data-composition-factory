package data.composition.factory.util;


import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * @author ZhangJinyu
 * @since 2020-10-06
 */
class SimpleCache<K, V> implements Iterable<Object>, Serializable {
    private static final long serialVersionUID = 1L;
    protected final Map<K, Lock> keyLockMap;
    private final Map<K, V> rawMap;
    private final ReadWriteLock lock;

    public SimpleCache() {
        this(new WeakReferenceConcurrentMap<>(new ConcurrentHashMap<>()));
    }

    public SimpleCache(Map<K, V> initMap) {
        this.lock = new ReentrantReadWriteLock();
        this.keyLockMap = new ConcurrentHashMap<>();
        this.rawMap = initMap;
    }

    public V get(K key) {
        this.lock.readLock().lock();
        V value;
        try {
            value = this.rawMap.get(key);
        } finally {
            this.lock.readLock().unlock();
        }
        return value;
    }

    public V put(K key, V value) {
        this.lock.writeLock().lock();

        try {
            this.rawMap.put(key, value);
        } finally {
            this.lock.writeLock().unlock();
        }

        return value;
    }

    public V remove(Object key) {
        this.lock.writeLock().lock();
        V value;
        try {
            value = this.rawMap.remove(key);
        } finally {
            this.lock.writeLock().unlock();
        }
        return value;
    }

    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.rawMap.clear();
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public Iterator<Object> iterator() {
        return new MapIterator<>(this.rawMap.entrySet().iterator(), (entry) -> new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return entry.getKey();
            }

            @Override
            public V getValue() {
                return entry.getValue();
            }

            @Override
            public V setValue(V value) {
                return entry.setValue(value);
            }

        });
    }

    public static class MapIterator<F, T> implements Iterator<T> {

        private final Iterator<? extends F> backingIterator;
        private final Function<? super F, ? extends T> func;

        public MapIterator(final Iterator<? extends F> backingIterator, final Function<? super F, ? extends T> func) {
            this.backingIterator = backingIterator;
            this.func = func;
        }

        @Override
        public final boolean hasNext() {
            return backingIterator.hasNext();
        }

        @Override
        public final T next() {
            return func.apply(backingIterator.next());
        }

        @Override
        public final void remove() {
            backingIterator.remove();
        }


    }
}