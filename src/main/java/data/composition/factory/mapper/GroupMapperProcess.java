package data.composition.factory.mapper;

import data.composition.factory.function.ConvertFunction;
import data.composition.factory.pipeline.FactoryPipeline;

import java.util.Collection;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface GroupMapperProcess<T, R> extends FactoryPipeline<T> {
    /**
     * 转换
     *
     * @param convertFunction 转换方法
     * @return 工厂生产线
     */
    FactoryPipeline<T> convert(ConvertFunction<T, Collection<R>> convertFunction);
}
