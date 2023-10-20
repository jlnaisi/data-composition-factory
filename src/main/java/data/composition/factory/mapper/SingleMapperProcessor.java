package data.composition.factory.mapper;

import data.composition.factory.function.ConvertFunction;
import data.composition.factory.function.FieldFunction;

import java.util.Map;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public interface SingleMapperProcessor<T, R> {
    boolean isAutoMap();
    Map<FieldFunction<T, ?>, FieldFunction<R, ?>> getFieldMapFunction();

    ConvertFunction<T, R> getConvertFunction();
}
