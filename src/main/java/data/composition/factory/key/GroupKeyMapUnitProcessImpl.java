package data.composition.factory.key;

import data.composition.factory.bean.MappingKey;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.GroupMapperProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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
    public GroupKeyMapUnitProcessImpl<T, R> dataField(FieldFunction<T, ?>... dataFieldFunction) {
        this.dataFieldFunctions.addAll(Arrays.asList(dataFieldFunction));
        return this;
    }

    @Override
    public GroupKeyMapUnitProcessImpl<T, R> sourceField(FieldFunction<R, ?>... sourceFieldFunction) {
        this.sourceFieldFunctions.addAll(Arrays.asList(sourceFieldFunction));
        return this;
    }

    private Consumer<MappingKey<T, R>> consumer;

    @Override
    public GroupMapperProcess<T, R> paired() {
        consumer.accept(MappingKey.create(dataFieldFunctions, sourceFieldFunctions));
        return groupMapperProcess;
    }

    @Override
    public void mappingKeyCreate(Consumer<MappingKey<T, R>> consumer) {
        this.consumer = consumer;
    }
}
