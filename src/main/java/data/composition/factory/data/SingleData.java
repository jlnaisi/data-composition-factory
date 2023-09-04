package data.composition.factory.data;

import cn.hutool.core.collection.CollUtil;
import data.composition.factory.source.Source;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-08-09
 */
public class SingleData<D> extends AbstractData<D, D> {
    private final List<Source<D, ?, ?, ?, ?>> sourceList;
    private final D data;

    public SingleData(D data) {
        this.data = data;
        this.sourceList = new ArrayList<>();
    }

    public SingleData(List<Source<D, ?, ?, ?, ?>> sourceList, D data) {
        this.sourceList = sourceList;
        this.data = data;
    }


    @Override
    public <S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> Data<D, D> source(Source<D, S, M, DF, VF> source) {
        if (Objects.nonNull(source) && source.enabled()) {
            this.sourceList.add(source);
        }
        return this;
    }

    @Override
    public void composition() {
        if (Objects.isNull(data) || CollUtil.isEmpty(sourceList)) {
            return;
        }
        if (Objects.nonNull(getPredicates())) {
            for (Predicate<? super D> predicate : getPredicates()) {
                if (!predicate.test(data)) {
                    return;
                }
            }
        }
        Map<String, Field> dataFiledNameMap = ReflectUtil.getStreamCacheFields(data.getClass(), true, field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, v -> v));
        execute(dataFiledNameMap, data);
    }

    @Override
    public List<Source<D, ?, ?, ?, ?>> getSourceList() {
        return sourceList;
    }
}
