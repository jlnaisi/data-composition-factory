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
    @SuppressWarnings("unused")
    public static <D, S> Source<D, S, Collection<S>> create(Collection<S> source, Class<D> clazz) {
        return new CollectionSource<>(source);
    }

    @Override
    public boolean enabled() {
        return CollUtil.isNotEmpty(source);
    }

    @Override
    public SourceKeyMap<D, S, Collection<S>> key(FieldFunction<D, ?> dataField, FieldFunction<S, ?> sourceField) {
        return new CollectionSourceKeyMap<>(this, dataField, sourceField);
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
    public Map<Object, List<S>> createValueGroupBy(String sourceDataKeyFieldName) {
        return getSourceData().stream().filter(s -> Objects.nonNull(ReflectUtil.getFieldValue(getSourceFieldMap().get(sourceDataKeyFieldName), s))).collect(Collectors.groupingBy(s -> ReflectUtil.getFieldValueSafe(sourceFieldMap.get(sourceDataKeyFieldName), s, null)));
    }

    @Override
    public Map<CompositionKey, Set<CompositionValue<? extends S>>> getCompositionMap() {
        if (Objects.isNull(getSourceData())) {
            return Collections.emptyMap();
        }
        if (Objects.isNull(compositionMap)) {
            compositionMap = createCompositionMap();
        } else {
            return compositionMap;
        }
        return compositionMap;
    }

}
