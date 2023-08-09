package data.composition.factory.data;

import data.composition.factory.source.Source;

/**
 * 数据</br>
 * 泛型:D 为数据类型,C为本类子类实现类型
 *
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface Data<D, C> {
    <S, M> Data<D, C> from(Source<D, S, M> source);

    void composition() throws IllegalAccessException;
}