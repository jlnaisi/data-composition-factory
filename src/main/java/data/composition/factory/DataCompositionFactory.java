package data.composition.factory;

import data.composition.factory.data.CollectionData;
import data.composition.factory.data.SingleData;

import java.util.Collection;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface DataCompositionFactory {

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

    /**
     * 设置组装结果的数据
     *
     * @param data 组装结果数据,是未拼装数据源之前的数据
     * @param <D>  结果数据泛型
     * @return {@link data.composition.factory.data.SingleData}
     */
    static <D> SingleData<D> data(D data) {
        return new SingleData<>(data);
    }

}
