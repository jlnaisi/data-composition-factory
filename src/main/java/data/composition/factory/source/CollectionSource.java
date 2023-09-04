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
public class CollectionSource<D, S> implements Source<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> {
    Collection<S> source;
    private final List<SourceKeyMap<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>>> sourceKeyMapList;
    private Map<String, Field> sourceFieldMap;
    private Map<CompositionKey, Set<CompositionValue<? extends Collection<S>>>> compositionMap;
    private Predicate<S> predicate;
    private boolean predicateDone;

    private CollectionSource(Collection<S> source) {
        this.source = source;
        this.sourceKeyMapList = new ArrayList<>();
    }

    /**
     * 创建数据源对象
     *
     * @param source 数据源数据
     * @param clazz  数据类,用来泛型传递,使{@link #key(Function, Function)}和{@link SourceKeyMap#value(Function, Function)}的入参泛型能够正确识别
     * @param <D>    数据泛型
     * @param <S>    数据源泛型
     * @return 数据源对象 {@link Source}
     */
    @SuppressWarnings("unused")
    public static <D, S> Source<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> data(Collection<S> source, Class<D> clazz) {
        return new CollectionSource<>(source);
    }

    @Override
    public boolean enabled() {
        return CollUtil.isNotEmpty(source);
    }

    @Override
    public CollectionSourceKeyMap<D, S> key(FieldFunction<D, ?> dataField, FieldFunction<S, ?> sourceField) {
        return new CollectionSourceKeyMap<>(this, dataField, sourceField);
    }

    @Override
    public List<SourceKeyMap<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>>> getSourceKeyMapList() {
        return sourceKeyMapList;
    }

    @Override
    public Source<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> filter(Predicate<S> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public Collection<S> getSourceData() {
        if (Objects.nonNull(predicate) && !predicateDone) {
            predicateDone = true;
            source = source.stream().filter(predicate).toList();
        }
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
    public Map<Object, Collection<S>> createValueGroupBy(String sourceDataKeyFieldName) {
        Map<Object, ? extends Collection<S>> collect = getSourceData().stream().filter(s -> Objects.nonNull(ReflectUtil.getFieldValue(getSourceFieldMap().get(sourceDataKeyFieldName), s))).collect(Collectors.groupingBy(s -> ReflectUtil.getFieldValueSafe(sourceFieldMap.get(sourceDataKeyFieldName), s, null)));
        return (Map<Object, Collection<S>>) collect;
    }

    @Override
    public Map<CompositionKey, Set<CompositionValue<? extends Collection<S>>>> getCompositionMap() {
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
