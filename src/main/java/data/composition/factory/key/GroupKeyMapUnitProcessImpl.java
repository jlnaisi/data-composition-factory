package data.composition.factory.key;

import data.composition.factory.bean.MappingKey;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.GroupMapperProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public class GroupKeyMapUnitProcessImpl<T, R> implements GroupKeyMapUnitProcess<T, R>, GroupKeyMapUnitProcessor<T, R> {
    private final List<FieldFunction<T, ?>> dataFieldFunctions;
    private final List<FieldFunction<R, ?>> sourceFieldFunctions;
    private final GroupMapperProcess<T, R> groupMapperProcess;

    public GroupKeyMapUnitProcessImpl(GroupMapperProcess<T, R> groupMapperProcess) {
        this.groupMapperProcess = groupMapperProcess;
        this.dataFieldFunctions = new ArrayList<>();
        this.sourceFieldFunctions = new ArrayList<>();
    }

    @Override
    public GroupKeyMapUnitProcessImpl<T, R> dataField(FieldFunction<T, ?> dataFieldFunction) {
        this.dataFieldFunctions.add(dataFieldFunction);
        return this;
    }

    @Override
    public GroupKeyMapUnitProcessImpl<T, R> sourceField(FieldFunction<R, ?> sourceFieldFunction) {
        this.sourceFieldFunctions.add(sourceFieldFunction);
        return this;
    }

    @Override
    public MappingKey<T, R> getMappingKey() {
        return MappingKey.create(dataFieldFunctions, sourceFieldFunctions);
    }

    @Override
    public GroupMapperProcess<T, R> paired() {
        return groupMapperProcess;
    }
}
