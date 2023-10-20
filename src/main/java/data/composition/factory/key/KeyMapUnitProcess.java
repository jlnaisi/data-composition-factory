package data.composition.factory.key;

import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.SingleMapperProcess;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface KeyMapUnitProcess<T,R> {
    KeyMapUnitProcessImpl<T, R> dataField(FieldFunction<T, ?> dataFieldFunction);

    KeyMapUnitProcessImpl<T, R> sourceField(FieldFunction<R, ?> sourceFieldFunction);

    SingleMapperProcess<T, R> paired();
}
