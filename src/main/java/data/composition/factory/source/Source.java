package data.composition.factory.source;

import data.composition.factory.bean.CompositionKey;
import data.composition.factory.bean.CompositionValue;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.keymap.SourceKeyMap;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface Source<D, S, M> {

    /**
     * 键,用来指定具有数据关系的键
     *
     * @param dataField   数据字段
     * @param sourceField 数据源字段
     * @return 数据源键映射对象 {@link SourceKeyMap}
     */
    SourceKeyMap<D, S, M> key(FieldFunction<D, ?> dataField, FieldFunction<S, ?> sourceField);

    void addSourceKeyMap(SourceKeyMap<D, S, M> sourceKeyMap);

    List<SourceKeyMap<D, S, Collection<S>>> getSourceKeyMapList();

    M getSourceData();

    Map<String, Field> getSourceFieldMap();

    Map<CompositionKey, Set<CompositionValue<? extends S>>> getCompositionMap();
}
