package data.composition.factory.data;

import data.composition.factory.bean.CompositionKey;
import data.composition.factory.bean.CompositionValue;
import data.composition.factory.source.Source;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * @author guanguan
 */
public abstract class AbstractData<D, C> implements Data<D, C> {
    private List<Predicate<? super D>> predicates;

    public abstract List<Source<D, ?, ?, ?, ?>> getSourceList();

    @Override
    public Data<D, C> filter(Predicate<? super D> predicate) {
        if (Objects.isNull(predicates)) {
            predicates = new ArrayList<>();
        }
        if (Objects.nonNull(predicate)) {
            predicates.add(predicate);
        }
        return this;
    }


    public List<Predicate<? super D>> getPredicates() {
        return predicates;
    }

    @SuppressWarnings("unchecked")
    protected void execute(Map<String, Field> dataFiledNameMap, D data) {
        for (Source<D, ?, ?, ?, ?> source : getSourceList()) {
            Map<CompositionKey, Set<CompositionValue<?>>> compositionMap = (Map<CompositionKey, Set<CompositionValue<?>>>) source.getCompositionMap();
            compositionMap.forEach((BiConsumer<? super CompositionKey, ? super Set<? extends CompositionValue<?>>>) (compositionKey, compositionValues) -> {
                String dataKeyFieldName = compositionKey.getDataKeyFieldName();
                Object fieldValue = ReflectUtil.getFieldValue(dataFiledNameMap.get(dataKeyFieldName), data);
                for (CompositionValue<?> compositionValue : compositionValues) {
                    Map<Object, ?> valueGroupBy = compositionValue.getValueGroupBy();
                    Field sourceValuefield = source.getSourceFieldMap().get(compositionValue.getSourceValueFieldName());
                    Field dataValueField = dataFiledNameMap.get(compositionValue.getDataValueFieldName());
                    Object values = ReflectUtil.unfold(sourceValuefield, valueGroupBy.get(fieldValue), source.isMerge(), source.isDistinct());
                    if (Objects.isNull(values)) {
                        break;
                    }
                    ReflectUtil.setFieldValue(dataValueField, data, values);
                }
            });
        }
    }
}
