package data.composition.factory.data;

import cn.hutool.core.collection.CollUtil;
import data.composition.factory.source.Source;
import data.composition.factory.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合数据,处理数据拼装的类
 *
 * @author zhangjinyu
 * @since 2023-07-28
 */
public class CollectionData<D> extends AbstractData<D, Collection<D>> {
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
    public <S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> Data<D, Collection<D>> source(Source<D, S, M, DF, VF> source) {
        if (Objects.nonNull(source) && source.enabled()) {
            this.sourceList.add(source);
        }
        return this;
    }


    /**
     * 开始组装
     */
    @Override
    public void composition() {
        if (Objects.isNull(data) || CollUtil.isEmpty(sourceList)) {
            return;
        }
        Map<String, Field> dataFiledNameMap = ReflectUtil.getStreamCacheFields(CollUtil.getFirst(data).getClass(), true, field -> field.setAccessible(true)).collect(Collectors.toMap(Field::getName, v -> v));
        if (Objects.isNull(getPredicates())) {
            for (D datum : data) {
                execute(dataFiledNameMap, datum);
            }
        } else {
            Stream<D> stream = data.stream();
            for (Predicate<? super D> predicate : getPredicates()) {
                stream = stream.filter(predicate);
            }
            stream.forEach(data -> execute(dataFiledNameMap, data));
        }

    }

    @Override
    public List<Source<D, ?, ?, ?, ?>> getSourceList() {
        return sourceList;
    }
}
