package data.composition.factory.process;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import data.composition.factory.bean.MappingKey;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.key.GroupKeyMapUnitProcess;
import data.composition.factory.key.GroupKeyMapUnitProcessImpl;
import data.composition.factory.key.KeyMapUnitProcess;
import data.composition.factory.key.KeyMapUnitProcessImpl;
import data.composition.factory.mapper.*;
import data.composition.factory.pipeline.FactoryPipeline;
import data.composition.factory.pipeline.FactoryPipelineImpl;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 集合类型资源加工
 *
 * @author zhangjinyu
 * @since 2023-10-19
 */
public class SourceProcessImpl<T, R> implements SourceProcessor<T, R> {
    private final Collection<R> source;
    private final FactoryPipeline<T> factoryPipeline;
    private MappingKey<T, R> mappingKey;
    private SingleMapperProcessorImpl<T, R> singleMapper;
    private GroupSingleMapperProcessorImpl<T, R> groupMapper;

    public SourceProcessImpl(Collection<R> source, FactoryPipelineImpl<T> factoryPipeline) {
        this.source = source;
        this.factoryPipeline = factoryPipeline;
    }

    @Override
    public boolean enabled() {
        return Objects.nonNull(source) && !source.isEmpty() && (Objects.nonNull(singleMapper) || Objects.nonNull(groupMapper));
    }

    @Override
    public SingleMapperProcessorImpl<T, R> getSingleMapper() {
        return singleMapper;
    }

    @Override
    public GroupMapperProcessor<T, R> getGroupMapper() {
        return groupMapper;
    }

    @Override
    public Collection<R> getSource() {
        return source;
    }

    @Override
    public MappingKey<T, R> getMappingKey() {
        return mappingKey;
    }

    @Override
    public List<String> getSourceFieldNames() {
        if (Objects.isNull(source) || source.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(ReflectUtil.getFields(CollUtil.getFirst(source).getClass())).map(Field::getName).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public SingleMapperProcess<T, R> key(FieldFunction<T, ?> dataField, FieldFunction<R, ?> sourceField) {
        if (Objects.isNull(singleMapper)) {
            singleMapper = new SingleMapperProcessorImpl<>(factoryPipeline);
        }
        mappingKey = MappingKey.create(Collections.singletonList(dataField), Collections.singletonList(sourceField));
        return singleMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GroupMapperProcess<T, R> groupKey(FieldFunction<T, ?> dataField, FieldFunction<R, ?> sourceField) {
        if (Objects.isNull(groupMapper)) {
            groupMapper = new GroupSingleMapperProcessorImpl<>(factoryPipeline);
        }
        mappingKey = MappingKey.create(Collections.singletonList(dataField), Collections.singletonList(sourceField));
        return groupMapper;
    }

    @Override
    public KeyMapUnitProcess<T, R> multipleKey() {
        if (Objects.isNull(singleMapper)) {
            singleMapper = new SingleMapperProcessorImpl<>(factoryPipeline);
        }
        KeyMapUnitProcessImpl<T, R> keyMapUnitProcess = new KeyMapUnitProcessImpl<>(singleMapper);
        mappingKey = keyMapUnitProcess.getMappingKey();
        return keyMapUnitProcess;
    }

    @Override
    public GroupKeyMapUnitProcess<T, R> multipleGroupKey() {
        if (Objects.isNull(groupMapper)) {
            groupMapper = new GroupSingleMapperProcessorImpl<>(factoryPipeline);
        }
        GroupKeyMapUnitProcessImpl<T, R> keyMapUnitProcess = new GroupKeyMapUnitProcessImpl<>(groupMapper);
        mappingKey = keyMapUnitProcess.getMappingKey();
        return keyMapUnitProcess;
    }
}
