package data.composition.factory.data;

import cn.hutool.core.collection.CollUtil;
import data.composition.factory.bean.CompositionKey;
import data.composition.factory.bean.CompositionValue;
import data.composition.factory.source.Source;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合数据,处理数据拼装的类
 *
 * @author zhangjinyu
 * @since 2023-07-28
 */
public class CollectionData<D> implements Data<D, Collection<D>> {
    private final List<Source<D, ?, ?, ?, ?>> sourceList;
    private final Collection<D> data;

    public CollectionData(Collection<D> data) {
        this.data = data;
        this.sourceList = new ArrayList<>();
    }

    /**
     * 组装数据的数据源
     *
     * @param source 数据源
     * @param <S>    数据源泛型
     * @param <M>    数据源对象泛型
     * @return 集合数据对象 {@link CollectionData}
     */
    @Override
    public <S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> Data<D, Collection<D>> from(Source<D, S, M, DF, VF> source) {
        if (Objects.nonNull(source) && source.enabled()) {
            this.sourceList.add(source);
        }
        return this;
    }

    /**
     * 开始组装
     */
    @Override
    @SuppressWarnings("unchecked")
    public void composition() {
        if (Objects.isNull(data)) {
            return;
        }
        if (CollUtil.isEmpty(sourceList)) {
            return;
        }
        Map<String, Field> dataFiledNameMap = ReflectUtil.getStreamCacheFields(CollUtil.getFirst(data).getClass(), true, field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, v -> v));
        for (D datum : data) {
            for (Source<D, ?, ?, ?, ?> source : sourceList) {
                Map<CompositionKey, Set<CompositionValue<?>>> compositionMap = (Map<CompositionKey, Set<CompositionValue<?>>>) source.getCompositionMap();
                compositionMap.forEach((BiConsumer<? super CompositionKey, ? super Set<? extends CompositionValue<?>>>) (compositionKey, compositionValues) -> {
                    String dataKeyFieldName = compositionKey.getDataKeyFieldName();
                    Object fieldValue = ReflectUtil.getFieldValue(dataFiledNameMap.get(dataKeyFieldName), datum);
                    for (CompositionValue<?> compositionValue : compositionValues) {
                        Map<Object, ?> valueGroupBy = compositionValue.getValueGroupBy();
                        Field sourceValuefield = source.getSourceFieldMap().get(compositionValue.getSourceValueFieldName());
                        Field dataValueField = dataFiledNameMap.get(compositionValue.getDataValueFieldName());
                        List<?> values = ReflectUtil.unfold(sourceValuefield, valueGroupBy.get(fieldValue));
                        if (Objects.isNull(values)) {
                            break;
                        }
                        ReflectUtil.setFieldValue(dataValueField, datum, values);
                    }
                });
            }
        }
    }
}
