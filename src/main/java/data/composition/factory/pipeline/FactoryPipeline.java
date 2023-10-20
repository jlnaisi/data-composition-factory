package data.composition.factory.pipeline;

import data.composition.factory.key.MapKeyUnit;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 流水线
 *
 * @author zhangjinyu
 * @since 2023-10-19
 */
public interface FactoryPipeline<T> {
    /**
     * 过滤
     *
     * @param predicate 过滤谓词
     * @return 工厂生产线
     */
    FactoryPipeline<T> filter(Predicate<T> predicate);

    /**
     * 加载数据源
     *
     * @param source 资源
     * @param <S>    资源泛型
     * @return 映射key单元
     */
    <S> MapKeyUnit<T, S> source(Collection<S> source);

    /**
     * 加载数据源
     *
     * @param source    资源
     * @param predicate 过滤谓词
     * @param <S>       资源泛型
     * @return 映射key单元
     */
    <S> MapKeyUnit<T, S> source(Collection<S> source, Predicate<S> predicate);

    /**
     * 转换为
     *
     * @param clazz 转换结果对象类
     * @param <AR>  结果泛型
     * @return 工厂生产线
     */
    <AR> FactoryPipeline<AR> as(Class<AR> clazz);

    /**
     * 转换为
     *
     * @param clazz     转换结果对象类
     * @param converter 转换器
     * @param <AR>      结果泛型
     * @return 工厂生产线
     */
    <AR> FactoryPipeline<AR> as(Class<AR> clazz, Function<T, AR> converter);

    /**
     * 组装
     *
     * @return 结果
     */
    Collection<T> composition();

    /**
     * 组装
     *
     * @param clazz 转换为对象类
     * @param <R>   对象泛型
     * @return 结果
     */
    <R> List<R> compositionAs(Class<R> clazz);

    /**
     * 组装
     *
     * @param clazz     转换为对象类
     * @param converter 转换器
     * @param <R>       对象泛型
     * @return 结果
     */
    <R> List<R> compositionAs(Class<R> clazz, Function<T, R> converter);
}
