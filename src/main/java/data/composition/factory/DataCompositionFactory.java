package data.composition.factory;

import data.composition.factory.data.CollectionData;

import java.util.Collection;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface DataCompositionFactory {

//    static <K, V> MapData<K, V> data(Map<K, V> data) {
//        return new MapData<>(data);
//    }

    /**
     * 设置组装结果的数据
     *
     * @param data 组装结果数据,是未拼装数据源之前的数据
     * @param <D>  结果数据泛型
     * @return {@link CollectionData}
     */
    static <D> CollectionData<D> data(Collection<D> data) {
        return new CollectionData<>(data);
    }
}
