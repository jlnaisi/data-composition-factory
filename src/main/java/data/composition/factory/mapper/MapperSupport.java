package data.composition.factory.mapper;

import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ReflectUtil;
import data.composition.factory.bean.MappingKey;
import data.composition.factory.function.ConvertFunction;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.process.SourceProcessor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public class MapperSupport<T> {
    private final SourceProcessor<T, ?> sourceProcessor;
    private final Collection<?> source;
    private final MappingKey mappingKey;
    private final List<String> sourceFieldNames;
    private Map<Object, Object> sourceKeyDataSingleMap;
    private Map<Object, ? extends Collection<?>> sourceKeyDataGroupMap;
    private Map<String, String> filedNameMap;
    private boolean autoMap;
    private Object dataKeyFieldValue;

    private MapperSupport(SourceProcessor<T, ?> sourceProcessor) {
        this.sourceProcessor = sourceProcessor;
        source = sourceProcessor.getSource();
        sourceFieldNames = sourceProcessor.getSourceFieldNames();
        mappingKey = sourceProcessor.getMappingKey();
        initSingleMap(sourceProcessor.getSingleMapper());
        initGroupMap(sourceProcessor.getGroupMapper());
    }


    public static <T> MapperSupport<T> create(SourceProcessor<T, ?> sourceProcessor) {
        return new MapperSupport<>(sourceProcessor);
    }

    private void initSingleMap(SingleMapperProcessor<T, ?> singleMapperProcessor) {
        if (Objects.isNull(singleMapperProcessor)) {
            return;
        }
        autoMap = singleMapperProcessor.isAutoMap();
        ConvertFunction<T, ?> convertFunction = singleMapperProcessor.getConvertFunction();
        Map<FieldFunction<T, ?>, ? extends FieldFunction<?, ?>> fieldMapFunction = singleMapperProcessor.getFieldMapFunction();
        if (Objects.isNull(convertFunction) && fieldMapFunction.isEmpty()) {
            filedNameMap = Collections.emptyMap();
        } else {
            filedNameMap = new HashMap<>(fieldMapFunction.size() + 1, 1);
            fieldMapFunction.forEach((BiConsumer<FieldFunction<T, ?>, FieldFunction<?, ?>>) (dataFieldFunction, sourceFieldFunction) -> filedNameMap.put(LambdaUtil.getFieldName(dataFieldFunction), LambdaUtil.getFieldName(sourceFieldFunction)));
        }
        sourceKeyDataSingleMap = source.stream().filter((Predicate<Object>) o -> Objects.nonNull(mappingKey.getSourceFieldValue(o))).collect(Collectors.toMap((Function<Object, Object>) mappingKey::getSourceFieldValue, v -> v));
    }

    private void initGroupMap(GroupMapperProcessor<T, ?> groupMapper) {
        if (Objects.isNull(groupMapper)) {
            return;
        }
        sourceKeyDataGroupMap = source.stream().filter((Predicate<Object>) o -> Objects.nonNull(mappingKey.getSourceFieldValue(o))).collect(Collectors.groupingBy((Function<Object, Object>) mappingKey::getSourceFieldValue));
    }

    public void composition(T data, List<String> dataFieldNames) {
        dataKeyFieldValue = mappingKey.getDataFieldValue(data);
        if (Objects.isNull(dataKeyFieldValue)) {
            return;
        }
        singleMap(data, dataFieldNames);
        groupMap(data);
    }

    @SuppressWarnings("unchecked")
    private void singleMap(T data, List<String> dataFieldNames) {
        SingleMapperProcessor<T, ?> singleMapper = sourceProcessor.getSingleMapper();
        if (Objects.isNull(singleMapper)) {
            return;
        }
        Object sourceValue = sourceKeyDataSingleMap.get(dataKeyFieldValue);
        if (Objects.nonNull(sourceValue)) {
            filedNameMap.forEach((dataFieldName, sourceFieldName) -> ReflectUtil.setFieldValue(data, dataFieldName, ReflectUtil.getFieldValue(sourceValue, sourceFieldName)));
            if (autoMap) {
                sourceFieldNames.removeAll(filedNameMap.keySet());
                sourceFieldNames.stream().filter(dataFieldNames::contains).forEach(fieldName -> ReflectUtil.setFieldValue(data, fieldName, ReflectUtil.getFieldValue(sourceValue, fieldName)));
            }
        }
        ConvertFunction<T, Object> convertFunction = (ConvertFunction<T, Object>) singleMapper.getConvertFunction();
        if (Objects.nonNull(convertFunction)) {
            convertFunction.apply(data, sourceValue);
        }
    }

    @SuppressWarnings("unchecked")
    private void groupMap(T data) {
        GroupMapperProcessor<T, ?> groupMapper = sourceProcessor.getGroupMapper();
        if (Objects.isNull(groupMapper)) {
            return;
        }
        ConvertFunction<T, Object> convertFunction = (ConvertFunction<T, Object>) groupMapper.getConvertFunction();
        if (Objects.nonNull(convertFunction)) {
            Collection<?> objects = sourceKeyDataGroupMap.get(dataKeyFieldValue);
            convertFunction.apply(data, objects);
        }
    }
}
