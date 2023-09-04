package data.composition.factory.source;

import data.composition.factory.bean.CompositionKey;
import data.composition.factory.bean.CompositionValue;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.keymap.SingleSourceKeyMap;
import data.composition.factory.keymap.SourceKeyMap;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-08-09
 */
public class SingleSource<D, S> implements Source<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> {
    private final List<SourceKeyMap<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>>> sourceKeyMapList;
    S source;
    private Map<String, Field> sourceFieldMap;
    private Map<CompositionKey, Set<CompositionValue<? extends S>>> compositionMap;
    private List<Predicate<S>> predicates;
    private boolean predicateDone;

    private SingleSource(S source) {
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
    public static <D, S> Source<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> data(S source, Class<D> clazz) {
        return new SingleSource<>(source);
    }

    @Override
    public boolean enabled() {
        return Objects.nonNull(source);
    }

    @Override
    public SourceKeyMap<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> key(FieldFunction<D, ?> dataField, FieldFunction<S, ?> sourceField) {
        return new SingleSourceKeyMap<>(this, dataField, sourceField);
    }


    @Override
    public List<SourceKeyMap<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>>> getSourceKeyMapList() {
        return sourceKeyMapList;
    }

    @Override
    public Source<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> filter(Predicate<S> predicate) {
        if (Objects.isNull(predicates)) {
            predicates = new ArrayList<>();
        }
        if (Objects.nonNull(predicate)) {
            this.predicates.add(predicate);
        }
        return this;
    }

    @Override
    public Optional<S> getSourceData() {
        if (Objects.nonNull(predicates) && !predicateDone) {
            predicateDone = true;
            for (Predicate<S> predicate : predicates) {
                if (!predicate.test(source)) {
                    source = null;
                    break;
                }
            }
        }
        return Optional.ofNullable(source);
    }

    @Override
    public Map<String, Field> getSourceFieldMap() {
        if (Objects.isNull(sourceFieldMap)) {
            Optional<S> sourceData = getSourceData();
            sourceFieldMap = sourceData.map(s -> ReflectUtil.getStreamCacheFields(s.getClass(), true, field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, v -> v))).orElse(Collections.emptyMap());
        }
        return sourceFieldMap;
    }

    @Override
    public Map<Object, S> createValueGroupBy(String sourceDataKeyFieldName) {
        if (getSourceData().isPresent()) {
            Map<Object, S> valueGroupBy = new HashMap<>(2, 1);
            valueGroupBy.put(ReflectUtil.getFieldValue(getSourceFieldMap().get(sourceDataKeyFieldName), getSourceData().get()), getSourceData().get());
            return valueGroupBy;
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<CompositionKey, Set<CompositionValue<? extends S>>> getCompositionMap() {
        if (Objects.isNull(compositionMap)) {
            compositionMap = createCompositionMap();
        } else {
            return compositionMap;
        }
        if (Objects.isNull(getSourceData())) {
            return Collections.emptyMap();
        }
        return compositionMap;
    }
}
