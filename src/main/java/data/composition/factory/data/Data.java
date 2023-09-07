package data.composition.factory.data;

import data.composition.factory.source.Source;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 数据</br>
 * 泛型:D 为数据类型,C为本类子类实现类型
 *
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface Data<D, C> {
    Data<D, C> filter(Predicate<? super D> predicate);
    <S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> Data<D, C> source(Source<D, S, M, DF, VF> source);

    void composition();
}
