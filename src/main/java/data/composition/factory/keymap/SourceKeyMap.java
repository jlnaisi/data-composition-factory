package data.composition.factory.keymap;

import data.composition.factory.bean.ValueFieldMap;
import data.composition.factory.data.Data;
import data.composition.factory.source.Source;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface SourceKeyMap<D, S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> {
    /**
     * 数据赋值
     *
     * @param dataValueField       数据值字段,被数据源{@code sourceValueDataField}字段值赋值
     * @param sourceValueDataField 数据源值字段,赋值给{@code dataValueField}字段
     * @return 数据源对象 {@link Source}
     */
    SourceKeyMap<D, S, M, DF, VF> value(DF dataValueField, VF sourceValueDataField);
    SourceKeyMap<D, S, M, DF, VF> filter(Predicate<? super D> predicate);

    Predicate<? super D> getFilterPredicate();

    DF getDataKeyField();

    VF getSourceDataKeyField();

    List<ValueFieldMap<D, S, DF, VF>> getValueFieldMaps();

    Source<D, S, M, DF, VF> build();
}
