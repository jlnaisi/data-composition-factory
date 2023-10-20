package data.composition.factory.key;

import data.composition.factory.bean.MappingKey;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.SingleMapperProcess;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public KeyMapUnitProcessImpl<T, R> dataField(FieldFunction<T, ?> dataFieldFunction) {
        this.dataFieldFunctions.add(dataFieldFunction);
        return this;
    }

    @Override
    public KeyMapUnitProcessImpl<T, R> sourceField(FieldFunction<R, ?> sourceFieldFunction) {
        this.sourceFieldFunctions.add(sourceFieldFunction);
        return this;
    }
    @Override
    public MappingKey<T, R> getMappingKey() {
        return MappingKey.create(dataFieldFunctions, sourceFieldFunctions);
    }

    @Override
    public SingleMapperProcess<T, R> paired() {
        return singleMapperProcess;
    }
}
