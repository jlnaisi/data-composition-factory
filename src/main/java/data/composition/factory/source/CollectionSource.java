package data.composition.factory.source;

import cn.hutool.core.collection.CollUtil;
import data.composition.factory.bean.CompositionKey;
import data.composition.factory.bean.CompositionValue;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.keymap.CollectionSourceKeyMap;
import data.composition.factory.keymap.SourceKeyMap;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public class CollectionSource<D, S> implements Source<D, S, Collection<S>> {
    final Collection<S> source;
    private final List<SourceKeyMap<D, S, Collection<S>>> sourceKeyMapList;
    private Map<String, Field> sourceFieldMap;
    private Map<CompositionKey, Set<CompositionValue<? extends S>>> compositionMap;

    private CollectionSource(Collection<S> source) {
        this.source = source;
        this.sourceKeyMapList = new ArrayList<>();
    }

    /**
     * 创建数据源对象
     *
     * @param source 数据源数据
     * @param clazz  数据类,用来泛型传递,使{@link #key(FieldFunction, FieldFunction)}和{@link SourceKeyMap#value(FieldFunction, FieldFunction)}的入参泛型能够正确识别
     * @param <D>    数据泛型
     * @param <S>    数据源泛型
     * @return 数据源对象 {@link Source}
     */
    public static <D, S> Source<D, S, Collection<S>> create(Collection<S> source, Class<D> clazz) {
        return new CollectionSource<>(source);
    }

    @Override
    public SourceKeyMap<D, S, Collection<S>> key(FieldFunction<D, ?> dataField, FieldFunction<S, ?> sourceField) {
        return new CollectionSourceKeyMap<>(this, dataField, sourceField);
    }

    @Override
    public void addSourceKeyMap(SourceKeyMap<D, S, Collection<S>> sourceKeyMap) {
        sourceKeyMapList.add(sourceKeyMap);
    }

    @Override
    public List<SourceKeyMap<D, S, Collection<S>>> getSourceKeyMapList() {
        return sourceKeyMapList;
    }

    @Override
    public Collection<S> getSourceData() {
        return source;
    }

    @Override
    public Map<String, Field> getSourceFieldMap() {
        if (Objects.isNull(sourceFieldMap)) {
            sourceFieldMap = ReflectUtil.getStreamCacheFields(CollUtil.getFirst(getSourceData()).getClass(), true, field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, v -> v));
        }
        return sourceFieldMap;
    }

    @Override
    public Map<CompositionKey, Set<CompositionValue<? extends S>>> getCompositionMap() {
        if (Objects.isNull(compositionMap)) {
            compositionMap = new HashMap<>();
        } else {
            return compositionMap;
        }
        if (Objects.isNull(getSourceData())) {
            return Collections.emptyMap();
        }
        List<? extends SourceKeyMap<D, ?, ? extends Collection<?>>> sourceKeyMapList = getSourceKeyMapList().stream().filter((Predicate<SourceKeyMap<D, ?, ? extends Collection<?>>>) dSourceKeyMap -> Objects.nonNull(dSourceKeyMap.getDataKeyField()) && Objects.nonNull(dSourceKeyMap.getSourceDataKeyField()) && Objects.nonNull(dSourceKeyMap.getDataValueField()) && Objects.nonNull(dSourceKeyMap.getSourceDataValueField())).toList();
        Collection<S> sourceData = getSourceData();
        if (CollUtil.isNotEmpty(sourceKeyMapList)) {
            for (SourceKeyMap<D, ?, ? extends Collection<?>> dSourceKeyMap : sourceKeyMapList) {
                Function<D, ?> dataKeyField = dSourceKeyMap.getDataKeyField();
                Function<?, ?> sourceDataKeyField = dSourceKeyMap.getSourceDataKeyField();
                Function<D, ?> dataValueField = dSourceKeyMap.getDataValueField();
                Function<?, ?> sourceDataValueField = dSourceKeyMap.getSourceDataValueField();
                String dataKeyFieldName = ReflectUtil.getFieldName(dataKeyField);
                String sourceDataKeyFieldName = ReflectUtil.getFieldName(sourceDataKeyField);
                String dataValueFieldName = ReflectUtil.getFieldName(dataValueField);
                String sourceDataValueFieldName = ReflectUtil.getFieldName(sourceDataValueField);
                Map<Object, List<S>> collect = sourceData.stream().filter(s -> Objects.nonNull(ReflectUtil.getFieldValue(getSourceFieldMap().get(sourceDataKeyFieldName), s))).collect(Collectors.groupingBy(s -> ReflectUtil.getFieldValueSafe(sourceFieldMap.get(sourceDataKeyFieldName), s, null)));
                CompositionKey compositionKey = new CompositionKey(dataKeyFieldName, sourceDataKeyFieldName);
                Set<CompositionValue<? extends S>> compositionValues = compositionMap.get(compositionKey);
                CompositionValue<S> compositionValue = new CompositionValue<>(dataValueFieldName, sourceDataValueFieldName, collect);
                if (Objects.isNull(compositionValues)) {
                    compositionValues = new HashSet<>();
                    compositionMap.put(compositionKey, compositionValues);
                }
                compositionValues.add(compositionValue);
            }
        }
        return compositionMap;
    }

}
