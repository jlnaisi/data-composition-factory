package data.composition.factory.mapper;

import data.composition.factory.function.ConvertFunction;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.pipeline.FactoryPipeline;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface SingleMapperProcess<T, R> extends FactoryPipeline<T> {
    /**
     * 映射字段
     *
     * @param dataField   数据字段
     * @param sourceField 数据源字段
     * @return 单映射器进程
     */
    SingleMapperProcess<T, R> map(FieldFunction<T, ?> dataField, FieldFunction<R, ?> sourceField);

    /**
     * 转换
     *
     * @param convertFunction 转换方法
     * @return 工厂生产线
     */
    FactoryPipeline<T> convert(ConvertFunction<T, R> convertFunction);

    /**
     * 自动映射,自动映射数据和数据源相同名称字段,除了{@link #map(FieldFunction, FieldFunction)}中设置的字段
     *
     * @return 工厂生产线
     */
    FactoryPipeline<T> autoMap();
}
