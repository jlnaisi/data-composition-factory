package data.composition.factory.source;

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
import java.util.stream.Stream;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public class CollectionSource<D, S> extends AbstractSource<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> {
    private final List<SourceKeyMap<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>>> sourceKeyMapList;
    Collection<S> source;
    private Map<String, Field> sourceFieldMap;
    private Map<CompositionKey, Set<CompositionValue<? extends Collection<S>>>> compositionMap;
    private List<Predicate<S>> predicates;
    private boolean predicateDone;

    private CollectionSource(Collection<S> source) {
        this.source = source;
        this.sourceKeyMapList = new ArrayList<>();
    }

    /**
     * 创建数据源对象
     *
     * @param source 数据源数据
     * @param clazz  数据类,用来泛型传递,使{@link #key(FieldFunction, FieldFunction)}和{@link SourceKeyMap#value(Function, Function)}的入参泛型能够正确识别
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
        return Objects.nonNull(source) && !source.isEmpty();
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
        if (Objects.isNull(predicates)) {
            predicates = new ArrayList<>();
        }
        if (Objects.nonNull(predicate)) {
            this.predicates.add(predicate);
        }
        return this;
    }

    @Override
    public Optional<Collection<S>> getSourceData() {
        if (Objects.nonNull(predicates) && !predicateDone) {
            predicateDone = true;
            Stream<S> stream = source.stream();
            for (Predicate<S> predicate : predicates) {
                stream = stream.filter(predicate);
            }
            source = stream.collect(Collectors.toList());
        }
        if (source.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(source);
    }

    @Override
    public Map<String, Field> getSourceFieldMap() {
        if (Objects.isNull(sourceFieldMap)) {
            Optional<Collection<S>> sourceData = getSourceData();
            sourceFieldMap = sourceData.map(s -> ReflectUtil.getStreamCacheFields(s.iterator().next().getClass(), true, field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, v -> v))).orElse(Collections.emptyMap());
        }
        return sourceFieldMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Collection<S>> createValueGroupBy(String sourceDataKeyFieldName) {
        if (getSourceData().isPresent()) {
            Map<Object, ? extends Collection<S>> collect = getSourceData().get().stream().filter(s -> Objects.nonNull(ReflectUtil.getFieldValue(getSourceFieldMap().get(sourceDataKeyFieldName), s))).collect(Collectors.groupingBy(s -> ReflectUtil.getFieldValueSafe(sourceFieldMap.get(sourceDataKeyFieldName), s, null)));
            return (Map<Object, Collection<S>>) collect;
        } else {
            return Collections.emptyMap();
        }
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
