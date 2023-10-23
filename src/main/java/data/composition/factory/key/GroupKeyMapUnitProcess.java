package data.composition.factory.key;

import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.GroupMapperProcess;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface GroupKeyMapUnitProcess<T, R> {
    GroupKeyMapUnitProcessImpl<T, R> dataField(FieldFunction<T, ?>... dataFieldFunction);

    GroupKeyMapUnitProcessImpl<T, R> sourceField(FieldFunction<R, ?>... sourceFieldFunction);

    GroupMapperProcess<T, R> paired();
}
