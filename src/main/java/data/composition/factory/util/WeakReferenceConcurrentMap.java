package data.composition.factory.util;


import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线程安全的ReferenceMap实现<br>
 * 参考：jdk.management.resource.internal.WeakKeyConcurrentHashMap
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
class WeakReferenceConcurrentMap<K, V> implements ConcurrentMap<K, V>, Iterable<Map.Entry<K, V>>, Serializable {

    final ConcurrentMap<Reference<K>, V> raw;
    private final ReferenceQueue<K> lastQueue;

    private BiConsumer<Reference<? extends K>, V> purgeListener;

    public WeakReferenceConcurrentMap(ConcurrentMap<Reference<K>, V> raw) {
        this.raw = raw;
        lastQueue = new ReferenceQueue<>();
    }

    public void setPurgeListener(BiConsumer<Reference<? extends K>, V> purgeListener) {
        this.purgeListener = purgeListener;
    }

    @Override
    public int size() {
        this.purgeStaleKeys();
        return this.raw.size();
    }

    @Override
    public boolean isEmpty() {
        return 0 == size();
    }

    @Override
    public V get(Object key) {
        this.purgeStaleKeys();
        return this.raw.get(ofKey((K) key, null));
    }

    @Override
    public boolean containsKey(Object key) {
        this.purgeStaleKeys();
        return this.raw.containsKey(ofKey((K) key, null));
    }

    @Override
    public boolean containsValue(Object value) {
        this.purgeStaleKeys();
        return this.raw.containsValue(value);
    }

    @Override
    public V put(K key, V value) {
        this.purgeStaleKeys();
        return this.raw.put(ofKey(key, this.lastQueue), value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        this.purgeStaleKeys();
        return this.raw.putIfAbsent(ofKey(key, this.lastQueue), value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public V replace(K key, V value) {
        this.purgeStaleKeys();
        return this.raw.replace(ofKey(key, this.lastQueue), value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        this.purgeStaleKeys();
        return this.raw.replace(ofKey(key, this.lastQueue), oldValue, newValue);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        this.purgeStaleKeys();
        this.raw.replaceAll((kWeakKey, value) -> function.apply(kWeakKey.get(), value));
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        this.purgeStaleKeys();
        return this.raw.computeIfAbsent(ofKey(key, this.lastQueue), kWeakKey -> mappingFunction.apply(key));
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        this.purgeStaleKeys();
        return this.raw.computeIfPresent(ofKey(key, this.lastQueue), (kWeakKey, value) -> remappingFunction.apply(key, value));
    }

    @Override
    public V remove(Object key) {
        this.purgeStaleKeys();
        return this.raw.remove(ofKey((K) key, null));
    }

    @Override
    public boolean remove(Object key, Object value) {
        this.purgeStaleKeys();
        return this.raw.remove(ofKey((K) key, null), value);
    }

    @Override
    public void clear() {
        this.raw.clear();
        while (lastQueue.poll() != null) ;
    }

    @Override
    public Set<K> keySet() {
        return this.raw.keySet().stream().map(kReference -> Objects.isNull(kReference) ? null : kReference.get()).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        this.purgeStaleKeys();
        return this.raw.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        this.purgeStaleKeys();
        return this.raw.entrySet().stream().map(entry -> new AbstractMap.SimpleImmutableEntry<>(entry.getKey().get(), entry.getValue())).collect(Collectors.toSet());
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        this.purgeStaleKeys();
        this.raw.forEach((key, value) -> action.accept(key.get(), value));
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entrySet().iterator();
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        this.purgeStaleKeys();
        return this.raw.compute(ofKey(key, this.lastQueue), (kWeakKey, value) -> remappingFunction.apply(key, value));
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        this.purgeStaleKeys();
        return this.raw.merge(ofKey(key, this.lastQueue), value, remappingFunction);
    }

    private void purgeStaleKeys() {
        Reference<? extends K> reference;
        V value;
        while ((reference = this.lastQueue.poll()) != null) {
            value = this.raw.remove(reference);
            if (null != purgeListener) {
                purgeListener.accept(reference, value);
            }
        }
    }

    private Reference<K> ofKey(K key, ReferenceQueue<? super K> queue) {
        return new WeakKey<>(key, queue);
    }

    /**
     * 弱键
     *
     * @param <K> 键类型
     */
    private static class WeakKey<K> extends WeakReference<K> {
        private final int hashCode;

        /**
         * 构造
         *
         * @param key   原始Key，不能为{@code null}
         * @param queue {@link ReferenceQueue}
         */
        WeakKey(K key, ReferenceQueue<? super K> queue) {
            super(key, queue);
            hashCode = key.hashCode();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (other instanceof WeakKey) {
                return Objects.equals(((WeakKey<?>) other).get(), get());
            }
            return false;
        }
    }

    /**
     * 弱键
     *
     * @param <K> 键类型
     */
    private static class SoftKey<K> extends SoftReference<K> {
        private final int hashCode;

        /**
         * 构造
         *
         * @param key   原始Key，不能为{@code null}
         * @param queue {@link ReferenceQueue}
         */
        SoftKey(K key, ReferenceQueue<? super K> queue) {
            super(key, queue);
            hashCode = key.hashCode();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (other instanceof SoftKey) {
                return Objects.equals(((SoftKey<?>) other).get(), get());
            }
            return false;
        }
    }
}
