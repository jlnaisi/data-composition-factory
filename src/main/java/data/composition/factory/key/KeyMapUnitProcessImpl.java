package data.composition.factory.key;

import data.composition.factory.bean.MappingKey;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.SingleMapperProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public class KeyMapUnitProcessImpl<T, R> implements KeyMapUnitProcess<T, R>, KeyMapUnitProcessor<T, R> {
    private final List<FieldFunction<T, ?>> dataFieldFunctions;
    private final List<FieldFunction<R, ?>> sourceFieldFunctions;
    private final SingleMapperProcess<T, R> singleMapperProcess;

    public KeyMapUnitProcessImpl(SingleMapperProcess<T, R> singleMapperProcess) {
        this.singleMapperProcess = singleMapperProcess;
        this.dataFieldFunctions = new ArrayList<>();
        this.sourceFieldFunctions = new ArrayList<>();
    }

    private Consumer<MappingKey<T, R>> consumer;

    @Override
    @SafeVarargs
    public final KeyMapUnitProcessImpl<T, R> dataField(FieldFunction<T, ?>... dataFieldFunction) {
        this.dataFieldFunctions.addAll(Arrays.asList(dataFieldFunction));
        return this;
    }

    @Override
    @SafeVarargs
    public final KeyMapUnitProcessImpl<T, R> sourceField(FieldFunction<R, ?>... sourceFieldFunction) {
        this.sourceFieldFunctions.addAll(Arrays.asList(sourceFieldFunction));
        return this;
    }

    @Override
    public void mappingKeyCreate(Consumer<MappingKey<T, R>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public SingleMapperProcess<T, R> paired() {
        consumer.accept(MappingKey.create(this.dataFieldFunctions, this.sourceFieldFunctions));
        return singleMapperProcess;
    }
}
