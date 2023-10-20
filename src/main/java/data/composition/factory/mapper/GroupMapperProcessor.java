package data.composition.factory.mapper;

import data.composition.factory.function.ConvertFunction;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public interface GroupMapperProcessor<T, R> {


    ConvertFunction<T, ?> getConvertFunction();
}
