package data.composition.factory.source;

import cn.hutool.core.collection.CollUtil;
import data.composition.factory.bean.CompositionKey;
import data.composition.factory.bean.CompositionValue;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.keymap.SourceKeyMap;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface Source<D, S, M> {
    boolean enabled();

    /**
     * 键,用来指定具有数据关系的键
     *
     * @param dataField   数据字段
     * @param sourceField 数据源字段
     * @return 数据源键映射对象 {@link SourceKeyMap}
     */
    SourceKeyMap<D, S, M> key(FieldFunction<D, ?> dataField, FieldFunction<S, ?> sourceField);

    List<SourceKeyMap<D, S, M>> getSourceKeyMapList();

    M getSourceData();

    Map<String, Field> getSourceFieldMap();

    default Map<CompositionKey, Set<CompositionValue<? extends S>>> createCompositionMap() {
        Map<CompositionKey, Set<CompositionValue<? extends S>>> compositionMap = new HashMap<>();
        List<SourceKeyMap<D, S, M>> sourceKeyMapList = getSourceKeyMapList().stream().filter(dssSourceKeyMap -> Objects.nonNull(dssSourceKeyMap.getDataKeyField()) && Objects.nonNull(dssSourceKeyMap.getDataValueField()) && Objects.nonNull(dssSourceKeyMap.getSourceDataKeyField()) && Objects.nonNull(dssSourceKeyMap.getSourceDataValueField())).toList();
        if (CollUtil.isNotEmpty(sourceKeyMapList)) {
            for (SourceKeyMap<D, S, M> dSourceKeyMap : sourceKeyMapList) {
                Function<D, ?> dataKeyField = dSourceKeyMap.getDataKeyField();
                Function<?, ?> sourceDataKeyField = dSourceKeyMap.getSourceDataKeyField();
                Function<D, ?> dataValueField = dSourceKeyMap.getDataValueField();
                Function<?, ?> sourceDataValueField = dSourceKeyMap.getSourceDataValueField();
                String dataKeyFieldName = ReflectUtil.getFieldName(dataKeyField);
                String sourceDataKeyFieldName = ReflectUtil.getFieldName(sourceDataKeyField);
                String dataValueFieldName = ReflectUtil.getFieldName(dataValueField);
                String sourceDataValueFieldName = ReflectUtil.getFieldName(sourceDataValueField);
                CompositionKey compositionKey = new CompositionKey(dataKeyFieldName, sourceDataKeyFieldName);
                Set<CompositionValue<? extends S>> compositionValues = compositionMap.get(compositionKey);
                CompositionValue<S> compositionValue = new CompositionValue<>(dataValueFieldName, sourceDataValueFieldName, createValueGroupBy(sourceDataKeyFieldName));
                if (Objects.isNull(compositionValues)) {
                    compositionValues = new HashSet<>();
                    compositionMap.put(compositionKey, compositionValues);
                }
                compositionValues.add(compositionValue);
            }
        }
        return compositionMap;
    }

    Map<Object, List<S>> createValueGroupBy(String sourceDataKeyFieldName);

    Map<CompositionKey, Set<CompositionValue<? extends S>>> getCompositionMap();
}
